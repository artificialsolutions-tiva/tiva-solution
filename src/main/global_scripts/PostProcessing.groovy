//Debugging: Returns an unmodifiable copy of the vertexprocessing path for the current turn
def list;
list=_.processingPath.findAll{it.type=="output"&&!it.skipped}.vertexId;
zOutputIds=list.toString();
if(zOutputIds)zOutputIds=zOutputIds.substring(1,zOutputIds.length()-1);
_.putOutputParameter('OUTPUT_NODE_IDS',zOutputIds);
_.putLogVariable('OUTPUT_NODE_IDS',zOutputIds);

// BUILDING VOICE RESPONSE (CSG IVR)
if (sChannel == "ivr") {
	def verb1 = [:];
	def verb2 = [:];
	List next = [];
	Map responseObj = [:]

	Map output = new HashMap()
	output.put("voice", "Joanna")
	output.put("ssml","<speak>" + _.getOutputText().replaceAll("\n"," ") + "</speak>")
	def say = [:]
	say.put("Say", output)
	def collect = [:]

	if (_.getOutputParameter("handover") == "true") {
		//Sample CTI Details + Destination phone
		//Uncomment if handover details needed
		//def map = [:]
		//map.put("cti", mCallTransferInformation)
		//map.put("destination", sDestinationPhone)
		//map.put("bridge", false)
		verb2.put ("Transfer", map)
		verb1.put("Say", output)
	} else if (_.getOutputParameter("endConversation") == "true") {
		verb2.put ("Disconnect", new Disconnect("Teneo ended conversation"))
		verb1.put("Say", output)
	} else {

		def context = _.getOutputParameter("context")
		//Removed action per Gabriel's feedback on 6/20/2019
		//collect.put("action", "")
		collect.put("mode", "both")
		if (context) {
			collect.put("recognitionContext", context)
		}
		collect.put("Say", output)
		verb1.put ("Collect", collect)

	}

	next << verb1
	if(verb2){
		next << verb2
	}

	responseObj.put("next", next)
	responseObj.put("SessionAttributes", [:])

	// CADE: Per Gabriel's feedback on 04/22/20
	//_.putOutputParameter("VoiceResponse", JsonHandler.objectToString(responseObj).replaceAll('"', '\\\\"'))
	_.putOutputParameter("VoiceResponse", JsonHandler.objectToString(responseObj))


	// BUILDING VOICE RESPONSE (CSG IVR)
	// Examples
	/*
	"VoiceResponse" :

		//EXAMPLE 1 (Normal interaction)
		{
			"next": [
				{
					"Collect": {
						"mode": "both",
						"Say": {
							"voice": "Joanna",
							"ssml": "<speak>Hello. It's good to see you!</speak>"
						}
					}
				}
			],
			"SessionAttributes": {}
		}

		//EXAMPLE 2 (Recognition Context)
		{
			"next": [
				{
					"Collect": {
						"mode": "both",
						"recognitionContext": "YesNo",
						"Say": {
							"voice": "Joanna",
							"ssml": "<speak>I'm transferring you to an agent. Is that okay?</speak>"
						}
					}
				}
			],
			"SessionAttributes": {}
		}

		//EXAMPLE 3 (Goodbye)
		{
			"next": [
				{
					"Say": {
						"voice": "Joanna",
						"ssml": "<speak>Goodbye! See you again soon, I hope.</speak>"
					}
				},
				{
					"Disconnect": {
						"reason": "Teneo ended conversation"
					}
				}
			],
			"SessionAttributes": {}
		}

		//EXAMPLE 4 (Transfer)
		{
			"next": [
				{
					"Say": {
						"voice": "Joanna",
						"ssml": "<speak>Thanks! Connecting you with an agent!</speak>"
					}
				},
				{
					"Transfer": {
						"cti": {
							"Key": "value",
							"Key": "value"
						},
						"destination": "",
						"bridge": false
					}
				}
			],
			"SessionAttributes": {}
		}
	*/
}

// Setting output parameters
if(bNeedExtensionOutputParam) {
	lExtensionOutputParams.each { it ->
		it.each { key, value ->
			_.putOutputParameter(key, value)
		}
	}

}


if(bNeedUrlOutput) {
	_.setOutputURL(sOutputUrl)
}


if(bNeedAdditionalOutputText) {
	println "appending answer text"
	String originalAnswer = _.getOutputText()
	String newAnswer =  originalAnswer + sAdditionalOutputText
	_.setOutputText(newAnswer)
}
println "final response:"
println _.getOutputText()


//Reset Output Variables
bNeedExtensionOutputParam = false
sExtensionOutputParam = ''
sExtensionOutputParamName = 'extensions'
lExtensionOutputParams = []
nNumOfExtensions = 0
bNeedUrlOutput = false
sOutputUrl = ''
bNeedAdditionalOutputText = false
sAdditionalOutputText = ''