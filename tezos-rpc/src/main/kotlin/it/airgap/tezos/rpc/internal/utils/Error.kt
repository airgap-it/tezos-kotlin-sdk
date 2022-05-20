package it.airgap.tezos.rpc.internal.utils

import it.airgap.tezos.rpc.exception.RpcException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement
import kotlin.reflect.KClass

internal fun failWithRpcException(message: String? = null, cause: Throwable? = null): Nothing =
    throw RpcException(message, cause)

internal fun failWithUnexpectedJsonType(type: KClass<out JsonElement>): Nothing =
    throw SerializationException("Could not deserialize, unexpected JSON type $type.")