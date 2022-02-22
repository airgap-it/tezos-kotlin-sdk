package it.airgap.tezos.rpc.type

import it.airgap.tezos.rpc.internal.serializer.RpcIPAddressSerializer
import kotlinx.serialization.Serializable
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress

// -- RpcIPAddress --

internal typealias TransitionalRpcIPAddress = Unistring

@Serializable(with = RpcIPAddressSerializer::class)
public sealed interface RpcIPAddress {
    public companion object {
        public fun fromStringOrNull(string: String): RpcIPAddress? =
            when {
                RpcIPv4Address.isValid(string) -> RpcIPv4Address(string)
                RpcIPv6Address.isValid(string) -> RpcIPv6Address(string)
                else -> null
            }

        public fun isValid(string: String): Boolean = RpcIPv4Address.isValid(string) || RpcIPv6Address.isValid(string)
    }
}

@Serializable
@JvmInline
public value class RpcIPv4Address(public val string: String) : RpcIPAddress {
    public companion object {
        public fun isValid(string: String): Boolean = runCatching { InetAddress.getByName(string) }.getOrNull()?.takeIf { it is Inet4Address } != null
    }
}

@Serializable
@JvmInline
public value class RpcIPv6Address(public val string: String) : RpcIPAddress {
    public companion object {
        public fun isValid(string: String): Boolean = runCatching { InetAddress.getByName(string) }.getOrNull()?.takeIf { it is Inet6Address } != null
    }
}
