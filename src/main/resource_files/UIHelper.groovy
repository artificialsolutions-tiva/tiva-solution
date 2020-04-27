import groovy.json.JsonBuilder
import groovy.json.JsonOutput

/*
* Class for displaying various types of files in UI
*/
class UIHelper {
	//add global inline
	/* General refactoring that must be done
	 * Use the ExtensionHelper functions which takes in the same parameters here
	 */
	static String displayLink(def binding, def url, def channel, def inline = true) {
		def outputParameter = ""
		switch(channel) {
			case "webview":
				// binding.bNeedExtensionOutputParam = true
				// def content = '<a href="' + url + '"><a>'
				// binding.sExtensionOutputParam = outputParameter = ExtensionHelper.displayPanel(content, channel)
				if (binding.bDemoMode) {
					binding.bNeedUrlOutput = true
					binding.sOutputUrl = outputParameter = url
				} else {
					binding.bNeedAdditionalOutputText = true
					binding.sAdditionalOutputText += getNewLine(binding, channel) + "<a href=\"$url\">$url<a>"
				}

				break
			case "twilio-sms":
				binding.bNeedAdditionalOutputText = true
				binding.sAdditionalOutputText += getNewLine(binding, channel) + url
				break
			case "botframework-msteams":
				binding.bNeedAdditionalOutputText = true
				binding.sAdditionalOutputText += getNewLine(binding, channel)+ "[$binding.sLinkInro $url]($url)"
				break
			default:
				println("channel unknown")
				break
		}
		return outputParameter
	}

	static String displayImage(def binding, def imageUrl, def channel, def inline = true) {
		def outputParameter = ""
		switch(channel) {
			case "webview":
				outputParameter = ExtensionHelper.displayImage(imageUrl, channel, inline)
				binding.sExtensionOutputParam = outputParameter
				binding.bNeedExtensionOutputParam = true
				binding.nNumOfExtensions += 1
				binding.sExtensionOutputParamName = 'extensions' + binding.nNumOfExtensions.toString()
				def sEOPN = binding.sExtensionOutputParamName
				binding.lExtensionOutputParams << [(sEOPN) : (outputParameter)]
				break
			case "botframework-msteams":
				def json = BotFrameworkHelper.createImageJSON(imageUrl,"Image")
				binding.bNeedExtensionOutputParam = true
				binding.lExtensionOutputParams << ["msbotframework": json ]
				break;
			default:
				println("channel unknown")
				break
		}
		return outputParameter
	}

	static String displayImageCarousel(def binding, def images, def channel, def inline = true) {
		def outputParameter = ""
		switch(channel) {
			case "webview":
				outputParameter = ExtensionHelper.displayImageCarousel(images, channel, inline)
				binding.sExtensionOutputParam = outputParameter
				binding.bNeedExtensionOutputParam = true
				binding.nNumOfExtensions += 1
				binding.sExtensionOutputParamName = 'extensions' + binding.nNumOfExtensions.toString()
				def sEOPN = binding.sExtensionOutputParamName
				binding.lExtensionOutputParams << [(sEOPN) : (outputParameter)]
				break
			case "botframework-msteams":
				break;
			default:
				println("channel unknown")
				break
		}
		return outputParameter
	}

	static String displayVideo(def binding, def videoUrl, def channel, def inline = true) {
		def outputParameter = ""
		switch(channel) {
			case "webview":
				outputParameter = ExtensionHelper.displayVideo(videoUrl, channel, inline)
				binding.sExtensionOutputParam = outputParameter
				binding.bNeedExtensionOutputParam = true
				binding.nNumOfExtensions += 1
				binding.sExtensionOutputParamName = 'extensions' + binding.nNumOfExtensions.toString()
				def sEOPN = binding.sExtensionOutputParamName
				binding.lExtensionOutputParams << [(sEOPN) : (outputParameter)]
				break
			case "botframework-msteams":
				//def json = BotFrameworkHelper.createVideoJSON(videoUrl, "Video")
				//binding.bNeedExtensionOutputParam = true
				//binding.lExtensionOutputParams << ["msbotframework": json ]

				binding.bNeedAdditionalOutputText = true
				binding.sAdditionalOutputText += getNewLine(binding, channel)+"[$binding.sVideoLinkInro $videoUrl]($videoUrl)"
				break;
			default:
				break
		}
		return outputParameter
	}

	static String displayAudio(def binding, def audioUrl, def channel, def inline = true) {
		def outputParameter = ""
		switch(channel) {
			case "webview":
				outputParameter = ExtensionHelper.displayVideo(audioUrl, channel, inline)
				binding.sExtensionOutputParam = outputParameter
				binding.bNeedExtensionOutputParam = true
				binding.nNumOfExtensions += 1
				binding.sExtensionOutputParamName = 'extensions' + binding.nNumOfExtensions.toString()
				def sEOPN = binding.sExtensionOutputParamName
				binding.lExtensionOutputParams << [(sEOPN) : (outputParameter)]
				break
			case "botframework-msteams":
				break;
			default:
				break
		}
		return outputParameter
	}

	static String displayMap(def binding, def address, def channel, def inline = false) {
		def outputParameter = ""
		switch(channel) {
			case "webview":
				outputParameter = ExtensionHelper.displayMap(address, channel, inline)
				binding.sExtensionOutputParam = outputParameter
				binding.bNeedExtensionOutputParam = true
				binding.nNumOfExtensions += 1
				binding.sExtensionOutputParamName = 'extensions' + binding.nNumOfExtensions.toString()
				def sEOPN = binding.sExtensionOutputParamName
				binding.lExtensionOutputParams << [(sEOPN) : (outputParameter)]
				break
			case "botframework-msteams":
				break;
			default:
				break
		}
		return outputParameter
	}

