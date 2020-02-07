package com.blazeit.game.graphics.models.meshes

import org.lwjgl.opengl.GL11.*

enum class Primitive(val mode: Int) {

    POINT(GL_POINTS),
    LINE(GL_LINES),
    TRIANGLE(GL_TRIANGLES),
    QUAD(GL_QUADS)

}