package it.airgap.tezos.rpc.shell.data

import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash
import it.airgap.tezos.rpc.type.RpcConnection
import it.airgap.tezos.rpc.type.RpcCryptoboxPublicKeyHash
import it.airgap.tezos.rpc.type.RpcUnistring
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

// ==== /network ====

// -- /connections --

@Serializable
@JvmInline
internal value class GetConnectionsTransitionalResponse(val connections: List<RpcConnection<RpcCryptoboxPublicKeyHash, RpcUnistring, RpcUnistring>>)

@Serializable
@JvmInline
public value class GetConnectionsResponse(public val connections: List<RpcConnection<@Contextual CryptoboxPublicKeyHash, String, String>>)

// -- /connections/<peer_id> --

@Serializable
@JvmInline
internal value class GetConnectionTransitionalResponse(val connection: RpcConnection<RpcCryptoboxPublicKeyHash, RpcUnistring, RpcUnistring>)

@Serializable
@JvmInline
public value class GetConnectionResponse(public val connection: RpcConnection<@Contextual CryptoboxPublicKeyHash, String, String>)

public typealias CloseConnectionResponse = Unit

// -- /greylist --

public typealias ClearGreylistResponse = Unit

@Serializable
internal data class GetGreylistedIPsTransitionalResponse(val ips: List<RpcUnistring>)