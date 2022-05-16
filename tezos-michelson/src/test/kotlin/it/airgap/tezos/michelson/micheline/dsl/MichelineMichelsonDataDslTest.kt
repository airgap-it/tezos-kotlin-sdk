package it.airgap.tezos.michelson.micheline.dsl

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.michelson.internal.converter.MichelsonToMichelineConverter
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MichelineMichelsonDataDslTest {

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var michelsonToMichelineConverter: MichelsonToMichelineConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        michelsonToMichelineConverter = MichelsonToMichelineConverter()

        every { dependencyRegistry.michelsonToMichelineConverter } returns michelsonToMichelineConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `builds Micheline Michelson Data Expression`() {
        val expectedWithActual = listOf(
            MichelinePrimitiveApplication("Unit") to listOf(
                micheline { Unit },
                micheline(michelsonToMichelineConverter) { Unit },
                micheline { Unit() },
                micheline(michelsonToMichelineConverter) { Unit() },
                michelineData { Unit },
                michelineData(michelsonToMichelineConverter) { Unit },
                michelineData { Unit() },
                michelineData(michelsonToMichelineConverter) { Unit() },
            ),
            MichelinePrimitiveApplication("True") to listOf(
                micheline { True },
                micheline(michelsonToMichelineConverter) { True },
                micheline { True() },
                micheline(michelsonToMichelineConverter) { True() },
                michelineData { True },
                michelineData(michelsonToMichelineConverter) { True },
                michelineData { True() },
                michelineData(michelsonToMichelineConverter) { True() },
            ),
            MichelinePrimitiveApplication("False") to listOf(
                micheline { False },
                micheline(michelsonToMichelineConverter) { False },
                micheline { False() },
                micheline(michelsonToMichelineConverter) { False() },
                michelineData { False },
                michelineData(michelsonToMichelineConverter) { False },
                michelineData { False() },
                michelineData(michelsonToMichelineConverter) { False() },
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
                micheline(michelsonToMichelineConverter) {
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
                micheline {
                    Left {
                        arg { True }
                    }
                },
                micheline(michelsonToMichelineConverter) {
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
                micheline(michelsonToMichelineConverter) {
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
                michelineData(michelsonToMichelineConverter) {
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
                micheline {
                    Right {
                        arg { True }
                    }
                },
                micheline(michelsonToMichelineConverter) {
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
                micheline(michelsonToMichelineConverter) {
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
                michelineData(michelsonToMichelineConverter) {
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
                micheline {
                    Some {
                        arg { True }
                    }
                },
                micheline(michelsonToMichelineConverter) {
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
                micheline(michelsonToMichelineConverter) {
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
                michelineData(michelsonToMichelineConverter) {
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
                michelineData(michelsonToMichelineConverter) {
                    Some {
                        arg { False }
                        arg { True }
                    }
                },
            ),
            MichelinePrimitiveApplication("None") to listOf(
                micheline { None },
                micheline(michelsonToMichelineConverter) { None },
                micheline { None() },
                micheline(michelsonToMichelineConverter) { None() },
                michelineData { None },
                michelineData(michelsonToMichelineConverter) { None },
                michelineData { None() },
                michelineData(michelsonToMichelineConverter) { None() },
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
                micheline(michelsonToMichelineConverter) {
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
                micheline(michelsonToMichelineConverter) {
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
                michelineData(michelsonToMichelineConverter) {
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