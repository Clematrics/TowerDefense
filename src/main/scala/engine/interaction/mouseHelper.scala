package engine.interaction

import engine.Cst
import engine.core.Renderer
import engine.helpers.ScreenPoint

import java.awt.Point

/**
  * Computes mouse positions depending on the current dimensions of the screen.
  */
object MouseHelper {
	def fromMouse(p: Point): ScreenPoint = {
		Renderer.computeAdjustments
		val realX = (p.getX - Renderer.offsetX) * Cst.layerResolutionWidth  / Renderer.w
		val realY = (p.getY - Renderer.offsetY) * Cst.layerResolutionHeight / Renderer.h
		return new ScreenPoint(realX.toInt, realY.toInt)
	}
}