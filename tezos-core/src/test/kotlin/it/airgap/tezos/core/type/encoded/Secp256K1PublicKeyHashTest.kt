package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class Secp256K1PublicKeyHashTest {

    @Test
    fun `should recognize valid and invalid Secp256K1PublicKeyHash strings`() {
        validStrings.forEach {
            assertTrue(Secp256K1PublicKeyHash.isValid(it), "Expected `$it` to be recognized as valid Secp256K1PublicKeyHash string.")
        }

        invalidStrings.forEach {
            assertFalse(Secp256K1PublicKeyHash.isValid(it), "Expected `$it` to be recognized as invalid Secp256K1PublicKeyHash string.")
        }
    }

    @Test
    fun `should recognize valid and invalid Secp256K1PublicKeyHash bytes`() {
        validBytes.forEach {
            assertTrue(Secp256K1PublicKeyHash.isValid(it), "Expected `$it` to be recognized as valid Secp256K1PublicKeyHash.")
            assertTrue(Secp256K1PublicKeyHash.isValid(it.toList()), "Expected `$it` to be recognized as valid Secp256K1PublicKeyHash.")
        }

        invalidBytes.forEach {
            assertFalse(Secp256K1PublicKeyHash.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid Secp256K1PublicKeyHash.")
            assertFalse(Secp256K1PublicKeyHash.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid Secp256K1PublicKeyHash.")
        }
    }

    @Test
    fun `should create Secp256K1PublicKeyHash from valid string`() {
        validStrings.forEach {
            assertEquals(it, Secp256K1PublicKeyHash.createValue(it).base58)
            assertEquals(it, Secp256K1PublicKeyHash.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create Secp256K1PublicKeyHash from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { Secp256K1PublicKeyHash.createValue(it) }
            assertNull(Secp256K1PublicKeyHash.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "tz2KQXryA14TgvXv2KmsaAeXrk5u3Jy4zTDj",
            "tz2SVFy5cx3uXvy3mGiHRdrSto5qqXcFeCvb",
            "tz2Vkrb2evBikgS7V5DwXXFhAJoAYjJuhLAE",
            "tz28fXQsdaryVnHqHmXzoHuSarDyqgS3ejSH",
            "tz2S6s3HhQ8Ax25LrmA8GfXqdaNhXq3MC57d",
            "tz2MiPwmQeV3GTAYKFXeN35zuZRrPgL3npqh",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "KQXryA14TgvXv2KmsaAeXrk5u3Jy4zTDj",
            "tz2SVFy5cx3uXvy3mGiHRdrSto5qqXcF",
            "tz2Vkrb2evBikgS7V5DwXXFhAJoAYjJuhLAE8fXQ",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "79a9d25d5f227def7c72cfebffb6402967ee31a8",
            "06a1a179a9d25d5f227def7c72cfebffb6402967ee31a8",
            "c757838c91c77ffe03062713e6196f35e15163c3",
            "06a1a1c757838c91c77ffe03062713e6196f35e15163c3",
            "eb3307a11d528afaf092e15ff4dfa206f20640aa",
            "06a1a1eb3307a11d528afaf092e15ff4dfa206f20640aa",
            "03d64d4de4e667dc868a6cf5497074ec1c0b65ba",
            "06a1a103d64d4de4e667dc868a6cf5497074ec1c0b65ba",
            "c31b394d69892acf0d8252b9609c83e31f00e45e",
            "06a1a1c31b394d69892acf0d8252b9609c83e31f00e45e",
            "92faf39c10cb5712f3ce42b33c782cf25a01999f",
            "06a1a192faf39c10cb5712f3ce42b33c782cf25a01999f",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "79a9d25d5f227def7c72cfebffb64029",
            "c757838c91c77ffe03062713e6196f35e15163c35783",
            "16a1a192faf39c10cb5712f3ce42b33c782cf25a01999f",
        ).map { it.asHexString().toByteArray() }
}
