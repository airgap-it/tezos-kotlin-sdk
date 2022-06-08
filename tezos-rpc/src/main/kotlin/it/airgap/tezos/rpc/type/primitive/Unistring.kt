package it.airgap.tezos.rpc.type.primitive

import it.airgap.tezos.rpc.internal.serializer.RpcUnistringSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = RpcUnistringSerializer::class)
public sealed interface RpcUnistring {

    @Serializable
    @JvmInline
    public value class PlainUtf8(public val string: String) : RpcUnistring

    @Serializable
    public data class InvalidUtf8(@SerialName("invalid_utf8_string") public val invalidUtf8String: ByteArray) : RpcUnistring {
        override fun equals(other: Any?): Boolean =
            when {
                this === other -> true
                other is InvalidUtf8 -> invalidUtf8String.contentEquals(other.invalidUtf8String)
                else -> false
            }

        override fun hashCode(): Int = invalidUtf8String.contentHashCode()
    }
}
