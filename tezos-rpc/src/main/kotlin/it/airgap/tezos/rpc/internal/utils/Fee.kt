package it.airgap.tezos.rpc.internal.utils

import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent

// -- Operation --

internal val Operation.fee: Mutez
    get() = contents.fold(Mutez(0U)) { acc, content -> acc + content.fee }

// -- OperationContent --

internal val OperationContent.fee: Mutez
    get() = when (this) {
        is OperationContent.Manager -> fee
        else -> Mutez(0U)
    }

internal val OperationContent.hasFee: Boolean
    get() = fee != Mutez(0U)