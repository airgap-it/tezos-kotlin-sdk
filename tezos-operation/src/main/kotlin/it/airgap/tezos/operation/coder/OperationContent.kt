package it.airgap.tezos.operation.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.internal.operation

// -- OperationContent <-> ByteArray --

public fun OperationContent.forgeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    forgeToBytes(tezos.operation().dependencyRegistry.operationContentBytesCoder)

public fun OperationContent.Companion.unforgeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): OperationContent =
    unforgeFromBytes(bytes, tezos.operation().dependencyRegistry.operationContentBytesCoder)

@InternalTezosSdkApi
public fun OperationContent.forgeToBytes(operationContentBytesCoder: ConsumingBytesCoder<OperationContent>): ByteArray =
    operationContentBytesCoder.encode(this)

@InternalTezosSdkApi
public fun OperationContent.Companion.unforgeFromBytes(bytes: ByteArray, operationContentBytesCoder: ConsumingBytesCoder<OperationContent>): OperationContent =
    operationContentBytesCoder.decode(bytes)

// -- OperationContent <-> String --

public fun OperationContent.forgeToString(withHexPrefix: Boolean = false, tezos: Tezos = Tezos.Default): String =
    forgeToString(withHexPrefix, tezos.operation().dependencyRegistry.operationContentBytesCoder)

public fun OperationContent.Companion.unforgeFromString(string: String, tezos: Tezos = Tezos.Default): OperationContent =
    unforgeFromString(string, tezos.operation().dependencyRegistry.operationContentBytesCoder)

@InternalTezosSdkApi
public fun OperationContent.forgeToString(withHexPrefix: Boolean = false, operationContentBytesCoder: ConsumingBytesCoder<OperationContent>): String =
    forgeToBytes(operationContentBytesCoder).toHexString().asString(withHexPrefix)

@InternalTezosSdkApi
public fun OperationContent.Companion.unforgeFromString(string: String, operationContentBytesCoder: ConsumingBytesCoder<OperationContent>): OperationContent =
    unforgeFromBytes(string.asHexString().toByteArray(), operationContentBytesCoder)