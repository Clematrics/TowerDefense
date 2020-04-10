import java.awt.{Color, Image}
import java.awt.geom.AffineTransform

import engine.core.GamePanel.{ps, view}
import engine.core.Renderer
import engine.loaders.SpriteLoader
import engine.helpers.Delay

import scala.collection.mutable

class WallTower extends Tower {
	cost = 50
	var life = 100

	def getName(): String = {
		return "Thunder Tower"
	}

	def tick(time: Double, delta: Double): Unit = {
		if (life <= 0) {
			Game.map.map(pos.x.toInt)(pos.y.toInt) = engine.map.Path
			println("Wall destroyed")
			println("Computing path from tower")
			Game.entities.filter(_.isInstanceOf[MovingEnemy]).foreach(_.asInstanceOf[MovingEnemy].computePath)
			valid = false
		}
	}

	def damage(dmg: Int) : Unit = {
		life -= dmg
	}

	def render(time: Double, delta: Double): Unit = {
		val s: Image = SpriteLoader.fromResource("wall.png")
		val sPos = pos.toScreenPosition
		Renderer.groundEntities.drawImage(s, new AffineTransform(1.3, 0, 0, 1.3, sPos.x - 7, sPos.y - 8), null)
	}

}