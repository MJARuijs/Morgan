package com.blazeit.game.graphics.models

import com.blazeit.game.graphics.models.meshes.Mesh
import com.blazeit.game.resources.Resource

data class Model(val shapes: List<Shape>): Resource {

    override fun destroy() {
        shapes.map(Shape::mesh).distinct().forEach(Mesh::destroy)
    }

}