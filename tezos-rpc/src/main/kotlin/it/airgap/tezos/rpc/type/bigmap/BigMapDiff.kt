package it.airgap.tezos.rpc.type.bigmap

import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.michelson.micheline.MichelineNode
import kotlinx.serialization.*
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator(RpcBigMapDiff.CLASS_DISCRIMINATOR)
public sealed class RpcBigMapDiff {

    @Transient
    public open val bigMap: String? = null

    @Transient
    public open val keyHash: ScriptExprHash? = null

    @Transient
    public open val key: MichelineNode? = null

    @Transient
    public open val keyType: MichelineNode? = null

    @Transient
    public open val value: MichelineNode? = null

    @Transient
    public open val valueType: MichelineNode? = null

    @Transient
    public open val sourceBigMap: String? = null

    @Transient
    public open val destinationBigMap: String? = null

    @Serializable
    @SerialName(Update.KIND)
    public data class Update(
        @SerialName("big_map") override val bigMap: String,
        @SerialName("key_hash") override val keyHash: @Contextual ScriptExprHash,
        override val key: MichelineNode,
        override val value: MichelineNode? = null,
    ) : RpcBigMapDiff() {
        public companion object {
            internal const val KIND = "update"
        }
    }

    @Serializable
    @SerialName(Remove.KIND)
    public data class Remove(@SerialName("big_map") override val bigMap: String) : RpcBigMapDiff() {
        public companion object {
            internal const val KIND = "remove"
        }
    }

    @Serializable
    @SerialName(Copy.KIND)
    public data class Copy(
        @SerialName("source_big_map") override val sourceBigMap: String,
        @SerialName("destination_big_map") override val destinationBigMap: String,
    ) : RpcBigMapDiff() {
        public companion object {
            internal const val KIND = "copy"
        }
    }

    @Serializable
    @SerialName(Alloc.KIND)
    public data class Alloc(
        @SerialName("big_map") override val bigMap: String,
        @SerialName("key_type") override val keyType: MichelineNode,
        @SerialName("value_type") override val valueType: MichelineNode,
    ) : RpcBigMapDiff() {
        public companion object {
            internal const val KIND = "alloc"
        }
    }

    public companion object {
        internal const val CLASS_DISCRIMINATOR = "action"
    }
}