import groovy.json.JsonSlurper

class KnowledgeHelper {

	public static getIntentDetails(binding, parentScript, intent){
		String answer = ""
		List intentsAndAnswers = getIntentsAndAnswers(binding, parentScript, binding.sSheetId)
		def answerRow = intentsAndAnswers.find{it.Intent == intent}
		return answerRow
	}

	public static getIntentCriticalityLevel(binding, parentScript, intent){
		def answerRow  = getIntentDetails(binding, parentScript, intent)
		return answerRow?.NotificationLevel.toString()
	}

	public static buildAnswer(binding, parentScript, intent, region, channel){
		String link, image, youTube, audio, map;

		def answerRow  = getIntentDetails(binding, parentScript, intent)
		def linkLocalizedElement = answerRow?.Link_Localized?:""
		List regionalLinks = linkLocalizedElement.trim().tokenize("|")
		def regions = [:]
		regionalLinks.each {
			each ->
				def tokenized = each.trim().tokenize(":")
				regions.putAt(tokenized[0],tokenized[1])
		}

		def regionalLink = regions.get(region)
		//LINK
		if (regionalLink) {
			link = regionalLink
		} else if (answerRow?.Link) {
			link = answerRow.Link
		}
		if (link) {
			UIHelper.displayLink(binding, link, channel)
		}
		//IMAGE
		if (answerRow["Image URL"]) {
			image = answerRow["Image URL"]
			UIHelper.displayImage(binding, image, channel)
		}
		//YouTube
		if (answerRow["YouTube URL"]) {
			youTube = answerRow["YouTube URL"]
			UIHelper.displayVideo(binding, youTube, channel)
		}
		//Audio
		if (answerRow["Inline Audio"]) {
			audio = answerRow["Inline Audio"]
			UIHelper.displayAudio(binding, audio, channel)
		}
		//Map
		if (answerRow["Map URL (location)"]) {
			map = answerRow["Map URL (location)"]
			UIHelper.displayMap(binding, map,channel)
		}
	}

	public static getRandomAnswer(binding, parentScript, intent, region, channel) {
		def answerRow  = getIntentDetails(binding, parentScript, intent)
		def regionalAnswersElement = answerRow?.Answers_Localized?:""
		List regionalAnswers = regionalAnswersElement.trim().tokenize("|")
		def regions = [:]
		regionalAnswers.each {
			each ->
				def tokenized = each.trim().tokenize(":")
				regions.putAt(tokenized[0],tokenized[1])
		}

		def regionalAnswer = regions.get(region)
		if (regionalAnswer) {
			return regionalAnswer
		} else {
			return getRandomAnswer(binding, parentScript, intent)
		}
	}


	public static getRandomAnswer(binding, parentScript, intent) {
		def answerRow  = getIntentDetails(binding, parentScript, intent)
		List answers = answerRow?.Answers?.tokenize("|")
		def random = new Random();
		def size = answers?.size()?:0
		def i = 0
		if (size) {
			i = random.nextInt(size)
			return answers[i]
		} else {
			return ""
		}

	}

	public static getClientConfig(binding, parentScript, sheetKey){

		def configJSON = GoogleSheetConnector.getSheetJSON(binding, sheetKey, "ClientConfig").data[0]
		binding.Lib_sBotName = configJSON.BotName
		binding.Lib_sCompanyName = configJSON.CompanyName
		binding.sCompanyEmailDomain = configJSON.CompanyEmailDomain
		binding.bUseFAQs = configJSON.UseFAQs.toBoolean()
	}

	public static getClientServices(binding, parentScript, sheetKey) {
		def servicesJSON = GoogleSheetConnector.getSheetJSON(binding, sheetKey, "Services").data
		binding.lServices = servicesJSON
	}

	public static getProactiveResponses(binding, parentScript, sheetKey) {
		def proactiveJSON = GoogleSheetConnector.getSheetJSON(binding, sheetKey, "ProactiveResponse").data
	}

	public static getProactiveNotification(binding, parentScript) {
		def map = [:]
		def json = getProactiveResponses(binding, parentScript, binding.sSheetId)
		def alerts = json.findAll{it.Type == "Alert" && isNotificationDateValid(it.ExpirationDate?:"")}
		def tips = json.findAll{it.Type == "Tip" && isNotificationDateValid(it.ExpirationDate?:"")}

		//return random alert if available otherwise random tip
		def notifictionList = alerts?:tips
		def notificationType = alerts ? "warning" : "info"
		def random = new Random();
		def i = random.nextInt(notifictionList.size())

		map.put("type", notificationType)
		map.put("text", notifictionList[i].Text)
		return map
	}

