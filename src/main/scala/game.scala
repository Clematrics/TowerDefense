import scala.collection.mutable._

/**
  * 
  */
object Game {
	var health = 100
	var maxHealth = 100
	var gold = 100
	var experience = 0

	var mapName = ""
	var map: Map = null
	var towers = Array.ofDim[Tower](45, 30)
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
}