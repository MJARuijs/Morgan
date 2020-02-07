package com.blazeit.game.math

import com.blazeit.game.math.vectors.Vector3

enum class Axis(val normal: Vector3) {

    X(Vector3(1.0f, 0.0f, 0.0f)),
    Y(Vector3(0.0f, 1.0f, 0.0f)),
    Z(Vector3(0.0f, 0.0f, 1.0f))

}