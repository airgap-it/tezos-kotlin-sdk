package it.airgap.tezos.rpc.type

import it.airgap.tezos.core.type.HexString
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RpcBlockHeader<BlockHash, Timestamp, OperationListListHash, ContextHash>(
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
