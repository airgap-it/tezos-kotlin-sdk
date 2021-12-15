package it.airgap.tezos.michelson

import it.airgap.tezos.michelson.internal.coder.MichelineJsonCoder
import it.airgap.tezos.michelson.micheline.MichelineNode
import kotlinx.serialization.json.Json

public fun <T : MichelineNode> T.toJsonString(): String = MichelineJsonCoder.encode(this).toString()
public fun MichelineNode.Companion.fromJsonString(json: String): MichelineNode = MichelineJsonCoder.decode(Json.parseToJsonElement(json))