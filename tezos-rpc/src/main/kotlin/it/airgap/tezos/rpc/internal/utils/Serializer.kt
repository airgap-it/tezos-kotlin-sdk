package it.airgap.tezos.rpc.internal.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.full.createType

@PublishedApi
@Suppress("UNCHECKED_CAST")
internal inline fun <reified T : Any> serializer(): KSerializer<T> {
    val type = T::class.createType()
    return serializer(type) as KSerializer<T>
}
