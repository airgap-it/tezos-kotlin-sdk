package it.airgap.tezos.michelson

import it.airgap.tezos.core.TezosSdk
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import it.airgap.tezos.michelson.internal.di.michelson
import it.airgap.tezos.michelson.internal.packer.MichelinePacker
import it.airgap.tezos.michelson.micheline.MichelineNode

// -- Micheline <-> ByteArray --

@InternalTezosSdkApi
public fun <T : MichelineNode> T.packToBytes(
    schema: MichelineNode? = null,
    michelinePacker: MichelinePacker = TezosSdk.instance.dependencyRegistry.michelson().michelinePacker,
): ByteArray = michelinePacker.pack(this, schema)

@InternalTezosSdkApi
public fun MichelineNode.Companion.unpackFromBytes(
    bytes: ByteArray,
    schema: MichelineNode? = null,
    michelinePacker: MichelinePacker = TezosSdk.instance.dependencyRegistry.michelson().michelinePacker,
): MichelineNode = michelinePacker.unpack(bytes, schema)

// -- Micheline <-> String --

@InternalTezosSdkApi
public fun <T : MichelineNode> T.packToString(
    schema: MichelineNode? = null,
    withHexPrefix: Boolean = false,
    michelinePacker: MichelinePacker = TezosSdk.instance.dependencyRegistry.michelson().michelinePacker,
): String = packToBytes(schema, michelinePacker).toHexString().asString(withPrefix = withHexPrefix)

@InternalTezosSdkApi
public fun MichelineNode.Companion.unpackFromString(
    string: String,
    schema: MichelineNode? = null,
    michelinePacker: MichelinePacker = TezosSdk.instance.dependencyRegistry.michelson().michelinePacker,
): MichelineNode = MichelineNode.unpackFromBytes(string.asHexString().toByteArray(), schema, michelinePacker)
