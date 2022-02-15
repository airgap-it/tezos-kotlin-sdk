package it.airgap.tezos.rpc.internal.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

@PublishedApi
internal inline fun <reified T : Any> Json.encodeToString(value: T): String = encodeToString(serializer(), value)

@PublishedApi
internal inline fun <reified T : Any> Json.decodeFromString(string: String): T = decodeFromString(serializer(), string)

internal fun JsonObject.getString(key: String): String =
    get(key)?.jsonPrimitive?.content ?: failWithMissingField(key)

internal fun <T> JsonObject.getSerializable(key: String, jsonDecoder: JsonDecoder, deserializer: KSerializer<T>): T =
    get(key)?.let { jsonDecoder.json.decodeFromJsonElement(deserializer, it) } ?: failWithMissingField(key)