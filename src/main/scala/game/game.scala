import engine.core.Entity
import engine.helpers.CellPoint
import engine.map.Map
import java.io._

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

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
		return entities.filter(e => e.isInstanceOf[MovingEnemy]
		       && pos.distance(e.asInstanceOf[MovingEnemy].pos) <= radius).map(x => x.asInstanceOf[Enemy]).toArray[Enemy]
	}

	/**
	  * 
	  *
	  * @param filter
	  * @return
	  */
	def getEnemiesWhere(filter: MovingEnemy => Boolean): Array[Enemy] = {
		return entities.filter(e => e.isInstanceOf[MovingEnemy] && filter(e.asInstanceOf[MovingEnemy])).map(x => x.asInstanceOf[Enemy]).toArray[Enemy]
	}

	def reset(): Unit = {
		health = maxHealth
		/*We can earn money and gain experience, but when are we using them?*/
		//gold = 100
		//experience = 0
		map = MapLoader.loadMap(Game.map.name)
		entities = ArrayBuffer()
	}

	def save(): Unit = {
		val writer = new PrintWriter(new File("save.ini"))

		writer.println(maxHealth)
		writer.println(gold)
		writer.println(experience)

		writer.close()
	}

	def load(): Unit = {
		try {
			val str = Source.fromFile("save.ini").getLines()

			val nbs =	str.map(_.toInt).toArray

			maxHealth = nbs(0)
			health = maxHealth
			gold = nbs(1)
			experience = nbs(2)
		}
		catch {
			case _ : java.io.FileNotFoundException => ()
		}
	}
}