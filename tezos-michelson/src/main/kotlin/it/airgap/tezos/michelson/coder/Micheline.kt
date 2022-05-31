package it.airgap.tezos.michelson.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.asHexString
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.toHexString
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.MichelineNode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

// -- Micheline <-> JSON --

public fun <T : MichelineNode> T.toJsonString(tezos: Tezos = Tezos.Default): String = withTezosContext {
    toJsonString(tezos.michelsonModule.dependencyRegistry.michelineJsonCoder)
}

public fun MichelineNode.Companion.fromJsonString(json: String, tezos: Tezos = Tezos.Default): MichelineNode = withTezosContext {
    fromJsonString(json, tezos.michelsonModule.dependencyRegistry.michelineJsonCoder)
}

// -- Micheline <-> ByteArray --

public fun <T : MichelineNode> T.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.michelsonModule.dependencyRegistry.michelineBytesCoder)
}

public fun MichelineNode.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): MichelineNode = withTezosContext {
    decodeFromBytes(bytes, tezos.michelsonModule.dependencyRegistry.michelineBytesCoder)
}

public fun MichelineNode.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, tezos: Tezos = Tezos.Default): MichelineNode = withTezosContext {
    decodeConsumingFromBytes(bytes, tezos.michelsonModule.dependencyRegistry.michelineBytesCoder)
}

// -- Micheline <-> String --

public fun <T : MichelineNode> T.encodeToString(withHexPrefix: Boolean = false, tezos: Tezos = Tezos.Default): String = withTezosContext {
    encodeToString(tezos.michelsonModule.dependencyRegistry.michelineBytesCoder, withHexPrefix)
}

public fun MichelineNode.Companion.decodeFromString(string: String, tezos: Tezos = Tezos.Default): MichelineNode = withTezosContext {
    decodeFromString(string, tezos.michelsonModule.dependencyRegistry.michelineBytesCoder)
}

@InternalTezosSdkApi
public interface MichelineCoderContext {
    public fun <T : MichelineNode> T.toJsonString(michelineJsonCoder: Coder<MichelineNode, JsonElement>): String =
        michelineJsonCoder.encode(this).toString()

    public fun MichelineNode.Companion.fromJsonString(json: String, michelineJsonCoder: Coder<MichelineNode, JsonElement>): MichelineNode =
        michelineJsonCoder.decode(Json.parseToJsonElement(json))

    public fun <T : MichelineNode> T.encodeToBytes(michelineBytesCoder: ConsumingBytesCoder<MichelineNode>): ByteArray =
        michelineBytesCoder.encode(this)

    public fun MichelineNode.Companion.decodeFromBytes(bytes: ByteArray, michelineBytesCoder: ConsumingBytesCoder<MichelineNode>): MichelineNode =
        michelineBytesCoder.decode(bytes)

    public fun MichelineNode.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, michelineBytesCoder: ConsumingBytesCoder<MichelineNode>): MichelineNode =
        michelineBytesCoder.decodeConsuming(bytes)

    public fun <T : MichelineNode> T.encodeToString(michelineBytesCoder: ConsumingBytesCoder<MichelineNode>, withHexPrefix: Boolean = false): String =
        encodeToBytes(michelineBytesCoder).toHexString().asString(withHexPrefix)

    public fun MichelineNode.Companion.decodeFromString(string: String, michelineBytesCoder: ConsumingBytesCoder<MichelineNode>): MichelineNode =
        MichelineNode.decodeFromBytes(string.asHexString().toByteArray(), michelineBytesCoder)
}