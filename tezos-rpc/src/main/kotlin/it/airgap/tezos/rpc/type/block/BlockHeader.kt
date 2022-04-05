package it.airgap.tezos.rpc.type.block

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcBlockHeader --

@Serializable
public data class RpcBlockHeader(
    public val level: Int,
    public val proto: UByte,
    public val predecessor: @Contextual BlockHash,
    public val timestamp: @Contextual Timestamp,
    @SerialName("validation_pass") public val validationPass: UByte,
    @SerialName("operations_hash") public val operationsHash: @Contextual OperationListListHash,
    public val fitness: List<@Contextual HexString>,
    public val context: @Contextual ContextHash,
    @SerialName("payload_hash") public val payloadHash: @Contextual BlockPayloadHash,
    @SerialName("payload_round") public val payloadRound: Int,
    @SerialName("proof_of_work_nonce") public val proofOfWorkNonce: @Contextual HexString,
    @SerialName("seed_nonce_hash") public val seedNonceHash: @Contextual NonceHash? = null,
    @SerialName("liquidity_baking_escape_vote") public val liquidityBakingEscapeVote: Boolean,
    public val signature: @Contextual SignatureEncoded,
)