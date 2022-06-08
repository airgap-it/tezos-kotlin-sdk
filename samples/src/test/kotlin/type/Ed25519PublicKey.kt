package type

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.Ed25519PublicKey
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class Ed25519PublicKeySamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val publicKey = "edpktmJqEE79FtfdWse1gqnUey1vNBkB3zNV99Pi95SRAs8NMatczG"
            assertTrue(Ed25519PublicKey.isValid(publicKey))

            val unknownKey = "tmJqEE79FtfdWse1gqnUey1vNBkB3zNV99Pi95SRAs8NMatczG"
            assertFalse(Ed25519PublicKey.isValid(unknownKey))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val publicKey = Ed25519PublicKey("edpktmJqEE79FtfdWse1gqnUey1vNBkB3zNV99Pi95SRAs8NMatczG")
            assertContentEquals(hexToBytes("101604858fea69724d3f0fef83e557324b60674ce5b52c6fc9f6d2fdd8bafe04"), publicKey.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val publicKey = hexToBytes("101604858fea69724d3f0fef83e557324b60674ce5b52c6fc9f6d2fdd8bafe04")
            assertEquals(Ed25519PublicKey("edpktmJqEE79FtfdWse1gqnUey1vNBkB3zNV99Pi95SRAs8NMatczG"), Ed25519PublicKey.decodeFromBytes(publicKey))
        }
    }
}
            