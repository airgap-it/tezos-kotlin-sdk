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

class MichelineMichelsonComparableTypeDslTest {

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
    fun `builds Micheline Michelson Comparable Type Expression`() {
        val expectedWithActual = listOf(
            MichelinePrimitiveApplication("unit") to listOf(
                micheline(tezos) { unit },
                micheline(michelsonToMichelineConverter) { unit },
                micheline(tezos) { unit() },
                micheline(michelsonToMichelineConverter) { unit() },
                michelineType(tezos) { unit },
                michelineType(michelsonToMichelineConverter) { unit },
                michelineType(tezos) { unit() },
                michelineType(michelsonToMichelineConverter) { unit() },
                michelineComparableType(tezos) { unit },
                michelineComparableType(michelsonToMichelineConverter) { unit },
                michelineComparableType(tezos) { unit() },
                michelineComparableType(michelsonToMichelineConverter) { unit() },
            ),
            MichelinePrimitiveApplication("never") to listOf(
                micheline(tezos) { never },
                micheline(michelsonToMichelineConverter) { never },
                micheline(tezos) { never() },
                micheline(michelsonToMichelineConverter) { never() },
                michelineType(tezos) { never },
                michelineType(michelsonToMichelineConverter) { never },
                michelineType(tezos) { never() },
                michelineType(michelsonToMichelineConverter) { never() },
                michelineComparableType(tezos) { never },
                michelineComparableType(michelsonToMichelineConverter) { never },
                michelineComparableType(tezos) { never() },
                michelineComparableType(michelsonToMichelineConverter) { never() },
            ),
            MichelinePrimitiveApplication("bool") to listOf(
                micheline(tezos) { bool },
                micheline(michelsonToMichelineConverter) { bool },
                micheline(tezos) { bool() },
                micheline(michelsonToMichelineConverter) { bool() },
                michelineType(tezos) { bool },
                michelineType(michelsonToMichelineConverter) { bool },
                michelineType(tezos) { bool() },
                michelineType(michelsonToMichelineConverter) { bool() },
                michelineComparableType(tezos) { bool },
                michelineComparableType(michelsonToMichelineConverter) { bool },
                michelineComparableType(tezos) { bool() },
                michelineComparableType(michelsonToMichelineConverter) { bool() },
            ),
            MichelinePrimitiveApplication("int") to listOf(
                micheline(tezos) { int },
                micheline(tezos) { int() },
                michelineType(tezos) { int },
                michelineType(tezos) { int() },
                michelineComparableType(tezos) { int },
                michelineComparableType(tezos) { int() },
            ),
            MichelinePrimitiveApplication("nat") to listOf(
                micheline(tezos) { nat },
                micheline(michelsonToMichelineConverter) { nat },
                micheline(tezos) { nat() },
                micheline(michelsonToMichelineConverter) { nat() },
                michelineType(tezos) { nat },
                michelineType(michelsonToMichelineConverter) { nat },
                michelineType(tezos) { nat() },
                michelineType(michelsonToMichelineConverter) { nat() },
                michelineComparableType(tezos) { nat },
                michelineComparableType(michelsonToMichelineConverter) { nat },
                michelineComparableType(tezos) { nat() },
                michelineComparableType(michelsonToMichelineConverter) { nat() },
            ),
            MichelinePrimitiveApplication("string") to listOf(
                micheline(tezos) { string },
                micheline(michelsonToMichelineConverter) { string },
                micheline(tezos) { string() },
                micheline(michelsonToMichelineConverter) { string() },
                michelineType(tezos) { string },
                michelineType(michelsonToMichelineConverter) { string },
                michelineType(tezos) { string() },
                michelineType(michelsonToMichelineConverter) { string() },
                michelineComparableType(tezos) { string },
                michelineComparableType(michelsonToMichelineConverter) { string },
                michelineComparableType(tezos) { string() },
                michelineComparableType(michelsonToMichelineConverter) { string() },
            ),
            MichelinePrimitiveApplication("chain_id") to listOf(
                micheline(tezos) { chainId },
                micheline(michelsonToMichelineConverter) { chainId },
                micheline(tezos) { chainId() },
                micheline(michelsonToMichelineConverter) { chainId() },
                michelineType(tezos) { chainId },
                michelineType(michelsonToMichelineConverter) { chainId },
                michelineType(tezos) { chainId() },
                michelineType(michelsonToMichelineConverter) { chainId() },
                michelineComparableType(tezos) { chainId },
                michelineComparableType(michelsonToMichelineConverter) { chainId },
                michelineComparableType(tezos) { chainId() },
                michelineComparableType(michelsonToMichelineConverter) { chainId() },
            ),
            MichelinePrimitiveApplication("bytes") to listOf(
                micheline(tezos) { bytes },
                micheline(michelsonToMichelineConverter) { bytes },
                micheline(tezos) { bytes() },
                micheline(michelsonToMichelineConverter) { bytes() },
                michelineType(tezos) { bytes },
                michelineType(michelsonToMichelineConverter) { bytes },
                michelineType(tezos) { bytes() },
                michelineType(michelsonToMichelineConverter) { bytes() },
                michelineComparableType(tezos) { bytes },
                michelineComparableType(michelsonToMichelineConverter) { bytes },
                michelineComparableType(tezos) { bytes() },
                michelineComparableType(michelsonToMichelineConverter) { bytes() },
            ),
            MichelinePrimitiveApplication("mutez") to listOf(
                micheline(tezos) { mutez },
                micheline(michelsonToMichelineConverter) { mutez },
                micheline(tezos) { mutez() },
                micheline(michelsonToMichelineConverter) { mutez() },
                michelineType(tezos) { mutez },
                michelineType(michelsonToMichelineConverter) { mutez },
                michelineType(tezos) { mutez() },
                michelineType(michelsonToMichelineConverter) { mutez() },
                michelineComparableType(tezos) { mutez },
                michelineComparableType(michelsonToMichelineConverter) { mutez },
                michelineComparableType(tezos) { mutez() },
                michelineComparableType(michelsonToMichelineConverter) { mutez() },
            ),
            MichelinePrimitiveApplication("key_hash") to listOf(
                micheline(tezos) { keyHash },
                micheline(michelsonToMichelineConverter) { keyHash },
                micheline(tezos) { keyHash() },
                micheline(michelsonToMichelineConverter) { keyHash() },
                michelineType(tezos) { keyHash },
                michelineType(michelsonToMichelineConverter) { keyHash },
                michelineType(tezos) { keyHash() },
                michelineType(michelsonToMichelineConverter) { keyHash() },
                michelineComparableType(tezos) { keyHash },
                michelineComparableType(michelsonToMichelineConverter) { keyHash },
                michelineComparableType(tezos) { keyHash() },
                michelineComparableType(michelsonToMichelineConverter) { keyHash() },
            ),
            MichelinePrimitiveApplication("key") to listOf(
                micheline(tezos) { key },
                micheline(michelsonToMichelineConverter) { key },
                micheline(tezos) { key() },
                micheline(michelsonToMichelineConverter) { key() },
                michelineType(tezos) { key },
                michelineType(michelsonToMichelineConverter) { key },
                michelineType(tezos) { key() },
                michelineType(michelsonToMichelineConverter) { key() },
                michelineComparableType(tezos) { key },
                michelineComparableType(michelsonToMichelineConverter) { key },
                michelineComparableType(tezos) { key() },
                michelineComparableType(michelsonToMichelineConverter) { key() },
            ),
            MichelinePrimitiveApplication("signature") to listOf(
                micheline(tezos) { signature },
                micheline(michelsonToMichelineConverter) { signature },
                micheline(tezos) { signature() },
                micheline(michelsonToMichelineConverter) { signature() },
                michelineType(tezos) { signature },
                michelineType(michelsonToMichelineConverter) { signature },
                michelineType(tezos) { signature() },
                michelineType(michelsonToMichelineConverter) { signature() },
                michelineComparableType(tezos) { signature },
                michelineComparableType(michelsonToMichelineConverter) { signature },
                michelineComparableType(tezos) { signature() },
                michelineComparableType(michelsonToMichelineConverter) { signature() },
            ),
            MichelinePrimitiveApplication("timestamp") to listOf(
                micheline(tezos) { timestamp },
                micheline(michelsonToMichelineConverter) { timestamp },
                micheline(tezos) { timestamp() },
                micheline(michelsonToMichelineConverter) { timestamp() },
                michelineType(tezos) { timestamp },
                michelineType(michelsonToMichelineConverter) { timestamp },
                michelineType(tezos) { timestamp() },
                michelineType(michelsonToMichelineConverter) { timestamp() },
                michelineComparableType(tezos) { timestamp },
                michelineComparableType(michelsonToMichelineConverter) { timestamp },
                michelineComparableType(tezos) { timestamp() },
                michelineComparableType(michelsonToMichelineConverter) { timestamp() },
            ),
            MichelinePrimitiveApplication("address") to listOf(
                micheline(tezos) { address },
                micheline(michelsonToMichelineConverter) { address },
                micheline(tezos) { address() },
                micheline(michelsonToMichelineConverter) { address() },
                michelineType(tezos) { address },
                michelineType(michelsonToMichelineConverter) { address },
                michelineType(tezos) { address() },
                michelineType(michelsonToMichelineConverter) { address() },
                michelineComparableType(tezos) { address },
                michelineComparableType(michelsonToMichelineConverter) { address },
                michelineComparableType(tezos) { address() },
                michelineComparableType(michelsonToMichelineConverter) { address() },
            ),
            MichelinePrimitiveApplication(
                "option",
                args = listOf(MichelinePrimitiveApplication("operation")),
            ) to listOf(
                micheline(tezos) {
                    option {
                        arg { operation }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    option {
                        arg { operation }
                    }
                },
                michelineType(tezos) {
                    option {
                        arg { operation }
                    }
                },
                michelineType(michelsonToMichelineConverter) {
                    option {
                        arg { operation }
                    }
                },
            ),
            MichelinePrimitiveApplication(
                "option",
                args = listOf(MichelinePrimitiveApplication("unit")),
            ) to listOf(
                micheline(tezos) {
                    option {
                        arg { unit }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    option {
                        arg { unit }
                    }
                },
                michelineType(tezos) {
                    option {
                        arg { unit }
                    }
                },
                michelineType(michelsonToMichelineConverter) {
                    option {
                        arg { unit }
                    }
                },
                michelineComparableType(tezos) {
                    option {
                        arg { unit }
                    }
                },
                michelineComparableType(michelsonToMichelineConverter) {
                    option {
                        arg { unit }
                    }
                },
            )
        )

        expectedWithActual.forEach { (expected, actual) ->
            actual.forEach { assertEquals(expected, it) }
        }
    }
}