package it.airgap.tezos.core.internal.coder.encoded

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeConsumingFromBytes
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.type.encoded.Ed25519PublicKey
import it.airgap.tezos.core.type.encoded.P256PublicKey
import it.airgap.tezos.core.type.encoded.PublicKey
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKey
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PublicKeyBytesCoderTest {

    private lateinit var tezos: Tezos
    private lateinit var publicKeyBytesCoder: PublicKeyBytesCoder

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        publicKeyBytesCoder = PublicKeyBytesCoder(tezos.core().dependencyRegistry.encodedBytesCoder)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode PublicKeyEncoded to bytes`() {
        keysWithBytes.forEach {
            assertContentEquals(it.second, publicKeyBytesCoder.encode(it.first))
            assertContentEquals(it.second, it.first.encodeToBytes(tezos))
            assertContentEquals(it.second, it.first.encodeToBytes(publicKeyBytesCoder))
        }
    }

    @Test
    fun `should decode PublicKeyEncoded from bytes`() {
        keysWithBytes.forEach {
            assertEquals(it.first, publicKeyBytesCoder.decode(it.second))
            assertEquals(it.first, publicKeyBytesCoder.decodeConsuming(it.second.toMutableList()))
            assertEquals(it.first, PublicKey.decodeFromBytes(it.second, publicKeyBytesCoder))
            assertEquals(it.first, PublicKey.decodeFromBytes(it.second, tezos))
            assertEquals(it.first, PublicKey.decodeConsumingFromBytes(it.second.toMutableList(), publicKeyBytesCoder))
        }
    }

    @Test
    fun `should fail to decode PublicKeyEncoded from invalid bytes`() {
        listOf(
            invalidBytes,
            listOf(
                "00cca9abca61275f5b17a51e48dcb73bacbdaa40d62dde2e6b15a79b1fbe3d2acd23".asHexString().toByteArray(),
                "01026207a23537de0c1c1bb5b5633b56c5a6016848c7fecfb557f235f571c95c4a97a5".asHexString().toByteArray(),
                "02e3fb8448c253f3d2274f8283bbc7ef9e6296be5a6d6c0089656b051aa9eee90f0442".asHexString().toByteArray(),
            ),
        ).flatten().forEach {
            assertFailsWith<IllegalArgumentException> { publicKeyBytesCoder.decode(it) }
            assertFailsWith<IllegalArgumentException> { PublicKey.decodeFromBytes(it, tezos) }
            assertFailsWith<IllegalArgumentException> { PublicKey.decodeFromBytes(it, publicKeyBytesCoder) }
        }

        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { publicKeyBytesCoder.decodeConsuming(it.toMutableList()) }
            assertFailsWith<IllegalArgumentException> { PublicKey.decodeConsumingFromBytes(it.toMutableList(), publicKeyBytesCoder) }
        }
    }

    private val keysWithBytes: List<Pair<PublicKey, ByteArray>>
        get() = listOf(
            Ed25519PublicKey("edpkuHhTYggbo1d3vRJTtoKy9hFnZGc8Vpr6qEzbZMXWV69odaM3a4") to "0055172873cf63b37a6e5b4ef3058fe13bd1885419325730cadc59fa1a0bdf7273".asHexString().toByteArray(),
            Ed25519PublicKey("edpkuD7yUDwnwVyThg8s3THVTem2cvFSub7iJjf4zXMqxoHPcBE1dD") to "004ab2761a1c7560400d0e3c9d26dc14e6368cd33854959e5524855ade324244be".asHexString().toByteArray(),
            Ed25519PublicKey("edpkvCMmn8syMgEv9kqkRCRNn5zzUGACxnga9iguRuLnp4wLQuVrkB") to "00cca9abca61275f5b17a51e48dcb73bacbdaa40d62dde2e6b15a79b1fbe3d2acd".asHexString().toByteArray(),
            Secp256K1PublicKey("sppkCVP3G6y4SsGAiHdR8UUd9dpawhAMpe5RT87F8wHKT7izLgrUncF") to "01952b150170718eb11620bad91c9a6c7196ca7d529645532c708f33dd6b585552f5".asHexString().toByteArray(),
            Secp256K1PublicKey("sppkDRiBzsjJL9hb3W2GgUJ3g6otXms74a8dVVxs87EcEepV6giLKDr") to "01b11e27b39378558e6ffd2a1092f59fdb9e949ea75183cb482133af03fa63796317".asHexString().toByteArray(),
            Secp256K1PublicKey("sppk7a3otu8KgoNYvRZxWgMg52ZuKoghT4CumjS3dj7X8sb78Ho6qhQ") to "01026207a23537de0c1c1bb5b5633b56c5a6016848c7fecfb557f235f571c95c4a97".asHexString().toByteArray(),
            P256PublicKey("p2pkE3k5ZLRUvXTtjqGesGCZQBQjPE1cZghFFAmZTeQm7WNTwfsqeZg") to "02f045cf3096e3843c3ab59788c0c6d60609cb5bd4a68d23ac1791bd7aa62b841d24".asHexString().toByteArray(),
            P256PublicKey("p2pk6zAUidUniZuM9NcFopvt59SWGzYaY7MW6GwYSmC2ya3D21HL7XN") to "021d91cfd7a0e83e45db63396299d438458b372bb7dc19b58f3f1599d1d93784429b".asHexString().toByteArray(),
            P256PublicKey("p2pkDdrRjgF3ARitNw9auutDL8ykX7DZxad89gzyNfSuYnxe6t55ksk") to "02e3fb8448c253f3d2274f8283bbc7ef9e6296be5a6d6c0089656b051aa9eee90f04".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "".asHexString().toByteArray(),
            "0055172873cf63b37a6e5b4ef3058fe13bd1885419325730cadc59fa1a0bdf72".asHexString().toByteArray(),
            "4ab2761a1c7560400d0e3c9d26dc14e6368cd33854959e5524855ade324244be".asHexString().toByteArray(),
            "01952b150170718eb11620bad91c9a6c7196ca7d529645532c708f33dd6b585552".asHexString().toByteArray(),
            "b11e27b39378558e6ffd2a1092f59fdb9e949ea75183cb482133af03fa63796317".asHexString().toByteArray(),
            "02f045cf3096e3843c3ab59788c0c6d60609cb5bd4a68d23ac1791bd7aa62b841d".asHexString().toByteArray(),
            "1d91cfd7a0e83e45db63396299d438458b372bb7dc19b58f3f1599d1d93784429b".asHexString().toByteArray(),
        )
}