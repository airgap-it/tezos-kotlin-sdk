package it.airgap.tezos.rpc.type

import it.airgap.tezos.rpc.internal.serializer.UnistringSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = UnistringSerializer::class)
internal sealed interface Unistring {

    @Serializable
    @JvmInline
    value class PlainUtf8(val string: String) : Unistring

    @Serializable
    data class InvalidUtf8(@SerialName("invalid_utf8_string") val invalidUtf8String: ByteArray) :
        Unistring {
        override fun equals(other: Any?): Boolean =
            when {
                this === other -> true
                other is InvalidUtf8 -> invalidUtf8String.contentEquals(other.invalidUtf8String)
                else -> false
            }

        override fun hashCode(): Int = invalidUtf8String.contentHashCode()
    }
}
