package it.airgap.tezos.operation.header

public data class Fitness(
    public val level: Int,
    public val lockedRound: Int? = null,
    public val predecessorRound: Int,
    public val round: Int,
)
