@file:OptIn(ExperimentalSerializationApi::class)

package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.rpc.internal.utils.KJsonSerializer
import it.airgap.tezos.rpc.internal.utils.failWithUnexpectedJsonType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

// -- HexString --

@Serializer(forClass = HexString::class)
internal object HexStringSerializer : KSerializer<HexString> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(HexString::class.toString(), PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): HexString = HexString(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: HexString) {
        encoder.encodeString(value.asString())
    }
}

// -- Timestamp --

@Serializer(forClass = Timestamp::class)
internal object TimestampSerializer : KJsonSerializer<Timestamp> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(Timestamp::class.toString())

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): Timestamp {
        val jsonPrimitive = jsonElement as? JsonPrimitive ?: failWithUnexpectedJsonType(jsonElement::class)

        return when {
            jsonPrimitive.isRfc3339DateString() -> Timestamp.Rfc3339(jsonPrimitive.content)
            jsonPrimitive.isMillis() -> Timestamp.Millis(jsonPrimitive.long)
            else -> failWithInvalidSerializedValue(jsonPrimitive.content)
        }
    }

    override fun serialize(jsonEncoder: JsonEncoder, value: Timestamp) {
        when (value) {
            is Timestamp.Rfc3339 -> jsonEncoder.encodeString(value.dateString)
            is Timestamp.Millis -> jsonEncoder.encodeLong(value.long)
        }
    }

    private fun JsonPrimitive.isRfc3339DateString(): Boolean = isString && Timestamp.Rfc3339.isValid(content)
    private fun JsonPrimitive.isMillis(): Boolean = longOrNull != null

    private fun failWithInvalidSerializedValue(value: String): Nothing = throw SerializationException("Could not deserialize, `$value` is not a valid Timestamp value.")
}

@Serializer(forClass = Timestamp.Rfc3339::class)
internal object TimestampRfc3339Serializer : KSerializer<Timestamp.Rfc3339> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(Timestamp.Rfc3339::class.toString(), PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Timestamp.Rfc3339 = Timestamp.Rfc3339(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: Timestamp.Rfc3339) {
        encoder.encodeString(value.dateString)
    }
}

@Serializer(forClass = Timestamp.Millis::class)
internal object TimestampMillisSerializer : KSerializer<Timestamp.Millis> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(Timestamp.Millis::class.toString(), PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): Timestamp.Millis = Timestamp.Millis(decoder.decodeLong())

    override fun serialize(encoder: Encoder, value: Timestamp.Millis) {
        encoder.encodeLong(value.long)
    }
}

// -- Encoded --

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

@Serializer(forClass = CryptoboxPublicKeyHash::class)
internal object CryptoboxPublicKeyHashSerializer : KSerializer<CryptoboxPublicKeyHash> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(CryptoboxPublicKeyHash::class.toString(), PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): CryptoboxPublicKeyHash = CryptoboxPublicKeyHash(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: CryptoboxPublicKeyHash) {
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
