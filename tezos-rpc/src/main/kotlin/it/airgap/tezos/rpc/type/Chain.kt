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
public sealed class GenericRpcActiveChain<ChainId, ProtocolHash, Timestamp> {
    public abstract val chainId: ChainId?
    public abstract val testProtocol: ProtocolHash?
    public abstract val expirationDate: Timestamp?
    public abstract val stopping: ChainId?
}

@Serializable
public data class GenericRpcMainChain<ChainId, ProtocolHash, Timestamp>(override val chainId: ChainId) : GenericRpcActiveChain<ChainId, ProtocolHash, Timestamp>() {
    override val testProtocol: ProtocolHash? = null
    override val expirationDate: Timestamp? = null
    override val stopping: ChainId? = null
}

@Serializable
public data class GenericRpcTestChain<ChainId, ProtocolHash, Timestamp>(
    override val chainId: ChainId,
    @SerialName("test_protocol") override val testProtocol: ProtocolHash,
    @SerialName("expiration_date") override val expirationDate: Timestamp,
) : GenericRpcActiveChain<ChainId, ProtocolHash, Timestamp>() {
    override val stopping: ChainId? = null
}

@Serializable
public data class GenericRpcStoppingChain<ChainId, ProtocolHash, Timestamp>(override val stopping: ChainId) : GenericRpcActiveChain<ChainId, ProtocolHash, Timestamp>() {
    override val chainId: ChainId? = null
    override val testProtocol: ProtocolHash? = null
    override val expirationDate: Timestamp? = null
}

internal typealias TransitionalRpcActiveChain = GenericRpcActiveChain<TransitionalRpcChainId, TransitionalRpcProtocolHash, TransitionalTimestamp>
internal typealias TransitionalRpcMainChain = GenericRpcMainChain<TransitionalRpcChainId, TransitionalRpcProtocolHash, TransitionalTimestamp>
internal typealias TransitionalRpcTestChain = GenericRpcTestChain<TransitionalRpcChainId, TransitionalRpcProtocolHash, TransitionalTimestamp>
internal typealias TransitionalRpcStoppingChain = GenericRpcStoppingChain<TransitionalRpcChainId, TransitionalRpcProtocolHash, TransitionalTimestamp>

public typealias RpcActiveChain = GenericRpcActiveChain<@Contextual ChainId, @Contextual ProtocolHash, @Contextual Timestamp>
public typealias RpcMainChain = GenericRpcMainChain<@Contextual ChainId, @Contextual ProtocolHash, @Contextual Timestamp>
public typealias RpcTestChain = GenericRpcTestChain<@Contextual ChainId, @Contextual ProtocolHash, @Contextual Timestamp>
public typealias RpcStoppingChain = GenericRpcStoppingChain<@Contextual ChainId, @Contextual ProtocolHash, @Contextual Timestamp>
