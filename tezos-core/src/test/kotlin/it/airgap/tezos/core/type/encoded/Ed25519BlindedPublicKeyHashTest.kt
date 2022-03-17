package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
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
            "btz1ZqQDoqbzUenjA5XXXa2EtBNTberdjj7Tt",
            "btz1cjgXx6gbcTccNzinzfAqNN7C6kES8c3Np",
            "btz1hFdJy1jMbGCXNBh2C2BsysxcxJYJ9UtY4",
            "btz1TuiwEo8Wtn3HKL2Y7oRcpPQgbEGMH8dcK",
            "btz1ZWzfsGu6bndRJzEyqgc8v9yGUBXdmAnou",
            "btz1Ro2B83RjpwQNF7UqrcrmPLpxgv96oZvmN",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "Z9oh78VxaDbKJqhLKWsiteTSkBiJFkpcr",
            "btz1ZqQDoqbzUenjA5XXXa2EtBNT",
            "btz1cjgXx6gbcTccNzinzfAqNN7C6kES8c3Np5Ag",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "943b4312dbf184b88d2b31e90be9cb0996e696b0",
            "010231df943b4312dbf184b88d2b31e90be9cb0996e696b0",
            "b40ec2c58ec749e5c6413b41dae8616af10463dc",
            "010231dfb40ec2c58ec749e5c6413b41dae8616af10463dc",
            "e5990d3a1bfe31a737ca8c260ae3814be031574d",
            "010231dfe5990d3a1bfe31a737ca8c260ae3814be031574d",
            "533bcb06f47f89a64f829dbb7e9bdb294686fd10",
            "010231df533bcb06f47f89a64f829dbb7e9bdb294686fd10",
            "90c01d9530e8a2f010737dcecfa88aadcf26a0e2",
            "010231df90c01d9530e8a2f010737dcecfa88aadcf26a0e2",
            "3c07053e05348eae56e5d009a06a6d9fecb384af",
            "010231df3c07053e05348eae56e5d009a06a6d9fecb384af",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "943b4312dbf184b88d2b31e90be9cb09",
            "b40ec2c58ec749e5c6413b41dae8616af10463dc0ec2",
            "16a19f3c07053e05348eae56e5d009a06a6d9fecb384af",
        ).map { it.asHexString().toByteArray() }
}
