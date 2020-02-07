package com.blazeit.game

import com.blazeit.game.devices.*
import com.blazeit.game.entities.Entity
import com.blazeit.game.entities.EntityRenderer
import com.blazeit.game.graphics.Camera
import com.blazeit.game.graphics.GraphicsContext
import com.blazeit.game.graphics.GraphicsOption.*
import com.blazeit.game.graphics.lights.AmbientLight
import com.blazeit.game.graphics.lights.DirectionalLight
import com.blazeit.game.graphics.rendertargets.RenderTargetManager
import com.blazeit.game.graphics.shadows.ShadowBox
import graphics.shadows.ShadowRenderer
import com.blazeit.game.math.Color
import com.blazeit.game.math.vectors.Vector3
import com.blazeit.game.math.vectors.Vector4
import devices.Key
import devices.Timer
import devices.Window
import graphics.shadows.ShadowRenderer
import org.lwjgl.opengl.GL11.*

fun main() {

    val window = Window("Game")
    val keyboard = window.keyboard
    val mouse = window.mouse
    val timer = Timer()

    GraphicsContext.init(Color(0.25f, 0.25f, 0.25f))
    GraphicsContext.enable(DEPTH_TESTING, FACE_CULLING, TEXTURE_MAPPING)

    val camera = Camera(aspectRatio = window.aspectRatio)
    val light = DirectionalLight(Color(1.0f, 1.0f, 1.0f), Vector3(0.0f, 1.0f, -1.0f))
    val ambient = AmbientLight(Color(0.25f, 0.25f, 0.25f))

    RenderTargetManager.setWindow(window)

    val entities = ArrayList<Entity>()

    val box = ShadowBox(camera)
    ShadowRenderer.add(box)

    window.capture()
    val entity = Entity(Matrix4(), ModelCache.get("models/cube.dae"))
    val entities = ArrayList<Entity>()
    entities += entity

    mouse.capture()
    window.show()
    timer.reset()

    while (window.running) {

        window.poll()

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        camera.update(keyboard, mouse, timer.getDelta())

        if (keyboard.isPressed(Key.ESCAPE)) {
            if (mouse.captured) {
                window.release()
            } else {
                window.capture()
            }
        }

        val shadows = ShadowRenderer.render(camera, entities, light)

        EntityRenderer.render(camera, entities, ambient, light, shadows, Vector4())

        window.synchronize()
        RenderTargetManager.update(window)
        keyboard.update()
        mouse.update()
        timer.update()
    }

    mouse.release()
    window.hide()

    EntityRenderer.destroy()
    keyboard.destroy()
    window.destroy()
    glfw.destroy()
}