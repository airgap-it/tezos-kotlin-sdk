package it.airgap.tezos.rpc.internal.utils

import it.airgap.tezos.core.type.encoded.SignatureEncoded
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.rpc.type.operation.RpcOperationContent
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

internal fun Operation.withFeeFrom(contents: List<RpcOperationContent>): Operation {
    TODO()
}

internal val Operation.signatureOrPlaceholder: SignatureEncoded
    get() = when (this) {
        is Operation.Unsigned -> SignatureEncoded.placeholder
        is Operation.Signed -> signature
    }

internal val RpcRunnableOperation.isSigned: Boolean
    get() = !signature.isPlaceholder