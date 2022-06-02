package it.airgap.tezos.michelson.packer

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.asHexString
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.toHexString
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.internal.packer.Packer
import it.airgap.tezos.michelson.micheline.MichelineNode

/**
 * Packs [MichelineNode] to [bytes][ByteArray] using an optional [schema].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/michelson/Micheline/MichelineSamples.Usage#pack` for a sample usage.
 */
public fun <T : MichelineNode> T.packToBytes(schema: MichelineNode? = null, tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    packToBytes(schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

/**
 * Unpacks [MichelineNode] from [ByteArray][bytes] using an optional [schema].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/michelson/Micheline/MichelineSamples.Usage#unpack` for a sample usage.
 */
public fun MichelineNode.Companion.unpackFromBytes(bytes: ByteArray, schema: MichelineNode? = null, tezos: Tezos = Tezos.Default): MichelineNode = withTezosContext {
    unpackFromBytes(bytes, schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

/**
 * Packs [MichelineNode] to hexadecimal [String] representation [with or without hex prefix][withHexPrefix] using an optional [schema].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/michelson/Micheline/MichelineSamples.Usage#pack` for a sample usage.
 */
public fun <T : MichelineNode> T.packToString(schema: MichelineNode? = null, withHexPrefix: Boolean = false, tezos: Tezos = Tezos.Default): String = withTezosContext {
    packToString(schema, withHexPrefix, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

/**
 * Unpacks [MichelineNode] from [String][string] using an optional [schema].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/michelson/Micheline/MichelineSamples.Usage#unpack` for a sample usage.
 */
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