package it.airgap.tezos.operation.header

import it.airgap.tezos.core.internal.type.BytesTag

public enum class LiquidityBakingToggleVote(override val value: ByteArray): BytesTag {
    On(byteArrayOf(0)),
    Off(byteArrayOf(1)),
    Pass(byteArrayOf(2)),
}