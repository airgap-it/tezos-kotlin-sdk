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

class KeyBytesCoderTest {

    @MockK
    private lateinit var crypto: Crypto

    private lateinit var keyBytesCoder: KeyBytesCoder

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { crypto.hashSha256(any<ByteArray>()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        val base58Check = Base58Check(Base58(), crypto)
        val base58BytesCoder = Base58BytesCoder(base58Check)

        keyBytesCoder = KeyBytesCoder(base58BytesCoder)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode key to bytes`() {
        keysWithBytes.forEach {
            assertContentEquals(it.second, keyBytesCoder.encode(it.first))
        }
    }

    @Test
    fun `should fail to encode invalid key to bytes`() {
        invalidKeys.forEach {
            assertFailsWith<IllegalArgumentException> {
                keyBytesCoder.encode(it)
            }
        }
    }

    @Test
    fun `should decode key from bytes`() {
        keysWithBytes.forEach {
            assertEquals(it.first, keyBytesCoder.decode(it.second))
        }
    }

    @Test
    fun `should fail to decode key from invalid bytes`() {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> {
                keyBytesCoder.decode(it)
            }
        }
    }

    private val keysWithBytes: List<Pair<String, ByteArray>>
        get() = listOf(
            "edpkuHhTYggbo1d3vRJTtoKy9hFnZGc8Vpr6qEzbZMXWV69odaM3a4" to "0055172873cf63b37a6e5b4ef3058fe13bd1885419325730cadc59fa1a0bdf7273".asHexString().toByteArray(),
            "edpkuD7yUDwnwVyThg8s3THVTem2cvFSub7iJjf4zXMqxoHPcBE1dD" to "004ab2761a1c7560400d0e3c9d26dc14e6368cd33854959e5524855ade324244be".asHexString().toByteArray(),
            "edpkvCMmn8syMgEv9kqkRCRNn5zzUGACxnga9iguRuLnp4wLQuVrkB" to "00cca9abca61275f5b17a51e48dcb73bacbdaa40d62dde2e6b15a79b1fbe3d2acd".asHexString().toByteArray(),
            "sppkCVP3G6y4SsGAiHdR8UUd9dpawhAMpe5RT87F8wHKT7izLgrUncF" to "01952b150170718eb11620bad91c9a6c7196ca7d529645532c708f33dd6b585552f5".asHexString().toByteArray(),
            "sppkDRiBzsjJL9hb3W2GgUJ3g6otXms74a8dVVxs87EcEepV6giLKDr" to "01b11e27b39378558e6ffd2a1092f59fdb9e949ea75183cb482133af03fa63796317".asHexString().toByteArray(),
            "sppk7a3otu8KgoNYvRZxWgMg52ZuKoghT4CumjS3dj7X8sb78Ho6qhQ" to "01026207a23537de0c1c1bb5b5633b56c5a6016848c7fecfb557f235f571c95c4a97".asHexString().toByteArray(),
            "p2pkE3k5ZLRUvXTtjqGesGCZQBQjPE1cZghFFAmZTeQm7WNTwfsqeZg" to "02f045cf3096e3843c3ab59788c0c6d60609cb5bd4a68d23ac1791bd7aa62b841d24".asHexString().toByteArray(),
            "p2pk6zAUidUniZuM9NcFopvt59SWGzYaY7MW6GwYSmC2ya3D21HL7XN" to "021d91cfd7a0e83e45db63396299d438458b372bb7dc19b58f3f1599d1d93784429b".asHexString().toByteArray(),
            "p2pkDdrRjgF3ARitNw9auutDL8ykX7DZxad89gzyNfSuYnxe6t55ksk" to "02e3fb8448c253f3d2274f8283bbc7ef9e6296be5a6d6c0089656b051aa9eee90f04".asHexString().toByteArray(),
        )

    private val invalidKeys: List<String>
        get() = listOf(
            "",
            "invalidKey",
            "edpuHhTYggbo1d3vRJTtoKy9hFnZGc8Vpr6qEzbZMXWV69odaM3a4",
            "edpkuHhTYggbo1d3vRJTtoKy9hFnZGc8Vpr6qEzbZMXWV69odaM3a4F2",
            "edpkuD7yUDwnwVyThg8s3THVTem2cvFSub7iJjf4zXMqxoHPcBE1",
            "spkCVP3G6y4SsGAiHdR8UUd9dpawhAMpe5RT87F8wHKT7izLgrUncF",
            "sppkDRiBzsjJL9hb3W2GgUJ3g6otXms74a8dVVxs87EcEepV6giLKDr82",
            "sppk7a3otu8KgoNYvRZxWgMg52ZuKoghT4CumjS3dj7X8sb78Ho6qhQ543",
            "2pkE3k5ZLRUvXTtjqGesGCZQBQjPE1cZghFFAmZTeQm7WNTwfsqeZg",
            "p2pk6zAUidUniZuM9NcFopvt59SWGzYaY7MW6GwYSmC2ya3D21",
            "p2pkDdrRjgF3ARitNw9auutDL8ykX7DZxad89gzyNfSuYnxe6t55kskg56",
            "edsigtxXiEKRsdooVzfiJJL9SLAmaYg233QfsmEjcLuauLFJwE2MChg48rZ544YLDQDWf7nzBmLECC3FT5CW2rEhAFJAitfYk2J",
            "sigeX278dsWiXFVYJmhXWo4nh3Dwzg1H3GFLn38bK9sBHZ8at3r2Bk8MGkA2ZvKuVcHYmfLPJht5Uua9podB6nLdSpoWLqoz",
            "tz2Mqgq7nrEXD51JkMe85w3H2WT3qMwz3Emt",
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "".asHexString().toByteArray(),
            "0055172873cf63b37a6e5b4ef3058fe13bd1885419325730cadc59fa1a0bdf72".asHexString().toByteArray(),
            "4ab2761a1c7560400d0e3c9d26dc14e6368cd33854959e5524855ade324244be".asHexString().toByteArray(),
            "00cca9abca61275f5b17a51e48dcb73bacbdaa40d62dde2e6b15a79b1fbe3d2acd23".asHexString().toByteArray(),
            "01952b150170718eb11620bad91c9a6c7196ca7d529645532c708f33dd6b585552".asHexString().toByteArray(),
            "b11e27b39378558e6ffd2a1092f59fdb9e949ea75183cb482133af03fa63796317".asHexString().toByteArray(),
            "01026207a23537de0c1c1bb5b5633b56c5a6016848c7fecfb557f235f571c95c4a97a5".asHexString().toByteArray(),
            "02f045cf3096e3843c3ab59788c0c6d60609cb5bd4a68d23ac1791bd7aa62b841d".asHexString().toByteArray(),
            "1d91cfd7a0e83e45db63396299d438458b372bb7dc19b58f3f1599d1d93784429b".asHexString().toByteArray(),
            "02e3fb8448c253f3d2274f8283bbc7ef9e6296be5a6d6c0089656b051aa9eee90f0442".asHexString().toByteArray(),
        )
}