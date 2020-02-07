package com.blazeit.game.graphics

import org.lwjgl.opengl.GL11.GL_TRUE
import org.lwjgl.opengl.GL15.*

class Query(private val type: Int) {

    private val id = glGenQueries()
    var inUse = false

    fun start() {
        glBeginQuery(type, id)
        inUse = true
    }

    fun stop() {
        glEndQuery(type)
    }

    fun isResultAvailable(): Boolean {
        return glGetQueryObjecti(id, GL_QUERY_RESULT_AVAILABLE) == GL_TRUE
    }

    fun getResult(): Int {
        inUse = false
        return glGetQueryObjecti(id, GL_QUERY_RESULT)
    }

}