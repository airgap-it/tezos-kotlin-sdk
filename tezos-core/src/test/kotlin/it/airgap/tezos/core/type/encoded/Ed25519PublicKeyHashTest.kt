package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import org.junit.Test
import kotlin.test.*

class Ed25519PublicKeyHashTest {

    @Test
    fun `should recognize valid and invalid Ed25519PublicKeyHash strings`() {
        validStrings.forEach {
            assertTrue(Ed25519PublicKeyHash.isValid(it), "Expected `$it` to be recognized as valid Ed25519PublicKeyHash string.")
        }

        invalidStrings.forEach {
            assertFalse(Ed25519PublicKeyHash.isValid(it), "Expected `$it` to be recognized as invalid Ed25519PublicKeyHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid Ed25519PublicKeyHash bytes`() {
        validBytes.forEach {
            assertTrue(Ed25519PublicKeyHash.isValid(it), "Expected `$it` to be recognized as valid Ed25519PublicKeyHash.")
            assertTrue(Ed25519PublicKeyHash.isValid(it.toList()), "Expected `$it` to be recognized as valid Ed25519PublicKeyHash.")
        }

        invalidBytes.forEach {
            assertFalse(Ed25519PublicKeyHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid Ed25519PublicKeyHash.")
            assertFalse(Ed25519PublicKeyHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid Ed25519PublicKeyHash.")
        }
    }

    @Test
    fun `should create Ed25519PublicKeyHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, Ed25519PublicKeyHash.createValue(it).base58)
            assertEquals(it, Ed25519PublicKeyHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create Ed25519PublicKeyHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { Ed25519PublicKeyHash.createValue(it) }
            assertNull(Ed25519PublicKeyHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "tz1Z9oh78VxaDbKJqhLKWsiteTSkBiJFkpcr",
            "tz1c461FPaZi2RCXktbnc2KNqCBFH61S6BtD",
            "tz1ga2nGJdKgq17Wwrpyy3MzM3c6qPr19Fzm",
            "tz1TE8QY62UzLqsU6CLukH6prVfjm7tqxMSd",
            "tz1YqQ9AZo4hMS1TkQnddTcvd4FciPDVb9F4",
            "tz1R7ReRLKhvWCxPseeeZiFPouwqSzg8ugwH",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "Z9oh78VxaDbKJqhLKWsiteTSkBiJFkpcr",
            "tz1c461FPaZi2RCXktbnc2KNqCBFH61S",
            "tz1ga2nGJdKgq17Wwrpyy3MzM3c6qPr19FzmTE8Q",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "943b4312dbf184b88d2b31e90be9cb0996e696b0",
            "06a19f943b4312dbf184b88d2b31e90be9cb0996e696b0",
            "b40ec2c58ec749e5c6413b41dae8616af10463dc",
            "06a19fb40ec2c58ec749e5c6413b41dae8616af10463dc",
            "e5990d3a1bfe31a737ca8c260ae3814be031574d",
            "06a19fe5990d3a1bfe31a737ca8c260ae3814be031574d",
            "533bcb06f47f89a64f829dbb7e9bdb294686fd10",
            "06a19f533bcb06f47f89a64f829dbb7e9bdb294686fd10",
            "90c01d9530e8a2f010737dcecfa88aadcf26a0e2",
            "06a19f90c01d9530e8a2f010737dcecfa88aadcf26a0e2",
            "3c07053e05348eae56e5d009a06a6d9fecb384af",
            "06a19f3c07053e05348eae56e5d009a06a6d9fecb384af",
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
