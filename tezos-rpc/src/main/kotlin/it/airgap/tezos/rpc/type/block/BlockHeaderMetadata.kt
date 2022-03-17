package it.airgap.tezos.rpc.type.block

import it.airgap.tezos.core.type.encoded.Address
import it.airgap.tezos.core.type.encoded.NonceHash
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.type.chain.RpcTestChainStatus
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RpcBlockHeaderMetadata(
    public val protocol: @Contextual ProtocolHash,
    @SerialName("next_protocol") public val nextProtocol: @Contextual ProtocolHash,
    @SerialName("test_chain_status") public val testChainStatus: RpcTestChainStatus,
    @SerialName("max_operations_ttl") public val maxOperationsTtl: Int,
    @SerialName("max_operation_data_length") public val maxOperationDataLength: Int,
    @SerialName("max_block_header_length") public val maxBlockHeaderLength: Int,
    @SerialName("max_operation_list_length") public val maxOperationListLength: RpcOperationListMetadata,
    public val proposer: @Contextual Address,
    public val baker: @Contextual Address,
    @SerialName("level_info") public val levelInfo: RpcLevelInfo,
    @SerialName("voting_period_info") public val votingPeriodInfo: RpcVotingPeriodInfo,
    @SerialName("nonce_hash") public val nonceHash: @Contextual NonceHash? = null,
    @SerialName("consumed_gas") public val consumedGas: String,
    public val deactivated: List<@Contextual Address>,
    @SerialName("balance_updates") public val balanceUpdates: @Contextual Any, // TODO: define type
    @SerialName("liquidity_baking_escape_ema") public val liquidityBakingEscapeEma: Int,
    @SerialName("implicit_operations_results") public val implicitOperationsResults: @Contextual Any, // TODO: define type
)

@Serializable
public data class RpcOperationListMetadata(
    @SerialName("max_size") public val maxSize: Int,
    @SerialName("max_op") public val maxOperations: Int,
)
