package com.blazeit.game.graphics.shadows

import com.blazeit.game.graphics.Camera
import com.blazeit.game.graphics.lights.DirectionalLight
//import com.blazeit.game.graphics.rendertargets.RenderTargetManager
import com.blazeit.game.math.matrices.Matrix4
import com.blazeit.game.math.vectors.Vector3
import com.blazeit.game.math.vectors.Vector4
import java.lang.Math.toRadians
import kotlin.math.*

class ShadowBox(camera: Camera, val maxDistance: Float = 10f) {

    val offset = 15f
    private val nearWidth = camera.zNear * tan(toRadians(camera.fieldOfView.toDouble()).toFloat())
    private val farWidth = maxDistance * tan(toRadians(camera.fieldOfView.toDouble()).toFloat())

    private val nearHeight: Float
    private val farHeight: Float

    private var nearRightUp = Vector3()
    private var nearLeftUp = Vector3()
    private var nearLeftDown = Vector3()
    private var nearRightDown = Vector3()
    private var farRightUp = Vector3()
    private var farLeftUp = Vector3()
    private var farLeftDown = Vector3()
    private var farRightDown = Vector3()

    private var minX = 0.0f
    private var maxX = 0.0f
    private var minY = 0.0f
    private var maxY = 0.0f
    private var minZ = 0.0f
    private var maxZ = 0.0f

    private var center = Vector3()
    var translation = Vector3()

    private var projectionMatrix = Matrix4()
    private var viewMatrix = Matrix4()

    init {
//        val ratio = RenderTargetManager.default().getAspectRatio()
        val ratio = camera.aspectRatio
        nearHeight = nearWidth / ratio
        farHeight = farWidth / ratio
    }

    fun height() = if (maxY > minY) maxY - minY else minY - maxY

    fun width() = if (maxX > minX) maxX - minX else minX - maxX

    fun depth() = if (maxZ > minZ) maxZ - minZ else maxZ - minZ

