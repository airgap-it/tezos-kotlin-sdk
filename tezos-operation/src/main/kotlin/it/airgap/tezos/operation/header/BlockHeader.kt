package it.airgap.tezos.operation.header

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*

/**
 * Shell Tezos block header.
 *
 * See [P2P message format](https://tezos.gitlab.io/shell/p2p_api.html#block-header-shell) for more details.
 */
public interface ShellBlockHeader {
    public val level: Int
    public val proto: UByte
    public val predecessor: BlockHash
    public val timestamp: Timestamp
    public val validationPass: UByte
    public val operationsHash: OperationListListHash
    public val fitness: List<HexString>
    public val context: ContextHash
}

/**
 * Protocol specific Tezos block header.
 *
 * See [P2P message format](https://tezos.gitlab.io/shell/p2p_api.html#block-header-alpha-specific) for more details.
 */
public interface ProtocolBlockHeader {
    public val payloadHash: BlockPayloadHash
    public val payloadRound: Int
    public val proofOfWorkNonce: HexString
    public val seedNonceHash: NonceHash?
    public val liquidityBakingEscapeVote: Boolean
    public val signature: Signature
}

/**
 * Full Tezos block header.
 *
 * See [P2P message format](https://tezos.gitlab.io/shell/p2p_api.html#alpha-block-header-alpha-full-header) for more details.
 */
public data class BlockHeader(
    override val level: Int,
    override val proto: UByte,
    override val predecessor: BlockHash,
    override val timestamp: Timestamp,
    override val validationPass: UByte,
    override val operationsHash: OperationListListHash,
    override val fitness: List<HexString>,
    override val context: ContextHash,
    override val payloadHash: BlockPayloadHash,
    override val payloadRound: Int,
    override val proofOfWorkNonce: HexString,
    override val seedNonceHash: NonceHash? = null,
    override val liquidityBakingEscapeVote: Boolean,
    override val signature: Signature,
) : ShellBlockHeader, ProtocolBlockHeader