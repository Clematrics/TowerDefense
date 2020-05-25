import java.awt.geom.AffineTransform

import engine.core.{GamePanel, Renderer, View}
import engine.interaction.{Button, MouseHelper}
import engine.loaders.SpriteLoader
import engine.Cst

import scala.swing.event.KeyTyped
import java.awt.{Dimension, Point}

import scala.collection.mutable.ArrayBuffer

class CreditsView extends View {
	outer =>


	reactions += {
		case KeyTyped(_, ' ', _, _) =>
			GamePanel.changeView("MainMenu")
	}

	val spriteTitle = SpriteLoader.fromString("TowerDef[ENS]e, Programmation Project #2",
		640,
		25)

	val spriteAuthors = SpriteLoader.fromString("Clematrics \n& VBonczak from 411",
		250,
		15)

	val spriteThanks = SpriteLoader.fromString("Under the supervision of        Stefan S. and Matthieu H.",
		500,
		15)

	val spritePhoto = SpriteLoader.fromResource("credits/stefan.jpg")

	val spriteLogo = SpriteLoader.fromResource("credits/varlogo.png")

	var begin = 0.0

	override def render(time: Double, delta: Double): Unit = {
		Renderer.drawOnTextLayerCentered(spriteTitle, 320, 0)

		Renderer.drawOnTextLayerCentered(spriteLogo, 240, 40)
		Renderer.drawOnTextLayer(spriteAuthors, 320, 40)
		Renderer.drawOnTextLayer(spriteThanks, 120, 160)
		Renderer.background.drawImage(spritePhoto, new AffineTransform(.5, 0, 0, .5,
			320, 100), null)

		if (begin == 0.0) {
			begin = time
		} else if (begin > 0 && time - begin > 5000) {
			buttons.append(new Button(new Point(320, 300), new Dimension(70, 30)) {
				spriteBack = SpriteLoader.fromResource("menuButtonLargeVar.png")
				spriteFront = SpriteLoader.fromString("Back", 50, 15)
				action = () => {
					GamePanel.changeView("MainMenu")
				}
			})
			begin = -1
		}

		if (buttons.length > 0) {
			buttons(0).render(time, delta)
		}
	}
}
