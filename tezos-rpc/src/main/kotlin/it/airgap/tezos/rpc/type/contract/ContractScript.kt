package it.airgap.tezos.rpc.type.contract

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcScriptParsing --

@Serializable
public enum class RpcScriptParsing {
    Readable,
    Optimized,
    @SerialName("Optimized_legacy") OptimizedLegacy,
}