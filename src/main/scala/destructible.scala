trait Destructible {
  def isAlive(): Boolean
  def takeDamage(dmg: Int)
}