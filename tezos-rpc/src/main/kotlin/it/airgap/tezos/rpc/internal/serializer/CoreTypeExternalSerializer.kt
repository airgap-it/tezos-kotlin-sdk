@file:OptIn(ExperimentalSerializationApi::class)

package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.core.fromString
import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.rpc.internal.utils.KBaseEncodedSerializer
import it.airgap.tezos.rpc.internal.utils.KEncodedSerializer
import it.airgap.tezos.rpc.internal.utils.KJsonSerializer
import it.airgap.tezos.rpc.internal.utils.failWithUnexpectedJsonType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

// -- HexString --

internal object HexStringSerializer : KSerializer<HexString> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(HexString::class.toString(), PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): HexString = HexString(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: HexString) {
        encoder.encodeString(value.asString())
    }
}

// -- Timestamp --

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

internal object TimestampRfc3339Serializer : KSerializer<Timestamp.Rfc3339> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(Timestamp.Rfc3339::class.toString(), PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Timestamp.Rfc3339 = Timestamp.Rfc3339(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: Timestamp.Rfc3339) {
        encoder.encodeString(value.dateString)
    }
}

internal object TimestampMillisSerializer : KSerializer<Timestamp.Millis> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(Timestamp.Millis::class.toString(), PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): Timestamp.Millis = Timestamp.Millis(decoder.decodeLong())

    override fun serialize(encoder: Encoder, value: Timestamp.Millis) {
        encoder.encodeLong(value.long)
    }
}

// -- Encoded --

internal object AddressSerializer : KBaseEncodedSerializer<Address>(Address.Companion::fromString, Address::class)
internal object PublicKeyHashEncodedSerializer : KBaseEncodedSerializer<PublicKeyHashEncoded>(PublicKeyHashEncoded.Companion::fromString, PublicKeyHashEncoded::class)
internal object BlindedPublicKeyHashEncodedSerializer : KBaseEncodedSerializer<BlindedPublicKeyHashEncoded>(BlindedPublicKeyHashEncoded.Companion::fromString, BlindedPublicKeyHashEncoded::class)
internal object SignatureEncodedSerializer : KBaseEncodedSerializer<SignatureEncoded>(SignatureEncoded.Companion::fromString, SignatureEncoded::class)

internal object BlockHashSerializer : KEncodedSerializer<BlockHash>(BlockHash, BlockHash::class)
internal object ChainIdSerializer : KEncodedSerializer<ChainId>(ChainId, ChainId::class)
internal object ContractHashSerializer : KEncodedSerializer<ContractHash>(ContractHash, ContractHash::class)
internal object ContextHashSerializer : KEncodedSerializer<ContextHash>(ContextHash, ContextHash::class)
internal object CryptoboxPublicKeyHashSerializer : KEncodedSerializer<CryptoboxPublicKeyHash>(CryptoboxPublicKeyHash, CryptoboxPublicKeyHash::class)
internal object Ed25519BlindedPublicKeyHashSerializer : KEncodedSerializer<Ed25519BlindedPublicKeyHash>(Ed25519BlindedPublicKeyHash, Ed25519BlindedPublicKeyHash::class)
internal object Ed25519PublicKeyHashSerializer : KEncodedSerializer<Ed25519PublicKeyHash>(Ed25519PublicKeyHash, Ed25519PublicKeyHash::class)
internal object Ed25519SignatureSerializer : KEncodedSerializer<Ed25519Signature>(Ed25519Signature, Ed25519Signature::class)
internal object GenericSignatureSerializer : KEncodedSerializer<GenericSignature>(GenericSignature, GenericSignature::class)
internal object NonceHashSerializer : KEncodedSerializer<NonceHash>(NonceHash, NonceHash::class)
internal object OperationHashSerializer : KEncodedSerializer<OperationHash>(OperationHash, OperationHash::class)
internal object OperationListListHashSerializer : KEncodedSerializer<OperationListListHash>(OperationListListHash, OperationListListHash::class)
internal object P256PublicKeyHashSerializer : KEncodedSerializer<P256PublicKeyHash>(P256PublicKeyHash, P256PublicKeyHash::class)
internal object P256SignatureSerializer : KEncodedSerializer<P256Signature>(P256Signature, P256Signature::class)
internal object ProtocolHashSerializer : KEncodedSerializer<ProtocolHash>(ProtocolHash, ProtocolHash::class)
internal object Secp256K1PublicKeyHashSerializer : KEncodedSerializer<Secp256K1PublicKeyHash>(Secp256K1PublicKeyHash, Secp256K1PublicKeyHash::class)
internal object Secp256K1SignatureSerializer : KEncodedSerializer<Secp256K1Signature>(Secp256K1Signature, Secp256K1Signature::class)
