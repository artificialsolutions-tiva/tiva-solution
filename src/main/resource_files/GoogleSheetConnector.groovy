import groovy.json.JsonSlurper

class GoogleSheetConnector {

	public static getSheetJSON(binding, sheetKey, sheetName) {

		def apiUrl = "$binding.sJaguarEndpoint/utils/gsheet?spreadsheetKey=" + sheetKey + "&worksheetTitle=" +  sheetName
		def response = new JsonSlurper().parseText(apiUrl.toURL().text)
	}
}
