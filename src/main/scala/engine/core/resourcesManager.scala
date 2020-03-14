package engine.core

import scala.collection.mutable._
import java.awt.Image
import java.awt.image.BufferedImage

/**
  * This class manages access of resources, making sure an image is
  * loaded from disk at its first use, and stored in RAM for the next
  * times.
  */
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