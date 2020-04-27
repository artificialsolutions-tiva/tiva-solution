//Inputs: sUserPrincipalName
//Outputs: sManagerDisplayName, sManagerPrincipalName

import groovy.json.JsonSlurper
import groovy.json.JsonOutput

def graphUrl = "https://graph.microsoft.com/v1.0/"

if (sUserPrincipalName){
	graphUrl = graphUrl + 'users/' + sUserPrincipalName + '/manager'
} else {
	graphUrl = graphUrl + 'me/manager'
}

import groovy.json.JsonSlurper

def results = new URL(graphUrl).getText(connectTimeout: 5000,
		readTimeout: 10000,
		requestProperties: ['Authorization': 'Bearer ' + oMsAuth.accessToken, 'Accept':'application/json'])

def jsonSlurper = new JsonSlurper()
def object = jsonSlurper.parseText(results)

sManagerDisplayName = object.displayName
sManagerPrincipalName = object.userPrincipalName
