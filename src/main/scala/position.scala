import java.awt.Point
import java.awt.geom.Point2D

/**
  * ScreenPoint is a pair of Int representing a position on the screen, in pixels
  *
  * @param x
  * @param y
  */
class ScreenPoint(xIn: Int, yIn: Int) extends Point2D {
	var x: Int = xIn
	var y: Int = yIn

	def this(p: Point) = {
		this(p.getX.toInt, p.getY.toInt)
	}

	def getX(): Double = {
		return x
	}

	def getY(): Double = {
		return y
	}

	def setLocation(xx: Double, yy: Double): Unit = {
		x = xx.toInt
		y = yy.toInt
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

/**
  * CellPoint is a pair of Double to represent the position of an entity in the level
  *
  * @param x
  * @param y
  */
class CellPoint(xIn: Double, yIn: Double) extends Point2D {
	var x: Double = xIn
	var y: Double = yIn

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