import scala.swing.event.Event
import scala.swing.Publisher
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import System.nanoTime

/**
  * Tick
  *
  * @param running_for Time in milliseconds the timer is running for
  * @param delta Time in milliseconds since the last tick
  */
case class Tick(running_for: Double, delta: Double) extends Event

/**
  * This class handles the synchronized rendering of the game.
  *
  * Inspired from https://stackoverflow.com/questions/6825729/how-to-write-a-scala-wrapper-for-javax-swing-timer
  */
abstract class Timer extends Publisher {
	private val timer = this

	private var start_time: Long     = 0
	private var last_tick_time: Long = 0
	private val tick = new AbstractAction(){
		def actionPerformed(e:ActionEvent) = {
			var current_time = System.nanoTime
			var running_for  = (current_time - start_time) / 1000000.0
			var delta        = (current_time - last_tick_time) / 1000000.0
			publish(Tick(running_for, delta))
			last_tick_time = current_time
		}
	}
	
	private var _interval = 1000
	def interval:Int = _interval
	def interval_=(i:Int):Unit = {
		_interval = i
		peer.setDelay(i)
	}

	private var _repeat = true
	def repeat:Boolean = _repeat
	def repeat_=(f:Boolean):Unit = {
		_repeat = f
		peer.setRepeats(_repeat)
	}

	private var _run = false
	def run:Boolean = _run
	def run_=(f:Boolean):Unit = {
		_run = f
		runStop(f)
	}

	private def runStop(f:Boolean) = f match {
		case true => {
			peer.start()
			start_time = System.nanoTime
		}
		case false =>
			peer.stop()
	}
	val peer = new javax.swing.Timer(_interval, tick)
}