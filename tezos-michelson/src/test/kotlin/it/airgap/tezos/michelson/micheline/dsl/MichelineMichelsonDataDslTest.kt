package it.airgap.tezos.michelson.micheline.dsl

import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import org.junit.Test
import kotlin.test.assertEquals

class MichelineMichelsonDataDslTest {

    @Test
    fun `builds Micheline Michelson Data Expression`() {
        val expectedWithActual = listOf(
            MichelinePrimitiveApplication("Unit") to listOf(
                micheline { Unit },
                micheline { Unit() },
                michelineData { Unit },
                michelineData { Unit() },
            ),
            MichelinePrimitiveApplication("True") to listOf(
                micheline { True },
                micheline { True() },
                michelineData { True },
                michelineData { True() },
            ),
            MichelinePrimitiveApplication("False") to listOf(
                micheline { False },
                micheline { False() },
                michelineData { False },
                michelineData { False() },
            ),
            MichelinePrimitiveApplication(
                "Pair",
                args = listOf(
                    MichelinePrimitiveApplication("True"),
                    MichelinePrimitiveApplication("False"),
                ),
            ) to listOf(
                micheline {
                    Pair {
                        arg { True }
                        arg { False }
                    }
                },
                michelineData {
                    Pair {
                        arg { True }
                        arg { False }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "Left",
                args = listOf(MichelinePrimitiveApplication("True")),
            ) to listOf(
                micheline {
                    Left {
                        arg { True }
                    }
                },
                micheline {
                    Left {
                        arg { False }
                        arg { True }
                    }
                },
                michelineData {
                    Left {
                        arg { True }
                    }
                },
                michelineData {
                    Left {
                        arg { False }
                        arg { True }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "Right",
                args = listOf(MichelinePrimitiveApplication("True")),
            ) to listOf(
                micheline {
                    Right {
                        arg { True }
                    }
                },
                micheline {
                    Right {
                        arg { False }
                        arg { True }
                    }
                },
                michelineData {
                    Right {
                        arg { True }
                    }
                },
                michelineData {
                    Right {
                        arg { False }
                        arg { True }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "Some",
                args = listOf(MichelinePrimitiveApplication("True")),
            ) to listOf(
                micheline {
                    Some {
                        arg { True }
                    }
                },
                micheline {
                    Some {
                        arg { False }
                        arg { True }
                    }
                },
                michelineData {
                    Some {
                        arg { True }
                    }
                },
                michelineData {
                    Some {
                        arg { False }
                        arg { True }
                    }
                },
            ),
            MichelinePrimitiveApplication("None") to listOf(
                micheline { None },
                micheline { None() },
                michelineData { None },
                michelineData { None() },
            ),
            MichelinePrimitiveApplication(
                "Elt",
                args = listOf(
                    MichelinePrimitiveApplication("True"),
                    MichelinePrimitiveApplication("False"),
                )
            ) to listOf(
                micheline {
                    Elt {
                        key { True }
                        value { False }
                    }
                },
                micheline {
                    Elt {
                        key { Unit }
                        key { True }
                        value { Unit }
                        value { False }
                    }
                },
                michelineData {
                    Elt {
                        key { True }
                        value { False }
                    }
                },
                michelineData {
                    Elt {
                        key { Unit }
                        key { True }
                        value { Unit }
                        value { False }
                    }
                },
            )
        )

        expectedWithActual.forEach { (expected, actual) ->
            actual.forEach { assertEquals(expected, it) }
        }
    }
}