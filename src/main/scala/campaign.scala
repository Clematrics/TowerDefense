class Round {
}

class Campaign(str: String, rnds: List[Round]) {
	var name    = str
	var rounds  = rnds
}

object Campaigns {
	var selectedCampaign: Int = 0
	var campaigns = List(
		new Campaign(
			"The valley of prime numbers",
			List()
		),
		new Campaign(
			"The sea of differential equations",
			List(new Round)
		),
		new Campaign(
			"Number theory's battlefield",
			List(new Round, new Round)
		),
		new Campaign(
			"true",
			List(new Round, new Round, new Round)
		),
		new Campaign(
			"Into the maze of quantum theory",
			List(new Round, new Round, new Round, new Round)
		),
		new Campaign(
			"The curse of hypercomputation",
			List(new Round, new Round, new Round, new Round, new Round)
		),
		new Campaign(
			"true",
			List(new Round, new Round, new Round, new Round, new Round, new Round)
		),
		new Campaign(
			"The castle of complexity classes",
			List(new Round, new Round, new Round, new Round, new Round, new Round, new Round, new Round, new Round, new Round, new Round, new Round, new Round, new Round)
		),
	)

	def previousCampaign(): Campaign = {
		selectedCampaign = (selectedCampaign - 1).max(0)
		return currentCampaign
	}
	def currentCampaign(): Campaign = {
		return campaigns(selectedCampaign)
	}
	def nextCampaign(): Campaign = {
		selectedCampaign = (selectedCampaign + 1).min(campaigns.length - 1)
		return currentCampaign
	}

}