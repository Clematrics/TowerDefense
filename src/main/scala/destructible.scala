/**
  * This trait is implemented by all objects that can be destructed, like
  * some kinds of tower and monsters.
  */
trait Destructible {
	def isAlive(): Boolean
	def takeDamage(dmg: Int)
}