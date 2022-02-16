@file:OptIn(ExperimentalSerializationApi::class)

package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializer(forClass = HexString::class)
internal object HexStringSerializer : KSerializer<HexString> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(HexString::class.toString(), PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): HexString = HexString(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: HexString) {
        encoder.encodeString(value.asString())
    }
}

@Serializer(forClass = BlockHash::class)
internal object BlockHashSerializer : KSerializer<BlockHash> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(BlockHash::class.toString(), PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): BlockHash = BlockHash(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: BlockHash) {
        encoder.encodeString(value.base58)
    }
}

@Serializer(forClass = ChainId::class)
internal object ChainIdSerializer : KSerializer<ChainId> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(ChainId::class.toString(), PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ChainId = ChainId(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: ChainId) {
        encoder.encodeString(value.base58)
    }
}

@Serializer(forClass = ContextHash::class)
internal object ContextHashSerializer : KSerializer<ContextHash> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(ContextHash::class.toString(), PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ContextHash = ContextHash(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: ContextHash) {
        encoder.encodeString(value.base58)
    }
}

@Serializer(forClass = OperationHash::class)
internal object OperationHashSerializer : KSerializer<OperationHash> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(OperationHash::class.toString(), PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): OperationHash = OperationHash(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: OperationHash) {
        encoder.encodeString(value.base58)
    }
}

@Serializer(forClass = OperationListListHash::class)
internal object OperationListListHashSerializer : KSerializer<OperationListListHash> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(OperationListListHash::class.toString(), PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): OperationListListHash = OperationListListHash(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: OperationListListHash) {
        encoder.encodeString(value.base58)
    }
}

@Serializer(forClass = ProtocolHash::class)
internal object ProtocolHashSerializer : KSerializer<ProtocolHash> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(ProtocolHash::class.toString(), PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ProtocolHash = ProtocolHash(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: ProtocolHash) {
        encoder.encodeString(value.base58)
    }
}
