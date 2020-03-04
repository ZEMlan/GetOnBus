package ru.zemlyanaya.getonbus.routing

enum class Icons {
    Heart, Home, Work
}

const val DEFAULT_KEY = -1

fun <E> Iterable<E>.replace(old: E, new: E) = map { if (it == old) new else it }