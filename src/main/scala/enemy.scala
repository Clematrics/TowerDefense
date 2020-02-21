/**
  * All enemies like monsters implement this trait. It extends
  * Entity and Destructible.
  */
trait Enemy extends Entity with Destructible {
	def getName(): String
}