package com.blazeit.game.graphics.lights

import com.blazeit.game.math.Color
import com.blazeit.game.math.vectors.Vector3

/**
 * A directional light representing a light coming from a single direction.
 */
open class DirectionalLight(var color: Color = Color(), var direction: Vector3 = Vector3(0.0f, 1.0f, 0.0f))
