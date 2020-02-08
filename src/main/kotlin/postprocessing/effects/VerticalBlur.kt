package com.blazeit.game.graphics.postprocessing.effects

import com.blazeit.game.graphics.Quad
import com.blazeit.game.graphics.postprocessing.SimpleEffect
import com.blazeit.game.graphics.rendertargets.RenderTarget
import com.blazeit.game.graphics.samplers.Sampler
import com.blazeit.game.graphics.shaders.ShaderProgram
import kotlin.math.max

class VerticalBlur(private val strength: Float): SimpleEffect() {

    private val shaderProgram = ShaderProgram.load(
            "shaders/postprocessing/gaussian_vertical_blur.vert",
            "shaders/postprocessing/gaussian_blur.frag"
    )

    private val sampler = Sampler(0)
    private val quad = Quad()

    override fun apply(source: RenderTarget, target: RenderTarget) {

        target.start()
        target.clear()

        shaderProgram.start()
        shaderProgram.set("sampler", sampler.index)
        shaderProgram.set("strength", max(0.1f, strength))

        sampler.bind(source.getColorMap(0))

        quad.draw()

        shaderProgram.stop()

        target.stop()
    }

}