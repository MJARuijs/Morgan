package com.blazeit.game.graphics

import com.blazeit.game.math.Color
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import java.util.concurrent.atomic.AtomicBoolean

object GraphicsContext {

    private val initialised = AtomicBoolean(false)

    fun init(background: Color = Color(), vararg options: GraphicsOption) {
        if (!initialised.getAndSet(true)) {

            GL.createCapabilities()
            glClearColor(background.r, background.g, background.b, 1.0f)
            glClearDepth(1.0)

            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

            options.forEach(GraphicsOption::enable)
            GraphicsOption.values().filter(options::contains).forEach(GraphicsOption::enable)
        }
    }

    fun enable(vararg options: GraphicsOption) = options.forEach(GraphicsOption::enable)

    fun disable(vararg options: GraphicsOption) = options.forEach(GraphicsOption::disable)

}