package com.blazeit.game.devices

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWWindowSizeCallback
import org.lwjgl.system.MemoryUtil.NULL

/**
 * Construct w window instance that provides w canvas for rendering w graphical representation of the application to the user.
 * @param title the window's title.
 * @param mode the window's mode (default: Windowed).
 * @param width the window's width in pixels (ignored when not in windowed mode).
 * @param height the window's height in pixels (ignored when not in windowed mode).
 * @param vsync whether to vertically synchronize updating the window.
 * @param samples the number of samples used for rendering to the window.
 * @constructor
 */
class Window(
        title: String,
        private var mode: WindowMode = WindowMode.Windowed,
        private var width: Int = 1280,
        private var height: Int = 720,
        vsync: Boolean = true,
        samples: Int = 1
) {

    /**
     * The size callback is called whenever the window it is attached to is resized by either the user of the system.
     * @param window the window the callback is going to be attached to.
     */
    class SizeCallback(private val window: Window) : GLFWWindowSizeCallback() {

        /**
         * Validate that the provided handle corresponds to the window that was passed to the constructor earlier.
         * Then, iff the window handle checks out, update the window's internal width and height fields.
         * @param handle the GLFW window handle.
         * @param width the new window width.
         * @param height the new window height.
         */
        override fun invoke(handle: Long, width: Int, height: Int) {
            if (window.handle == handle) {
                window.resized = true
                window.width = width
                window.height = height
            }
        }

    }

    private val handle: Long
    private var resized: Boolean
    private var visible: Boolean

    init {

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE)
        glfwWindowHint(GLFW_SAMPLES, samples)

        val monitor = glfwGetPrimaryMonitor()
        val videoMode = glfwGetVideoMode(monitor)!!

        if (mode == WindowMode.Borderless || mode == WindowMode.FullScreen) {
            width = videoMode.width()
            height = videoMode.height()
        }

        glfwWindowHint(GLFW_RESIZABLE, if (mode == WindowMode.Windowed) GLFW_TRUE else GLFW_FALSE)

        handle = when (mode) {
            WindowMode.Windowed -> glfwCreateWindow(width, height, title, NULL, NULL)
            WindowMode.Borderless -> glfwCreateWindow(width, height, title, NULL, NULL)
            WindowMode.FullScreen -> glfwCreateWindow(width, height, title, monitor, NULL)
        }

        if (handle == NULL) {
            throw Exception("Could not initialize Window")
        }

        val x = 1920 + (videoMode.width() - width) / 2
        val y = (videoMode.height() - height) / 2

        glfwMakeContextCurrent(handle)
        glfwSetWindowPos(handle, x, y)
        glfwSwapInterval(if (vsync) 1 else 0)
        glfwSetWindowSizeCallback(handle, SizeCallback(this))

        resized = false
        visible = false
    }

    /**
     * @Return the internal GLFW window handle.
     */
    fun getHandle(): Long {
        return handle
    }

    /**
     * @Return whether or not the window was resized during the last frame.
     */
    fun isResized(): Boolean {
        return resized
    }

    /**
     * @return the window's inner-width in pixels.
     */
    fun getWidth(): Int {
        return width
    }

    /**
     * @return the window's inner-height in pixels.
     */
    fun getHeight(): Int {
        return height
    }

    /**
     * @return the window's aspect ratio.
     */
    fun getAspectRatio(): Float {
        return width.toFloat() / height.toFloat()
    }

    /**
     * @return the current visibility of the window.
     */
    fun isVisible(): Boolean {
        return visible
    }

    /**
     * @return whether the window was requested to close.
     */
    fun isClosed(): Boolean {
        return glfwWindowShouldClose(handle)
    }

    /**
     * Resize the window to the provided width and height.
     * @param width the window's renderTarget inner-width in pixels.
     * @param height the window's renderTarget inner-height in pixels.
     */
    fun resize(width: Int, height: Int) {
        glfwSetWindowSize(handle, width, height)
    }

    /**
     * Make the window visible to the user.
     */
    fun show() {
        glfwShowWindow(handle)
        visible = true
    }

    /**
     * Hide the window from the user.
     */
    fun hide() {
        glfwHideWindow(handle)
        visible = false
    }

    /**
     *  Update the window's canvas by swapping its internal rendering buffer.
     */
    fun update() {
        resized = false
        glfwSwapBuffers(handle)
    }

    /**
     * Request the window to close.
     */
    fun close() {
        glfwSetWindowShouldClose(handle, true)
    }

    /**
     * Destroy the window and free its GLFW resources.
     */
    fun destroy() {
        glfwSetWindowSizeCallback(handle, null)
        glfwDestroyWindow(handle)
    }

}