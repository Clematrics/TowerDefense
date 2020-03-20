/**
  * This is the base class for all towers composed by two or more
  * positions.
  */
abstract class TowerCompound(className: String, number: Int, maxDistFromLast: Double) extends Tower {
	var name = className
	var n = number
	var dist = maxDistFromLast
}