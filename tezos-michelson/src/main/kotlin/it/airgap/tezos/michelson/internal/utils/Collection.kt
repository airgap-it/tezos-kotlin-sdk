package it.airgap.tezos.michelson.internal.utils

import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.allIsInstance
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.anyIsInstance
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.firstInstanceOfOrNull
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.secondInstanceOfOrNull

internal inline fun <reified T: Any> Collection<*>.firstInstanceOfOrNull(): T? = firstInstanceOfOrNull(T::class)
internal inline fun <reified T: Any> Collection<*>.secondInstanceOfOrNull(): T? = secondInstanceOfOrNull(T::class)

internal inline fun <reified T: Any> Collection<*>.allIsInstance(): Boolean = allIsInstance(T::class)
internal inline fun <reified T: Any> Collection<*>.anyIsInstance(): Boolean = anyIsInstance(T::class)

internal fun <T> List<T>.second(): T =
    if (size < 2) throw NoSuchElementException("List does not have enough elements.")
    else this[1]

internal fun <T> List<T>.third(): T =
    if (size < 3) throw NoSuchElementException("List does not have enough elements.")
    else this[2]
