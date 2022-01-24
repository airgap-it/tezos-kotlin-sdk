package it.airgap.tezos.michelson

import it.airgap.tezos.core.TezosSdk
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import it.airgap.tezos.michelson.internal.coder.MichelineBytesCoder
import it.airgap.tezos.michelson.internal.coder.MichelineJsonCoder
import it.airgap.tezos.michelson.internal.di.scoped
import it.airgap.tezos.michelson.micheline.MichelineNode
import kotlinx.serialization.json.Json

// -- Micheline: JSON --

public fun <T : MichelineNode> T.toJsonString(): String = toJsonString(TezosSdk.instance.dependencyRegistry.scoped().michelineJsonCoder)
internal fun <T : MichelineNode> T.toJsonString(michelineJsonCoder: MichelineJsonCoder): String = michelineJsonCoder.encode(this).toString()

public fun MichelineNode.Companion.fromJsonString(json: String): MichelineNode = fromJsonString(json, TezosSdk.instance.dependencyRegistry.scoped().michelineJsonCoder)
internal fun MichelineNode.Companion.fromJsonString(json: String, michelineJsonCoder: MichelineJsonCoder): MichelineNode = michelineJsonCoder.decode(Json.parseToJsonElement(json))

// -- Micheline: ByteArray --

public fun <T : MichelineNode> T.encodeToBytes(): ByteArray = encodeToBytes(TezosSdk.instance.dependencyRegistry.scoped().michelineBytesCoder)
internal fun <T : MichelineNode> T.encodeToBytes(michelineBytesCoder: MichelineBytesCoder): ByteArray = michelineBytesCoder.encode(this)

public fun MichelineNode.Companion.decodeFromBytes(bytes: ByteArray): MichelineNode = decodeFromBytes(bytes, TezosSdk.instance.dependencyRegistry.scoped().michelineBytesCoder)
internal fun MichelineNode.Companion.decodeFromBytes(bytes: ByteArray, michelineBytesCoder: MichelineBytesCoder): MichelineNode = michelineBytesCoder.decode(bytes)

// -- Micheline: String --

public fun <T : MichelineNode> T.encodeToString(withHexPrefix: Boolean = false): String = encodeToString(TezosSdk.instance.dependencyRegistry.scoped().michelineBytesCoder, withHexPrefix)
internal fun <T : MichelineNode> T.encodeToString(michelineBytesCoder: MichelineBytesCoder, withHexPrefix: Boolean = false): String = encodeToBytes(michelineBytesCoder).toHexString().asString(withHexPrefix)

public fun MichelineNode.Companion.decodeFromString(string: String): MichelineNode = decodeFromString(string, TezosSdk.instance.dependencyRegistry.scoped().michelineBytesCoder)
internal fun MichelineNode.Companion.decodeFromString(string: String, michelineBytesCoder: MichelineBytesCoder): MichelineNode = MichelineNode.decodeFromBytes(string.asHexString().toByteArray(), michelineBytesCoder)