    fun updateBox(camera: Camera, light: DirectionalLight) {
//        val position = camera.position

//        val lightDirection = -light.direction
//        val horizontalDirection = -light.direction.xz()
//
//        horizontalDirection.normalize()
//        lightDirection.normalize()
//
//        val xRotation = asin(-lightDirection.y)
//        val yRotation = if (horizontalDirection.x == 0.0f && horizontalDirection.y <= 0.0f){
//            0.0f
//        } else if (horizontalDirection.x == 0.0f && horizontalDirection.y > 0.0f) {
//            PI.toFloat()
//        } else if (horizontalDirection.x < 0.0f) {
//            -acos(-horizontalDirection.y)
//        } else {
//            acos(-horizontalDirection.y)
//        }
//
//        var lightRotation = Matrix4()
//        lightRotation = lightRotation.rotateX(xRotation)
//        lightRotation = lightRotation.rotateY(yRotation)
//
//        var inverseLightDirection = Matrix4()
//        inverseLightDirection = inverseLightDirection.rotateY(-yRotation)
//        inverseLightDirection = inverseLightDirection.rotateX(-xRotation)

//        val cameraRotation = camera.rotationMatrix
//        val totalRotation = lightRotation dot cameraRotation
//
//        val nearMinX = -nearWidth / 2.0f
//        val nearMaxX = nearWidth / 2.0f
//
//        val nearMinY = -nearHeight / 2.0f
//        val nearMaxY = nearHeight / 2.0f
//        val farMinY = -farHeight / 2.0f
//        val farMaxY = farHeight / 2.0f
//
//        val farMaxX = farWidth / 2.0f
//        val farMinX = -farWidth / 2.0f
//
//        nearRightUp = Vector3(nearMaxX, nearMaxY, -camera.zNear + offset)
//        nearLeftUp = Vector3(nearMinX, nearMaxY, -camera.zNear + offset)
//        nearLeftDown = Vector3(nearMinX, nearMinY, -camera.zNear + offset)
//        nearRightDown = Vector3(nearMaxX, nearMinY, -camera.zNear + offset)
//        farRightUp = Vector3(farMaxX, farMaxY, -maxDistance)
//        farLeftUp = Vector3(farMinX, farMaxY, -maxDistance)
//        farLeftDown = Vector3(farMinX, farMinY, -maxDistance)
//        farRightDown = Vector3(farMaxX, farMinY, -maxDistance)
//
//        nearRightUp = (totalRotation dot Vector4(nearRightUp, 1.0f)).xyz()
//        nearLeftUp = (totalRotation dot Vector4(nearLeftUp, 1.0f)).xyz()
//        nearLeftDown = (totalRotation dot Vector4(nearLeftDown, 1.0f)).xyz()
//        nearRightDown = (totalRotation dot Vector4(nearRightDown, 1.0f)).xyz()
//        farRightUp = (totalRotation dot Vector4(farRightUp, 1.0f)).xyz()
//        farLeftUp = (totalRotation dot Vector4(farLeftUp, 1.0f)).xyz()
//        farLeftDown = (totalRotation dot Vector4(farLeftDown, 1.0f)).xyz()
//        farRightDown = (totalRotation dot Vector4(farRightDown, 1.0f)).xyz()
//
//        val minNearX = min(nearLeftDown.x, min(nearRightDown.x, min(nearLeftUp.x, nearRightUp.x)))
//        val minFarX = min(farLeftDown.x, min(farRightDown.x, min(farLeftUp.x, farRightUp.x)))
//        minX = min(minNearX, minFarX)
//
//        val maxNearX = max(nearLeftDown.x, max(nearRightDown.x, max(nearLeftUp.x, nearRightUp.x)))
//        val maxFarX = max(farLeftDown.x, max(farRightDown.x, max(farLeftUp.x, farRightUp.x)))
//        maxX = max(maxNearX, maxFarX)
//
//        val minNearY = min(nearLeftDown.y, min(nearRightDown.y, min(nearLeftUp.y, nearRightUp.y)))
//        val minFarY = min(farLeftDown.y, min(farRightDown.y, min(farLeftUp.y, farRightUp.y)))
//        minY = min(minNearY, minFarY)
//
//        val maxNearY = max(nearLeftDown.y, max(nearRightDown.y, max(nearLeftUp.y, nearRightUp.y)))
//        val maxFarY = max(farLeftDown.y, max(farRightDown.y, max(farLeftUp.y, farRightUp.y)))
//        maxY = max(maxNearY, maxFarY)
//
//        val minNearZ = min(nearLeftDown.z, min(nearRightDown.z, min(nearLeftUp.z, nearRightUp.z)))
//        val minFarZ = min(farLeftDown.z, min(farRightDown.z, min(farLeftUp.z, farRightUp.z)))
//        minZ = min(minNearZ, minFarZ)
//
//        val maxNearZ = max(nearLeftDown.z, max(nearRightDown.z, max(nearLeftUp.z, nearRightUp.z)))
//        val maxFarZ = max(farLeftDown.z, max(farRightDown.z, max(farLeftUp.z, farRightUp.z)))
//        maxZ = max(maxNearZ, maxFarZ)
//
//        nearRightUp = (inverseLightDirection dot Vector4(nearRightUp, 1.0f)).xyz()
//        nearLeftUp = (inverseLightDirection dot Vector4(nearLeftUp, 1.0f)).xyz()
//        nearLeftDown = (inverseLightDirection dot Vector4(nearLeftDown, 1.0f)).xyz()
//        nearRightDown = (inverseLightDirection dot Vector4(nearRightDown, 1.0f)).xyz()
//        farRightUp = (inverseLightDirection dot Vector4(farRightUp, 1.0f)).xyz()
//        farLeftUp = (inverseLightDirection dot Vector4(farLeftUp, 1.0f)).xyz()
//        farLeftDown = (inverseLightDirection dot Vector4(farLeftDown, 1.0f)).xyz()
//        farRightDown = (inverseLightDirection dot Vector4(farRightDown, 1.0f)).xyz()
//
//        nearRightUp += position
//        nearLeftUp += position
//        nearLeftDown += position
//        nearRightDown += position
//        farRightUp += position
//        farLeftUp += position
//        farLeftDown += position
//        farRightDown += position
//
//        val x = (maxX + minX) / 2.0f
//        val y = (maxY + minY) / 2.0f
//        val z = maxZ

//        translation = Vector3(x, y, z)
//        translation = (inverseLightDirection dot Vector4(translation, 1.0f)).xyz()
//        translation += position
//
//        center.x = (maxX + minX) / 2.0f
//        center.y = (maxY + minY) / 2.0f
//        center.z = (maxZ + minZ) / 2.0f

//        updateViewMatrix(light)
//        updateProjectionMatrix()
    }

    private fun updateProjectionMatrix() {
        projectionMatrix = Matrix4()
        projectionMatrix[0, 0] = 2.0f / width()
        projectionMatrix[1, 1] = 2.0f / height()
        projectionMatrix[2, 2] = -2.0f / depth()
        projectionMatrix[2, 3] = -1.0f
        projectionMatrix[3, 3] = 1.0f
    }

    private fun updateViewMatrix(light: DirectionalLight) {
        val lightDirection = -light.direction
        val horizontalDirection = -light.direction.xz()

        horizontalDirection.normalize()
        lightDirection.normalize()

        val xRotation = asin(-lightDirection.y)
        val yRotation = if (horizontalDirection.x == 0.0f && horizontalDirection.y <= 0.0f){
            0.0f
        } else if (horizontalDirection.x == 0.0f && horizontalDirection.y > 0.0f) {
            PI.toFloat()
        } else if (horizontalDirection.x < 0.0f) {
            -acos(-horizontalDirection.y)
        } else {
            acos(-horizontalDirection.y)
        }

        viewMatrix = Matrix4()
        viewMatrix = viewMatrix.rotateX(xRotation)
        viewMatrix = viewMatrix.rotateY(yRotation)
        viewMatrix = viewMatrix.translate(-translation)
    }

    fun getProjectionMatrix(): Matrix4 {
        return projectionMatrix
    }

    fun getViewMatrix(): Matrix4 {
        return viewMatrix
    }

}