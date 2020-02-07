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
import org.lwjgl.opengl.GL11.*

fun main() {

    val glfw = GLFW()
    val window = Window("Game")
    val keyboard = Keyboard(window)
    val mouse = Mouse(window)
    val timer = Timer()

    GraphicsContext.init(Color(0f, 0f, 0f))
    GraphicsContext.enable(DEPTH_TESTING, FACE_CULLING, TEXTURE_MAPPING)

    val camera = Camera(aspectRatio = window.getAspectRatio())
    val light = DirectionalLight(Color(1.0f, 1.0f, 1.0f), Vector3(0.0f, 1.0f, -1.0f))
    val ambient = AmbientLight(Color(0.25f, 0.25f, 0.25f))

    RenderTargetManager.setWindow(window)

    val entities = ArrayList<Entity>()

    val box = ShadowBox(camera)
    ShadowRenderer.add(box)

    mouse.capture()
    window.show()
    timer.reset()

    while (!window.isClosed()) {
        glfw.poll()
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        camera.update(keyboard, mouse, timer.getDelta())

        if (keyboard.isPressed(Key.ESCAPE)) {
            if (mouse.isCaptured()) {
                mouse.release()
            } else {
                mouse.capture()
            }
        }

        val shadows = ShadowRenderer.render(camera, entities, light)

        EntityRenderer.render(camera, entities, ambient, light, shadows, Vector4())

        window.update()
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