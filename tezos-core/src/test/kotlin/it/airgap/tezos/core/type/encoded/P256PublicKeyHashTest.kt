package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class P256PublicKeyHashTest {

    @Test
    fun `should recognize valid and invalid P256PublicKeyHash strings`() {
        validStrings.forEach {
            assertTrue(P256PublicKeyHash.isValid(it), "Expected `$it` to be recognized as valid P256PublicKeyHash string.")
        }

        invalidStrings.forEach {
            assertFalse(P256PublicKeyHash.isValid(it), "Expected `$it` to be recognized as invalid P256PublicKeyHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid P256PublicKeyHash bytes`() {
        validBytes.forEach {
            assertTrue(P256PublicKeyHash.isValid(it), "Expected `$it` to be recognized as valid P256PublicKeyHash.")
            assertTrue(P256PublicKeyHash.isValid(it.toList()), "Expected `$it` to be recognized as valid P256PublicKeyHash.")
        }

        invalidBytes.forEach {
            assertFalse(P256PublicKeyHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid P256PublicKeyHash.")
            assertFalse(P256PublicKeyHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid P256PublicKeyHash.")
        }
    }

    @Test
    fun `should create P256PublicKeyHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, P256PublicKeyHash.createValue(it).base58)
            assertEquals(it, P256PublicKeyHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create P256PublicKeyHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { P256PublicKeyHash.createValue(it) }
            assertNull(P256PublicKeyHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "tz3geu1UG69oiSoJjmLvK6zQbyzHXWywxHM6",
            "tz3g6DDozSAT5mvYdUMJjboyspUdHaxBM3eL",
            "tz3jVxh9cyn5cERGfGpFPofnComxmvu74NWr",
            "tz3ebQZu2a8He2CePT2CriFrdyXeg9pDW5eu",
            "tz3PPYANBR3gxXNhAriRiizjAVfiGbCFkks2",
            "tz3TXAgC8U71XgFVkBwBBHn5KAMy3roAL9PB",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "geu1UG69oiSoJjmLvK6zQbyzHXWywxHM6",
            "tz3g6DDozSAT5mvYdUMJjboyspUdHaxB",
            "tz3jVxh9cyn5cERGfGpFPofnComxmvu74NWrebQZ",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "def33c0195bacd21fc234c0eb51f1dd94b4eb29d",
            "06a1a4def33c0195bacd21fc234c0eb51f1dd94b4eb29d",
            "d8c4b5fe0f82971b4ef8be52a08d57eb04ab8c7c",
            "06a1a4d8c4b5fe0f82971b4ef8be52a08d57eb04ab8c7c",
            "fe2af185285e1ca26f48186018f777b62974f757",
            "06a1a4fe2af185285e1ca26f48186018f777b62974f757",
            "c859fe60fa437e2807a8800a1909a8fbaa490f0c",
            "06a1a4c859fe60fa437e2807a8800a1909a8fbaa490f0c",
            "2191746ab92a56d4061a955cf3e7658880509683",
            "06a1a42191746ab92a56d4061a955cf3e7658880509683",
            "4ee359a5ba0175be7a6d1ffe5c8c0588120e0e1d",
            "06a1a44ee359a5ba0175be7a6d1ffe5c8c0588120e0e1d",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "def33c0195bacd21fc234c0eb51f1dd9",
            "d8c4b5fe0f82971b4ef8be52a08d57eb04ab8c7cc4b5",
            "16a1a44ee359a5ba0175be7a6d1ffe5c8c0588120e0e1d",
        ).map { it.asHexString().toByteArray() }
}
