package it.airgap.tezos.michelson.internal.coder

import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.michelson.micheline.MichelineNode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

internal class MichelineJsonCoder : Coder<MichelineNode, JsonElement> {
    override fun encode(value: MichelineNode): JsonElement = Json.encodeToJsonElement(MichelineNode.serializer(), value)
    override fun decode(value: JsonElement): MichelineNode = Json.decodeFromJsonElement(MichelineNode.serializer(), value)
}
