package engine.helpers

import scala.swing.event.Event
import scala.swing.Publisher
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import System.nanoTime

/**
  * Tick
  *
  * @param time Time in milliseconds the timer is running for
  * @param delta Time in milliseconds since the last tick
  */
case class Tick(time: Double, delta: Double) extends Event

/**
  * This class handles the synchronized rendering of the game.
  *
  * Inspired from https://stackoverflow.com/questions/6825729/how-to-write-a-scala-wrapper-for-javax-swing-timer
  */
abstract class Timer extends Publisher {
	private val timer = this

	private var startTime: Long    = 0
	private var lastTickTime: Long = 0
	private val tick = new AbstractAction(){
		def actionPerformed(e:ActionEvent) = {
			var currentTime = System.nanoTime
			var time  = (currentTime - startTime) / 1000000.0
			var delta = (currentTime - lastTickTime) / 1000000.0
			publish(Tick(time, delta))
			lastTickTime = currentTime
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
			startTime = System.nanoTime
		}
		case false =>
			peer.stop()
	}
	val peer = new javax.swing.Timer(_interval, tick)
}