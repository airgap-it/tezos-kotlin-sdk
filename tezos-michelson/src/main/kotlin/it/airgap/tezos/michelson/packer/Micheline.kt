package it.airgap.tezos.michelson.packer

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.internal.packer.Packer
import it.airgap.tezos.michelson.micheline.MichelineNode

// -- Micheline <-> ByteArray --

public fun <T : MichelineNode> T.packToBytes(schema: MichelineNode? = null, tezos: Tezos = Tezos.Default): ByteArray =
    packToBytes(schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)

public fun MichelineNode.Companion.unpackFromBytes(bytes: ByteArray, schema: MichelineNode? = null, tezos: Tezos = Tezos.Default): MichelineNode =
    unpackFromBytes(bytes, schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)

@InternalTezosSdkApi
public fun <T : MichelineNode> T.packToBytes(schema: MichelineNode? = null, michelinePacker: Packer<MichelineNode>): ByteArray =
    michelinePacker.pack(this, schema)

@InternalTezosSdkApi
public fun MichelineNode.Companion.unpackFromBytes(bytes: ByteArray, schema: MichelineNode? = null, michelinePacker: Packer<MichelineNode>): MichelineNode =
    michelinePacker.unpack(bytes, schema)

// -- Micheline <-> String --

public fun <T : MichelineNode> T.packToString(schema: MichelineNode? = null, withHexPrefix: Boolean = false, tezos: Tezos = Tezos.Default): String =
    packToString(schema, withHexPrefix, tezos.michelsonModule.dependencyRegistry.michelinePacker)

public fun MichelineNode.Companion.unpackFromString(string: String, schema: MichelineNode? = null, tezos: Tezos = Tezos.Default): MichelineNode =
    unpackFromString(string, schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)

@InternalTezosSdkApi
public fun <T : MichelineNode> T.packToString(schema: MichelineNode? = null, withHexPrefix: Boolean = false, michelinePacker: Packer<MichelineNode>): String =
    packToBytes(schema, michelinePacker).toHexString().asString(withPrefix = withHexPrefix)

@InternalTezosSdkApi
public fun MichelineNode.Companion.unpackFromString(string: String, schema: MichelineNode? = null, michelinePacker: Packer<MichelineNode>): MichelineNode =
    MichelineNode.unpackFromBytes(string.asHexString().toByteArray(), schema, michelinePacker)