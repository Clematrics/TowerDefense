/**
  * This class contains all available compaigns.
  */
object Campaigns {
	var selectedCampaign: Int = 0
	var campaigns = List(
		new Campaign(
			"The valley of prime numbers",
			List(MapLoader.loadMap("map"), MapLoader.loadMap("mapSimple"))
		)
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