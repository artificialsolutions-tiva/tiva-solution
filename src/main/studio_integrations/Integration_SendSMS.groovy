//Inputs: content, phoneNumber
//Outputs: sent
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.POST
import static groovyx.net.http.ContentType.JSON

def http = new HTTPBuilder('http://jaguar.jolzee.xyz')
http.request( POST, JSON ) { req ->
	uri.path = '/utils/send-sms'
	body = [to: phoneNumber, message: content]

	response.success = { resp, json ->
		println json.status
		if (json.status == "sent") {
			println "Your sms was sent 📧"
			sent = true
		} else if (json.status == 'queued'){
			println "Your sms is queued"
			sent = true
		} else {
			println "There was an issue 😮"
			sent = false
		}
	}
}