package it.airgap.tezos.michelson.micheline.dsl

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.converter.MichelsonToMichelineConverter
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MichelineMichelsonInstructionDslTest {

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
    fun `builds Micheline Michelson Instruction Expression`() = withTezosContext {
        val expectedWithActual = listOf(
            MichelinePrimitiveApplication("DROP") to listOf(
                micheline(tezos) { DROP },
                micheline(michelsonToMichelineConverter) { DROP },
                micheline(tezos) { DROP() },
                micheline(michelsonToMichelineConverter) { DROP() },
                michelineData(tezos) { DROP },
                michelineData(michelsonToMichelineConverter) { DROP },
                michelineData(tezos) { DROP() },
                michelineData(michelsonToMichelineConverter) { DROP() },
                michelineInstruction(tezos) { DROP },
                michelineInstruction(michelsonToMichelineConverter) { DROP },
                michelineInstruction(tezos) { DROP() },
                michelineInstruction(michelsonToMichelineConverter) { DROP() },
            ),
            MichelinePrimitiveApplication(
                "DROP",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline(tezos) { DROP("1") },
                micheline(michelsonToMichelineConverter) { DROP("1") },
                micheline(tezos) { DROP((1).toUByte()) },
                micheline(michelsonToMichelineConverter) { DROP((1).toUByte()) },
                micheline(tezos) { DROP((1).toUShort()) },
                micheline(michelsonToMichelineConverter) { DROP((1).toUShort()) },
                micheline(tezos) { DROP(1U) },
                micheline(michelsonToMichelineConverter) { DROP(1U) },
                micheline(tezos) { DROP(1UL) },
                micheline(michelsonToMichelineConverter) { DROP(1UL) },
                micheline(tezos) {
                    DROP { arg("1") }
                },
                micheline(michelsonToMichelineConverter) {
                    DROP { arg("1") }
                },
                micheline(tezos) {
                    DROP { arg((1).toUByte()) }
                },
                micheline(michelsonToMichelineConverter) {
                    DROP { arg((1).toUByte()) }
                },
                micheline(tezos) {
                    DROP { arg((1).toUShort()) }
                },
                micheline(michelsonToMichelineConverter) {
                    DROP { arg((1).toUShort()) }
                },
                micheline(tezos) {
                    DROP { arg(1U) }
                },
                micheline(michelsonToMichelineConverter) {
                    DROP { arg(1U) }
                },
                micheline(tezos) {
                    DROP { arg(1UL) }
                },
                micheline(michelsonToMichelineConverter) {
                    DROP { arg(1UL) }
                },
                michelineData(tezos) { DROP("1") },
                michelineData(michelsonToMichelineConverter) { DROP("1") },
                michelineData(tezos) { DROP((1).toUByte()) },
                michelineData(michelsonToMichelineConverter) { DROP((1).toUByte()) },
                michelineData(tezos) { DROP((1).toUShort()) },
                michelineData(michelsonToMichelineConverter) { DROP((1).toUShort()) },
                michelineData(tezos) { DROP(1U) },
                michelineData(michelsonToMichelineConverter) { DROP(1U) },
                michelineData(tezos) { DROP(1UL) },
                michelineData(michelsonToMichelineConverter) { DROP(1UL) },
                michelineData(tezos) {
                    DROP { arg("1") }
                },
                michelineData(michelsonToMichelineConverter) {
                    DROP { arg("1") }
                },
                michelineData(tezos) {
                    DROP { arg((1).toUByte()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DROP { arg((1).toUByte()) }
                },
                michelineData(tezos) {
                    DROP { arg((1).toUShort()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DROP { arg((1).toUShort()) }
                },
                michelineData(tezos) {
                    DROP { arg(1U) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DROP { arg(1U) }
                },
                michelineData(tezos) {
                    DROP { arg(1UL) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DROP { arg(1UL) }
                },
                michelineInstruction(tezos) { DROP("1") },
                michelineInstruction(michelsonToMichelineConverter) { DROP("1") },
                michelineInstruction(tezos) { DROP((1).toUByte()) },
                michelineInstruction(michelsonToMichelineConverter) { DROP((1).toUByte()) },
                michelineInstruction(tezos) { DROP((1).toUShort()) },
                michelineInstruction(michelsonToMichelineConverter) { DROP((1).toUShort()) },
                michelineInstruction(tezos) { DROP(1U) },
                michelineInstruction(michelsonToMichelineConverter) { DROP(1U) },
                michelineInstruction(tezos) { DROP(1UL) },
                michelineInstruction(michelsonToMichelineConverter) { DROP(1UL) },
                michelineInstruction(tezos) {
                    DROP { arg("1") }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DROP { arg("1") }
                },
                michelineInstruction(tezos) {
                    DROP { arg((1).toUByte()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DROP { arg((1).toUByte()) }
                },
                michelineInstruction(tezos) {
                    DROP { arg((1).toUShort()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DROP { arg((1).toUShort()) }
                },
                michelineInstruction(tezos) {
                    DROP { arg(1U) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DROP { arg(1U) }
                },
                michelineInstruction(tezos) {
                    DROP { arg(1UL) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DROP { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication("DUP") to listOf(
                micheline(tezos) { DUP },
                micheline(michelsonToMichelineConverter) { DUP },
                micheline(tezos) { DUP() },
                micheline(michelsonToMichelineConverter) { DUP() },
                michelineData(tezos) { DUP },
                michelineData(michelsonToMichelineConverter) { DUP },
                michelineData(tezos) { DUP() },
                michelineData(michelsonToMichelineConverter) { DUP() },
                michelineInstruction(tezos) { DUP },
                michelineInstruction(michelsonToMichelineConverter) { DUP },
                michelineInstruction(tezos) { DUP() },
                michelineInstruction(michelsonToMichelineConverter) { DUP() },
            ),
            MichelinePrimitiveApplication(
                "DUP",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline(tezos) { DUP("1") },
                micheline(michelsonToMichelineConverter) { DUP("1") },
                micheline(tezos) { DUP((1).toUByte()) },
                micheline(michelsonToMichelineConverter) { DUP((1).toUByte()) },
                micheline(tezos) { DUP((1).toUShort()) },
                micheline(michelsonToMichelineConverter) { DUP((1).toUShort()) },
                micheline(tezos) { DUP(1U) },
                micheline(michelsonToMichelineConverter) { DUP(1U) },
                micheline(tezos) { DUP(1UL) },
                micheline(michelsonToMichelineConverter) { DUP(1UL) },
                micheline(tezos) {
                    DUP { arg("1") }
                },
                micheline(michelsonToMichelineConverter) {
                    DUP { arg("1") }
                },
                micheline(tezos) {
                    DUP { arg((1).toUByte()) }
                },
                micheline(michelsonToMichelineConverter) {
                    DUP { arg((1).toUByte()) }
                },
                micheline(tezos) {
                    DUP { arg((1).toUShort()) }
                },
                micheline(michelsonToMichelineConverter) {
                    DUP { arg((1).toUShort()) }
                },
                micheline(tezos) {
                    DUP { arg(1U) }
                },
                micheline(michelsonToMichelineConverter) {
                    DUP { arg(1U) }
                },
                micheline(tezos) {
                    DUP { arg(1UL) }
                },
                micheline(michelsonToMichelineConverter) {
                    DUP { arg(1UL) }
                },
                michelineData(tezos) { DUP("1") },
                michelineData(michelsonToMichelineConverter) { DUP("1") },
                michelineData(tezos) { DUP((1).toUByte()) },
                michelineData(michelsonToMichelineConverter) { DUP((1).toUByte()) },
                michelineData(tezos) { DUP((1).toUShort()) },
                michelineData(michelsonToMichelineConverter) { DUP((1).toUShort()) },
                michelineData(tezos) { DUP(1U) },
                michelineData(michelsonToMichelineConverter) { DUP(1U) },
                michelineData(tezos) { DUP(1UL) },
                michelineData(michelsonToMichelineConverter) { DUP(1UL) },
                michelineData(tezos) {
                    DUP { arg("1") }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUP { arg("1") }
                },
                michelineData(tezos) {
                    DUP { arg((1).toUByte()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUP { arg((1).toUByte()) }
                },
                michelineData(tezos) {
                    DUP { arg((1).toUShort()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUP { arg((1).toUShort()) }
                },
                michelineData(tezos) {
                    DUP { arg(1U) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUP { arg(1U) }
                },
                michelineData(tezos) {
                    DUP { arg(1UL) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUP { arg(1UL) }
                },
                michelineInstruction(tezos) { DUP("1") },
                michelineInstruction(michelsonToMichelineConverter) { DUP("1") },
                michelineInstruction(tezos) { DUP((1).toUByte()) },
                michelineInstruction(michelsonToMichelineConverter) { DUP((1).toUByte()) },
                michelineInstruction(tezos) { DUP((1).toUShort()) },
                michelineInstruction(michelsonToMichelineConverter) { DUP((1).toUShort()) },
                michelineInstruction(tezos) { DUP(1U) },
                michelineInstruction(michelsonToMichelineConverter) { DUP(1U) },
                michelineInstruction(tezos) { DUP(1UL) },
                michelineInstruction(michelsonToMichelineConverter) { DUP(1UL) },
                michelineInstruction(tezos) {
                    DUP { arg("1") }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUP { arg("1") }
                },
                michelineInstruction(tezos) {
                    DUP { arg((1).toUByte()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUP { arg((1).toUByte()) }
                },
                michelineInstruction(tezos) {
                    DUP { arg((1).toUShort()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUP { arg((1).toUShort()) }
                },
                michelineInstruction(tezos) {
                    DUP { arg(1U) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUP { arg(1U) }
                },
                michelineInstruction(tezos) {
                    DUP { arg(1UL) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUP { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication("SWAP") to listOf(
                micheline(tezos) { SWAP },
                micheline(michelsonToMichelineConverter) { SWAP },
                micheline(tezos) { SWAP() },
                micheline(michelsonToMichelineConverter) { SWAP() },
                michelineData(tezos) { SWAP },
                michelineData(michelsonToMichelineConverter) { SWAP },
                michelineData(tezos) { SWAP() },
                michelineData(michelsonToMichelineConverter) { SWAP() },
                michelineInstruction(tezos) { SWAP },
                michelineInstruction(michelsonToMichelineConverter) { SWAP },
                michelineInstruction(tezos) { SWAP() },
                michelineInstruction(michelsonToMichelineConverter) { SWAP() },
            ),
            MichelinePrimitiveApplication("DIG") to listOf(
                micheline(tezos) { DIG },
                micheline(michelsonToMichelineConverter) { DIG },
                micheline(tezos) { DIG() },
                micheline(michelsonToMichelineConverter) { DIG() },
                michelineData(tezos) { DIG },
                michelineData(michelsonToMichelineConverter) { DIG },
                michelineData(tezos) { DIG() },
                michelineData(michelsonToMichelineConverter) { DIG() },
                michelineInstruction(tezos) { DIG },
                michelineInstruction(michelsonToMichelineConverter) { DIG },
                michelineInstruction(tezos) { DIG() },
                michelineInstruction(michelsonToMichelineConverter) { DIG() },
            ),
            MichelinePrimitiveApplication(
                "DIG",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline(tezos) { DIG("1") },
                micheline(michelsonToMichelineConverter) { DIG("1") },
                micheline(tezos) { DIG((1).toUByte()) },
                micheline(michelsonToMichelineConverter) { DIG((1).toUByte()) },
                micheline(tezos) { DIG((1).toUShort()) },
                micheline(michelsonToMichelineConverter) { DIG((1).toUShort()) },
                micheline(tezos) { DIG(1U) },
                micheline(michelsonToMichelineConverter) { DIG(1U) },
                micheline(tezos) { DIG(1UL) },
                micheline(michelsonToMichelineConverter) { DIG(1UL) },
                micheline(tezos) {
                    DIG { arg("1") }
                },
                micheline(michelsonToMichelineConverter) {
                    DIG { arg("1") }
                },
                micheline(tezos) {
                    DIG { arg((1).toUByte()) }
                },
                micheline(michelsonToMichelineConverter) {
                    DIG { arg((1).toUByte()) }
                },
                micheline(tezos) {
                    DIG { arg((1).toUShort()) }
                },
                micheline(michelsonToMichelineConverter) {
                    DIG { arg((1).toUShort()) }
                },
                micheline(tezos) {
                    DIG { arg(1U) }
                },
                micheline(michelsonToMichelineConverter) {
                    DIG { arg(1U) }
                },
                micheline(tezos) {
                    DIG { arg(1UL) }
                },
                micheline(michelsonToMichelineConverter) {
                    DIG { arg(1UL) }
                },
                michelineData(michelsonToMichelineConverter) { DIG("1") },
                michelineData(michelsonToMichelineConverter) { DIG((1).toUByte()) },
                michelineData(michelsonToMichelineConverter) { DIG((1).toUShort()) },
                michelineData(michelsonToMichelineConverter) { DIG(1U) },
                michelineData(michelsonToMichelineConverter) { DIG(1UL) },
                michelineData(tezos) {
                    DIG { arg("1") }
                },
                michelineData(michelsonToMichelineConverter) {
                    DIG { arg("1") }
                },
                michelineData(tezos) {
                    DIG { arg((1).toUByte()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DIG { arg((1).toUByte()) }
                },
                michelineData(tezos) {
                    DIG { arg((1).toUShort()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DIG { arg((1).toUShort()) }
                },
                michelineData(tezos) {
                    DIG { arg(1U) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DIG { arg(1U) }
                },
                michelineData(tezos) {
                    DIG { arg(1UL) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DIG { arg(1UL) }
                },
                michelineInstruction(michelsonToMichelineConverter) { DIG("1") },
                michelineInstruction(michelsonToMichelineConverter) { DIG((1).toUByte()) },
                michelineInstruction(michelsonToMichelineConverter) { DIG((1).toUShort()) },
                michelineInstruction(michelsonToMichelineConverter) { DIG(1U) },
                michelineInstruction(michelsonToMichelineConverter) { DIG(1UL) },
                michelineInstruction(tezos) {
                    DIG { arg("1") }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DIG { arg("1") }
                },
                michelineInstruction(tezos) {
                    DIG { arg((1).toUByte()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DIG { arg((1).toUByte()) }
                },
                michelineInstruction(tezos) {
                    DIG { arg((1).toUShort()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DIG { arg((1).toUShort()) }
                },
                michelineInstruction(tezos) {
                    DIG { arg(1U) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DIG { arg(1U) }
                },
                michelineInstruction(tezos) {
                    DIG { arg(1UL) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DIG { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication("DUG") to listOf(
                micheline(michelsonToMichelineConverter) { DUG },
                micheline(michelsonToMichelineConverter) { DUG() },
                michelineData(michelsonToMichelineConverter) { DUG },
                michelineData(michelsonToMichelineConverter) { DUG() },
                michelineInstruction(michelsonToMichelineConverter) { DUG },
                michelineInstruction(michelsonToMichelineConverter) { DUG() },
            ),
            MichelinePrimitiveApplication(
                "DUG",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline(michelsonToMichelineConverter) { DUG("1") },
                micheline(michelsonToMichelineConverter) { DUG((1).toUByte()) },
                micheline(michelsonToMichelineConverter) { DUG((1).toUShort()) },
                micheline(michelsonToMichelineConverter) { DUG(1U) },
                micheline(michelsonToMichelineConverter) { DUG(1UL) },
                micheline(tezos) {
                    DUG { arg("1") }
                },
                micheline(michelsonToMichelineConverter) {
                    DUG { arg("1") }
                },
                micheline(tezos) {
                    DUG { arg((1).toUByte()) }
                },
                micheline(michelsonToMichelineConverter) {
                    DUG { arg((1).toUByte()) }
                },
                micheline(tezos) {
                    DUG { arg((1).toUShort()) }
                },
                micheline(michelsonToMichelineConverter) {
                    DUG { arg((1).toUShort()) }
                },
                micheline(tezos) {
                    DUG { arg(1U) }
                },
                micheline(michelsonToMichelineConverter) {
                    DUG { arg(1U) }
                },
                micheline(tezos) {
                    DUG { arg(1UL) }
                },
                micheline(michelsonToMichelineConverter) {
                    DUG { arg(1UL) }
                },
                michelineData(michelsonToMichelineConverter) { DUG("1") },
                michelineData(michelsonToMichelineConverter) { DUG((1).toUByte()) },
                michelineData(michelsonToMichelineConverter) { DUG((1).toUShort()) },
                michelineData(michelsonToMichelineConverter) { DUG(1U) },
                michelineData(michelsonToMichelineConverter) { DUG(1UL) },
                michelineData(tezos) {
                    DUG { arg("1") }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUG { arg("1") }
                },
                michelineData(tezos) {
                    DUG { arg((1).toUByte()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUG { arg((1).toUByte()) }
                },
                michelineData(tezos) {
                    DUG { arg((1).toUShort()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUG { arg((1).toUShort()) }
                },
                michelineData(tezos) {
                    DUG { arg(1U) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUG { arg(1U) }
                },
                michelineData(tezos) {
                    DUG { arg(1UL) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUG { arg(1UL) }
                },
                michelineInstruction(michelsonToMichelineConverter) { DUG("1") },
                michelineInstruction(michelsonToMichelineConverter) { DUG((1).toUByte()) },
                michelineInstruction(michelsonToMichelineConverter) { DUG((1).toUShort()) },
                michelineInstruction(michelsonToMichelineConverter) { DUG(1U) },
                michelineInstruction(michelsonToMichelineConverter) { DUG(1UL) },
                michelineInstruction(tezos) {
                    DUG { arg("1") }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUG { arg("1") }
                },
                michelineInstruction(tezos) {
                    DUG { arg((1).toUByte()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUG { arg((1).toUByte()) }
                },
                michelineInstruction(tezos) {
                    DUG { arg((1).toUShort()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUG { arg((1).toUShort()) }
                },
                michelineInstruction(tezos) {
                    DUG { arg(1U) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUG { arg(1U) }
                },
                michelineInstruction(tezos) {
                    DUG { arg(1UL) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUG { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication(
                "PUSH",
                args = listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("Unit")),
            ) to listOf(
                micheline(tezos) {
                    PUSH {
                        type { unit }
                        value { Unit }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    PUSH {
                        type { unit }
                        value { Unit }
                    }
                },
                michelineData(tezos) {
                    PUSH {
                        type { unit }
                        value { Unit }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    PUSH {
                        type { unit }
                        value { Unit }
                    }
                },
                michelineInstruction(tezos) {
                    PUSH {
                        type { unit }
                        value { Unit }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    PUSH {
                        type { unit }
                        value { Unit }
                    }
                },
            ),
            MichelinePrimitiveApplication("SOME") to listOf(
                micheline(michelsonToMichelineConverter) { SOME },
                micheline(michelsonToMichelineConverter) { SOME() },
                michelineData(michelsonToMichelineConverter) { SOME },
                michelineData(michelsonToMichelineConverter) { SOME() },
                michelineInstruction(michelsonToMichelineConverter) { SOME },
                michelineInstruction(michelsonToMichelineConverter) { SOME() },
            ),
            MichelinePrimitiveApplication(
                "NONE",
                args = listOf(MichelinePrimitiveApplication("UNIT"))
            ) to listOf(
                micheline(tezos) {
                    NONE {
                        arg { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    NONE {
                        arg { UNIT }
                    }
                },
                micheline(tezos) {
                    NONE {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    NONE {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineData(tezos) {
                    NONE {
                        arg { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    NONE {
                        arg { UNIT }
                    }
                },
                michelineData(tezos) {
                    NONE {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    NONE {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    NONE {
                        arg { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    NONE {
                        arg { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    NONE {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    NONE {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication("UNIT") to listOf(
                micheline(michelsonToMichelineConverter) { UNIT },
                micheline(michelsonToMichelineConverter) { UNIT() },
                michelineData(michelsonToMichelineConverter) { UNIT },
                michelineData(michelsonToMichelineConverter) { UNIT() },
                michelineInstruction(michelsonToMichelineConverter) { UNIT },
                michelineInstruction(michelsonToMichelineConverter) { UNIT() },
            ),
            MichelinePrimitiveApplication("NEVER") to listOf(
                micheline(michelsonToMichelineConverter) { NEVER },
                micheline(michelsonToMichelineConverter) { NEVER() },
                michelineData(michelsonToMichelineConverter) { NEVER },
                michelineData(michelsonToMichelineConverter) { NEVER() },
                michelineInstruction(michelsonToMichelineConverter) { NEVER },
                michelineInstruction(michelsonToMichelineConverter) { NEVER() },
            ),
            MichelinePrimitiveApplication(
                "IF_NONE",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")), MichelineSequence(MichelinePrimitiveApplication("UNIT")))
            ) to listOf(
                micheline(tezos) {
                    IF_NONE {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    IF_NONE {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineData(tezos) {
                    IF_NONE {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    IF_NONE {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    IF_NONE {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    IF_NONE {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "IF_NONE",
                args = listOf(
                    MichelineSequence(MichelinePrimitiveApplication("UNIT"), MichelinePrimitiveApplication("UNIT")),
                    MichelineSequence(MichelinePrimitiveApplication("UNIT"))
                ),
            ) to listOf(
                micheline(tezos) {
                    IF_NONE {
                        ifBranch { UNIT; UNIT }
                        elseBranch { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    IF_NONE {
                        ifBranch { UNIT; UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineData(tezos) {
                    IF_NONE {
                        ifBranch { UNIT; UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    IF_NONE {
                        ifBranch { UNIT; UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    IF_NONE {
                        ifBranch { UNIT; UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    IF_NONE {
                        ifBranch { UNIT; UNIT }
                        elseBranch { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication("PAIR") to listOf(
                micheline(michelsonToMichelineConverter) { PAIR },
                micheline(michelsonToMichelineConverter) { PAIR() },
                michelineData(michelsonToMichelineConverter) { PAIR },
                michelineData(michelsonToMichelineConverter) { PAIR() },
                michelineInstruction(michelsonToMichelineConverter) { PAIR },
                michelineInstruction(michelsonToMichelineConverter) { PAIR() },
            ),
            MichelinePrimitiveApplication(
                "PAIR",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline(michelsonToMichelineConverter) { PAIR("1") },
                micheline(michelsonToMichelineConverter) { PAIR((1).toUByte()) },
                micheline(michelsonToMichelineConverter) { PAIR((1).toUShort()) },
                micheline(michelsonToMichelineConverter) { PAIR(1U) },
                micheline(michelsonToMichelineConverter) { PAIR(1UL) },
                micheline(tezos) {
                    PAIR { arg("1") }
                },
                micheline(michelsonToMichelineConverter) {
                    PAIR { arg("1") }
                },
                micheline(tezos) {
                    PAIR { arg((1).toUByte()) }
                },
                micheline(michelsonToMichelineConverter) {
                    PAIR { arg((1).toUByte()) }
                },
                micheline(tezos) {
                    PAIR { arg((1).toUShort()) }
                },
                micheline(michelsonToMichelineConverter) {
                    PAIR { arg((1).toUShort()) }
                },
                micheline(tezos) {
                    PAIR { arg(1U) }
                },
                micheline(michelsonToMichelineConverter) {
                    PAIR { arg(1U) }
                },
                micheline(tezos) {
                    PAIR { arg(1UL) }
                },
                micheline(michelsonToMichelineConverter) {
                    PAIR { arg(1UL) }
                },
                michelineData(michelsonToMichelineConverter) { PAIR("1") },
                michelineData(michelsonToMichelineConverter) { PAIR((1).toUByte()) },
                michelineData(michelsonToMichelineConverter) { PAIR((1).toUShort()) },
                michelineData(michelsonToMichelineConverter) { PAIR(1U) },
                michelineData(michelsonToMichelineConverter) { PAIR(1UL) },
                michelineData(tezos) {
                    PAIR { arg("1") }
                },
                michelineData(michelsonToMichelineConverter) {
                    PAIR { arg("1") }
                },
                michelineData(tezos) {
                    PAIR { arg((1).toUByte()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    PAIR { arg((1).toUByte()) }
                },
                michelineData(tezos) {
                    PAIR { arg((1).toUShort()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    PAIR { arg((1).toUShort()) }
                },
                michelineData(tezos) {
                    PAIR { arg(1U) }
                },
                michelineData(michelsonToMichelineConverter) {
                    PAIR { arg(1U) }
                },
                michelineData(tezos) {
                    PAIR { arg(1UL) }
                },
                michelineData(michelsonToMichelineConverter) {
                    PAIR { arg(1UL) }
                },
                michelineInstruction(michelsonToMichelineConverter) { PAIR("1") },
                michelineInstruction(michelsonToMichelineConverter) { PAIR((1).toUByte()) },
                michelineInstruction(michelsonToMichelineConverter) { PAIR((1).toUShort()) },
                michelineInstruction(michelsonToMichelineConverter) { PAIR(1U) },
                michelineInstruction(michelsonToMichelineConverter) { PAIR(1UL) },
                michelineInstruction(tezos) {
                    PAIR { arg("1") }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    PAIR { arg("1") }
                },
                michelineInstruction(tezos) {
                    PAIR { arg((1).toUByte()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    PAIR { arg((1).toUByte()) }
                },
                michelineInstruction(tezos) {
                    PAIR { arg((1).toUShort()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    PAIR { arg((1).toUShort()) }
                },
                michelineInstruction(tezos) {
                    PAIR { arg(1U) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    PAIR { arg(1U) }
                },
                michelineInstruction(tezos) {
                    PAIR { arg(1UL) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    PAIR { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication("CAR") to listOf(
                micheline(michelsonToMichelineConverter) { CAR },
                micheline(michelsonToMichelineConverter) { CAR() },
                michelineData(michelsonToMichelineConverter) { CAR },
                michelineData(michelsonToMichelineConverter) { CAR() },
                michelineInstruction(michelsonToMichelineConverter) { CAR },
                michelineInstruction(michelsonToMichelineConverter) { CAR() },
            ),
            MichelinePrimitiveApplication("CDR") to listOf(
                micheline(michelsonToMichelineConverter) { CDR },
                micheline(michelsonToMichelineConverter) { CDR() },
                michelineData(michelsonToMichelineConverter) { CDR },
                michelineData(michelsonToMichelineConverter) { CDR() },
                michelineInstruction(michelsonToMichelineConverter) { CDR },
                michelineInstruction(michelsonToMichelineConverter) { CDR() },
            ),
            MichelinePrimitiveApplication("UNPAIR") to listOf(
                micheline(michelsonToMichelineConverter) { UNPAIR },
                micheline(michelsonToMichelineConverter) { UNPAIR() },
                michelineData(michelsonToMichelineConverter) { UNPAIR },
                michelineData(michelsonToMichelineConverter) { UNPAIR() },
                michelineInstruction(michelsonToMichelineConverter) { UNPAIR },
                michelineInstruction(michelsonToMichelineConverter) { UNPAIR() },
            ),
            MichelinePrimitiveApplication(
                "UNPAIR",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline(michelsonToMichelineConverter) { UNPAIR("1") },
                micheline(michelsonToMichelineConverter) { UNPAIR((1).toUByte()) },
                micheline(michelsonToMichelineConverter) { UNPAIR((1).toUShort()) },
                micheline(michelsonToMichelineConverter) { UNPAIR(1U) },
                micheline(michelsonToMichelineConverter) { UNPAIR(1UL) },
                micheline(tezos) {
                    UNPAIR { arg("1") }
                },
                micheline(michelsonToMichelineConverter) {
                    UNPAIR { arg("1") }
                },
                micheline(tezos) {
                    UNPAIR { arg((1).toUByte()) }
                },
                micheline(michelsonToMichelineConverter) {
                    UNPAIR { arg((1).toUByte()) }
                },
                micheline(tezos) {
                    UNPAIR { arg((1).toUShort()) }
                },
                micheline(michelsonToMichelineConverter) {
                    UNPAIR { arg((1).toUShort()) }
                },
                micheline(tezos) {
                    UNPAIR { arg(1U) }
                },
                micheline(michelsonToMichelineConverter) {
                    UNPAIR { arg(1U) }
                },
                micheline(tezos) {
                    UNPAIR { arg(1UL) }
                },
                micheline(michelsonToMichelineConverter) {
                    UNPAIR { arg(1UL) }
                },
                michelineData(michelsonToMichelineConverter) { UNPAIR("1") },
                michelineData(michelsonToMichelineConverter) { UNPAIR((1).toUByte()) },
                michelineData(michelsonToMichelineConverter) { UNPAIR((1).toUShort()) },
                michelineData(michelsonToMichelineConverter) { UNPAIR(1U) },
                michelineData(michelsonToMichelineConverter) { UNPAIR(1UL) },
                michelineData(tezos) {
                    UNPAIR { arg("1") }
                },
                michelineData(michelsonToMichelineConverter) {
                    UNPAIR { arg("1") }
                },
                michelineData(tezos) {
                    UNPAIR { arg((1).toUByte()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    UNPAIR { arg((1).toUByte()) }
                },
                michelineData(tezos) {
                    UNPAIR { arg((1).toUShort()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    UNPAIR { arg((1).toUShort()) }
                },
                michelineData(tezos) {
                    UNPAIR { arg(1U) }
                },
                michelineData(michelsonToMichelineConverter) {
                    UNPAIR { arg(1U) }
                },
                michelineData(tezos) {
                    UNPAIR { arg(1UL) }
                },
                michelineData(michelsonToMichelineConverter) {
                    UNPAIR { arg(1UL) }
                },
                michelineInstruction(michelsonToMichelineConverter) { UNPAIR("1") },
                michelineInstruction(michelsonToMichelineConverter) { UNPAIR((1).toUByte()) },
                michelineInstruction(michelsonToMichelineConverter) { UNPAIR((1).toUShort()) },
                michelineInstruction(michelsonToMichelineConverter) { UNPAIR(1U) },
                michelineInstruction(michelsonToMichelineConverter) { UNPAIR(1UL) },
                michelineInstruction(tezos) {
                    UNPAIR { arg("1") }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UNPAIR { arg("1") }
                },
                michelineInstruction(tezos) {
                    UNPAIR { arg((1).toUByte()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UNPAIR { arg((1).toUByte()) }
                },
                michelineInstruction(tezos) {
                    UNPAIR { arg((1).toUShort()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UNPAIR { arg((1).toUShort()) }
                },
                michelineInstruction(tezos) {
                    UNPAIR { arg(1U) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UNPAIR { arg(1U) }
                },
                michelineInstruction(tezos) {
                    UNPAIR { arg(1UL) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UNPAIR { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication(
                "LEFT",
                args = listOf(MichelinePrimitiveApplication("UNIT"))
            ) to listOf(
                micheline(tezos) {
                    LEFT {
                        arg { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    LEFT {
                        arg { UNIT }
                    }
                },
                micheline(tezos) {
                    LEFT {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    LEFT {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineData(tezos) {
                    LEFT {
                        arg { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    LEFT {
                        arg { UNIT }
                    }
                },
                michelineData(tezos) {
                    LEFT {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    LEFT {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    LEFT {
                        arg { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    LEFT {
                        arg { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    LEFT {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    LEFT {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "RIGHT",
                args = listOf(MichelinePrimitiveApplication("UNIT"))
            ) to listOf(
                micheline(tezos) {
                    RIGHT {
                        arg { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    RIGHT {
                        arg { UNIT }
                    }
                },
                micheline(tezos) {
                    RIGHT {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    RIGHT {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineData(tezos) {
                    RIGHT {
                        arg { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    RIGHT {
                        arg { UNIT }
                    }
                },
                michelineData(tezos) {
                    RIGHT {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    RIGHT {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    RIGHT {
                        arg { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    RIGHT {
                        arg { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    RIGHT {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    RIGHT {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "IF_LEFT",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")), MichelineSequence(MichelinePrimitiveApplication("UNIT")))
            ) to listOf(
                micheline(tezos) {
                    IF_LEFT {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    IF_LEFT {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineData(tezos) {
                    IF_LEFT {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    IF_LEFT {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    IF_LEFT {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    IF_LEFT {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "NIL",
                args = listOf(MichelinePrimitiveApplication("UNIT"))
            ) to listOf(
                micheline(tezos) {
                    NIL {
                        arg { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    NIL {
                        arg { UNIT }
                    }
                },
                micheline(tezos) {
                    NIL {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    NIL {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineData(tezos) {
                    NIL {
                        arg { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    NIL {
                        arg { UNIT }
                    }
                },
                michelineData(tezos) {
                    NIL {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    NIL {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    NIL {
                        arg { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    NIL {
                        arg { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    NIL {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    NIL {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication("CONS") to listOf(
                micheline(tezos) { CONS },
                micheline(michelsonToMichelineConverter) { CONS },
                micheline(tezos) { CONS() },
                micheline(michelsonToMichelineConverter) { CONS() },
                michelineData(tezos) { CONS },
                michelineData(michelsonToMichelineConverter) { CONS },
                michelineData(tezos) { CONS() },
                michelineData(michelsonToMichelineConverter) { CONS() },
                michelineInstruction(tezos) { CONS },
                michelineInstruction(michelsonToMichelineConverter) { CONS },
                michelineInstruction(tezos) { CONS() },
                michelineInstruction(michelsonToMichelineConverter) { CONS() },
            ),
            MichelinePrimitiveApplication(
                "IF_CONS",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")), MichelineSequence(MichelinePrimitiveApplication("UNIT")))
            ) to listOf(
                micheline(tezos) {
                    IF_CONS {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    IF_CONS {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineData(tezos) {
                    IF_CONS {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    IF_CONS {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    IF_CONS {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    IF_CONS {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication("SIZE") to listOf(
                micheline(tezos) { SIZE },
                micheline(michelsonToMichelineConverter) { SIZE },
                micheline(tezos) { SIZE() },
                micheline(michelsonToMichelineConverter) { SIZE() },
                michelineData(tezos) { SIZE },
                michelineData(michelsonToMichelineConverter) { SIZE },
                michelineData(tezos) { SIZE() },
                michelineData(michelsonToMichelineConverter) { SIZE() },
                michelineInstruction(tezos) { SIZE },
                michelineInstruction(michelsonToMichelineConverter) { SIZE },
                michelineInstruction(tezos) { SIZE() },
                michelineInstruction(michelsonToMichelineConverter) { SIZE() },
            ),
            MichelinePrimitiveApplication(
                "EMPTY_SET",
                args = listOf(MichelinePrimitiveApplication("UNIT"))
            ) to listOf(
                micheline(tezos) {
                    EMPTY_SET {
                        arg { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    EMPTY_SET {
                        arg { UNIT }
                    }
                },
                micheline(tezos) {
                    EMPTY_SET {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    EMPTY_SET {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineData(tezos) {
                    EMPTY_SET {
                        arg { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    EMPTY_SET {
                        arg { UNIT }
                    }
                },
                michelineData(tezos) {
                    EMPTY_SET {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    EMPTY_SET {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    EMPTY_SET {
                        arg { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    EMPTY_SET {
                        arg { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    EMPTY_SET {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    EMPTY_SET {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "EMPTY_MAP",
                args = listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("bool"))
            ) to listOf(
                micheline(tezos) {
                    EMPTY_MAP {
                        key { unit }
                        value { bool }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    EMPTY_MAP {
                        key { unit }
                        value { bool }
                    }
                },
                michelineData(tezos) {
                    EMPTY_MAP {
                        key { unit }
                        value { bool }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    EMPTY_MAP {
                        key { unit }
                        value { bool }
                    }
                },
                michelineInstruction(tezos) {
                    EMPTY_MAP {
                        key { unit }
                        value { bool }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    EMPTY_MAP {
                        key { unit }
                        value { bool }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "EMPTY_BIG_MAP",
                args = listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("bool"))
            ) to listOf(
                micheline(tezos) {
                    EMPTY_BIG_MAP {
                        key { unit }
                        value { bool }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    EMPTY_BIG_MAP {
                        key { unit }
                        value { bool }
                    }
                },
                michelineData(tezos) {
                    EMPTY_BIG_MAP {
                        key { unit }
                        value { bool }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    EMPTY_BIG_MAP {
                        key { unit }
                        value { bool }
                    }
                },
                michelineInstruction(tezos) {
                    EMPTY_BIG_MAP {
                        key { unit }
                        value { bool }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    EMPTY_BIG_MAP {
                        key { unit }
                        value { bool }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "MAP",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")))
            ) to listOf(
                micheline(tezos) {
                    MAP {
                        expression { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    MAP {
                        expression { UNIT }
                    }
                },
                micheline(tezos) {
                    MAP {
                        expression { UNIT }
                        expression { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    MAP {
                        expression { UNIT }
                        expression { UNIT }
                    }
                },
                michelineData(tezos) {
                    MAP {
                        expression { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    MAP {
                        expression { UNIT }
                    }
                },
                michelineData(tezos) {
                    MAP {
                        expression { UNIT }
                        expression { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    MAP {
                        expression { UNIT }
                        expression { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    MAP {
                        expression { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    MAP {
                        expression { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    MAP {
                        expression { UNIT }
                        expression { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    MAP {
                        expression { UNIT }
                        expression { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "ITER",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")))
            ) to listOf(
                micheline(tezos) {
                    ITER {
                        expression { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    ITER {
                        expression { UNIT }
                    }
                },
                micheline(tezos) {
                    ITER {
                        expression { UNIT }
                        expression { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    ITER {
                        expression { UNIT }
                        expression { UNIT }
                    }
                },
                michelineData(tezos) {
                    ITER {
                        expression { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    ITER {
                        expression { UNIT }
                    }
                },
                michelineData(tezos) {
                    ITER {
                        expression { UNIT }
                        expression { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    ITER {
                        expression { UNIT }
                        expression { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    ITER {
                        expression { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    ITER {
                        expression { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    ITER {
                        expression { UNIT }
                        expression { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    ITER {
                        expression { UNIT }
                        expression { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication("MEM") to listOf(
                micheline(tezos) { MEM },
                micheline(michelsonToMichelineConverter) { MEM },
                micheline(tezos) { MEM() },
                micheline(michelsonToMichelineConverter) { MEM() },
                michelineData(tezos) { MEM },
                michelineData(michelsonToMichelineConverter) { MEM },
                michelineData(tezos) { MEM() },
                michelineData(michelsonToMichelineConverter) { MEM() },
                michelineInstruction(tezos) { MEM },
                michelineInstruction(michelsonToMichelineConverter) { MEM },
                michelineInstruction(tezos) { MEM() },
                michelineInstruction(michelsonToMichelineConverter) { MEM() },
            ),
            MichelinePrimitiveApplication("GET") to listOf(
                micheline(tezos) { GET },
                micheline(michelsonToMichelineConverter) { GET },
                micheline(tezos) { GET() },
                micheline(michelsonToMichelineConverter) { GET() },
                michelineData(tezos) { GET },
                michelineData(michelsonToMichelineConverter) { GET },
                michelineData(tezos) { GET() },
                michelineData(michelsonToMichelineConverter) { GET() },
                michelineInstruction(tezos) { GET },
                michelineInstruction(michelsonToMichelineConverter) { GET },
                michelineInstruction(tezos) { GET() },
                michelineInstruction(michelsonToMichelineConverter) { GET() },
            ),
            MichelinePrimitiveApplication(
                "GET",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline(michelsonToMichelineConverter) { GET("1") },
                micheline(michelsonToMichelineConverter) { GET((1).toUByte()) },
                micheline(michelsonToMichelineConverter) { GET((1).toUShort()) },
                micheline(michelsonToMichelineConverter) { GET(1U) },
                micheline(michelsonToMichelineConverter) { GET(1UL) },
                micheline(tezos) {
                    GET { arg("1") }
                },
                micheline(michelsonToMichelineConverter) {
                    GET { arg("1") }
                },
                micheline(tezos) {
                    GET { arg((1).toUByte()) }
                },
                micheline(michelsonToMichelineConverter) {
                    GET { arg((1).toUByte()) }
                },
                micheline(tezos) {
                    GET { arg((1).toUShort()) }
                },
                micheline(michelsonToMichelineConverter) {
                    GET { arg((1).toUShort()) }
                },
                micheline(tezos) {
                    GET { arg(1U) }
                },
                micheline(michelsonToMichelineConverter) {
                    GET { arg(1U) }
                },
                micheline(tezos) {
                    GET { arg(1UL) }
                },
                micheline(michelsonToMichelineConverter) {
                    GET { arg(1UL) }
                },
                michelineData(tezos) { GET("1") },
                michelineData(michelsonToMichelineConverter) { GET("1") },
                michelineData(tezos) { GET((1).toUByte()) },
                michelineData(michelsonToMichelineConverter) { GET((1).toUByte()) },
                michelineData(tezos) { GET((1).toUShort()) },
                michelineData(michelsonToMichelineConverter) { GET((1).toUShort()) },
                michelineData(tezos) { GET(1U) },
                michelineData(michelsonToMichelineConverter) { GET(1U) },
                michelineData(tezos) { GET(1UL) },
                michelineData(michelsonToMichelineConverter) { GET(1UL) },
                michelineData(tezos) {
                    GET { arg("1") }
                },
                michelineData(michelsonToMichelineConverter) {
                    GET { arg("1") }
                },
                michelineData(tezos) {
                    GET { arg((1).toUByte()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    GET { arg((1).toUByte()) }
                },
                michelineData(tezos) {
                    GET { arg((1).toUShort()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    GET { arg((1).toUShort()) }
                },
                michelineData(tezos) {
                    GET { arg(1U) }
                },
                michelineData(michelsonToMichelineConverter) {
                    GET { arg(1U) }
                },
                michelineData(tezos) {
                    GET { arg(1UL) }
                },
                michelineData(michelsonToMichelineConverter) {
                    GET { arg(1UL) }
                },
                michelineInstruction(tezos) { GET("1") },
                michelineInstruction(michelsonToMichelineConverter) { GET("1") },
                michelineInstruction(tezos) { GET((1).toUByte()) },
                michelineInstruction(michelsonToMichelineConverter) { GET((1).toUByte()) },
                michelineInstruction(tezos) { GET((1).toUShort()) },
                michelineInstruction(michelsonToMichelineConverter) { GET((1).toUShort()) },
                michelineInstruction(tezos) { GET(1U) },
                michelineInstruction(michelsonToMichelineConverter) { GET(1U) },
                michelineInstruction(tezos) { GET(1UL) },
                michelineInstruction(michelsonToMichelineConverter) { GET(1UL) },
                michelineInstruction(tezos) {
                    GET { arg("1") }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    GET { arg("1") }
                },
                michelineInstruction(tezos) {
                    GET { arg((1).toUByte()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    GET { arg((1).toUByte()) }
                },
                michelineInstruction(tezos) {
                    GET { arg((1).toUShort()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    GET { arg((1).toUShort()) }
                },
                michelineInstruction(tezos) {
                    GET { arg(1U) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    GET { arg(1U) }
                },
                michelineInstruction(tezos) {
                    GET { arg(1UL) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    GET { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication("UPDATE") to listOf(
                micheline(tezos) { UPDATE },
                micheline(michelsonToMichelineConverter) { UPDATE },
                micheline(tezos) { UPDATE() },
                micheline(michelsonToMichelineConverter) { UPDATE() },
                michelineData(tezos) { UPDATE },
                michelineData(michelsonToMichelineConverter) { UPDATE },
                michelineData(tezos) { UPDATE() },
                michelineData(michelsonToMichelineConverter) { UPDATE() },
                michelineInstruction(tezos) { UPDATE },
                michelineInstruction(michelsonToMichelineConverter) { UPDATE },
                michelineInstruction(tezos) { UPDATE() },
                michelineInstruction(michelsonToMichelineConverter) { UPDATE() },
            ),
            MichelinePrimitiveApplication(
                "UPDATE",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline(tezos) { UPDATE("1") },
                micheline(michelsonToMichelineConverter) { UPDATE("1") },
                micheline(tezos) { UPDATE((1).toUByte()) },
                micheline(michelsonToMichelineConverter) { UPDATE((1).toUByte()) },
                micheline(tezos) { UPDATE((1).toUShort()) },
                micheline(michelsonToMichelineConverter) { UPDATE((1).toUShort()) },
                micheline(tezos) { UPDATE(1U) },
                micheline(michelsonToMichelineConverter) { UPDATE(1U) },
                micheline(tezos) { UPDATE(1UL) },
                micheline(michelsonToMichelineConverter) { UPDATE(1UL) },
                micheline(tezos) {
                    UPDATE { arg("1") }
                },
                micheline(michelsonToMichelineConverter) {
                    UPDATE { arg("1") }
                },
                micheline(tezos) {
                    UPDATE { arg((1).toUByte()) }
                },
                micheline(michelsonToMichelineConverter) {
                    UPDATE { arg((1).toUByte()) }
                },
                micheline(tezos) {
                    UPDATE { arg((1).toUShort()) }
                },
                micheline(michelsonToMichelineConverter) {
                    UPDATE { arg((1).toUShort()) }
                },
                micheline(tezos) {
                    UPDATE { arg(1U) }
                },
                micheline(michelsonToMichelineConverter) {
                    UPDATE { arg(1U) }
                },
                micheline(tezos) {
                    UPDATE { arg(1UL) }
                },
                micheline(michelsonToMichelineConverter) {
                    UPDATE { arg(1UL) }
                },
                michelineData(tezos) { UPDATE("1") },
                michelineData(michelsonToMichelineConverter) { UPDATE("1") },
                michelineData(tezos) { UPDATE((1).toUByte()) },
                michelineData(michelsonToMichelineConverter) { UPDATE((1).toUByte()) },
                michelineData(tezos) { UPDATE((1).toUShort()) },
                michelineData(michelsonToMichelineConverter) { UPDATE((1).toUShort()) },
                michelineData(tezos) { UPDATE(1U) },
                michelineData(michelsonToMichelineConverter) { UPDATE(1U) },
                michelineData(tezos) { UPDATE(1UL) },
                michelineData(michelsonToMichelineConverter) { UPDATE(1UL) },
                michelineData(tezos) {
                    UPDATE { arg("1") }
                },
                michelineData(michelsonToMichelineConverter) {
                    UPDATE { arg("1") }
                },
                michelineData(tezos) {
                    UPDATE { arg((1).toUByte()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    UPDATE { arg((1).toUByte()) }
                },
                michelineData(tezos) {
                    UPDATE { arg((1).toUShort()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    UPDATE { arg((1).toUShort()) }
                },
                michelineData(tezos) {
                    UPDATE { arg(1U) }
                },
                michelineData(michelsonToMichelineConverter) {
                    UPDATE { arg(1U) }
                },
                michelineData(tezos) {
                    UPDATE { arg(1UL) }
                },
                michelineData(michelsonToMichelineConverter) {
                    UPDATE { arg(1UL) }
                },
                michelineInstruction(tezos) { UPDATE("1") },
                michelineInstruction(michelsonToMichelineConverter) { UPDATE("1") },
                michelineInstruction(tezos) { UPDATE((1).toUByte()) },
                michelineInstruction(michelsonToMichelineConverter) { UPDATE((1).toUByte()) },
                michelineInstruction(tezos) { UPDATE((1).toUShort()) },
                michelineInstruction(michelsonToMichelineConverter) { UPDATE((1).toUShort()) },
                michelineInstruction(tezos) { UPDATE(1U) },
                michelineInstruction(michelsonToMichelineConverter) { UPDATE(1U) },
                michelineInstruction(tezos) { UPDATE(1UL) },
                michelineInstruction(michelsonToMichelineConverter) { UPDATE(1UL) },
                michelineInstruction(tezos) {
                    UPDATE { arg("1") }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UPDATE { arg("1") }
                },
                michelineInstruction(tezos) {
                    UPDATE { arg((1).toUByte()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UPDATE { arg((1).toUByte()) }
                },
                michelineInstruction(tezos) {
                    UPDATE { arg((1).toUShort()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UPDATE { arg((1).toUShort()) }
                },
                michelineInstruction(tezos) {
                    UPDATE { arg(1U) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UPDATE { arg(1U) }
                },
                michelineInstruction(tezos) {
                    UPDATE { arg(1UL) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UPDATE { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication("GET_AND_UPDATE") to listOf(
                micheline(tezos) { GET_AND_UPDATE },
                micheline(michelsonToMichelineConverter) { GET_AND_UPDATE },
                micheline(tezos) { GET_AND_UPDATE() },
                micheline(michelsonToMichelineConverter) { GET_AND_UPDATE() },
                michelineData(tezos) { GET_AND_UPDATE },
                michelineData(michelsonToMichelineConverter) { GET_AND_UPDATE },
                michelineData(tezos) { GET_AND_UPDATE() },
                michelineData(michelsonToMichelineConverter) { GET_AND_UPDATE() },
                michelineInstruction(tezos) { GET_AND_UPDATE },
                michelineInstruction(michelsonToMichelineConverter) { GET_AND_UPDATE },
                michelineInstruction(tezos) { GET_AND_UPDATE() },
                michelineInstruction(michelsonToMichelineConverter) { GET_AND_UPDATE() },
            ),
            MichelinePrimitiveApplication(
                "IF",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")), MichelineSequence(MichelinePrimitiveApplication("UNIT")))
            ) to listOf(
                micheline(tezos) {
                    IF {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    IF {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineData(tezos) {
                    IF {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    IF {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    IF {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    IF {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "LOOP",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")))
            ) to listOf(
                micheline(tezos) {
                    LOOP {
                        body { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    LOOP {
                        body { UNIT }
                    }
                },
                micheline(tezos) {
                    LOOP {
                        body { UNIT }
                        body { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    LOOP {
                        body { UNIT }
                        body { UNIT }
                    }
                },
                michelineData(tezos) {
                    LOOP {
                        body { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    LOOP {
                        body { UNIT }
                    }
                },
                michelineData(tezos) {
                    LOOP {
                        body { UNIT }
                        body { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    LOOP {
                        body { UNIT }
                        body { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    LOOP {
                        body { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    LOOP {
                        body { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    LOOP {
                        body { UNIT }
                        body { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    LOOP {
                        body { UNIT }
                        body { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "LOOP_LEFT",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")))
            ) to listOf(
                micheline(tezos) {
                    LOOP_LEFT {
                        body { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    LOOP_LEFT {
                        body { UNIT }
                    }
                },
                micheline(tezos) {
                    LOOP_LEFT {
                        body { UNIT }
                        body { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    LOOP_LEFT {
                        body { UNIT }
                        body { UNIT }
                    }
                },
                michelineData(tezos) {
                    LOOP_LEFT {
                        body { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    LOOP_LEFT {
                        body { UNIT }
                    }
                },
                michelineData(tezos) {
                    LOOP_LEFT {
                        body { UNIT }
                        body { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    LOOP_LEFT {
                        body { UNIT }
                        body { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    LOOP_LEFT {
                        body { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    LOOP_LEFT {
                        body { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    LOOP_LEFT {
                        body { UNIT }
                        body { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    LOOP_LEFT {
                        body { UNIT }
                        body { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "LAMBDA",
                args = listOf(
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit"),
                    MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                ),
            ) to listOf(
                micheline(tezos) {
                    LAMBDA {
                        parameter { unit }
                        returnType { unit }
                        body { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    LAMBDA {
                        parameter { unit }
                        returnType { unit }
                        body { UNIT }
                    }
                },
                micheline(tezos) {
                    LAMBDA {
                        parameter { unit }
                        returnType { unit }
                        body { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    LAMBDA {
                        parameter { unit }
                        returnType { unit }
                        body { UNIT }
                    }
                },
                micheline(tezos) {
                    LAMBDA {
                        parameter { unit }
                        returnType { unit }
                        body { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    LAMBDA {
                        parameter { unit }
                        returnType { unit }
                        body { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication("EXEC") to listOf(
                micheline(tezos) { EXEC },
                micheline(michelsonToMichelineConverter) { EXEC },
                micheline(tezos) { EXEC() },
                micheline(michelsonToMichelineConverter) { EXEC() },
                michelineData(tezos) { EXEC },
                michelineData(michelsonToMichelineConverter) { EXEC },
                michelineData(tezos) { EXEC() },
                michelineData(michelsonToMichelineConverter) { EXEC() },
                michelineInstruction(tezos) { EXEC },
                michelineInstruction(michelsonToMichelineConverter) { EXEC },
                michelineInstruction(tezos) { EXEC() },
                michelineInstruction(michelsonToMichelineConverter) { EXEC() },
            ),
            MichelinePrimitiveApplication("APPLY") to listOf(
                micheline(tezos) { APPLY },
                micheline(michelsonToMichelineConverter) { APPLY },
                micheline(tezos) { APPLY() },
                micheline(michelsonToMichelineConverter) { APPLY() },
                michelineData(tezos) { APPLY },
                michelineData(michelsonToMichelineConverter) { APPLY },
                michelineData(tezos) { APPLY() },
                michelineData(michelsonToMichelineConverter) { APPLY() },
                michelineInstruction(tezos) { APPLY },
                michelineInstruction(michelsonToMichelineConverter) { APPLY },
                michelineInstruction(tezos) { APPLY() },
                michelineInstruction(michelsonToMichelineConverter) { APPLY() },
            ),
            MichelinePrimitiveApplication(
                "DIP",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT"))),
            ) to listOf(
                micheline(tezos) {
                    DIP {
                        instruction { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    DIP {
                        instruction { UNIT }
                    }
                },
                michelineData(tezos) {
                    DIP {
                        instruction { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    DIP {
                        instruction { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    DIP {
                        instruction { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DIP {
                        instruction { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "DIP",
                args = listOf(MichelineLiteral.Integer(1), MichelineSequence(MichelinePrimitiveApplication("UNIT"))),
            ) to listOf(
                micheline(tezos) {
                    DIP {
                        n("1")
                        instruction { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    DIP {
                        n("1")
                        instruction { UNIT }
                    }
                },
                micheline(tezos) {
                    DIP {
                        n((1).toUByte())
                        instruction { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    DIP {
                        n((1).toUByte())
                        instruction { UNIT }
                    }
                },
                micheline(tezos) {
                    DIP {
                        n((1).toUShort())
                        instruction { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    DIP {
                        n((1).toUShort())
                        instruction { UNIT }
                    }
                },
                micheline(tezos) {
                    DIP {
                        n(1U)
                        instruction { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    DIP {
                        n(1U)
                        instruction { UNIT }
                    }
                },
                micheline(tezos) {
                    DIP {
                        n(1UL)
                        instruction { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    DIP {
                        n(1UL)
                        instruction { UNIT }
                    }
                },
                michelineData(tezos) {
                    DIP {
                        n("1")
                        instruction { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    DIP {
                        n("1")
                        instruction { UNIT }
                    }
                },
                michelineData(tezos) {
                    DIP {
                        n((1).toUByte())
                        instruction { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    DIP {
                        n((1).toUByte())
                        instruction { UNIT }
                    }
                },
                michelineData(tezos) {
                    DIP {
                        n((1).toUShort())
                        instruction { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    DIP {
                        n((1).toUShort())
                        instruction { UNIT }
                    }
                },
                michelineData(tezos) {
                    DIP {
                        n(1U)
                        instruction { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    DIP {
                        n(1U)
                        instruction { UNIT }
                    }
                },
                michelineData(tezos) {
                    DIP {
                        n(1UL)
                        instruction { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    DIP {
                        n(1UL)
                        instruction { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    DIP {
                        n("1")
                        instruction { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DIP {
                        n("1")
                        instruction { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    DIP {
                        n((1).toUByte())
                        instruction { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DIP {
                        n((1).toUByte())
                        instruction { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    DIP {
                        n((1).toUShort())
                        instruction { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DIP {
                        n((1).toUShort())
                        instruction { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    DIP {
                        n(1U)
                        instruction { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DIP {
                        n(1U)
                        instruction { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    DIP {
                        n(1UL)
                        instruction { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DIP {
                        n(1UL)
                        instruction { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication("FAILWITH") to listOf(
                micheline(tezos) { FAILWITH },
                micheline(michelsonToMichelineConverter) { FAILWITH },
                micheline(tezos) { FAILWITH() },
                micheline(michelsonToMichelineConverter) { FAILWITH() },
                michelineData(tezos) { FAILWITH },
                michelineData(michelsonToMichelineConverter) { FAILWITH },
                michelineData(tezos) { FAILWITH() },
                michelineData(michelsonToMichelineConverter) { FAILWITH() },
                michelineInstruction(tezos) { FAILWITH },
                michelineInstruction(michelsonToMichelineConverter) { FAILWITH },
                michelineInstruction(tezos) { FAILWITH() },
                michelineInstruction(michelsonToMichelineConverter) { FAILWITH() },
            ),
            MichelinePrimitiveApplication("CAST") to listOf(
                micheline(tezos) { CAST },
                micheline(michelsonToMichelineConverter) { CAST },
                micheline(tezos) { CAST() },
                micheline(michelsonToMichelineConverter) { CAST() },
                michelineData(tezos) { CAST },
                michelineData(michelsonToMichelineConverter) { CAST },
                michelineData(tezos) { CAST() },
                michelineData(michelsonToMichelineConverter) { CAST() },
                michelineInstruction(tezos) { CAST },
                michelineInstruction(michelsonToMichelineConverter) { CAST },
                michelineInstruction(tezos) { CAST() },
                michelineInstruction(michelsonToMichelineConverter) { CAST() },
            ),
            MichelinePrimitiveApplication("RENAME") to listOf(
                micheline(tezos) { RENAME },
                micheline(michelsonToMichelineConverter) { RENAME },
                micheline(tezos) { RENAME() },
                micheline(michelsonToMichelineConverter) { RENAME() },
                michelineData(tezos) { RENAME },
                michelineData(michelsonToMichelineConverter) { RENAME },
                michelineData(tezos) { RENAME() },
                michelineData(michelsonToMichelineConverter) { RENAME() },
                michelineInstruction(tezos) { RENAME },
                michelineInstruction(michelsonToMichelineConverter) { RENAME },
                michelineInstruction(tezos) { RENAME() },
                michelineInstruction(michelsonToMichelineConverter) { RENAME() },
            ),
            MichelinePrimitiveApplication("CONCAT") to listOf(
                micheline(tezos) { CONCAT },
                micheline(michelsonToMichelineConverter) { CONCAT },
                micheline(tezos) { CONCAT() },
                micheline(michelsonToMichelineConverter) { CONCAT() },
                michelineData(tezos) { CONCAT },
                michelineData(michelsonToMichelineConverter) { CONCAT },
                michelineData(tezos) { CONCAT() },
                michelineData(michelsonToMichelineConverter) { CONCAT() },
                michelineInstruction(tezos) { CONCAT },
                michelineInstruction(michelsonToMichelineConverter) { CONCAT },
                michelineInstruction(tezos) { CONCAT() },
                michelineInstruction(michelsonToMichelineConverter) { CONCAT() },
            ),
            MichelinePrimitiveApplication("SLICE") to listOf(
                micheline(tezos) { SLICE },
                micheline(michelsonToMichelineConverter) { SLICE },
                micheline(tezos) { SLICE() },
                micheline(michelsonToMichelineConverter) { SLICE() },
                michelineData(tezos) { SLICE },
                michelineData(michelsonToMichelineConverter) { SLICE },
                michelineData(tezos) { SLICE() },
                michelineData(michelsonToMichelineConverter) { SLICE() },
                michelineInstruction(tezos) { SLICE },
                michelineInstruction(michelsonToMichelineConverter) { SLICE },
                michelineInstruction(tezos) { SLICE() },
                michelineInstruction(michelsonToMichelineConverter) { SLICE() },
            ),
            MichelinePrimitiveApplication("PACK") to listOf(
                micheline(tezos) { PACK },
                micheline(michelsonToMichelineConverter) { PACK },
                micheline(tezos) { PACK() },
                micheline(michelsonToMichelineConverter) { PACK() },
                michelineData(tezos) { PACK },
                michelineData(michelsonToMichelineConverter) { PACK },
                michelineData(tezos) { PACK() },
                michelineData(michelsonToMichelineConverter) { PACK() },
                michelineInstruction(tezos) { PACK },
                michelineInstruction(michelsonToMichelineConverter) { PACK },
                michelineInstruction(tezos) { PACK() },
                michelineInstruction(michelsonToMichelineConverter) { PACK() },
            ),
            MichelinePrimitiveApplication(
                "UNPACK",
                args = listOf(MichelinePrimitiveApplication("unit"))
            ) to listOf(
                micheline(tezos) {
                    UNPACK {
                        arg { unit }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    UNPACK {
                        arg { unit }
                    }
                },
                micheline(tezos) {
                    UNPACK {
                        arg { unit }
                        arg { unit }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    UNPACK {
                        arg { unit }
                        arg { unit }
                    }
                },
                michelineData(tezos) {
                    UNPACK {
                        arg { unit }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    UNPACK {
                        arg { unit }
                    }
                },
                michelineData(tezos) {
                    UNPACK {
                        arg { unit }
                        arg { unit }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    UNPACK {
                        arg { unit }
                        arg { unit }
                    }
                },
                michelineInstruction(tezos) {
                    UNPACK {
                        arg { unit }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UNPACK {
                        arg { unit }
                    }
                },
                michelineInstruction(tezos) {
                    UNPACK {
                        arg { unit }
                        arg { unit }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UNPACK {
                        arg { unit }
                        arg { unit }
                    }
                },
            ),
            MichelinePrimitiveApplication("ADD") to listOf(
                micheline(tezos) { ADD },
                micheline(michelsonToMichelineConverter) { ADD },
                micheline(tezos) { ADD() },
                micheline(michelsonToMichelineConverter) { ADD() },
                michelineData(tezos) { ADD },
                michelineData(michelsonToMichelineConverter) { ADD },
                michelineData(tezos) { ADD() },
                michelineData(michelsonToMichelineConverter) { ADD() },
                michelineInstruction(tezos) { ADD },
                michelineInstruction(michelsonToMichelineConverter) { ADD },
                michelineInstruction(tezos) { ADD() },
                michelineInstruction(michelsonToMichelineConverter) { ADD() },
            ),
            MichelinePrimitiveApplication("SUB") to listOf(
                micheline(tezos) { SUB },
                micheline(michelsonToMichelineConverter) { SUB },
                micheline(tezos) { SUB() },
                micheline(michelsonToMichelineConverter) { SUB() },
                michelineData(tezos) { SUB },
                michelineData(michelsonToMichelineConverter) { SUB },
                michelineData(tezos) { SUB() },
                michelineData(michelsonToMichelineConverter) { SUB() },
                michelineInstruction(tezos) { SUB },
                michelineInstruction(michelsonToMichelineConverter) { SUB },
                michelineInstruction(tezos) { SUB() },
                michelineInstruction(michelsonToMichelineConverter) { SUB() },
            ),
            MichelinePrimitiveApplication("MUL") to listOf(
                micheline(tezos) { MUL },
                micheline(michelsonToMichelineConverter) { MUL },
                micheline(tezos) { MUL() },
                micheline(michelsonToMichelineConverter) { MUL() },
                michelineData(tezos) { MUL },
                michelineData(michelsonToMichelineConverter) { MUL },
                michelineData(tezos) { MUL() },
                michelineData(michelsonToMichelineConverter) { MUL() },
                michelineInstruction(tezos) { MUL },
                michelineInstruction(michelsonToMichelineConverter) { MUL },
                michelineInstruction(tezos) { MUL() },
                michelineInstruction(michelsonToMichelineConverter) { MUL() },
            ),
            MichelinePrimitiveApplication("EDIV") to listOf(
                micheline(tezos) { EDIV },
                micheline(michelsonToMichelineConverter) { EDIV },
                micheline(tezos) { EDIV() },
                micheline(michelsonToMichelineConverter) { EDIV() },
                michelineData(tezos) { EDIV },
                michelineData(michelsonToMichelineConverter) { EDIV },
                michelineData(tezos) { EDIV() },
                michelineData(michelsonToMichelineConverter) { EDIV() },
                michelineInstruction(tezos) { EDIV },
                michelineInstruction(michelsonToMichelineConverter) { EDIV },
                michelineInstruction(tezos) { EDIV() },
                michelineInstruction(michelsonToMichelineConverter) { EDIV() },
            ),
            MichelinePrimitiveApplication("ABS") to listOf(
                micheline(tezos) { ABS },
                micheline(michelsonToMichelineConverter) { ABS },
                micheline(tezos) { ABS() },
                micheline(michelsonToMichelineConverter) { ABS() },
                michelineData(tezos) { ABS },
                michelineData(michelsonToMichelineConverter) { ABS },
                michelineData(tezos) { ABS() },
                michelineData(michelsonToMichelineConverter) { ABS() },
                michelineInstruction(tezos) { ABS },
                michelineInstruction(michelsonToMichelineConverter) { ABS },
                michelineInstruction(tezos) { ABS() },
                michelineInstruction(michelsonToMichelineConverter) { ABS() },
            ),
            MichelinePrimitiveApplication("ISNAT") to listOf(
                micheline(tezos) { ISNAT },
                micheline(michelsonToMichelineConverter) { ISNAT },
                micheline(tezos) { ISNAT() },
                micheline(michelsonToMichelineConverter) { ISNAT() },
                michelineData(tezos) { ISNAT },
                michelineData(michelsonToMichelineConverter) { ISNAT },
                michelineData(tezos) { ISNAT() },
                michelineData(michelsonToMichelineConverter) { ISNAT() },
                michelineInstruction(tezos) { ISNAT },
                michelineInstruction(michelsonToMichelineConverter) { ISNAT },
                michelineInstruction(tezos) { ISNAT() },
                michelineInstruction(michelsonToMichelineConverter) { ISNAT() },
            ),
            MichelinePrimitiveApplication("INT") to listOf(
                micheline(tezos) { INT },
                micheline(michelsonToMichelineConverter) { INT },
                micheline(tezos) { INT() },
                micheline(michelsonToMichelineConverter) { INT() },
                michelineData(tezos) { INT },
                michelineData(michelsonToMichelineConverter) { INT },
                michelineData(tezos) { INT() },
                michelineData(michelsonToMichelineConverter) { INT() },
                michelineInstruction(tezos) { INT },
                michelineInstruction(michelsonToMichelineConverter) { INT },
                michelineInstruction(tezos) { INT() },
                michelineInstruction(michelsonToMichelineConverter) { INT() },
            ),
            MichelinePrimitiveApplication("NEG") to listOf(
                micheline(tezos) { NEG },
                micheline(michelsonToMichelineConverter) { NEG },
                micheline(tezos) { NEG() },
                micheline(michelsonToMichelineConverter) { NEG() },
                michelineData(tezos) { NEG },
                michelineData(michelsonToMichelineConverter) { NEG },
                michelineData(tezos) { NEG() },
                michelineData(michelsonToMichelineConverter) { NEG() },
                michelineInstruction(tezos) { NEG },
                michelineInstruction(michelsonToMichelineConverter) { NEG },
                michelineInstruction(tezos) { NEG() },
                michelineInstruction(michelsonToMichelineConverter) { NEG() },
            ),
            MichelinePrimitiveApplication("LSL") to listOf(
                micheline(tezos) { LSL },
                micheline(michelsonToMichelineConverter) { LSL },
                micheline(tezos) { LSL() },
                micheline(michelsonToMichelineConverter) { LSL() },
                michelineData(tezos) { LSL },
                michelineData(michelsonToMichelineConverter) { LSL },
                michelineData(tezos) { LSL() },
                michelineData(michelsonToMichelineConverter) { LSL() },
                michelineInstruction(tezos) { LSL },
                michelineInstruction(michelsonToMichelineConverter) { LSL },
                michelineInstruction(tezos) { LSL() },
                michelineInstruction(michelsonToMichelineConverter) { LSL() },
            ),
            MichelinePrimitiveApplication("LSR") to listOf(
                micheline(tezos) { LSR },
                micheline(michelsonToMichelineConverter) { LSR },
                micheline(tezos) { LSR() },
                micheline(michelsonToMichelineConverter) { LSR() },
                michelineData(tezos) { LSR },
                michelineData(michelsonToMichelineConverter) { LSR },
                michelineData(tezos) { LSR() },
                michelineData(michelsonToMichelineConverter) { LSR() },
                michelineInstruction(tezos) { LSR },
                michelineInstruction(michelsonToMichelineConverter) { LSR },
                michelineInstruction(tezos) { LSR() },
                michelineInstruction(michelsonToMichelineConverter) { LSR() },
            ),
            MichelinePrimitiveApplication("OR") to listOf(
                micheline(tezos) { OR },
                micheline(michelsonToMichelineConverter) { OR },
                micheline(tezos) { OR() },
                micheline(michelsonToMichelineConverter) { OR() },
                michelineData(tezos) { OR },
                michelineData(michelsonToMichelineConverter) { OR },
                michelineData(tezos) { OR() },
                michelineData(michelsonToMichelineConverter) { OR() },
                michelineInstruction(tezos) { OR },
                michelineInstruction(michelsonToMichelineConverter) { OR },
                michelineInstruction(tezos) { OR() },
                michelineInstruction(michelsonToMichelineConverter) { OR() },
            ),
            MichelinePrimitiveApplication("AND") to listOf(
                micheline(tezos) { AND },
                micheline(michelsonToMichelineConverter) { AND },
                micheline(tezos) { AND() },
                micheline(michelsonToMichelineConverter) { AND() },
                michelineData(tezos) { AND },
                michelineData(michelsonToMichelineConverter) { AND },
                michelineData(tezos) { AND() },
                michelineData(michelsonToMichelineConverter) { AND() },
                michelineInstruction(tezos) { AND },
                michelineInstruction(michelsonToMichelineConverter) { AND },
                michelineInstruction(tezos) { AND() },
                michelineInstruction(michelsonToMichelineConverter) { AND() },
            ),
            MichelinePrimitiveApplication("XOR") to listOf(
                micheline(tezos) { XOR },
                micheline(michelsonToMichelineConverter) { XOR },
                micheline(tezos) { XOR() },
                micheline(michelsonToMichelineConverter) { XOR() },
                michelineData(tezos) { XOR },
                michelineData(michelsonToMichelineConverter) { XOR },
                michelineData(tezos) { XOR() },
                michelineData(michelsonToMichelineConverter) { XOR() },
                michelineInstruction(tezos) { XOR },
                michelineInstruction(michelsonToMichelineConverter) { XOR },
                michelineInstruction(tezos) { XOR() },
                michelineInstruction(michelsonToMichelineConverter) { XOR() },
            ),
            MichelinePrimitiveApplication("NOT") to listOf(
                micheline(tezos) { NOT },
                micheline(michelsonToMichelineConverter) { NOT },
                micheline(tezos) { NOT() },
                micheline(michelsonToMichelineConverter) { NOT() },
                michelineData(tezos) { NOT },
                michelineData(michelsonToMichelineConverter) { NOT },
                michelineData(tezos) { NOT() },
                michelineData(michelsonToMichelineConverter) { NOT() },
                michelineInstruction(tezos) { NOT },
                michelineInstruction(michelsonToMichelineConverter) { NOT },
                michelineInstruction(tezos) { NOT() },
                michelineInstruction(michelsonToMichelineConverter) { NOT() },
            ),
            MichelinePrimitiveApplication("COMPARE") to listOf(
                micheline(tezos) { COMPARE },
                micheline(michelsonToMichelineConverter) { COMPARE },
                micheline(tezos) { COMPARE() },
                micheline(michelsonToMichelineConverter) { COMPARE() },
                michelineData(tezos) { COMPARE },
                michelineData(michelsonToMichelineConverter) { COMPARE },
                michelineData(tezos) { COMPARE() },
                michelineData(michelsonToMichelineConverter) { COMPARE() },
                michelineInstruction(tezos) { COMPARE },
                michelineInstruction(michelsonToMichelineConverter) { COMPARE },
                michelineInstruction(tezos) { COMPARE() },
                michelineInstruction(michelsonToMichelineConverter) { COMPARE() },
            ),
            MichelinePrimitiveApplication("EQ") to listOf(
                micheline(tezos) { EQ },
                micheline(michelsonToMichelineConverter) { EQ },
                micheline(tezos) { EQ() },
                micheline(michelsonToMichelineConverter) { EQ() },
                michelineData(tezos) { EQ },
                michelineData(michelsonToMichelineConverter) { EQ },
                michelineData(tezos) { EQ() },
                michelineData(michelsonToMichelineConverter) { EQ() },
                michelineInstruction(tezos) { EQ },
                michelineInstruction(michelsonToMichelineConverter) { EQ },
                michelineInstruction(tezos) { EQ() },
                michelineInstruction(michelsonToMichelineConverter) { EQ() },
            ),
            MichelinePrimitiveApplication("NEQ") to listOf(
                micheline(tezos) { NEQ },
                micheline(michelsonToMichelineConverter) { NEQ },
                micheline(tezos) { NEQ() },
                micheline(michelsonToMichelineConverter) { NEQ() },
                michelineData(tezos) { NEQ },
                michelineData(michelsonToMichelineConverter) { NEQ },
                michelineData(tezos) { NEQ() },
                michelineData(michelsonToMichelineConverter) { NEQ() },
                michelineInstruction(tezos) { NEQ },
                michelineInstruction(michelsonToMichelineConverter) { NEQ },
                michelineInstruction(tezos) { NEQ() },
                michelineInstruction(michelsonToMichelineConverter) { NEQ() },
            ),
            MichelinePrimitiveApplication("LT") to listOf(
                micheline(tezos) { LT },
                micheline(michelsonToMichelineConverter) { LT },
                micheline(tezos) { LT() },
                micheline(michelsonToMichelineConverter) { LT() },
                michelineData(tezos) { LT },
                michelineData(michelsonToMichelineConverter) { LT },
                michelineData(tezos) { LT() },
                michelineData(michelsonToMichelineConverter) { LT() },
                michelineInstruction(tezos) { LT },
                michelineInstruction(michelsonToMichelineConverter) { LT },
                michelineInstruction(tezos) { LT() },
                michelineInstruction(michelsonToMichelineConverter) { LT() },
            ),
            MichelinePrimitiveApplication("GT") to listOf(
                micheline(tezos) { GT },
                micheline(michelsonToMichelineConverter) { GT },
                micheline(tezos) { GT() },
                micheline(michelsonToMichelineConverter) { GT() },
                michelineData(tezos) { GT },
                michelineData(michelsonToMichelineConverter) { GT },
                michelineData(tezos) { GT() },
                michelineData(michelsonToMichelineConverter) { GT() },
                michelineInstruction(tezos) { GT },
                michelineInstruction(michelsonToMichelineConverter) { GT },
                michelineInstruction(tezos) { GT() },
                michelineInstruction(michelsonToMichelineConverter) { GT() },
            ),
            MichelinePrimitiveApplication("LE") to listOf(
                micheline(tezos) { LE },
                micheline(michelsonToMichelineConverter) { LE },
                micheline(tezos) { LE() },
                micheline(michelsonToMichelineConverter) { LE() },
                michelineData(tezos) { LE },
                michelineData(michelsonToMichelineConverter) { LE },
                michelineData(tezos) { LE() },
                michelineData(michelsonToMichelineConverter) { LE() },
                michelineInstruction(tezos) { LE },
                michelineInstruction(michelsonToMichelineConverter) { LE },
                michelineInstruction(tezos) { LE() },
                michelineInstruction(michelsonToMichelineConverter) { LE() },
            ),
            MichelinePrimitiveApplication("GE") to listOf(
                micheline(tezos) { GE },
                micheline(michelsonToMichelineConverter) { GE },
                micheline(tezos) { GE() },
                micheline(michelsonToMichelineConverter) { GE() },
                michelineData(tezos) { GE },
                michelineData(michelsonToMichelineConverter) { GE },
                michelineData(tezos) { GE() },
                michelineData(michelsonToMichelineConverter) { GE() },
                michelineInstruction(tezos) { GE },
                michelineInstruction(michelsonToMichelineConverter) { GE },
                michelineInstruction(tezos) { GE() },
                michelineInstruction(michelsonToMichelineConverter) { GE() },
            ),
            MichelinePrimitiveApplication("SELF") to listOf(
                micheline(tezos) { SELF },
                micheline(michelsonToMichelineConverter) { SELF },
                micheline(tezos) { SELF() },
                micheline(michelsonToMichelineConverter) { SELF() },
                michelineData(tezos) { SELF },
                michelineData(michelsonToMichelineConverter) { SELF },
                michelineData(tezos) { SELF() },
                michelineData(michelsonToMichelineConverter) { SELF() },
                michelineInstruction(tezos) { SELF },
                michelineInstruction(michelsonToMichelineConverter) { SELF },
                michelineInstruction(tezos) { SELF() },
                michelineInstruction(michelsonToMichelineConverter) { SELF() },
            ),
            MichelinePrimitiveApplication("SELF_ADDRESS") to listOf(
                micheline(tezos) { SELF_ADDRESS },
                micheline(michelsonToMichelineConverter) { SELF_ADDRESS },
                micheline(tezos) { SELF_ADDRESS() },
                micheline(michelsonToMichelineConverter) { SELF_ADDRESS() },
                michelineData(tezos) { SELF_ADDRESS },
                michelineData(michelsonToMichelineConverter) { SELF_ADDRESS },
                michelineData(tezos) { SELF_ADDRESS() },
                michelineData(michelsonToMichelineConverter) { SELF_ADDRESS() },
                michelineInstruction(tezos) { SELF_ADDRESS },
                michelineInstruction(michelsonToMichelineConverter) { SELF_ADDRESS },
                michelineInstruction(tezos) { SELF_ADDRESS() },
                michelineInstruction(michelsonToMichelineConverter) { SELF_ADDRESS() },
            ),
            MichelinePrimitiveApplication(
                "CONTRACT",
                args = listOf(MichelinePrimitiveApplication("unit"))
            ) to listOf(
                micheline(tezos) {
                    CONTRACT {
                        arg { unit }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    CONTRACT {
                        arg { unit }
                    }
                },
                micheline(tezos) {
                    CONTRACT {
                        arg { unit }
                        arg { unit }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    CONTRACT {
                        arg { unit }
                        arg { unit }
                    }
                },
                michelineData(tezos) {
                    CONTRACT {
                        arg { unit }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    CONTRACT {
                        arg { unit }
                    }
                },
                michelineData(tezos) {
                    CONTRACT {
                        arg { unit }
                        arg { unit }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    CONTRACT {
                        arg { unit }
                        arg { unit }
                    }
                },
                michelineInstruction(tezos) {
                    CONTRACT {
                        arg { unit }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    CONTRACT {
                        arg { unit }
                    }
                },
                michelineInstruction(tezos) {
                    CONTRACT {
                        arg { unit }
                        arg { unit }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    CONTRACT {
                        arg { unit }
                        arg { unit }
                    }
                },
            ),
            MichelinePrimitiveApplication("TRANSFER_TOKENS") to listOf(
                micheline(tezos) { TRANSFER_TOKENS },
                micheline(michelsonToMichelineConverter) { TRANSFER_TOKENS },
                micheline(tezos) { TRANSFER_TOKENS() },
                micheline(michelsonToMichelineConverter) { TRANSFER_TOKENS() },
                michelineData(tezos) { TRANSFER_TOKENS },
                michelineData(michelsonToMichelineConverter) { TRANSFER_TOKENS },
                michelineData(tezos) { TRANSFER_TOKENS() },
                michelineData(michelsonToMichelineConverter) { TRANSFER_TOKENS() },
                michelineInstruction(tezos) { TRANSFER_TOKENS },
                michelineInstruction(michelsonToMichelineConverter) { TRANSFER_TOKENS },
                michelineInstruction(tezos) { TRANSFER_TOKENS() },
                michelineInstruction(michelsonToMichelineConverter) { TRANSFER_TOKENS() },
            ),
            MichelinePrimitiveApplication("SET_DELEGATE") to listOf(
                micheline(tezos) { SET_DELEGATE },
                micheline(michelsonToMichelineConverter) { SET_DELEGATE },
                micheline(tezos) { SET_DELEGATE() },
                micheline(michelsonToMichelineConverter) { SET_DELEGATE() },
                michelineData(tezos) { SET_DELEGATE },
                michelineData(michelsonToMichelineConverter) { SET_DELEGATE },
                michelineData(tezos) { SET_DELEGATE() },
                michelineData(michelsonToMichelineConverter) { SET_DELEGATE() },
                michelineInstruction(tezos) { SET_DELEGATE },
                michelineInstruction(michelsonToMichelineConverter) { SET_DELEGATE },
                michelineInstruction(tezos) { SET_DELEGATE() },
                michelineInstruction(michelsonToMichelineConverter) { SET_DELEGATE() },
            ),
            MichelinePrimitiveApplication(
                "CREATE_CONTRACT",
                args = listOf(
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit"),
                    MichelineSequence(MichelinePrimitiveApplication("UNIT")),
                ),
            ) to listOf(
                micheline(tezos) {
                    CREATE_CONTRACT {
                        parameter { unit }
                        storage { unit }
                        code { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    CREATE_CONTRACT {
                        parameter { unit }
                        storage { unit }
                        code { UNIT }
                    }
                },
                michelineData(tezos) {
                    CREATE_CONTRACT {
                        parameter { unit }
                        storage { unit }
                        code { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    CREATE_CONTRACT {
                        parameter { unit }
                        storage { unit }
                        code { UNIT }
                    }
                },
                michelineInstruction(tezos) {
                    CREATE_CONTRACT {
                        parameter { unit }
                        storage { unit }
                        code { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    CREATE_CONTRACT {
                        parameter { unit }
                        storage { unit }
                        code { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication("IMPLICIT_ACCOUNT") to listOf(
                micheline(tezos) { IMPLICIT_ACCOUNT },
                micheline(michelsonToMichelineConverter) { IMPLICIT_ACCOUNT },
                micheline(tezos) { IMPLICIT_ACCOUNT() },
                micheline(michelsonToMichelineConverter) { IMPLICIT_ACCOUNT() },
                michelineData(tezos) { IMPLICIT_ACCOUNT },
                michelineData(michelsonToMichelineConverter) { IMPLICIT_ACCOUNT },
                michelineData(tezos) { IMPLICIT_ACCOUNT() },
                michelineData(michelsonToMichelineConverter) { IMPLICIT_ACCOUNT() },
                michelineInstruction(tezos) { IMPLICIT_ACCOUNT },
                michelineInstruction(michelsonToMichelineConverter) { IMPLICIT_ACCOUNT },
                michelineInstruction(tezos) { IMPLICIT_ACCOUNT() },
                michelineInstruction(michelsonToMichelineConverter) { IMPLICIT_ACCOUNT() },
            ),
            MichelinePrimitiveApplication("VOTING_POWER") to listOf(
                micheline(tezos) { VOTING_POWER },
                micheline(michelsonToMichelineConverter) { VOTING_POWER },
                micheline(tezos) { VOTING_POWER() },
                micheline(michelsonToMichelineConverter) { VOTING_POWER() },
                michelineData(tezos) { VOTING_POWER },
                michelineData(michelsonToMichelineConverter) { VOTING_POWER },
                michelineData(tezos) { VOTING_POWER() },
                michelineData(michelsonToMichelineConverter) { VOTING_POWER() },
                michelineInstruction(tezos) { VOTING_POWER },
                michelineInstruction(michelsonToMichelineConverter) { VOTING_POWER },
                michelineInstruction(tezos) { VOTING_POWER() },
                michelineInstruction(michelsonToMichelineConverter) { VOTING_POWER() },
            ),
            MichelinePrimitiveApplication("NOW") to listOf(
                micheline(tezos) { NOW },
                micheline(michelsonToMichelineConverter) { NOW },
                micheline(tezos) { NOW() },
                micheline(michelsonToMichelineConverter) { NOW() },
                michelineData(tezos) { NOW },
                michelineData(michelsonToMichelineConverter) { NOW },
                michelineData(tezos) { NOW() },
                michelineData(michelsonToMichelineConverter) { NOW() },
                michelineInstruction(tezos) { NOW },
                michelineInstruction(michelsonToMichelineConverter) { NOW },
                michelineInstruction(tezos) { NOW() },
                michelineInstruction(michelsonToMichelineConverter) { NOW() },
            ),
            MichelinePrimitiveApplication("LEVEL") to listOf(
                micheline(tezos) { LEVEL },
                micheline(michelsonToMichelineConverter) { LEVEL },
                micheline(tezos) { LEVEL() },
                micheline(michelsonToMichelineConverter) { LEVEL() },
                michelineData(tezos) { LEVEL },
                michelineData(michelsonToMichelineConverter) { LEVEL },
                michelineData(tezos) { LEVEL() },
                michelineData(michelsonToMichelineConverter) { LEVEL() },
                michelineInstruction(tezos) { LEVEL },
                michelineInstruction(michelsonToMichelineConverter) { LEVEL },
                michelineInstruction(tezos) { LEVEL() },
                michelineInstruction(michelsonToMichelineConverter) { LEVEL() },
            ),
            MichelinePrimitiveApplication("AMOUNT") to listOf(
                micheline(tezos) { AMOUNT },
                micheline(michelsonToMichelineConverter) { AMOUNT },
                micheline(tezos) { AMOUNT() },
                micheline(michelsonToMichelineConverter) { AMOUNT() },
                michelineData(tezos) { AMOUNT },
                michelineData(michelsonToMichelineConverter) { AMOUNT },
                michelineData(tezos) { AMOUNT() },
                michelineData(michelsonToMichelineConverter) { AMOUNT() },
                michelineInstruction(tezos) { AMOUNT },
                michelineInstruction(michelsonToMichelineConverter) { AMOUNT },
                michelineInstruction(tezos) { AMOUNT() },
                michelineInstruction(michelsonToMichelineConverter) { AMOUNT() },
            ),
            MichelinePrimitiveApplication("BALANCE") to listOf(
                micheline(tezos) { BALANCE },
                micheline(michelsonToMichelineConverter) { BALANCE },
                micheline(tezos) { BALANCE() },
                micheline(michelsonToMichelineConverter) { BALANCE() },
                michelineData(tezos) { BALANCE },
                michelineData(michelsonToMichelineConverter) { BALANCE },
                michelineData(tezos) { BALANCE() },
                michelineData(michelsonToMichelineConverter) { BALANCE() },
                michelineInstruction(tezos) { BALANCE },
                michelineInstruction(michelsonToMichelineConverter) { BALANCE },
                michelineInstruction(tezos) { BALANCE() },
                michelineInstruction(michelsonToMichelineConverter) { BALANCE() },
            ),
            MichelinePrimitiveApplication("CHECK_SIGNATURE") to listOf(
                micheline(tezos) { CHECK_SIGNATURE },
                micheline(michelsonToMichelineConverter) { CHECK_SIGNATURE },
                micheline(tezos) { CHECK_SIGNATURE() },
                micheline(michelsonToMichelineConverter) { CHECK_SIGNATURE() },
                michelineData(tezos) { CHECK_SIGNATURE },
                michelineData(michelsonToMichelineConverter) { CHECK_SIGNATURE },
                michelineData(tezos) { CHECK_SIGNATURE() },
                michelineData(michelsonToMichelineConverter) { CHECK_SIGNATURE() },
                michelineInstruction(tezos) { CHECK_SIGNATURE },
                michelineInstruction(michelsonToMichelineConverter) { CHECK_SIGNATURE },
                michelineInstruction(tezos) { CHECK_SIGNATURE() },
                michelineInstruction(michelsonToMichelineConverter) { CHECK_SIGNATURE() },
            ),
            MichelinePrimitiveApplication("BLAKE2B") to listOf(
                micheline(tezos) { BLAKE2B },
                micheline(tezos) { BLAKE2B },
                micheline(tezos) { BLAKE2B() },
                micheline(tezos) { BLAKE2B() },
                michelineData(tezos) { BLAKE2B },
                michelineData(tezos) { BLAKE2B },
                michelineData(tezos) { BLAKE2B() },
                michelineData(tezos) { BLAKE2B() },
                michelineInstruction(tezos) { BLAKE2B },
                michelineInstruction(tezos) { BLAKE2B },
                michelineInstruction(tezos) { BLAKE2B() },
                michelineInstruction(tezos) { BLAKE2B() },
            ),
            MichelinePrimitiveApplication("KECCAK") to listOf(
                micheline(tezos) { KECCAK },
                micheline(tezos) { KECCAK },
                micheline(tezos) { KECCAK() },
                micheline(tezos) { KECCAK() },
                michelineData(tezos) { KECCAK },
                michelineData(tezos) { KECCAK },
                michelineData(tezos) { KECCAK() },
                michelineData(tezos) { KECCAK() },
                michelineInstruction(tezos) { KECCAK },
                michelineInstruction(tezos) { KECCAK },
                michelineInstruction(tezos) { KECCAK() },
                michelineInstruction(tezos) { KECCAK() },
            ),
            MichelinePrimitiveApplication("SHA3") to listOf(
                micheline(tezos) { SHA3 },
                micheline(michelsonToMichelineConverter) { SHA3 },
                micheline(tezos) { SHA3() },
                micheline(michelsonToMichelineConverter) { SHA3() },
                michelineData(tezos) { SHA3 },
                michelineData(michelsonToMichelineConverter) { SHA3 },
                michelineData(tezos) { SHA3() },
                michelineData(michelsonToMichelineConverter) { SHA3() },
                michelineInstruction(tezos) { SHA3 },
                michelineInstruction(michelsonToMichelineConverter) { SHA3 },
                michelineInstruction(tezos) { SHA3() },
                michelineInstruction(michelsonToMichelineConverter) { SHA3() },
            ),
            MichelinePrimitiveApplication("SHA256") to listOf(
                micheline(tezos) { SHA256 },
                micheline(michelsonToMichelineConverter) { SHA256 },
                micheline(tezos) { SHA256() },
                micheline(michelsonToMichelineConverter) { SHA256() },
                michelineData(tezos) { SHA256 },
                michelineData(michelsonToMichelineConverter) { SHA256 },
                michelineData(tezos) { SHA256() },
                michelineData(michelsonToMichelineConverter) { SHA256() },
                michelineInstruction(tezos) { SHA256 },
                michelineInstruction(michelsonToMichelineConverter) { SHA256 },
                michelineInstruction(tezos) { SHA256() },
                michelineInstruction(michelsonToMichelineConverter) { SHA256() },
            ),
            MichelinePrimitiveApplication("SHA512") to listOf(
                micheline(tezos) { SHA512 },
                micheline(michelsonToMichelineConverter) { SHA512 },
                micheline(tezos) { SHA512() },
                micheline(michelsonToMichelineConverter) { SHA512() },
                michelineData(tezos) { SHA512 },
                michelineData(michelsonToMichelineConverter) { SHA512 },
                michelineData(tezos) { SHA512() },
                michelineData(michelsonToMichelineConverter) { SHA512() },
                michelineInstruction(tezos) { SHA512 },
                michelineInstruction(michelsonToMichelineConverter) { SHA512 },
                michelineInstruction(tezos) { SHA512() },
                michelineInstruction(michelsonToMichelineConverter) { SHA512() },
            ),
            MichelinePrimitiveApplication("HASH_KEY") to listOf(
                micheline(tezos) { HASH_KEY },
                micheline(michelsonToMichelineConverter) { HASH_KEY },
                micheline(tezos) { HASH_KEY() },
                micheline(michelsonToMichelineConverter) { HASH_KEY() },
                michelineData(tezos) { HASH_KEY },
                michelineData(michelsonToMichelineConverter) { HASH_KEY },
                michelineData(tezos) { HASH_KEY() },
                michelineData(michelsonToMichelineConverter) { HASH_KEY() },
                michelineInstruction(tezos) { HASH_KEY },
                michelineInstruction(michelsonToMichelineConverter) { HASH_KEY },
                michelineInstruction(tezos) { HASH_KEY() },
                michelineInstruction(michelsonToMichelineConverter) { HASH_KEY() },
            ),
            MichelinePrimitiveApplication("SOURCE") to listOf(
                micheline(tezos) { SOURCE },
                micheline(michelsonToMichelineConverter) { SOURCE },
                micheline(tezos) { SOURCE() },
                micheline(michelsonToMichelineConverter) { SOURCE() },
                michelineData(tezos) { SOURCE },
                michelineData(michelsonToMichelineConverter) { SOURCE },
                michelineData(tezos) { SOURCE() },
                michelineData(michelsonToMichelineConverter) { SOURCE() },
                michelineInstruction(tezos) { SOURCE },
                michelineInstruction(michelsonToMichelineConverter) { SOURCE },
                michelineInstruction(tezos) { SOURCE() },
                michelineInstruction(michelsonToMichelineConverter) { SOURCE() },
            ),
            MichelinePrimitiveApplication("SENDER") to listOf(
                micheline(tezos) { SENDER },
                micheline(michelsonToMichelineConverter) { SENDER },
                micheline(tezos) { SENDER() },
                micheline(michelsonToMichelineConverter) { SENDER() },
                michelineData(tezos) { SENDER },
                michelineData(michelsonToMichelineConverter) { SENDER },
                michelineData(tezos) { SENDER() },
                michelineData(michelsonToMichelineConverter) { SENDER() },
                michelineInstruction(tezos) { SENDER },
                michelineInstruction(michelsonToMichelineConverter) { SENDER },
                michelineInstruction(tezos) { SENDER() },
                michelineInstruction(michelsonToMichelineConverter) { SENDER() },
            ),
            MichelinePrimitiveApplication("ADDRESS") to listOf(
                micheline(tezos) { ADDRESS },
                micheline(michelsonToMichelineConverter) { ADDRESS },
                micheline(tezos) { ADDRESS() },
                micheline(michelsonToMichelineConverter) { ADDRESS() },
                michelineData(tezos) { ADDRESS },
                michelineData(michelsonToMichelineConverter) { ADDRESS },
                michelineData(tezos) { ADDRESS() },
                michelineData(michelsonToMichelineConverter) { ADDRESS() },
                michelineInstruction(tezos) { ADDRESS },
                michelineInstruction(michelsonToMichelineConverter) { ADDRESS },
                michelineInstruction(tezos) { ADDRESS() },
                michelineInstruction(michelsonToMichelineConverter) { ADDRESS() },
            ),
            MichelinePrimitiveApplication("CHAIN_ID") to listOf(
                micheline(tezos) { CHAIN_ID },
                micheline(michelsonToMichelineConverter) { CHAIN_ID },
                micheline(tezos) { CHAIN_ID() },
                micheline(michelsonToMichelineConverter) { CHAIN_ID() },
                michelineData(tezos) { CHAIN_ID },
                michelineData(michelsonToMichelineConverter) { CHAIN_ID },
                michelineData(tezos) { CHAIN_ID() },
                michelineData(michelsonToMichelineConverter) { CHAIN_ID() },
                michelineInstruction(tezos) { CHAIN_ID },
                michelineInstruction(michelsonToMichelineConverter) { CHAIN_ID },
                michelineInstruction(tezos) { CHAIN_ID() },
                michelineInstruction(michelsonToMichelineConverter) { CHAIN_ID() },
            ),
            MichelinePrimitiveApplication("TOTAL_VOTING_POWER") to listOf(
                micheline(tezos) { TOTAL_VOTING_POWER },
                micheline(michelsonToMichelineConverter) { TOTAL_VOTING_POWER },
                micheline(tezos) { TOTAL_VOTING_POWER() },
                micheline(michelsonToMichelineConverter) { TOTAL_VOTING_POWER() },
                michelineData(tezos) { TOTAL_VOTING_POWER },
                michelineData(michelsonToMichelineConverter) { TOTAL_VOTING_POWER },
                michelineData(tezos) { TOTAL_VOTING_POWER() },
                michelineData(michelsonToMichelineConverter) { TOTAL_VOTING_POWER() },
                michelineInstruction(tezos) { TOTAL_VOTING_POWER },
                michelineInstruction(michelsonToMichelineConverter) { TOTAL_VOTING_POWER },
                michelineInstruction(tezos) { TOTAL_VOTING_POWER() },
                michelineInstruction(michelsonToMichelineConverter) { TOTAL_VOTING_POWER() },
            ),
            MichelinePrimitiveApplication("PAIRING_CHECK") to listOf(
                micheline(tezos) { PAIRING_CHECK },
                micheline(michelsonToMichelineConverter) { PAIRING_CHECK },
                micheline(tezos) { PAIRING_CHECK() },
                micheline(michelsonToMichelineConverter) { PAIRING_CHECK() },
                michelineData(tezos) { PAIRING_CHECK },
                michelineData(michelsonToMichelineConverter) { PAIRING_CHECK },
                michelineData(tezos) { PAIRING_CHECK() },
                michelineData(michelsonToMichelineConverter) { PAIRING_CHECK() },
                michelineInstruction(tezos) { PAIRING_CHECK },
                michelineInstruction(michelsonToMichelineConverter) { PAIRING_CHECK },
                michelineInstruction(tezos) { PAIRING_CHECK() },
                michelineInstruction(michelsonToMichelineConverter) { PAIRING_CHECK() },
            ),
            MichelinePrimitiveApplication(
                "SAPLING_EMPTY_STATE",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline(tezos) { SAPLING_EMPTY_STATE("1") },
                micheline(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE("1") },
                micheline(tezos) { SAPLING_EMPTY_STATE((1).toUByte()) },
                micheline(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE((1).toUByte()) },
                micheline(tezos) { SAPLING_EMPTY_STATE((1).toUShort()) },
                micheline(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE((1).toUShort()) },
                micheline(tezos) { SAPLING_EMPTY_STATE(1U) },
                micheline(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE(1U) },
                micheline(tezos) { SAPLING_EMPTY_STATE(1UL) },
                micheline(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE(1UL) },
                micheline(tezos) { SAPLING_EMPTY_STATE("1") },
                micheline(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE("1") },
                michelineData(tezos) { SAPLING_EMPTY_STATE((1).toUByte()) },
                michelineData(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE((1).toUByte()) },
                michelineData(tezos) { SAPLING_EMPTY_STATE((1).toUShort()) },
                michelineData(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE((1).toUShort()) },
                michelineData(tezos) { SAPLING_EMPTY_STATE(1U) },
                michelineData(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE(1U) },
                michelineData(tezos) { SAPLING_EMPTY_STATE(1UL) },
                michelineData(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE(1UL) },
                micheline(tezos) { SAPLING_EMPTY_STATE("1") },
                micheline(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE("1") },
                michelineInstruction(tezos) { SAPLING_EMPTY_STATE((1).toUByte()) },
                michelineInstruction(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE((1).toUByte()) },
                michelineInstruction(tezos) { SAPLING_EMPTY_STATE((1).toUShort()) },
                michelineInstruction(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE((1).toUShort()) },
                michelineInstruction(tezos) { SAPLING_EMPTY_STATE(1U) },
                michelineInstruction(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE(1U) },
                michelineInstruction(tezos) { SAPLING_EMPTY_STATE(1UL) },
                michelineInstruction(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE(1UL) },
            ),
            MichelinePrimitiveApplication("SAPLING_VERIFY_UPDATE") to listOf(
                micheline(tezos) { SAPLING_VERIFY_UPDATE },
                micheline(michelsonToMichelineConverter) { SAPLING_VERIFY_UPDATE },
                micheline(tezos) { SAPLING_VERIFY_UPDATE() },
                micheline(michelsonToMichelineConverter) { SAPLING_VERIFY_UPDATE() },
                michelineData(tezos) { SAPLING_VERIFY_UPDATE },
                michelineData(michelsonToMichelineConverter) { SAPLING_VERIFY_UPDATE },
                michelineData(tezos) { SAPLING_VERIFY_UPDATE() },
                michelineData(michelsonToMichelineConverter) { SAPLING_VERIFY_UPDATE() },
                michelineInstruction(tezos) { SAPLING_VERIFY_UPDATE },
                michelineInstruction(michelsonToMichelineConverter) { SAPLING_VERIFY_UPDATE },
                michelineInstruction(tezos) { SAPLING_VERIFY_UPDATE() },
                michelineInstruction(michelsonToMichelineConverter) { SAPLING_VERIFY_UPDATE() },
            ),
            MichelinePrimitiveApplication("TICKET") to listOf(
                micheline(tezos) { TICKET },
                micheline(michelsonToMichelineConverter) { TICKET },
                micheline(tezos) { TICKET() },
                micheline(michelsonToMichelineConverter) { TICKET() },
                michelineData(tezos) { TICKET },
                michelineData(michelsonToMichelineConverter) { TICKET },
                michelineData(tezos) { TICKET() },
                michelineData(michelsonToMichelineConverter) { TICKET() },
                michelineInstruction(tezos) { TICKET },
                michelineInstruction(michelsonToMichelineConverter) { TICKET },
                michelineInstruction(tezos) { TICKET() },
                michelineInstruction(michelsonToMichelineConverter) { TICKET() },
            ),
            MichelinePrimitiveApplication("READ_TICKET") to listOf(
                micheline(tezos) { READ_TICKET },
                micheline(michelsonToMichelineConverter) { READ_TICKET },
                micheline(tezos) { READ_TICKET() },
                micheline(michelsonToMichelineConverter) { READ_TICKET() },
                michelineData(tezos) { READ_TICKET },
                michelineData(michelsonToMichelineConverter) { READ_TICKET },
                michelineData(tezos) { READ_TICKET() },
                michelineData(michelsonToMichelineConverter) { READ_TICKET() },
                michelineInstruction(tezos) { READ_TICKET },
                michelineInstruction(michelsonToMichelineConverter) { READ_TICKET },
                michelineInstruction(tezos) { READ_TICKET() },
                michelineInstruction(michelsonToMichelineConverter) { READ_TICKET() },
            ),
            MichelinePrimitiveApplication("SPLIT_TICKET") to listOf(
                micheline(tezos) { SPLIT_TICKET },
                micheline(michelsonToMichelineConverter) { SPLIT_TICKET },
                micheline(tezos) { SPLIT_TICKET() },
                micheline(michelsonToMichelineConverter) { SPLIT_TICKET() },
                michelineData(tezos) { SPLIT_TICKET },
                michelineData(michelsonToMichelineConverter) { SPLIT_TICKET },
                michelineData(tezos) { SPLIT_TICKET() },
                michelineData(michelsonToMichelineConverter) { SPLIT_TICKET() },
                michelineInstruction(tezos) { SPLIT_TICKET },
                michelineInstruction(michelsonToMichelineConverter) { SPLIT_TICKET },
                michelineInstruction(tezos) { SPLIT_TICKET() },
                michelineInstruction(michelsonToMichelineConverter) { SPLIT_TICKET() },
            ),
            MichelinePrimitiveApplication("JOIN_TICKETS") to listOf(
                micheline(tezos) { JOIN_TICKETS },
                micheline(michelsonToMichelineConverter) { JOIN_TICKETS },
                micheline(tezos) { JOIN_TICKETS() },
                micheline(michelsonToMichelineConverter) { JOIN_TICKETS() },
                michelineData(tezos) { JOIN_TICKETS },
                michelineData(michelsonToMichelineConverter) { JOIN_TICKETS },
                michelineData(tezos) { JOIN_TICKETS() },
                michelineData(michelsonToMichelineConverter) { JOIN_TICKETS() },
                michelineInstruction(tezos) { JOIN_TICKETS },
                michelineInstruction(michelsonToMichelineConverter) { JOIN_TICKETS },
                michelineInstruction(tezos) { JOIN_TICKETS() },
                michelineInstruction(michelsonToMichelineConverter) { JOIN_TICKETS() },
            ),
            MichelinePrimitiveApplication("OPEN_CHEST") to listOf(
                micheline(tezos) { OPEN_CHEST },
                micheline(michelsonToMichelineConverter) { OPEN_CHEST },
                micheline(tezos) { OPEN_CHEST() },
                micheline(michelsonToMichelineConverter) { OPEN_CHEST() },
                michelineData(tezos) { OPEN_CHEST },
                michelineData(michelsonToMichelineConverter) { OPEN_CHEST },
                michelineData(tezos) { OPEN_CHEST() },
                michelineData(michelsonToMichelineConverter) { OPEN_CHEST() },
                michelineInstruction(tezos) { OPEN_CHEST },
                michelineInstruction(michelsonToMichelineConverter) { OPEN_CHEST },
                michelineInstruction(tezos) { OPEN_CHEST() },
                michelineInstruction(michelsonToMichelineConverter) { OPEN_CHEST() },
            ),
        )

        expectedWithActual.forEach { (expected, actual) ->
            actual.forEach { assertEquals(expected, it) }
        }
    }
}