package com.blazeit.game.graphics.godrays

import com.blazeit.game.graphics.Camera
import com.blazeit.game.graphics.GraphicsContext
import com.blazeit.game.graphics.GraphicsOption
import com.blazeit.game.graphics.Plane
import com.blazeit.game.graphics.rendertargets.RenderTarget
import com.blazeit.game.graphics.rendertargets.attachments.AttachmentType
import com.blazeit.game.graphics.samplers.Sampler
import com.blazeit.game.graphics.shaders.ShaderProgram
import com.blazeit.game.graphics.shadows.ShadowData
import com.blazeit.game.math.vectors.Vector2
import org.lwjgl.opengl.GL11.*

object GodRayRenderer {

    private val shaderProgram = ShaderProgram.load("shaders/godray.vert", "shaders/godray.frag")
    private val renderTarget = RenderTarget(960, 540, AttachmentType.COLOR_TEXTURE, AttachmentType.DEPTH_TEXTURE)

    fun render(camera: Camera, shadows: List<ShadowData> = ArrayList(), entityTarget: RenderTarget, box: GodRayBox): RenderTarget {

        entityTarget.renderTo(renderTarget)

        renderTarget.start()
        glClear(GL_COLOR_BUFFER_BIT)

        GraphicsContext.enable(GraphicsOption.ALPHA_BLENDING)

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

        val inverse = camera.viewMatrix.inverse()

        shaderProgram.set("projection", camera.projectionMatrix)
        shaderProgram.set("view", camera.viewMatrix)
        shaderProgram.set("cameraPosition", camera.position)
        shaderProgram.set("depthTexture", entityTarget.getDepthMap().handle)
        shaderProgram.set("model", inverse)
        shaderProgram.set("levels", box.levels / 10)

        box.draw()

        shaderProgram.stop()

        GraphicsContext.disable(GraphicsOption.ALPHA_BLENDING)

        renderTarget.stop()

        return renderTarget
    }

    fun destroy() {
        shaderProgram.destroy()
    }

}