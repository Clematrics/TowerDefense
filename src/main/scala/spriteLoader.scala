import javax.swing.ImageIcon
import java.awt.{Color, Font, Graphics2D, Image, RenderingHints}
import java.awt.font.{FontRenderContext, TextLayout}
import java.awt.image.BufferedImage

object SpriteLoader {
	def fromResource(str: String): Image = {
		return new ImageIcon(getClass.getResource(str)).getImage()
	}

	// inspired from https://stackoverflow.com/questions/8281886/stretch-a-jlabel-text/8282330#8282330
	def fromString(str: String, width: Int, height: Int, fontsize: Int): Image = {
		val font = Font.createFont(Font.TRUETYPE_FONT, getClass.getResourceAsStream("/Some Time Later.otf")).deriveFont(Font.PLAIN, fontsize)
		val frc = new FontRenderContext(null, true, true);
		val layout = new TextLayout(str, font, frc);
		val r = layout.getPixelBounds(null, 0, 0);
		val bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		val g2d = bi.getGraphics.asInstanceOf[Graphics2D];
		g2d.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(new Color(0, 0, 0, 0));
		g2d.fillRect(0, 0, bi.getWidth(), bi.getHeight());
		g2d.setColor(new Color(255, 255, 255));
		layout.draw(g2d, (width - r.width) / 2  - r.x, (height - r.height) / 2 - r.y);
		g2d.dispose();
		return bi;
	}
}