	private static isNotificationDateValid(dateStr) {
		//get current date
		if (dateStr) {
			Date now = new Date()
			Date expiration = Date.parse("dd/MM/yyyy",dateStr)

			return now < expiration
		} else {
			return true
		}


	}

	public static getIntentsAndAnswers(binding, parentScript, sheetKey) {
		//if we don't have answers go fetch
		if (!binding.oAnswerJSON){
			binding.oAnswerJSON = GoogleSheetConnector.getSheetJSON(binding, sheetKey, "IntentsAndAnswers").data
		}
		return binding.oAnswerJSON
	}

	public static getEmergencyContacts(binding, parentScript, sheetKey){
		//if we don't have answers go fetch
		if (!binding.oContactJSON){
			binding.oContactJSON = GoogleSheetConnector.getSheetJSON(binding, sheetKey, "EmergencyContact").data
		}
		return binding.oContactJSON
	}

	public static getContactInformationPerLevel(binding, parentScript, level) {
		def contacts = getEmergencyContacts(binding, parentScript, binding.sSheetId).findAll {it.Level.toString() == level}
		return contacts

	}

	public static getGreetingConfig(binding, parentScript, sheetKey){

		def configJSON = GoogleSheetConnector.getSheetJSON(binding, sheetKey, "Greeting").data[0]
		binding.lTopics = configJSON.Topics.tokenize("|")
		binding.lGreetings = configJSON["Greeting Message"].tokenize("|")
		binding.sGreetingUrl = configJSON["Greeting URL"]
		binding.bShowEmergencyButton = configJSON["Criticality Button"].toBoolean()
	}

	public static getRandomGreeting(binding, parentScript){
		def random = new Random();
		def i = random.nextInt(binding.lGreetings.size())

		return binding.lGreetings[i]
	}

	public static buildGreeting (binding, parentScript, channel) {
		UIHelper.displayOptions(binding, binding.lTopics, channel)
		def toast = getProactiveNotification(binding, parentScript)
		UIHelper.displayToast(binding, toast.type, toast.text, channel)
	}

	public static searchForTopicIntents(binding, parentScript, topicKey) {
		def intents = getIntentsAndAnswers(binding, parentScript, binding.sSheetId)
		//search where intent name contains topicKey ()
		def possibilities = intents.findAll{it.Intent.contains(topicKey)}.Question
		// return list with results sample questions
		return possibilities
	}

	public static getRelatedIntents(binding, parentScript, intentName){
		//try using cosine similarity to return similar intents to current one

		//return list of potential related intents
	}

	public static findRelatedQA(binding, parentScript, intent) {
		def question = getIntentDetails(binding, parentScript, intent).Question
		//In Solution
		//def relatedQuestions = CosineSimilarity.mostSimilar( question, TSVHelper.getQuestions(binding, "/USpage_2020-03-27.tsv"), 0.58)
		//return TSVHelper.getQuestionsAnswers(binding, relatedQuestions, "/USpage_2020-03-27.tsv")
		//IDE
		def relatedQuestions = CosineSimilarity.mostSimilar( question, getQuestions(binding, parentScript), 0.58)
		return getQuestionsAnswers(binding, relatedQuestions)
	}

	public static getScrapedFAQs(binding, parentScript) {
		//if we don't have answers go fetch
		if (!binding.oFAQJSON){
			binding.oFAQJSON = new JsonSlurper().parseText(binding.sFAQScraperURL.toURL().text).results
		}
		return binding.oFAQJSON
	}

	public static getQuestions(binding, parentScript) {
		def responseList  = getScrapedFAQs(binding, parentScript).collect{it.question}
		return responseList
	}

	public static getQuestionsAnswers(binding, questions) {
		def responseList = getScrapedFAQs(binding, this).findAll{row -> questions.find{ it -> it == row.question}}.collect{["question" : it.question, "answer" : it.answer]}
		return responseList
	}

	public static buildAnswerFromFAQ(binding, parentScript, channel, qaPairs) {
		def text = binding.sFAQIntro + UIHelper.getNewLine(binding, channel)
		qaPairs.each {
			it ->
			text += binding.sQuestionIntro + it.question + UIHelper.getNewLine(binding, channel);
				text += binding.sAnswerIntro + it.answer + UIHelper.getNewLine(binding, channel) + UIHelper.getMessageDelimiter(binding, channel)
		}
		return  text
	}
}
