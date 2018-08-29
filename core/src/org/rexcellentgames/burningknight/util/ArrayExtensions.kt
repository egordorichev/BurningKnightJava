package org.rexcellentgames.burningknight.util

fun <E> Array<E>.random(): E? = if (size > 0) get(Random.newInt(size - 1)) else null