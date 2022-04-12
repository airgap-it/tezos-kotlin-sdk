package it.airgap.tezos.rpc.type.primitive

import kotlinx.serialization.Serializable

// -- RpcRatio --

@Serializable
public data class RpcRatio(
    public val numerator: UShort,
    public val denominator: UShort,
)