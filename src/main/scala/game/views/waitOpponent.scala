import engine.core.{GamePanel, Renderer, View}
import engine.loaders.SpriteLoader
import engine.interaction.Button

import scala.swing.event._
import java.awt.{Color, Dimension, Graphics2D, Point}
import java.awt.geom.AffineTransform
import scala.collection.mutable.ArrayBuffer

/**
  * A simple menu to wait for the opponent
  *
  */
class WaitOpponent extends View { outer =>
	def render(time: Double, delta: Double): Unit = {
		val waitImg = SpriteLoader.fromString("Waiting for opponent...", 400, 35)
		Renderer.drawOnTextLayerCentered(waitImg, 320, 110)
	}
}