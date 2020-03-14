import java.awt.Point
import java.awt.geom.Point2D

/**
  * CellPoint is a pair of Double to represent the position of an entity in the level
  *
  * @param x Abscissa
  * @param y Ordinate
  */
class CellPoint(xIn: Double, yIn: Double) extends Point2D {
	var x: Double = xIn
	var y: Double = yIn

	def this(p: CellPoint) = {
		this(p.x, p.y)
	}

	def getX(): Double = {
		return x
	}

	def getY(): Double = {
		return y
	}

	def setLocation(xx: Double, yy: Double): Unit = {
		x = xx
		y = yy
	}

	def toScreenPosition(): ScreenPoint = {
		return new ScreenPoint((x * 24).toInt, (y * 24).toInt)
	}

	def +(p: CellPoint): CellPoint = {
		return new CellPoint(x + p.x, y + p.y)
	}

	def -(p: CellPoint): CellPoint = {
		return new CellPoint(x - p.x, y - p.y)
	}

	def +=(p: CellPoint): Unit = {
		x += p.x
		y += p.y
	}

	def *(p: CellPoint): CellPoint = {
		return new CellPoint(x * p.x, y * p.y)
	}

	def *(z: Double): CellPoint = {
		return new CellPoint(x * z, y * z)
	}

	def /(p: CellPoint): CellPoint = {
		return new CellPoint(x / p.x, y / p.y)
	}

	def apply(xIn: Double, yIn: Double): CellPoint = {
		return new CellPoint(xIn, yIn)
	}
}