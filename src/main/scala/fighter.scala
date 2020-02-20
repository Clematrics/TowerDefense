/**
  * Entities attacking towers implement this trait.
  */
trait Fighter {
    def getTarget(): Destructible
    def attack(entity: Destructible): Unit
}