import java.awt.geom.AffineTransform

import engine.core.{GamePanel, Renderer, View}
import engine.interaction.{Button, MouseHelper}
import engine.loaders.SpriteLoader
import  engine.Cst
import scala.swing.event.KeyTyped
import java.awt.{Dimension, Point}

import scala.collection.mutable.ArrayBuffer
class CreditsView extends View { outer =>
	reactions += {
		case KeyTyped(_, ' ', _, _) =>
			GamePanel.changeView("MainMenu")
	}

	val spriteTitle = SpriteLoader.fromString("TowerDef[ENS]e, Programmation Project #2",
		640,
		28)

	override def render(time: Double, delta: Double): Unit = {
		Renderer.drawOnTextLayerCentered(spriteTitle,320, 0)
	}
}
