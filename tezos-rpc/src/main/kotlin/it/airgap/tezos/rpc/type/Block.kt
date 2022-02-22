package it.airgap.tezos.rpc.type

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ContextHash
import it.airgap.tezos.core.type.encoded.OperationListListHash
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcBlockHash --

internal typealias TransitionalRpcBlockHash = Unistring

// -- RpcBlockHeader --

@Serializable
public data class GenericRpcBlockHeader<BlockHash, Timestamp, OperationListListHash, ContextHash>(
    public val hash: BlockHash,
    public val level: Int,
    public val proto: UByte,
    public val predecessor: BlockHash,
    public val timestamp: Timestamp,
    @SerialName("validation_pass") public val validationPass: UByte,
    @SerialName("operations_hash") public val operationsHash: OperationListListHash,
    public val fitness: List<@Contextual HexString>,
    public val context: ContextHash,
    @SerialName("protocol_data") public val protocolData: @Contextual HexString,
)
internal typealias TransitionalRpcBlockHeader = GenericRpcBlockHeader<TransitionalRpcBlockHash, TransitionalRpcTimestamp, TransitionalRpcOperationListListHash, TransitionalRpcContextHash>
public typealias RpcBlockHeader = GenericRpcBlockHeader<@Contextual BlockHash, @Contextual Timestamp, @Contextual OperationListListHash, @Contextual ContextHash>

// -- RpcInvalidBlock --

@Serializable
public data class GenericRpcInvalidBlock<BlockHash>(
    public val block: BlockHash,
    public val level: Int,
    public val errors: List<RpcError>,
)
internal typealias TransitionalRpcInvalidBlock = GenericRpcInvalidBlock<TransitionalRpcBlockHash>
public typealias RpcInvalidBlock = GenericRpcInvalidBlock<@Contextual BlockHash>