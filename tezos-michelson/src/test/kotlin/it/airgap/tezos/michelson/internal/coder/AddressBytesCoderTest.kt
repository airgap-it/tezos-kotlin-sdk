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
import java.security.MessageDigest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AddressBytesCoderTest {

    @MockK
    private lateinit var crypto: Crypto

    private lateinit var addressBytesCoder: AddressBytesCoder

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { crypto.hashSha256(any<ByteArray>()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        val base58Check = Base58Check(Base58(), crypto)

        val base58BytesCoder = Base58BytesCoder(base58Check)
        val keyHashBytesCoder = KeyHashBytesCoder(base58BytesCoder)

        addressBytesCoder = AddressBytesCoder(keyHashBytesCoder, base58BytesCoder)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode address to bytes`() {
        addressesWithBytes.forEach {
            assertContentEquals(it.second, addressBytesCoder.encode(it.first))
        }
    }

    @Test
    fun `should fail to encode invalid address to bytes`() {
        invalidAddresses.forEach {
            assertFailsWith<IllegalArgumentException> {
                addressBytesCoder.encode(it)
            }
        }
    }

    @Test
    fun `should decode address from bytes`() {
        addressesWithBytes.forEach {
            assertEquals(it.first, addressBytesCoder.decode(it.second))
        }
    }

    @Test
    fun `should fail to decode address from invalid bytes`() {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> {
                addressBytesCoder.decode(it)
            }
        }
    }

    private val addressesWithBytes: List<Pair<String, ByteArray>>
        get() = listOf(
            "tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e" to "0000d7a60d4e90e8a33ec835159191b14ce4452f12f8".asHexString().toByteArray(),
            "tz1VmzGTgLxb2qw8tXQvvM5QMRigMMoStTDR" to "00006f328fa5c2ea6fd8596b63eafeabf91a78d37ae0".asHexString().toByteArray(),
            "tz1hByhpsNVUsEHndaAaqk6ikEwpeCKQdUZe" to "0000ec6575487c0f706d9f936a33a0dd5a2f7f822502".asHexString().toByteArray(),
            "tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy" to "00011a868789216112194b3bb003e8384c8f7217b288".asHexString().toByteArray(),
            "tz2Mqgq7nrEXD51JkMe85w3H2WT3qMwz3Emt" to "0001945bf5622be703dcce774f9d940ed307b52905f5".asHexString().toByteArray(),
            "tz2JEDwptRu3P69Sv7UhVqzN3shFR2vSZzdk" to "00016cbe94a25b5de8b2aede4816d7e744c761d1c39e".asHexString().toByteArray(),
            "tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B" to "00021a78f4332a6fe15b979904c6c2e5f9521e1ffc4a".asHexString().toByteArray(),
            "tz3imABPfVxJ33MAW9uFVnvV4qVSiU69AQut" to "0002f612a5590e21bcb4cabc85b30dbbe5f63533e0e7".asHexString().toByteArray(),
            "tz3RA3NP8Y8MnuJSzUZCvpyD3AsLZ6CG9h5L" to "000234f443872ae83d91d50747de6abf6a125d63112b".asHexString().toByteArray(),
            "KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7" to "016077cd98fd8aca94851b83a4c44203b705d2004b".asHexString().toByteArray(),
            "KT1PMSWmevCWDhSRqxkRTknQNyfdJetctoab" to "01a2049364703140f6e6f83b54e15021d63ed3700a".asHexString().toByteArray(),
            "KT1WoDngLZcTnKdW9KDray2BH8PbGW5BWPhT" to "01f3ada5fa3d0003009b493ae4b1af6bf5e7cd6963".asHexString().toByteArray(),
        )

    private val invalidAddresses: List<String>
        get() = listOf(
            "",
            "invalidAddress",
            "tz1fJGtrdmckD3VkiDxqUEci5h4gGcvoc",
            "tz2AjVPbMHdDF1XwHVhUrTg6ZvqY8",
            "tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV6",
            "tz0fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e",
            "tzfJGtrdmckD3VkiDxqUEci5h4gGcvocw6e",
            "KT1HNqxFJxnmUcX8wF915wxxaAAU4ixDwW",
            "kt1HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7",
            "KT0HNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7",
            "KTHNqxFJxnmUcX8wF915wxxaAAU4ixDwWQ7",
            "edsigtxXiEKRsdooVzfiJJL9SLAmaYg233QfsmEjcLuauLFJwE2MChg48rZ544YLDQDWf7nzBmLECC3FT5CW2rEhAFJAitfYk2J",
            "sigeX278dsWiXFVYJmhXWo4nh3Dwzg1H3GFLn38bK9sBHZ8at3r2Bk8MGkA2ZvKuVcHYmfLPJht5Uua9podB6nLdSpoWLqoz",
            "sppk7a3otu8KgoNYvRZxWgMg52ZuKoghT4CumjS3dj7X8sb78Ho6qhQ",
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "".asHexString().toByteArray(),
            "0000d7a60d4e90e8a33ec835159191b14ce4452f12".asHexString().toByteArray(),
            "006f328fa5c2ea6fd8596b63eafeabf91a78d37ae0".asHexString().toByteArray(),
            "0000ec6575487c0f706d9f936a33a0dd5a2f7f822502a5".asHexString().toByteArray(),
            "00011a868789216112194b3bb003e8384c8f7217b2".asHexString().toByteArray(),
            "00945bf5622be703dcce774f9d940ed307b52905f5".asHexString().toByteArray(),
            "00016cbe94a25b5de8b2aede4816d7e744c761d1c39e73".asHexString().toByteArray(),
            "00021a78f4332a6fe15b979904c6c2e5f9521e1ffc".asHexString().toByteArray(),
            "00f612a5590e21bcb4cabc85b30dbbe5f63533e0e7".asHexString().toByteArray(),
            "000234f443872ae83d91d50747de6abf6a125d63112b26".asHexString().toByteArray(),
            "016077cd98fd8aca94851b83a4c44203b705d200".asHexString().toByteArray(),
            "a2049364703140f6e6f83b54e15021d63ed3700a".asHexString().toByteArray(),
            "01f3ada5fa3d0003009b493ae4b1af6bf5e7cd6963d8".asHexString().toByteArray(),
        )
}