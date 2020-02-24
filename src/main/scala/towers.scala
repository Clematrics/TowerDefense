import java.awt._
import java.awt.geom.{AffineTransform, Point2D}

class ProtoTower extends Tower {
	var pos: CellPosition = new CellPosition(15, 10)

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

class ArmedTower extends Tower {
	var pos: CellPosition = new CellPosition(15, 10)
	var radius:Double = 3
	def getName(): String = {
		return "Armed Tower"
	}

	def tick(running_for: Double, delta: Double) : Unit = {
		
	}

	override def render(g: Graphics2D): Unit = {
		val s:Image = SpriteLoader.fromResource("armedtour.png")
		val sPos = pos.toScreenPosition
		g.drawImage(s, new AffineTransform(0.1, 0, 0, 0.1, sPos.x-40, sPos.y-40), null)	
	}
}