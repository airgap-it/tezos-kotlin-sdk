package it.airgap.tezos.rpc.active

import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.core.type.encoded.PublicKeyHashEncoded
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.rpc.active.data.*
import it.airgap.tezos.rpc.http.HttpHeader

// https://tezos.gitlab.io/active/rpc.html
public interface ActiveRpc {

    // -- ../<block_id> --

    public suspend fun getBlock(chainId: String, blockId: String, headers: List<HttpHeader> = emptyList()): GetBlockResponse

    // -- ../<block_id>/context/big_maps --

    public suspend fun getBigMapValues(
        chainId: String,
        blockId: String,
        bigMapId: String,
        offset: UInt? = null,
        length: UInt? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapValuesResponse

    public suspend fun getBigMapValue(
        chainId: String,
        blockId: String,
        bigMapId: String,
        key: ScriptExprHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetBigMapValueResponse

    // -- ../<block_id>/context/constants --

    public suspend fun getConstants(chainId: String, blockId: String, headers: List<HttpHeader> = emptyList()): GetConstantsResponse

    // -- ../<block_id>/context/contracts --

    public suspend fun getContractDetails(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractDetailsResponse

    public suspend fun getContractBalance(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractBalanceResponse

    public suspend fun getContractCounter(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractCounterResponse

    public suspend fun getContractDelegate(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractDelegateResponse

    public suspend fun getContractEntrypoints(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractEntrypointsResponse

    public suspend fun getContractEntrypointType(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        entrypoint: String,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractEntrypointTypeResponse

    public suspend fun getContractManager(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractManagerResponse

    public suspend fun getContractScript(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractScriptResponse

    public suspend fun getContractSaplingStateDiff(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        commitmentOffset: ULong? = null,
        nullifierOffset: ULong? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractSaplingStateDiffResponse

    public suspend fun getContractStorage(
        chainId: String,
        blockId: String,
        contractId: ContractHash,
        headers: List<HttpHeader> = emptyList(),
    ): GetContractStorageResponse

    // -- ../<block_id>/context/delegates --

    public suspend fun getDelegateDetails(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDetailsResponse

    public suspend fun getDelegateCurrentFrozenDeposits(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateCurrentFrozenDepositsResponse

    public suspend fun isDelegateDeactivated(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDeactivatedStatusResponse

    public suspend fun getDelegateDelegatedBalance(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDelegatedBalanceResponse

    public suspend fun getDelegateDelegatedContracts(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateDelegatedContractsResponse

    public suspend fun getDelegateFrozenDeposits(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFrozenDepositsResponse

    public suspend fun getDelegateFrozenDepositsLimit(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFrozenDepositsLimitResponse

    public suspend fun getDelegateFullBalance(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateFullBalanceResponse

    public suspend fun getDelegateGracePeriod(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateGracePeriodResponse

    public suspend fun getDelegateParticipation(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateParticipationResponse

    public suspend fun getDelegateStakingBalance(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateStakingBalanceResponse

    public suspend fun getDelegateVotingPower(
        chainId: String,
        blockId: String,
        publicKeyHash: PublicKeyHashEncoded,
        headers: List<HttpHeader> = emptyList(),
    ): GetDelegateVotingPowerResponse

    // -- ../<block_id>/context/sapling --

    public suspend fun getSaplingStateDiff(
        chainId: String,
        blockId: String,
        stateId: String,
        commitmentOffset: ULong? = null,
        nullifierOffset: ULong? = null,
        headers: List<HttpHeader> = emptyList(),
    ): GetSaplingStateDiffResponse

    // -- ../<block_id>/header --

    public suspend fun getBlockHeader(
        chainId: String,
        blockId: String,
        headers: List<HttpHeader> = emptyList(),
    ): GetBlockHeaderResponse
}