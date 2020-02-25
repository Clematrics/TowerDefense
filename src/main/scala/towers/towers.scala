import java.awt._
import java.awt.geom.{AffineTransform, Point2D}

abstract class RadiusTower(radiusValue:Int, reloadTime:Double, power:Int) extends Tower {
  var radius = radiusValue
  var reload = reloadTime
  var lastShot = 0.0
  var pow = power
}

class ProtoTower extends Tower {
	def getName(): String = {
		return "Chess Tower"
	}

	def tick(running_for: Double, delta: Double) : Unit = {
		
	}

	override def render(g: Graphics2D): Unit = {
		val s:Image = SpriteLoader.fromResource("tour.png")
		val sPos = pos.toScreenPosition
		g.drawImage(s, new AffineTransform(0.3, 0, 0, 0.3, sPos.x-40, sPos.y-40), null)	
	}
}

class ArmedTower extends RadiusTower(3, 1000, 5) {
	def getName(): String = {
		return "Armed Tower"
	}

	def tick(running_for: Double, delta: Double) : Unit = {
		if (lastShot + reload < running_for) {
			var enemiesNear : Array[Enemy] = Game.getEnemiesAround(pos, radius)
			
			for (e <- enemiesNear) {
				e.takeDamage(pow)
				Game.entities.+=(new Particle(e.asInstanceOf[MovingEnemy], this))
			}
			lastShot = running_for
		}
	}

	override def render(g: Graphics2D): Unit = {
		val s:Image = SpriteLoader.fromResource("armedtour.png")
		val sPos = pos.toScreenPosition
		g.drawImage(s, new AffineTransform(1.5, 0, 0, 1.5, sPos.x-40, sPos.y-40), null)	
	}
}

class LaserTower extends RadiusTower(3, 2000, 20) {
	def getName(): String = {
		return "Laser Tower"
	}

	def tick(running_for: Double, delta: Double) : Unit = {
		if (lastShot + reload < running_for) {
			var enemiesNear : Array[Enemy] = Game.getEnemiesAround(pos, radius)
			
			for (e <- enemiesNear) {
				e.takeDamage(pow)
				Game.entities.+=(new LaserBeam(running_for, e.asInstanceOf[MovingEnemy], this))
			}
			lastShot = running_for
		}
	}

	override def render(g: Graphics2D): Unit = {
		val s:Image = SpriteLoader.fromResource("lasertour.png")
		val sPos = pos.toScreenPosition
		g.drawImage(s, new AffineTransform(1.5, 0, 0, 1.5, sPos.x-40, sPos.y-40), null)	
	}
}