/**
  * A round in a game of TowerDefense.
  */
class Round {
}

/**
  * 
  *
  * @param str
  * @param rnds
  */
class Campaign(str: String, rnds: List[Round]) {
	var name    = str
	var rounds  = rnds
}

/**
  * This class contains all available compaigns.
  */
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
			"The shitfuckery of Logics and Semantics",
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
			"The story of Levi-Civita in Lorentzian manifolds",
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