package it.airgap.tezos.operation

import it.airgap.tezos.core.TezosSdk
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import it.airgap.tezos.operation.internal.coder.OperationBytesCoder
import it.airgap.tezos.operation.internal.di.scoped

// -- Operation <-> ByteArray --

public fun Operation.forgeToBytes(): ByteArray = forgeToBytes(TezosSdk.instance.dependencyRegistry.scoped().operationBytesCoder)
internal fun Operation.forgeToBytes(operationBytesCoder: OperationBytesCoder): ByteArray = operationBytesCoder.encode(this)

public fun Operation.Companion.unforgeFromBytes(bytes: ByteArray): Operation = unforgeFromBytes(bytes, TezosSdk.instance.dependencyRegistry.scoped().operationBytesCoder)
internal fun Operation.Companion.unforgeFromBytes(bytes: ByteArray, operationBytesCoder: OperationBytesCoder): Operation = operationBytesCoder.decode(bytes)

// -- Operation <-> String --

public fun Operation.forgeToString(withHexPrefix: Boolean = false): String = forgeToString(TezosSdk.instance.dependencyRegistry.scoped().operationBytesCoder, withHexPrefix)
internal fun Operation.forgeToString(operationBytesCoder: OperationBytesCoder, withHexPrefix: Boolean = false): String = operationBytesCoder.encode(this).toHexString().asString(withHexPrefix)

public fun Operation.Companion.unforgeFromString(string: String): Operation = unforgeFromString(string, TezosSdk.instance.dependencyRegistry.scoped().operationBytesCoder)
internal fun Operation.Companion.unforgeFromString(string: String, operationBytesCoder: OperationBytesCoder): Operation = operationBytesCoder.decode(string.asHexString().toByteArray())