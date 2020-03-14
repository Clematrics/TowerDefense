/**
  * Represents an abstract vector
  */
trait VectorSpaceElement[T] {
	def plus(a: T, b: T): T
	def times(a: T, b:Double): T

	class Ops(lhs: T) {
		def +(rhs: T) = plus(lhs, rhs)
		def *(rhs: Double) = times(lhs, rhs)
	}
	
	implicit def mkNumericOps(lhs: T): Ops = new Ops(lhs)
}

/**
  * This class manages regular translation from one point to another.
  *
  * @param dur	Length of the animation
  * @param aIn	Start position
  * @param bIn	End position
  */
class LinearAnimation[T: VectorSpaceElement](dur: Double, aIn: T, bIn: T) extends Animation[T](dur) {
	val a = aIn
	val b = bIn

	def value(): T = {
		val t = implicitly[VectorSpaceElement[T]]
		import t._
		(a * (1 - position) + b * position)
	}
}
