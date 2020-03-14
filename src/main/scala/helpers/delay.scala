import java.awt.event.ActionListener
import java.awt.event.ActionEvent

/**
  * This class is used to execute an action after a specified amount of time.
  *
  * @param milli	The delay
  * @param f		The function to run
  */
class Delay(milli: Int, f: () => Unit) {
    val peer = new javax.swing.Timer(milli, new ActionListener {
        def actionPerformed(x: ActionEvent): Unit = {
			f()
        }
    })
    peer.setRepeats(false)

    private var _run = false
	def run:Boolean = _run
	def run_=(f:Boolean):Unit = {
		_run = f
		peer.start
	}
}