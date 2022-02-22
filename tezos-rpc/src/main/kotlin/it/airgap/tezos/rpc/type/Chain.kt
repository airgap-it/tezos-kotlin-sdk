package it.airgap.tezos.rpc.type

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.internal.serializer.GenericRpcActiveChainSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcChainId --

internal typealias TransitionalRpcChainId = Unistring

// -- RpcActiveChain --

@Serializable(with = GenericRpcActiveChainSerializer::class)
public sealed class GenericRpcActiveChain<out ChainId, out ProtocolHash, out Timestamp> {
    public open val chainId: ChainId? = null
    public open val testProtocol: ProtocolHash? = null
    public open val expirationDate: Timestamp? = null
    public open val stopping: ChainId? = null
}
internal typealias TransitionalRpcActiveChain = GenericRpcActiveChain<TransitionalRpcChainId, TransitionalRpcProtocolHash, TransitionalRpcTimestamp>
public typealias RpcActiveChain = GenericRpcActiveChain<@Contextual ChainId, @Contextual ProtocolHash, @Contextual Timestamp>

@Serializable
public data class GenericRpcMainChain<ChainId, ProtocolHash, Timestamp>(@SerialName("chain_id") override val chainId: ChainId) : GenericRpcActiveChain<ChainId, ProtocolHash, Timestamp>()
internal typealias TransitionalRpcMainChain = GenericRpcMainChain<TransitionalRpcChainId, TransitionalRpcProtocolHash, TransitionalRpcTimestamp>
public typealias RpcMainChain = GenericRpcMainChain<@Contextual ChainId, @Contextual ProtocolHash, @Contextual Timestamp>

@Serializable
public data class GenericRpcTestChain<ChainId, ProtocolHash, Timestamp>(
    @SerialName("chain_id") override val chainId: ChainId,
    @SerialName("test_protocol") override val testProtocol: ProtocolHash,
    @SerialName("expiration_date") override val expirationDate: Timestamp,
) : GenericRpcActiveChain<ChainId, ProtocolHash, Timestamp>()
internal typealias TransitionalRpcTestChain = GenericRpcTestChain<TransitionalRpcChainId, TransitionalRpcProtocolHash, TransitionalRpcTimestamp>
public typealias RpcTestChain = GenericRpcTestChain<@Contextual ChainId, @Contextual ProtocolHash, @Contextual Timestamp>

@Serializable
public data class GenericRpcStoppingChain<ChainId, ProtocolHash, Timestamp>(override val stopping: ChainId) : GenericRpcActiveChain<ChainId, ProtocolHash, Timestamp>()
internal typealias TransitionalRpcStoppingChain = GenericRpcStoppingChain<TransitionalRpcChainId, TransitionalRpcProtocolHash, TransitionalRpcTimestamp>
public typealias RpcStoppingChain = GenericRpcStoppingChain<@Contextual ChainId, @Contextual ProtocolHash, @Contextual Timestamp>

// -- RpcChainStatus --

@Serializable
public enum class RpcChainStatus {
    @SerialName("stuck") Stuck,
    @SerialName("synced") Synced,
    @SerialName("unsynced") Unsynced,
}