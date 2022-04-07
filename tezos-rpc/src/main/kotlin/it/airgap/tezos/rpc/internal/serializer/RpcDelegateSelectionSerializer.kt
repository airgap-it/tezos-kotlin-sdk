package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.core.type.encoded.PublicKeyEncoded
import it.airgap.tezos.rpc.internal.utils.KJsonSerializer
import it.airgap.tezos.rpc.internal.utils.KListListSerializer
import it.airgap.tezos.rpc.internal.utils.KStringSerializer
import it.airgap.tezos.rpc.internal.utils.failWithUnexpectedJsonType
import it.airgap.tezos.rpc.type.delegate.RpcDelegateSelection
import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.json.*

internal object RpcDelegateSelectionSerializer : KJsonSerializer<RpcDelegateSelection> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(RpcDelegateSelection::class.toString())

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): RpcDelegateSelection =
        when (jsonElement) {
            is JsonPrimitive -> jsonDecoder.json.decodeFromJsonElement(RpcDelegateSelection.Random.serializer(), jsonElement)
            is JsonArray -> jsonDecoder.json.decodeFromJsonElement(RpcDelegateSelection.RoundRobinOver.serializer(), jsonElement)
            else -> failWithUnexpectedJsonType(jsonElement::class)
        }

    override fun serialize(jsonEncoder: JsonEncoder, value: RpcDelegateSelection) {
        when (value) {
            is RpcDelegateSelection.Random -> jsonEncoder.encodeSerializableValue(RpcDelegateSelection.Random.serializer(), value)
            is RpcDelegateSelection.RoundRobinOver -> jsonEncoder.encodeSerializableValue(RpcDelegateSelection.RoundRobinOver.serializer(), value)
        }
    }
}

internal object RpcRandomDelegateSelectionSerializer : KStringSerializer<RpcDelegateSelection.Random>(RpcDelegateSelection.Random::class) {
    private const val SERIAL_NAME = "random"

    override fun valueFromString(string: String): RpcDelegateSelection.Random {
        if(string != SERIAL_NAME) failWithInvalidSerializedValue(string)
        return RpcDelegateSelection.Random
    }

    override fun valueToString(value: RpcDelegateSelection.Random): String = SERIAL_NAME

    private fun failWithInvalidSerializedValue(value: String): Nothing =
        throw SerializationException("Could not deserialize, `$value` is not a valid RpcDelegateSelection.Random value.")
}

@OptIn(ExperimentalSerializationApi::class)
internal object RpcRoundRobinOverDelegateSelectionSerializer : KListListSerializer<RpcDelegateSelection.RoundRobinOver, PublicKeyEncoded>(ContextualSerializer(PublicKeyEncoded::class)) {
    override fun valueFromListList(list: List<List<PublicKeyEncoded>>): RpcDelegateSelection.RoundRobinOver = RpcDelegateSelection.RoundRobinOver(list)
    override fun valueToListList(value: RpcDelegateSelection.RoundRobinOver): List<List<PublicKeyEncoded>> = value.publicKeys
}