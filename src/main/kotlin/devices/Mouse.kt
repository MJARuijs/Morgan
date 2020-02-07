package devices

import java.util.*
import kotlin.collections.HashSet

class Mouse {

    private data class Event(val button: Button, val action: Action)

    private val events = ArrayDeque<Event>()

    private val pressed = HashSet<Button>()
    private val released = HashSet<Button>()
    private val down = HashSet<Button>()

    internal fun post(button: Button, action: Action) = events.push(Event(button, action))

    var x = 0.0
        internal set

    var y = 0.0
        internal set

    var dx = 0.0
        internal set

    var dy = 0.0
        internal set

    var moved = false
        internal set

    var captured = false
        internal set

    fun isPressed(button: Button) = pressed.contains(button)

    fun isReleased(button: Button) = released.contains(button)

    fun isDown(button: Button) = down.contains(button)

    fun poll() {

        pressed.clear()
        released.clear()

        while (events.isNotEmpty()) {
            val event = events.pop()
            when (event.action) {

                Action.PRESS -> {
                    pressed.add(event.button)
                    down.add(event.button)
                }

                Action.RELEASE -> {
                    released.add(event.button)
                    down.remove(event.button)
                }

                Action.REPEAT -> {
                    // ignore
                }
            }
        }
    }
}