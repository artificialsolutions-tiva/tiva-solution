def binding = new GlobalEmulation()
def googleData = KnowledgeHelper.getIntentsAndAnswers(binding, this, binding.sSheetId)

googleData.each {
	row ->
		println("IntentName: " + row.Intent)
		println("Example Question: " + row.Question)
		println("Potential FAQ question Matches:")
		CosineSimilarity.mostSimilar(row.Question, KnowledgeHelper.getQuestions(binding, this), 0.58).each {println("\t" + it)}
		println("---***---")
}
