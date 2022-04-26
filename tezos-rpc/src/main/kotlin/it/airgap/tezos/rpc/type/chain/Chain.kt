package it.airgap.tezos.rpc.type.chain

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.internal.serializer.RpcActiveChainSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcActiveChain --

@Serializable(with = RpcActiveChainSerializer::class)
public sealed class RpcActiveChain {
    public open val chainId: ChainId? = null
    public open val testProtocol: ProtocolHash? = null
    public open val expirationDate: Timestamp? = null
    public open val stopping: ChainId? = null

    @Serializable
    public data class Main(@SerialName("chain_id") override val chainId: @Contextual ChainId) : RpcActiveChain()

    @Serializable
    public data class Test(
        @SerialName("chain_id") override val chainId: @Contextual ChainId,
        @SerialName("test_protocol") override val testProtocol: @Contextual ProtocolHash,
        @SerialName("expiration_date") override val expirationDate: @Contextual Timestamp,
    ) : RpcActiveChain()

    @Serializable
    public data class Stopping(override val stopping: @Contextual ChainId) : RpcActiveChain()

    public companion object {}
}
