package com.blazeit.game.devices

import org.lwjgl.glfw.GLFW.*

enum class MouseButton(val code: Int) {

    LEFT(GLFW_MOUSE_BUTTON_LEFT),
    RIGHT(GLFW_MOUSE_BUTTON_RIGHT),
    MIDDLE(GLFW_MOUSE_BUTTON_MIDDLE)

}