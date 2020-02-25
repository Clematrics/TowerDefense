import scala.swing.event._
import java.awt.{Dimension, Graphics2D, Point}
import scala.collection.mutable.ArrayBuffer
import java.awt.geom.AffineTransform

class CampaignMenu extends View { outer =>
	reactions += {
		case MouseMoved(_, point, _) =>
			for(b <- buttons) b.onMoved(point)
			for(b <- campaignButtons) b.onMoved(point)
		case MouseReleased(_, point, _, _, _) =>
			for(b <- buttons) b.onRelease(point)
			for(b <- campaignButtons) b.onRelease(point)
	}

	var campaign = Campaigns.currentCampaign
	private def loadCampaignButtons(): List[Button] = {
		val marginX = 240
		val marginY = 200
		val offsetX = 200
		val offsetY = 200
		val inARow = 5

		val arr: ArrayBuffer[Button] = new ArrayBuffer
		for((map,i) <- campaign.rounds.view.zipWithIndex) {
			arr += new Button(new Point(marginX + offsetX * (i % inARow), marginY + offsetY * (i / inARow)), new Dimension(150, 150)) {
				// println(f"$i position $position")
				sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
				action = () => {
					GameStatus.map = map
					GamePanel.changeView("DefensePhase")
				}
			}
		}
		return arr.toList
	}
	var campaignButtons = loadCampaignButtons()


	val buttons : List[Button] = List(
		new Button(new Point(80, 40), new Dimension(150, 60)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("go back", 150, 60, 30)
			action = () => {
				GamePanel.changeView("MainMenu")
			}
		},
		new Button(new Point(60, 360), new Dimension(100, 500)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString("<", 100, 500, 100)
			action = () => {
				campaign = Campaigns.previousCampaign
				campaignButtons = loadCampaignButtons()
			}
		},
		new Button(new Point(1220, 360), new Dimension(100, 500)) {
			sprite_back    = SpriteLoader.fromResource("menuButtonLarge.png")
			sprite_front   = SpriteLoader.fromString(">", 100, 500, 100)
			action = () => {
				campaign = Campaigns.nextCampaign
				campaignButtons = loadCampaignButtons()
			}
		}
	)


	def render(g: Graphics2D, running_for: Double, delta: Double): Unit = {
		val spriteTitle = SpriteLoader.fromString(campaign.name, 1280, 100, 57)
		g.drawImage(spriteTitle, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		for(b <- buttons) {
			b.render(g, running_for, delta)
		}
		for(b <- campaignButtons) {
			b.render(g, running_for, delta)
		}
	}
}

