package com.blazeit.game.graphics.rendertargets.attachments

import com.blazeit.game.graphics.textures.ColorMap
import org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0
import org.lwjgl.opengl.GL30.GL_FRAMEBUFFER
import org.lwjgl.opengl.GL32.glFramebufferTexture

class ColorTextureAttachment(val index: Int, width: Int, height: Int) : Attachment {

    val colorMap = ColorMap(width, height)

    init {
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + index, colorMap.handle, 0)
    }

    override val type = AttachmentType.COLOR_TEXTURE

    override fun resize(width: Int, height: Int) = colorMap.resize(width, height)

    override fun matches(other: Any?) = other is ColorTextureAttachment

    override fun destroy() {
        colorMap.destroy()
    }

}