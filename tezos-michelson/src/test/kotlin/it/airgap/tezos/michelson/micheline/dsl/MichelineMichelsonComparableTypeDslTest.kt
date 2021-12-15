package it.airgap.tezos.michelson.micheline.dsl

import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import org.junit.Test
import kotlin.test.assertEquals

class MichelineMichelsonComparableTypeDslTest {

    @Test
    fun `builds Micheline Michelson Comparable Type Expression`() {
        val expectedWithActual = listOf(
            MichelinePrimitiveApplication("unit") to listOf(
                micheline { unit },
                micheline { unit() },
                michelineType { unit },
                michelineType { unit() },
                michelineComparableType { unit },
                michelineComparableType { unit() },
            ),
            MichelinePrimitiveApplication("never") to listOf(
                micheline { never },
                micheline { never() },
                michelineType { never },
                michelineType { never() },
                michelineComparableType { never },
                michelineComparableType { never() },
            ),
            MichelinePrimitiveApplication("bool") to listOf(
                micheline { bool },
                micheline { bool() },
                michelineType { bool },
                michelineType { bool() },
                michelineComparableType { bool },
                michelineComparableType { bool() },
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
                micheline { nat() },
                michelineType { nat },
                michelineType { nat() },
                michelineComparableType { nat },
                michelineComparableType { nat() },
            ),
            MichelinePrimitiveApplication("string") to listOf(
                micheline { string },
                micheline { string() },
                michelineType { string },
                michelineType { string() },
                michelineComparableType { string },
                michelineComparableType { string() },
            ),
            MichelinePrimitiveApplication("chain_id") to listOf(
                micheline { chainId },
                micheline { chainId() },
                michelineType { chainId },
                michelineType { chainId() },
                michelineComparableType { chainId },
                michelineComparableType { chainId() },
            ),
            MichelinePrimitiveApplication("bytes") to listOf(
                micheline { bytes },
                micheline { bytes() },
                michelineType { bytes },
                michelineType { bytes() },
                michelineComparableType { bytes },
                michelineComparableType { bytes() },
            ),
            MichelinePrimitiveApplication("mutez") to listOf(
                micheline { mutez },
                micheline { mutez() },
                michelineType { mutez },
                michelineType { mutez() },
                michelineComparableType { mutez },
                michelineComparableType { mutez() },
            ),
            MichelinePrimitiveApplication("key_hash") to listOf(
                micheline { keyHash },
                micheline { keyHash() },
                michelineType { keyHash },
                michelineType { keyHash() },
                michelineComparableType { keyHash },
                michelineComparableType { keyHash() },
            ),
            MichelinePrimitiveApplication("signature") to listOf(
                micheline { signature },
                micheline { signature() },
                michelineType { signature },
                michelineType { signature() },
                michelineComparableType { signature },
                michelineComparableType { signature() },
            ),
            MichelinePrimitiveApplication("timestamp") to listOf(
                micheline { timestamp },
                micheline { timestamp() },
                michelineType { timestamp },
                michelineType { timestamp() },
                michelineComparableType { timestamp },
                michelineComparableType { timestamp() },
            ),
            MichelinePrimitiveApplication("address") to listOf(
                micheline { address },
                micheline { address() },
                michelineType { address },
                michelineType { address() },
                michelineComparableType { address },
                michelineComparableType { address() },
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
                michelineType {
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
                michelineType {
                    option {
                        arg { unit }
                    }
                },
                michelineComparableType {
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