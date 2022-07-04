package it.airgap.tezos.michelson.internal.coder

import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.michelson.micheline.Micheline
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

internal class MichelineJsonCoder : Coder<Micheline, JsonElement> {
    override fun encode(value: Micheline): JsonElement = Json.encodeToJsonElement(Micheline.serializer(), value)
    override fun decode(value: JsonElement): Micheline = Json.decodeFromJsonElement(Micheline.serializer(), value)
}
