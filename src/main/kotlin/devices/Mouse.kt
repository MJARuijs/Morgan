package com.blazeit.game.devices

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWCursorPosCallback
import org.lwjgl.glfw.GLFWMouseButtonCallback
import org.lwjgl.glfw.GLFWScrollCallback

/**
 * Construct w mouse instance that processes events posted to the provided window and provides methods for easily using
 * the processed button and cursor position information.
 * @param window the window to listen for events to.
 * @constructor
 */
class Mouse(window: Window) {

    private class ButtonCallback(private val mouse: Mouse): GLFWMouseButtonCallback() {

        override fun invoke(handle: Long, button: Int, action: Int, mods: Int) {
            if (mouse.handle == handle) {
                when (action) {
                    GLFW_PRESS -> {
                        mouse.buttonStates[button] = true
                        mouse.pressed.add(button)
                    }
                    GLFW_RELEASE -> {
                        mouse.buttonStates[button] = false
                        mouse.released.add(button)
                    }
                    GLFW_REPEAT -> {
                        mouse.buttonStates[button] = true
                        mouse.repeated.add(button)
                    }
                    else -> throw Exception("Unknown button action")
                }
            }
        }
    }

    private class ScrollCallback(private val mouse: Mouse): GLFWScrollCallback() {

        override fun invoke(handle: Long, xScroll: Double, yScroll: Double) {
            if (handle == mouse.handle) {
                mouse.ds = yScroll.toFloat()
            }
        }

    }

    private class PositionCallback(private val window: Window, private val mouse: Mouse): GLFWCursorPosCallback() {

        private var captured = mouse.isCaptured()

        override fun invoke(handle: Long, xPixel: Double, yPixel: Double) {

            if (captured != mouse.captured) {
                captured = mouse.captured
                return
            }

            if (mouse.handle == handle) {

                val x = (2.0 * (xPixel / window.getWidth()) - 1.0).toFloat()
                val y = (2.0 * (-yPixel / window.getHeight()) + 1.0).toFloat()

                mouse.dx = (x - mouse.x)
                mouse.dy = (y - mouse.y)
                mouse.x = x
                mouse.y = y
            }
        }

    }

    private val handle = window.getHandle()

    private var captured = false

    private val buttonStates = HashMap<Int, Boolean>()
    private val pressed = HashSet<Int>()
    private val released = HashSet<Int>()
    private val repeated = HashSet<Int>()

    private var x = 0.0f
    private var y = 0.0f
    private var dx = 0.0f
    private var dy = 0.0f
    private var ds = 0.0f

    init {
        glfwSetCursorPos(window.getHandle(), window.getWidth() / 2.0, window.getHeight() / 2.0)

        val buttonCallback = ButtonCallback(this)
        glfwSetMouseButtonCallback(handle, buttonCallback)

        val scrollCallback = ScrollCallback(this)
        glfwSetScrollCallback(handle, scrollCallback)

        val positionCallback = PositionCallback(window, this)
        glfwSetCursorPosCallback(handle, positionCallback)
    }

    /**
     * Update the keyboard. Should be called before polling for GLFW events.
     */
    fun update() {
        pressed.clear()
        released.clear()
        repeated.clear()
        dx = 0.0f
        dy = 0.0f
        ds = 0.0f
    }

    /**
     * Let the mouse be captured by the window.
     */
    fun capture() {
        glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
        captured = true
    }

    /**
     * Release the mouse from the window.
     */
    fun release() {
        glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
        captured = false
    }

    /**
     * @return whether the mouse is captured by the window or not.
     */
    fun isCaptured(): Boolean {
        return captured
    }

    /**
     * @return the mouse's x position in the GraphicsContext coordinate space.
     */
    fun getX() = x

    /**
     * @return the mouse's y position in the GraphicsContext coordinate space.
     */
    fun getY() = y

    /**
     * @return the mouse's x position delta in the GraphicsContext coordinate space.
     */
    fun getDeltaX() = dx

    /**
     * @return the mouse's y position delta in the GraphicsContext coordinate space.
     */
    fun getDeltaY() = dy

    fun getDeltaScroll() = ds

    fun isDown(button: MouseButton)= buttonStates.getOrElse(button.code) { false }

    /**
     * @return whether the requested key was pressed during the last frame.
     */
    fun isPressed(button: MouseButton) = pressed.contains(button.code)

    /**
     * @return whether the requested key was released during the last frame.
     */
    fun isReleased(button: MouseButton) = released.contains(button.code)

    /**
     * @return whether the requested key was repeated during the last frame.
     */
    fun isRepeated(button: MouseButton) = repeated.contains(button.code)

    /**
     * Destroy the keyboard by releasing its internal callbacks used for listening to events.
     */
    fun destroy() {
        glfwSetKeyCallback(handle, null)
    }
}