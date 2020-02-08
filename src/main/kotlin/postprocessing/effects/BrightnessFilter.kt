package com.blazeit.game.graphics.postprocessing.effects

import com.blazeit.game.graphics.Quad
import com.blazeit.game.graphics.postprocessing.SimpleEffect
import com.blazeit.game.graphics.rendertargets.RenderTarget
import com.blazeit.game.graphics.samplers.Sampler
import com.blazeit.game.graphics.shaders.ShaderProgram

class BrightnessFilter(private val strength: Float = 0.5f): SimpleEffect() {

    val shaderProgram = ShaderProgram.load(
            "shaders/postprocessing/brightness.vert",
            "shaders/postprocessing/brightness.frag"
    )

    val sampler = Sampler(0)
    private val quad = Quad()

    override fun apply(source: RenderTarget, target: RenderTarget) {

        target.start()
        target.clear()

        shaderProgram.start()
        shaderProgram.set("sampler", sampler.index)
        shaderProgram.set("strength", strength)

        sampler.bind(source.getColorMap(0))

        quad.draw()

        shaderProgram.stop()

        target.stop()
    }

}