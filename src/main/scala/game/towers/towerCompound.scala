import scala.collection.mutable.ArrayBuffer
import engine.helpers.{CellPoint, ScreenPoint}

/**
  * This is the base class for all towers composed by two or more
  * positions.
  */
abstract class TowerCompound(className: String, number: Int, maxDistFromLast: Double) extends Tower {
	var name = className
	var nb = number
	var dist = maxDistFromLast
	
	def makeTower(compounds: ArrayBuffer[TowerCompound]): Tower
	
	def isValidDistance(compounds: ArrayBuffer[TowerCompound], curPos: CellPoint): Boolean = {
		var valid = true
		for (t <- compounds) {
			valid = valid && (curPos.distance(t.pos) < dist)
		}
		valid
	}
}