package it.airgap.tezos.rpc.type.network

import it.airgap.tezos.rpc.internal.context.TezosRpcContext.failWithIllegalArgument
import it.airgap.tezos.rpc.internal.serializer.RpcIPAddressSerializer
import kotlinx.serialization.Serializable
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress

// -- RpcIPAddress --

@Serializable(with = RpcIPAddressSerializer::class)
public sealed interface RpcIPAddress {
    public companion object {
        public fun fromString(string: String): RpcIPAddress = fromStringOrNull(string) ?: failWithInvalidIPAddress(string)
        public fun fromStringOrNull(string: String): RpcIPAddress? =
            when {
                RpcIPv4Address.isValid(string) -> RpcIPv4Address(string)
                RpcIPv6Address.isValid(string) -> RpcIPv6Address(string)
                else -> null
            }

        public fun isValid(string: String): Boolean = RpcIPv4Address.isValid(string) || RpcIPv6Address.isValid(string)

        private fun failWithInvalidIPAddress(string: String): Nothing = failWithIllegalArgument("Value `${string}` is not a valid IP address.")
    }
}

@Serializable
@JvmInline
public value class RpcIPv4Address(public val string: String) : RpcIPAddress {
    init {
        require(isValid(string)) { "Invalid IPv4 address." }
    }

    public companion object {
        public fun isValid(string: String): Boolean =
            if (string.isBlank()) false
            else runCatching { InetAddress.getByName(string) }.getOrNull()?.takeIf { it is Inet4Address } != null
    }
}

@Serializable
@JvmInline
public value class RpcIPv6Address(public val string: String) : RpcIPAddress {
    init {
        require(isValid(string)) { "Invalid IPv6 address." }
    }

    public companion object {
        public fun isValid(string: String): Boolean =
            if (string.isBlank()) false
            else runCatching { InetAddress.getByName(string) }.getOrNull()?.takeIf { it is Inet6Address } != null
    }
}
