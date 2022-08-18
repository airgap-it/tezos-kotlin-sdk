package it.airgap.tezos.rpc.type.storage

import it.airgap.tezos.michelson.micheline.Micheline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonClassDiscriminator

// -- RpcStorageBigMapDiff --

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator(RpcStorageBigMapDiff.CLASS_DISCRIMINATOR)
public sealed class RpcStorageBigMapDiff {

    @Transient
    public open val updates: List<RpcStorageBigMapUpdate>? = null

    @Transient
    public open val source: String? = null

    @Transient
    public open val keyType: Micheline? = null

    @Transient
    public open val valueType: Micheline? = null

    @Serializable
    @SerialName(Update.KIND)
    public data class Update(override val updates: List<RpcStorageBigMapUpdate>) : RpcStorageBigMapDiff() {
        public companion object {
            internal const val KIND = "update"
        }
    }

    @Serializable
    @SerialName(Remove.KIND)
    public object Remove : RpcStorageBigMapDiff() {
        internal const val KIND = "remove"
    }

    @Serializable
    @SerialName(Copy.KIND)
    public data class Copy(
        override val source: String,
        override val updates: List<RpcStorageBigMapUpdate>,
    ) : RpcStorageBigMapDiff() {
        public companion object {
            internal const val KIND = "copy"
        }
    }

    @Serializable
    @SerialName(Alloc.KIND)
    public data class Alloc(
        override val updates: List<RpcStorageBigMapUpdate>,
        @SerialName("key_type") override val keyType: Micheline,
        @SerialName("value_type") override val valueType: Micheline,
    ) : RpcStorageBigMapDiff() {
        public companion object {
            internal const val KIND = "alloc"
        }
    }

    public companion object {
        internal const val CLASS_DISCRIMINATOR = "action"
    }
}

// -- SaplingState --

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator(RpcStorageSaplingStateDiff.CLASS_DISCRIMINATOR)
public sealed class RpcStorageSaplingStateDiff {
    @Transient
    public open val updates: List<RpcStorageSaplingStateUpdate>? = null

    @Transient
    public open val source: String? = null

    @Transient
    public open val memoSize: UShort? = null

    @Serializable
    @SerialName(Update.KIND)
    public data class Update(override val updates: List<RpcStorageSaplingStateUpdate>) : RpcStorageSaplingStateDiff() {
        public companion object {
            internal const val KIND = "update"
        }
    }

    @Serializable
    @SerialName(Remove.KIND)
    public object Remove : RpcStorageSaplingStateDiff() {
        internal const val KIND = "remove"
    }

    @Serializable
    @SerialName(Copy.KIND)
    public data class Copy(
        override val source: String,
        override val updates: List<RpcStorageSaplingStateUpdate>,
    ) : RpcStorageSaplingStateDiff() {
        public companion object {
            internal const val KIND = "copy"
        }
    }

    @Serializable
    @SerialName(Alloc.KIND)
    public data class Alloc(
        override val updates: List<RpcStorageSaplingStateUpdate>,
        @SerialName("memo_size") override val memoSize: UShort,
    ) : RpcStorageSaplingStateDiff() {
        public companion object {
            internal const val KIND = "alloc"
        }
    }

    public companion object {
        internal const val CLASS_DISCRIMINATOR = "action"
    }
}

// -- RpcLazyStorageDiff --

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator(RpcLazyStorageDiff.CLASS_DISCRIMINATOR)
public sealed class RpcLazyStorageDiff {

    public abstract val id: String

    @Serializable
    @SerialName(BigMap.KIND)
    public data class BigMap(
        override val id: String,
        public val diff: RpcStorageBigMapDiff,
    ) : RpcLazyStorageDiff() {
        public companion object {
            internal const val KIND = "big_map"
        }
    }

    @Serializable
    @SerialName(SaplingState.KIND)
    public data class SaplingState(
        override val id: String,
        public val diff: RpcStorageSaplingStateDiff,
    ) : RpcLazyStorageDiff() {
        public companion object {
            internal const val KIND = "sapling_state"
        }
    }

    public companion object {
        internal const val CLASS_DISCRIMINATOR = "kind"
    }
}
