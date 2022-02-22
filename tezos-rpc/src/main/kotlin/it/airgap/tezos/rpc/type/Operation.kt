package it.airgap.tezos.rpc.type

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.OperationHash
import it.airgap.tezos.core.type.encoded.OperationListListHash
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

// -- RpcOperationHash --

internal typealias TransitionalRpcOperationHash = Unistring

// -- RpcOperationListListHash --

internal typealias TransitionalRpcOperationListListHash = Unistring

// -- RpcOperation --

@Serializable
public data class RpcOperation(
    public val branch: @Contextual BlockHash,
    public val data: @Contextual HexString,
)