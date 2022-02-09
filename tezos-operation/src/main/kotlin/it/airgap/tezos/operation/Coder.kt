package it.airgap.tezos.operation

import it.airgap.tezos.core.TezosSdk
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import it.airgap.tezos.operation.internal.coder.OperationBytesCoder
import it.airgap.tezos.operation.internal.di.operation

// -- Operation <-> ByteArray --

public fun Operation.forgeToBytes(
    operationBytesCoder: OperationBytesCoder = TezosSdk.instance.dependencyRegistry.operation().operationBytesCoder
): ByteArray = operationBytesCoder.encode(this)

public fun Operation.Companion.unforgeFromBytes(
    bytes: ByteArray,
    operationBytesCoder: OperationBytesCoder = TezosSdk.instance.dependencyRegistry.operation().operationBytesCoder,
): Operation = operationBytesCoder.decode(bytes)

// -- Operation <-> String --

public fun Operation.forgeToString(
    withHexPrefix: Boolean = false,
    operationBytesCoder: OperationBytesCoder = TezosSdk.instance.dependencyRegistry.operation().operationBytesCoder,
): String = operationBytesCoder.encode(this).toHexString().asString(withHexPrefix)

public fun Operation.Companion.unforgeFromString(
    string: String,
    operationBytesCoder: OperationBytesCoder = TezosSdk.instance.dependencyRegistry.operation().operationBytesCoder,
): Operation = operationBytesCoder.decode(string.asHexString().toByteArray())