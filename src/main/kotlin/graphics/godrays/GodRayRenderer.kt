package com.blazeit.game.graphics.godrays

import com.blazeit.game.graphics.Camera
import com.blazeit.game.graphics.GraphicsContext
import com.blazeit.game.graphics.GraphicsOption
import com.blazeit.game.graphics.Plane
import com.blazeit.game.graphics.lights.AmbientLight
import com.blazeit.game.graphics.lights.DirectionalLight
import com.blazeit.game.graphics.rendertargets.RenderTarget
import com.blazeit.game.graphics.rendertargets.attachments.AttachmentType
import com.blazeit.game.graphics.samplers.Sampler
import com.blazeit.game.graphics.shaders.ShaderProgram
import com.blazeit.game.graphics.shadows.ShadowData
import com.blazeit.game.math.matrices.Matrix4
import com.blazeit.game.math.vectors.Vector2
import com.blazeit.game.math.vectors.Vector4

object GodRayRenderer {

    private val shaderProgram = ShaderProgram.load("shaders/godray.vert", "shaders/godray.frag")
    private val renderTarget = RenderTarget(960, 540, AttachmentType.COLOR_TEXTURE, AttachmentType.DEPTH_TEXTURE)

    fun render(camera: Camera, shadows: List<ShadowData> = ArrayList()): RenderTarget {

        renderTarget.start()
        renderTarget.clear()

        GraphicsContext.enable(GraphicsOption.ALPHA_BLENDING)
        GraphicsContext.disable(GraphicsOption.DEPTH_TESTING)

        shaderProgram.start()

        if (shadows.isNotEmpty()) {

            val shadowData = shadows[0]
            val shadowSampler = Sampler(0)

            shadowSampler.bind(shadowData.shadowMap)

            shaderProgram.set("shadowPosition", shadowData.shadowDistance)
            shaderProgram.set("shadowMatrix", shadowData.getShadowMatrix())
            shaderProgram.set("shadowMapSize", Vector2(
                    shadowData.shadowMap.getWidth(),
                    shadowData.shadowMap.getHeight()
            ))
            shaderProgram.set("shadowMap", shadowSampler.index)
        }

        shaderProgram.set("projection", camera.projectionMatrix)
        shaderProgram.set("view", camera.viewMatrix)

        shaderProgram.set("cameraPosition", camera.position)

        val plane = Plane()
        shaderProgram.set("levels", 100)

        val inverse = camera.viewMatrix.inverse().scale(10.0f, 10.0f, 10.0f)

        for (level in 0 until 100) {
            val transformation = inverse.translate(0.0f, 0.0f, -0.1f * (level + 1))
            shaderProgram.set("model", transformation)
            plane.draw()
        }

        shaderProgram.stop()

        GraphicsContext.disable(GraphicsOption.ALPHA_BLENDING)
        GraphicsContext.enable(GraphicsOption.DEPTH_TESTING)

        renderTarget.stop()

        return renderTarget
    }

    fun destroy() {
        shaderProgram.destroy()
    }

}