package com.blazeit.game.graphics.postprocessing.effects

import com.blazeit.game.graphics.Quad
import com.blazeit.game.graphics.postprocessing.SimpleEffect
import com.blazeit.game.graphics.rendertargets.RenderTarget
import com.blazeit.game.graphics.samplers.Sampler
import com.blazeit.game.graphics.shaders.ShaderProgram
import com.blazeit.game.math.vectors.Vector2

class RadialBlur(var position: Vector2 = Vector2(0.5f, 0.5f)): SimpleEffect() {

    private val shaderProgram = ShaderProgram.load(
            "shaders/postprocessing/radial_blur.vert",
            "shaders/postprocessing/radial_blur.frag"
    )

    private val sampler = Sampler(0)
    private val quad = Quad()

    override fun apply(source: RenderTarget, target: RenderTarget) {

        target.start()
        target.clear()

        shaderProgram.start()
        shaderProgram.set("sampler", sampler.index)
        shaderProgram.set("position", position)

        sampler.bind(source.getColorMap())

        quad.draw()

        shaderProgram.stop()

        target.stop()
    }

}