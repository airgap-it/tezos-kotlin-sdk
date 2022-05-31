package it.airgap.tezos.operation.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.internal.context.TezosOperationContext.asHexString
import it.airgap.tezos.operation.internal.context.TezosOperationContext.toHexString
import it.airgap.tezos.operation.internal.context.withTezosContext
import it.airgap.tezos.operation.internal.operationModule

// -- OperationContent <-> ByteArray --

public fun OperationContent.forgeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    forgeToBytes(tezos.operationModule.dependencyRegistry.operationContentBytesCoder)
}

public fun OperationContent.Companion.unforgeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): OperationContent = withTezosContext {
    unforgeFromBytes(bytes, tezos.operationModule.dependencyRegistry.operationContentBytesCoder)
}

// -- OperationContent <-> String --

public fun OperationContent.forgeToString(withHexPrefix: Boolean = false, tezos: Tezos = Tezos.Default): String = withTezosContext {
    forgeToString(withHexPrefix, tezos.operationModule.dependencyRegistry.operationContentBytesCoder)
}

public fun OperationContent.Companion.unforgeFromString(string: String, tezos: Tezos = Tezos.Default): OperationContent = withTezosContext {
    unforgeFromString(string, tezos.operationModule.dependencyRegistry.operationContentBytesCoder)
}

@InternalTezosSdkApi
public interface OperationContentCoderContext {
    public fun OperationContent.forgeToBytes(operationContentBytesCoder: ConsumingBytesCoder<OperationContent>): ByteArray =
        operationContentBytesCoder.encode(this)

    public fun OperationContent.Companion.unforgeFromBytes(bytes: ByteArray, operationContentBytesCoder: ConsumingBytesCoder<OperationContent>): OperationContent =
        operationContentBytesCoder.decode(bytes)

    public fun OperationContent.forgeToString(withHexPrefix: Boolean = false, operationContentBytesCoder: ConsumingBytesCoder<OperationContent>): String =
        forgeToBytes(operationContentBytesCoder).toHexString().asString(withHexPrefix)

    public fun OperationContent.Companion.unforgeFromString(string: String, operationContentBytesCoder: ConsumingBytesCoder<OperationContent>): OperationContent =
        unforgeFromBytes(string.asHexString().toByteArray(), operationContentBytesCoder)
}