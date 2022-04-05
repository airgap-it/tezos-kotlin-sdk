package it.airgap.tezos.rpc.type

import it.airgap.tezos.rpc.internal.serializer.RpcHistoryModeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcHistoryMode --

@Serializable(with = RpcHistoryModeSerializer::class)
public sealed class RpcHistoryMode {
    public object Archive : RpcHistoryMode()

    @Serializable
    public data class Full(public val full: RpcAdditionalCycles? = null) : RpcHistoryMode()

    @Serializable
    public data class Rolling(public val rolling: RpcAdditionalCycles? = null) : RpcHistoryMode()
}

// -- RpcAdditionalCycles

@Serializable
public data class RpcAdditionalCycles(@SerialName("additional_cycles") public val additionalCycles: UInt)