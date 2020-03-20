import scala.collection.mutable.ArrayBuffer

/**
  * This is the base class for all towers composed by two or more
  * positions.
  */
abstract class TowerCompound(className: String, number: Int, maxDistFromLast: Double) extends Tower {
	var name = className
	var nb = number
	var dist = maxDistFromLast
	
	def makeTower(compounds: ArrayBuffer[TowerCompound]): Tower
}