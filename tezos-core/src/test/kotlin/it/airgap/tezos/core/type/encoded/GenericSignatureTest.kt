package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class GenericSignatureTest {

    @Test
    fun `should recognize valid and invalid GenericSignature strings`() {
        validStrings.forEach {
            assertTrue(GenericSignature.isValid(it), "Expected `$it` to be recognized as valid GenericSignature string.")
        }

        invalidStrings.forEach {
            assertFalse(GenericSignature.isValid(it), "Expected `$it` to be recognized as invalid GenericSignature string.")
        }
    }

    @Test
    fun `should recognize valid and invalid GenericSignature bytes`() {
        validBytes.forEach {
            assertTrue(GenericSignature.isValid(it), "Expected `$it` to be recognized as valid GenericSignature.")
            assertTrue(GenericSignature.isValid(it.toList()), "Expected `$it` to be recognized as valid GenericSignature.")
        }

        invalidBytes.forEach {
            assertFalse(GenericSignature.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid GenericSignature.")
            assertFalse(GenericSignature.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid GenericSignature.")
        }
    }

    @Test
    fun `should create GenericSignature from valid string`() {
        validStrings.forEach {
            assertEquals(it, GenericSignature.createValue(it).base58)
            assertEquals(it, GenericSignature.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create GenericSignature from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { GenericSignature.createValue(it) }
            assertNull(GenericSignature.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "sigNrDbt3Zmm6NpvUsHsWhWsf8BSJTZXKerxECK28TYB6X6GWGnjkhfvS4VyBaQYzoWR6geAEpPUiuep1YQ4oMM6mK72SZdN",
            "sigsUsQoFA7HqGpT6kLX1n13NHoi9RYKMZFueKjRkG1MPsYtrfGPNm5FWe5gmAEQAF4cuYNAUpz4AB1EU8ZyZP95e7jexNb4",
            "sigNGXU8XatKUQUJA77dMedLMpbU8ymew53qT55EmuwJUNEaW47kccxvD1YMSnY1jyXKVVCYN2USG91umv5r7j6mjR2Vv9gq",
            "sigvFsg5Vp9erqB4kAdzYVMSpwogPt26sNNcdJT64s9yLgbLx7r3V6Z13Lv3sizb9aZj6CCJYrXy7u3fPrygGC21bYRQQenX",
            "sigWiQfKJMJPNhuGQ8Cu3SHYMoN93TSA1WYq4LeUDrdN6gTfTogJbUdxq3xNJEfXYg6MbGQ8Q3rwAQazG3ffNR4s2XDcRqMH",
            "sigWurpd8gF3DfiirxzzT9wNowWXgJWhQmhquPMMhiZNF9mkMNshyg2oJtp2K7mWsQR1eUTWsupzDyDGhRqbDufuCoh7edL3",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "NrDbt3Zmm6NpvUsHsWhWsf8BSJTZXKerxECK28TYB6X6GWGnjkhfvS4VyBaQYzoWR6geAEpPUiuep1YQ4oMM6mK72SZdN",
            "sigsUsQoFA7HqGpT6kLX1n13NHoi9RYKMZFueKjRkG1MPsYtrfGPNm5FWe5gmAEQAF4cuYNAUpz4AB1EU8ZyZP95e7je",
            "sigNGXU8XatKUQUJA77dMedLMpbU8ymew53qT55EmuwJUNEaW47kccxvD1YMSnY1jyXKVVCYN2USG91umv5r7j6mjR2Vv9gqvFsg",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "06944570fb85ecb6e05dcb31c2b29c3652b5d107ec571188fe61994aa2d8e2843a763fa952acb533412a97c170a3959e15c2a18c45433fb5b3f7aebf93dbadc7",
            "04822b06944570fb85ecb6e05dcb31c2b29c3652b5d107ec571188fe61994aa2d8e2843a763fa952acb533412a97c170a3959e15c2a18c45433fb5b3f7aebf93dbadc7",
            "e16e1a19b9e2111e5284f2ea0aedac80bc9cb08dcb345df9708ecae5f083f3925a2cf3a11c2376fd9bc39dfaf538c0c16eeb96e3035567ba31b5f7c02596ae10",
            "04822be16e1a19b9e2111e5284f2ea0aedac80bc9cb08dcb345df9708ecae5f083f3925a2cf3a11c2376fd9bc39dfaf538c0c16eeb96e3035567ba31b5f7c02596ae10",
            "022398de8b19e728e6d2e1aeb810d29ac5f0ffa98f71b79af5f359078f790ea1463b822bbea0d4946052ca59eaa4c9e25c45d8c6ef3a66f5b422607df4452965",
            "04822b022398de8b19e728e6d2e1aeb810d29ac5f0ffa98f71b79af5f359078f790ea1463b822bbea0d4946052ca59eaa4c9e25c45d8c6ef3a66f5b422607df4452965",
            "f6a5f3e7992596c5c15948dbe57dd13b6ba706d48ec66d86d804b7e135f9a8b200d7c60c7ddb8f0a993dcaa7a8ad5955fc496368398169a618a3e1b0ce3b2aad",
            "04822bf6a5f3e7992596c5c15948dbe57dd13b6ba706d48ec66d86d804b7e135f9a8b200d7c60c7ddb8f0a993dcaa7a8ad5955fc496368398169a618a3e1b0ce3b2aad",
            "42b2e419720242ce9dd169d92a629706ae848c2e99a2da4cc9086d471daf1af058ef86f11bd5a4891f1b11e947b3a3e6ee1a5ee0e31bf1fc73d044e1d426762a",
            "04822b42b2e419720242ce9dd169d92a629706ae848c2e99a2da4cc9086d471daf1af058ef86f11bd5a4891f1b11e947b3a3e6ee1a5ee0e31bf1fc73d044e1d426762a",
            "443537bcb96b5ea54daa89a2789341b2bc5f0f3fb5af91cb01f823bf971776b6418279bcac5187fbe5a582cc61ba67d584f5ab0c9003560c0732a879c3b35505",
            "04822b443537bcb96b5ea54daa89a2789341b2bc5f0f3fb5af91cb01f823bf971776b6418279bcac5187fbe5a582cc61ba67d584f5ab0c9003560c0732a879c3b35505",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "06944570fb85ecb6e05dcb31c2b29c3652b5d107ec571188fe61994aa2d8e2843a763fa952acb533412a97c170a3959e15c2a18c45433fb5b3f7aebf",
            "e16e1a19b9e2111e5284f2ea0aedac80bc9cb08dcb345df9708ecae5f083f3925a2cf3a11c2376fd9bc39dfaf538c0c16eeb96e3035567ba31b5f7c02596ae106e1a",
            "14822b443537bcb96b5ea54daa89a2789341b2bc5f0f3fb5af91cb01f823bf971776b6418279bcac5187fbe5a582cc61ba67d584f5ab0c9003560c0732a879c3b35505",
        ).map { it.asHexString().toByteArray() }
}