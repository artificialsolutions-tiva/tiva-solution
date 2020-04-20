import groovy.json.JsonSlurper

class GoogleSheetConnector {

	public static getSheetJSON(sheetKey, sheetName) {

		def apiUrl = "http://jaguar.jolzee.xyz/utils/gsheet?spreadsheetKey=" + sheetKey + "&worksheetTitle=" +  sheetName
		def response = new JsonSlurper().parseText(apiUrl.toURL().text)
	}
}
