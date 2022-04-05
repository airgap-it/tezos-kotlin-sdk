package it.airgap.tezos.rpc.internal.utils

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
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

// -- JSON --

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

// -- String --

internal abstract class KStringSerializer<T : Any>(targetClass: KClass<T>) : KSerializer<T> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(targetClass.toString(), PrimitiveKind.STRING)

    protected abstract fun valueFromString(string: String): T
    protected abstract fun valueToString(value: T): String

    override fun deserialize(decoder: Decoder): T {
        val string = decoder.decodeString()
        return valueFromString(string)
    }

    override fun serialize(encoder: Encoder, value: T) {
        val string = valueToString(value)
        encoder.encodeString(string)
    }
}

// -- List<List<T>> --

internal abstract class KListListSerializer<T, S>(elementSerializer: KSerializer<S>) : KSerializer<T> {
    protected val delegateSerializer = ListSerializer(ListSerializer(elementSerializer))
    override val descriptor: SerialDescriptor = delegateSerializer.descriptor

    protected abstract fun valueFromListList(list: List<List<S>>): T
    protected abstract fun valueToListList(value: T): List<List<S>>

    override fun deserialize(decoder: Decoder): T {
        val list = delegateSerializer.deserialize(decoder)
        return valueFromListList(list)
    }

    override fun serialize(encoder: Encoder, value: T) {
        val list = valueToListList(value)
        encoder.encodeSerializableValue(delegateSerializer, list)
    }
}

// -- extensions --

@OptIn(ExperimentalSerializationApi::class)
public val SerialDescriptor.elementIndices: Iterable<Int>
    get() = (0 until elementsCount)

@OptIn(ExperimentalSerializationApi::class)
public fun SerialDescriptor.getElementNames(indices: Collection<Int>): List<String> =
    indices.map { getElementName(it) }
