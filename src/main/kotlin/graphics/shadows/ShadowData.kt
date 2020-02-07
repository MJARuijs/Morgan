package com.blazeit.game.graphics.shadows

import com.blazeit.game.graphics.textures.DepthMap
import com.blazeit.game.math.matrices.Matrix4
import com.blazeit.game.math.vectors.Vector3

data class ShadowData(val translation: Vector3,
                      val orthoProjection: Matrix4,
                      val lightView: Matrix4,
                      val shadowDistance: Float,
                      val offset: Float,
                      val shadowMap: DepthMap,
                      val dimensions: Vector3
) {

    val inverseProjection = orthoProjection.inverse()
    val inverseView = lightView.inverse()

    fun getShadowMatrix(): Matrix4 {
        var offsetMatrix = Matrix4()
        offsetMatrix = offsetMatrix.translate(Vector3(0.5f, 0.5f, 0.5f))
        offsetMatrix = offsetMatrix.scale(Vector3(0.5f, 0.5f, 0.5f))
        return offsetMatrix dot orthoProjection dot lightView
    }

}