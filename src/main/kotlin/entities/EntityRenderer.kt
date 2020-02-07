package com.blazeit.game.entities

import com.blazeit.game.graphics.Camera
import com.blazeit.game.graphics.lights.AmbientLight
import com.blazeit.game.graphics.lights.DirectionalLight
import com.blazeit.game.graphics.samplers.Sampler
import com.blazeit.game.graphics.shaders.ShaderProgram
import com.blazeit.game.graphics.shadows.ShadowData
import com.blazeit.game.math.Color
import com.blazeit.game.math.vectors.Vector2
import com.blazeit.game.math.vectors.Vector4
import org.lwjgl.opengl.GL11.*

object EntityRenderer {

    private val shaderProgram = ShaderProgram.load("shaders/entity.vert", "shaders/entity.frag")
    private val blackProgram = ShaderProgram.load("shaders/shadow.vert", "shaders/shadow.frag")

    fun render(camera: Camera, entities: List<Entity>, ambient: AmbientLight, directional: DirectionalLight, shadows: List<ShadowData> = ArrayList(), waterPlane: Vector4 = Vector4()) {

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

        shaderProgram.set("ambient.color", ambient.color)

        shaderProgram.set("sun.color", directional.color)
        shaderProgram.set("sun.direction", directional.direction)

        shaderProgram.set("waterPlane", waterPlane)
        shaderProgram.set("cameraPosition", camera.position)

        for (entity in entities) {
            val model = entity.model
            val transformation = entity.transformation
            shaderProgram.set("model", transformation)
            for (shape in model.shapes) {
                shaderProgram.set("material.diffuseColor", shape.material.diffuse)
                shaderProgram.set("material.specularColor", shape.material.specular)
                shaderProgram.set("material.shininess", shape.material.shininess)
                shape.mesh.draw()
            }
        }

        shaderProgram.stop()
    }

    fun renderBlack(camera: Camera, entities: List<Entity>) {
        blackProgram.start()
        blackProgram.set("projection", camera.projectionMatrix)
        blackProgram.set("view", camera.viewMatrix)
        blackProgram.set("color", Color(0.0f, 0.0f, 0.0f, 1.0f))

        glDisable(GL_CULL_FACE)
        for (entity in entities) {
            val model = entity.model
            val transformation = entity.transformation
            blackProgram.set("model", transformation)
            for (shape in model.shapes) {
                shape.mesh.draw()
            }
        }
        glEnable(GL_CULL_FACE)
        blackProgram.stop()
    }

    fun destroy() {
        shaderProgram.destroy()
    }

}