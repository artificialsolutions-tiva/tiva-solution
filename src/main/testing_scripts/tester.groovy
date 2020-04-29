
def binding = new GlobalEmulation()
println(binding.sSheetId)


def stuff = KnowledgeHelper.getIntentsAndAnswers(binding, this, binding.sSheetId)
println stuff

println("WFH_COVID19_HR_WFH_PAY:" + KnowledgeHelper.getRandomAnswer(binding, this, "this intent doesn't exist"))
println KnowledgeHelper.getRandomAnswer(binding, this, "WFH_COVID19_HR_GENERAL_TRAVEL_POLICY")
println KnowledgeHelper.getRandomAnswer(binding, this, "WFH_COVID19_HR_CORONAVIRUS_AVOIDING_GIVING_IT_TO_OTHERS", "ES", "webview")
println KnowledgeHelper.getRandomAnswer(binding, this, "WFH_COVID19_HR_CORONAVIRUS_AVOIDING_GIVING_IT_TO_OTHERS", "US", "webview")

println ("demo true")
binding.bDemoMode = true
KnowledgeHelper.buildAnswer(binding, this, "WFH_COVID19_HR_CORONAVIRUS_SPREAD_FOOD", null, "webview")
println(binding.bNeedUrlOutput)
println(binding.sOutputUrl)

println ("demo false")
binding.bDemoMode = false
KnowledgeHelper.buildAnswer(binding, this, "WFH_COVID19_HR_CORONAVIRUS_SPREAD_FOOD", null, "webview")
println(binding.bNeedAdditionalOutputText)
println(binding.sAdditionalOutputText)
/*
println KnowledgeHelper.getIntentCriticalityLevel(binding, this, "WFH_COVID19_HR_GENERAL_TRAVEL_POLICY")
println KnowledgeHelper.getContactInformationPerLevel(binding, this, "1")
KnowledgeHelper.getGreetingConfig(binding, this, binding.sSheetId)
println(binding.lTopics)
println(binding.lGreetings)
println(binding.sGreetingUrl)
println(binding.bShowEmergencyButton)
println KnowledgeHelper.getRandomGreeting(binding,this)

KnowledgeHelper.getClientConfig(binding, this, binding.sSheetId)
println(binding.Lib_sBotName)
println(binding.Lib_sCompanyName)

println KnowledgeHelper.getProactiveNotification(binding,this)

KnowledgeHelper.getClientServices(binding,this,binding.sSheetId)

println KnowledgeHelper.searchForTopicIntents(binding, this, "HR_CORONAVIRUS")
println KnowledgeHelper.searchForTopicIntents(binding, this, "HR_WFH")
println KnowledgeHelper.searchForTopicIntents(binding, this, "WFH_IT")
println KnowledgeHelper.searchForTopicIntents(binding, this, "HR_GENERAL")
*/


//println("line seperator:" + UIHelper.getNewLine(binding, "webview"))
//println KnowledgeHelper.buildAnswerFromFAQ(binding,this,"webview", KnowledgeHelper.findRelatedQA(binding, this, "WFH_COVID19_HR_CORONAVIRUS_AVOIDING_GIVING_IT_TO_OTHERS"))
//println(KnowledgeHelper.findRelatedQA(binding, this,  "How can I prevent spreading COVID-19 illness to others?"))
//println( buildAnswerFromFAQ(binding, this, "", KnowledgeHelper.findRelatedQA(binding, this,  "WFH_COVID19_HR_CORONAVIRUS_AVOIDING_GIVING_IT_TO_OTHERS")))

//print KnowledgeHelper.buildAnswerFromFAQ(binding,this,  KnowledgeHelper.findRelatedQA(binding, this,  "WFH_COVID19_HR_CORONAVIRUS_AVOIDING_GIVING_IT_TO_OTHERS"))

/*
KnowledgeHelper.getClientConfig(binding, this, binding.sSheetId)
println(binding.Lib_sBotName)
println(binding.Lib_sCompanyName)
println(binding.bUseFAQs.getClass())


println(KnowledgeHelper.getScrapedFAQs(binding, this ))
println(KnowledgeHelper.getQuestions(binding, this ).getClass())
println(KnowledgeHelper.getQuestionsAnswers(binding, KnowledgeHelper.getQuestions(binding, this ) ))

 */

println UIHelper.supportsForms(binding,"webview")
println UIHelper.supportsForms(binding,"botframework-msteams")
println UIHelper.supportsForms(binding,"ivr")

