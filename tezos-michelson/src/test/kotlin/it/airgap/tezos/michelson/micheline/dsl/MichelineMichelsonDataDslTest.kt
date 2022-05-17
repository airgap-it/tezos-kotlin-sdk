package it.airgap.tezos.michelson.micheline.dsl

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.michelson.internal.converter.MichelsonToMichelineConverter
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MichelineMichelsonDataDslTest {

    private lateinit var tezos: Tezos
    private lateinit var michelsonToMichelineConverter: MichelsonToMichelineConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        michelsonToMichelineConverter = MichelsonToMichelineConverter()
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `builds Micheline Michelson Data Expression`() {
        val expectedWithActual = listOf(
            MichelinePrimitiveApplication("Unit") to listOf(
                micheline(tezos) { Unit },
                micheline(michelsonToMichelineConverter) { Unit },
                micheline(tezos) { Unit() },
                micheline(michelsonToMichelineConverter) { Unit() },
                michelineData(tezos) { Unit },
                michelineData(michelsonToMichelineConverter) { Unit },
                michelineData(tezos) { Unit() },
                michelineData(michelsonToMichelineConverter) { Unit() },
            ),
            MichelinePrimitiveApplication("True") to listOf(
                micheline(tezos) { True },
                micheline(michelsonToMichelineConverter) { True },
                micheline(tezos) { True() },
                micheline(michelsonToMichelineConverter) { True() },
                michelineData(tezos) { True },
                michelineData(michelsonToMichelineConverter) { True },
                michelineData(tezos) { True() },
                michelineData(michelsonToMichelineConverter) { True() },
            ),
            MichelinePrimitiveApplication("False") to listOf(
                micheline(tezos) { False },
                micheline(michelsonToMichelineConverter) { False },
                micheline(tezos) { False() },
                micheline(michelsonToMichelineConverter) { False() },
                michelineData(tezos) { False },
                michelineData(michelsonToMichelineConverter) { False },
                michelineData(tezos) { False() },
                michelineData(michelsonToMichelineConverter) { False() },
            ),
            MichelinePrimitiveApplication(
                "Pair",
                args = listOf(
                    MichelinePrimitiveApplication("True"),
                    MichelinePrimitiveApplication("False"),
                ),
            ) to listOf(
                micheline(tezos) {
                    Pair {
                        arg { True }
                        arg { False }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    Pair {
                        arg { True }
                        arg { False }
                    }
                },
                michelineData(tezos) {
                    Pair {
                        arg { True }
                        arg { False }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
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
                micheline(tezos) {
                    Left {
                        arg { True }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    Left {
                        arg { True }
                    }
                },
                micheline(tezos) {
                    Left {
                        arg { False }
                        arg { True }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    Left {
                        arg { False }
                        arg { True }
                    }
                },
                michelineData(tezos) {
                    Left {
                        arg { True }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    Left {
                        arg { True }
                    }
                },
                michelineData(tezos) {
                    Left {
                        arg { False }
                        arg { True }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
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
                micheline(tezos) {
                    Right {
                        arg { True }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    Right {
                        arg { True }
                    }
                },
                micheline(tezos) {
                    Right {
                        arg { False }
                        arg { True }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    Right {
                        arg { False }
                        arg { True }
                    }
                },
                michelineData(tezos) {
                    Right {
                        arg { True }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    Right {
                        arg { True }
                    }
                },
                michelineData(tezos) {
                    Right {
                        arg { False }
                        arg { True }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
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
                micheline(tezos) {
                    Some {
                        arg { True }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    Some {
                        arg { True }
                    }
                },
                micheline(tezos) {
                    Some {
                        arg { False }
                        arg { True }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    Some {
                        arg { False }
                        arg { True }
                    }
                },
                michelineData(tezos) {
                    Some {
                        arg { True }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    Some {
                        arg { True }
                    }
                },
                michelineData(tezos) {
                    Some {
                        arg { False }
                        arg { True }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    Some {
                        arg { False }
                        arg { True }
                    }
                },
            ),
            MichelinePrimitiveApplication("None") to listOf(
                micheline(tezos) { None },
                micheline(michelsonToMichelineConverter) { None },
                micheline(tezos) { None() },
                micheline(michelsonToMichelineConverter) { None() },
                michelineData(tezos) { None },
                michelineData(michelsonToMichelineConverter) { None },
                michelineData(tezos) { None() },
                michelineData(michelsonToMichelineConverter) { None() },
            ),
            MichelinePrimitiveApplication(
                "Elt",
                args = listOf(
                    MichelinePrimitiveApplication("True"),
                    MichelinePrimitiveApplication("False"),
                )
            ) to listOf(
                micheline(tezos) {
                    Elt {
                        key { True }
                        value { False }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    Elt {
                        key { True }
                        value { False }
                    }
                },
                micheline(tezos) {
                    Elt {
                        key { Unit }
                        key { True }
                        value { Unit }
                        value { False }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    Elt {
                        key { Unit }
                        key { True }
                        value { Unit }
                        value { False }
                    }
                },
                michelineData(tezos) {
                    Elt {
                        key { True }
                        value { False }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    Elt {
                        key { True }
                        value { False }
                    }
                },
                michelineData(tezos) {
                    Elt {
                        key { Unit }
                        key { True }
                        value { Unit }
                        value { False }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
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