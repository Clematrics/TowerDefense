import scala.collection.mutable._
/**
  * This class embodies the successive attack waves coming from enemies.
  * Soon to be removed?
  */
abstract class Wave {
	val wave: ArrayBuffer[Tuple3[Double, Int, String]]
}