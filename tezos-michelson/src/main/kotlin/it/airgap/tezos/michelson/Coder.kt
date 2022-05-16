package it.airgap.tezos.michelson

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import it.airgap.tezos.michelson.internal.coder.MichelineBytesCoder
import it.airgap.tezos.michelson.internal.coder.MichelineJsonCoder
import it.airgap.tezos.michelson.internal.di.michelson
import it.airgap.tezos.michelson.micheline.MichelineNode
import kotlinx.serialization.json.Json

// -- Micheline <-> JSON --

public fun <T : MichelineNode> T.toJsonString(
    michelineJsonCoder: MichelineJsonCoder = TezosSdk.instance.dependencyRegistry.michelson().michelineJsonCoder,
): String = michelineJsonCoder.encode(this).toString()

public fun MichelineNode.Companion.fromJsonString(
    json: String,
    michelineJsonCoder: MichelineJsonCoder = TezosSdk.instance.dependencyRegistry.michelson().michelineJsonCoder,
): MichelineNode = michelineJsonCoder.decode(Json.parseToJsonElement(json))

// -- Micheline <-> ByteArray --

public fun <T : MichelineNode> T.encodeToBytes(
    michelineBytesCoder: MichelineBytesCoder = TezosSdk.instance.dependencyRegistry.michelson().michelineBytesCoder,
): ByteArray = michelineBytesCoder.encode(this)

public fun MichelineNode.Companion.decodeFromBytes(
    bytes: ByteArray,
    michelineBytesCoder: MichelineBytesCoder = TezosSdk.instance.dependencyRegistry.michelson().michelineBytesCoder,
): MichelineNode = michelineBytesCoder.decode(bytes)

public fun MichelineNode.Companion.decodeConsumingFromBytes(
    bytes: MutableList<Byte>,
    michelineBytesCoder: MichelineBytesCoder = TezosSdk.instance.dependencyRegistry.michelson().michelineBytesCoder,
): MichelineNode = michelineBytesCoder.decodeConsuming(bytes)

// -- Micheline <-> String --

public fun <T : MichelineNode> T.encodeToString(
    michelineBytesCoder: MichelineBytesCoder = TezosSdk.instance.dependencyRegistry.michelson().michelineBytesCoder,
    withHexPrefix: Boolean = false,
): String = encodeToBytes(michelineBytesCoder).toHexString().asString(withHexPrefix)

public fun MichelineNode.Companion.decodeFromString(
    string: String,
    michelineBytesCoder: MichelineBytesCoder = TezosSdk.instance.dependencyRegistry.michelson().michelineBytesCoder,
): MichelineNode = MichelineNode.decodeFromBytes(string.asHexString().toByteArray(), michelineBytesCoder)
