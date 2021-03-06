package it.airgap.tezos.contract.internal.micheline

internal sealed interface MichelineTrace {
    val next: MichelineTrace?
    operator fun plus(other: MichelineTrace): MichelineTrace

    fun hasNext(): Boolean = next != null

    class Root(override val next: MichelineTrace? = null) : MichelineTrace {
        override fun plus(other: MichelineTrace): MichelineTrace = Root(next?.plus(other) ?: other)
    }

    class Node(val direction: Direction, override val next: MichelineTrace? = null) : MichelineTrace {
        constructor(index: Int, next: MichelineTrace? = null) : this(Direction.fromIndex(index), next)

        override fun plus(other: MichelineTrace): MichelineTrace = Node(direction, next?.plus(other) ?: other)

        enum class Direction(val index: Int) {
            Left(0), Right(1);

            companion object {
                fun fromIndex(index: Int): Direction {
                    val values = values()
                    return values[index % values.size]
                }
            }
        }
    }
}
