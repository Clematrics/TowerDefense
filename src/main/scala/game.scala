import scala.collection.mutable._

/**
  * This object manages the status of the game : health of the player, money and
  * EXP. It also contains a reference to the current map, and an array with every
  * entity present on the level.
  */
object Game {
	var health = 100
	var maxHealth = 100
	var gold = 100
	var experience = 0

	var map: Map = null
	var entities: ArrayBuffer[Entity] = ArrayBuffer()

	/**
	  * This function finds all enemies near the specified position and within the
	  * given radius.
	  *
	  * @param pos Center of the search
	  * @param radius Radius of view
	  * @return An array of Enemy objects. Note that the enemies that are found are instances of MovingEnemy at least.
	  */
	def getEnemiesAround(pos: CellPoint, radius: Double): Array[Enemy] = {
		return 	entities.filter(e => e.isInstanceOf[MovingEnemy]
				&& pos.distance(e.asInstanceOf[MovingEnemy].pos) <= radius).map(x => x.asInstanceOf[Enemy]).toArray[Enemy]
	}

	def reset(): Unit = {
		health = maxHealth
		gold = 100
		experience = 0
		map = MapLoader.loadMap(Game.map.name)
		entities = ArrayBuffer()
	}
}