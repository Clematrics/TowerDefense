import java.awt._
import java.awt.geom.{AffineTransform, Point2D}

abstract class RadiusTower(radiusValue:Int, reloadTime:Double, power:Int) extends Tower {
  var radius = radiusValue
  var reload = reloadTime
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
		g.drawImage(s, new AffineTransform(0.1, 0, 0, 0.1, sPos.x-40, sPos.y-40), null)	
	}
}

class ArmedTower extends RadiusTower(3, 1, 5) {
	def getName(): String = {
		return "Armed Tower"
	}

	def tick(running_for: Double, delta: Double) : Unit = {
		var enemiesNear : Array[Enemy] = Game.getEnemiesAround(pos, radius)
		
		for (e <- enemiesNear)
			e.takeDamage(pow)
	}

	override def render(g: Graphics2D): Unit = {
		val s:Image = SpriteLoader.fromResource("armedtour.png")
		val sPos = pos.toScreenPosition
		g.drawImage(s, new AffineTransform(0.1, 0, 0, 0.1, sPos.x-40, sPos.y-40), null)	
	}
}

class LaserTower extends RadiusTower(3, 2, 20) {
	def getName(): String = {
		return "Laser Tower"
	}

	def tick(running_for: Double, delta: Double) : Unit = {
		var enemiesNear : Array[Enemy] = Game.getEnemiesAround(pos, radius)
		
		for (e <- enemiesNear)
			e.takeDamage(pow)
	}

	override def render(g: Graphics2D): Unit = {
		val s:Image = SpriteLoader.fromResource("armedtour.png")
		val sPos = pos.toScreenPosition
		g.drawImage(s, new AffineTransform(0.1, 0, 0, 0.1, sPos.x-40, sPos.y-40), null)	
	}
}