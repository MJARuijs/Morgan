package com.blazeit.game.resources.images

import com.blazeit.game.util.File
import com.blazeit.game.resources.Loader
import org.lwjgl.BufferUtils
import org.lwjgl.stb.STBImage

class ImageLoader: Loader<ImageData> {

    override fun load(path: String): ImageData {

        val file = File(path)
        val absolutePath = file.getPath()

        val widthBuffer = BufferUtils.createIntBuffer(1)
        val heightBuffer = BufferUtils.createIntBuffer(1)
        val channelBuffer = BufferUtils.createIntBuffer(1)

        val pixels = STBImage.stbi_load(absolutePath, widthBuffer, heightBuffer, channelBuffer, 4)
                ?: throw IllegalArgumentException("Could not find texture file: $path")

        return ImageData(widthBuffer.get(), heightBuffer.get(), pixels)
    }

}