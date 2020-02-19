import javax.swing.ImageIcon
import java.awt.{Color, Font, Graphics2D, Image, RenderingHints}
import java.awt.font.{FontRenderContext, TextLayout}
import java.awt.image.BufferedImage
import java.text.AttributedCharacterIterator
import java.awt.font.LineBreakMeasurer
import java.text.AttributedString
import java.awt.font.TextAttribute

object SpriteLoader {
	def fromResource(str: String): Image = {
		return new ImageIcon(getClass.getResource(str)).getImage()
	}

	// inspired from https://stackoverflow.com/questions/8281886/stretch-a-jlabel-text/8282330#8282330
	def fromString(str: String, width: Int, height: Int, fontsize: Int): Image = {
		val font = Font.createFont(Font.TRUETYPE_FONT, getClass.getResourceAsStream("/Some Time Later.otf")).deriveFont(Font.PLAIN, fontsize)
		val frc = new FontRenderContext(null, true, true)
		val layout = new TextLayout(str, font, frc)
		val r = layout.getPixelBounds(null, 0, 0)
		val bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		val g2d = bi.getGraphics.asInstanceOf[Graphics2D]
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
		g2d.setColor(new Color(0, 0, 0, 0))
		g2d.fillRect(0, 0, bi.getWidth(), bi.getHeight())
		g2d.setColor(new Color(255, 255, 255))
		layout.draw(g2d, (width - r.width) / 2  - r.x, (height - r.height) / 2 - r.y)
		g2d.dispose()
		return bi
	}

	def tooltip(str: String): Image = {
		val font = Font.createFont(Font.TRUETYPE_FONT, getClass.getResourceAsStream("/Some Time Later.otf")).deriveFont(Font.PLAIN, 20)
		val frc = new FontRenderContext(null, true, true)

		// inspired from https://docs.oracle.com/javase/tutorial/2d/text/drawmulstring.html
		val attrString = new AttributedString(str)
		attrString.addAttribute(TextAttribute.FONT, font)
		val paragraph  = attrString.getIterator
		val paragraphStart = paragraph.getBeginIndex()
		val paragraphEnd = paragraph.getEndIndex()
		val lineMeasurer = new LineBreakMeasurer(paragraph, frc)

		// Set break width to width of Component.
		val breakWidth = 200.0f
		var width      = 0.0f
		var height     = 0.0f

		// Get lines from until the entire paragraph
		// has been displayed.
		lineMeasurer.setPosition(paragraphStart);
		while (lineMeasurer.getPosition() < paragraphEnd) {
			val layout = lineMeasurer.nextLayout(breakWidth)
			height += layout.getAscent + layout.getDescent + layout.getLeading
			width = width.max(layout.getBounds.getWidth.toFloat)
		}

		val bi = new BufferedImage(width.ceil.toInt, height.ceil.toInt, BufferedImage.TYPE_INT_ARGB)
		val g2d = bi.getGraphics.asInstanceOf[Graphics2D]
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
		g2d.setColor(new Color(0, 0, 0, 0))
		g2d.fillRect(0, 0, bi.getWidth(), bi.getHeight())
		g2d.setColor(new Color(255, 255, 255))

		// Get lines from until the entire paragraph
		// has been displayed.
		var drawPosY   = 0.0f
		lineMeasurer.setPosition(paragraphStart);
		while (lineMeasurer.getPosition() < paragraphEnd) {
			val layout = lineMeasurer.nextLayout(breakWidth)
			drawPosY += layout.getAscent
			layout.draw(g2d, 0, drawPosY)
			drawPosY += layout.getDescent + layout.getLeading
		}

		g2d.dispose()
		return bi
	}
}