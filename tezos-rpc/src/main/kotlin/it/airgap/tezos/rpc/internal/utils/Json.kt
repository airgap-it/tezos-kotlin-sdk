package it.airgap.tezos.rpc.internal.utils

import kotlinx.serialization.json.Json

@PublishedApi
internal inline fun <reified T : Any> Json.encodeToString(value: T): String = encodeToString(serializer(), value)

@PublishedApi
internal inline fun <reified T : Any> Json.decodeFromString(string: String): T = decodeFromString(serializer(), string)
