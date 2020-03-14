import engine.core.Renderer
import engine.loaders.SpriteLoader

import java.awt._
import java.awt.geom.{AffineTransform, Point2D}

/**
  * Radius Tower represents the basic definitions implemented by all towers
  * having an influence in a circle around them.
  *
  * @param radiusValue
  * @param reloadTime
  * @param power
  */
abstract class RadiusTower(radiusValue:Int, reloadTime:Double, power:Int) extends Tower {
  var radius = radiusValue
  var reload = reloadTime
  var lastShot = 0.0
  var pow = power
}

/**
  * A decoration tower.
  *
  */
class ProtoTower extends Tower {
	cost = 15

	def getName(): String = {
		return "Chess Tower"
	}

	def tick(time: Double, delta: Double) : Unit = {
	}

	def render(time: Double, delta: Double): Unit = {
		val s:Image = SpriteLoader.fromResource("tour.png")
		val sPos = pos.toScreenPosition
		Renderer.groundEntities.drawImage(s, new AffineTransform(0.2, 0, 0, 0.2, sPos.x, sPos.y - 20), null)
	}
}