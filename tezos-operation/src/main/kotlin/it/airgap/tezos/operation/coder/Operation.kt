package it.airgap.tezos.operation.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.internal.operation

// -- Operation <-> ByteArray --

public fun Operation.forgeToBytes(tezos: Tezos = Tezos.Default): ByteArray =
    forgeToBytes(tezos.operation().dependencyRegistry.operationBytesCoder)

public fun Operation.Companion.unforgeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Operation =
    unforgeFromBytes(bytes, tezos.operation().dependencyRegistry.operationBytesCoder)

@InternalTezosSdkApi
public fun Operation.forgeToBytes(operationBytesCoder: ConsumingBytesCoder<Operation>): ByteArray =
    operationBytesCoder.encode(this)

@InternalTezosSdkApi
public fun Operation.Companion.unforgeFromBytes(bytes: ByteArray, operationBytesCoder: ConsumingBytesCoder<Operation>): Operation =
    operationBytesCoder.decode(bytes)

// -- Operation <-> String --

public fun Operation.forgeToString(withHexPrefix: Boolean = false, tezos: Tezos = Tezos.Default): String =
    forgeToString(withHexPrefix, tezos.operation().dependencyRegistry.operationBytesCoder)

public fun Operation.Companion.unforgeFromString(string: String, tezos: Tezos = Tezos.Default): Operation =
    unforgeFromString(string, tezos.operation().dependencyRegistry.operationBytesCoder)

@InternalTezosSdkApi
public fun Operation.forgeToString(withHexPrefix: Boolean = false, operationBytesCoder: ConsumingBytesCoder<Operation>): String =
    forgeToBytes(operationBytesCoder).toHexString().asString(withHexPrefix)

@InternalTezosSdkApi
public fun Operation.Companion.unforgeFromString(string: String, operationBytesCoder: ConsumingBytesCoder<Operation>): Operation =
    unforgeFromBytes(string.asHexString().toByteArray(), operationBytesCoder)