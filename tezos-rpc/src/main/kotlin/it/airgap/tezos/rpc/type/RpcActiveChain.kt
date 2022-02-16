package it.airgap.tezos.rpc.type

import it.airgap.tezos.rpc.internal.serializer.RpcActiveChainSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = RpcActiveChainSerializer::class)
public sealed class RpcActiveChain<ChainId, ProtocolHash, Timestamp> {
    public abstract val chainId: ChainId?
    public abstract val testProtocol: ProtocolHash?
    public abstract val expirationDate: Timestamp?
    public abstract val stopping: ChainId?

    @Serializable
    public data class Main<ChainId, ProtocolHash, Timestamp>(override val chainId: ChainId) : RpcActiveChain<ChainId, ProtocolHash, Timestamp>() {
        override val testProtocol: ProtocolHash? = null
        override val expirationDate: Timestamp? = null
        override val stopping: ChainId? = null
    }

    @Serializable
    public data class Test<ChainId, ProtocolHash, Timestamp>(
        override val chainId: ChainId,
        @SerialName("test_protocol") override val testProtocol: ProtocolHash,
        @SerialName("expiration_date") override val expirationDate: Timestamp,
    ) : RpcActiveChain<ChainId, ProtocolHash, Timestamp>() {
        override val stopping: ChainId? = null
    }

    @Serializable
    public data class Stopping<ChainId, ProtocolHash, Timestamp>(override val stopping: ChainId) : RpcActiveChain<ChainId, ProtocolHash, Timestamp>() {
        override val chainId: ChainId? = null
        override val testProtocol: ProtocolHash? = null
        override val expirationDate: Timestamp? = null
    }
}