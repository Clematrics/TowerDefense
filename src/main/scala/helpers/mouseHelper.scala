import java.awt.Point

object MouseHelper {
	def fromMouse(p: Point): ScreenPoint = {
		Renderer.computeAdjustments
		val realX = (p.getX - Renderer.offsetX) * Cst.layerResolutionWidth  / Renderer.w
		val realY = (p.getY - Renderer.offsetY) * Cst.layerResolutionHeight / Renderer.h
		return new ScreenPoint(realX.toInt, realY.toInt)
	}
}