package it.airgap.tezos.operation.header

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.*

public data class FullHeader(
    override val level: Int,
    override val proto: UByte,
    override val predecessor: BlockHash,
    override val timestamp: Long,
    override val validationPass: UByte,
    override val operationsHash: OperationHash,
    override val fitness: List<Fitness>,
    override val context: ContextHash,
    override val payloadHash: BlockPayloadHash,
    override val payloadRound: Int,
    override val proofOfWorkNonce: HexString,
    override val seedNonceHash: NonceHash?,
    override val liquidityBakingEscapeVote: Boolean,
    override val signature: SignatureEncoded<*>,

) : ShellBlockHeader, ProtocolBlockHeader
