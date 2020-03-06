import java.awt.Point
import java.awt.geom.Point2D

/**
  * ScreenPoint is a pair of Int representing a position on the screen, in pixels
  *
  * @param x
  * @param y
  */
class ScreenPoint(xIn: Int, yIn: Int) extends Point(xIn, yIn) {
	def this(p: Point) = {
		this(p.getX.toInt, p.getY.toInt)
	}

	def toCellPoint(): CellPoint = {
		return new CellPoint(x / 24, y / 24)
	}

	def +(p: ScreenPoint): ScreenPoint = {
		return new ScreenPoint(x + p.x, y + p.y)
	}

	def +=(p: ScreenPoint): Unit = {
		x += p.x
		y += p.y
	}

	def -(p: ScreenPoint): ScreenPoint = {
		return new ScreenPoint(x - p.x, y - p.y)
	}

	def *(p: ScreenPoint): ScreenPoint = {
		return new ScreenPoint(x * p.x, y * p.y)
	}

	def /(p: ScreenPoint): ScreenPoint = {
		return new ScreenPoint(x / p.x, y / p.y)
	}

	def apply(xIn: Int, yIn: Int): ScreenPoint = {
		return new ScreenPoint(xIn, yIn)
	}
}