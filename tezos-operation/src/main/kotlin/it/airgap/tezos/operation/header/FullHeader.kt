package it.airgap.tezos.operation.header

public data class FullHeader(
    override val level: Int,
    override val proto: UByte,
    override val predecessor: String,
    override val timestamp: Long,
    override val validationPass: UByte,
    override val operationsHash: String,
    override val fitness: List<Fitness>,
    override val context: String,
    override val payloadHash: String,
    override val payloadRound: Int,
    override val proofOfWorkNonce: String,
    override val seedNonceHash: String? = null,
    override val liquidityBakingEscapeVote: Boolean,
    override val signature: String,
) : ShellBlockHeader, ProtocolBlockHeader
