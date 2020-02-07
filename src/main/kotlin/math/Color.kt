package com.blazeit.game.math

/**
 * A color defined by w red, green, and blue color channels and an alpha value.
 * @param r the red color channel's value (default 0.0f)
 * @param g the green color channel's value (default 0.0f)
 * @param b the blue color channel's value (default 0.0f)
 * @param b the alpha value (default 1.0f)
 * @constructor
 */
data class Color(var r: Float = 0.0f, var g: Float = 0.0f, var b: Float = 0.0f, var a: Float = 1.0f) {

    constructor(r: Int, g: Int, b: Int, a: Int = 255): this(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f)

    operator fun plus(other: Color) = Color(r + other.r, g + other.g, b + other.b, a + other.b)

    operator fun times(other: Color) = Color(r * other.r, g * other.g, b * other.b, a * other.b)

    operator fun times(factor: Float) = Color(r * factor, g * factor, b * factor, a * factor)

    operator fun div(factor: Float) = Color(r / factor, g / factor, b / factor, a / factor)

    fun toArray(): FloatArray = floatArrayOf(r, g, b, a)

    companion object {

        fun average(first: Color, second: Color): Color = (first + second) / 2.0f

        fun mix(first: Color, second: Color, weight: Float) = (first * (1.0f - weight)) + (second * weight)

    }

}