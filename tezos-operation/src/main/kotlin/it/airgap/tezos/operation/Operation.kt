package it.airgap.tezos.operation

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.Signature

public sealed interface Operation {
    public val branch: BlockHash
    public val contents: List<OperationContent>

    public data class Unsigned(override val branch: BlockHash, override val contents: List<OperationContent>) : Operation
    public data class Signed(
        override val branch: BlockHash,
        override val contents: List<OperationContent>,
        val signature: Signature,
    ) : Operation {
        public companion object {
            public fun from(unsigned: Unsigned, signature: Signature): Signed = Signed(unsigned.branch, unsigned.contents, signature)
        }
    }

    public companion object {}
}

public fun Operation(
    vararg contents: OperationContent,
    branch: BlockHash,
    signature: Signature? = null,
): Operation = Operation(contents.toList(), branch, signature)

public fun Operation(
    contents: List<OperationContent>,
    branch: BlockHash,
    signature: Signature? = null,
): Operation = if (signature != null) Operation.Signed(branch, contents, signature) else Operation.Unsigned(branch, contents)