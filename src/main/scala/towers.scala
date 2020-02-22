class ProtoTower extends Entity with Tower {
	var pos: CellPosition = new CellPosition(15, 10)

	def getName(): String = {
		return "Chess Tower"
	}

	def tick(running_for: Double, delta: Double) : Unit = {
		
	}

	def render(g: Graphics2D): Unit = {
		val s:Image = SpriteLoader.fromResource("tour.png")
		val sPos = pos.toScreenPosition
		g.drawImage(s, new AffineTransform(0.1, 0, 0, 0.1, sPos.x-40, sPos.y-40), null)	
	}
}