package graphics.shadows

import com.blazeit.game.entities.Entity
import com.blazeit.game.graphics.Camera
import com.blazeit.game.graphics.GraphicsContext
import com.blazeit.game.graphics.GraphicsOption
import com.blazeit.game.graphics.lights.DirectionalLight
import com.blazeit.game.graphics.rendertargets.RenderTarget
import com.blazeit.game.graphics.rendertargets.attachments.AttachmentType
import com.blazeit.game.graphics.shaders.ShaderProgram
import com.blazeit.game.graphics.shadows.ShadowBox
import com.blazeit.game.graphics.shadows.ShadowData
import com.blazeit.game.math.Color
import com.blazeit.game.math.vectors.Vector3

object ShadowRenderer {

    private val shaderProgram = ShaderProgram.load("shaders/shadow.vert", "shaders/shadow.frag")
    private val boxes = ArrayList<ShadowBox>()
    private val renderTarget = RenderTarget(4096, 4096, AttachmentType.DEPTH_TEXTURE)
//    private lateinit var renderTarget: RenderTarget

    fun add(vararg boxes: ShadowBox) = ShadowRenderer.boxes.addAll(boxes)

    fun render(camera: Camera, entities: List<Entity>, light: DirectionalLight): List<ShadowData> {

        val shadowData = ArrayList<ShadowData>()

//        renderTarget = RenderTargetManager.getAvailableTarget(AttachmentType.DEPTH_TEXTURE, width = 4096, height = 4096)
        renderTarget.start()
        renderTarget.clear()

        for (box in boxes) {

            box.updateBox(camera, light)

            shaderProgram.start()
            shaderProgram.set("projection", box.getProjectionMatrix())
            shaderProgram.set("view", box.getViewMatrix())
            shaderProgram.set("color", Color(1.0f, 1.0f, 1.0f, 1.0f))
            GraphicsContext.disable(GraphicsOption.FACE_CULLING)

            for (entity in entities) {
                val model = entity.model
                val transformation = entity.transformation
                shaderProgram.set("model", transformation)
                for (shape in model.shapes) {
                    shape.mesh.draw()
                }
            }

            GraphicsContext.enable(GraphicsOption.FACE_CULLING)

            shaderProgram.stop()

            shadowData += ShadowData(
                    box.translation,
                    box.getProjectionMatrix(),
                    box.getViewMatrix(),
                    box.maxDistance,
                    box.offset,
                    renderTarget.getDepthMap(),
                    Vector3(box.width(), box.height(), box.depth()))
        }

        GraphicsContext.enable(GraphicsOption.FACE_CULLING)

        renderTarget.stop()
        return shadowData
    }
}