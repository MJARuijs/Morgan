package com.blazeit.game

import com.blazeit.game.entities.Entity
import com.blazeit.game.entities.EntityRenderer
import com.blazeit.game.graphics.Camera
import com.blazeit.game.graphics.GraphicsContext
import com.blazeit.game.graphics.GraphicsOption.*
import com.blazeit.game.graphics.Texture
import com.blazeit.game.graphics.lights.AmbientLight
import com.blazeit.game.graphics.lights.DirectionalLight
import com.blazeit.game.graphics.models.ModelCache
import com.blazeit.game.graphics.shaders.ShaderProgram
import com.blazeit.game.graphics.shadows.ShadowBox
import graphics.shadows.ShadowRenderer
import com.blazeit.game.math.Color
import com.blazeit.game.math.matrices.Matrix4
import com.blazeit.game.math.vectors.Vector2
import com.blazeit.game.math.vectors.Vector3
import com.blazeit.game.math.vectors.Vector4
import devices.Key
import devices.Timer
import devices.Window
import org.lwjgl.opengl.GL11.*

fun main() {

    val window = Window("Game")
    val keyboard = window.keyboard
    val mouse = window.mouse
    val timer = Timer()

    GraphicsContext.init(Color(0.25f, 0.25f, 0.25f))
    GraphicsContext.enable(DEPTH_TESTING, FACE_CULLING, TEXTURE_MAPPING)

    val uiProgram = ShaderProgram.load("shaders/user_interface.vert", "shaders/user_interface.frag")

    val camera = Camera(aspectRatio = window.aspectRatio)
    val light = DirectionalLight(Color(1.0f, 1.0f, 1.0f), Vector3(0.5f, 0.75f, -1.0f))
    val ambient = AmbientLight(Color(0.25f, 0.25f, 0.25f))

    val box = ShadowBox(camera)
    ShadowRenderer.add(box)

    val entity = Entity(Matrix4().scale(4.0f, 4.0f, 4.0f), ModelCache.get("models/duck.dae"))
    val entities = ArrayList<Entity>()
    entities += entity

    timer.reset()

    val standardTexture = Texture(Vector2(0.5f, 0.5f), Vector2(0.5f, 0.5f), false)
    val depthTexture = Texture(Vector2(-0.5f, 0.5f), Vector2(0.5f, 0.5f), true)
    val shadowMapTexture = Texture(Vector2(0.5f, -0.5f), Vector2(0.5f, 0.5f), true)

    while (window.running) {

        // Process input

        if (keyboard.isPressed(Key.ESCAPE)) {
            break
        }

        camera.update(keyboard, mouse, timer.getDelta())

        // Render scene

        val shadows = ShadowRenderer.render(camera, entities, light)

        val target = EntityRenderer.render(camera, entities, ambient, light, shadows, Vector4())

        uiProgram.start()
        standardTexture.render(uiProgram, target.getColorMap().handle)
        shadowMapTexture.render(uiProgram, shadows[0].shadowMap.handle)
        depthTexture.render(uiProgram, target.getDepthMap().handle)

        uiProgram.stop()
        // Update devices

        window.synchronize()
        window.poll()

        timer.update()
    }

    EntityRenderer.destroy()
    window.destroy()
}