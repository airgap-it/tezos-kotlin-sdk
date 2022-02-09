package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class OperationMetadataListHashTest {

    @Test
    fun `should recognize valid and invalid OperationMetadataListHash strings`() {
        validStrings.forEach {
            assertTrue(OperationMetadataListHash.isValid(it), "Expected `$it` to be recognized as valid OperationMetadataListHash string.")
        }

        invalidStrings.forEach {
            assertFalse(OperationMetadataListHash.isValid(it), "Expected `$it` to be recognized as invalid OperationMetadataListHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid OperationMetadataListHash bytes`() {
        validBytes.forEach {
            assertTrue(OperationMetadataListHash.isValid(it), "Expected `$it` to be recognized as valid OperationMetadataListHash.")
            assertTrue(OperationMetadataListHash.isValid(it.toList()), "Expected `$it` to be recognized as valid OperationMetadataListHash.")
        }

        invalidBytes.forEach {
            assertFalse(OperationMetadataListHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid OperationMetadataListHash.")
            assertFalse(OperationMetadataListHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid OperationMetadataListHash.")
        }
    }

    @Test
    fun `should create OperationMetadataListHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, OperationMetadataListHash.createValue(it).base58)
            assertEquals(it, OperationMetadataListHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create OperationMetadataListHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { OperationMetadataListHash.createValue(it) }
            assertNull(OperationMetadataListHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "Lr1SDpNSTpNdGXppNk4mx7iPujmDVwgmDhU2WPwNTz6nSNTDAe4G",
            "Lr36SHh1RDtriUZJ2GqZKJnum6n7ADX2ShuVgVZyaQjTDYduWzy2",
            "Lr1dvQdYuY7Dx11HUYo6Ze4ojdNvAu8A5zMrRm9ZYsDQtvMarTvd",
            "Lr2ZQ5JTpw15XdM1VmFuqJDfeiEh288USkCGehpVAuQiSNxzrP44",
            "Lr2aXKZXvDhwYfNg8n8VZwaNPrWTDJzJQQwGa7csM8N1mwBDhf9D",
            "Lr2UgHyq3enjMxoytgFF6Ydft1KSuc6U3Y3X5bGBLBAX23Uvg6Yh",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "1SDpNSTpNdGXppNk4mx7iPujmDVwgmDhU2WPwNTz6nSNTDAe4G",
            "Lr36SHh1RDtriUZJ2GqZKJnum6n7ADX2ShuVgVZyaQjTDYdu",
            "Lr1dvQdYuY7Dx11HUYo6Ze4ojdNvAu8A5zMrRm9ZYsDQtvMarTvdZQ5J",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "1132ccef7bed1913557a4dce733b0199f6bd424aef225039e84c7da9fd28e26c",
            "86271132ccef7bed1913557a4dce733b0199f6bd424aef225039e84c7da9fd28e26c",
            "ebaa99b5dd3c6914e22ba7911aaad65cb005fd02936bcf209666771f2e7c394c",
            "8627ebaa99b5dd3c6914e22ba7911aaad65cb005fd02936bcf209666771f2e7c394c",
            "2bc3ace2191b637b4d5f2843f88c511b69fdf32f1a3f0d04bf506977cde0ed97",
            "86272bc3ace2191b637b4d5f2843f88c511b69fdf32f1a3f0d04bf506977cde0ed97",
            "a530c1bcd3c70f6a42cc66bd2df8afd878d7c84f8fa4dab6e62e43a17a33937e",
            "8627a530c1bcd3c70f6a42cc66bd2df8afd878d7c84f8fa4dab6e62e43a17a33937e",
            "a7bea78eaaa06fb70e0e897a8ebdef2a56cfdfbf58d2c3c02c4461a7318bbb26",
            "8627a7bea78eaaa06fb70e0e897a8ebdef2a56cfdfbf58d2c3c02c4461a7318bbb26",
            "9a78e7679865e6b12481675416e0cef63406fad195e803019a59041df1a6bf22",
            "86279a78e7679865e6b12481675416e0cef63406fad195e803019a59041df1a6bf22",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "1132ccef7bed1913557a4dce733b0199f6bd424aef225039e84c7da9fd",
            "ebaa99b5dd3c6914e22ba7911aaad65cb005fd02936bcf209666771f2e7c394caa99",
            "16279a78e7679865e6b12481675416e0cef63406fad195e803019a59041df1a6bf22",
        ).map { it.asHexString().toByteArray() }
}
