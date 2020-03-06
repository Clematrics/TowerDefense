import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import java.awt.BasicStroke
import scala.collection.mutable.ArrayBuffer

/**
  * This class is the phase of the game (seen as a Level class) where
  * enemies attack and towers defend the player.
  */
class AttackPhase extends View { outer =>
	val r = scala.util.Random
	var time = -5.0
	var wave: ArrayBuffer[Tuple3[Double, Int, String]] = ArrayBuffer(Game.map.wave: _*)

	buttons ++= ArrayBuffer(
		new Button(new Point(1200, 40), new Dimension(150, 60)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("nevermind", 150, 30)
			action = () => {
				Game.reset
				GamePanel.changeView("DefensePhase")
			}
		}
	)

	override def tick(running_for: Double, delta: Double): Unit = {
		if (Game.health <= 0) {
			GamePanel.changeView("LoseMenu")
		}

		time += delta

		while (wave.length > 0 && wave.head._1 * 1000 <= time) {
			val (t, i, name) = wave.head

			val constr = Class.forName(name).getConstructor()
			val enemy: Enemy = constr.newInstance().asInstanceOf[Enemy]

			if (enemy.isInstanceOf[MovingEnemy]) {
				val menemy = enemy.asInstanceOf[MovingEnemy]
				//Random choice of the target on the portal segment for spawn location
				val cp = Game.map.checkpoints(i)
				menemy.pos = new CellPoint(cp.a.x + r.nextFloat * (cp.b.x - cp.a.x), cp.a.y + r.nextFloat * (cp.b.y - cp.a.y))
				menemy.targetedCheckpoint = cp.next

				//Random choice of the target on the portal segment for destination
				val cpp = Game.map.checkpoints(cp.next)
				menemy.targetedCellPoint = new CellPoint(cpp.a.x + r.nextFloat * (cpp.b.x - cpp.a.x), cpp.a.y + r.nextFloat * (cpp.b.y - cpp.a.y))
			}

			Game.entities += enemy.asInstanceOf[Entity]
			wave.remove(0)
		}

		for (e <- Game.entities)
			e.tick(running_for, delta)

		Game.entities = Game.entities.filter((p: Entity) => p.valid)

		if (wave.length == 0 && Game.entities.filter(_.isInstanceOf[Enemy]).length == 0) {
			GamePanel.changeView("WinMenu")
		}
	}

	/**
	  * Rendering of the game, with the map and all entities
	  *
	  * @param g A Graphics2D object representing the drawing surface
	  * @param running_for Total time the game has been running
	  * @param delta
	  */
	def render(g: Graphics2D, running_for: Double, delta: Double): Unit = {
		RenderLayers.background.drawImage(Game.map.mapImg, new AffineTransform(6, 0, 0, 6, 0, 0), null)

		if (RenderLayers.debugMode) {
			RenderLayers.background.drawImage(Game.map.mapLayout, new AffineTransform(24, 0, 0, 24, 0, 0), null)
			for(cp <- Game.map.checkpoints) {
				val stroke = new BasicStroke(2)
				RenderLayers.debug.setStroke(stroke)
				RenderLayers.debug.setColor(new Color(255, 0, 255, 255))
				val spa = cp.a.toScreenPosition
				val spb = cp.b.toScreenPosition
				RenderLayers.debug.drawLine(spa.x, spa.y, spb.x, spb.y)
			}
		}

		for(e <- Game.entities) {
			e.render(g)
		}

		for(b <- buttons) {
			b.render(g, running_for, delta)
		}

		RenderLayers.userInterface.setColor(Color.BLACK)
		RenderLayers.userInterface.fillRect(1220, 100, 40, 600)
		RenderLayers.userInterface.setColor(Color.RED)
		RenderLayers.userInterface.fillRect(1220, 100 + ((100 - Game.health) * 600 / 100), 40, Game.health * 600 / 100)

		if (RenderLayers.debugMode) {
			RenderLayers.debug.setColor(Color.PINK)
			RenderLayers.debug.drawString(f"$time%.1f ms", 0, 30)
		}
	}
}