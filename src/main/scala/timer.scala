import scala.swing.event.Event
import scala.swing.Publisher
import scala.swing.Reactions
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

case class Tick(source: Timer) extends Event
// case class Timeout(source: Timer) extends Event

abstract class Timer extends Publisher {
	private val timer = this
	// private var counter = 0
	private val tick = new AbstractAction(){
		def actionPerformed(e:ActionEvent) = {
			// reactions(Tick(timer))
			publish(Tick(timer))
    		// if(_repeats > 0){
			// 	counter -= 1
			// 	if(counter == 0){
			// 		run = false
			// 		reactions(Timeout(timer))
			// 	}
			// }
		}
	}

	// override val reactions: Reactions = new Reactions.Impl
	private var _interval = 1000
	def interval:Int = _interval
	def interval_=(i:Int):Unit = {
		_interval = i
		peer.setDelay(i)
	}

	// private var _repeats = -1
	// def repeats:Int = _repeats
	// def repeats_=(i:Int):Unit = {
	// 	_repeats = i
	// 	counter = i
	// }

	private var _run = false
	def run:Boolean = _run
	def run_=(f:Boolean):Unit = {
		_run = f
		runStop(f)
	}

	private def runStop(f:Boolean) = f match {
		case true => {
			// counter = _repeats
			// if(counter != 0) {
				peer.start()
			// } else {
				// reactions(Timeout(timer))
			// }
		}
		case false =>
			peer.stop()
	}
	val peer = new javax.swing.Timer(_interval, tick)
	peer.setRepeats(true)
}