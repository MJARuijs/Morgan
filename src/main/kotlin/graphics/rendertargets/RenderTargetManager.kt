//package com.blazeit.game.graphics.rendertargets
//
//import com.blazeit.game.graphics.rendertargets.attachments.AttachmentType
//import devices.Window
//
//object RenderTargetManager {
//
//    class RenderTargetUsage(val renderTarget: RenderTarget, var unusedFrames: Int)
//
//    private var window: Window? = null
//    private lateinit var default: RenderTarget
//
//    private val renderTargets = ArrayList<RenderTargetUsage>()
//
//    fun setWindow(window: Window) {
//        this.window = window
//        default = RenderTarget(window.width, window.height, AttachmentType.COLOR_TEXTURE, AttachmentType.DEPTH_TEXTURE)
//    }
//
//    fun default() = default
//
//    fun update(window: Window) {
//
//        if (window.resized) {
//            renderTargets.forEach { it.renderTarget.resize(window.width, window.height) }
//        }
//    }
//
//    fun getAvailableTarget(vararg types: AttachmentType, width: Int = default.getWidth(), height: Int = default.getHeight()): RenderTarget {
//        val requestedTarget = renderTargets.find { it.renderTarget.matches(width, height, *types) && it.renderTarget.isAvailable() } ?: return createTarget(*types, width = width, height = height)
//        requestedTarget.unusedFrames = 0
//        return requestedTarget.renderTarget
//    }
//
//    private fun createTarget(vararg types: AttachmentType, width: Int, height: Int): RenderTarget {
//        return try {
//            val target = RenderTarget(width, height, *types)
//            renderTargets.add(RenderTargetUsage(target, 0))
//            target
//        } catch (e: NullPointerException) {
//            throw NullPointerException("Default renderTarget was not yet initialized. Set a Window first!")
//        }
//    }
//
//    fun size() = renderTargets.size
//
//}