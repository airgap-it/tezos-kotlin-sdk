package it.airgap.tezos.operation

import it.airgap.tezos.core.TezosSdk
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import it.airgap.tezos.operation.internal.coder.OperationBytesCoder
import it.airgap.tezos.operation.internal.coder.OperationContentBytesCoder
import it.airgap.tezos.operation.internal.di.operation

// -- Operation <-> ByteArray --

public fun Operation.forgeToBytes(
    operationBytesCoder: OperationBytesCoder = TezosSdk.instance.dependencyRegistry.operation().operationBytesCoder
): ByteArray = operationBytesCoder.encode(this)

public fun Operation.Companion.unforgeFromBytes(
    bytes: ByteArray,
    operationBytesCoder: OperationBytesCoder = TezosSdk.instance.dependencyRegistry.operation().operationBytesCoder,
): Operation = operationBytesCoder.decode(bytes)

// -- OperationContent <-> ByteArray --

public fun OperationContent.forgeToBytes(
    operationContentBytesCoder: OperationContentBytesCoder = TezosSdk.instance.dependencyRegistry.operation().operationContentBytesCoder
): ByteArray = operationContentBytesCoder.encode(this)

public fun OperationContent.Companion.unforgeFromBytes(
    bytes: ByteArray,
    operationContentBytesCoder: OperationContentBytesCoder = TezosSdk.instance.dependencyRegistry.operation().operationContentBytesCoder,
): OperationContent = operationContentBytesCoder.decode(bytes)

// -- Operation <-> String --

public fun Operation.forgeToString(
    withHexPrefix: Boolean = false,
    operationBytesCoder: OperationBytesCoder = TezosSdk.instance.dependencyRegistry.operation().operationBytesCoder,
): String = forgeToBytes(operationBytesCoder).toHexString().asString(withHexPrefix)

public fun Operation.Companion.unforgeFromString(
    string: String,
    operationBytesCoder: OperationBytesCoder = TezosSdk.instance.dependencyRegistry.operation().operationBytesCoder,
): Operation = unforgeFromBytes(string.asHexString().toByteArray(), operationBytesCoder)

// -- OperationContent <-> String --

public fun OperationContent.forgeToString(
    withHexPrefix: Boolean = false,
    operationContentBytesCoder: OperationContentBytesCoder = TezosSdk.instance.dependencyRegistry.operation().operationContentBytesCoder
): String = forgeToBytes(operationContentBytesCoder).toHexString().asString(withHexPrefix)

public fun OperationContent.Companion.unforgeFromString(
    string: String,
    operationContentBytesCoder: OperationContentBytesCoder = TezosSdk.instance.dependencyRegistry.operation().operationContentBytesCoder,
): OperationContent = unforgeFromBytes(string.asHexString().toByteArray(), operationContentBytesCoder)