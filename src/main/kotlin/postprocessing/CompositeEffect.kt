package com.blazeit.game.graphics.postprocessing

abstract class CompositeEffect private constructor(val effects: List<Effect>): Effect {

    constructor(vararg effects: Effect): this(effects.asList())

}