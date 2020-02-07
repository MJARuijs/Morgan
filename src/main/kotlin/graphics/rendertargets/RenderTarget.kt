package com.blazeit.game.graphics.rendertargets

import com.blazeit.game.graphics.rendertargets.attachments.*
import com.blazeit.game.graphics.rendertargets.attachments.AttachmentType.*
import com.blazeit.game.graphics.textures.ColorMap
import com.blazeit.game.graphics.textures.DepthMap
import org.lwjgl.BufferUtils.createIntBuffer
import org.lwjgl.opengl.GL20.glDrawBuffers
import org.lwjgl.opengl.GL30.*

class RenderTarget(private var width: Int, private var height: Int, vararg types: AttachmentType) {

    private var attachments: List<Attachment> = ArrayList()

    private val handle = glGenFramebuffers()

    init {
        glBindFramebuffer(GL_FRAMEBUFFER, handle)

        var colorCounter = 0
        for (type in types) {
            attachments += when (type) {
                COLOR_TEXTURE -> ColorTextureAttachment(colorCounter++, width, height)
                DEPTH_TEXTURE -> DepthTextureAttachment(width, height)
                DEPTH_BUFFER -> DepthBufferAttachment(width, height)
            }
        }

        val drawBuffers = createIntBuffer(colorCounter)

        for (i in 0 until colorCounter) {
            drawBuffers.put(GL_COLOR_ATTACHMENT0 + i)
        }

        drawBuffers.flip()

        glDrawBuffers(drawBuffers)

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    fun getWidth() = width

    fun getHeight() = height

    fun addAttachment(vararg types: AttachmentType) {
        var colorCounter = attachments.count { it.type == COLOR_TEXTURE }
        glBindFramebuffer(GL_FRAMEBUFFER, handle)

        for (type in types) {
            attachments += when (type) {
                COLOR_TEXTURE -> ColorTextureAttachment(colorCounter++, width, height)
                DEPTH_TEXTURE -> DepthTextureAttachment(width, height)
                DEPTH_BUFFER -> DepthBufferAttachment(width, height)
            }
        }

        val drawBuffers = createIntBuffer(colorCounter)

        for (i in 0 until colorCounter) {
            drawBuffers.put(GL_COLOR_ATTACHMENT0 + i)
        }

        drawBuffers.flip()

        glDrawBuffers(drawBuffers)

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    fun getAspectRatio() = width.toFloat() / height.toFloat()

    fun start() {
        glBindFramebuffer(GL_FRAMEBUFFER, handle)
        glViewport(0, 0, width, height)
    }

    fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height

        attachments.forEach { it.resize(width, height) }
    }

    fun stop() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
//        glViewport(0, 0, width, height)

    }

    fun getColorMap(index: Int = 0): ColorMap {
        attachments.forEach {
            if (it is ColorTextureAttachment) {
                if (it.index == index) return it.colorMap
            }
        }

        throw IllegalArgumentException("This RenderTarget does not have a ColorTexture Attachment that matches the requested index!")
    }

    fun getDepthMap(): DepthMap {
        attachments.forEach {
            if (it is DepthTextureAttachment) return it.depthMap
        }

        throw IllegalArgumentException("This RenderTarget does not have a DepthTexture Attachment!")
    }

    fun matches(width: Int, height: Int, vararg requiredTypes: AttachmentType): Boolean {

        if (width != this.width) return false
        if (height != this.height) return false

        if (requiredTypes.size != attachments.size) return false

        val requestedColorAttachments = requiredTypes.count { it == COLOR_TEXTURE }
        val availableColorAttachments = attachments.count { it.type == COLOR_TEXTURE }

        if (availableColorAttachments != requestedColorAttachments) return false

        for (type in requiredTypes) {
            attachments.find { it.type == type } ?: return false
        }

        for (attachment in attachments) {
            requiredTypes.find { it == attachment.type } ?: return false
        }

        return true
    }

    private fun renderTo(targetID: Int, colorBuffer: Int = 0) {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, targetID)
        glBindFramebuffer(GL_READ_FRAMEBUFFER, handle)

        glReadBuffer(GL_COLOR_ATTACHMENT0 + colorBuffer)

        glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT, GL_NEAREST)

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    fun renderTo(target: RenderTarget, colorBuffer: Int = 0) = renderTo(target.handle, colorBuffer)

    fun renderToScreen(colorBuffer: Int = 0) = renderTo(0, colorBuffer)

    fun destroy() {
        attachments.forEach(Attachment::destroy)
        glDeleteFramebuffers(handle)
    }
}