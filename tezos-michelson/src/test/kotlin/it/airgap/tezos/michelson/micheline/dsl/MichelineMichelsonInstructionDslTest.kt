package it.airgap.tezos.michelson.micheline.dsl

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.michelson.internal.converter.MichelsonToMichelineConverter
import it.airgap.tezos.michelson.internal.di.ScopedDependencyRegistry
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MichelineMichelsonInstructionDslTest {

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
    fun `builds Micheline Michelson Instruction Expression`() {
        val expectedWithActual = listOf(
            MichelinePrimitiveApplication("DROP") to listOf(
                micheline { DROP },
                micheline(michelsonToMichelineConverter) { DROP },
                micheline { DROP() },
                micheline(michelsonToMichelineConverter) { DROP() },
                michelineData { DROP },
                michelineData(michelsonToMichelineConverter) { DROP },
                michelineData { DROP() },
                michelineData(michelsonToMichelineConverter) { DROP() },
                michelineInstruction { DROP },
                michelineInstruction(michelsonToMichelineConverter) { DROP },
                michelineInstruction { DROP() },
                michelineInstruction(michelsonToMichelineConverter) { DROP() },
            ),
            MichelinePrimitiveApplication(
                "DROP",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline { DROP("1") },
                micheline(michelsonToMichelineConverter) { DROP("1") },
                micheline { DROP((1).toUByte()) },
                micheline(michelsonToMichelineConverter) { DROP((1).toUByte()) },
                micheline { DROP((1).toUShort()) },
                micheline(michelsonToMichelineConverter) { DROP((1).toUShort()) },
                micheline { DROP(1U) },
                micheline(michelsonToMichelineConverter) { DROP(1U) },
                micheline { DROP(1UL) },
                micheline(michelsonToMichelineConverter) { DROP(1UL) },
                micheline {
                    DROP { arg("1") }
                },
                micheline(michelsonToMichelineConverter) {
                    DROP { arg("1") }
                },
                micheline {
                    DROP { arg((1).toUByte()) }
                },
                micheline(michelsonToMichelineConverter) {
                    DROP { arg((1).toUByte()) }
                },
                micheline {
                    DROP { arg((1).toUShort()) }
                },
                micheline(michelsonToMichelineConverter) {
                    DROP { arg((1).toUShort()) }
                },
                micheline {
                    DROP { arg(1U) }
                },
                micheline(michelsonToMichelineConverter) {
                    DROP { arg(1U) }
                },
                micheline {
                    DROP { arg(1UL) }
                },
                micheline(michelsonToMichelineConverter) {
                    DROP { arg(1UL) }
                },
                michelineData { DROP("1") },
                michelineData(michelsonToMichelineConverter) { DROP("1") },
                michelineData { DROP((1).toUByte()) },
                michelineData(michelsonToMichelineConverter) { DROP((1).toUByte()) },
                michelineData { DROP((1).toUShort()) },
                michelineData(michelsonToMichelineConverter) { DROP((1).toUShort()) },
                michelineData { DROP(1U) },
                michelineData(michelsonToMichelineConverter) { DROP(1U) },
                michelineData { DROP(1UL) },
                michelineData(michelsonToMichelineConverter) { DROP(1UL) },
                michelineData {
                    DROP { arg("1") }
                },
                michelineData(michelsonToMichelineConverter) {
                    DROP { arg("1") }
                },
                michelineData {
                    DROP { arg((1).toUByte()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DROP { arg((1).toUByte()) }
                },
                michelineData {
                    DROP { arg((1).toUShort()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DROP { arg((1).toUShort()) }
                },
                michelineData {
                    DROP { arg(1U) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DROP { arg(1U) }
                },
                michelineData {
                    DROP { arg(1UL) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DROP { arg(1UL) }
                },
                michelineInstruction { DROP("1") },
                michelineInstruction(michelsonToMichelineConverter) { DROP("1") },
                michelineInstruction { DROP((1).toUByte()) },
                michelineInstruction(michelsonToMichelineConverter) { DROP((1).toUByte()) },
                michelineInstruction { DROP((1).toUShort()) },
                michelineInstruction(michelsonToMichelineConverter) { DROP((1).toUShort()) },
                michelineInstruction { DROP(1U) },
                michelineInstruction(michelsonToMichelineConverter) { DROP(1U) },
                michelineInstruction { DROP(1UL) },
                michelineInstruction(michelsonToMichelineConverter) { DROP(1UL) },
                michelineInstruction {
                    DROP { arg("1") }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DROP { arg("1") }
                },
                michelineInstruction {
                    DROP { arg((1).toUByte()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DROP { arg((1).toUByte()) }
                },
                michelineInstruction {
                    DROP { arg((1).toUShort()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DROP { arg((1).toUShort()) }
                },
                michelineInstruction {
                    DROP { arg(1U) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DROP { arg(1U) }
                },
                michelineInstruction {
                    DROP { arg(1UL) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DROP { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication("DUP") to listOf(
                micheline { DUP },
                micheline(michelsonToMichelineConverter) { DUP },
                micheline { DUP() },
                micheline(michelsonToMichelineConverter) { DUP() },
                michelineData { DUP },
                michelineData(michelsonToMichelineConverter) { DUP },
                michelineData { DUP() },
                michelineData(michelsonToMichelineConverter) { DUP() },
                michelineInstruction { DUP },
                michelineInstruction(michelsonToMichelineConverter) { DUP },
                michelineInstruction { DUP() },
                michelineInstruction(michelsonToMichelineConverter) { DUP() },
            ),
            MichelinePrimitiveApplication(
                "DUP",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline { DUP("1") },
                micheline(michelsonToMichelineConverter) { DUP("1") },
                micheline { DUP((1).toUByte()) },
                micheline(michelsonToMichelineConverter) { DUP((1).toUByte()) },
                micheline { DUP((1).toUShort()) },
                micheline(michelsonToMichelineConverter) { DUP((1).toUShort()) },
                micheline { DUP(1U) },
                micheline(michelsonToMichelineConverter) { DUP(1U) },
                micheline { DUP(1UL) },
                micheline(michelsonToMichelineConverter) { DUP(1UL) },
                micheline {
                    DUP { arg("1") }
                },
                micheline(michelsonToMichelineConverter) {
                    DUP { arg("1") }
                },
                micheline {
                    DUP { arg((1).toUByte()) }
                },
                micheline(michelsonToMichelineConverter) {
                    DUP { arg((1).toUByte()) }
                },
                micheline {
                    DUP { arg((1).toUShort()) }
                },
                micheline(michelsonToMichelineConverter) {
                    DUP { arg((1).toUShort()) }
                },
                micheline {
                    DUP { arg(1U) }
                },
                micheline(michelsonToMichelineConverter) {
                    DUP { arg(1U) }
                },
                micheline {
                    DUP { arg(1UL) }
                },
                micheline(michelsonToMichelineConverter) {
                    DUP { arg(1UL) }
                },
                michelineData { DUP("1") },
                michelineData(michelsonToMichelineConverter) { DUP("1") },
                michelineData { DUP((1).toUByte()) },
                michelineData(michelsonToMichelineConverter) { DUP((1).toUByte()) },
                michelineData { DUP((1).toUShort()) },
                michelineData(michelsonToMichelineConverter) { DUP((1).toUShort()) },
                michelineData { DUP(1U) },
                michelineData(michelsonToMichelineConverter) { DUP(1U) },
                michelineData { DUP(1UL) },
                michelineData(michelsonToMichelineConverter) { DUP(1UL) },
                michelineData {
                    DUP { arg("1") }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUP { arg("1") }
                },
                michelineData {
                    DUP { arg((1).toUByte()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUP { arg((1).toUByte()) }
                },
                michelineData {
                    DUP { arg((1).toUShort()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUP { arg((1).toUShort()) }
                },
                michelineData {
                    DUP { arg(1U) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUP { arg(1U) }
                },
                michelineData {
                    DUP { arg(1UL) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUP { arg(1UL) }
                },
                michelineInstruction { DUP("1") },
                michelineInstruction(michelsonToMichelineConverter) { DUP("1") },
                michelineInstruction { DUP((1).toUByte()) },
                michelineInstruction(michelsonToMichelineConverter) { DUP((1).toUByte()) },
                michelineInstruction { DUP((1).toUShort()) },
                michelineInstruction(michelsonToMichelineConverter) { DUP((1).toUShort()) },
                michelineInstruction { DUP(1U) },
                michelineInstruction(michelsonToMichelineConverter) { DUP(1U) },
                michelineInstruction { DUP(1UL) },
                michelineInstruction(michelsonToMichelineConverter) { DUP(1UL) },
                michelineInstruction {
                    DUP { arg("1") }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUP { arg("1") }
                },
                michelineInstruction {
                    DUP { arg((1).toUByte()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUP { arg((1).toUByte()) }
                },
                michelineInstruction {
                    DUP { arg((1).toUShort()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUP { arg((1).toUShort()) }
                },
                michelineInstruction {
                    DUP { arg(1U) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUP { arg(1U) }
                },
                michelineInstruction {
                    DUP { arg(1UL) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUP { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication("SWAP") to listOf(
                micheline { SWAP },
                micheline(michelsonToMichelineConverter) { SWAP },
                micheline { SWAP() },
                micheline(michelsonToMichelineConverter) { SWAP() },
                michelineData { SWAP },
                michelineData(michelsonToMichelineConverter) { SWAP },
                michelineData { SWAP() },
                michelineData(michelsonToMichelineConverter) { SWAP() },
                michelineInstruction { SWAP },
                michelineInstruction(michelsonToMichelineConverter) { SWAP },
                michelineInstruction { SWAP() },
                michelineInstruction(michelsonToMichelineConverter) { SWAP() },
            ),
            MichelinePrimitiveApplication("DIG") to listOf(
                micheline { DIG },
                micheline(michelsonToMichelineConverter) { DIG },
                micheline { DIG() },
                micheline(michelsonToMichelineConverter) { DIG() },
                michelineData { DIG },
                michelineData(michelsonToMichelineConverter) { DIG },
                michelineData { DIG() },
                michelineData(michelsonToMichelineConverter) { DIG() },
                michelineInstruction { DIG },
                michelineInstruction(michelsonToMichelineConverter) { DIG },
                michelineInstruction { DIG() },
                michelineInstruction(michelsonToMichelineConverter) { DIG() },
            ),
            MichelinePrimitiveApplication(
                "DIG",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline { DIG("1") },
                micheline(michelsonToMichelineConverter) { DIG("1") },
                micheline { DIG((1).toUByte()) },
                micheline(michelsonToMichelineConverter) { DIG((1).toUByte()) },
                micheline { DIG((1).toUShort()) },
                micheline(michelsonToMichelineConverter) { DIG((1).toUShort()) },
                micheline { DIG(1U) },
                micheline(michelsonToMichelineConverter) { DIG(1U) },
                micheline { DIG(1UL) },
                micheline(michelsonToMichelineConverter) { DIG(1UL) },
                micheline {
                    DIG { arg("1") }
                },
                micheline(michelsonToMichelineConverter) {
                    DIG { arg("1") }
                },
                micheline {
                    DIG { arg((1).toUByte()) }
                },
                micheline(michelsonToMichelineConverter) {
                    DIG { arg((1).toUByte()) }
                },
                micheline {
                    DIG { arg((1).toUShort()) }
                },
                micheline(michelsonToMichelineConverter) {
                    DIG { arg((1).toUShort()) }
                },
                micheline {
                    DIG { arg(1U) }
                },
                micheline(michelsonToMichelineConverter) {
                    DIG { arg(1U) }
                },
                micheline {
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
                michelineData {
                    DIG { arg("1") }
                },
                michelineData(michelsonToMichelineConverter) {
                    DIG { arg("1") }
                },
                michelineData {
                    DIG { arg((1).toUByte()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DIG { arg((1).toUByte()) }
                },
                michelineData {
                    DIG { arg((1).toUShort()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DIG { arg((1).toUShort()) }
                },
                michelineData {
                    DIG { arg(1U) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DIG { arg(1U) }
                },
                michelineData {
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
                michelineInstruction {
                    DIG { arg("1") }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DIG { arg("1") }
                },
                michelineInstruction {
                    DIG { arg((1).toUByte()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DIG { arg((1).toUByte()) }
                },
                michelineInstruction {
                    DIG { arg((1).toUShort()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DIG { arg((1).toUShort()) }
                },
                michelineInstruction {
                    DIG { arg(1U) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DIG { arg(1U) }
                },
                michelineInstruction {
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
                micheline {
                    DUG { arg("1") }
                },
                micheline(michelsonToMichelineConverter) {
                    DUG { arg("1") }
                },
                micheline {
                    DUG { arg((1).toUByte()) }
                },
                micheline(michelsonToMichelineConverter) {
                    DUG { arg((1).toUByte()) }
                },
                micheline {
                    DUG { arg((1).toUShort()) }
                },
                micheline(michelsonToMichelineConverter) {
                    DUG { arg((1).toUShort()) }
                },
                micheline {
                    DUG { arg(1U) }
                },
                micheline(michelsonToMichelineConverter) {
                    DUG { arg(1U) }
                },
                micheline {
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
                michelineData {
                    DUG { arg("1") }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUG { arg("1") }
                },
                michelineData {
                    DUG { arg((1).toUByte()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUG { arg((1).toUByte()) }
                },
                michelineData {
                    DUG { arg((1).toUShort()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUG { arg((1).toUShort()) }
                },
                michelineData {
                    DUG { arg(1U) }
                },
                michelineData(michelsonToMichelineConverter) {
                    DUG { arg(1U) }
                },
                michelineData {
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
                michelineInstruction {
                    DUG { arg("1") }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUG { arg("1") }
                },
                michelineInstruction {
                    DUG { arg((1).toUByte()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUG { arg((1).toUByte()) }
                },
                michelineInstruction {
                    DUG { arg((1).toUShort()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUG { arg((1).toUShort()) }
                },
                michelineInstruction {
                    DUG { arg(1U) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    DUG { arg(1U) }
                },
                michelineInstruction {
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
                micheline {
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
                michelineData {
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
                michelineInstruction {
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
                micheline {
                    NONE {
                        arg { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    NONE {
                        arg { UNIT }
                    }
                },
                micheline {
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
                michelineData {
                    NONE {
                        arg { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    NONE {
                        arg { UNIT }
                    }
                },
                michelineData {
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
                michelineInstruction {
                    NONE {
                        arg { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    NONE {
                        arg { UNIT }
                    }
                },
                michelineInstruction {
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
                micheline {
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
                michelineData {
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
                michelineInstruction {
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
                micheline {
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
                michelineData {
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
                michelineInstruction {
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
                micheline {
                    PAIR { arg("1") }
                },
                micheline(michelsonToMichelineConverter) {
                    PAIR { arg("1") }
                },
                micheline {
                    PAIR { arg((1).toUByte()) }
                },
                micheline(michelsonToMichelineConverter) {
                    PAIR { arg((1).toUByte()) }
                },
                micheline {
                    PAIR { arg((1).toUShort()) }
                },
                micheline(michelsonToMichelineConverter) {
                    PAIR { arg((1).toUShort()) }
                },
                micheline {
                    PAIR { arg(1U) }
                },
                micheline(michelsonToMichelineConverter) {
                    PAIR { arg(1U) }
                },
                micheline {
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
                michelineData {
                    PAIR { arg("1") }
                },
                michelineData(michelsonToMichelineConverter) {
                    PAIR { arg("1") }
                },
                michelineData {
                    PAIR { arg((1).toUByte()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    PAIR { arg((1).toUByte()) }
                },
                michelineData {
                    PAIR { arg((1).toUShort()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    PAIR { arg((1).toUShort()) }
                },
                michelineData {
                    PAIR { arg(1U) }
                },
                michelineData(michelsonToMichelineConverter) {
                    PAIR { arg(1U) }
                },
                michelineData {
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
                michelineInstruction {
                    PAIR { arg("1") }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    PAIR { arg("1") }
                },
                michelineInstruction {
                    PAIR { arg((1).toUByte()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    PAIR { arg((1).toUByte()) }
                },
                michelineInstruction {
                    PAIR { arg((1).toUShort()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    PAIR { arg((1).toUShort()) }
                },
                michelineInstruction {
                    PAIR { arg(1U) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    PAIR { arg(1U) }
                },
                michelineInstruction {
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
                micheline {
                    UNPAIR { arg("1") }
                },
                micheline(michelsonToMichelineConverter) {
                    UNPAIR { arg("1") }
                },
                micheline {
                    UNPAIR { arg((1).toUByte()) }
                },
                micheline(michelsonToMichelineConverter) {
                    UNPAIR { arg((1).toUByte()) }
                },
                micheline {
                    UNPAIR { arg((1).toUShort()) }
                },
                micheline(michelsonToMichelineConverter) {
                    UNPAIR { arg((1).toUShort()) }
                },
                micheline {
                    UNPAIR { arg(1U) }
                },
                micheline(michelsonToMichelineConverter) {
                    UNPAIR { arg(1U) }
                },
                micheline {
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
                michelineData {
                    UNPAIR { arg("1") }
                },
                michelineData(michelsonToMichelineConverter) {
                    UNPAIR { arg("1") }
                },
                michelineData {
                    UNPAIR { arg((1).toUByte()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    UNPAIR { arg((1).toUByte()) }
                },
                michelineData {
                    UNPAIR { arg((1).toUShort()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    UNPAIR { arg((1).toUShort()) }
                },
                michelineData {
                    UNPAIR { arg(1U) }
                },
                michelineData(michelsonToMichelineConverter) {
                    UNPAIR { arg(1U) }
                },
                michelineData {
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
                michelineInstruction {
                    UNPAIR { arg("1") }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UNPAIR { arg("1") }
                },
                michelineInstruction {
                    UNPAIR { arg((1).toUByte()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UNPAIR { arg((1).toUByte()) }
                },
                michelineInstruction {
                    UNPAIR { arg((1).toUShort()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UNPAIR { arg((1).toUShort()) }
                },
                michelineInstruction {
                    UNPAIR { arg(1U) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UNPAIR { arg(1U) }
                },
                michelineInstruction {
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
                micheline {
                    LEFT {
                        arg { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    LEFT {
                        arg { UNIT }
                    }
                },
                micheline {
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
                michelineData {
                    LEFT {
                        arg { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    LEFT {
                        arg { UNIT }
                    }
                },
                michelineData {
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
                michelineInstruction {
                    LEFT {
                        arg { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    LEFT {
                        arg { UNIT }
                    }
                },
                michelineInstruction {
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
                micheline {
                    RIGHT {
                        arg { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    RIGHT {
                        arg { UNIT }
                    }
                },
                micheline {
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
                michelineData {
                    RIGHT {
                        arg { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    RIGHT {
                        arg { UNIT }
                    }
                },
                michelineData {
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
                michelineInstruction {
                    RIGHT {
                        arg { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    RIGHT {
                        arg { UNIT }
                    }
                },
                michelineInstruction {
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
                micheline {
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
                michelineData {
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
                michelineInstruction {
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
                micheline {
                    NIL {
                        arg { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    NIL {
                        arg { UNIT }
                    }
                },
                micheline {
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
                michelineData {
                    NIL {
                        arg { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    NIL {
                        arg { UNIT }
                    }
                },
                michelineData {
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
                michelineInstruction {
                    NIL {
                        arg { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    NIL {
                        arg { UNIT }
                    }
                },
                michelineInstruction {
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
                micheline { CONS },
                micheline(michelsonToMichelineConverter) { CONS },
                micheline { CONS() },
                micheline(michelsonToMichelineConverter) { CONS() },
                michelineData { CONS },
                michelineData(michelsonToMichelineConverter) { CONS },
                michelineData { CONS() },
                michelineData(michelsonToMichelineConverter) { CONS() },
                michelineInstruction { CONS },
                michelineInstruction(michelsonToMichelineConverter) { CONS },
                michelineInstruction { CONS() },
                michelineInstruction(michelsonToMichelineConverter) { CONS() },
            ),
            MichelinePrimitiveApplication(
                "IF_CONS",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")), MichelineSequence(MichelinePrimitiveApplication("UNIT")))
            ) to listOf(
                micheline {
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
                michelineData {
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
                michelineInstruction {
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
                micheline { SIZE },
                micheline(michelsonToMichelineConverter) { SIZE },
                micheline { SIZE() },
                micheline(michelsonToMichelineConverter) { SIZE() },
                michelineData { SIZE },
                michelineData(michelsonToMichelineConverter) { SIZE },
                michelineData { SIZE() },
                michelineData(michelsonToMichelineConverter) { SIZE() },
                michelineInstruction { SIZE },
                michelineInstruction(michelsonToMichelineConverter) { SIZE },
                michelineInstruction { SIZE() },
                michelineInstruction(michelsonToMichelineConverter) { SIZE() },
            ),
            MichelinePrimitiveApplication(
                "EMPTY_SET",
                args = listOf(MichelinePrimitiveApplication("UNIT"))
            ) to listOf(
                micheline {
                    EMPTY_SET {
                        arg { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    EMPTY_SET {
                        arg { UNIT }
                    }
                },
                micheline {
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
                michelineData {
                    EMPTY_SET {
                        arg { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    EMPTY_SET {
                        arg { UNIT }
                    }
                },
                michelineData {
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
                michelineInstruction {
                    EMPTY_SET {
                        arg { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    EMPTY_SET {
                        arg { UNIT }
                    }
                },
                michelineInstruction {
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
                micheline {
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
                michelineData {
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
                michelineInstruction {
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
                micheline {
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
                michelineData {
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
                michelineInstruction {
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
                micheline {
                    MAP {
                        expression { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    MAP {
                        expression { UNIT }
                    }
                },
                micheline {
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
                michelineData {
                    MAP {
                        expression { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    MAP {
                        expression { UNIT }
                    }
                },
                michelineData {
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
                michelineInstruction {
                    MAP {
                        expression { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    MAP {
                        expression { UNIT }
                    }
                },
                michelineInstruction {
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
                micheline {
                    ITER {
                        expression { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    ITER {
                        expression { UNIT }
                    }
                },
                micheline {
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
                michelineData {
                    ITER {
                        expression { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    ITER {
                        expression { UNIT }
                    }
                },
                michelineData {
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
                michelineInstruction {
                    ITER {
                        expression { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    ITER {
                        expression { UNIT }
                    }
                },
                michelineInstruction {
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
                micheline { MEM },
                micheline(michelsonToMichelineConverter) { MEM },
                micheline { MEM() },
                micheline(michelsonToMichelineConverter) { MEM() },
                michelineData { MEM },
                michelineData(michelsonToMichelineConverter) { MEM },
                michelineData { MEM() },
                michelineData(michelsonToMichelineConverter) { MEM() },
                michelineInstruction { MEM },
                michelineInstruction(michelsonToMichelineConverter) { MEM },
                michelineInstruction { MEM() },
                michelineInstruction(michelsonToMichelineConverter) { MEM() },
            ),
            MichelinePrimitiveApplication("GET") to listOf(
                micheline { GET },
                micheline(michelsonToMichelineConverter) { GET },
                micheline { GET() },
                micheline(michelsonToMichelineConverter) { GET() },
                michelineData { GET },
                michelineData(michelsonToMichelineConverter) { GET },
                michelineData { GET() },
                michelineData(michelsonToMichelineConverter) { GET() },
                michelineInstruction { GET },
                michelineInstruction(michelsonToMichelineConverter) { GET },
                michelineInstruction { GET() },
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
                micheline {
                    GET { arg("1") }
                },
                micheline(michelsonToMichelineConverter) {
                    GET { arg("1") }
                },
                micheline {
                    GET { arg((1).toUByte()) }
                },
                micheline(michelsonToMichelineConverter) {
                    GET { arg((1).toUByte()) }
                },
                micheline {
                    GET { arg((1).toUShort()) }
                },
                micheline(michelsonToMichelineConverter) {
                    GET { arg((1).toUShort()) }
                },
                micheline {
                    GET { arg(1U) }
                },
                micheline(michelsonToMichelineConverter) {
                    GET { arg(1U) }
                },
                micheline {
                    GET { arg(1UL) }
                },
                micheline(michelsonToMichelineConverter) {
                    GET { arg(1UL) }
                },
                michelineData { GET("1") },
                michelineData(michelsonToMichelineConverter) { GET("1") },
                michelineData { GET((1).toUByte()) },
                michelineData(michelsonToMichelineConverter) { GET((1).toUByte()) },
                michelineData { GET((1).toUShort()) },
                michelineData(michelsonToMichelineConverter) { GET((1).toUShort()) },
                michelineData { GET(1U) },
                michelineData(michelsonToMichelineConverter) { GET(1U) },
                michelineData { GET(1UL) },
                michelineData(michelsonToMichelineConverter) { GET(1UL) },
                michelineData {
                    GET { arg("1") }
                },
                michelineData(michelsonToMichelineConverter) {
                    GET { arg("1") }
                },
                michelineData {
                    GET { arg((1).toUByte()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    GET { arg((1).toUByte()) }
                },
                michelineData {
                    GET { arg((1).toUShort()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    GET { arg((1).toUShort()) }
                },
                michelineData {
                    GET { arg(1U) }
                },
                michelineData(michelsonToMichelineConverter) {
                    GET { arg(1U) }
                },
                michelineData {
                    GET { arg(1UL) }
                },
                michelineData(michelsonToMichelineConverter) {
                    GET { arg(1UL) }
                },
                michelineInstruction { GET("1") },
                michelineInstruction(michelsonToMichelineConverter) { GET("1") },
                michelineInstruction { GET((1).toUByte()) },
                michelineInstruction(michelsonToMichelineConverter) { GET((1).toUByte()) },
                michelineInstruction { GET((1).toUShort()) },
                michelineInstruction(michelsonToMichelineConverter) { GET((1).toUShort()) },
                michelineInstruction { GET(1U) },
                michelineInstruction(michelsonToMichelineConverter) { GET(1U) },
                michelineInstruction { GET(1UL) },
                michelineInstruction(michelsonToMichelineConverter) { GET(1UL) },
                michelineInstruction {
                    GET { arg("1") }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    GET { arg("1") }
                },
                michelineInstruction {
                    GET { arg((1).toUByte()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    GET { arg((1).toUByte()) }
                },
                michelineInstruction {
                    GET { arg((1).toUShort()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    GET { arg((1).toUShort()) }
                },
                michelineInstruction {
                    GET { arg(1U) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    GET { arg(1U) }
                },
                michelineInstruction {
                    GET { arg(1UL) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    GET { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication("UPDATE") to listOf(
                micheline { UPDATE },
                micheline(michelsonToMichelineConverter) { UPDATE },
                micheline { UPDATE() },
                micheline(michelsonToMichelineConverter) { UPDATE() },
                michelineData { UPDATE },
                michelineData(michelsonToMichelineConverter) { UPDATE },
                michelineData { UPDATE() },
                michelineData(michelsonToMichelineConverter) { UPDATE() },
                michelineInstruction { UPDATE },
                michelineInstruction(michelsonToMichelineConverter) { UPDATE },
                michelineInstruction { UPDATE() },
                michelineInstruction(michelsonToMichelineConverter) { UPDATE() },
            ),
            MichelinePrimitiveApplication(
                "UPDATE",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline { UPDATE("1") },
                micheline(michelsonToMichelineConverter) { UPDATE("1") },
                micheline { UPDATE((1).toUByte()) },
                micheline(michelsonToMichelineConverter) { UPDATE((1).toUByte()) },
                micheline { UPDATE((1).toUShort()) },
                micheline(michelsonToMichelineConverter) { UPDATE((1).toUShort()) },
                micheline { UPDATE(1U) },
                micheline(michelsonToMichelineConverter) { UPDATE(1U) },
                micheline { UPDATE(1UL) },
                micheline(michelsonToMichelineConverter) { UPDATE(1UL) },
                micheline {
                    UPDATE { arg("1") }
                },
                micheline(michelsonToMichelineConverter) {
                    UPDATE { arg("1") }
                },
                micheline {
                    UPDATE { arg((1).toUByte()) }
                },
                micheline(michelsonToMichelineConverter) {
                    UPDATE { arg((1).toUByte()) }
                },
                micheline {
                    UPDATE { arg((1).toUShort()) }
                },
                micheline(michelsonToMichelineConverter) {
                    UPDATE { arg((1).toUShort()) }
                },
                micheline {
                    UPDATE { arg(1U) }
                },
                micheline(michelsonToMichelineConverter) {
                    UPDATE { arg(1U) }
                },
                micheline {
                    UPDATE { arg(1UL) }
                },
                micheline(michelsonToMichelineConverter) {
                    UPDATE { arg(1UL) }
                },
                michelineData { UPDATE("1") },
                michelineData(michelsonToMichelineConverter) { UPDATE("1") },
                michelineData { UPDATE((1).toUByte()) },
                michelineData(michelsonToMichelineConverter) { UPDATE((1).toUByte()) },
                michelineData { UPDATE((1).toUShort()) },
                michelineData(michelsonToMichelineConverter) { UPDATE((1).toUShort()) },
                michelineData { UPDATE(1U) },
                michelineData(michelsonToMichelineConverter) { UPDATE(1U) },
                michelineData { UPDATE(1UL) },
                michelineData(michelsonToMichelineConverter) { UPDATE(1UL) },
                michelineData {
                    UPDATE { arg("1") }
                },
                michelineData(michelsonToMichelineConverter) {
                    UPDATE { arg("1") }
                },
                michelineData {
                    UPDATE { arg((1).toUByte()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    UPDATE { arg((1).toUByte()) }
                },
                michelineData {
                    UPDATE { arg((1).toUShort()) }
                },
                michelineData(michelsonToMichelineConverter) {
                    UPDATE { arg((1).toUShort()) }
                },
                michelineData {
                    UPDATE { arg(1U) }
                },
                michelineData(michelsonToMichelineConverter) {
                    UPDATE { arg(1U) }
                },
                michelineData {
                    UPDATE { arg(1UL) }
                },
                michelineData(michelsonToMichelineConverter) {
                    UPDATE { arg(1UL) }
                },
                michelineInstruction { UPDATE("1") },
                michelineInstruction(michelsonToMichelineConverter) { UPDATE("1") },
                michelineInstruction { UPDATE((1).toUByte()) },
                michelineInstruction(michelsonToMichelineConverter) { UPDATE((1).toUByte()) },
                michelineInstruction { UPDATE((1).toUShort()) },
                michelineInstruction(michelsonToMichelineConverter) { UPDATE((1).toUShort()) },
                michelineInstruction { UPDATE(1U) },
                michelineInstruction(michelsonToMichelineConverter) { UPDATE(1U) },
                michelineInstruction { UPDATE(1UL) },
                michelineInstruction(michelsonToMichelineConverter) { UPDATE(1UL) },
                michelineInstruction {
                    UPDATE { arg("1") }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UPDATE { arg("1") }
                },
                michelineInstruction {
                    UPDATE { arg((1).toUByte()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UPDATE { arg((1).toUByte()) }
                },
                michelineInstruction {
                    UPDATE { arg((1).toUShort()) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UPDATE { arg((1).toUShort()) }
                },
                michelineInstruction {
                    UPDATE { arg(1U) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UPDATE { arg(1U) }
                },
                michelineInstruction {
                    UPDATE { arg(1UL) }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UPDATE { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication("GET_AND_UPDATE") to listOf(
                micheline { GET_AND_UPDATE },
                micheline(michelsonToMichelineConverter) { GET_AND_UPDATE },
                micheline { GET_AND_UPDATE() },
                micheline(michelsonToMichelineConverter) { GET_AND_UPDATE() },
                michelineData { GET_AND_UPDATE },
                michelineData(michelsonToMichelineConverter) { GET_AND_UPDATE },
                michelineData { GET_AND_UPDATE() },
                michelineData(michelsonToMichelineConverter) { GET_AND_UPDATE() },
                michelineInstruction { GET_AND_UPDATE },
                michelineInstruction(michelsonToMichelineConverter) { GET_AND_UPDATE },
                michelineInstruction { GET_AND_UPDATE() },
                michelineInstruction(michelsonToMichelineConverter) { GET_AND_UPDATE() },
            ),
            MichelinePrimitiveApplication(
                "IF",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT")), MichelineSequence(MichelinePrimitiveApplication("UNIT")))
            ) to listOf(
                micheline {
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
                michelineData {
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
                michelineInstruction {
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
                micheline {
                    LOOP {
                        body { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    LOOP {
                        body { UNIT }
                    }
                },
                micheline {
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
                michelineData {
                    LOOP {
                        body { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    LOOP {
                        body { UNIT }
                    }
                },
                michelineData {
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
                michelineInstruction {
                    LOOP {
                        body { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    LOOP {
                        body { UNIT }
                    }
                },
                michelineInstruction {
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
                micheline {
                    LOOP_LEFT {
                        body { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    LOOP_LEFT {
                        body { UNIT }
                    }
                },
                micheline {
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
                michelineData {
                    LOOP_LEFT {
                        body { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    LOOP_LEFT {
                        body { UNIT }
                    }
                },
                michelineData {
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
                michelineInstruction {
                    LOOP_LEFT {
                        body { UNIT }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    LOOP_LEFT {
                        body { UNIT }
                    }
                },
                michelineInstruction {
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
                micheline { 
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
                micheline {
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
                micheline {
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
                micheline { EXEC },
                micheline(michelsonToMichelineConverter) { EXEC },
                micheline { EXEC() },
                micheline(michelsonToMichelineConverter) { EXEC() },
                michelineData { EXEC },
                michelineData(michelsonToMichelineConverter) { EXEC },
                michelineData { EXEC() },
                michelineData(michelsonToMichelineConverter) { EXEC() },
                michelineInstruction { EXEC },
                michelineInstruction(michelsonToMichelineConverter) { EXEC },
                michelineInstruction { EXEC() },
                michelineInstruction(michelsonToMichelineConverter) { EXEC() },
            ),
            MichelinePrimitiveApplication("APPLY") to listOf(
                micheline { APPLY },
                micheline(michelsonToMichelineConverter) { APPLY },
                micheline { APPLY() },
                micheline(michelsonToMichelineConverter) { APPLY() },
                michelineData { APPLY },
                michelineData(michelsonToMichelineConverter) { APPLY },
                michelineData { APPLY() },
                michelineData(michelsonToMichelineConverter) { APPLY() },
                michelineInstruction { APPLY },
                michelineInstruction(michelsonToMichelineConverter) { APPLY },
                michelineInstruction { APPLY() },
                michelineInstruction(michelsonToMichelineConverter) { APPLY() },
            ),
            MichelinePrimitiveApplication(
                "DIP",
                args = listOf(MichelineSequence(MichelinePrimitiveApplication("UNIT"))),
            ) to listOf(
                micheline {
                    DIP {
                        instruction { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    DIP {
                        instruction { UNIT }
                    }
                },
                michelineData {
                    DIP {
                        instruction { UNIT }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    DIP {
                        instruction { UNIT }
                    }
                },
                michelineInstruction {
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
                micheline {
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
                micheline {
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
                micheline {
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
                micheline {
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
                micheline {
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
                michelineData {
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
                michelineData {
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
                michelineData {
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
                michelineData {
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
                michelineData {
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
                michelineInstruction {
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
                michelineInstruction {
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
                michelineInstruction {
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
                michelineInstruction {
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
                michelineInstruction {
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
                micheline { FAILWITH },
                micheline(michelsonToMichelineConverter) { FAILWITH },
                micheline { FAILWITH() },
                micheline(michelsonToMichelineConverter) { FAILWITH() },
                michelineData { FAILWITH },
                michelineData(michelsonToMichelineConverter) { FAILWITH },
                michelineData { FAILWITH() },
                michelineData(michelsonToMichelineConverter) { FAILWITH() },
                michelineInstruction { FAILWITH },
                michelineInstruction(michelsonToMichelineConverter) { FAILWITH },
                michelineInstruction { FAILWITH() },
                michelineInstruction(michelsonToMichelineConverter) { FAILWITH() },
            ),
            MichelinePrimitiveApplication("CAST") to listOf(
                micheline { CAST },
                micheline(michelsonToMichelineConverter) { CAST },
                micheline { CAST() },
                micheline(michelsonToMichelineConverter) { CAST() },
                michelineData { CAST },
                michelineData(michelsonToMichelineConverter) { CAST },
                michelineData { CAST() },
                michelineData(michelsonToMichelineConverter) { CAST() },
                michelineInstruction { CAST },
                michelineInstruction(michelsonToMichelineConverter) { CAST },
                michelineInstruction { CAST() },
                michelineInstruction(michelsonToMichelineConverter) { CAST() },
            ),
            MichelinePrimitiveApplication("RENAME") to listOf(
                micheline { RENAME },
                micheline(michelsonToMichelineConverter) { RENAME },
                micheline { RENAME() },
                micheline(michelsonToMichelineConverter) { RENAME() },
                michelineData { RENAME },
                michelineData(michelsonToMichelineConverter) { RENAME },
                michelineData { RENAME() },
                michelineData(michelsonToMichelineConverter) { RENAME() },
                michelineInstruction { RENAME },
                michelineInstruction(michelsonToMichelineConverter) { RENAME },
                michelineInstruction { RENAME() },
                michelineInstruction(michelsonToMichelineConverter) { RENAME() },
            ),
            MichelinePrimitiveApplication("CONCAT") to listOf(
                micheline { CONCAT },
                micheline(michelsonToMichelineConverter) { CONCAT },
                micheline { CONCAT() },
                micheline(michelsonToMichelineConverter) { CONCAT() },
                michelineData { CONCAT },
                michelineData(michelsonToMichelineConverter) { CONCAT },
                michelineData { CONCAT() },
                michelineData(michelsonToMichelineConverter) { CONCAT() },
                michelineInstruction { CONCAT },
                michelineInstruction(michelsonToMichelineConverter) { CONCAT },
                michelineInstruction { CONCAT() },
                michelineInstruction(michelsonToMichelineConverter) { CONCAT() },
            ),
            MichelinePrimitiveApplication("SLICE") to listOf(
                micheline { SLICE },
                micheline(michelsonToMichelineConverter) { SLICE },
                micheline { SLICE() },
                micheline(michelsonToMichelineConverter) { SLICE() },
                michelineData { SLICE },
                michelineData(michelsonToMichelineConverter) { SLICE },
                michelineData { SLICE() },
                michelineData(michelsonToMichelineConverter) { SLICE() },
                michelineInstruction { SLICE },
                michelineInstruction(michelsonToMichelineConverter) { SLICE },
                michelineInstruction { SLICE() },
                michelineInstruction(michelsonToMichelineConverter) { SLICE() },
            ),
            MichelinePrimitiveApplication("PACK") to listOf(
                micheline { PACK },
                micheline(michelsonToMichelineConverter) { PACK },
                micheline { PACK() },
                micheline(michelsonToMichelineConverter) { PACK() },
                michelineData { PACK },
                michelineData(michelsonToMichelineConverter) { PACK },
                michelineData { PACK() },
                michelineData(michelsonToMichelineConverter) { PACK() },
                michelineInstruction { PACK },
                michelineInstruction(michelsonToMichelineConverter) { PACK },
                michelineInstruction { PACK() },
                michelineInstruction(michelsonToMichelineConverter) { PACK() },
            ),
            MichelinePrimitiveApplication(
                "UNPACK",
                args = listOf(MichelinePrimitiveApplication("unit"))
            ) to listOf(
                micheline {
                    UNPACK {
                        arg { unit }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    UNPACK {
                        arg { unit }
                    }
                },
                micheline {
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
                michelineData {
                    UNPACK {
                        arg { unit }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    UNPACK {
                        arg { unit }
                    }
                },
                michelineData {
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
                michelineInstruction {
                    UNPACK {
                        arg { unit }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    UNPACK {
                        arg { unit }
                    }
                },
                michelineInstruction {
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
                micheline { ADD },
                micheline(michelsonToMichelineConverter) { ADD },
                micheline { ADD() },
                micheline(michelsonToMichelineConverter) { ADD() },
                michelineData { ADD },
                michelineData(michelsonToMichelineConverter) { ADD },
                michelineData { ADD() },
                michelineData(michelsonToMichelineConverter) { ADD() },
                michelineInstruction { ADD },
                michelineInstruction(michelsonToMichelineConverter) { ADD },
                michelineInstruction { ADD() },
                michelineInstruction(michelsonToMichelineConverter) { ADD() },
            ),
            MichelinePrimitiveApplication("SUB") to listOf(
                micheline { SUB },
                micheline(michelsonToMichelineConverter) { SUB },
                micheline { SUB() },
                micheline(michelsonToMichelineConverter) { SUB() },
                michelineData { SUB },
                michelineData(michelsonToMichelineConverter) { SUB },
                michelineData { SUB() },
                michelineData(michelsonToMichelineConverter) { SUB() },
                michelineInstruction { SUB },
                michelineInstruction(michelsonToMichelineConverter) { SUB },
                michelineInstruction { SUB() },
                michelineInstruction(michelsonToMichelineConverter) { SUB() },
            ),
            MichelinePrimitiveApplication("MUL") to listOf(
                micheline { MUL },
                micheline(michelsonToMichelineConverter) { MUL },
                micheline { MUL() },
                micheline(michelsonToMichelineConverter) { MUL() },
                michelineData { MUL },
                michelineData(michelsonToMichelineConverter) { MUL },
                michelineData { MUL() },
                michelineData(michelsonToMichelineConverter) { MUL() },
                michelineInstruction { MUL },
                michelineInstruction(michelsonToMichelineConverter) { MUL },
                michelineInstruction { MUL() },
                michelineInstruction(michelsonToMichelineConverter) { MUL() },
            ),
            MichelinePrimitiveApplication("EDIV") to listOf(
                micheline { EDIV },
                micheline(michelsonToMichelineConverter) { EDIV },
                micheline { EDIV() },
                micheline(michelsonToMichelineConverter) { EDIV() },
                michelineData { EDIV },
                michelineData(michelsonToMichelineConverter) { EDIV },
                michelineData { EDIV() },
                michelineData(michelsonToMichelineConverter) { EDIV() },
                michelineInstruction { EDIV },
                michelineInstruction(michelsonToMichelineConverter) { EDIV },
                michelineInstruction { EDIV() },
                michelineInstruction(michelsonToMichelineConverter) { EDIV() },
            ),
            MichelinePrimitiveApplication("ABS") to listOf(
                micheline { ABS },
                micheline(michelsonToMichelineConverter) { ABS },
                micheline { ABS() },
                micheline(michelsonToMichelineConverter) { ABS() },
                michelineData { ABS },
                michelineData(michelsonToMichelineConverter) { ABS },
                michelineData { ABS() },
                michelineData(michelsonToMichelineConverter) { ABS() },
                michelineInstruction { ABS },
                michelineInstruction(michelsonToMichelineConverter) { ABS },
                michelineInstruction { ABS() },
                michelineInstruction(michelsonToMichelineConverter) { ABS() },
            ),
            MichelinePrimitiveApplication("ISNAT") to listOf(
                micheline { ISNAT },
                micheline(michelsonToMichelineConverter) { ISNAT },
                micheline { ISNAT() },
                micheline(michelsonToMichelineConverter) { ISNAT() },
                michelineData { ISNAT },
                michelineData(michelsonToMichelineConverter) { ISNAT },
                michelineData { ISNAT() },
                michelineData(michelsonToMichelineConverter) { ISNAT() },
                michelineInstruction { ISNAT },
                michelineInstruction(michelsonToMichelineConverter) { ISNAT },
                michelineInstruction { ISNAT() },
                michelineInstruction(michelsonToMichelineConverter) { ISNAT() },
            ),
            MichelinePrimitiveApplication("INT") to listOf(
                micheline { INT },
                micheline(michelsonToMichelineConverter) { INT },
                micheline { INT() },
                micheline(michelsonToMichelineConverter) { INT() },
                michelineData { INT },
                michelineData(michelsonToMichelineConverter) { INT },
                michelineData { INT() },
                michelineData(michelsonToMichelineConverter) { INT() },
                michelineInstruction { INT },
                michelineInstruction(michelsonToMichelineConverter) { INT },
                michelineInstruction { INT() },
                michelineInstruction(michelsonToMichelineConverter) { INT() },
            ),
            MichelinePrimitiveApplication("NEG") to listOf(
                micheline { NEG },
                micheline(michelsonToMichelineConverter) { NEG },
                micheline { NEG() },
                micheline(michelsonToMichelineConverter) { NEG() },
                michelineData { NEG },
                michelineData(michelsonToMichelineConverter) { NEG },
                michelineData { NEG() },
                michelineData(michelsonToMichelineConverter) { NEG() },
                michelineInstruction { NEG },
                michelineInstruction(michelsonToMichelineConverter) { NEG },
                michelineInstruction { NEG() },
                michelineInstruction(michelsonToMichelineConverter) { NEG() },
            ),
            MichelinePrimitiveApplication("LSL") to listOf(
                micheline { LSL },
                micheline(michelsonToMichelineConverter) { LSL },
                micheline { LSL() },
                micheline(michelsonToMichelineConverter) { LSL() },
                michelineData { LSL },
                michelineData(michelsonToMichelineConverter) { LSL },
                michelineData { LSL() },
                michelineData(michelsonToMichelineConverter) { LSL() },
                michelineInstruction { LSL },
                michelineInstruction(michelsonToMichelineConverter) { LSL },
                michelineInstruction { LSL() },
                michelineInstruction(michelsonToMichelineConverter) { LSL() },
            ),
            MichelinePrimitiveApplication("LSR") to listOf(
                micheline { LSR },
                micheline(michelsonToMichelineConverter) { LSR },
                micheline { LSR() },
                micheline(michelsonToMichelineConverter) { LSR() },
                michelineData { LSR },
                michelineData(michelsonToMichelineConverter) { LSR },
                michelineData { LSR() },
                michelineData(michelsonToMichelineConverter) { LSR() },
                michelineInstruction { LSR },
                michelineInstruction(michelsonToMichelineConverter) { LSR },
                michelineInstruction { LSR() },
                michelineInstruction(michelsonToMichelineConverter) { LSR() },
            ),
            MichelinePrimitiveApplication("OR") to listOf(
                micheline { OR },
                micheline(michelsonToMichelineConverter) { OR },
                micheline { OR() },
                micheline(michelsonToMichelineConverter) { OR() },
                michelineData { OR },
                michelineData(michelsonToMichelineConverter) { OR },
                michelineData { OR() },
                michelineData(michelsonToMichelineConverter) { OR() },
                michelineInstruction { OR },
                michelineInstruction(michelsonToMichelineConverter) { OR },
                michelineInstruction { OR() },
                michelineInstruction(michelsonToMichelineConverter) { OR() },
            ),
            MichelinePrimitiveApplication("AND") to listOf(
                micheline { AND },
                micheline(michelsonToMichelineConverter) { AND },
                micheline { AND() },
                micheline(michelsonToMichelineConverter) { AND() },
                michelineData { AND },
                michelineData(michelsonToMichelineConverter) { AND },
                michelineData { AND() },
                michelineData(michelsonToMichelineConverter) { AND() },
                michelineInstruction { AND },
                michelineInstruction(michelsonToMichelineConverter) { AND },
                michelineInstruction { AND() },
                michelineInstruction(michelsonToMichelineConverter) { AND() },
            ),
            MichelinePrimitiveApplication("XOR") to listOf(
                micheline { XOR },
                micheline(michelsonToMichelineConverter) { XOR },
                micheline { XOR() },
                micheline(michelsonToMichelineConverter) { XOR() },
                michelineData { XOR },
                michelineData(michelsonToMichelineConverter) { XOR },
                michelineData { XOR() },
                michelineData(michelsonToMichelineConverter) { XOR() },
                michelineInstruction { XOR },
                michelineInstruction(michelsonToMichelineConverter) { XOR },
                michelineInstruction { XOR() },
                michelineInstruction(michelsonToMichelineConverter) { XOR() },
            ),
            MichelinePrimitiveApplication("NOT") to listOf(
                micheline { NOT },
                micheline(michelsonToMichelineConverter) { NOT },
                micheline { NOT() },
                micheline(michelsonToMichelineConverter) { NOT() },
                michelineData { NOT },
                michelineData(michelsonToMichelineConverter) { NOT },
                michelineData { NOT() },
                michelineData(michelsonToMichelineConverter) { NOT() },
                michelineInstruction { NOT },
                michelineInstruction(michelsonToMichelineConverter) { NOT },
                michelineInstruction { NOT() },
                michelineInstruction(michelsonToMichelineConverter) { NOT() },
            ),
            MichelinePrimitiveApplication("COMPARE") to listOf(
                micheline { COMPARE },
                micheline(michelsonToMichelineConverter) { COMPARE },
                micheline { COMPARE() },
                micheline(michelsonToMichelineConverter) { COMPARE() },
                michelineData { COMPARE },
                michelineData(michelsonToMichelineConverter) { COMPARE },
                michelineData { COMPARE() },
                michelineData(michelsonToMichelineConverter) { COMPARE() },
                michelineInstruction { COMPARE },
                michelineInstruction(michelsonToMichelineConverter) { COMPARE },
                michelineInstruction { COMPARE() },
                michelineInstruction(michelsonToMichelineConverter) { COMPARE() },
            ),
            MichelinePrimitiveApplication("EQ") to listOf(
                micheline { EQ },
                micheline(michelsonToMichelineConverter) { EQ },
                micheline { EQ() },
                micheline(michelsonToMichelineConverter) { EQ() },
                michelineData { EQ },
                michelineData(michelsonToMichelineConverter) { EQ },
                michelineData { EQ() },
                michelineData(michelsonToMichelineConverter) { EQ() },
                michelineInstruction { EQ },
                michelineInstruction(michelsonToMichelineConverter) { EQ },
                michelineInstruction { EQ() },
                michelineInstruction(michelsonToMichelineConverter) { EQ() },
            ),
            MichelinePrimitiveApplication("NEQ") to listOf(
                micheline { NEQ },
                micheline(michelsonToMichelineConverter) { NEQ },
                micheline { NEQ() },
                micheline(michelsonToMichelineConverter) { NEQ() },
                michelineData { NEQ },
                michelineData(michelsonToMichelineConverter) { NEQ },
                michelineData { NEQ() },
                michelineData(michelsonToMichelineConverter) { NEQ() },
                michelineInstruction { NEQ },
                michelineInstruction(michelsonToMichelineConverter) { NEQ },
                michelineInstruction { NEQ() },
                michelineInstruction(michelsonToMichelineConverter) { NEQ() },
            ),
            MichelinePrimitiveApplication("LT") to listOf(
                micheline { LT },
                micheline(michelsonToMichelineConverter) { LT },
                micheline { LT() },
                micheline(michelsonToMichelineConverter) { LT() },
                michelineData { LT },
                michelineData(michelsonToMichelineConverter) { LT },
                michelineData { LT() },
                michelineData(michelsonToMichelineConverter) { LT() },
                michelineInstruction { LT },
                michelineInstruction(michelsonToMichelineConverter) { LT },
                michelineInstruction { LT() },
                michelineInstruction(michelsonToMichelineConverter) { LT() },
            ),
            MichelinePrimitiveApplication("GT") to listOf(
                micheline { GT },
                micheline(michelsonToMichelineConverter) { GT },
                micheline { GT() },
                micheline(michelsonToMichelineConverter) { GT() },
                michelineData { GT },
                michelineData(michelsonToMichelineConverter) { GT },
                michelineData { GT() },
                michelineData(michelsonToMichelineConverter) { GT() },
                michelineInstruction { GT },
                michelineInstruction(michelsonToMichelineConverter) { GT },
                michelineInstruction { GT() },
                michelineInstruction(michelsonToMichelineConverter) { GT() },
            ),
            MichelinePrimitiveApplication("LE") to listOf(
                micheline { LE },
                micheline(michelsonToMichelineConverter) { LE },
                micheline { LE() },
                micheline(michelsonToMichelineConverter) { LE() },
                michelineData { LE },
                michelineData(michelsonToMichelineConverter) { LE },
                michelineData { LE() },
                michelineData(michelsonToMichelineConverter) { LE() },
                michelineInstruction { LE },
                michelineInstruction(michelsonToMichelineConverter) { LE },
                michelineInstruction { LE() },
                michelineInstruction(michelsonToMichelineConverter) { LE() },
            ),
            MichelinePrimitiveApplication("GE") to listOf(
                micheline { GE },
                micheline(michelsonToMichelineConverter) { GE },
                micheline { GE() },
                micheline(michelsonToMichelineConverter) { GE() },
                michelineData { GE },
                michelineData(michelsonToMichelineConverter) { GE },
                michelineData { GE() },
                michelineData(michelsonToMichelineConverter) { GE() },
                michelineInstruction { GE },
                michelineInstruction(michelsonToMichelineConverter) { GE },
                michelineInstruction { GE() },
                michelineInstruction(michelsonToMichelineConverter) { GE() },
            ),
            MichelinePrimitiveApplication("SELF") to listOf(
                micheline { SELF },
                micheline(michelsonToMichelineConverter) { SELF },
                micheline { SELF() },
                micheline(michelsonToMichelineConverter) { SELF() },
                michelineData { SELF },
                michelineData(michelsonToMichelineConverter) { SELF },
                michelineData { SELF() },
                michelineData(michelsonToMichelineConverter) { SELF() },
                michelineInstruction { SELF },
                michelineInstruction(michelsonToMichelineConverter) { SELF },
                michelineInstruction { SELF() },
                michelineInstruction(michelsonToMichelineConverter) { SELF() },
            ),
            MichelinePrimitiveApplication("SELF_ADDRESS") to listOf(
                micheline { SELF_ADDRESS },
                micheline(michelsonToMichelineConverter) { SELF_ADDRESS },
                micheline { SELF_ADDRESS() },
                micheline(michelsonToMichelineConverter) { SELF_ADDRESS() },
                michelineData { SELF_ADDRESS },
                michelineData(michelsonToMichelineConverter) { SELF_ADDRESS },
                michelineData { SELF_ADDRESS() },
                michelineData(michelsonToMichelineConverter) { SELF_ADDRESS() },
                michelineInstruction { SELF_ADDRESS },
                michelineInstruction(michelsonToMichelineConverter) { SELF_ADDRESS },
                michelineInstruction { SELF_ADDRESS() },
                michelineInstruction(michelsonToMichelineConverter) { SELF_ADDRESS() },
            ),
            MichelinePrimitiveApplication(
                "CONTRACT",
                args = listOf(MichelinePrimitiveApplication("unit"))
            ) to listOf(
                micheline {
                    CONTRACT {
                        arg { unit }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    CONTRACT {
                        arg { unit }
                    }
                },
                micheline {
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
                michelineData {
                    CONTRACT {
                        arg { unit }
                    }
                },
                michelineData(michelsonToMichelineConverter) {
                    CONTRACT {
                        arg { unit }
                    }
                },
                michelineData {
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
                michelineInstruction {
                    CONTRACT {
                        arg { unit }
                    }
                },
                michelineInstruction(michelsonToMichelineConverter) {
                    CONTRACT {
                        arg { unit }
                    }
                },
                michelineInstruction {
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
                micheline { TRANSFER_TOKENS },
                micheline(michelsonToMichelineConverter) { TRANSFER_TOKENS },
                micheline { TRANSFER_TOKENS() },
                micheline(michelsonToMichelineConverter) { TRANSFER_TOKENS() },
                michelineData { TRANSFER_TOKENS },
                michelineData(michelsonToMichelineConverter) { TRANSFER_TOKENS },
                michelineData { TRANSFER_TOKENS() },
                michelineData(michelsonToMichelineConverter) { TRANSFER_TOKENS() },
                michelineInstruction { TRANSFER_TOKENS },
                michelineInstruction(michelsonToMichelineConverter) { TRANSFER_TOKENS },
                michelineInstruction { TRANSFER_TOKENS() },
                michelineInstruction(michelsonToMichelineConverter) { TRANSFER_TOKENS() },
            ),
            MichelinePrimitiveApplication("SET_DELEGATE") to listOf(
                micheline { SET_DELEGATE },
                micheline(michelsonToMichelineConverter) { SET_DELEGATE },
                micheline { SET_DELEGATE() },
                micheline(michelsonToMichelineConverter) { SET_DELEGATE() },
                michelineData { SET_DELEGATE },
                michelineData(michelsonToMichelineConverter) { SET_DELEGATE },
                michelineData { SET_DELEGATE() },
                michelineData(michelsonToMichelineConverter) { SET_DELEGATE() },
                michelineInstruction { SET_DELEGATE },
                michelineInstruction(michelsonToMichelineConverter) { SET_DELEGATE },
                michelineInstruction { SET_DELEGATE() },
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
                micheline {
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
                michelineData {
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
                michelineInstruction {
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
                micheline { IMPLICIT_ACCOUNT },
                micheline(michelsonToMichelineConverter) { IMPLICIT_ACCOUNT },
                micheline { IMPLICIT_ACCOUNT() },
                micheline(michelsonToMichelineConverter) { IMPLICIT_ACCOUNT() },
                michelineData { IMPLICIT_ACCOUNT },
                michelineData(michelsonToMichelineConverter) { IMPLICIT_ACCOUNT },
                michelineData { IMPLICIT_ACCOUNT() },
                michelineData(michelsonToMichelineConverter) { IMPLICIT_ACCOUNT() },
                michelineInstruction { IMPLICIT_ACCOUNT },
                michelineInstruction(michelsonToMichelineConverter) { IMPLICIT_ACCOUNT },
                michelineInstruction { IMPLICIT_ACCOUNT() },
                michelineInstruction(michelsonToMichelineConverter) { IMPLICIT_ACCOUNT() },
            ),
            MichelinePrimitiveApplication("VOTING_POWER") to listOf(
                micheline { VOTING_POWER },
                micheline(michelsonToMichelineConverter) { VOTING_POWER },
                micheline { VOTING_POWER() },
                micheline(michelsonToMichelineConverter) { VOTING_POWER() },
                michelineData { VOTING_POWER },
                michelineData(michelsonToMichelineConverter) { VOTING_POWER },
                michelineData { VOTING_POWER() },
                michelineData(michelsonToMichelineConverter) { VOTING_POWER() },
                michelineInstruction { VOTING_POWER },
                michelineInstruction(michelsonToMichelineConverter) { VOTING_POWER },
                michelineInstruction { VOTING_POWER() },
                michelineInstruction(michelsonToMichelineConverter) { VOTING_POWER() },
            ),
            MichelinePrimitiveApplication("NOW") to listOf(
                micheline { NOW },
                micheline(michelsonToMichelineConverter) { NOW },
                micheline { NOW() },
                micheline(michelsonToMichelineConverter) { NOW() },
                michelineData { NOW },
                michelineData(michelsonToMichelineConverter) { NOW },
                michelineData { NOW() },
                michelineData(michelsonToMichelineConverter) { NOW() },
                michelineInstruction { NOW },
                michelineInstruction(michelsonToMichelineConverter) { NOW },
                michelineInstruction { NOW() },
                michelineInstruction(michelsonToMichelineConverter) { NOW() },
            ),
            MichelinePrimitiveApplication("LEVEL") to listOf(
                micheline { LEVEL },
                micheline(michelsonToMichelineConverter) { LEVEL },
                micheline { LEVEL() },
                micheline(michelsonToMichelineConverter) { LEVEL() },
                michelineData { LEVEL },
                michelineData(michelsonToMichelineConverter) { LEVEL },
                michelineData { LEVEL() },
                michelineData(michelsonToMichelineConverter) { LEVEL() },
                michelineInstruction { LEVEL },
                michelineInstruction(michelsonToMichelineConverter) { LEVEL },
                michelineInstruction { LEVEL() },
                michelineInstruction(michelsonToMichelineConverter) { LEVEL() },
            ),
            MichelinePrimitiveApplication("AMOUNT") to listOf(
                micheline { AMOUNT },
                micheline(michelsonToMichelineConverter) { AMOUNT },
                micheline { AMOUNT() },
                micheline(michelsonToMichelineConverter) { AMOUNT() },
                michelineData { AMOUNT },
                michelineData(michelsonToMichelineConverter) { AMOUNT },
                michelineData { AMOUNT() },
                michelineData(michelsonToMichelineConverter) { AMOUNT() },
                michelineInstruction { AMOUNT },
                michelineInstruction(michelsonToMichelineConverter) { AMOUNT },
                michelineInstruction { AMOUNT() },
                michelineInstruction(michelsonToMichelineConverter) { AMOUNT() },
            ),
            MichelinePrimitiveApplication("BALANCE") to listOf(
                micheline { BALANCE },
                micheline(michelsonToMichelineConverter) { BALANCE },
                micheline { BALANCE() },
                micheline(michelsonToMichelineConverter) { BALANCE() },
                michelineData { BALANCE },
                michelineData(michelsonToMichelineConverter) { BALANCE },
                michelineData { BALANCE() },
                michelineData(michelsonToMichelineConverter) { BALANCE() },
                michelineInstruction { BALANCE },
                michelineInstruction(michelsonToMichelineConverter) { BALANCE },
                michelineInstruction { BALANCE() },
                michelineInstruction(michelsonToMichelineConverter) { BALANCE() },
            ),
            MichelinePrimitiveApplication("CHECK_SIGNATURE") to listOf(
                micheline { CHECK_SIGNATURE },
                micheline(michelsonToMichelineConverter) { CHECK_SIGNATURE },
                micheline { CHECK_SIGNATURE() },
                micheline(michelsonToMichelineConverter) { CHECK_SIGNATURE() },
                michelineData { CHECK_SIGNATURE },
                michelineData(michelsonToMichelineConverter) { CHECK_SIGNATURE },
                michelineData { CHECK_SIGNATURE() },
                michelineData(michelsonToMichelineConverter) { CHECK_SIGNATURE() },
                michelineInstruction { CHECK_SIGNATURE },
                michelineInstruction(michelsonToMichelineConverter) { CHECK_SIGNATURE },
                michelineInstruction { CHECK_SIGNATURE() },
                michelineInstruction(michelsonToMichelineConverter) { CHECK_SIGNATURE() },
            ),
            MichelinePrimitiveApplication("BLAKE2B") to listOf(
                micheline { BLAKE2B },
                micheline { BLAKE2B },
                micheline { BLAKE2B() },
                micheline { BLAKE2B() },
                michelineData { BLAKE2B },
                michelineData { BLAKE2B },
                michelineData { BLAKE2B() },
                michelineData { BLAKE2B() },
                michelineInstruction { BLAKE2B },
                michelineInstruction { BLAKE2B },
                michelineInstruction { BLAKE2B() },
                michelineInstruction { BLAKE2B() },
            ),
            MichelinePrimitiveApplication("KECCAK") to listOf(
                micheline { KECCAK },
                micheline { KECCAK },
                micheline { KECCAK() },
                micheline { KECCAK() },
                michelineData { KECCAK },
                michelineData { KECCAK },
                michelineData { KECCAK() },
                michelineData { KECCAK() },
                michelineInstruction { KECCAK },
                michelineInstruction { KECCAK },
                michelineInstruction { KECCAK() },
                michelineInstruction { KECCAK() },
            ),
            MichelinePrimitiveApplication("SHA3") to listOf(
                micheline { SHA3 },
                micheline(michelsonToMichelineConverter) { SHA3 },
                micheline { SHA3() },
                micheline(michelsonToMichelineConverter) { SHA3() },
                michelineData { SHA3 },
                michelineData(michelsonToMichelineConverter) { SHA3 },
                michelineData { SHA3() },
                michelineData(michelsonToMichelineConverter) { SHA3() },
                michelineInstruction { SHA3 },
                michelineInstruction(michelsonToMichelineConverter) { SHA3 },
                michelineInstruction { SHA3() },
                michelineInstruction(michelsonToMichelineConverter) { SHA3() },
            ),
            MichelinePrimitiveApplication("SHA256") to listOf(
                micheline { SHA256 },
                micheline(michelsonToMichelineConverter) { SHA256 },
                micheline { SHA256() },
                micheline(michelsonToMichelineConverter) { SHA256() },
                michelineData { SHA256 },
                michelineData(michelsonToMichelineConverter) { SHA256 },
                michelineData { SHA256() },
                michelineData(michelsonToMichelineConverter) { SHA256() },
                michelineInstruction { SHA256 },
                michelineInstruction(michelsonToMichelineConverter) { SHA256 },
                michelineInstruction { SHA256() },
                michelineInstruction(michelsonToMichelineConverter) { SHA256() },
            ),
            MichelinePrimitiveApplication("SHA512") to listOf(
                micheline { SHA512 },
                micheline(michelsonToMichelineConverter) { SHA512 },
                micheline { SHA512() },
                micheline(michelsonToMichelineConverter) { SHA512() },
                michelineData { SHA512 },
                michelineData(michelsonToMichelineConverter) { SHA512 },
                michelineData { SHA512() },
                michelineData(michelsonToMichelineConverter) { SHA512() },
                michelineInstruction { SHA512 },
                michelineInstruction(michelsonToMichelineConverter) { SHA512 },
                michelineInstruction { SHA512() },
                michelineInstruction(michelsonToMichelineConverter) { SHA512() },
            ),
            MichelinePrimitiveApplication("HASH_KEY") to listOf(
                micheline { HASH_KEY },
                micheline(michelsonToMichelineConverter) { HASH_KEY },
                micheline { HASH_KEY() },
                micheline(michelsonToMichelineConverter) { HASH_KEY() },
                michelineData { HASH_KEY },
                michelineData(michelsonToMichelineConverter) { HASH_KEY },
                michelineData { HASH_KEY() },
                michelineData(michelsonToMichelineConverter) { HASH_KEY() },
                michelineInstruction { HASH_KEY },
                michelineInstruction(michelsonToMichelineConverter) { HASH_KEY },
                michelineInstruction { HASH_KEY() },
                michelineInstruction(michelsonToMichelineConverter) { HASH_KEY() },
            ),
            MichelinePrimitiveApplication("SOURCE") to listOf(
                micheline { SOURCE },
                micheline(michelsonToMichelineConverter) { SOURCE },
                micheline { SOURCE() },
                micheline(michelsonToMichelineConverter) { SOURCE() },
                michelineData { SOURCE },
                michelineData(michelsonToMichelineConverter) { SOURCE },
                michelineData { SOURCE() },
                michelineData(michelsonToMichelineConverter) { SOURCE() },
                michelineInstruction { SOURCE },
                michelineInstruction(michelsonToMichelineConverter) { SOURCE },
                michelineInstruction { SOURCE() },
                michelineInstruction(michelsonToMichelineConverter) { SOURCE() },
            ),
            MichelinePrimitiveApplication("SENDER") to listOf(
                micheline { SENDER },
                micheline(michelsonToMichelineConverter) { SENDER },
                micheline { SENDER() },
                micheline(michelsonToMichelineConverter) { SENDER() },
                michelineData { SENDER },
                michelineData(michelsonToMichelineConverter) { SENDER },
                michelineData { SENDER() },
                michelineData(michelsonToMichelineConverter) { SENDER() },
                michelineInstruction { SENDER },
                michelineInstruction(michelsonToMichelineConverter) { SENDER },
                michelineInstruction { SENDER() },
                michelineInstruction(michelsonToMichelineConverter) { SENDER() },
            ),
            MichelinePrimitiveApplication("ADDRESS") to listOf(
                micheline { ADDRESS },
                micheline(michelsonToMichelineConverter) { ADDRESS },
                micheline { ADDRESS() },
                micheline(michelsonToMichelineConverter) { ADDRESS() },
                michelineData { ADDRESS },
                michelineData(michelsonToMichelineConverter) { ADDRESS },
                michelineData { ADDRESS() },
                michelineData(michelsonToMichelineConverter) { ADDRESS() },
                michelineInstruction { ADDRESS },
                michelineInstruction(michelsonToMichelineConverter) { ADDRESS },
                michelineInstruction { ADDRESS() },
                michelineInstruction(michelsonToMichelineConverter) { ADDRESS() },
            ),
            MichelinePrimitiveApplication("CHAIN_ID") to listOf(
                micheline { CHAIN_ID },
                micheline(michelsonToMichelineConverter) { CHAIN_ID },
                micheline { CHAIN_ID() },
                micheline(michelsonToMichelineConverter) { CHAIN_ID() },
                michelineData { CHAIN_ID },
                michelineData(michelsonToMichelineConverter) { CHAIN_ID },
                michelineData { CHAIN_ID() },
                michelineData(michelsonToMichelineConverter) { CHAIN_ID() },
                michelineInstruction { CHAIN_ID },
                michelineInstruction(michelsonToMichelineConverter) { CHAIN_ID },
                michelineInstruction { CHAIN_ID() },
                michelineInstruction(michelsonToMichelineConverter) { CHAIN_ID() },
            ),
            MichelinePrimitiveApplication("TOTAL_VOTING_POWER") to listOf(
                micheline { TOTAL_VOTING_POWER },
                micheline(michelsonToMichelineConverter) { TOTAL_VOTING_POWER },
                micheline { TOTAL_VOTING_POWER() },
                micheline(michelsonToMichelineConverter) { TOTAL_VOTING_POWER() },
                michelineData { TOTAL_VOTING_POWER },
                michelineData(michelsonToMichelineConverter) { TOTAL_VOTING_POWER },
                michelineData { TOTAL_VOTING_POWER() },
                michelineData(michelsonToMichelineConverter) { TOTAL_VOTING_POWER() },
                michelineInstruction { TOTAL_VOTING_POWER },
                michelineInstruction(michelsonToMichelineConverter) { TOTAL_VOTING_POWER },
                michelineInstruction { TOTAL_VOTING_POWER() },
                michelineInstruction(michelsonToMichelineConverter) { TOTAL_VOTING_POWER() },
            ),
            MichelinePrimitiveApplication("PAIRING_CHECK") to listOf(
                micheline { PAIRING_CHECK },
                micheline(michelsonToMichelineConverter) { PAIRING_CHECK },
                micheline { PAIRING_CHECK() },
                micheline(michelsonToMichelineConverter) { PAIRING_CHECK() },
                michelineData { PAIRING_CHECK },
                michelineData(michelsonToMichelineConverter) { PAIRING_CHECK },
                michelineData { PAIRING_CHECK() },
                michelineData(michelsonToMichelineConverter) { PAIRING_CHECK() },
                michelineInstruction { PAIRING_CHECK },
                michelineInstruction(michelsonToMichelineConverter) { PAIRING_CHECK },
                michelineInstruction { PAIRING_CHECK() },
                michelineInstruction(michelsonToMichelineConverter) { PAIRING_CHECK() },
            ),
            MichelinePrimitiveApplication(
                "SAPLING_EMPTY_STATE",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline { SAPLING_EMPTY_STATE("1") },
                micheline(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE("1") },
                micheline { SAPLING_EMPTY_STATE((1).toUByte()) },
                micheline(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE((1).toUByte()) },
                micheline { SAPLING_EMPTY_STATE((1).toUShort()) },
                micheline(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE((1).toUShort()) },
                micheline { SAPLING_EMPTY_STATE(1U) },
                micheline(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE(1U) },
                micheline { SAPLING_EMPTY_STATE(1UL) },
                micheline(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE(1UL) },
                micheline { SAPLING_EMPTY_STATE("1") },
                micheline(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE("1") },
                michelineData { SAPLING_EMPTY_STATE((1).toUByte()) },
                michelineData(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE((1).toUByte()) },
                michelineData { SAPLING_EMPTY_STATE((1).toUShort()) },
                michelineData(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE((1).toUShort()) },
                michelineData { SAPLING_EMPTY_STATE(1U) },
                michelineData(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE(1U) },
                michelineData { SAPLING_EMPTY_STATE(1UL) },
                michelineData(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE(1UL) },
                micheline { SAPLING_EMPTY_STATE("1") },
                micheline(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE("1") },
                michelineInstruction { SAPLING_EMPTY_STATE((1).toUByte()) },
                michelineInstruction(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE((1).toUByte()) },
                michelineInstruction { SAPLING_EMPTY_STATE((1).toUShort()) },
                michelineInstruction(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE((1).toUShort()) },
                michelineInstruction { SAPLING_EMPTY_STATE(1U) },
                michelineInstruction(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE(1U) },
                michelineInstruction { SAPLING_EMPTY_STATE(1UL) },
                michelineInstruction(michelsonToMichelineConverter) { SAPLING_EMPTY_STATE(1UL) },
            ),
            MichelinePrimitiveApplication("SAPLING_VERIFY_UPDATE") to listOf(
                micheline { SAPLING_VERIFY_UPDATE },
                micheline(michelsonToMichelineConverter) { SAPLING_VERIFY_UPDATE },
                micheline { SAPLING_VERIFY_UPDATE() },
                micheline(michelsonToMichelineConverter) { SAPLING_VERIFY_UPDATE() },
                michelineData { SAPLING_VERIFY_UPDATE },
                michelineData(michelsonToMichelineConverter) { SAPLING_VERIFY_UPDATE },
                michelineData { SAPLING_VERIFY_UPDATE() },
                michelineData(michelsonToMichelineConverter) { SAPLING_VERIFY_UPDATE() },
                michelineInstruction { SAPLING_VERIFY_UPDATE },
                michelineInstruction(michelsonToMichelineConverter) { SAPLING_VERIFY_UPDATE },
                michelineInstruction { SAPLING_VERIFY_UPDATE() },
                michelineInstruction(michelsonToMichelineConverter) { SAPLING_VERIFY_UPDATE() },
            ),
            MichelinePrimitiveApplication("TICKET") to listOf(
                micheline { TICKET },
                micheline(michelsonToMichelineConverter) { TICKET },
                micheline { TICKET() },
                micheline(michelsonToMichelineConverter) { TICKET() },
                michelineData { TICKET },
                michelineData(michelsonToMichelineConverter) { TICKET },
                michelineData { TICKET() },
                michelineData(michelsonToMichelineConverter) { TICKET() },
                michelineInstruction { TICKET },
                michelineInstruction(michelsonToMichelineConverter) { TICKET },
                michelineInstruction { TICKET() },
                michelineInstruction(michelsonToMichelineConverter) { TICKET() },
            ),
            MichelinePrimitiveApplication("READ_TICKET") to listOf(
                micheline { READ_TICKET },
                micheline(michelsonToMichelineConverter) { READ_TICKET },
                micheline { READ_TICKET() },
                micheline(michelsonToMichelineConverter) { READ_TICKET() },
                michelineData { READ_TICKET },
                michelineData(michelsonToMichelineConverter) { READ_TICKET },
                michelineData { READ_TICKET() },
                michelineData(michelsonToMichelineConverter) { READ_TICKET() },
                michelineInstruction { READ_TICKET },
                michelineInstruction(michelsonToMichelineConverter) { READ_TICKET },
                michelineInstruction { READ_TICKET() },
                michelineInstruction(michelsonToMichelineConverter) { READ_TICKET() },
            ),
            MichelinePrimitiveApplication("SPLIT_TICKET") to listOf(
                micheline { SPLIT_TICKET },
                micheline(michelsonToMichelineConverter) { SPLIT_TICKET },
                micheline { SPLIT_TICKET() },
                micheline(michelsonToMichelineConverter) { SPLIT_TICKET() },
                michelineData { SPLIT_TICKET },
                michelineData(michelsonToMichelineConverter) { SPLIT_TICKET },
                michelineData { SPLIT_TICKET() },
                michelineData(michelsonToMichelineConverter) { SPLIT_TICKET() },
                michelineInstruction { SPLIT_TICKET },
                michelineInstruction(michelsonToMichelineConverter) { SPLIT_TICKET },
                michelineInstruction { SPLIT_TICKET() },
                michelineInstruction(michelsonToMichelineConverter) { SPLIT_TICKET() },
            ),
            MichelinePrimitiveApplication("JOIN_TICKETS") to listOf(
                micheline { JOIN_TICKETS },
                micheline(michelsonToMichelineConverter) { JOIN_TICKETS },
                micheline { JOIN_TICKETS() },
                micheline(michelsonToMichelineConverter) { JOIN_TICKETS() },
                michelineData { JOIN_TICKETS },
                michelineData(michelsonToMichelineConverter) { JOIN_TICKETS },
                michelineData { JOIN_TICKETS() },
                michelineData(michelsonToMichelineConverter) { JOIN_TICKETS() },
                michelineInstruction { JOIN_TICKETS },
                michelineInstruction(michelsonToMichelineConverter) { JOIN_TICKETS },
                michelineInstruction { JOIN_TICKETS() },
                michelineInstruction(michelsonToMichelineConverter) { JOIN_TICKETS() },
            ),
            MichelinePrimitiveApplication("OPEN_CHEST") to listOf(
                micheline { OPEN_CHEST },
                micheline(michelsonToMichelineConverter) { OPEN_CHEST },
                micheline { OPEN_CHEST() },
                micheline(michelsonToMichelineConverter) { OPEN_CHEST() },
                michelineData { OPEN_CHEST },
                michelineData(michelsonToMichelineConverter) { OPEN_CHEST },
                michelineData { OPEN_CHEST() },
                michelineData(michelsonToMichelineConverter) { OPEN_CHEST() },
                michelineInstruction { OPEN_CHEST },
                michelineInstruction(michelsonToMichelineConverter) { OPEN_CHEST },
                michelineInstruction { OPEN_CHEST() },
                michelineInstruction(michelsonToMichelineConverter) { OPEN_CHEST() },
            ),
        )

        expectedWithActual.forEach { (expected, actual) ->
            actual.forEach { assertEquals(expected, it) }
        }
    }
}