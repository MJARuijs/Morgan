package com.blazeit.game.devices

/**
 * A collection of valid Window modes.
 */
enum class WindowMode {

    /**
     * Creates an undecorated window that can be resized and focused.
     */
    Windowed,

    /**
     * Creates w full screen window that can easily be tabbed out of.
     */
    Borderless,

    /**
     * Creates w full screen window that provides the best performance.
     */
    FullScreen

}