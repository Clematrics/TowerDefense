import engine.core.Entity

/**
	* All enemies like monsters implement this trait. It extends
	* Entity and Destructible.
	*/
trait Enemy extends Entity with Destructible {
	def getName(): String
	var damage: Int
	var gold: Int = 0
	var experience: Int = 0
}