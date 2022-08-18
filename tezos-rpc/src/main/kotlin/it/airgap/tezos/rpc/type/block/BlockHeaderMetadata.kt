package it.airgap.tezos.rpc.type.block

import it.airgap.tezos.core.type.encoded.NonceHash
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.core.type.encoded.PublicKeyHash
import it.airgap.tezos.rpc.type.chain.RpcTestChainStatus
import it.airgap.tezos.rpc.type.operation.RpcBalanceUpdate
import it.airgap.tezos.rpc.type.operation.RpcOperationListMetadata
import it.airgap.tezos.rpc.type.operation.RpcSuccessfulManagerOperationResult
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcBlockHeaderMetadata --

@Serializable
public data class RpcBlockHeaderMetadata(
    public val protocol: @Contextual ProtocolHash,
    @SerialName("next_protocol") public val nextProtocol: @Contextual ProtocolHash,
    @SerialName("test_chain_status") public val testChainStatus: RpcTestChainStatus,
    @SerialName("max_operations_ttl") public val maxOperationsTtl: Int,
    @SerialName("max_operation_data_length") public val maxOperationDataLength: Int,
    @SerialName("max_block_header_length") public val maxBlockHeaderLength: Int,
    @SerialName("max_operation_list_length") public val maxOperationListLength: List<RpcOperationListMetadata>,
    public val proposer: @Contextual PublicKeyHash,
    public val baker: @Contextual PublicKeyHash,
    @SerialName("level_info") public val levelInfo: RpcLevelInfo,
    @SerialName("voting_period_info") public val votingPeriodInfo: RpcVotingPeriodInfo,
    @SerialName("nonce_hash") public val nonceHash: @Contextual NonceHash?,
    @SerialName("consumed_gas") public val consumedGas: String,
    public val deactivated: List<@Contextual PublicKeyHash>,
    @SerialName("balance_updates") public val balanceUpdates: List<RpcBalanceUpdate>,
    @SerialName("liquidity_baking_toggle_ema") public val liquidityBakingToggleEma: Int,
    @SerialName("implicit_operations_results") public val implicitOperationsResults: List<@Contextual RpcSuccessfulManagerOperationResult>,
)
