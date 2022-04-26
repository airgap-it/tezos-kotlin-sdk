package it.airgap.tezos.rpc.type.chain

import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.ProtocolHash
import kotlinx.serialization.*
import kotlinx.serialization.json.JsonClassDiscriminator

// -- RpcChainStatus --

@Serializable
public enum class RpcChainStatus {
    @SerialName("stuck") Stuck,
    @SerialName("synced") Synced,
    @SerialName("unsynced") Unsynced,
}

// -- RpcTestChainStatus --

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator(RpcTestChainStatus.CLASS_DISCRIMINATOR)
public sealed class RpcTestChainStatus {

    @Transient
    public open val chainId: ChainId? = null

    @Transient
    public open val genesis: ProtocolHash? = null

    @Transient
    public open val protocol: ProtocolHash? = null

    @Transient
    public open val expiration: Timestamp? = null

    @Serializable
    @SerialName(NotRunning.KIND)
    public object NotRunning : RpcTestChainStatus() {
        internal const val KIND = "not_running"
    }

    @Serializable
    @SerialName(Forking.KIND)
    public data class Forking(
        override val protocol: @Contextual ProtocolHash,
        override val expiration: @Contextual Timestamp,
    ) : RpcTestChainStatus() {

        public companion object {
            internal const val KIND = "forking"
        }
    }

    @Serializable
    @SerialName(Running.KIND)
    public data class Running(
        @SerialName("chain_id") override val chainId: @Contextual ChainId,
        override val genesis: @Contextual ProtocolHash,
        override val protocol: @Contextual ProtocolHash,
        override val expiration: @Contextual Timestamp,
    ) : RpcTestChainStatus() {

        public companion object {
            internal const val KIND = "running"
        }
    }

    public companion object {
        internal const val CLASS_DISCRIMINATOR = "status"
    }
}
