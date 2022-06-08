package it.airgap.tezos.michelson.internal.utils

import it.airgap.tezos.michelson.Michelson
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement
import kotlin.reflect.KClass

@PublishedApi
internal fun failWithUnexpectedMichelsonPrim(prim: KClass<out Michelson.Prim>, caller: String): Nothing =
    error("Unexpected Michelson prim (${prim.qualifiedName?.removePrefix(".Companion")?.substringAfterLast(".")}) for `$caller`.")

internal fun failWithUnexpectedJsonType(type: KClass<out JsonElement>): Nothing =
    throw SerializationException("Could not deserialize, unexpected JSON type $type.")