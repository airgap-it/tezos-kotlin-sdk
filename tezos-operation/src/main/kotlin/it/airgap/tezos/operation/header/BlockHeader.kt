package it.airgap.tezos.operation.header

public interface ShellBlockHeader {
    public val level: Int
    public val proto: UByte
    public val predecessor: String
    public val timestamp: Long
    public val validationPass: UByte
    public val operationsHash: String
    public val fitness: List<Fitness>
    public val context: String
}

public interface ProtocolBlockHeader {
    public val payloadHash: String
    public val payloadRound: Int
    public val proofOfWorkNonce: String
    public val seedNonceHash: String?
    public val liquidityBakingEscapeVote: Boolean
    public val signature: String
}