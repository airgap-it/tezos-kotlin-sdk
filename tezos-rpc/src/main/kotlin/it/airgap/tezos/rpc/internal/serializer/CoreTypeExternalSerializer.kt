@file:OptIn(ExperimentalSerializationApi::class)

package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.core.fromString
import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
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
import kotlin.reflect.KClass

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

internal object AddressSerializer : BaseEncodedSerializer<Address>(Address.Companion::fromString, Address::class)
internal object ImplicitAddressSerializer : BaseEncodedSerializer<ImplicitAddress>(ImplicitAddress.Companion::fromString, ImplicitAddress::class)
internal object PublicKeyEncodedSerializer : BaseEncodedSerializer<PublicKeyEncoded>(PublicKeyEncoded.Companion::fromString, PublicKeyEncoded::class)
internal object PublicKeyHashEncodedSerializer : BaseEncodedSerializer<PublicKeyHashEncoded>(PublicKeyHashEncoded.Companion::fromString, PublicKeyHashEncoded::class)
internal object BlindedPublicKeyHashEncodedSerializer : BaseEncodedSerializer<BlindedPublicKeyHashEncoded>(BlindedPublicKeyHashEncoded.Companion::fromString, BlindedPublicKeyHashEncoded::class)
internal object SignatureEncodedSerializer : BaseEncodedSerializer<SignatureEncoded>(SignatureEncoded.Companion::fromString, SignatureEncoded::class)

internal object BlockHashSerializer : EncodedSerializer<BlockHash>(BlockHash, BlockHash::class)
internal object BlockPayloadHashSerializer : EncodedSerializer<BlockPayloadHash>(BlockPayloadHash, BlockPayloadHash::class)
internal object ChainIdSerializer : EncodedSerializer<ChainId>(ChainId, ChainId::class)
internal object ContractHashSerializer : EncodedSerializer<ContractHash>(ContractHash, ContractHash::class)
internal object ContextHashSerializer : EncodedSerializer<ContextHash>(ContextHash, ContextHash::class)
internal object CryptoboxPublicKeyHashSerializer : EncodedSerializer<CryptoboxPublicKeyHash>(CryptoboxPublicKeyHash, CryptoboxPublicKeyHash::class)
internal object Ed25519BlindedPublicKeyHashSerializer : EncodedSerializer<Ed25519BlindedPublicKeyHash>(Ed25519BlindedPublicKeyHash, Ed25519BlindedPublicKeyHash::class)
internal object Ed25519PublicKeyHashSerializer : EncodedSerializer<Ed25519PublicKeyHash>(Ed25519PublicKeyHash, Ed25519PublicKeyHash::class)
internal object Ed25519SignatureSerializer : EncodedSerializer<Ed25519Signature>(Ed25519Signature, Ed25519Signature::class)
internal object GenericSignatureSerializer : EncodedSerializer<GenericSignature>(GenericSignature, GenericSignature::class)
internal object NonceHashSerializer : EncodedSerializer<NonceHash>(NonceHash, NonceHash::class)
internal object OperationHashSerializer : EncodedSerializer<OperationHash>(OperationHash, OperationHash::class)
internal object OperationListListHashSerializer : EncodedSerializer<OperationListListHash>(OperationListListHash, OperationListListHash::class)
internal object P256PublicKeyHashSerializer : EncodedSerializer<P256PublicKeyHash>(P256PublicKeyHash, P256PublicKeyHash::class)
internal object P256SignatureSerializer : EncodedSerializer<P256Signature>(P256Signature, P256Signature::class)
internal object ProtocolHashSerializer : EncodedSerializer<ProtocolHash>(ProtocolHash, ProtocolHash::class)
internal object ScriptExprHashSerializer : EncodedSerializer<ScriptExprHash>(ScriptExprHash, ScriptExprHash::class)
internal object Secp256K1PublicKeyHashSerializer : EncodedSerializer<Secp256K1PublicKeyHash>(Secp256K1PublicKeyHash, Secp256K1PublicKeyHash::class)
internal object Secp256K1SignatureSerializer : EncodedSerializer<Secp256K1Signature>(Secp256K1Signature, Secp256K1Signature::class)

internal open class BaseEncodedSerializer<E : Encoded>(
    private val createValue: (String) -> E,
    kClass: KClass<E>,
) : KSerializer<E> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(kClass.toString(), PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): E = createValue(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: E) {
        encoder.encodeString(value.base58)
    }
}

internal open class EncodedSerializer<E>(kind: MetaEncoded.Kind<E>, kClass: KClass<E>) : BaseEncodedSerializer<E>(kind::createValue, kClass) where E : Encoded, E : MetaEncoded<E>
