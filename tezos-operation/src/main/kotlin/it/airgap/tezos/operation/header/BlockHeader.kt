package it.airgap.tezos.operation.header

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.*

public interface ShellBlockHeader {
    public val level: Int
    public val proto: UByte
    public val predecessor: BlockHash
    public val timestamp: Long
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
    public val signature: SignatureEncoded<*>
}