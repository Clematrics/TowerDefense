/**
  * This class embodies the successive attack waves coming from enemies.
  */
abstract class Wave {
    val wave : List[Tuple2[Double, Enemy]]
}