package com.blazeit.game.resources

import java.util.concurrent.ConcurrentHashMap

open class Cache<T: Resource>(private val loader: Loader<T>) {

    private val cache = ConcurrentHashMap<String, T>()

    fun get(path: String) = cache.computeIfAbsent(path, loader::load)

    fun clear() = cache.clear()

}