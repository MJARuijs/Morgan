package com.blazeit.game.graphics

import com.blazeit.game.graphics.models.meshes.Attribute
import com.blazeit.game.graphics.models.meshes.Layout
import com.blazeit.game.graphics.models.meshes.Mesh
import com.blazeit.game.graphics.models.meshes.Primitive

class Plane: Mesh(
        Layout(
                Primitive.TRIANGLE,
                Attribute(0, 3)
        ),
        floatArrayOf(
                -1.0f, 1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
                1.0f, 1.0f, 0.0f
        ),
        intArrayOf(
                0, 1, 2,
                0, 2, 3
        )
)