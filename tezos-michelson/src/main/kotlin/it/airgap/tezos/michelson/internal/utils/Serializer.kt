package it.airgap.tezos.michelson.internal.utils

import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelineSequence
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlin.reflect.KClass

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

internal abstract class KListSerializer<T, S>(elementSerializer: KSerializer<S>) : KSerializer<T> {
    protected val delegateSerializer = ListSerializer(elementSerializer)
    override val descriptor: SerialDescriptor = delegateSerializer.descriptor

    protected abstract fun valueFromList(list: List<S>): T
    protected abstract fun valueToList(value: T): List<S>

    override fun deserialize(decoder: Decoder): T {
        val nodes = delegateSerializer.deserialize(decoder)
        return valueFromList(nodes)
    }

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeSerializableValue(delegateSerializer, valueToList(value))
    }
}
