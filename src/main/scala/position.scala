import java.awt.Point
import java.awt.geom.Point2D

/**
  * ScreenPosition is a pair of Int representing a position on the screen, in pixels
  *
  * @param x
  * @param y
  */
class ScreenPosition(xIn: Int, yIn: Int) extends Point2D {
	var x = xIn
	var y = yIn

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

	def toCellPosition(): CellPosition = {
		return new CellPosition(x / 24, y / 24)
	}

	def this(p: Point) = {
		this(p.getX.toInt, p.getY.toInt)
	}

	def +(p: ScreenPosition): ScreenPosition = {
		return new ScreenPosition(x + p.x, y + p.y)
	}

	def apply(p: (Int, Int)): ScreenPosition = {
		return new ScreenPosition(p._1, p._2)
	}
}

/**
  * CellPosition is a pair of Double to represent the position of an entity in the level
  *
  * @param x
  * @param y
  */
class CellPosition (xIn: Double, yIn: Double) extends Point2D {
	var x = xIn
	var y = yIn

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
	
	def move(offsetx: Double, offsety: Double): Unit = {
		x += offsetx
		y += offsety
	}

	def toScreenPosition(): ScreenPosition = {
		return new ScreenPosition((x * 24).toInt, (y * 24).toInt)
	}
}