package it.airgap.tezos.rpc.type

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcBlock --

@Serializable
public data class RpcBlock(
    public val protocol: @Contextual ProtocolHash,
    @SerialName("chain_id") public val chainId: @Contextual ChainId,
    public val hash: @Contextual BlockHash,
    public val header: RpcRawBlockHeader,
)

// -- RpcRawBlockHeader --

@Serializable
public data class RpcRawBlockHeader(
    public val hash: @Contextual BlockHash,
    public val level: Int,
    public val proto: UByte,
    public val predecessor: @Contextual BlockHash,
    public val timestamp: @Contextual Timestamp,
    @SerialName("validation_pass") public val validationPass: UByte,
    @SerialName("operations_hash") public val operationsHash: @Contextual OperationListListHash,
    public val fitness: List<@Contextual HexString>,
    public val context: @Contextual ContextHash,
    @SerialName("protocol_data") public val protocolData: @Contextual HexString,
)

// -- RpcInvalidBlock --

@Serializable
public data class RpcInvalidBlock(
    public val block: @Contextual BlockHash,
    public val level: Int,
    public val errors: List<RpcError>,
)
