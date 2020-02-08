//package com.blazeit.game.graphics.postprocessing
//
//import com.blazeit.game.ecs.Entity
//import com.blazeit.game.entities.EntityRenderer
//import com.blazeit.game.environment.sky.sun.Sun
//import com.blazeit.game.graphics.Quad
//import com.blazeit.game.graphics.cameras.Camera
//import com.blazeit.game.graphics.postprocessing.effects.RadialBlur
//import com.blazeit.game.graphics.samplers.Sampler
//import com.blazeit.game.graphics.shaders.ShaderProgram
//import com.blazeit.game.math.vectors.Vector4
//
//class Shafts(private val entities: List<Entity>, private val camera: Camera, private val sun: Sun): SimpleEffect() {
//
//    private val overlayProgram = ShaderProgram.load(
//            "shaders/postprocessing/overlay.vert",
//            "shaders/postprocessing/overlay.frag"
//    )
//
//    private val radialBlur = RadialBlur()
//
//    private val sampler = Sampler(0)
//    private val overlaySampler = Sampler(1)
//    private lateinit var renderTarget : RenderTarget
//    private val quad = Quad()
//
//    init {
//        val eyeSpace = camera.viewMatrix dot Vector4(sun.getPosition(), 1.0f)
//        val clipSpace = camera.projectionMatrix dot eyeSpace
//        val ndc = clipSpace.xy() / clipSpace.w
//        ndc.x = (ndc.x + 1.0f) / 2.0f
//        ndc.y = (ndc.y + 1.0f) / 2.0f
//        radialBlur.position = ndc
//    }
//
//    override fun apply(source: RenderTarget, renderTarget: RenderTarget) {
//        renderTarget = RenderTargetManager.getAvailableTarget()
//        renderTarget.start()
//        renderTarget.clear()
//
//        EntityRenderer.renderBlack(camera, entities)
//
//        sun.render(camera, false)
//        radialBlur.apply(renderTarget, renderTarget)
//
//        renderTarget.stop()
//
//        renderTarget.start()
//        renderTarget.clear()
//        renderTarget.renderTo(renderTarget)
//        renderTarget.stop()
//
//        renderTarget.start()
//        renderTarget.clear()
//
//        overlayProgram.start()
//
//        sampler.bind(source.colorMap)
//        overlaySampler.bind(renderTarget.colorMap)
//
//        overlayProgram.set("sampler", sampler.index)
//        overlayProgram.set("overlay", overlaySampler.index)
//
//        quad.draw()
//        overlayProgram.stop()
//        renderTarget.stop()
//    }
//
//}