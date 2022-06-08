package it.airgap.tezos.operation

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.Signature

/**
 * Tezos operation.
 *
 * See [P2P message format](https://tezos.gitlab.io/shell/p2p_api.html#operation-shell) for more details.
 */
public sealed interface Operation {
    public val branch: BlockHash
    public val contents: List<OperationContent>

    /**
     * An unsigned Tezos operation.
     */
    public data class Unsigned(override val branch: BlockHash, override val contents: List<OperationContent>) : Operation

    /**
     * A signed Tezos operation.
     */
    public data class Signed(
        override val branch: BlockHash,
        override val contents: List<OperationContent>,
        val signature: Signature,
    ) : Operation {
        public companion object {

            /**
             * Creates a [signed operation][Operation.Signed] from an [unsigned] one and a [signature].
             */
            public fun from(unsigned: Unsigned, signature: Signature): Signed = Signed(unsigned.branch, unsigned.contents, signature)
        }
    }

    public companion object {}
}

/**
 * Creates an [operation][Operation] from [contents], [branch] and an optional [signature].
 * The final operation will be [signed][Operation.Signed] or [unsigned][Operation.Unsigned] depending on
 * whether the [signature] was provided or not.
 *
 * @see Operation
 */
public fun Operation(
    vararg contents: OperationContent,
    branch: BlockHash,
    signature: Signature? = null,
): Operation = Operation(contents.toList(), branch, signature)

/**
 * Creates an [operation][Operation] from [contents], [branch] and an optional [signature].
 * The final operation will be [signed][Operation.Signed] or [unsigned][Operation.Unsigned] depending on
 * whether the [signature] was provided or not.
 *
 * @see Operation
 */
public fun Operation(
    contents: List<OperationContent>,
    branch: BlockHash,
    signature: Signature? = null,
): Operation = if (signature != null) Operation.Signed(branch, contents, signature) else Operation.Unsigned(branch, contents)