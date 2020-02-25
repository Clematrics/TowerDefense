import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import java.awt.BasicStroke
import scala.collection.mutable.ArrayBuffer

/**
  * This class is the phase of the game (seen as a Level class) where
  * enemies attack and towers defend the player.
  */
class AttackPhase extends Level { outer =>
	val r = scala.util.Random
	var time = -5.0
	var wave: ArrayBuffer[Tuple3[Double, Int, String]] = ArrayBuffer(GameStatus.map.wave: _*)
	var entities: ArrayBuffer[Entity] = ArrayBuffer()

	reactions += {
		case MouseMoved(_, point, _) =>
			for(b <- buttons) b.onMoved(point)
		case MouseReleased(_, point, _, _, _) =>
			for(b <- buttons) b.onRelease(point)
	}

	val buttons : List[Button] = List(
		new Button(new Point(1200, 40), new Dimension(150, 60)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("nevermind", 150, 60, 30)
			action = () => {
				GamePanel.changeLevel("DefensePhase")
			}
		}
	)

	override def tick(running_for: Double, delta: Double): Unit = {
		if (GameStatus.health <= 0) {
			GamePanel.changeLevel("LoseMenu")
		}

		time += delta

		while (wave.length > 0 && wave.head._1 * 1000 <= time) {
			val (t, i, name) = wave.head

			val constr = Class.forName(name).getConstructor()
			val enemy: Enemy = constr.newInstance().asInstanceOf[Enemy]

			if (enemy.isInstanceOf[MovingEnemy]) {
				val menemy = enemy.asInstanceOf[MovingEnemy]
				//Random choice of the target on the portal segment for spawn location
				val cp = GameStatus.map.checkpoints(i)
				menemy.pos = new CellPoint(cp.a.x + r.nextFloat * (cp.b.x - cp.a.x), cp.a.y + r.nextFloat * (cp.b.y - cp.a.y))
				menemy.targetedCheckpoint = cp.next

				//Random choice of the target on the portal segment for destination
				val cpp = GameStatus.map.checkpoints(cp.next)
				menemy.targetedCellPoint = new CellPoint(cpp.a.x + r.nextFloat * (cpp.b.x - cpp.a.x), cpp.a.y + r.nextFloat * (cpp.b.y - cpp.a.y))
			}

			entities += enemy.asInstanceOf[Entity]
			wave.remove(0)
		}

		for (e <- entities)
			e.tick(running_for, delta)

		entities = entities.filter((p: Entity) => p.valid)

		if (wave.length == 0 && entities.length == 0) {
			GamePanel.changeLevel("WinMenu")
		}
	}

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

	/**
	  * Rendering of the game, with the map and all entities
	  *
	  * @param g A Graphics2D object representing the drawing surface
	  * @param running_for Total time the game has been running
	  * @param delta
	  */
	def render(g: Graphics2D, running_for: Double, delta: Double): Unit = {
		if (debugMode) {
			g.drawImage(GameStatus.map.mapImg, new AffineTransform(24, 0, 0, 24, 0, 0), null)
			for(cp <- GameStatus.map.checkpoints) {
				val stroke = new BasicStroke(2)
				g.setStroke(stroke)
				g.setColor(new Color(255, 0, 255, 255))
				val spa = cp.a.toScreenPosition
				val spb = cp.b.toScreenPosition
				g.drawLine(spa.x, spa.y, spb.x, spb.y)
			}
		}

		for(e <- entities) {
			e.render(g)
		}

		for(b <- buttons) {
			b.render(g, running_for, delta)
		}

		g.setColor(Color.BLACK)
		g.fillRect(1220, 100, 40, 600)
		g.setColor(Color.RED)
		g.fillRect(1220, 100 + ((100 - GameStatus.health) * 600 / 100), 40, GameStatus.health * 600 / 100)

		if (debugMode) {
			g.setColor(Color.PINK)
			g.drawString(f"$time%.1f ms", 0, 30)
		}
	}
}