	static String displayClickableList(def binding, def options, def channel) {
		def outputParameter = ["title": "Options",
							   "items": [] ]
		switch(channel) {
			case "webview":
				def nameList = []
				options.each { name ->
					def nameMap = ["name":name]
					nameList.add(nameMap)
				}
				outputParameter.items = nameList
				outputParameter = ExtensionHelper.displayClickableList(outputParameter, channel, true).toString()
				binding.sExtensionOutputParam = outputParameter
				binding.bNeedExtensionOutputParam = true
				binding.nNumOfExtensions += 1
				binding.sExtensionOutputParamName = 'extensions' + binding.nNumOfExtensions.toString()
				def sEOPN = binding.sExtensionOutputParamName
				binding.lExtensionOutputParams << [(sEOPN) : (outputParameter)]
				break
			case "botframework-msteams":
				def json  = BotFrameworkHelper.createSuggestedActionsJSON(options)
				binding.bNeedExtensionOutputParam = true
				binding.lExtensionOutputParams << ["msbotframework": json ]
				break;
			default:
				println("Unknown channel")
				break
		}

		return outputParameter
	}

	static String displayOptions(def binding, def options, def channel) {
		def outputParameter = ["title": "Options",
							   "items": [] ]
		switch(channel) {
			case "webview":
				def nameList = []
				options.each { name ->
					def nameMap = ["name":name]
					nameList.add(nameMap)
				}
				outputParameter.items = nameList
				outputParameter = ExtensionHelper.displayPermanentClickableList(outputParameter, channel).toString()
				binding.sExtensionOutputParam = outputParameter
				binding.bNeedExtensionOutputParam = true
				binding.nNumOfExtensions += 1
				binding.sExtensionOutputParamName = 'extensions' + binding.nNumOfExtensions.toString()
				def sEOPN = binding.sExtensionOutputParamName
				binding.lExtensionOutputParams << [(sEOPN) : (outputParameter)]
				break
			case "botframework-msteams":
				def json  = BotFrameworkHelper.createSuggestedActionsJSON(options)
				binding.bNeedExtensionOutputParam = true
				binding.lExtensionOutputParams << ["msbotframework": json ]
				break;
			default:
				println("Unknown channel")
				break
		}

		return outputParameter
	}

	static String displayToast(def binding, def type, def content, def channel) {
		def outputParameter = ''
		switch(channel) {
			case "webview":
				def toast = ["type":type, "body":content, "config":["timeout":45000, "showProgressBar":true, "closeOnClick":true, "pauseOnHover":true, "position":"rightBottom"]]
				outputParameter = JsonOutput.toJson(toast).toString()
				binding.sExtensionOutputParam = outputParameter
				binding.bNeedExtensionOutputParam = true
				binding.sExtensionOutputParamName = 'toast'
				binding.lExtensionOutputParams << ['toast':(outputParameter)]
				break
			case "botframework-msteams":
				break;
			default:
				println("Unknown channel")
				break
		}
		return outputParameter
	}

	static String displayToast(def binding, def typeContentMap, def channel) {
		def outputParameter = ''
		switch(channel) {
			case "webview":
				def toast = [typeContentMap]
				toast.add("config":["timeout":45000, "showProgressBar":true, "closeOnClick":true, "pauseOnHover":true, "position":"rightBottom"])
				outputParameter = JsonOutput.toJson(toast).toString()
				binding.sExtensionOutputParam = outputParameter
				binding.bNeedExtensionOutputParam = true
				binding.sExtensionOutputParamName = 'toast'
				binding.lExtensionOutputParams << ['toast':(outputParameter)]
				break
			case "botframework-msteams":
				break;
			default:
				println("Unknown channel")
				break
		}
	}

	static String getMessageDelimiter(binding, channel){
		def messageDelimiter = ""
		switch (channel){
			case "webview":
				messageDelimiter = "||"
				break;
			case "botframework-msteams":
				messageDelimiter = "||"
				break;
			case "twilio-sms":
				messageDelimiter = getNewLine(binding, channel) + getNewLine(binding, channel)
				break;
			default:
				println("Unknown channel")
				break
		}
		return messageDelimiter;
	}

	static boolean supportsForms(binding, channel) {
		boolean answer = false
		switch (channel){
			case "webview":
				answer = true
				break;
			case "botframework-msteams":
				answer = false
				break;
			case "twilio-sms":
				answer = false
				break;
			default:
				println("Unknown channel")
				break
		}
		return answer;
	}

	static displayForm(binding, channel) {
		switch (channel) {
			case "webview":
				//TODO
				binding.bNeedExtensionOutputParam = true
				//binding.sExtensionOutputParamName = 'formConfig'
				binding.lExtensionOutputParams << ['formConfig': ""]
				break;
			case "botframework-msteams":
				//TODO
				break;
			case "twilio-sms":
				break;
			default:
				println("Unknown channel")
				break;
		}
	}

	static boolean supportsClickableList(binding, channel) {
		boolean answer = false
		switch (channel){
			case "webview":
				answer = true
				break;
			case "botframework-msteams":
				answer = true
				break;
			case "twilio-sms":
				answer = false
				break;
			default:
				println("Unknown channel")
				break
		}
		return answer;
	}

	static getNewLine(binding, channel) {
		String newLine = System.getProperty("line.separator")
		switch (channel){
			case "webview":
				newLine = System.getProperty("line.separator")
				break;
			case "botframework-msteams":
				newLine = "\n\n"
				break;
			case "twilio-sms":
				newLine = "\n"
				break;
			default:
				newLine = System.getProperty("line.separator")
				break
		}
	}
}
