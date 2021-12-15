package it.airgap.tezos.michelson.micheline.dsl

import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import org.junit.Test
import kotlin.test.assertEquals

class MichelineMichelsonInstructionDslTest {

    @Test
    fun `builds Micheline Michelson Instruction Expression`() {
        val expectedWithActual = listOf(
            MichelinePrimitiveApplication("DROP") to listOf(
                micheline { DROP },
                micheline { DROP() },
                michelineData { DROP },
                michelineData { DROP() },
                michelineInstruction { DROP },
                michelineInstruction { DROP() },
            ),
            MichelinePrimitiveApplication(
                "DROP",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline { DROP("1") },
                micheline { DROP((1).toUByte()) },
                micheline { DROP((1).toUShort()) },
                micheline { DROP(1U) },
                micheline { DROP(1UL) },
                micheline {
                    DROP { arg("1") }
                },
                micheline {
                    DROP { arg((1).toUByte()) }
                },
                micheline {
                    DROP { arg((1).toUShort()) }
                },
                micheline {
                    DROP { arg(1U) }
                },
                micheline {
                    DROP { arg(1UL) }
                },
                michelineData { DROP("1") },
                michelineData { DROP((1).toUByte()) },
                michelineData { DROP((1).toUShort()) },
                michelineData { DROP(1U) },
                michelineData { DROP(1UL) },
                michelineData {
                    DROP { arg("1") }
                },
                michelineData {
                    DROP { arg((1).toUByte()) }
                },
                michelineData {
                    DROP { arg((1).toUShort()) }
                },
                michelineData {
                    DROP { arg(1U) }
                },
                michelineData {
                    DROP { arg(1UL) }
                },
                michelineInstruction { DROP("1") },
                michelineInstruction { DROP((1).toUByte()) },
                michelineInstruction { DROP((1).toUShort()) },
                michelineInstruction { DROP(1U) },
                michelineInstruction { DROP(1UL) },
                michelineInstruction {
                    DROP { arg("1") }
                },
                michelineInstruction {
                    DROP { arg((1).toUByte()) }
                },
                michelineInstruction {
                    DROP { arg((1).toUShort()) }
                },
                michelineInstruction {
                    DROP { arg(1U) }
                },
                michelineInstruction {
                    DROP { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication("DUP") to listOf(
                micheline { DUP },
                micheline { DUP() },
                michelineData { DUP },
                michelineData { DUP() },
                michelineInstruction { DUP },
                michelineInstruction { DUP() },
            ),
            MichelinePrimitiveApplication(
                "DUP",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline { DUP("1") },
                micheline { DUP((1).toUByte()) },
                micheline { DUP((1).toUShort()) },
                micheline { DUP(1U) },
                micheline { DUP(1UL) },
                micheline {
                    DUP { arg("1") }
                },
                micheline {
                    DUP { arg((1).toUByte()) }
                },
                micheline {
                    DUP { arg((1).toUShort()) }
                },
                micheline {
                    DUP { arg(1U) }
                },
                micheline {
                    DUP { arg(1UL) }
                },
                michelineData { DUP("1") },
                michelineData { DUP((1).toUByte()) },
                michelineData { DUP((1).toUShort()) },
                michelineData { DUP(1U) },
                michelineData { DUP(1UL) },
                michelineData {
                    DUP { arg("1") }
                },
                michelineData {
                    DUP { arg((1).toUByte()) }
                },
                michelineData {
                    DUP { arg((1).toUShort()) }
                },
                michelineData {
                    DUP { arg(1U) }
                },
                michelineData {
                    DUP { arg(1UL) }
                },
                michelineInstruction { DUP("1") },
                michelineInstruction { DUP((1).toUByte()) },
                michelineInstruction { DUP((1).toUShort()) },
                michelineInstruction { DUP(1U) },
                michelineInstruction { DUP(1UL) },
                michelineInstruction {
                    DUP { arg("1") }
                },
                michelineInstruction {
                    DUP { arg((1).toUByte()) }
                },
                michelineInstruction {
                    DUP { arg((1).toUShort()) }
                },
                michelineInstruction {
                    DUP { arg(1U) }
                },
                michelineInstruction {
                    DUP { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication("SWAP") to listOf(
                micheline { SWAP },
                micheline { SWAP() },
                michelineData { SWAP },
                michelineData { SWAP() },
                michelineInstruction { SWAP },
                michelineInstruction { SWAP() },
            ),
            MichelinePrimitiveApplication("DIG") to listOf(
                micheline { DIG },
                micheline { DIG() },
                michelineData { DIG },
                michelineData { DIG() },
                michelineInstruction { DIG },
                michelineInstruction { DIG() },
            ),
            MichelinePrimitiveApplication(
                "DIG",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline { DIG("1") },
                micheline { DIG((1).toUByte()) },
                micheline { DIG((1).toUShort()) },
                micheline { DIG(1U) },
                micheline { DIG(1UL) },
                micheline {
                    DIG { arg("1") }
                },
                micheline {
                    DIG { arg((1).toUByte()) }
                },
                micheline {
                    DIG { arg((1).toUShort()) }
                },
                micheline {
                    DIG { arg(1U) }
                },
                micheline {
                    DIG { arg(1UL) }
                },
                michelineData { DIG("1") },
                michelineData { DIG((1).toUByte()) },
                michelineData { DIG((1).toUShort()) },
                michelineData { DIG(1U) },
                michelineData { DIG(1UL) },
                michelineData {
                    DIG { arg("1") }
                },
                michelineData {
                    DIG { arg((1).toUByte()) }
                },
                michelineData {
                    DIG { arg((1).toUShort()) }
                },
                michelineData {
                    DIG { arg(1U) }
                },
                michelineData {
                    DIG { arg(1UL) }
                },
                michelineInstruction { DIG("1") },
                michelineInstruction { DIG((1).toUByte()) },
                michelineInstruction { DIG((1).toUShort()) },
                michelineInstruction { DIG(1U) },
                michelineInstruction { DIG(1UL) },
                michelineInstruction {
                    DIG { arg("1") }
                },
                michelineInstruction {
                    DIG { arg((1).toUByte()) }
                },
                michelineInstruction {
                    DIG { arg((1).toUShort()) }
                },
                michelineInstruction {
                    DIG { arg(1U) }
                },
                michelineInstruction {
                    DIG { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication("DUG") to listOf(
                micheline { DUG },
                micheline { DUG() },
                michelineData { DUG },
                michelineData { DUG() },
                michelineInstruction { DUG },
                michelineInstruction { DUG() },
            ),
            MichelinePrimitiveApplication(
                "DUG",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline { DUG("1") },
                micheline { DUG((1).toUByte()) },
                micheline { DUG((1).toUShort()) },
                micheline { DUG(1U) },
                micheline { DUG(1UL) },
                micheline {
                    DUG { arg("1") }
                },
                micheline {
                    DUG { arg((1).toUByte()) }
                },
                micheline {
                    DUG { arg((1).toUShort()) }
                },
                micheline {
                    DUG { arg(1U) }
                },
                micheline {
                    DUG { arg(1UL) }
                },
                michelineData { DUG("1") },
                michelineData { DUG((1).toUByte()) },
                michelineData { DUG((1).toUShort()) },
                michelineData { DUG(1U) },
                michelineData { DUG(1UL) },
                michelineData {
                    DUG { arg("1") }
                },
                michelineData {
                    DUG { arg((1).toUByte()) }
                },
                michelineData {
                    DUG { arg((1).toUShort()) }
                },
                michelineData {
                    DUG { arg(1U) }
                },
                michelineData {
                    DUG { arg(1UL) }
                },
                michelineInstruction { DUG("1") },
                michelineInstruction { DUG((1).toUByte()) },
                michelineInstruction { DUG((1).toUShort()) },
                michelineInstruction { DUG(1U) },
                michelineInstruction { DUG(1UL) },
                michelineInstruction {
                    DUG { arg("1") }
                },
                michelineInstruction {
                    DUG { arg((1).toUByte()) }
                },
                michelineInstruction {
                    DUG { arg((1).toUShort()) }
                },
                michelineInstruction {
                    DUG { arg(1U) }
                },
                michelineInstruction {
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
                michelineData {
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
            ),
            MichelinePrimitiveApplication("SOME") to listOf(
                micheline { SOME },
                micheline { SOME() },
                michelineData { SOME },
                michelineData { SOME() },
                michelineInstruction { SOME },
                michelineInstruction { SOME() },
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
                micheline {
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
                michelineData {
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
                michelineInstruction {
                    NONE {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication("UNIT") to listOf(
                micheline { UNIT },
                micheline { UNIT() },
                michelineData { UNIT },
                michelineData { UNIT() },
                michelineInstruction { UNIT },
                michelineInstruction { UNIT() },
            ),
            MichelinePrimitiveApplication("NEVER") to listOf(
                micheline { NEVER },
                micheline { NEVER() },
                michelineData { NEVER },
                michelineData { NEVER() },
                michelineInstruction { NEVER },
                michelineInstruction { NEVER() },
            ),
            MichelinePrimitiveApplication(
                "IF_NONE",
                args = listOf(MichelinePrimitiveApplication("UNIT"), MichelinePrimitiveApplication("UNIT"))
            ) to listOf(
                micheline {
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
                michelineInstruction {
                    IF_NONE {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication("PAIR") to listOf(
                micheline { PAIR },
                micheline { PAIR() },
                michelineData { PAIR },
                michelineData { PAIR() },
                michelineInstruction { PAIR },
                michelineInstruction { PAIR() },
            ),
            MichelinePrimitiveApplication(
                "PAIR",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline { PAIR("1") },
                micheline { PAIR((1).toUByte()) },
                micheline { PAIR((1).toUShort()) },
                micheline { PAIR(1U) },
                micheline { PAIR(1UL) },
                micheline {
                    PAIR { arg("1") }
                },
                micheline {
                    PAIR { arg((1).toUByte()) }
                },
                micheline {
                    PAIR { arg((1).toUShort()) }
                },
                micheline {
                    PAIR { arg(1U) }
                },
                micheline {
                    PAIR { arg(1UL) }
                },
                michelineData { PAIR("1") },
                michelineData { PAIR((1).toUByte()) },
                michelineData { PAIR((1).toUShort()) },
                michelineData { PAIR(1U) },
                michelineData { PAIR(1UL) },
                michelineData {
                    PAIR { arg("1") }
                },
                michelineData {
                    PAIR { arg((1).toUByte()) }
                },
                michelineData {
                    PAIR { arg((1).toUShort()) }
                },
                michelineData {
                    PAIR { arg(1U) }
                },
                michelineData {
                    PAIR { arg(1UL) }
                },
                michelineInstruction { PAIR("1") },
                michelineInstruction { PAIR((1).toUByte()) },
                michelineInstruction { PAIR((1).toUShort()) },
                michelineInstruction { PAIR(1U) },
                michelineInstruction { PAIR(1UL) },
                michelineInstruction {
                    PAIR { arg("1") }
                },
                michelineInstruction {
                    PAIR { arg((1).toUByte()) }
                },
                michelineInstruction {
                    PAIR { arg((1).toUShort()) }
                },
                michelineInstruction {
                    PAIR { arg(1U) }
                },
                michelineInstruction {
                    PAIR { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication("CAR") to listOf(
                micheline { CAR },
                micheline { CAR() },
                michelineData { CAR },
                michelineData { CAR() },
                michelineInstruction { CAR },
                michelineInstruction { CAR() },
            ),
            MichelinePrimitiveApplication("CDR") to listOf(
                micheline { CDR },
                micheline { CDR() },
                michelineData { CDR },
                michelineData { CDR() },
                michelineInstruction { CDR },
                michelineInstruction { CDR() },
            ),
            MichelinePrimitiveApplication("UNPAIR") to listOf(
                micheline { UNPAIR },
                micheline { UNPAIR() },
                michelineData { UNPAIR },
                michelineData { UNPAIR() },
                michelineInstruction { UNPAIR },
                michelineInstruction { UNPAIR() },
            ),
            MichelinePrimitiveApplication(
                "UNPAIR",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline { UNPAIR("1") },
                micheline { UNPAIR((1).toUByte()) },
                micheline { UNPAIR((1).toUShort()) },
                micheline { UNPAIR(1U) },
                micheline { UNPAIR(1UL) },
                micheline {
                    UNPAIR { arg("1") }
                },
                micheline {
                    UNPAIR { arg((1).toUByte()) }
                },
                micheline {
                    UNPAIR { arg((1).toUShort()) }
                },
                micheline {
                    UNPAIR { arg(1U) }
                },
                micheline {
                    UNPAIR { arg(1UL) }
                },
                michelineData { UNPAIR("1") },
                michelineData { UNPAIR((1).toUByte()) },
                michelineData { UNPAIR((1).toUShort()) },
                michelineData { UNPAIR(1U) },
                michelineData { UNPAIR(1UL) },
                michelineData {
                    UNPAIR { arg("1") }
                },
                michelineData {
                    UNPAIR { arg((1).toUByte()) }
                },
                michelineData {
                    UNPAIR { arg((1).toUShort()) }
                },
                michelineData {
                    UNPAIR { arg(1U) }
                },
                michelineData {
                    UNPAIR { arg(1UL) }
                },
                michelineInstruction { UNPAIR("1") },
                michelineInstruction { UNPAIR((1).toUByte()) },
                michelineInstruction { UNPAIR((1).toUShort()) },
                michelineInstruction { UNPAIR(1U) },
                michelineInstruction { UNPAIR(1UL) },
                michelineInstruction {
                    UNPAIR { arg("1") }
                },
                michelineInstruction {
                    UNPAIR { arg((1).toUByte()) }
                },
                michelineInstruction {
                    UNPAIR { arg((1).toUShort()) }
                },
                michelineInstruction {
                    UNPAIR { arg(1U) }
                },
                michelineInstruction {
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
                micheline {
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
                michelineData {
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
                michelineInstruction {
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
                micheline {
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
                michelineData {
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
                michelineInstruction {
                    RIGHT {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "IF_LEFT",
                args = listOf(MichelinePrimitiveApplication("UNIT"), MichelinePrimitiveApplication("UNIT"))
            ) to listOf(
                micheline {
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
                michelineInstruction {
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
                micheline {
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
                michelineData {
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
                michelineInstruction {
                    NIL {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication("CONS") to listOf(
                micheline { CONS },
                micheline { CONS() },
                michelineData { CONS },
                michelineData { CONS() },
                michelineInstruction { CONS },
                michelineInstruction { CONS() },
            ),
            MichelinePrimitiveApplication(
                "IF_CONS",
                args = listOf(MichelinePrimitiveApplication("UNIT"), MichelinePrimitiveApplication("UNIT"))
            ) to listOf(
                micheline {
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
                michelineInstruction {
                    IF_CONS {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication("SIZE") to listOf(
                micheline { SIZE },
                micheline { SIZE() },
                michelineData { SIZE },
                michelineData { SIZE() },
                michelineInstruction { SIZE },
                michelineInstruction { SIZE() },
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
                micheline {
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
                michelineData {
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
                michelineInstruction {
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
                michelineData {
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
                michelineData {
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
            ),
            MichelinePrimitiveApplication(
                "MAP",
                args = listOf(MichelinePrimitiveApplication("UNIT"))
            ) to listOf(
                micheline {
                    MAP {
                        arg { UNIT }
                    }
                },
                micheline {
                    MAP {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineData {
                    MAP {
                        arg { UNIT }
                    }
                },
                michelineData {
                    MAP {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineInstruction {
                    MAP {
                        arg { UNIT }
                    }
                },
                michelineInstruction {
                    MAP {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "ITER",
                args = listOf(MichelinePrimitiveApplication("UNIT"))
            ) to listOf(
                micheline {
                    ITER {
                        arg { UNIT }
                    }
                },
                micheline {
                    ITER {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineData {
                    ITER {
                        arg { UNIT }
                    }
                },
                michelineData {
                    ITER {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineInstruction {
                    ITER {
                        arg { UNIT }
                    }
                },
                michelineInstruction {
                    ITER {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication("MEM") to listOf(
                micheline { MEM },
                micheline { MEM() },
                michelineData { MEM },
                michelineData { MEM() },
                michelineInstruction { MEM },
                michelineInstruction { MEM() },
            ),
            MichelinePrimitiveApplication("GET") to listOf(
                micheline { GET },
                micheline { GET() },
                michelineData { GET },
                michelineData { GET() },
                michelineInstruction { GET },
                michelineInstruction { GET() },
            ),
            MichelinePrimitiveApplication(
                "GET",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline { GET("1") },
                micheline { GET((1).toUByte()) },
                micheline { GET((1).toUShort()) },
                micheline { GET(1U) },
                micheline { GET(1UL) },
                micheline {
                    GET { arg("1") }
                },
                micheline {
                    GET { arg((1).toUByte()) }
                },
                micheline {
                    GET { arg((1).toUShort()) }
                },
                micheline {
                    GET { arg(1U) }
                },
                micheline {
                    GET { arg(1UL) }
                },
                michelineData { GET("1") },
                michelineData { GET((1).toUByte()) },
                michelineData { GET((1).toUShort()) },
                michelineData { GET(1U) },
                michelineData { GET(1UL) },
                michelineData {
                    GET { arg("1") }
                },
                michelineData {
                    GET { arg((1).toUByte()) }
                },
                michelineData {
                    GET { arg((1).toUShort()) }
                },
                michelineData {
                    GET { arg(1U) }
                },
                michelineData {
                    GET { arg(1UL) }
                },
                michelineInstruction { GET("1") },
                michelineInstruction { GET((1).toUByte()) },
                michelineInstruction { GET((1).toUShort()) },
                michelineInstruction { GET(1U) },
                michelineInstruction { GET(1UL) },
                michelineInstruction {
                    GET { arg("1") }
                },
                michelineInstruction {
                    GET { arg((1).toUByte()) }
                },
                michelineInstruction {
                    GET { arg((1).toUShort()) }
                },
                michelineInstruction {
                    GET { arg(1U) }
                },
                michelineInstruction {
                    GET { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication("UPDATE") to listOf(
                micheline { UPDATE },
                micheline { UPDATE() },
                michelineData { UPDATE },
                michelineData { UPDATE() },
                michelineInstruction { UPDATE },
                michelineInstruction { UPDATE() },
            ),
            MichelinePrimitiveApplication(
                "UPDATE",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline { UPDATE("1") },
                micheline { UPDATE((1).toUByte()) },
                micheline { UPDATE((1).toUShort()) },
                micheline { UPDATE(1U) },
                micheline { UPDATE(1UL) },
                micheline {
                    UPDATE { arg("1") }
                },
                micheline {
                    UPDATE { arg((1).toUByte()) }
                },
                micheline {
                    UPDATE { arg((1).toUShort()) }
                },
                micheline {
                    UPDATE { arg(1U) }
                },
                micheline {
                    UPDATE { arg(1UL) }
                },
                michelineData { UPDATE("1") },
                michelineData { UPDATE((1).toUByte()) },
                michelineData { UPDATE((1).toUShort()) },
                michelineData { UPDATE(1U) },
                michelineData { UPDATE(1UL) },
                michelineData {
                    UPDATE { arg("1") }
                },
                michelineData {
                    UPDATE { arg((1).toUByte()) }
                },
                michelineData {
                    UPDATE { arg((1).toUShort()) }
                },
                michelineData {
                    UPDATE { arg(1U) }
                },
                michelineData {
                    UPDATE { arg(1UL) }
                },
                michelineInstruction { UPDATE("1") },
                michelineInstruction { UPDATE((1).toUByte()) },
                michelineInstruction { UPDATE((1).toUShort()) },
                michelineInstruction { UPDATE(1U) },
                michelineInstruction { UPDATE(1UL) },
                michelineInstruction {
                    UPDATE { arg("1") }
                },
                michelineInstruction {
                    UPDATE { arg((1).toUByte()) }
                },
                michelineInstruction {
                    UPDATE { arg((1).toUShort()) }
                },
                michelineInstruction {
                    UPDATE { arg(1U) }
                },
                michelineInstruction {
                    UPDATE { arg(1UL) }
                },
            ),
            MichelinePrimitiveApplication(
                "IF",
                args = listOf(MichelinePrimitiveApplication("UNIT"), MichelinePrimitiveApplication("UNIT"))
            ) to listOf(
                micheline {
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
                michelineInstruction {
                    IF {
                        ifBranch { UNIT }
                        elseBranch { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "LOOP",
                args = listOf(MichelinePrimitiveApplication("UNIT"))
            ) to listOf(
                micheline {
                    LOOP {
                        arg { UNIT }
                    }
                },
                micheline {
                    LOOP {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineData {
                    LOOP {
                        arg { UNIT }
                    }
                },
                michelineData {
                    LOOP {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineInstruction {
                    LOOP {
                        arg { UNIT }
                    }
                },
                michelineInstruction {
                    LOOP {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "LOOP_LEFT",
                args = listOf(MichelinePrimitiveApplication("UNIT"))
            ) to listOf(
                micheline {
                    LOOP_LEFT {
                        arg { UNIT }
                    }
                },
                micheline {
                    LOOP_LEFT {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineData {
                    LOOP_LEFT {
                        arg { UNIT }
                    }
                },
                michelineData {
                    LOOP_LEFT {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
                michelineInstruction {
                    LOOP_LEFT {
                        arg { UNIT }
                    }
                },
                michelineInstruction {
                    LOOP_LEFT {
                        arg { UNIT }
                        arg { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "LAMBDA",
                args = listOf(
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("UNIT"),
                ),
            ) to listOf(
                micheline { 
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
                micheline {
                    LAMBDA {
                        parameter { unit }
                        returnType { unit }
                        body { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication("EXEC") to listOf(
                micheline { EXEC },
                micheline { EXEC() },
                michelineData { EXEC },
                michelineData { EXEC() },
                michelineInstruction { EXEC },
                michelineInstruction { EXEC() },
            ),
            MichelinePrimitiveApplication("APPLY") to listOf(
                micheline { APPLY },
                micheline { APPLY() },
                michelineData { APPLY },
                michelineData { APPLY() },
                michelineInstruction { APPLY },
                michelineInstruction { APPLY() },
            ),
            MichelinePrimitiveApplication(
                "DIP",
                args = listOf(MichelinePrimitiveApplication("UNIT")),
            ) to listOf(
                micheline {
                    DIP {
                        instruction { UNIT }
                    }
                },
                michelineData {
                    DIP {
                        instruction { UNIT }
                    }
                },
                michelineInstruction {
                    DIP {
                        instruction { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "DIP",
                args = listOf(MichelineLiteral.Integer(1), MichelinePrimitiveApplication("UNIT")),
            ) to listOf(
                micheline {
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
                micheline {
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
                micheline {
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
                michelineData {
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
                michelineData {
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
                michelineInstruction {
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
                michelineInstruction {
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
                michelineInstruction {
                    DIP {
                        n(1UL)
                        instruction { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication("FAILWITH") to listOf(
                micheline { FAILWITH },
                micheline { FAILWITH() },
                michelineData { FAILWITH },
                michelineData { FAILWITH() },
                michelineInstruction { FAILWITH },
                michelineInstruction { FAILWITH() },
            ),
            MichelinePrimitiveApplication("CAST") to listOf(
                micheline { CAST },
                micheline { CAST() },
                michelineData { CAST },
                michelineData { CAST() },
                michelineInstruction { CAST },
                michelineInstruction { CAST() },
            ),
            MichelinePrimitiveApplication("RENAME") to listOf(
                micheline { RENAME },
                micheline { RENAME() },
                michelineData { RENAME },
                michelineData { RENAME() },
                michelineInstruction { RENAME },
                michelineInstruction { RENAME() },
            ),
            MichelinePrimitiveApplication("CONCAT") to listOf(
                micheline { CONCAT },
                micheline { CONCAT() },
                michelineData { CONCAT },
                michelineData { CONCAT() },
                michelineInstruction { CONCAT },
                michelineInstruction { CONCAT() },
            ),
            MichelinePrimitiveApplication("SLICE") to listOf(
                micheline { SLICE },
                micheline { SLICE() },
                michelineData { SLICE },
                michelineData { SLICE() },
                michelineInstruction { SLICE },
                michelineInstruction { SLICE() },
            ),
            MichelinePrimitiveApplication("PACK") to listOf(
                micheline { PACK },
                micheline { PACK() },
                michelineData { PACK },
                michelineData { PACK() },
                michelineInstruction { PACK },
                michelineInstruction { PACK() },
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
                micheline {
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
                michelineData {
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
                michelineInstruction {
                    UNPACK {
                        arg { unit }
                        arg { unit }
                    }
                },
            ),
            MichelinePrimitiveApplication("ADD") to listOf(
                micheline { ADD },
                micheline { ADD() },
                michelineData { ADD },
                michelineData { ADD() },
                michelineInstruction { ADD },
                michelineInstruction { ADD() },
            ),
            MichelinePrimitiveApplication("SUB") to listOf(
                micheline { SUB },
                micheline { SUB() },
                michelineData { SUB },
                michelineData { SUB() },
                michelineInstruction { SUB },
                michelineInstruction { SUB() },
            ),
            MichelinePrimitiveApplication("MUL") to listOf(
                micheline { MUL },
                micheline { MUL() },
                michelineData { MUL },
                michelineData { MUL() },
                michelineInstruction { MUL },
                michelineInstruction { MUL() },
            ),
            MichelinePrimitiveApplication("EDIV") to listOf(
                micheline { EDIV },
                micheline { EDIV() },
                michelineData { EDIV },
                michelineData { EDIV() },
                michelineInstruction { EDIV },
                michelineInstruction { EDIV() },
            ),
            MichelinePrimitiveApplication("ABS") to listOf(
                micheline { ABS },
                micheline { ABS() },
                michelineData { ABS },
                michelineData { ABS() },
                michelineInstruction { ABS },
                michelineInstruction { ABS() },
            ),
            MichelinePrimitiveApplication("ISNAT") to listOf(
                micheline { ISNAT },
                micheline { ISNAT() },
                michelineData { ISNAT },
                michelineData { ISNAT() },
                michelineInstruction { ISNAT },
                michelineInstruction { ISNAT() },
            ),
            MichelinePrimitiveApplication("INT") to listOf(
                micheline { INT },
                micheline { INT() },
                michelineData { INT },
                michelineData { INT() },
                michelineInstruction { INT },
                michelineInstruction { INT() },
            ),
            MichelinePrimitiveApplication("NEG") to listOf(
                micheline { NEG },
                micheline { NEG() },
                michelineData { NEG },
                michelineData { NEG() },
                michelineInstruction { NEG },
                michelineInstruction { NEG() },
            ),
            MichelinePrimitiveApplication("LSL") to listOf(
                micheline { LSL },
                micheline { LSL() },
                michelineData { LSL },
                michelineData { LSL() },
                michelineInstruction { LSL },
                michelineInstruction { LSL() },
            ),
            MichelinePrimitiveApplication("LSR") to listOf(
                micheline { LSR },
                micheline { LSR() },
                michelineData { LSR },
                michelineData { LSR() },
                michelineInstruction { LSR },
                michelineInstruction { LSR() },
            ),
            MichelinePrimitiveApplication("OR") to listOf(
                micheline { OR },
                micheline { OR() },
                michelineData { OR },
                michelineData { OR() },
                michelineInstruction { OR },
                michelineInstruction { OR() },
            ),
            MichelinePrimitiveApplication("AND") to listOf(
                micheline { AND },
                micheline { AND() },
                michelineData { AND },
                michelineData { AND() },
                michelineInstruction { AND },
                michelineInstruction { AND() },
            ),
            MichelinePrimitiveApplication("XOR") to listOf(
                micheline { XOR },
                micheline { XOR() },
                michelineData { XOR },
                michelineData { XOR() },
                michelineInstruction { XOR },
                michelineInstruction { XOR() },
            ),
            MichelinePrimitiveApplication("NOT") to listOf(
                micheline { NOT },
                micheline { NOT() },
                michelineData { NOT },
                michelineData { NOT() },
                michelineInstruction { NOT },
                michelineInstruction { NOT() },
            ),
            MichelinePrimitiveApplication("COMPARE") to listOf(
                micheline { COMPARE },
                micheline { COMPARE() },
                michelineData { COMPARE },
                michelineData { COMPARE() },
                michelineInstruction { COMPARE },
                michelineInstruction { COMPARE() },
            ),
            MichelinePrimitiveApplication("EQ") to listOf(
                micheline { EQ },
                micheline { EQ() },
                michelineData { EQ },
                michelineData { EQ() },
                michelineInstruction { EQ },
                michelineInstruction { EQ() },
            ),
            MichelinePrimitiveApplication("NEQ") to listOf(
                micheline { NEQ },
                micheline { NEQ() },
                michelineData { NEQ },
                michelineData { NEQ() },
                michelineInstruction { NEQ },
                michelineInstruction { NEQ() },
            ),
            MichelinePrimitiveApplication("LT") to listOf(
                micheline { LT },
                micheline { LT() },
                michelineData { LT },
                michelineData { LT() },
                michelineInstruction { LT },
                michelineInstruction { LT() },
            ),
            MichelinePrimitiveApplication("GT") to listOf(
                micheline { GT },
                micheline { GT() },
                michelineData { GT },
                michelineData { GT() },
                michelineInstruction { GT },
                michelineInstruction { GT() },
            ),
            MichelinePrimitiveApplication("LE") to listOf(
                micheline { LE },
                micheline { LE() },
                michelineData { LE },
                michelineData { LE() },
                michelineInstruction { LE },
                michelineInstruction { LE() },
            ),
            MichelinePrimitiveApplication("GE") to listOf(
                micheline { GE },
                micheline { GE() },
                michelineData { GE },
                michelineData { GE() },
                michelineInstruction { GE },
                michelineInstruction { GE() },
            ),
            MichelinePrimitiveApplication("SELF") to listOf(
                micheline { SELF },
                micheline { SELF() },
                michelineData { SELF },
                michelineData { SELF() },
                michelineInstruction { SELF },
                michelineInstruction { SELF() },
            ),
            MichelinePrimitiveApplication("SELF_ADDRESS") to listOf(
                micheline { SELF_ADDRESS },
                micheline { SELF_ADDRESS() },
                michelineData { SELF_ADDRESS },
                michelineData { SELF_ADDRESS() },
                michelineInstruction { SELF_ADDRESS },
                michelineInstruction { SELF_ADDRESS() },
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
                micheline {
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
                michelineData {
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
                michelineInstruction {
                    CONTRACT {
                        arg { unit }
                        arg { unit }
                    }
                },
            ),
            MichelinePrimitiveApplication("TRANSFER_TOKENS") to listOf(
                micheline { TRANSFER_TOKENS },
                micheline { TRANSFER_TOKENS() },
                michelineData { TRANSFER_TOKENS },
                michelineData { TRANSFER_TOKENS() },
                michelineInstruction { TRANSFER_TOKENS },
                michelineInstruction { TRANSFER_TOKENS() },
            ),
            MichelinePrimitiveApplication("SET_DELEGATE") to listOf(
                micheline { SET_DELEGATE },
                micheline { SET_DELEGATE() },
                michelineData { SET_DELEGATE },
                michelineData { SET_DELEGATE() },
                michelineInstruction { SET_DELEGATE },
                michelineInstruction { SET_DELEGATE() },
            ),
            MichelinePrimitiveApplication(
                "CREATE_CONTRACT",
                args = listOf(
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("unit"),
                    MichelinePrimitiveApplication("UNIT"),
                ),
            ) to listOf(
                micheline {
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
                michelineInstruction {
                    CREATE_CONTRACT {
                        parameter { unit }
                        storage { unit }
                        code { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication("IMPLICIT_ACCOUNT") to listOf(
                micheline { IMPLICIT_ACCOUNT },
                micheline { IMPLICIT_ACCOUNT() },
                michelineData { IMPLICIT_ACCOUNT },
                michelineData { IMPLICIT_ACCOUNT() },
                michelineInstruction { IMPLICIT_ACCOUNT },
                michelineInstruction { IMPLICIT_ACCOUNT() },
            ),
            MichelinePrimitiveApplication("VOTING_POWER") to listOf(
                micheline { VOTING_POWER },
                micheline { VOTING_POWER() },
                michelineData { VOTING_POWER },
                michelineData { VOTING_POWER() },
                michelineInstruction { VOTING_POWER },
                michelineInstruction { VOTING_POWER() },
            ),
            MichelinePrimitiveApplication("NOW") to listOf(
                micheline { NOW },
                micheline { NOW() },
                michelineData { NOW },
                michelineData { NOW() },
                michelineInstruction { NOW },
                michelineInstruction { NOW() },
            ),
            MichelinePrimitiveApplication("LEVEL") to listOf(
                micheline { LEVEL },
                micheline { LEVEL() },
                michelineData { LEVEL },
                michelineData { LEVEL() },
                michelineInstruction { LEVEL },
                michelineInstruction { LEVEL() },
            ),
            MichelinePrimitiveApplication("AMOUNT") to listOf(
                micheline { AMOUNT },
                micheline { AMOUNT() },
                michelineData { AMOUNT },
                michelineData { AMOUNT() },
                michelineInstruction { AMOUNT },
                michelineInstruction { AMOUNT() },
            ),
            MichelinePrimitiveApplication("BALANCE") to listOf(
                micheline { BALANCE },
                micheline { BALANCE() },
                michelineData { BALANCE },
                michelineData { BALANCE() },
                michelineInstruction { BALANCE },
                michelineInstruction { BALANCE() },
            ),
            MichelinePrimitiveApplication("CHECK_SIGNATURE") to listOf(
                micheline { CHECK_SIGNATURE },
                micheline { CHECK_SIGNATURE() },
                michelineData { CHECK_SIGNATURE },
                michelineData { CHECK_SIGNATURE() },
                michelineInstruction { CHECK_SIGNATURE },
                michelineInstruction { CHECK_SIGNATURE() },
            ),
            MichelinePrimitiveApplication("BLAKE2B") to listOf(
                micheline { BLAKE2B },
                micheline { BLAKE2B() },
                michelineData { BLAKE2B },
                michelineData { BLAKE2B() },
                michelineInstruction { BLAKE2B },
                michelineInstruction { BLAKE2B() },
            ),
            MichelinePrimitiveApplication("KECCAK") to listOf(
                micheline { KECCAK },
                micheline { KECCAK() },
                michelineData { KECCAK },
                michelineData { KECCAK() },
                michelineInstruction { KECCAK },
                michelineInstruction { KECCAK() },
            ),
            MichelinePrimitiveApplication("SHA3") to listOf(
                micheline { SHA3 },
                micheline { SHA3() },
                michelineData { SHA3 },
                michelineData { SHA3() },
                michelineInstruction { SHA3 },
                michelineInstruction { SHA3() },
            ),
            MichelinePrimitiveApplication("SHA256") to listOf(
                micheline { SHA256 },
                micheline { SHA256() },
                michelineData { SHA256 },
                michelineData { SHA256() },
                michelineInstruction { SHA256 },
                michelineInstruction { SHA256() },
            ),
            MichelinePrimitiveApplication("SHA512") to listOf(
                micheline { SHA512 },
                micheline { SHA512() },
                michelineData { SHA512 },
                michelineData { SHA512() },
                michelineInstruction { SHA512 },
                michelineInstruction { SHA512() },
            ),
            MichelinePrimitiveApplication("HASH_KEY") to listOf(
                micheline { HASH_KEY },
                micheline { HASH_KEY() },
                michelineData { HASH_KEY },
                michelineData { HASH_KEY() },
                michelineInstruction { HASH_KEY },
                michelineInstruction { HASH_KEY() },
            ),
            MichelinePrimitiveApplication("SOURCE") to listOf(
                micheline { SOURCE },
                micheline { SOURCE() },
                michelineData { SOURCE },
                michelineData { SOURCE() },
                michelineInstruction { SOURCE },
                michelineInstruction { SOURCE() },
            ),
            MichelinePrimitiveApplication("SENDER") to listOf(
                micheline { SENDER },
                micheline { SENDER() },
                michelineData { SENDER },
                michelineData { SENDER() },
                michelineInstruction { SENDER },
                michelineInstruction { SENDER() },
            ),
            MichelinePrimitiveApplication("ADDRESS") to listOf(
                micheline { ADDRESS },
                micheline { ADDRESS() },
                michelineData { ADDRESS },
                michelineData { ADDRESS() },
                michelineInstruction { ADDRESS },
                michelineInstruction { ADDRESS() },
            ),
            MichelinePrimitiveApplication("CHAIN_ID") to listOf(
                micheline { CHAIN_ID },
                micheline { CHAIN_ID() },
                michelineData { CHAIN_ID },
                michelineData { CHAIN_ID() },
                michelineInstruction { CHAIN_ID },
                michelineInstruction { CHAIN_ID() },
            ),
            MichelinePrimitiveApplication("TOTAL_VOTING_POWER") to listOf(
                micheline { TOTAL_VOTING_POWER },
                micheline { TOTAL_VOTING_POWER() },
                michelineData { TOTAL_VOTING_POWER },
                michelineData { TOTAL_VOTING_POWER() },
                michelineInstruction { TOTAL_VOTING_POWER },
                michelineInstruction { TOTAL_VOTING_POWER() },
            ),
            MichelinePrimitiveApplication("PAIRING_CHECK") to listOf(
                micheline { PAIRING_CHECK },
                micheline { PAIRING_CHECK() },
                michelineData { PAIRING_CHECK },
                michelineData { PAIRING_CHECK() },
                michelineInstruction { PAIRING_CHECK },
                michelineInstruction { PAIRING_CHECK() },
            ),
            MichelinePrimitiveApplication(
                "SAPLING_EMPTY_STATE",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline { SAPLING_EMPTY_STATE("1") },
                micheline { SAPLING_EMPTY_STATE((1).toUByte()) },
                micheline { SAPLING_EMPTY_STATE((1).toUShort()) },
                micheline { SAPLING_EMPTY_STATE(1U) },
                micheline { SAPLING_EMPTY_STATE(1UL) },
                micheline { SAPLING_EMPTY_STATE("1") },
                michelineData { SAPLING_EMPTY_STATE((1).toUByte()) },
                michelineData { SAPLING_EMPTY_STATE((1).toUShort()) },
                michelineData { SAPLING_EMPTY_STATE(1U) },
                michelineData { SAPLING_EMPTY_STATE(1UL) },
                micheline { SAPLING_EMPTY_STATE("1") },
                michelineInstruction { SAPLING_EMPTY_STATE((1).toUByte()) },
                michelineInstruction { SAPLING_EMPTY_STATE((1).toUShort()) },
                michelineInstruction { SAPLING_EMPTY_STATE(1U) },
                michelineInstruction { SAPLING_EMPTY_STATE(1UL) },
            ),
            MichelinePrimitiveApplication("SAPLING_VERIFY_UPDATE") to listOf(
                micheline { SAPLING_VERIFY_UPDATE },
                micheline { SAPLING_VERIFY_UPDATE() },
                michelineData { SAPLING_VERIFY_UPDATE },
                michelineData { SAPLING_VERIFY_UPDATE() },
                michelineInstruction { SAPLING_VERIFY_UPDATE },
                michelineInstruction { SAPLING_VERIFY_UPDATE() },
            ),
            MichelinePrimitiveApplication("TICKET") to listOf(
                micheline { TICKET },
                micheline { TICKET() },
                michelineData { TICKET },
                michelineData { TICKET() },
                michelineInstruction { TICKET },
                michelineInstruction { TICKET() },
            ),
            MichelinePrimitiveApplication("READ_TICKET") to listOf(
                micheline { READ_TICKET },
                micheline { READ_TICKET() },
                michelineData { READ_TICKET },
                michelineData { READ_TICKET() },
                michelineInstruction { READ_TICKET },
                michelineInstruction { READ_TICKET() },
            ),
            MichelinePrimitiveApplication("SPLIT_TICKET") to listOf(
                micheline { SPLIT_TICKET },
                micheline { SPLIT_TICKET() },
                michelineData { SPLIT_TICKET },
                michelineData { SPLIT_TICKET() },
                michelineInstruction { SPLIT_TICKET },
                michelineInstruction { SPLIT_TICKET() },
            ),
            MichelinePrimitiveApplication("JOIN_TICKETS") to listOf(
                micheline { JOIN_TICKETS },
                micheline { JOIN_TICKETS() },
                michelineData { JOIN_TICKETS },
                michelineData { JOIN_TICKETS() },
                michelineInstruction { JOIN_TICKETS },
                michelineInstruction { JOIN_TICKETS() },
            ),
            MichelinePrimitiveApplication("OPEN_CHEST") to listOf(
                micheline { OPEN_CHEST },
                micheline { OPEN_CHEST() },
                michelineData { OPEN_CHEST },
                michelineData { OPEN_CHEST() },
                michelineInstruction { OPEN_CHEST },
                michelineInstruction { OPEN_CHEST() },
            ),
        )

        expectedWithActual.forEach { (expected, actual) ->
            actual.forEach { assertEquals(expected, it) }
        }
    }
}