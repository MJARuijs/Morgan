package com.blazeit.game.math.vectors

import kotlin.math.abs

data class Vector2(var x: Float = 0.0f, var y: Float = 0.0f): Vector<Vector2> {

    constructor(x: Int, y: Int): this(x.toFloat(), y.toFloat())

    override operator fun get(index: Int) = when (index) {
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException("Vector component's index was out of bounds: $index")
    }

    override operator fun set(index: Int, value: Float) = when (index) {
        0 -> x = value
        1 -> y = value
        else -> throw IndexOutOfBoundsException("Vector component's index was out of bounds: $index")
    }

    override operator fun unaryMinus() = Vector2(-x, -y)

    override operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)

    override operator fun minus(other: Vector2) = Vector2(x - other.x, y - other.y)

    override operator fun times(other: Vector2) = Vector2(x * other.x, y * other.y)

    override operator fun div(other: Vector2) = Vector2(x / other.x, y / other.y)

    override operator fun times(factor: Float) = Vector2(x * factor, y * factor)

    override operator fun div(factor: Float) = Vector2(x / factor, y / factor)

    override infix fun dot(other: Vector2) = (x * other.x) + (y * other.y)

    /**
     * @return the conjugate (orthogonal/perpendicular) of the vector.
     */
    fun conjugate() = Vector2(y, -x)

    override fun absolute() = Vector2(abs(x), abs(y))

    override fun normalize() {
        val norm = norm()
        if (x != 0.0f) {
            x /= norm
        }
        if (y != 0.0f) {
            y /= norm
        }
    }

    override fun toString() = "<$x, $y>"

    override fun toArray() = floatArrayOf(x, y)

}
