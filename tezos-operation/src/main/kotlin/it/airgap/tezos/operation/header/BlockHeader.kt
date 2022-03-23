package it.airgap.tezos.operation.header

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*

public interface ShellBlockHeader {
    public val level: Int
    public val proto: UByte
    public val predecessor: BlockHash
    public val timestamp: Timestamp
    public val validationPass: UByte
    public val operationsHash: OperationHash
    public val fitness: List<Fitness>
    public val context: ContextHash
}

public interface ProtocolBlockHeader {
    public val payloadHash: BlockPayloadHash
    public val payloadRound: Int
    public val proofOfWorkNonce: HexString
    public val seedNonceHash: NonceHash?
    public val liquidityBakingEscapeVote: Boolean
    public val signature: SignatureEncoded
}

public data class FullBlockHeader(
    override val level: Int,
    override val proto: UByte,
    override val predecessor: BlockHash,
    override val timestamp: Timestamp,
    override val validationPass: UByte,
    override val operationsHash: OperationHash,
    override val fitness: List<Fitness>,
    override val context: ContextHash,
    override val payloadHash: BlockPayloadHash,
    override val payloadRound: Int,
    override val proofOfWorkNonce: HexString,
    override val seedNonceHash: NonceHash?,
    override val liquidityBakingEscapeVote: Boolean,
    override val signature: SignatureEncoded,
) : ShellBlockHeader, ProtocolBlockHeader