package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import org.junit.Test
import kotlin.test.*

class CryptoboxPublicKeyHashTest {

    @Test
    fun `should recognize valid and invalid CryptoboxPublicKeyHash strings`() {
        validStrings.forEach {
            assertTrue(CryptoboxPublicKeyHash.isValid(it), "Expected `$it` to be recognized as valid CryptoboxPublicKeyHash string.")
        }

        invalidStrings.forEach {
            assertFalse(CryptoboxPublicKeyHash.isValid(it), "Expected `$it` to be recognized as invalid CryptoboxPublicKeyHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid CryptoboxPublicKeyHash bytes`() {
        validBytes.forEach {
            assertTrue(CryptoboxPublicKeyHash.isValid(it), "Expected `$it` to be recognized as valid CryptoboxPublicKeyHash.")
            assertTrue(CryptoboxPublicKeyHash.isValid(it.toList()), "Expected `$it` to be recognized as valid CryptoboxPublicKeyHash.")
        }

        invalidBytes.forEach {
            assertFalse(CryptoboxPublicKeyHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid CryptoboxPublicKeyHash.")
            assertFalse(CryptoboxPublicKeyHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid CryptoboxPublicKeyHash.")
        }
    }

    @Test
    fun `should create CryptoboxPublicKeyHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, CryptoboxPublicKeyHash.createValue(it).base58)
            assertEquals(it, CryptoboxPublicKeyHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create CryptoboxPublicKeyHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { CryptoboxPublicKeyHash.createValue(it) }
            assertNull(CryptoboxPublicKeyHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "idtXTZ5vaJZ262UXjtfDAdpDepWGVU",
            "idsY3nCuq4WAmqMLXiHwtyJQCDuqCm",
            "idqcTokXHwRrNM3k3DUdZYftDYLsSa",
            "idqknpm5QRLqFRYjpXFhbXNGSUYcEa",
            "idqoJtnFAPfkRBdAL5UcnuSG4z4voV",
            "idtFyVByUrPLRfim39KVfFPoMx77f6",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "tXTZ5vaJZ262UXjtfDAdpDepWGVU",
            "idsY3nCuq4WAmqMLXiHwtyJQCD",
            "idqcTokXHwRrNM3k3DUdZYftDYLsSaknpm",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "de8315cf321cb8038123de71da4fa381",
            "9967de8315cf321cb8038123de71da4fa381",
            "9776a7f0c713537fafa550da284c110b",
            "99679776a7f0c713537fafa550da284c110b",
            "0d5e655727364244fbaba694198de735",
            "99670d5e655727364244fbaba694198de735",
            "17accfcb42ec71307eccdd8a41aaa3cb",
            "996717accfcb42ec71307eccdd8a41aaa3cb",
            "1acab1f428359a8538144feab2e79a1f",
            "99671acab1f428359a8538144feab2e79a1f",
            "cb598473d66a140cda4d9c60143b3985",
            "9967cb598473d66a140cda4d9c60143b3985",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "de8315cf321cb8038123de71da",
            "9776a7f0c713537fafa550da284c110b76a7",
            "1967cb598473d66a140cda4d9c60143b3985",
        ).map { it.asHexString().toByteArray() }
}