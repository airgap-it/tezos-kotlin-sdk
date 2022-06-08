package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import org.junit.Test
import kotlin.test.*

class OperationListListHashTest {

    @Test
    fun `should recognize valid and invalid OperationListListHash strings`() {
        validStrings.forEach {
            assertTrue(OperationListListHash.isValid(it), "Expected `$it` to be recognized as valid OperationListListHash string.")
        }

        invalidStrings.forEach {
            assertFalse(OperationListListHash.isValid(it), "Expected `$it` to be recognized as invalid OperationListListHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid OperationListListHash bytes`() {
        validBytes.forEach {
            assertTrue(OperationListListHash.isValid(it), "Expected `$it` to be recognized as valid OperationListListHash.")
            assertTrue(OperationListListHash.isValid(it.toList()), "Expected `$it` to be recognized as valid OperationListListHash.")
        }

        invalidBytes.forEach {
            assertFalse(OperationListListHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid OperationListListHash.")
            assertFalse(OperationListListHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid OperationListListHash.")
        }
    }

    @Test
    fun `should create OperationListListHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, OperationListListHash.createValue(it).base58)
            assertEquals(it, OperationListListHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create OperationListListHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { OperationListListHash.createValue(it) }
            assertNull(OperationListListHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "LLoas1rDDYxNFbzn61c7meh3dEyN6bz79WSiqqUP4CyiLmzjY8RyK",
            "LLoaiuDJREHJe89Usk3nSDwoYJNebroyPFheSF1N2vKgQSCkYygBF",
            "LLoaT5ftSDM4WTWxJXGhR7PmBeuBiTzAR7jQk3i5qsirJdLxBde8x",
            "LLoZPniUrF5LYMmf18Ktnkt9rCthmZWkbcgC6kKsAsn7bpcw43mcb",
            "LLobBUvrNyWFEc4yc3WVLLMFQf8iq8bZ3vkAGNe3Ab7HiyoYKmnNz",
            "LLoaFkD8NCoqNDetesbKnNqTTNKAfYfc9VYisZTF6RkcDHrMKjczy",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "as1rDDYxNFbzn61c7meh3dEyN6bz79WSiqqUP4CyiLmzjY8RyK",
            "LLoaiuDJREHJe89Usk3nSDwoYJNebroyPFheSF1N2vKgQSCkY",
            "LLoaT5ftSDM4WTWxJXGhR7PmBeuBiTzAR7jQk3i5qsirJdLxBde8xZPni",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "cac89d41fa06b0062e755e49be1b3b85ca857ca60d714cf64351040daa181552",
            "1d9f6dcac89d41fa06b0062e755e49be1b3b85ca857ca60d714cf64351040daa181552",
            "b85bde98f3129fe8058f6887f280a3a82b23da2fd6bf53de5b153db3c4310b5f",
            "1d9f6db85bde98f3129fe8058f6887f280a3a82b23da2fd6bf53de5b153db3c4310b5f",
            "9470370ab461254141144e70e4b608e205eb2345f1d453153d33698d4337c6ee",
            "1d9f6d9470370ab461254141144e70e4b608e205eb2345f1d453153d33698d4337c6ee",
            "09443ff2bca9b3b35b10fa9b5cb539d148dc000f852ccb04ce12d82ea441aaa4",
            "1d9f6d09443ff2bca9b3b35b10fa9b5cb539d148dc000f852ccb04ce12d82ea441aaa4",
            "f4b70a18581ba6993db862e06a8bef6ecb0f9d5f22dcc2a8435178e1f2c7872a",
            "1d9f6df4b70a18581ba6993db862e06a8bef6ecb0f9d5f22dcc2a8435178e1f2c7872a",
            "7ab3197861a05500c1162fbe48d4d917abbea88b2e537febb462cc7bd46dad92",
            "1d9f6d7ab3197861a05500c1162fbe48d4d917abbea88b2e537febb462cc7bd46dad92",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "cac89d41fa06b0062e755e49be1b3b85ca857ca60d714cf64351040d",
            "b85bde98f3129fe8058f6887f280a3a82b23da2fd6bf53de5b153db3c4310b5f5bde",
            "2d9f6d7ab3197861a05500c1162fbe48d4d917abbea88b2e537febb462cc7bd46dad92",
        ).map { it.asHexString().toByteArray() }
}