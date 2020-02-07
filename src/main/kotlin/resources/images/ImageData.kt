package com.blazeit.game.resources.images

import com.blazeit.game.resources.Resource
import java.nio.ByteBuffer

class ImageData(val width: Int, val height: Int, val data: ByteBuffer): Resource {

    override fun destroy() {
        data.clear()
    }

}