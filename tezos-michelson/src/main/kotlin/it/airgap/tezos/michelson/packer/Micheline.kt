package it.airgap.tezos.michelson.packer

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.asHexString
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.toHexString
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.internal.packer.Packer
import it.airgap.tezos.michelson.micheline.MichelineNode

// -- Micheline <-> ByteArray --

public fun <T : MichelineNode> T.packToBytes(schema: MichelineNode? = null, tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    packToBytes(schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

public fun MichelineNode.Companion.unpackFromBytes(bytes: ByteArray, schema: MichelineNode? = null, tezos: Tezos = Tezos.Default): MichelineNode = withTezosContext {
    unpackFromBytes(bytes, schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

// -- Micheline <-> String --

public fun <T : MichelineNode> T.packToString(schema: MichelineNode? = null, withHexPrefix: Boolean = false, tezos: Tezos = Tezos.Default): String = withTezosContext {
    packToString(schema, withHexPrefix, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

public fun MichelineNode.Companion.unpackFromString(string: String, schema: MichelineNode? = null, tezos: Tezos = Tezos.Default): MichelineNode = withTezosContext {
    unpackFromString(string, schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

@InternalTezosSdkApi
public interface MichelinePackerContext {
    public fun <T : MichelineNode> T.packToBytes(schema: MichelineNode? = null, michelinePacker: Packer<MichelineNode>): ByteArray =
        michelinePacker.pack(this, schema)

    public fun MichelineNode.Companion.unpackFromBytes(bytes: ByteArray, schema: MichelineNode? = null, michelinePacker: Packer<MichelineNode>): MichelineNode =
        michelinePacker.unpack(bytes, schema)

    public fun <T : MichelineNode> T.packToString(schema: MichelineNode? = null, withHexPrefix: Boolean = false, michelinePacker: Packer<MichelineNode>): String =
        packToBytes(schema, michelinePacker).toHexString().asString(withPrefix = withHexPrefix)

    public fun MichelineNode.Companion.unpackFromString(string: String, schema: MichelineNode? = null, michelinePacker: Packer<MichelineNode>): MichelineNode =
        MichelineNode.unpackFromBytes(string.asHexString().toByteArray(), schema, michelinePacker)
}