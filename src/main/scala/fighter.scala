trait Fighter {
    def getTarget(): Destructible
    def attack(entity: Destructible): Unit
}