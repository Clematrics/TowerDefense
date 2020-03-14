import scala.swing.event._
import java.awt.{Dimension, Graphics2D, Point}
import scala.collection.mutable.ArrayBuffer
import java.awt.geom.AffineTransform

/**
  * The window to select a campaign.
  *
  */
class CampaignMenu extends View { outer =>
	val fixedButtons: ArrayBuffer[Button] = ArrayBuffer(
		new Button(new Point(80, 40), new Dimension(150, 60)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("go back", 150, 30)
			action = () => {
				GamePanel.changeView("MainMenu")
			}
		},
		new Button(new Point(60, 360), new Dimension(100, 500)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("<", 100, 100)
			action = () => {
				campaign = Campaigns.previousCampaign
				campaignButtons = loadCampaignButtons()
			}
		},
		new Button(new Point(1220, 360), new Dimension(100, 500)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString(">", 100, 100)
			action = () => {
				campaign = Campaigns.nextCampaign
				campaignButtons = loadCampaignButtons()
			}
		}
	)

	var campaign = Campaigns.currentCampaign
	private def loadCampaignButtons(): Unit = {
		val marginX = 240
		val marginY = 200
		val offsetX = 200
		val offsetY = 200
		val inARow = 5

		val arr: ArrayBuffer[Button] = new ArrayBuffer
		for((map,i) <- campaign.rounds.view.zipWithIndex) {
			arr += new Button(new Point(marginX + offsetX * (i % inARow), marginY + offsetY * (i / inARow)), new Dimension(150, 150)) {
				spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
				spriteFront   = SpriteLoader.fromString(map.name, 150, 30)
				action = () => {
					Game.map = map
					GamePanel.changeView("DefensePhase")
				}
			}
		}
		buttons = fixedButtons ++ arr
	}
	var campaignButtons = loadCampaignButtons()


	def render(time: Double, delta: Double): Unit = {
		val spriteTitle = SpriteLoader.fromString(campaign.name, 1280, 57)
		Renderer.userInterface.drawImage(spriteTitle, new AffineTransform(1, 0, 0, 1, 640 - spriteTitle.getWidth(null) / 2, 0), null)
		for(b <- buttons) {
			b.render(time, delta)
		}
	}
}

