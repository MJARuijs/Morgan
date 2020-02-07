package com.blazeit.game.devices

import org.lwjgl.glfw.GLFW.*

/**
 * The GLFW class provides helper functions for the starting, stopping,
 * and polling of the hardware devices and APIs.
 */
class GLFW {

    init {
        val initialized = glfwInit()
        if (!initialized) {
            throw Exception("Could not initialize GLFW")
        }
    }

    /**
     * Poll for updates and/or changes.
     */
    fun poll() {
        glfwPollEvents()
    }

    /**
     * Terminate the devices and APIs.
     */
    fun destroy() {
        glfwTerminate()
    }

}