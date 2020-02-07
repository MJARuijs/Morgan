package com.blazeit.game.util

import java.nio.ByteBuffer
import java.nio.ByteOrder

object NativeMemory {

    fun createByteBuffer(size: Int): ByteBuffer {
        val buffer = ByteBuffer.allocateDirect(size)
        buffer.order(ByteOrder.nativeOrder())
        return buffer
    }

    fun createIntBuffer(size: Int) = createByteBuffer(4 * size).asIntBuffer()!!

    fun createFloatBuffer(size: Int) = createByteBuffer(4 * size).asFloatBuffer()!!

}