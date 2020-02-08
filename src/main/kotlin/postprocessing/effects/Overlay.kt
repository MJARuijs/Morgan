package com.blazeit.game.graphics.postprocessing.effects

import com.blazeit.game.graphics.Quad
import com.blazeit.game.graphics.postprocessing.SimpleEffect
import com.blazeit.game.graphics.rendertargets.RenderTarget
import com.blazeit.game.graphics.samplers.Sampler
import com.blazeit.game.graphics.shaders.ShaderProgram
import com.blazeit.game.graphics.textures.ColorMap

class Overlay() : SimpleEffect() {

    private val shaderProgram = ShaderProgram.load(
            "shaders/postprocessing/overlay.vert",
            "shaders/postprocessing/overlay.frag"
    )

    private val sampler = Sampler(0)
    private val overlaySampler = Sampler(1)
    private val quad = Quad()

    var overlay: ColorMap? = null

    override fun apply(source: RenderTarget, target: RenderTarget) {

        target.start()
        target.clear()

        shaderProgram.start()
        shaderProgram.set("sampler", sampler.index)
        shaderProgram.set("overlay", overlaySampler.index)

        sampler.bind(source.getColorMap())
        overlaySampler.bind(overlay!!)

        quad.draw()

        shaderProgram.stop()

        target.stop()
    }
}