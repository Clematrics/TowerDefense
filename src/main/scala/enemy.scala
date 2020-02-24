/**
	* All enemies like monsters implement this trait. It extends
	* Entity and Destructible.
	*/
trait Enemy extends Entity with Destructible {
	var targetedCheckpoint: Int = -2
	var targetedCellPoint: CellPosition = new CellPosition(0, 0)
	var pos: CellPosition = new CellPosition(0, 0)
	def getName(): String
}