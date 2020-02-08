package com.blazeit.game.graphics.godrays

import com.blazeit.game.graphics.Camera
import com.blazeit.game.graphics.models.meshes.Attribute
import com.blazeit.game.graphics.models.meshes.Layout
import com.blazeit.game.graphics.models.meshes.Mesh
import com.blazeit.game.graphics.models.meshes.Primitive
import com.blazeit.game.math.vectors.Vector3
import kotlin.math.*

class GodRayBox(camera: Camera, val levels: Int, maxDistance: Float = 25f) {

    private val mesh: Mesh

    init {
        val nearWidth = camera.zNear * tan(Math.toRadians(camera.fieldOfView.toDouble()).toFloat())
        val farWidth = maxDistance * tan(Math.toRadians(camera.fieldOfView.toDouble()).toFloat())
        val nearHeight = nearWidth / camera.aspectRatio
        val farHeight = farWidth / camera.aspectRatio

        val vertices = ArrayList<Float>()
        val indices = ArrayList<Int>()
        val corners = arrayListOf(
            Vector3(-0.5f, 0.5f, -1.0f),
            Vector3(-0.5f, -0.5f, -1.0f),
            Vector3(0.5f, -0.5f, -1.0f),
            Vector3(0.5f, 0.5f, -1.0f)
        )

        for (level in 0 until levels) {
            val ratio = level.toFloat() / levels.toFloat()
            val width = (1 - ratio) * nearWidth + ratio * farWidth
            val height = (1 - ratio) * nearHeight + ratio * farHeight
            val depth = (ratio) * camera.zNear + (1 - ratio) * maxDistance

            val scale = Vector3(width, height, depth)

            for (corner in corners) {
                val scaled = corner * scale
                vertices += scaled.x
                vertices += scaled.y
                vertices += scaled.z
            }

            indices += 4 * level
            indices += 4 * level + 1
            indices += 4 * level + 2
            indices += 4 * level
            indices += 4 * level + 2
            indices += 4 * level + 3
        }

        mesh = Mesh(
            Layout(Primitive.TRIANGLE, Attribute(0, 3)),
            vertices.toFloatArray(),
            indices.toIntArray()
        )
    }

    fun draw() {
        mesh.draw()
    }
}