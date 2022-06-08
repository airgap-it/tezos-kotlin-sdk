package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import org.junit.Test
import kotlin.test.*

class Secp256K1ElementTest {

    @Test
    fun `should recognize valid and invalid Secp256K1Element strings`() {
        validStrings.forEach {
            assertTrue(Secp256K1Element.isValid(it), "Expected `$it` to be recognized as valid Secp256K1Element string.")
        }

        invalidStrings.forEach {
            assertFalse(Secp256K1Element.isValid(it), "Expected `$it` to be recognized as invalid Secp256K1Element string.")
        }
    }

    @Test
    fun `should recognize valid and invalid Secp256K1Element bytes`() {
        validBytes.forEach {
            assertTrue(Secp256K1Element.isValid(it), "Expected `$it` to be recognized as valid Secp256K1Element.")
            assertTrue(Secp256K1Element.isValid(it.toList()), "Expected `$it` to be recognized as valid Secp256K1Element.")
        }

        invalidBytes.forEach {
            assertFalse(Secp256K1Element.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid Secp256K1Element.")
            assertFalse(Secp256K1Element.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid Secp256K1Element.")
        }
    }

    @Test
    fun `should create Secp256K1Element from valid string`() {
        validStrings.forEach {
            assertEquals(it, Secp256K1Element.createValue(it).base58)
            assertEquals(it, Secp256K1Element.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create Secp256K1Element from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { Secp256K1Element.createValue(it) }
            assertNull(Secp256K1Element.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "GSp8t6AKU9LexQMNgecop7yQWbrQwaRf6eUMsVNVNaNrXegq82gneV",
            "GSpA8GQY5XQf4827PGWKhvVWyqYY4RPHbTc3MSLf8vFU9Tiqz8iuZ6",
            "GSpFexrFHgJfsSRP8PAC3XvbQQ5hBR5T3gmvMmkHiVHe3zoXNLtZH1",
            "GSpEfLF6n6NFxLrJ2hSFd9Tt7mPZxA26wpjNSHX7tEnGmCCz5qZUh2",
            "GSp8hVKrXkdtdiqXrKU5m1eG5kv64Q2oPXVNhQzjUkq66zQYagJ3JJ",
            "GSpGDBMpYxNK3hiPYFPTuAdk2Zuw2xuhzLrEVMXpARNdmZHokPBdgS",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "8t6AKU9LexQMNgecop7yQWbrQwaRf6eUMsVNVNaNrXegq82gneV",
            "GSpA8GQY5XQf4827PGWKhvVWyqYY4RPHbTc3MSLf8vFU9Tiqz8",
            "GSpFexrFHgJfsSRP8PAC3XvbQQ5hBR5T3gmvMmkHiVHe3zoXNLtZH1EfLF",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "10cebd3a31222a29dbb57ac23764167930624f70c3e236265685b17edb3a02bf10",
            "055c0010cebd3a31222a29dbb57ac23764167930624f70c3e236265685b17edb3a02bf10",
            "35f01eefe5224edd3f00a5fc35b9623caf43546371decd71a5815fbab41d8d6605",
            "055c0035f01eefe5224edd3f00a5fc35b9623caf43546371decd71a5815fbab41d8d6605",
            "daeab3e9086a50dd8df66a40950d9b9f1c5ce4caa9075f91388e5e05e4d4b60194",
            "055c00daeab3e9086a50dd8df66a40950d9b9f1c5ce4caa9075f91388e5e05e4d4b60194",
            "bd44e83083f46cba96baee963b10a7ad2bc123d9a758aa1187265367a94d90825f",
            "055c00bd44e83083f46cba96baee963b10a7ad2bc123d9a758aa1187265367a94d90825f",
            "0b5aae035942444216fcc226ef5f410506ca2c38001c702600210dbccca69b8cd7",
            "055c000b5aae035942444216fcc226ef5f410506ca2c38001c702600210dbccca69b8cd7",
            "eb7d620ff2db991037f79acf15c7a9f578b3b86f96a40b2847cb0388a7f6e0a084",
            "055c00eb7d620ff2db991037f79acf15c7a9f578b3b86f96a40b2847cb0388a7f6e0a084",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "10cebd3a31222a29dbb57ac23764167930624f70c3e236265685b17edb",
            "35f01eefe5224edd3f00a5fc35b9623caf43546371decd71a5815fbab41d8d6605f01e",
            "155c00eb7d620ff2db991037f79acf15c7a9f578b3b86f96a40b2847cb0388a7f6e0a084",
        ).map { it.asHexString().toByteArray() }
}