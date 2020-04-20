//CHANNEL
/*
Channel types:
   - IVR
   - webview
   - sms
   - dialogueTester
   - other
*/
if (engineEnvironment.getParameter("channel")) {
	sChannel = engineEnvironment.getParameter("channel").toLowerCase();
} else if (engineEnvironment.getParameter('currentPageUrl') && engineEnvironment.getParameter('currentPageUrl') == 'dialogueTester') {
	//DialogueTester inputs can be filtered out from reporting KPIs
	sChannel = "dialogueTester";
} else {
	sChannel = "other"
}

//DemoMode
if (engineEnvironment.getParameter("demoMode")){
	bDemoMode = engineEnvironment.getParameter("demoMode").toBoolean()
}

//Leopard UI

/*
timeZone: America/Los_Angeles
city: Sammamish
continentCode: NA
continentName: North America
countryCode: US
countryName: United States
currencySymbol: $
currencyCode: USD
latitude: 47.5857
longitude: -122.0345
regionCode: WA
*/

//TIME ZONE
if (engineEnvironment.getParameter("timeZone")){
	Lib_sUserTimeZone = engineEnvironment.getParameter("timeZone")
}

//CONTINENT
if (engineEnvironment.getParameter("continentCode")){
	mUserLocationDetails.continentCode = engineEnvironment.getParameter("continentCode")
}
if (engineEnvironment.getParameter("continentName")){
	mUserLocationDetails.continentName = engineEnvironment.getParameter("continentName")
}

//COUNTRY
if (engineEnvironment.getParameter("countryCode")){
	mUserLocationDetails.countryCode = engineEnvironment.getParameter("countryCode")
	sRegion = engineEnvironment.getParameter("countryCode");
}
if (engineEnvironment.getParameter("countryName")){
	mUserLocationDetails.countryName = engineEnvironment.getParameter("countryName")
}

//CITY
if (engineEnvironment.getParameter("city")){
	mUserLocationDetails.city = engineEnvironment.getParameter("city")
}

//AUTO-TEST
//Overrides the value of bAutoTest if it has been set to "true"
//Send input param autoTest = false in Studio if you want to continue testing while others are running Auto-Test
if (engineEnvironment.getParameter("autoTest")){
	bAutoTest = engineEnvironment.getParameter("autoTest").toBoolean();
}


// The below script add values for current day, month and year to the variables Lib_iDay, Lib_iMonth and Lib_iYear.
def c = Calendar.getInstance()
Lib_iDay = c.get(Calendar.DAY_OF_MONTH)
Lib_iMonth = c.get(Calendar.MONTH) + 1 // Returns 0..11
Lib_iYear = c.get(Calendar.YEAR)

//Get sheetId input parameter or use default
if (engineEnvironment.getParameter("sheetId")) {
	sSheetId = engineEnvironment.getParameter("sheetId")
} else {
	sSheetId = "191ozzVXeg88pzykiozfLYy5-518fVWdqDjrcrnQM6LU"
}

//ELST: Moved this above NER as the sheet information is required for NER
//Get sheet info for conversation start
try {
	KnowledgeHelper.getClientConfig(binding, this, sSheetId)
	KnowledgeHelper.getGreetingConfig(binding, this, sSheetId)
	KnowledgeHelper.getClientServices(binding, this, sSheetId)
} catch(ExceptionInInitializerError ex){
	println 'ERROR! Begin dialog script exception: '+ex;
}

//Gazeteer NER for Topics
def topicEntityList = new LinkedList<ner.Entity>();
lTopics.each {
	item ->

		def map = [:]
		map.put("topic", item)
		Map<String, Object> vars = map
		List<String> tokens = Arrays.asList(_.divideIntoWords(item));
		topicEntityList.add(new ner.Entity(tokens, vars));
}

//Topic -> EXACT MATCH NER
//Set settings for the matching process
Properties propsExact = new Properties()
propsExact.put("word match","exact")
propsExact.put("case sensitive","false")
propsExact.put("spelling threshold","80")

oTopicExactNER = new ner.NER("TOPIC_EXACT", topicEntityList, propsExact)
def topicBlockWordsExact = []
oTopicExactNER.setBlockwords(topicBlockWordsExact);

//Topic -> TOLERANT MATCH NER
//Set settings for the matching process
Properties propsTolerant = new Properties()
propsTolerant.put("word match","tolerant")
propsTolerant.put("case sensitive","false")
propsTolerant.put("score threshold","30")
propsTolerant.put("spelling threshold","90")

oTopicTolerantNER = new ner.NER("TOPIC_APPROX", topicEntityList, propsTolerant)
def topicBlockWordsTolerant = []
oTopicTolerantNER.setBlockwords(topicBlockWordsTolerant);

//Gazeteer NER for Services

def serviceEntityList = new LinkedList<ner.Entity>();
lServices.each {
	item ->

		Map<String, Object> vars = item
		List<String> tokens = Arrays.asList(_.divideIntoWords(item["Service Name"]));
		serviceEntityList.add(new ner.Entity(tokens, vars));
}

//Service -> EXACT MATCH NER
//Use same settings from Topic

oServiceExactNER = new ner.NER("SERVICE_EXACT", serviceEntityList, propsExact)
def serviceBlockWordsExact = []
oServiceExactNER.setBlockwords(serviceBlockWordsExact);

//Service -> TOLERANT MATCH NER
//Use same settings from Topic

oServiceTolerantNER = new ner.NER("SERVICE_APPROX", serviceEntityList, propsTolerant)
def serviceBlockWordsTolerant = []
oServiceTolerantNER.setBlockwords(serviceBlockWordsTolerant);




