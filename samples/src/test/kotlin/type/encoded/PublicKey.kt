package type.encoded

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.converter.encoded.PublicKey
import it.airgap.tezos.core.type.encoded.Ed25519PublicKey
import it.airgap.tezos.core.type.encoded.P256PublicKey
import it.airgap.tezos.core.type.encoded.PublicKey
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKey
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class PublicKeySamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val edPublicKey = "edpkuHhTYggbo1d3vRJTtoKy9hFnZGc8Vpr6qEzbZMXWV69odaM3a4"
            assertTrue(PublicKey.isValid(edPublicKey))

            val spPublicKey = "sppkCVP3G6y4SsGAiHdR8UUd9dpawhAMpe5RT87F8wHKT7izLgrUncF"
            assertTrue(PublicKey.isValid(spPublicKey))

            val p2PublicKey = "p2pkE3k5ZLRUvXTtjqGesGCZQBQjPE1cZghFFAmZTeQm7WNTwfsqeZg"
            assertTrue(PublicKey.isValid(p2PublicKey))

            val unknownKey = "uHhTYggbo1d3vRJTtoKy9hFnZGc8Vpr6qEzbZMXWV69odaM3a4"
            assertFalse(PublicKey.isValid(unknownKey))
        }

        @Test
        fun create() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val edPublicKey = PublicKey("edpkuHhTYggbo1d3vRJTtoKy9hFnZGc8Vpr6qEzbZMXWV69odaM3a4")
            assertEquals(Ed25519PublicKey("edpkuHhTYggbo1d3vRJTtoKy9hFnZGc8Vpr6qEzbZMXWV69odaM3a4"), edPublicKey)

            val spPublicKey = PublicKey("sppkCVP3G6y4SsGAiHdR8UUd9dpawhAMpe5RT87F8wHKT7izLgrUncF")
            assertEquals(Secp256K1PublicKey("sppkCVP3G6y4SsGAiHdR8UUd9dpawhAMpe5RT87F8wHKT7izLgrUncF"), spPublicKey)

            val p2PublicKey = PublicKey("p2pkE3k5ZLRUvXTtjqGesGCZQBQjPE1cZghFFAmZTeQm7WNTwfsqeZg")
            assertEquals(P256PublicKey("p2pkE3k5ZLRUvXTtjqGesGCZQBQjPE1cZghFFAmZTeQm7WNTwfsqeZg"), p2PublicKey)
        }
    }

    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val edPublicKey = PublicKey("edpkuHhTYggbo1d3vRJTtoKy9hFnZGc8Vpr6qEzbZMXWV69odaM3a4")
            assertContentEquals(hexToBytes("0055172873cf63b37a6e5b4ef3058fe13bd1885419325730cadc59fa1a0bdf7273"), edPublicKey.encodeToBytes())

            val spPublicKey = PublicKey("sppkCVP3G6y4SsGAiHdR8UUd9dpawhAMpe5RT87F8wHKT7izLgrUncF")
            assertContentEquals(hexToBytes("01952b150170718eb11620bad91c9a6c7196ca7d529645532c708f33dd6b585552f5"), spPublicKey.encodeToBytes())

            val p2PublicKey = PublicKey("p2pkE3k5ZLRUvXTtjqGesGCZQBQjPE1cZghFFAmZTeQm7WNTwfsqeZg")
            assertContentEquals(hexToBytes("02f045cf3096e3843c3ab59788c0c6d60609cb5bd4a68d23ac1791bd7aa62b841d24"), p2PublicKey.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val edPublicKey = hexToBytes("0055172873cf63b37a6e5b4ef3058fe13bd1885419325730cadc59fa1a0bdf7273")
            assertEquals(Ed25519PublicKey("edpkuHhTYggbo1d3vRJTtoKy9hFnZGc8Vpr6qEzbZMXWV69odaM3a4"), PublicKey.decodeFromBytes(edPublicKey))

            val spPublicKey = hexToBytes("01952b150170718eb11620bad91c9a6c7196ca7d529645532c708f33dd6b585552f5")
            assertEquals(Secp256K1PublicKey("sppkCVP3G6y4SsGAiHdR8UUd9dpawhAMpe5RT87F8wHKT7izLgrUncF"), PublicKey.decodeFromBytes(spPublicKey))

            val p2PublicKey = hexToBytes("02f045cf3096e3843c3ab59788c0c6d60609cb5bd4a68d23ac1791bd7aa62b841d24")
            assertEquals(P256PublicKey("p2pkE3k5ZLRUvXTtjqGesGCZQBQjPE1cZghFFAmZTeQm7WNTwfsqeZg"), PublicKey.decodeFromBytes(p2PublicKey))
        }
    }
}
            