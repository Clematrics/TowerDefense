import engine.map.Map

/**
  * A sequence of worlds
  *
  * @param str	Name of the campaign
  * @param rnds	Number of rounds
  */
class Campaign(str: String, rnds: List[Map]) {
	var name    = str
	var rounds  = rnds
}