import scala.collection.mutable._
import java.awt.Image
import java.awt.image.BufferedImage

object ResourcesManager {
	val images = HashMap[String, Image]()

	def isRegisteredImage(str: String): Boolean = {
		return images.contains(str)
	}

	def registerImage(str: String, img: Image): Unit = {
		images.+=(str -> img)
	}

	def getImage(str: String): Option[Image] = {
		return images.get(str)
	}
}