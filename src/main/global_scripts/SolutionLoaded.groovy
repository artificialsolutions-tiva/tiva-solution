import groovy.json.JsonBuilder



/*
* Class DataStore is used to handle structured IIP data
* One object of this class is instantiated as a global variable (oSDS)
* Add a field to this class for every user property you want to access in the solution, but keep out of the session logs
* @version 1.0
*/

public class DataStore {
	public String sCallingNumber;          // received by ANI from IVR
	public String sEmailAddressMentioned;  // any email address mentioned in the user input
	public String sPhoneNumberMentioned;   // any phone number mentioned in the user input
	public String sUserFirstName;          // used by TDR flows
}


/*
* Class with Machine Learning Classifier helper methods
* @version 1.0
*/

public class MLClassifierHelper {

	public static Number iConfidenceMinimum = 0.20
	public static Number iConfidenceMaximum = 0.45

}


/*
* Class for helping with JSON stuctures
* @version 1.0
*/

public class JsonHandler {
	public static jsonOutput = new groovy.json.JsonOutput()
	public static jsonSlurper = new groovy.json.JsonSlurper()

	public static objectToString(def obj) {
		String str =jsonOutput.toJson(obj)
		return str
	}

	public static stringToObject(String str) {
		def obj = jsonSlurper.parseText(str)
		return obj
	}

	public static stringToPrettyString(String str) {
		return jsonOutput.prettyPrint(str)
	}

}


/*
* Class for defining IVR disconnect
* @version 1.0
*/

@groovy.transform.ToString(includeNames = true, includeFields = true)
class Disconnect {
	String reason

	public Disconnect(String str) {
		reason = str;
	}
}
