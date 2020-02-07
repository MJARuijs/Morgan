package com.blazeit.game.graphics.rendertargets.attachments

interface Attachment {

    val type: AttachmentType

    fun resize(width: Int, height: Int)

    fun matches(other: Any?): Boolean

    fun destroy()

}