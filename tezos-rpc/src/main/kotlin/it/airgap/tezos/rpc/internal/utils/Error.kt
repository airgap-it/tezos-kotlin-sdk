package it.airgap.tezos.rpc.internal.utils

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement
import kotlin.reflect.KClass

internal fun failWithUnexpectedJsonType(type: KClass<out JsonElement>): Nothing =
    throw SerializationException("Could not deserialize, unexpected JSON type $type.")

internal fun failWithMissingField(name: String): Nothing =
    throw SerializationException("Could not deserialize, `$name` field is missing.")