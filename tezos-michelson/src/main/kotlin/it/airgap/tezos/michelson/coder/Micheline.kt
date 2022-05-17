package it.airgap.tezos.michelson.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import it.airgap.tezos.michelson.internal.michelson
import it.airgap.tezos.michelson.micheline.MichelineNode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

// -- Micheline <-> JSON --

public fun <T : MichelineNode> T.toJsonString(tezos: Tezos = Tezos.Default): String =
    toJsonString(tezos.michelson().dependencyRegistry.michelineJsonCoder)

public fun MichelineNode.Companion.fromJsonString(json: String, tezos: Tezos = Tezos.Default): MichelineNode =
    fromJsonString(json, tezos.michelson().dependencyRegistry.michelineJsonCoder)

@InternalTezosSdkApi
public fun <T : MichelineNode> T.toJsonString(michelineJsonCoder: Coder<MichelineNode, JsonElement>): String =
    michelineJsonCoder.encode(this).toString()

@InternalTezosSdkApi
public fun MichelineNode.Companion.fromJsonString(json: String, michelineJsonCoder: Coder<MichelineNode, JsonElement>): MichelineNode =
    michelineJsonCoder.decode(Json.parseToJsonElement(json))

// -- Micheline <-> ByteArray --

public fun <T : MichelineNode> T.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    encodeToBytes(tezos.michelson().dependencyRegistry.michelineBytesCoder)

public fun MichelineNode.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): MichelineNode =
    decodeFromBytes(bytes, tezos.michelson().dependencyRegistry.michelineBytesCoder)

public fun MichelineNode.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, tezos: Tezos = Tezos.Default): MichelineNode =
    decodeConsumingFromBytes(bytes, tezos.michelson().dependencyRegistry.michelineBytesCoder)

@InternalTezosSdkApi
public fun <T : MichelineNode> T.encodeToBytes(michelineBytesCoder: ConsumingBytesCoder<MichelineNode>): ByteArray =
    michelineBytesCoder.encode(this)

@InternalTezosSdkApi
public fun MichelineNode.Companion.decodeFromBytes(bytes: ByteArray, michelineBytesCoder: ConsumingBytesCoder<MichelineNode>): MichelineNode =
    michelineBytesCoder.decode(bytes)

@InternalTezosSdkApi
public fun MichelineNode.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, michelineBytesCoder: ConsumingBytesCoder<MichelineNode>): MichelineNode =
    michelineBytesCoder.decodeConsuming(bytes)

// -- Micheline <-> String --

public fun <T : MichelineNode> T.encodeToString(withHexPrefix: Boolean = false, tezos: Tezos = Tezos.Default): String =
    encodeToString(tezos.michelson().dependencyRegistry.michelineBytesCoder, withHexPrefix)

public fun MichelineNode.Companion.decodeFromString(string: String, tezos: Tezos = Tezos.Default): MichelineNode =
    decodeFromString(string, tezos.michelson().dependencyRegistry.michelineBytesCoder)

@InternalTezosSdkApi
public fun <T : MichelineNode> T.encodeToString(michelineBytesCoder: ConsumingBytesCoder<MichelineNode>, withHexPrefix: Boolean = false): String =
    encodeToBytes(michelineBytesCoder).toHexString().asString(withHexPrefix)

@InternalTezosSdkApi
public fun MichelineNode.Companion.decodeFromString(string: String, michelineBytesCoder: ConsumingBytesCoder<MichelineNode>): MichelineNode =
    MichelineNode.decodeFromBytes(string.asHexString().toByteArray(), michelineBytesCoder)