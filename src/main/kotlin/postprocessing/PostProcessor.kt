//package com.blazeit.game.graphics.postprocessing
//
//import com.blazeit.game.graphics.Quad
//import com.blazeit.game.graphics.rendertargets.RenderTarget
//import com.blazeit.game.graphics.rendertargets.RenderTargetManager
//import com.blazeit.game.graphics.rendertargets.attachments.AttachmentType
//import com.blazeit.game.graphics.rendertargets.attachments.ColorTextureAttachment
//import com.blazeit.game.graphics.rendertargets.attachments.DepthBufferAttachment
//import com.blazeit.game.graphics.samplers.Sampler
//import com.blazeit.game.graphics.shaders.ShaderProgram
//import org.lwjgl.opengl.GL11.*
//
//class PostProcessor {
//
//    private lateinit var source: RenderTarget
//    private lateinit var target: RenderTarget
//
//    private val shaderProgram = ShaderProgram.load("shaders/2D.vert", "shaders/2D.frag")
//    private val quad = Quad()
//    private val sampler = Sampler(0)
//
//    private fun apply(effects: List<Effect>) {
//        for (effect in effects) {
//            when (effect) {
//                is CompositeEffect -> apply(effect.effects)
//                is SimpleEffect -> {
//                    effect.apply(source, target)
//                    val temporary = target
//                    target = source
//                    source = temporary
//                }
//            }
//        }
//    }
//
//    private fun apply(original: RenderTarget, effects: List<Effect>) {
//        source = RenderTargetManager.getAvailableTarget(AttachmentType.COLOR_TEXTURE, AttachmentType.DEPTH_TEXTURE)
//        source.start()
//        source.clear()
//        original.renderTo(source)
//
//        target = RenderTargetManager.getAvailableTarget(AttachmentType.COLOR_TEXTURE, AttachmentType.DEPTH_TEXTURE)
//        target.start()
//        target.clear()
//
//        apply(effects)
//
//        original.start()
//        original.clear()
//        source.renderTo(original)
//
//        target.stop()
//        source.stop()
//
//        original.start()
//        glClear(GL_DEPTH_BUFFER_BIT)
//    }
//
//    fun apply(original: RenderTarget, vararg effects: Effect) = apply(original, effects.asList())
//
//}