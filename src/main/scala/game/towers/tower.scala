import engine.core.Entity
import engine.helpers.CellPoint

/**
 * The abstract representation of a tower.
 */
abstract class Tower extends Entity {
	var pos: CellPoint = _
	var cost: Int = _

	def getName(): String
}