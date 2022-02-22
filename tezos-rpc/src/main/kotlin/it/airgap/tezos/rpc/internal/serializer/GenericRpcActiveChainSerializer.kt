package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.rpc.internal.utils.KJsonSerializer
import it.airgap.tezos.rpc.internal.utils.failWithUnexpectedJsonType
import it.airgap.tezos.rpc.internal.utils.hasElements
import it.airgap.tezos.rpc.type.GenericRpcActiveChain
import it.airgap.tezos.rpc.type.GenericRpcMainChain
import it.airgap.tezos.rpc.type.GenericRpcStoppingChain
import it.airgap.tezos.rpc.type.GenericRpcTestChain
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject

@OptIn(ExperimentalSerializationApi::class)
internal class GenericRpcActiveChainSerializer<ChainId, ProtocolHash, Timestamp>(
    private val chainIdSerializer: KSerializer<ChainId>,
    private val protocolHashSerializer: KSerializer<ProtocolHash>,
    private val timestampSerializer: KSerializer<Timestamp>,
): KJsonSerializer<GenericRpcActiveChain<ChainId, ProtocolHash, Timestamp>> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(GenericRpcActiveChain::class.toString()) {
        element("chain_id", chainIdSerializer.descriptor, isOptional = true)
        element("test_protocol", protocolHashSerializer.descriptor, isOptional = true)
        element("expiration_date", timestampSerializer.descriptor, isOptional = true)
        element("stopping", chainIdSerializer.descriptor, isOptional = true)
    }

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): GenericRpcActiveChain<ChainId, ProtocolHash, Timestamp> {
        val jsonObject = jsonElement as? JsonObject ?: failWithUnexpectedJsonType(jsonElement::class)

        return when {
            jsonObject.isMain() -> jsonDecoder.decodeSerializableValue(GenericRpcMainChain.serializer(chainIdSerializer, protocolHashSerializer, timestampSerializer))
            jsonObject.isTest() -> jsonDecoder.decodeSerializableValue(GenericRpcTestChain.serializer(chainIdSerializer, protocolHashSerializer, timestampSerializer))
            jsonObject.isStopping() -> jsonDecoder.decodeSerializableValue(GenericRpcStoppingChain.serializer(chainIdSerializer, protocolHashSerializer, timestampSerializer))
            else -> failWithUnknownValue(jsonObject.toString())
        }
    }

    override fun serialize(jsonEncoder: JsonEncoder, value: GenericRpcActiveChain<ChainId, ProtocolHash, Timestamp>) {
        when (value) {
            is GenericRpcMainChain -> jsonEncoder.encodeSerializableValue(GenericRpcMainChain.serializer(chainIdSerializer, protocolHashSerializer, timestampSerializer), value)
            is GenericRpcTestChain -> jsonEncoder.encodeSerializableValue(GenericRpcTestChain.serializer(chainIdSerializer, protocolHashSerializer, timestampSerializer), value)
            is GenericRpcStoppingChain -> jsonEncoder.encodeSerializableValue(GenericRpcStoppingChain.serializer(chainIdSerializer, protocolHashSerializer, timestampSerializer), value)
        }
    }

    private val mainElementIndices: Set<Int> = setOf(0)
    private val testElementIndices: Set<Int> = setOf(0, 1, 2)
    private val stoppingElementIndices: Set<Int> = setOf(3)

    private fun JsonObject.isMain(): Boolean = hasElements(descriptor, mainElementIndices)
    private fun JsonObject.isTest(): Boolean = hasElements(descriptor, testElementIndices)
    private fun JsonObject.isStopping(): Boolean = hasElements(descriptor, stoppingElementIndices)

    private fun failWithUnknownValue(value: String): Nothing = throw SerializationException("Unknown ActiveChain value `$value`.")
}