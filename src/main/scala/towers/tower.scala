/**
	* The abstract representation of a tower.
	*/
abstract class Tower extends Entity {
	var pos: CellPoint = _

	def getName(): String
}