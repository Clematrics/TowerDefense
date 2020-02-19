class Round {
}

class Campaign(b: Boolean, rnds: List[Round]) {
	var backgroundSprite = b
	var rounds           = rnds
}

object Campaigns {
	var selectedCampaign: Int = 0
	var campaigns = List(
		new Campaign(true, List()),
		new Campaign(true, List(new Round)),
		new Campaign(true, List(new Round, new Round)),
		new Campaign(true, List(new Round, new Round, new Round)),
		new Campaign(true, List(new Round, new Round, new Round, new Round)),
		new Campaign(true, List(new Round, new Round, new Round, new Round, new Round)),
		new Campaign(true, List(new Round, new Round, new Round, new Round, new Round, new Round)),
		new Campaign(true, List(new Round, new Round, new Round, new Round, new Round, new Round, new Round, new Round, new Round, new Round, new Round, new Round, new Round, new Round)),
	)

	def previousCampaign(): Campaign = {
		selectedCampaign = (selectedCampaign - 1).max(0)
		return campaigns(selectedCampaign)
	}
	def nextCampaign(): Campaign = {
		selectedCampaign = (selectedCampaign + 1).min(campaigns.length - 1)
		return campaigns(selectedCampaign)
	}

}