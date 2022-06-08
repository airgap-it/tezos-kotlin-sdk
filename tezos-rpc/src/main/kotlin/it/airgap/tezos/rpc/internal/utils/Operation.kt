package it.airgap.tezos.rpc.internal.utils

import it.airgap.tezos.core.type.encoded.Signature
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

internal val Operation.signatureOrPlaceholder: Signature
    get() = when (this) {
        is Operation.Unsigned -> Signature.placeholder
        is Operation.Signed -> signature
    }

internal val RpcRunnableOperation.isSigned: Boolean
    get() = !signature.isPlaceholder
