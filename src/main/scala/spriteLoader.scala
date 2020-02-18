import java.awt.Image
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import java.awt.Graphics2D
import java.awt.Font
import java.awt.Color
import java.awt.RenderingHints
import java.awt.font.{FontRenderContext, TextLayout}

object SpriteLoader {
	def fromResource(str: String): Image = {
		return new ImageIcon(getClass.getResource(str)).getImage()
	}

	def fromString(str: String): Image = {
		val font = new Font(Font.SERIF, Font.PLAIN, 256);
        val frc = new FontRenderContext(null, true, true);
        val layout = new TextLayout(str, font, frc);
        val r = layout.getPixelBounds(null, 0, 0);
        System.out.println(r);
        val bi = new BufferedImage(
            r.width + 1, r.height + 1, BufferedImage.TYPE_INT_ARGB);
        val g2d = bi.getGraphics.asInstanceOf[Graphics2D];
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(255, 255, 255, 0));
        g2d.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        g2d.setColor(new Color(255, 255, 255));
        layout.draw(g2d, 0, -r.y);
        g2d.dispose();
        return bi;
	}
}