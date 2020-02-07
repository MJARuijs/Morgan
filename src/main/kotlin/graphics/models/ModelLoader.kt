package com.blazeit.game.graphics.models

import com.blazeit.game.graphics.models.meshes.Attribute
import com.blazeit.game.graphics.models.meshes.Layout
import com.blazeit.game.graphics.models.meshes.Mesh
import com.blazeit.game.graphics.models.meshes.Primitive
import com.blazeit.game.math.Color
import com.blazeit.game.math.matrices.Matrix3
import com.blazeit.game.math.matrices.Matrix4
import com.blazeit.game.math.vectors.Vector2
import com.blazeit.game.math.vectors.Vector3
import com.blazeit.game.util.File
import com.blazeit.game.resources.Loader
import org.lwjgl.BufferUtils
import org.lwjgl.assimp.*
import org.lwjgl.assimp.Assimp.*

class ModelLoader: Loader<Model> {

    override fun load(path: String): Model {

        val scene = loadScene(path)
        val root = scene.mRootNode() ?: throw Exception("Scene does not contain root node")
        val shapes = parseNode(scene, root)

        return Model(shapes)
    }

    private fun loadScene(path: String) = aiImportFile(
            File(path).getPath(),
            aiProcess_Triangulate or aiProcess_OptimizeGraph or aiProcess_RemoveRedundantMaterials
    ) ?: throw Exception("Could not load scene: $path")

    private fun parseNode(scene: AIScene, node: AINode): List<Shape> {

        val materials = ArrayList<Material>()
        val aiMaterials = scene.mMaterials()
        for (index in 0 until scene.mNumMaterials()) {
            materials += parseMaterial(AIMaterial.create(aiMaterials!!.get(index)))
        }

        val shapes = ArrayList<Shape>()
        val aiMeshes = scene.mMeshes()
        for (i in 0 until scene.mNumMeshes()) {

            val aiTransformation = node.mTransformation()
            val aiMesh = AIMesh.create(aiMeshes!!.get(i))

            val material = materials[aiMesh.mMaterialIndex()]
            val transformation = parseMatrix(aiTransformation)
            val mesh = parseData(aiMesh, transformation)

            shapes += Shape(mesh, material)
        }

//        val aiNodes = node.mChildren()
//        for (i in 0 until node.mNumChildren()) {
//            shapes += parseNode(scene, AINode.create(aiNodes!!.get(i)))
//        }

        return shapes
    }

    private fun parseData(aiMesh: AIMesh, transformation: Matrix4): Mesh {

        var containsTexCoords = false
        var containsNormals = false

        var vertices = FloatArray(0)
        var indices = IntArray(0)

        val aiVertices = aiMesh.mVertices()
        val aiTexCoords = aiMesh.mTextureCoords(0)
        val aiNormals = aiMesh.mNormals()

        for (i in 0 until aiMesh.mNumVertices()) {

            val aiVertex = aiVertices.get()
            val aiTexture = aiTexCoords?.get()
            val aiNormal = aiNormals?.get()

            val position = transformation.dot(Vector3(aiVertex.x(), aiVertex.y(), aiVertex.z()))
            vertices += position.x
            vertices += position.y
            vertices += position.z

            if (aiTexture != null) {
                val texCoord = Vector2(aiTexture.x(), aiTexture.y())
                vertices += texCoord.x
                vertices += texCoord.y
                containsTexCoords = true
            }

            if (aiNormal != null) {
                val normal = Matrix3(transformation).dot(Vector3(aiNormal.x(), aiNormal.y(), aiNormal.z()))
                vertices += normal.x
                vertices += normal.y
                vertices += normal.z
                containsNormals = true
            }
        }

        for (i in 0 until aiMesh.mNumFaces()) {
            val face = aiMesh.mFaces().get(i)

            val intBuffer = face.mIndices()

            while (intBuffer.remaining() > 0) {
                val index = intBuffer.get()
                indices += index
            }
        }

        val attributes = ArrayList<Attribute>()
        attributes += Attribute(0, 3)
        if (containsTexCoords) attributes += Attribute(1, 2)
        if (containsNormals) attributes += Attribute(2, 3)

        val layout = Layout(Primitive.TRIANGLE, attributes)

        return Mesh(layout, vertices, indices)
    }

    private fun parseMaterial(aiMaterial: AIMaterial) = Material(
            getColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE),
            getColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR),
            getFloat(aiMaterial, AI_MATKEY_SHININESS)
    )

    private fun getColor(material: AIMaterial, key: String): Color {
        val aiColor = AIColor4D.create()
        val result = aiGetMaterialColor(material, key, aiTextureType_NONE, 0, aiColor)
        return if (result == 0) {
            Color(aiColor.r(), aiColor.g(), aiColor.b(), aiColor.a())
        } else {
            Color()
        }
    }

    private fun getFloat(material: AIMaterial, key: String): Float {
        val intBuffer = BufferUtils.createIntBuffer(1)
        val floatBuffer = BufferUtils.createFloatBuffer(1)
        val result = aiGetMaterialFloatArray(material, key, aiTextureType_NONE, 1, floatBuffer, intBuffer)
        return if (result == 0) {
            floatBuffer.get()
        } else {
            50.0f
        }
    }

    private fun parseMatrix(aiMatrix: AIMatrix4x4): Matrix4 {
        return Matrix4(floatArrayOf(
                aiMatrix.a1(), aiMatrix.a2(), aiMatrix.a3(), aiMatrix.a4(),
                aiMatrix.b1(), aiMatrix.b2(), aiMatrix.b3(), aiMatrix.b4(),
                aiMatrix.c1(), aiMatrix.c2(), aiMatrix.c3(), aiMatrix.c4(),
                aiMatrix.d1(), aiMatrix.d2(), aiMatrix.d3(), aiMatrix.d4())
        )
    }

}