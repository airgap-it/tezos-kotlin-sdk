package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class Ed25519PublicKeyTest {

    @Test
    fun `should recognize valid and invalid Ed25519PublicKey strings`() {
        validStrings.forEach {
            assertTrue(Ed25519PublicKey.isValid(it), "Expected `$it` to be recognized as valid Ed25519PublicKey string.")
        }

        invalidStrings.forEach {
            assertFalse(Ed25519PublicKey.isValid(it), "Expected `$it` to be recognized as invalid Ed25519PublicKey string.")
        }
    }

    @Test
    fun `should recognize valid and invalid Ed25519PublicKey bytes`() {
        validBytes.forEach {
            assertTrue(Ed25519PublicKey.isValid(it), "Expected `$it` to be recognized as valid Ed25519PublicKey.")
            assertTrue(Ed25519PublicKey.isValid(it.toList()), "Expected `$it` to be recognized as valid Ed25519PublicKey.")
        }

        invalidBytes.forEach {
            assertFalse(Ed25519PublicKey.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid Ed25519PublicKey.")
            assertFalse(Ed25519PublicKey.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid Ed25519PublicKey.")
        }
    }

    @Test
    fun `should create Ed25519PublicKey from valid string`() {
        validStrings.forEach {
            assertEquals(it, Ed25519PublicKey.createValue(it).base58)
            assertEquals(it, Ed25519PublicKey.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create Ed25519PublicKey from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { Ed25519PublicKey.createValue(it) }
            assertNull(Ed25519PublicKey.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "edpkuF5y5V7NNH5xKMCKHHqVDzq6YuUXiPT3FFjA9CGnht6xCgziTe",
            "edpkvGJQQYtR7cbNyp5KLAc7XD7wHqFdjxmcGWNis6SmGHRAdHxnFn",
            "edpku92jFufmi4bQ2K5szShHLJkJyjTMEZumcgFkdw1PvUDNHvpmC7",
            "edpkvSzhcyVLHgCQaqDB5QEkheetiCEGkTn5PJUYSZceJK4CjfgLyf",
            "edpku4Ze3BHAX9nVEjZu3KFqbdp3tPr7ucy3FMBLQ8ijcbrVQADbSU",
            "edpkv9S6FN8TXNdy7Sue6FsUidJ7jGvngLJs3xVjD8c5RngEip1Q14",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "uF5y5V7NNH5xKMCKHHqVDzq6YuUXiPT3FFjA9CGnht6xCgziTe",
            "edpkvGJQQYtR7cbNyp5KLAc7XD7wHqFdjxmcGWNis6SmGHRAdH",
            "edpku92jFufmi4bQ2K5szShHLJkJyjTMEZumcgFkdw1PvUDNHvpmC7kvSz",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "4f28e87a1bb7dcd52359ba8438dddefd6c5bcc89c49bd255f79fa9cf229a1f2c",
            "0d0f25d94f28e87a1bb7dcd52359ba8438dddefd6c5bcc89c49bd255f79fa9cf229a1f2c",
            "d59d07779e17b4b7a52f05b6b61e3dfd6c108e026b8b3563d6718d1c3168d07d",
            "0d0f25d9d59d07779e17b4b7a52f05b6b61e3dfd6c108e026b8b3563d6718d1c3168d07d",
            "4168c729547dfb24329054435e6c7eff094e09448d73b89807d890295eaf8488",
            "0d0f25d94168c729547dfb24329054435e6c7eff094e09448d73b89807d890295eaf8488",
            "ede5addc5168c905417f0e4fb96c933ffa02c380170361336e0d5be92d25c389",
            "0d0f25d9ede5addc5168c905417f0e4fb96c933ffa02c380170361336e0d5be92d25c389",
            "374429cb973086aade4ae27370caff53c81b273effc524038753d9a1718507b0",
            "0d0f25d9374429cb973086aade4ae27370caff53c81b273effc524038753d9a1718507b0",
            "c6051cdcf6be6c42151e1ed4c2a7980b9e87b5d3bc23371c4bd38df3333bec9a",
            "0d0f25d9c6051cdcf6be6c42151e1ed4c2a7980b9e87b5d3bc23371c4bd38df3333bec9a",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "4f28e87a1bb7dcd52359ba8438dddefd6c5bcc89c49bd255f79fa9",
            "d59d07779e17b4b7a52f05b6b61e3dfd6c108e026b8b3563d6718d1c3168d07d9d07",
            "1d0f25d9c6051cdcf6be6c42151e1ed4c2a7980b9e87b5d3bc23371c4bd38df3333bec9a",
        ).map { it.asHexString().toByteArray() }
}