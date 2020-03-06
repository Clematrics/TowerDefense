import java.awt._
import java.awt.geom.{AffineTransform, Point2D}

abstract class RadiusTower(radiusValue:Int, reloadTime:Double, power:Int) extends Tower {
  var radius = radiusValue
  var reload = reloadTime
  var lastShot = 0.0
  var pow = power
}

class ProtoTower extends Tower {
	cost = 15

	def getName(): String = {
		return "Chess Tower"
	}

	def tick(running_for: Double, delta: Double) : Unit = {
	}

	override def render(g: Graphics2D): Unit = {
		val s:Image = SpriteLoader.fromResource("tour.png")
		val sPos = pos.toScreenPosition
		Renderer.groundEntities.drawImage(s, new AffineTransform(0.2, 0, 0, 0.2, sPos.x, sPos.y - 20), null)
	}
}