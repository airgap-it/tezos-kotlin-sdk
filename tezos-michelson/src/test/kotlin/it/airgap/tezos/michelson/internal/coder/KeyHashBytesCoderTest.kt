package it.airgap.tezos.michelson.internal.coder

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.internal.base58.Base58
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.coder.Base58BytesCoder
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.internal.utils.asHexString
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class KeyHashBytesCoderTest {

    @MockK
    private lateinit var crypto: Crypto

    private lateinit var keyHashBytesCoder: KeyHashBytesCoder

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { crypto.hashSha256(any<ByteArray>()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        val base58Check = Base58Check(Base58(), crypto)
        val base58BytesCoder = Base58BytesCoder(base58Check)

        keyHashBytesCoder = KeyHashBytesCoder(base58BytesCoder)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode key hash to bytes`() {
        keyHashesWithBytes.forEach {
            assertContentEquals(it.second, keyHashBytesCoder.encode(it.first))
        }
    }

    @Test
    fun `should fail to encode invalid key hash to bytes`() {
        invalidKeyHashes.forEach {
            assertFailsWith<IllegalArgumentException> {
                keyHashBytesCoder.encode(it)
            }
        }
    }

    @Test
    fun `should decode key hash from bytes`() {
        keyHashesWithBytes.forEach {
            assertEquals(it.first, keyHashBytesCoder.decode(it.second))
        }
    }

    @Test
    fun `should fail to decode key hash from invalid bytes`() {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> {
                keyHashBytesCoder.decode(it)
            }
        }
    }

    private val keyHashesWithBytes: List<Pair<String, ByteArray>>
        get() = listOf(
            "tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e" to "00d7a60d4e90e8a33ec835159191b14ce4452f12f8".asHexString().toByteArray(),
            "tz1VmzGTgLxb2qw8tXQvvM5QMRigMMoStTDR" to "006f328fa5c2ea6fd8596b63eafeabf91a78d37ae0".asHexString().toByteArray(),
            "tz1hByhpsNVUsEHndaAaqk6ikEwpeCKQdUZe" to "00ec6575487c0f706d9f936a33a0dd5a2f7f822502".asHexString().toByteArray(),
            "tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy" to "011a868789216112194b3bb003e8384c8f7217b288".asHexString().toByteArray(),
            "tz2Mqgq7nrEXD51JkMe85w3H2WT3qMwz3Emt" to "01945bf5622be703dcce774f9d940ed307b52905f5".asHexString().toByteArray(),
            "tz2JEDwptRu3P69Sv7UhVqzN3shFR2vSZzdk" to "016cbe94a25b5de8b2aede4816d7e744c761d1c39e".asHexString().toByteArray(),
            "tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B" to "021a78f4332a6fe15b979904c6c2e5f9521e1ffc4a".asHexString().toByteArray(),
            "tz3imABPfVxJ33MAW9uFVnvV4qVSiU69AQut" to "02f612a5590e21bcb4cabc85b30dbbe5f63533e0e7".asHexString().toByteArray(),
            "tz3RA3NP8Y8MnuJSzUZCvpyD3AsLZ6CG9h5L" to "0234f443872ae83d91d50747de6abf6a125d63112b".asHexString().toByteArray(),
        )

    private val invalidKeyHashes: List<String>
        get() = listOf(
            "",
            "invalidAddress",
            "tz1fJGtrdmckD3VkiDxqUEci5h4gGcvoc",
            "tz2AjVPbMHdDF1XwHVhUrTg6ZvqY8",
            "tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV6",
            "tz0fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e",
            "tzfJGtrdmckD3VkiDxqUEci5h4gGcvocw6e",
            "edsigtxXiEKRsdooVzfiJJL9SLAmaYg233QfsmEjcLuauLFJwE2MChg48rZ544YLDQDWf7nzBmLECC3FT5CW2rEhAFJAitfYk2J",
            "sigeX278dsWiXFVYJmhXWo4nh3Dwzg1H3GFLn38bK9sBHZ8at3r2Bk8MGkA2ZvKuVcHYmfLPJht5Uua9podB6nLdSpoWLqoz",
            "sppk7a3otu8KgoNYvRZxWgMg52ZuKoghT4CumjS3dj7X8sb78Ho6qhQ",
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "".asHexString().toByteArray(),
            "00d7a60d4e90e8a33ec835159191b14ce4452f12".asHexString().toByteArray(),
            "6f328fa5c2ea6fd8596b63eafeabf91a78d37ae0".asHexString().toByteArray(),
            "00ec6575487c0f706d9f936a33a0dd5a2f7f822502a5".asHexString().toByteArray(),
            "011a868789216112194b3bb003e8384c8f7217b2".asHexString().toByteArray(),
            "945bf5622be703dcce774f9d940ed307b52905f5".asHexString().toByteArray(),
            "016cbe94a25b5de8b2aede4816d7e744c761d1c39e73".asHexString().toByteArray(),
            "021a78f4332a6fe15b979904c6c2e5f9521e1ffc".asHexString().toByteArray(),
            "f612a5590e21bcb4cabc85b30dbbe5f63533e0e7".asHexString().toByteArray(),
            "0234f443872ae83d91d50747de6abf6a125d63112b26".asHexString().toByteArray(),
        )
}