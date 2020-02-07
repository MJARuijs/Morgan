package com.blazeit.game.graphics.models.meshes

import org.lwjgl.opengl.GL11.GL_FLOAT
import org.lwjgl.opengl.GL11.GL_UNSIGNED_INT
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL31.glDrawElementsInstanced

open class Mesh(layout: Layout, vertices: FloatArray, indices: IntArray) {

    private val vao = glGenVertexArrays()
    private val vbo = glGenBuffers()
    private val ebo = glGenBuffers()

    private val mode = layout.primitive.mode
    private val count = indices.size

    init {

        glBindVertexArray(vao)
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        var offset = 0
        for (attribute in layout.attributes) {
            glVertexAttribPointer(attribute.location, attribute.size, GL_FLOAT, false, 4 * layout.stride, 4L * offset)
            glEnableVertexAttribArray(attribute.location)
            offset += attribute.size
        }
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBindVertexArray(0)

        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
        glBindBuffer(GL_ARRAY_BUFFER, 0)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    fun draw(instances: Int = 1) {
        glBindVertexArray(vao)
        glDrawElementsInstanced(mode, count, GL_UNSIGNED_INT, 0, instances)
        glBindVertexArray(0)
    }

    fun destroy() {
        glDeleteBuffers(ebo)
        glDeleteBuffers(vbo)
        glDeleteVertexArrays(vao)
    }

}