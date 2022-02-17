package it.airgap.tezos.michelson.internal.utils

import kotlinx.serialization.json.JsonObject

internal fun JsonObject.containsOneOfKeys(vararg keys: String): Boolean = keys.any { containsKey(it) }