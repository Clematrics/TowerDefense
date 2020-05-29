package engine.helpers

import java.awt.Point
import java.awt.geom.Point2D
import engine.Cst

/**
  * CellPoint is a pair of Double to represent the position of an entity in the level
  *
  * @param x Abscissa
  * @param y Ordinate
  */
class CellPoint(var x: Double, var y: Double) extends Point2D {
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
		return new ScreenPoint((x * Cst.cellSize).toInt, (y * Cst.cellSize).toInt)
	}

	def nearestMiddle(): CellPoint = {
		// take into account points that are outside of the map
		val nearestX = 0.5 max (x.floor + 0.5) min (Cst.mapWidth - 0.5)
		val nearestY = 0.5 max (y.floor + 0.5) min (Cst.mapHeight - 0.5)
		return new CellPoint(nearestX, nearestY)
	}

	def neighborCells(): List[CellPoint] = {
		return List(
			new CellPoint(-1, -1),
			new CellPoint(-1,  0),
			new CellPoint(-1,  1),
			new CellPoint( 0, -1),
			new CellPoint( 0,  1),
			new CellPoint( 1, -1),
			new CellPoint( 1,  0),
			new CellPoint( 1,  1)
		).map(
			_ + nearestMiddle()
		).filter( c =>
			0 < c.x && c.x < Cst.mapWidth && 0 < c.y && c.y < Cst.mapHeight
		)
	}

	def mirror(): CellPoint = {
		return new CellPoint(Cst.mapWidth.toDouble - x - 1, Cst.mapHeight.toDouble - y - 1)
	}

	def insideBox(a: CellPoint, b: CellPoint): Boolean = {
		return (a.x min b.x) <= x && (a.y min b.y) <= y && x <= (a.x max b.x) && y <= (a.y max b.y)
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

	def /(z: Double): CellPoint = {
		return new CellPoint(x / z, y / z)
	}
}