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
 * Packs [Micheline] to [bytes][ByteArray] using an optional [schema].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun <T : Micheline> T.packToBytes(schema: Micheline? = null, tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    packToBytes(schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

/**
 * Packs [Micheline] to [bytes][ByteArray] and optionally [buildSchema] to use in the process.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun <T : Micheline> T.packToBytes(tezos: Tezos = Tezos.Default, buildSchema: MichelineMichelsonExpressionBuilder.() -> Unit): ByteArray = withTezosContext {
    packToBytes(micheline(tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter, buildSchema), tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

/**
 * Unpacks [Micheline] from [ByteArray][bytes] using an optional [schema].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Micheline.Companion.unpackFromBytes(bytes: ByteArray, schema: Micheline? = null, tezos: Tezos = Tezos.Default): Micheline = withTezosContext {
    unpackFromBytes(bytes, schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

/**
 * Unpacks [Micheline] from [ByteArray][bytes] and optionally [buildSchema] to use in the process.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Micheline.Companion.unpackFromBytes(
    bytes: ByteArray,
    tezos: Tezos = Tezos.Default,
    buildSchema: MichelineMichelsonExpressionBuilder.() -> Unit,
): Micheline = withTezosContext {
    unpackFromBytes(
        bytes,
        micheline(tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter, buildSchema),
        tezos.michelsonModule.dependencyRegistry.michelinePacker,
    )
}

/**
 * Packs [Micheline] to hexadecimal [String] representation [with or without hex prefix][withHexPrefix] using an optional [schema].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun <T : Micheline> T.packToString(schema: Micheline? = null, withHexPrefix: Boolean = false, tezos: Tezos = Tezos.Default): String = withTezosContext {
    packToString(schema, withHexPrefix, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

/**
 * Packs [Micheline] to hexadecimal [String] representation [with or without hex prefix][withHexPrefix] and optionally [buildSchema] to use in the process.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun <T : Micheline> T.packToString(withHexPrefix: Boolean = false, tezos: Tezos = Tezos.Default, buildSchema: MichelineMichelsonExpressionBuilder.() -> Unit): String = withTezosContext {
    packToString(micheline(tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter, buildSchema), withHexPrefix, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

/**
 * Unpacks [Micheline] from [String][string] using an optional [schema].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Micheline.Companion.unpackFromString(string: String, schema: Micheline? = null, tezos: Tezos = Tezos.Default): Micheline = withTezosContext {
    unpackFromString(string, schema, tezos.michelsonModule.dependencyRegistry.michelinePacker)
}

/**
 * Unpacks [Micheline] from [String][string] and optionally [buildSchema] to use in the process.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Micheline.Companion.unpackFromString(
    string: String,
    tezos: Tezos = Tezos.Default,
    buildSchema: MichelineMichelsonExpressionBuilder.() -> Unit,
): Micheline = withTezosContext {
    unpackFromString(
        string,
        micheline(tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter, buildSchema),
        tezos.michelsonModule.dependencyRegistry.michelinePacker,
    )
}

@InternalTezosSdkApi
public interface MichelinePackerContext {
    public fun <T : Micheline> T.packToBytes(schema: Micheline? = null, michelinePacker: Packer<Micheline>): ByteArray =
        michelinePacker.pack(this, schema)

    public fun Micheline.Companion.unpackFromBytes(bytes: ByteArray, schema: Micheline? = null, michelinePacker: Packer<Micheline>): Micheline =
        michelinePacker.unpack(bytes, schema)

    public fun <T : Micheline> T.packToString(schema: Micheline? = null, withHexPrefix: Boolean = false, michelinePacker: Packer<Micheline>): String =
        packToBytes(schema, michelinePacker).toHexString().asString(withPrefix = withHexPrefix)

    public fun Micheline.Companion.unpackFromString(string: String, schema: Micheline? = null, michelinePacker: Packer<Micheline>): Micheline =
        Micheline.unpackFromBytes(string.asHexString().toByteArray(), schema, michelinePacker)
}