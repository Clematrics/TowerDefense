import java.awt.{Color, Image}
import java.awt.geom.AffineTransform

import engine.core.GamePanel.{ps, view}
import engine.core.Renderer
import engine.loaders.SpriteLoader
import engine.helpers.Delay

import scala.collection.mutable

/**
  * The ThunderTower launches every 10 seconds a 'freeze ray' that
  * freezes all enemies nearby.
  */
class ThunderTower extends RadiusTower(5, 10000, 1) {
  def getName(): String = {
    return "Thunder Tower"
  }

  def tick(time: Double, delta: Double) : Unit = {
    if (lastShot + reload < time) {
      var enemiesNear : Array[MovingEnemy] = Game.getEnemiesAround(pos, radius).map(e => e.asInstanceOf[MovingEnemy])

      var speeds : mutable.HashMap[MovingEnemy, Double] = mutable.HashMap.empty[MovingEnemy, Double]

      for (e <- enemiesNear) {
        speeds += (e -> e.speed)
        e.speed = 0
        Game.entities += new ShockWave(time, this, Color.CYAN)
        lastShot = time
      }

      new Delay(5000, () => {
        for (e <- enemiesNear) {
          e.speed = speeds(e)
        }
      }) { run = true }
    }
  }

  def render(time: Double, delta: Double): Unit = {
    val s:Image = SpriteLoader.fromResource("thundertour.png")
    val sPos = pos.toScreenPosition
    Renderer.groundEntities.drawImage(s, new AffineTransform(1.3, 0, 0, 1.3, sPos.x - 15, sPos.y - 8), null)
  }
}
