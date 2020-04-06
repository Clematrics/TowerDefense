import engine.core.{GamePanel, Renderer, View}
import engine.loaders.SpriteLoader
import engine.interaction.Button

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
		new Button(new Point(40, 20), new Dimension(75, 30)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("go back", 75, 15)
			action = () => {
				GamePanel.changeView("MainMenu")
			}
		},
		new Button(new Point(30, 180), new Dimension(50, 250)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString("<", 50, 50)
			action = () => {
				campaign = Campaigns.previousCampaign
				campaignButtons = loadCampaignButtons()
			}
		},
		new Button(new Point(610, 180), new Dimension(50, 250)) {
			spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
			spriteFront   = SpriteLoader.fromString(">", 50, 50)
			action = () => {
				campaign = Campaigns.nextCampaign
				campaignButtons = loadCampaignButtons()
			}
		}
	)

	var campaign = Campaigns.currentCampaign
	private def loadCampaignButtons(): Unit = {
		val marginX = 120
		val marginY = 100
		val offsetX = 100
		val offsetY = 100
		val inARow = 5

		val arr: ArrayBuffer[Button] = new ArrayBuffer
		for((map,i) <- campaign.rounds.view.zipWithIndex) {
			if (map.expNeeded <= Game.experience) {
				arr += new Button(new Point(marginX + offsetX * (i % inARow), marginY + offsetY * (i / inARow)), new Dimension(75, 75)) {
					spriteBack    = SpriteLoader.fromResource("menuButtonLarge.png")
					spriteFront   = SpriteLoader.fromString(map.name, 75, 15)
					action = () => {
						Game.map = map
						GamePanel.changeView("DefensePhase")
					}
				}
			}
		}
		buttons = fixedButtons ++ arr
	}
	var campaignButtons = loadCampaignButtons()


	def render(time: Double, delta: Double): Unit = {
		val spriteTitle = SpriteLoader.fromString(campaign.name, 640, 28)
		Renderer.userInterface.drawImage(spriteTitle, new AffineTransform(1, 0, 0, 1, 320 - spriteTitle.getWidth(null) / 2, 0), null)
		for(b <- buttons) {
			b.render(time, delta)
		}
	}
}

