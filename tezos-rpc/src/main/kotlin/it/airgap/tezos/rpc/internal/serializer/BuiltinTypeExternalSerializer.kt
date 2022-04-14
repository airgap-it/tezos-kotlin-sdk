package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.rpc.internal.utils.KJsonSerializer
import it.airgap.tezos.rpc.internal.utils.failWithUnexpectedJsonType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder

// -- Pair<T, S> --

internal class PairSerializer<T, S>(
    private val firstSerializer: KSerializer<T>,
    private val secondSerializer: KSerializer<S>,
) : KJsonSerializer<Pair<T, S>> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(Pair::class.toString())

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): Pair<T, S> {
        val jsonArray = jsonElement as? JsonArray ?: failWithUnexpectedJsonType(jsonElement::class)
        if (jsonArray.size != 2) failWithInvalidSerializedValue(jsonArray)

        val first = jsonDecoder.json.decodeFromJsonElement(firstSerializer, jsonArray[0])
        val second = jsonDecoder.json.decodeFromJsonElement(secondSerializer, jsonArray[1])

        return Pair(first, second)
    }

    override fun serialize(jsonEncoder: JsonEncoder, value: Pair<T, S>) {
        val first = jsonEncoder.json.encodeToJsonElement(firstSerializer, value.first)
        val second = jsonEncoder.json.encodeToJsonElement(secondSerializer, value.second)

        val jsonArray = JsonArray(listOf(first, second))

        jsonEncoder.encodeJsonElement(jsonArray)
    }

    private fun failWithInvalidSerializedValue(value: JsonArray): Nothing =
        throw SerializationException("Could not deserialize, `${value}` is not a valid Pair.")
}