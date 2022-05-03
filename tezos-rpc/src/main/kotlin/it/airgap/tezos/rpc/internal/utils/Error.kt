package it.airgap.tezos.rpc.internal.utils

import it.airgap.tezos.rpc.type.RpcError
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement
import kotlin.reflect.KClass

internal fun failWithUnexpectedJsonType(type: KClass<out JsonElement>): Nothing =
    throw SerializationException("Could not deserialize, unexpected JSON type $type.")

internal fun failWithRpcErrors(errors: List<RpcError>): Nothing = // TODO: Better error handling
    throw Exception("Operation failed with errors: $errors")