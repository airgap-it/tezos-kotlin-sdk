package it.airgap.tezos.rpc.active.data

import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.rpc.type.contract.RpcScript
import it.airgap.tezos.rpc.type.contract.RpcUnreachableEntrypoint
import it.airgap.tezos.rpc.type.sapling.RpcSaplingStateDiff
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

// ==== ../<block_id>/context/contracts ====

// -- /<contract_id> --

@Serializable
public data class GetContractDetailsResponse(
    public val balance: String,
    public val delegate: @Contextual ImplicitAddress? = null,
    public val script: RpcScript? = null,
    public val counter: String? = null,
)

// -- /<contract_id>/balance --

@Serializable
@JvmInline
public value class GetContractBalanceResponse(public val balance: String)

// -- /<contract_id>/counter --

@Serializable
@JvmInline
public value class GetContractCounterResponse(public val counter: String)

// -- /<contract_id>/delegate --

@Serializable
@JvmInline
public value class GetContractDelegateResponse(public val delegate: @Contextual ImplicitAddress)

// -- /<contract_id>/entrypoints --

@Serializable
public data class GetContractEntrypointsResponse(
    public val unreachable: List<RpcUnreachableEntrypoint>,
    public val entrypoints: Map<String, MichelineNode>,
)

// -- /<contract_id>/entrypoints/<string> --

@Serializable
@JvmInline
public value class GetContractEntrypointTypeResponse(public val entrypoint: MichelineNode)

// -- /<contract_id>/manager_key --

@Serializable
@JvmInline
public value class GetContractManagerResponse(public val manager: String)

// -- /<contract_id>/script --

@Serializable
@JvmInline
public value class GetContractScriptResponse(public val script: RpcScript)

// -- /<contract_id>/single_sapling_get_diff --

@Serializable
@JvmInline
public value class GetContractSaplingStateDiffResponse(public val stateDiff: RpcSaplingStateDiff)

// -- /<contract_id>/storage --

@Serializable
@JvmInline
public value class GetContractStorageResponse(public val storage: MichelineNode)