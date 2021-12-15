package it.airgap.tezos.michelson.micheline.dsl

import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import org.junit.Test
import kotlin.test.assertEquals

class MichelineMichelsonTypeDslTest {

    @Test
    fun `builds Micheline Michelson Type Expression`() {
        val expectedWithActual = listOf(
            MichelinePrimitiveApplication(
                "option",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to listOf(
                micheline {
                    option {
                        arg { unit }
                    }
                },
                michelineType {
                    option {
                        arg { unit }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "list",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to listOf(
                micheline {
                    list {
                        arg { unit }
                    }
                },
                micheline {
                    list {
                        arg { unit }
                        arg { unit }
                    }
                },
                michelineType {
                    list {
                        arg { unit }
                    }
                },
                michelineType {
                    list {
                        arg { unit }
                        arg { unit }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "set",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to listOf(
                micheline {
                    set {
                        arg { unit }
                    }
                },
                micheline {
                    set {
                        arg { unit }
                    }
                },
                michelineType {
                    set {
                        arg { unit }
                        arg { unit }
                    }
                },
                michelineType {
                    set {
                        arg { unit }
                        arg { unit }
                    }
                },
            ),
            MichelinePrimitiveApplication("operation") to listOf(
                micheline { operation },
                micheline { operation() },
                michelineType { operation },
                michelineType { operation() },
            ),
            MichelinePrimitiveApplication(
                "contract",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to listOf(
                micheline {
                    contract {
                        arg { unit }
                    }
                },
                michelineType {
                    contract {
                        arg { unit }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "ticket",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to listOf(
                micheline {
                    ticket {
                        arg { unit }
                    }
                },
                michelineType {
                    ticket {
                        arg { unit }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "pair",
                args = listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("bool")),
            ) to listOf(
                micheline {
                    pair {
                        arg { unit }
                        arg { bool }
                    }
                },
                michelineType {
                    pair {
                        arg { unit }
                        arg { bool }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "or",
                args = listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("bool")),
            ) to listOf(
                micheline {
                    or {
                        lhs { unit }
                        rhs { bool }
                    }
                },
                michelineType {
                    or {
                        lhs { unit }
                        rhs { bool }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "lambda",
                args = listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("bool")),
            ) to listOf(
                micheline {
                    lambda {
                        parameter { unit }
                        returnType { bool }
                    }
                },
                michelineType {
                    lambda {
                        parameter { unit }
                        returnType { bool }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "map",
                args = listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("bool")),
            ) to listOf(
                micheline {
                    map {
                        key { unit }
                        value { bool }
                    }
                },
                michelineType {
                    map {
                        key { unit }
                        value { bool }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "big_map",
                args = listOf(MichelinePrimitiveApplication("unit"), MichelinePrimitiveApplication("bool")),
            ) to listOf(
                micheline {
                    bigMap {
                        key { unit }
                        value { bool }
                    }
                },
                michelineType {
                    bigMap {
                        key { unit }
                        value { bool }
                    }
                },
            ),
            MichelinePrimitiveApplication("bls12_381_g1") to listOf(
                micheline { bls12_381G1 },
                micheline { bls12_381G1() },
                michelineType { bls12_381G1 },
                michelineType { bls12_381G1() },
            ),
            MichelinePrimitiveApplication("bls12_381_g2") to listOf(
                micheline { bls12_381G2 },
                micheline { bls12_381G2() },
                michelineType { bls12_381G2 },
                michelineType { bls12_381G2() },
            ),
            MichelinePrimitiveApplication("bls12_381_fr") to listOf(
                micheline { bls12_381Fr },
                micheline { bls12_381Fr() },
                michelineType { bls12_381Fr },
                michelineType { bls12_381Fr() },
            ),
            MichelinePrimitiveApplication(
                "sapling_transaction",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline {
                    saplingTransaction("1")
                },
                micheline {
                    saplingTransaction((1).toUByte())
                },
                micheline {
                    saplingTransaction((1).toUShort())
                },
                micheline {
                    saplingTransaction(1U)
                },
                micheline {
                    saplingTransaction(1UL)
                },
                michelineType {
                    saplingTransaction("1")
                },
                michelineType {
                    saplingTransaction((1).toUByte())
                },
                michelineType {
                    saplingTransaction((1).toUShort())
                },
                michelineType {
                    saplingTransaction(1U)
                },
                michelineType {
                    saplingTransaction(1UL)
                },
            ),
            MichelinePrimitiveApplication(
                "sapling_state",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline {
                    saplingState("1")
                },
                micheline {
                    saplingState((1).toUByte())
                },
                micheline {
                    saplingState((1).toUShort())
                },
                micheline {
                    saplingState(1U)
                },
                micheline {
                    saplingState(1UL)
                },
                michelineType {
                    saplingState("1")
                },
                michelineType {
                    saplingState((1).toUByte())
                },
                michelineType {
                    saplingState((1).toUShort())
                },
                michelineType {
                    saplingState(1U)
                },
                michelineType {
                    saplingState(1UL)
                },
            ),
            MichelinePrimitiveApplication("chest") to listOf(
                micheline { chest },
                micheline { chest() },
                michelineType { chest },
                michelineType { chest() },
            ),
            MichelinePrimitiveApplication("chest_key") to listOf(
                micheline { chestKey },
                micheline { chestKey() },
                michelineType { chestKey },
                michelineType { chestKey() },
            ),
        )

        expectedWithActual.forEach { (expected, actual) ->
            actual.forEach { assertEquals(expected, it) }
        }
    }
}