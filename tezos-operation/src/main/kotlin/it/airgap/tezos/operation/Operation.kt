package it.airgap.tezos.operation

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.SignatureEncoded

public data class Operation(
    public val branch: BlockHash,
    public val contents: List<OperationContent>,
    public val signature: SignatureEncoded<*>? = null,
) {
    public companion object {}
}

@Suppress("FunctionName")
public fun Operation(
    vararg contents: OperationContent,
    branch: BlockHash,
    signature: SignatureEncoded<*>? = null,
): Operation = Operation(branch, contents.toList(), signature)