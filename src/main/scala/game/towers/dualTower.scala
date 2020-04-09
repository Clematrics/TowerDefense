import engine.core.Renderer
import engine.loaders.SpriteLoader
import engine.helpers.CellPoint

import java.awt._
import java.awt.geom.{AffineTransform, Point2D}

import scala.collection.mutable.ArrayBuffer

/**
  * Helper class to select the positions of the towers in the entity
  * DualTower.
  */
class HalfDualTower extends TowerCompound("DualTower", 2, 10) {
	cost = 80
	def getName(): String = {
		return "Dual Tower"
	}

	def tick(time: Double, delta: Double) : Unit = {
		
	}

	def render(time: Double, delta: Double): Unit = {
		val s:Image = SpriteLoader.fromResource("dualtour.png")
		val sPos = pos.toScreenPosition
		Renderer.groundEntities.drawImage(s, new AffineTransform(0.5, 0, 0, 0.5, sPos.x, sPos.y - 20), null)
	}

	override def makeTower(compounds: ArrayBuffer[TowerCompound]): Tower = {
		var r = new DualTower
		r.posA = compounds(0).pos
		r.posB = compounds(1).pos
		r
	}
}

/**
  * The DualTower is in fact two towers deploying a yellow laser between
  * them in order to harm all enemies passing through it.
  * 
  */
class DualTower extends RadiusTower(0, 250, 20) {
	cost = 80

	var posA: CellPoint = /*pos +*/new CellPoint(2,-4)
	var posB: CellPoint = /*pos +*/ new CellPoint(-1,4)

	def getName(): String = {
		return "Dual Tower"
	}

	def tick(time: Double, delta: Double) : Unit = {
		if (lastShot + reload < time) {
			//Computing the enemies' distance from the laser
			val distABsq = Math.pow(posB.x - posA.x, 2) + Math.pow(posB.y - posA.y, 2)
			var enemiesNear : Array[Enemy] = Game.getEnemiesWhere(e =>
			{
				val distAPsq = Math.pow(e.pos.x - posA.x, 2) + Math.pow(e.pos.y - posA.y, 2)
				val scalarAPAB = (e.pos.x-posA.x)*(posB.x-posA.x) + (e.pos.y-posA.y)*(posB.y-posA.y)
				val scalarBABP = (posA.x-posB.x)*(e.pos.x-posB.x) + (posA.y-posB.y)*(e.pos.y-posB.y)
				val delta = Math.sqrt(distAPsq - Math.pow(scalarAPAB,2) / distABsq)
				delta < 1 && scalarAPAB > 0 && scalarBABP > 0
			})

			for (e <- enemiesNear) {
				e.takeDamage(pow)
			}

			if (enemiesNear.length > 0)
				lastShot = time
				
				
		}
	}

	def render(time: Double, delta: Double): Unit = {
		val s:Image = SpriteLoader.fromResource("dualtour.png")

		Renderer.flyingEntities.setColor(Color.YELLOW)
        Renderer.flyingEntities.setStroke(new BasicStroke(6))
        val sa = posA.toScreenPosition
        val sb = posB.toScreenPosition
		Renderer.groundEntities.drawImage(s, new AffineTransform(0.5, 0, 0, 0.5, sa.x, sa.y - 20), null)
		Renderer.groundEntities.drawImage(s, new AffineTransform(0.5, 0, 0, 0.5, sb.x, sb.y - 20), null)
		Renderer.flyingEntities.drawLine(sa.x + 15, sa.y, sb.x + 15, sb.y)
	}
}