/**
  * This class contains all available compaigns.
  */
object Campaigns {
	var selectedCampaign: Int = 0
	var campaigns = List(
		new Campaign(
			"The valley of prime numbers",
			List(MapLoader.loadMap("map"), MapLoader.loadMap("mapSimple"), MapLoader.loadMap("maze"))
		),
		new Campaign(
			"The sea of differential equations",
			List(MapLoader.loadMap("map"))
		),
		new Campaign(
			"Number theory's battlefield",
			List(MapLoader.loadMap("map"), MapLoader.loadMap("map"))
		),
		new Campaign(
			"The shitfuckery of Logics and Semantics",
			List(MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"))
		),
		new Campaign(
			"Into the maze of quantum theory",
			List(MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"))
		),
		new Campaign(
			"The curse of hypercomputation",
			List(MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"))
		),
		new Campaign(
			"The story of Levi-Civita in Lorentzian manifolds",
			List(MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"))
		),
		new Campaign(
			"The castle of complexity classes",
			List(MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"), MapLoader.loadMap("map"))
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