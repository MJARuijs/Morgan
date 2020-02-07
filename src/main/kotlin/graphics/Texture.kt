package com.blazeit.game.graphics

import com.blazeit.game.graphics.shaders.ShaderProgram
import com.blazeit.game.math.vectors.Vector2
import org.lwjgl.opengl.GL13.*

class Texture(var translation: Vector2, var scale: Vector2, var depthTexture: Boolean = false) {

    private val quad = Quad()

    fun render(shaderProgram: ShaderProgram, textureId: Int) {

        glActiveTexture(GL_TEXTURE0 )
        glBindTexture(GL_TEXTURE_2D, textureId)

        shaderProgram.set("translation", translation)
        shaderProgram.set("scale", scale)
        shaderProgram.set("depthTexture", depthTexture)
        shaderProgram.set("sampler", 0)

        quad.draw()
    }
}