package com.blazeit.game.graphics.postprocessing.effects

import com.blazeit.game.graphics.postprocessing.CompositeEffect

class GaussianBlur(strength: Float): CompositeEffect(HorizontalBlur(strength), VerticalBlur(strength))