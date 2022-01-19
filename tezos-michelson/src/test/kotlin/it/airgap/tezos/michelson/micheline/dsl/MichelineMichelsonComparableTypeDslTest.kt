package it.airgap.tezos.michelson.micheline.dsl

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.michelson.internal.converter.MichelsonToMichelineConverter
import it.airgap.tezos.michelson.internal.di.ScopedDependencyRegistry
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MichelineMichelsonComparableTypeDslTest {

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
    fun `builds Micheline Michelson Comparable Type Expression`() {
        val expectedWithActual = listOf(
            MichelinePrimitiveApplication("unit") to listOf(
                micheline { unit },
                micheline(michelsonToMichelineConverter) { unit },
                micheline { unit() },
                micheline(michelsonToMichelineConverter) { unit() },
                michelineType { unit },
                michelineType(michelsonToMichelineConverter) { unit },
                michelineType { unit() },
                michelineType(michelsonToMichelineConverter) { unit() },
                michelineComparableType { unit },
                michelineComparableType(michelsonToMichelineConverter) { unit },
                michelineComparableType { unit() },
                michelineComparableType(michelsonToMichelineConverter) { unit() },
            ),
            MichelinePrimitiveApplication("never") to listOf(
                micheline { never },
                micheline(michelsonToMichelineConverter) { never },
                micheline { never() },
                micheline(michelsonToMichelineConverter) { never() },
                michelineType { never },
                michelineType(michelsonToMichelineConverter) { never },
                michelineType { never() },
                michelineType(michelsonToMichelineConverter) { never() },
                michelineComparableType { never },
                michelineComparableType(michelsonToMichelineConverter) { never },
                michelineComparableType { never() },
                michelineComparableType(michelsonToMichelineConverter) { never() },
            ),
            MichelinePrimitiveApplication("bool") to listOf(
                micheline { bool },
                micheline(michelsonToMichelineConverter) { bool },
                micheline { bool() },
                micheline(michelsonToMichelineConverter) { bool() },
                michelineType { bool },
                michelineType(michelsonToMichelineConverter) { bool },
                michelineType { bool() },
                michelineType(michelsonToMichelineConverter) { bool() },
                michelineComparableType { bool },
                michelineComparableType(michelsonToMichelineConverter) { bool },
                michelineComparableType { bool() },
                michelineComparableType(michelsonToMichelineConverter) { bool() },
            ),
            MichelinePrimitiveApplication("int") to listOf(
                micheline { int },
                micheline { int() },
                michelineType { int },
                michelineType { int() },
                michelineComparableType { int },
                michelineComparableType { int() },
            ),
            MichelinePrimitiveApplication("nat") to listOf(
                micheline { nat },
                micheline(michelsonToMichelineConverter) { nat },
                micheline { nat() },
                micheline(michelsonToMichelineConverter) { nat() },
                michelineType { nat },
                michelineType(michelsonToMichelineConverter) { nat },
                michelineType { nat() },
                michelineType(michelsonToMichelineConverter) { nat() },
                michelineComparableType { nat },
                michelineComparableType(michelsonToMichelineConverter) { nat },
                michelineComparableType { nat() },
                michelineComparableType(michelsonToMichelineConverter) { nat() },
            ),
            MichelinePrimitiveApplication("string") to listOf(
                micheline { string },
                micheline(michelsonToMichelineConverter) { string },
                micheline { string() },
                micheline(michelsonToMichelineConverter) { string() },
                michelineType { string },
                michelineType(michelsonToMichelineConverter) { string },
                michelineType { string() },
                michelineType(michelsonToMichelineConverter) { string() },
                michelineComparableType { string },
                michelineComparableType(michelsonToMichelineConverter) { string },
                michelineComparableType { string() },
                michelineComparableType(michelsonToMichelineConverter) { string() },
            ),
            MichelinePrimitiveApplication("chain_id") to listOf(
                micheline { chainId },
                micheline(michelsonToMichelineConverter) { chainId },
                micheline { chainId() },
                micheline(michelsonToMichelineConverter) { chainId() },
                michelineType { chainId },
                michelineType(michelsonToMichelineConverter) { chainId },
                michelineType { chainId() },
                michelineType(michelsonToMichelineConverter) { chainId() },
                michelineComparableType { chainId },
                michelineComparableType(michelsonToMichelineConverter) { chainId },
                michelineComparableType { chainId() },
                michelineComparableType(michelsonToMichelineConverter) { chainId() },
            ),
            MichelinePrimitiveApplication("bytes") to listOf(
                micheline { bytes },
                micheline(michelsonToMichelineConverter) { bytes },
                micheline { bytes() },
                micheline(michelsonToMichelineConverter) { bytes() },
                michelineType { bytes },
                michelineType(michelsonToMichelineConverter) { bytes },
                michelineType { bytes() },
                michelineType(michelsonToMichelineConverter) { bytes() },
                michelineComparableType { bytes },
                michelineComparableType(michelsonToMichelineConverter) { bytes },
                michelineComparableType { bytes() },
                michelineComparableType(michelsonToMichelineConverter) { bytes() },
            ),
            MichelinePrimitiveApplication("mutez") to listOf(
                micheline { mutez },
                micheline(michelsonToMichelineConverter) { mutez },
                micheline { mutez() },
                micheline(michelsonToMichelineConverter) { mutez() },
                michelineType { mutez },
                michelineType(michelsonToMichelineConverter) { mutez },
                michelineType { mutez() },
                michelineType(michelsonToMichelineConverter) { mutez() },
                michelineComparableType { mutez },
                michelineComparableType(michelsonToMichelineConverter) { mutez },
                michelineComparableType { mutez() },
                michelineComparableType(michelsonToMichelineConverter) { mutez() },
            ),
            MichelinePrimitiveApplication("key_hash") to listOf(
                micheline { keyHash },
                micheline(michelsonToMichelineConverter) { keyHash },
                micheline { keyHash() },
                micheline(michelsonToMichelineConverter) { keyHash() },
                michelineType { keyHash },
                michelineType(michelsonToMichelineConverter) { keyHash },
                michelineType { keyHash() },
                michelineType(michelsonToMichelineConverter) { keyHash() },
                michelineComparableType { keyHash },
                michelineComparableType(michelsonToMichelineConverter) { keyHash },
                michelineComparableType { keyHash() },
                michelineComparableType(michelsonToMichelineConverter) { keyHash() },
            ),
            MichelinePrimitiveApplication("signature") to listOf(
                micheline { signature },
                micheline(michelsonToMichelineConverter) { signature },
                micheline { signature() },
                micheline(michelsonToMichelineConverter) { signature() },
                michelineType { signature },
                michelineType(michelsonToMichelineConverter) { signature },
                michelineType { signature() },
                michelineType(michelsonToMichelineConverter) { signature() },
                michelineComparableType { signature },
                michelineComparableType(michelsonToMichelineConverter) { signature },
                michelineComparableType { signature() },
                michelineComparableType(michelsonToMichelineConverter) { signature() },
            ),
            MichelinePrimitiveApplication("timestamp") to listOf(
                micheline { timestamp },
                micheline(michelsonToMichelineConverter) { timestamp },
                micheline { timestamp() },
                micheline(michelsonToMichelineConverter) { timestamp() },
                michelineType { timestamp },
                michelineType(michelsonToMichelineConverter) { timestamp },
                michelineType { timestamp() },
                michelineType(michelsonToMichelineConverter) { timestamp() },
                michelineComparableType { timestamp },
                michelineComparableType(michelsonToMichelineConverter) { timestamp },
                michelineComparableType { timestamp() },
                michelineComparableType(michelsonToMichelineConverter) { timestamp() },
            ),
            MichelinePrimitiveApplication("address") to listOf(
                micheline { address },
                micheline(michelsonToMichelineConverter) { address },
                micheline { address() },
                micheline(michelsonToMichelineConverter) { address() },
                michelineType { address },
                michelineType(michelsonToMichelineConverter) { address },
                michelineType { address() },
                michelineType(michelsonToMichelineConverter) { address() },
                michelineComparableType { address },
                michelineComparableType(michelsonToMichelineConverter) { address },
                michelineComparableType { address() },
                michelineComparableType(michelsonToMichelineConverter) { address() },
            ),
            MichelinePrimitiveApplication(
                "option",
                args = listOf(MichelinePrimitiveApplication("operation")),
            ) to listOf(
                micheline {
                    option {
                        arg { operation }
                    }
                },
                micheline(michelsonToMichelineConverter) {
                    option {
                        arg { operation }
                    }
                },
                michelineType {
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
                michelineComparableType {
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