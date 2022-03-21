package it.airgap.tezos.rpc.internal.utils

import it.airgap.tezos.core.type.encoded.Encoded
import it.airgap.tezos.core.type.encoded.MetaEncoded
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

@PublishedApi
@Suppress("UNCHECKED_CAST")
internal inline fun <reified T : Any> serializer(): KSerializer<T> {
    val type = T::class.createType()
    return serializer(type) as KSerializer<T>
}

internal interface KJsonSerializer<T> : KSerializer<T> {
    override fun deserialize(decoder: Decoder): T {
        val jsonDecoder = decoder as? JsonDecoder ?: failWithExpectedJsonDecoder(decoder::class)
        val jsonElement = jsonDecoder.decodeJsonElement()

        return deserialize(jsonDecoder, jsonElement)
    }

    fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): T

    override fun serialize(encoder: Encoder, value: T) {
        val jsonEncoder = encoder as? JsonEncoder ?: failWithExpectedJsonEncoder(encoder::class)

        serialize(jsonEncoder, value)
    }

    fun serialize(jsonEncoder: JsonEncoder, value: T)

    private fun failWithExpectedJsonDecoder(actual: KClass<out Decoder>): Nothing =
        throw SerializationException("Expected Json decoder, got $actual.")

    private fun failWithExpectedJsonEncoder(actual: KClass<out Encoder>): Nothing =
        throw SerializationException("Expected Json encoder, got $actual.")
}

internal open class KBaseEncodedSerializer<E : Encoded>(
    private val createValue: (String) -> E,
    kClass: KClass<E>,
) : KSerializer<E> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(kClass.toString(), PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): E = createValue(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: E) {
        encoder.encodeString(value.base58)
    }
}

internal open class KEncodedSerializer<E>(kind: MetaEncoded.Kind<E>, kClass: KClass<E>) : KBaseEncodedSerializer<E>(kind::createValue, kClass) where E : Encoded, E : MetaEncoded<E>

@OptIn(ExperimentalSerializationApi::class)
public val SerialDescriptor.elementIndices: Iterable<Int>
    get() = (0 until elementsCount)

@OptIn(ExperimentalSerializationApi::class)
public fun SerialDescriptor.getElementNames(indices: Collection<Int>): List<String> =
    indices.map { getElementName(it) }