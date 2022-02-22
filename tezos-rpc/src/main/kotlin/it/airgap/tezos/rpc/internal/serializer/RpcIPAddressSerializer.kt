package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.rpc.type.RpcIPAddress
import it.airgap.tezos.rpc.type.RpcIPv4Address
import it.airgap.tezos.rpc.type.RpcIPv6Address
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object RpcIPAddressSerializer : KSerializer<RpcIPAddress> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(RpcIPAddress::class.toString(), PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): RpcIPAddress {
        val string = decoder.decodeString()

        return RpcIPAddress.fromStringOrNull(string) ?: failWithInvalidIPAddress(string)
    }

    override fun serialize(encoder: Encoder, value: RpcIPAddress) {
        when (value) {
            is RpcIPv4Address -> encoder.encodeString(value.string)
            is RpcIPv6Address -> encoder.encodeString(value.string)
        }
    }

    private fun failWithInvalidIPAddress(value: String): Nothing = throw SerializationException("Value `$value` is not a valid IP address.")
}