package it.airgap.tezos.rpc.type.storage

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
public sealed class RpcStorageDiff {

    @Serializable
    public sealed class BigMap : RpcStorageDiff() {
        // TODO
    }

    @Serializable
    public sealed class SaplingState : RpcStorageDiff() {
        // TODO
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator(RpcLazyStorageDiff.CLASS_DISCRIMINATOR)
public sealed class RpcLazyStorageDiff {

    public abstract val id: String
    public abstract val diff: RpcStorageDiff

    @Serializable
    @SerialName(BigMap.KIND)
    public data class BigMap(
        override val id: String,
        override val diff: RpcStorageDiff.BigMap,
    ) : RpcLazyStorageDiff() {
        public companion object {
            internal const val KIND = "big_map"
        }
    }

    @Serializable
    @SerialName(SaplingState.KIND)
    public data class SaplingState(
        override val id: String,
        override val diff: RpcStorageDiff.SaplingState,
    ) : RpcLazyStorageDiff() {
        public companion object {
            internal const val KIND = "sapling_state"
        }
    }

    public companion object {
        internal const val CLASS_DISCRIMINATOR = "kind"
    }
}
