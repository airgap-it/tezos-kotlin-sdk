package it.airgap.tezos.michelson.packer

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.asHexString
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.toHexString
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.internal.packer.Packer
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.MichelineMichelsonExpressionBuilder

/**
 * Packs [Micheline] to [bytes][ByteArray] using an optional [schema]. If specified, can [return bytes without the preceding packing tag (0x05)][dropTag].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun <T : Micheline> T.packToBytes(
    schema: Micheline? = null,
    dropTag: Boolean = false,
    tezos: Tezos = Tezos.Default,
): ByteArray = withTezosContext {
    if (dropTag) prePackToBytes(schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
    else packToBytes(schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

/**
 * Packs [Micheline] to [bytes][ByteArray] and optionally [builds schema][buildSchema] to use in the process.
 * If specified, can [return bytes without the preceding packing tag (0x05)][dropTag]. Takes an optional [tezos] object to provide context.
 * If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun <T : Micheline> T.packToBytes(
    dropTag: Boolean = false,
    tezos: Tezos = Tezos.Default,
    buildSchema: MichelineMichelsonExpressionBuilder.() -> Unit,
): ByteArray = withTezosContext {
    val schema = micheline(tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter, buildSchema)

    if (dropTag) prePackToBytes(schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
    else packToBytes(schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

/**
 * Unpacks [Micheline] from [ByteArray][bytes] using an optional [schema].
 * If specified, [skips the preceding packing tag (0x05) checks][ignoreTag] (useful for unpacking bytes packed with the `dropTag` flag set to `true`).
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Micheline.Companion.unpackFromBytes(
    bytes: ByteArray,
    schema: Micheline? = null,
    ignoreTag: Boolean = false,
    tezos: Tezos = Tezos.Default,
): Micheline = withTezosContext {
    if (ignoreTag) postUnpackFromBytes(bytes, schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
    else unpackFromBytes(bytes, schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

/**
 * Unpacks [Micheline] from [ByteArray][bytes] and optionally [builds schema][buildSchema] to use in the process.
 * If specified, [skips the preceding packing tag (0x05) checks][ignoreTag] (useful for unpacking bytes packed with the `dropTag` flag set to `true`).
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Micheline.Companion.unpackFromBytes(
    bytes: ByteArray,
    ignoreTag: Boolean = false,
    tezos: Tezos = Tezos.Default,
    buildSchema: MichelineMichelsonExpressionBuilder.() -> Unit,
): Micheline = withTezosContext {
    val schema = micheline(tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter, buildSchema)

    if (ignoreTag) postUnpackFromBytes(bytes, schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
    else unpackFromBytes(bytes, schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

/**
 * Packs [Micheline] to hexadecimal [String] representation [with or without hex prefix][withHexPrefix] using an optional [schema].
 * If specified, can [return bytes without the preceding packing tag (0x05)][dropTag]. Takes an optional [tezos] object to provide context.
 * If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun <T : Micheline> T.packToString(
    schema: Micheline? = null,
    withHexPrefix: Boolean = false,
    dropTag: Boolean = false,
    tezos: Tezos = Tezos.Default,
): String = withTezosContext {
    if (dropTag) prePackToString(schema, withHexPrefix, tezos.michelsonModule.dependencyRegistry.michelinePacker)
    else packToString(schema, withHexPrefix, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

/**
 * Packs [Micheline] to hexadecimal [String] representation [with or without hex prefix][withHexPrefix] and optionally [builds schema][buildSchema] to use in the process.
 * If specified, can [return bytes without the preceding packing tag (0x05)][dropTag]. Takes an optional [tezos] object to provide context.
 * If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun <T : Micheline> T.packToString(
    withHexPrefix: Boolean = false,
    dropTag: Boolean = false,
    tezos: Tezos = Tezos.Default,
    buildSchema: MichelineMichelsonExpressionBuilder.() -> Unit,
): String = withTezosContext {
    val schema = micheline(tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter, buildSchema)

    if (dropTag) prePackToString(schema, withHexPrefix, tezos.michelsonModule.dependencyRegistry.michelinePacker)
    else packToString(schema, withHexPrefix, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

/**
 * Unpacks [Micheline] from [String][string] using an optional [schema].
 * If specified, [skips the preceding packing tag (0x05) checks][ignoreTag] (useful for unpacking bytes packed with the `dropTag` flag set to `true`).
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Micheline.Companion.unpackFromString(
    string: String,
    schema: Micheline? = null,
    ignoreTag: Boolean = false,
    tezos: Tezos = Tezos.Default,
): Micheline = withTezosContext {
    if (ignoreTag) postUnpackFromString(string, schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
    else unpackFromString(string, schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

/**
 * Unpacks [Micheline] from [String][string] and optionally [builds schema][buildSchema] to use in the process.
 * If specified, [skips the preceding packing tag (0x05) checks][ignoreTag] (useful for unpacking bytes packed with the `dropTag` flag set to `true`).
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Micheline.Companion.unpackFromString(
    string: String,
    ignoreTag: Boolean = false,
    tezos: Tezos = Tezos.Default,
    buildSchema: MichelineMichelsonExpressionBuilder.() -> Unit,
): Micheline = withTezosContext {
    val schema = micheline(tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter, buildSchema)

    if (ignoreTag) postUnpackFromString(string, schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
    else unpackFromString(string, schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

@InternalTezosSdkApi
public interface MichelinePackerContext {
    public fun <T : Micheline> T.packToBytes(schema: Micheline? = null, michelinePacker: Packer<Micheline>): ByteArray =
        michelinePacker.pack(this, schema)

    public fun <T : Micheline> T.prePackToBytes(schema: Micheline? = null, michelinePacker: Packer<Micheline>): ByteArray =
        michelinePacker.prePack(this, schema)

    public fun Micheline.Companion.unpackFromBytes(bytes: ByteArray, schema: Micheline? = null, michelinePacker: Packer<Micheline>): Micheline =
        michelinePacker.unpack(bytes, schema)

    public fun Micheline.Companion.postUnpackFromBytes(bytes: ByteArray, schema: Micheline? = null, michelinePacker: Packer<Micheline>): Micheline =
        michelinePacker.postUnpack(bytes, schema)

    public fun <T : Micheline> T.packToString(schema: Micheline? = null, withHexPrefix: Boolean = false, michelinePacker: Packer<Micheline>): String =
        packToBytes(schema, michelinePacker).toHexString().asString(withPrefix = withHexPrefix)

    public fun <T : Micheline> T.prePackToString(schema: Micheline? = null, withHexPrefix: Boolean = false, michelinePacker: Packer<Micheline>): String =
        prePackToBytes(schema, michelinePacker).toHexString().asString(withPrefix = withHexPrefix)

    public fun Micheline.Companion.unpackFromString(string: String, schema: Micheline? = null, michelinePacker: Packer<Micheline>): Micheline =
        Micheline.unpackFromBytes(string.asHexString().toByteArray(), schema, michelinePacker)

    public fun Micheline.Companion.postUnpackFromString(string: String, schema: Micheline? = null, michelinePacker: Packer<Micheline>): Micheline =
        Micheline.postUnpackFromBytes(string.asHexString().toByteArray(), schema, michelinePacker)
}