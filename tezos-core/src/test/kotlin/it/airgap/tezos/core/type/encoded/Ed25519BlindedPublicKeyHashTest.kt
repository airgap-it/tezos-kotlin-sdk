package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import org.junit.Test
import kotlin.test.*

class Ed25519BlindedPublicKeyHashTest {

    @Test
    fun `should recognize valid and invalid Ed25519BlindedPublicKeyHash strings`() {
        validStrings.forEach {
            assertTrue(Ed25519BlindedPublicKeyHash.isValid(it), "Expected `$it` to be recognized as valid Ed25519BlindedPublicKeyHash string.")
        }

        invalidStrings.forEach {
            assertFalse(Ed25519BlindedPublicKeyHash.isValid(it), "Expected `$it` to be recognized as invalid Ed25519BlindedPublicKeyHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid Ed25519BlindedPublicKeyHash bytes`() {
        validBytes.forEach {
            assertTrue(Ed25519BlindedPublicKeyHash.isValid(it), "Expected `$it` to be recognized as valid Ed25519BlindedPublicKeyHash.")
            assertTrue(Ed25519BlindedPublicKeyHash.isValid(it.toList()), "Expected `$it` to be recognized as valid Ed25519BlindedPublicKeyHash.")
        }

        invalidBytes.forEach {
            assertFalse(Ed25519BlindedPublicKeyHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid Ed25519BlindedPublicKeyHash.")
            assertFalse(Ed25519BlindedPublicKeyHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid Ed25519BlindedPublicKeyHash.")
        }
    }

    @Test
    fun `should create Ed25519BlindedPublicKeyHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, Ed25519BlindedPublicKeyHash.createValue(it).base58)
            assertEquals(it, Ed25519BlindedPublicKeyHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create Ed25519BlindedPublicKeyHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { Ed25519BlindedPublicKeyHash.createValue(it) }
            assertNull(Ed25519BlindedPublicKeyHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "btz1Tx4dqRwTNBiK6F5Jyq88Cwjn4cec4NM3R",
            "btz1fLh5y8tLXZeeQzyVSsbiPxEsXy8FP6eun",
            "btz1PygiUsaqj9Vwh2YfKSwzLfWMUtGGHdKgd",
            "btz1QT48NpP7XN8eEQfCNam15vBVwR7jEnSCi",
            "btz1cRC5WSgVvn4YmnxzU9mRE4ENHxFVF7c81",
            "btz1UQFdA6Sk6BMQ73x5DWWMspoCLCKKzKiZS",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "Tx4dqRwTNBiK6F5Jyq88Cwjn4cec4NM3R",
            "btz1fLh5y8tLXZeeQzyVSsbiPxEsXy8F",
            "btz1PygiUsaqj9Vwh2YfKSwzLfWMUtGGHdKgd5Q1s",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "53ad11d727f4b0c72e2f6be6e9a296ea6946c92b",
            "010231df53ad11d727f4b0c72e2f6be6e9a296ea6946c92b",
            "d09e0828b039c128217d1b3169681f84474e8f92",
            "010231dfd09e0828b039c128217d1b3169681f84474e8f92",
            "281b19c50f7a46a9fe07042e49f926517e0b788d",
            "010231df281b19c50f7a46a9fe07042e49f926517e0b788d",
            "2d4834993c3a5c25be9c53112d73a860e0443f6d",
            "010231df2d4834993c3a5c25be9c53112d73a860e0443f6d",
            "b08f850c9ea335afdbfdb70dfca2b0ec141f2f8c",
            "010231dfb08f850c9ea335afdbfdb70dfca2b0ec141f2f8c",
            "58a10ea9664a9a687e03a46105ff00860ff9627d",
            "010231df58a10ea9664a9a687e03a46105ff00860ff9627d",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "3d9ebef11bc002f9cd055e1207affae6872b47",
            "010231dfc8ffade42aca4cb7e1262f282bfd35956a1d",
            "110231df6e097851dd5636f8a320b47b974d63418524c13d",
        ).map { it.asHexString().toByteArray() }
}
