package it.airgap.tezos.michelson.micheline.dsl

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.michelson.internal.converter.MichelsonToMichelineConverter
import it.airgap.tezos.michelson.internal.di.ScopedDependencyRegistry
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MichelineMichelsonTypeDslTest {

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
    fun `builds Micheline Michelson Type Expression`() {
        val expectedWithActual = listOf(
            MichelinePrimitiveApplication(
                "parameter",
                args = listOf(MichelinePrimitiveApplication("unit"))
            ) to listOf(
                micheline {
                    parameter {
                        arg { unit }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    parameter {
                        arg { unit }
                    }
                },
                michelineType {
                    parameter {
                        arg { unit }
                    }
                },
                michelineType(michelsonToMichelineConverter) {
                    parameter {
                        arg { unit }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "storage",
                args = listOf(MichelinePrimitiveApplication("unit"))
            ) to listOf(
                micheline {
                    storage {
                        arg { unit }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    storage {
                        arg { unit }
                    }
                },
                michelineType {
                    storage {
                        arg { unit }
                    }
                },
                michelineType(michelsonToMichelineConverter) {
                    storage {
                        arg { unit }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "code",
                args = listOf(MichelinePrimitiveApplication("UNIT"))
            ) to listOf(
                micheline {
                    code {
                        arg { UNIT }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    code {
                        arg { UNIT }
                    }
                },
                michelineType {
                    code {
                        arg { UNIT }
                    }
                },
                michelineType(michelsonToMichelineConverter) {
                    code {
                        arg { UNIT }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "option",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to listOf(
                micheline {
                    option {
                        arg { unit }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    option {
                        arg { unit }
                    }
                },
                michelineType {
                    option {
                        arg { unit }
                    }
                },
                michelineType(michelsonToMichelineConverter) {
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
                micheline(michelsonToMichelineConverter) {
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
                micheline(michelsonToMichelineConverter) {
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
                michelineType(michelsonToMichelineConverter) {
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
                michelineType(michelsonToMichelineConverter) {
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
                micheline(michelsonToMichelineConverter) {
                    set {
                        arg { unit }
                    }
                },
                micheline {
                    set {
                        arg { unit }
                    }
                },
                micheline(michelsonToMichelineConverter) {
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
                michelineType(michelsonToMichelineConverter) {
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
                michelineType(michelsonToMichelineConverter) {
                    set {
                        arg { unit }
                        arg { unit }
                    }
                },
            ),
            MichelinePrimitiveApplication("operation") to listOf(
                micheline { operation },
                micheline(michelsonToMichelineConverter) { operation },
                micheline { operation() },
                micheline(michelsonToMichelineConverter) { operation() },
                michelineType { operation },
                michelineType(michelsonToMichelineConverter) { operation },
                michelineType { operation() },
                michelineType(michelsonToMichelineConverter) { operation() },
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
                micheline(michelsonToMichelineConverter) {
                    contract {
                        arg { unit }
                    }
                },
                michelineType {
                    contract {
                        arg { unit }
                    }
                },
                michelineType(michelsonToMichelineConverter) {
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
                micheline(michelsonToMichelineConverter) {
                    ticket {
                        arg { unit }
                    }
                },
                michelineType {
                    ticket {
                        arg { unit }
                    }
                },
                michelineType(michelsonToMichelineConverter) {
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
                micheline(michelsonToMichelineConverter) {
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
                michelineType(michelsonToMichelineConverter) {
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
                micheline(michelsonToMichelineConverter) {
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
                michelineType(michelsonToMichelineConverter) {
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
                micheline(michelsonToMichelineConverter) {
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
                michelineType(michelsonToMichelineConverter) {
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
                micheline(michelsonToMichelineConverter) {
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
                michelineType(michelsonToMichelineConverter) {
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
                micheline(michelsonToMichelineConverter) {
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
                michelineType(michelsonToMichelineConverter) {
                    bigMap {
                        key { unit }
                        value { bool }
                    }
                },
            ),
            MichelinePrimitiveApplication("bls12_381_g1") to listOf(
                micheline { bls12_381G1 },
                micheline(michelsonToMichelineConverter) { bls12_381G1 },
                micheline { bls12_381G1() },
                micheline(michelsonToMichelineConverter) { bls12_381G1() },
                michelineType { bls12_381G1 },
                michelineType(michelsonToMichelineConverter) { bls12_381G1 },
                michelineType { bls12_381G1() },
                michelineType(michelsonToMichelineConverter) { bls12_381G1() },
            ),
            MichelinePrimitiveApplication("bls12_381_g2") to listOf(
                micheline { bls12_381G2 },
                micheline(michelsonToMichelineConverter) { bls12_381G2 },
                micheline { bls12_381G2() },
                micheline(michelsonToMichelineConverter) { bls12_381G2() },
                michelineType { bls12_381G2 },
                michelineType(michelsonToMichelineConverter) { bls12_381G2 },
                michelineType { bls12_381G2() },
                michelineType(michelsonToMichelineConverter) { bls12_381G2() },
            ),
            MichelinePrimitiveApplication("bls12_381_fr") to listOf(
                micheline { bls12_381Fr },
                micheline(michelsonToMichelineConverter) { bls12_381Fr },
                micheline { bls12_381Fr() },
                micheline(michelsonToMichelineConverter) { bls12_381Fr() },
                michelineType { bls12_381Fr },
                michelineType(michelsonToMichelineConverter) { bls12_381Fr },
                michelineType { bls12_381Fr() },
                michelineType(michelsonToMichelineConverter) { bls12_381Fr() },
            ),
            MichelinePrimitiveApplication(
                "sapling_transaction",
                args = listOf(MichelineLiteral.Integer(1)),
            ) to listOf(
                micheline {
                    saplingTransaction("1")
                },
                micheline(michelsonToMichelineConverter) {
                    saplingTransaction("1")
                },
                micheline {
                    saplingTransaction((1).toUByte())
                },
                micheline(michelsonToMichelineConverter) {
                    saplingTransaction((1).toUByte())
                },
                micheline {
                    saplingTransaction((1).toUShort())
                },
                micheline(michelsonToMichelineConverter) {
                    saplingTransaction((1).toUShort())
                },
                micheline {
                    saplingTransaction(1U)
                },
                micheline(michelsonToMichelineConverter) {
                    saplingTransaction(1U)
                },
                micheline {
                    saplingTransaction(1UL)
                },
                micheline(michelsonToMichelineConverter) {
                    saplingTransaction(1UL)
                },
                michelineType {
                    saplingTransaction("1")
                },
                michelineType(michelsonToMichelineConverter) {
                    saplingTransaction("1")
                },
                michelineType {
                    saplingTransaction((1).toUByte())
                },
                michelineType(michelsonToMichelineConverter) {
                    saplingTransaction((1).toUByte())
                },
                michelineType {
                    saplingTransaction((1).toUShort())
                },
                michelineType(michelsonToMichelineConverter) {
                    saplingTransaction((1).toUShort())
                },
                michelineType {
                    saplingTransaction(1U)
                },
                michelineType(michelsonToMichelineConverter) {
                    saplingTransaction(1U)
                },
                michelineType {
                    saplingTransaction(1UL)
                },
                michelineType(michelsonToMichelineConverter) {
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
                micheline(michelsonToMichelineConverter) {
                    saplingState("1")
                },
                micheline {
                    saplingState((1).toUByte())
                },
                micheline(michelsonToMichelineConverter) {
                    saplingState((1).toUByte())
                },
                micheline {
                    saplingState((1).toUShort())
                },
                micheline(michelsonToMichelineConverter) {
                    saplingState((1).toUShort())
                },
                micheline {
                    saplingState(1U)
                },
                micheline(michelsonToMichelineConverter) {
                    saplingState(1U)
                },
                micheline {
                    saplingState(1UL)
                },
                micheline(michelsonToMichelineConverter) {
                    saplingState(1UL)
                },
                michelineType {
                    saplingState("1")
                },
                michelineType(michelsonToMichelineConverter) {
                    saplingState("1")
                },
                michelineType {
                    saplingState((1).toUByte())
                },
                michelineType(michelsonToMichelineConverter) {
                    saplingState((1).toUByte())
                },
                michelineType {
                    saplingState((1).toUShort())
                },
                michelineType(michelsonToMichelineConverter) {
                    saplingState((1).toUShort())
                },
                michelineType {
                    saplingState(1U)
                },
                michelineType(michelsonToMichelineConverter) {
                    saplingState(1U)
                },
                michelineType {
                    saplingState(1UL)
                },
                michelineType(michelsonToMichelineConverter) {
                    saplingState(1UL)
                },
            ),
            MichelinePrimitiveApplication("chest") to listOf(
                micheline { chest },
                micheline(michelsonToMichelineConverter) { chest },
                micheline { chest() },
                micheline(michelsonToMichelineConverter) { chest() },
                michelineType { chest },
                michelineType(michelsonToMichelineConverter) { chest },
                michelineType { chest() },
                michelineType(michelsonToMichelineConverter) { chest() },
            ),
            MichelinePrimitiveApplication("chest_key") to listOf(
                micheline { chestKey },
                micheline(michelsonToMichelineConverter) { chestKey },
                micheline { chestKey() },
                micheline(michelsonToMichelineConverter) { chestKey() },
                michelineType { chestKey },
                michelineType(michelsonToMichelineConverter) { chestKey },
                michelineType { chestKey() },
                michelineType(michelsonToMichelineConverter) { chestKey() },
            ),
        )

        expectedWithActual.forEach { (expected, actual) ->
            actual.forEach { assertEquals(expected, it) }
        }
    }
}