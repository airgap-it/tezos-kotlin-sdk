package it.airgap.tezos.operation.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.internal.context.TezosOperationContext.asHexString
import it.airgap.tezos.operation.internal.context.TezosOperationContext.toHexString
import it.airgap.tezos.operation.internal.context.withTezosContext
import it.airgap.tezos.operation.internal.operationModule

/**
 * Forges this [operation][Operation] to [bytes][ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Operation.forgeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    forgeToBytes(tezos.operationModule.dependencyRegistry.operationBytesCoder)
}

/**
 * Unforges an [operation][Operation] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Operation.Companion.unforgeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Operation = withTezosContext {
    unforgeFromBytes(bytes, tezos.operationModule.dependencyRegistry.operationBytesCoder)
}

/**
 * Forges this [operation][Operation] to a hexadecimal [string][String] representation [with or without prefix][withHexPrefix].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Operation.forgeToString(withHexPrefix: Boolean = false, tezos: Tezos = Tezos.Default): String = withTezosContext {
    forgeToString(withHexPrefix, tezos.operationModule.dependencyRegistry.operationBytesCoder)
}

/**
 * Unforges an [operation][Operation] from [a hexadecimal String][string].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Operation.Companion.unforgeFromString(string: String, tezos: Tezos = Tezos.Default): Operation = withTezosContext {
    unforgeFromString(string, tezos.operationModule.dependencyRegistry.operationBytesCoder)
}

@InternalTezosSdkApi
public interface OperationCoderContext {
    public fun Operation.forgeToBytes(operationBytesCoder: ConsumingBytesCoder<Operation>): ByteArray =
        operationBytesCoder.encode(this)

    public fun Operation.Companion.unforgeFromBytes(bytes: ByteArray, operationBytesCoder: ConsumingBytesCoder<Operation>): Operation =
        operationBytesCoder.decode(bytes)

    public fun Operation.forgeToString(withHexPrefix: Boolean = false, operationBytesCoder: ConsumingBytesCoder<Operation>): String =
        forgeToBytes(operationBytesCoder).toHexString().asString(withHexPrefix)

    public fun Operation.Companion.unforgeFromString(string: String, operationBytesCoder: ConsumingBytesCoder<Operation>): Operation =
        unforgeFromBytes(string.asHexString().toByteArray(), operationBytesCoder)
}