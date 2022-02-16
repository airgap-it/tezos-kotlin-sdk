package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.rpc.internal.utils.KJsonSerializer
import it.airgap.tezos.rpc.internal.utils.containsKeys
import it.airgap.tezos.rpc.internal.utils.doesNotContainKeys
import it.airgap.tezos.rpc.internal.utils.failWithUnexpectedJsonType
import it.airgap.tezos.rpc.type.RpcActiveChain
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
internal class RpcActiveChainSerializer<ChainId, ProtocolHash, Timestamp>(
    private val chainIdSerializer: KSerializer<ChainId>,
    private val protocolHashSerializer: KSerializer<ProtocolHash>,
    private val timestampSerializer: KSerializer<Timestamp>,
): KJsonSerializer<RpcActiveChain<ChainId, ProtocolHash, Timestamp>> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(RpcActiveChain::class.toString()) {
        element("chain_id", chainIdSerializer.descriptor, isOptional = true)
        element("test_protocol", protocolHashSerializer.descriptor, isOptional = true)
        element("expiration_date", timestampSerializer.descriptor, isOptional = true)
        element("stopping", chainIdSerializer.descriptor, isOptional = true)
    }

    override fun deserialize(jsonDecoder: JsonDecoder, jsonElement: JsonElement): RpcActiveChain<ChainId, ProtocolHash, Timestamp> {
        val jsonObject = jsonElement as? JsonObject ?: failWithUnexpectedJsonType(jsonElement::class)

        return when {
            jsonObject.isMain() -> jsonDecoder.decodeSerializableValue(RpcActiveChain.Main.serializer(chainIdSerializer, protocolHashSerializer, timestampSerializer))
            jsonObject.isTest() -> jsonDecoder.decodeSerializableValue(RpcActiveChain.Test.serializer(chainIdSerializer, protocolHashSerializer, timestampSerializer))
            jsonObject.isStopping() -> jsonDecoder.decodeSerializableValue(RpcActiveChain.Stopping.serializer(chainIdSerializer, protocolHashSerializer, timestampSerializer))
            else -> failWithUnknownValue(jsonElement.toString())
        }
    }

    private fun JsonObject.isMain(): Boolean = with(descriptor) {
        containsKey(getElementName(0)) && doesNotContainKeys(getElementName(1), getElementName(2), getElementName(3))
    }

    private fun JsonObject.isTest(): Boolean = with(descriptor) {
        containsKeys(getElementName(0), getElementName(1), getElementName(2)) && !containsKey(getElementName(3))
    }

    private fun JsonObject.isStopping(): Boolean = with(descriptor) {
        containsKey(getElementName(3)) && doesNotContainKeys(getElementName(0), getElementName(1), getElementName(2))
    }

    override fun serialize(jsonEncoder: JsonEncoder, value: RpcActiveChain<ChainId, ProtocolHash, Timestamp>) {
        when (value) {
            is RpcActiveChain.Main -> jsonEncoder.encodeSerializableValue(RpcActiveChain.Main.serializer(chainIdSerializer, protocolHashSerializer, timestampSerializer), value)
            is RpcActiveChain.Test -> jsonEncoder.encodeSerializableValue(RpcActiveChain.Test.serializer(chainIdSerializer, protocolHashSerializer, timestampSerializer), value)
            is RpcActiveChain.Stopping -> jsonEncoder.encodeSerializableValue(RpcActiveChain.Stopping.serializer(chainIdSerializer, protocolHashSerializer, timestampSerializer), value)
        }
    }

    private fun failWithUnknownValue(value: String): Nothing = throw SerializationException("Unknown ActiveChain value `$value`.")
}