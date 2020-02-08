package com.blazeit.game.graphics.postprocessing

import com.blazeit.game.graphics.rendertargets.RenderTarget

abstract class SimpleEffect: Effect {

    abstract fun apply(source: RenderTarget, target: RenderTarget)

}