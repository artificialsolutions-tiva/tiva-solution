//Inputs: content, emailAddress, subject
//Outputs: sent
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.POST
import static groovyx.net.http.ContentType.JSON

def http = new HTTPBuilder('http://jaguar.jolzee.xyz')
sent = false

http.request( POST, JSON ) { req ->
	uri.path = '/utils/send-email'
	body = [to: emailAddress, subject: subject, text: content]
	response.success = { resp, json ->
		if (json.status == "sent") {
			println "You email was sent 📧"
			sent = true
		} else {
			println "There was an issue 😮"
			sent = false
		}
	}
}