package it.airgap.tezos.operation

public data class Operation(
    public val branch: String,
    public val contents: List<OperationContent>,
    public val signature: String? = null,
) {
    public companion object {}
}

@Suppress("FunctionName")
public fun Operation(
    vararg contents: OperationContent,
    branch: String,
    signature: String? = null,
): Operation = Operation(branch, contents.toList(), signature)