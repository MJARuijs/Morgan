package devices

import java.util.*
import kotlin.collections.HashSet

class Keyboard {

    private data class Event(val key: Key, val action: Action)

    private val events = ArrayDeque<Event>()

    private val pressed = HashSet<Key>()
    private val released = HashSet<Key>()
    private val repeated = HashSet<Key>()
    private val down = HashSet<Key>()

    internal fun post(key: Key, action: Action) = events.push(Event(key, action))

    fun isPressed(key: Key) = pressed.contains(key)

    fun isReleased(key: Key) = released.contains(key)

    fun isRepeated(key: Key) = repeated.contains(key)

    fun isDown(key: Key) = down.contains(key)

    fun poll() {

        pressed.clear()
        released.clear()
        repeated.clear()

        while (events.isNotEmpty()) {
            val event = events.poll()
            when (event.action) {

                Action.PRESS -> {
                    pressed.add(event.key)
                    down.add(event.key)
                }

                Action.RELEASE -> {
                    released.add(event.key)
                    down.remove(event.key)
                }

                Action.REPEAT -> {
                    repeated.add(event.key)
                }
            }
        }
    }
}