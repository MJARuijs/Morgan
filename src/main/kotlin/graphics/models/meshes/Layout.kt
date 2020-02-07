package com.blazeit.game.graphics.models.meshes

data class Layout(val primitive: Primitive, val attributes: List<Attribute>) {

    constructor(primitive: Primitive, vararg attributes: Attribute): this(primitive, attributes.asList())

    val stride = attributes.sumBy(Attribute::size)

}