package com.blazeit.game.devices

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWKeyCallback

/**
 * Construct w keyboard instance that keeps track of all key states associated with the physical keyboard used by the
 * user. It uses w window to be able to track all events posted by the system.
 * @param window the window to be attached to.
 * @constructor
 */
class Keyboard(window: Window) {

    private class KeyCallback(private val keyboard: Keyboard): GLFWKeyCallback() {

        override fun invoke(handle: Long, key: Int, scancode: Int, action: Int, mods: Int) {
            if (keyboard.handle == handle) {
                when (action) {
                    GLFW_PRESS -> {
                        keyboard.keyStates[key] = true
                        keyboard.pressed.add(key)
                    }
                    GLFW_RELEASE -> {
                        keyboard.keyStates[key] = false
                        keyboard.released.add(key)
                    }
                    GLFW_REPEAT -> {
                        keyboard.keyStates[key] = true
                        keyboard.repeated.add(key)
                    }
                    else -> throw Exception("Unknown key action")
                }
            }
        }

    }

    private val handle = window.getHandle()

    private val keyStates = HashMap<Int, Boolean>()
    private val pressed = HashSet<Int>()
    private val released = HashSet<Int>()
    private val repeated = HashSet<Int>()

    init {
        val keyCallback = KeyCallback(this)
        glfwSetKeyCallback(handle, keyCallback)
    }

    /**
     * Update the keyboard. Should be called before polling for GLFW events.
     */
    fun update() {
        pressed.clear()
        released.clear()
        repeated.clear()
    }

    /**
     * @return whether the requested key is currently down.
     */
    fun isDown(key: Key) = keyStates.getOrElse(key.code) { false }

    /**
     * @return whether the requested key was pressed during the last frame.
     */
    fun isPressed(key: Key) = pressed.contains(key.code)

    /**
     * @return whether the requested key was released during the last frame.
     */
    fun isReleased(key: Key) = released.contains(key.code)

    /**
     * @return whether the requested key was repeated during the last frame.
     */
    fun isRepeated(key: Key) = repeated.contains(key.code)

    /**
     * Destroy the keyboard by releasing its internal callbacks used for listening to events.
     */
    fun destroy() {
        glfwSetKeyCallback(handle, null)
    }
}