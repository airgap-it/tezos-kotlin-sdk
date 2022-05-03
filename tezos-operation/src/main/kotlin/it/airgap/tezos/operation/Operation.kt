package it.airgap.tezos.operation

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.SignatureEncoded

public sealed interface Operation {
    public val branch: BlockHash
    public val contents: List<OperationContent>

    public data class Unsigned(override val branch: BlockHash, override val contents: List<OperationContent>) : Operation
    public data class Signed(
        override val branch: BlockHash,
        override val contents: List<OperationContent>,
        val signature: SignatureEncoded,
    ) : Operation {
        public companion object {
            public fun from(unsigned: Unsigned, signature: SignatureEncoded): Signed = Signed(unsigned.branch, unsigned.contents, signature)
        }
    }

    public companion object {}
}

public fun Operation(
    vararg contents: OperationContent,
    branch: BlockHash,
    signature: SignatureEncoded? = null,
): Operation = Operation(contents.toList(), branch, signature)

public fun Operation(
    contents: List<OperationContent>,
    branch: BlockHash,
    signature: SignatureEncoded? = null,
): Operation = if (signature != null) Operation.Signed(branch, contents, signature) else Operation.Unsigned(branch, contents)