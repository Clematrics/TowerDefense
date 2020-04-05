package engine.animation

/**
 * This class manages fundamental properties and behaviour of animations
 */
abstract class Animation[T](dur: Double) {
	private var _run: Boolean = true
	def run = _run
	def run_=(b: Boolean) { _run = b }

	private   var loop: Boolean    = false  // between 0 and 1
	protected var position: Double = 0      // between 0 and 1
	private   var duration: Double = dur    // in milliseconds
	private   var direction: Double = 1

	def tick(running_for: Double, delta: Double): Unit = {
		if (_run) {
			position += direction * delta
			if (loop) position %= 1
			else position = position.max(0).min(1)
		}
	}

	def value(): T

	def set(pos: Double): Unit = { position = pos.max(0).min(1) }
	def reset(): Unit          = { position = 0 }

	def forward(): Unit  = { direction =   direction.abs }
	def backward(): Unit = { direction = - direction.abs }
	def reverse(): Unit  = { direction *= -1 }
}