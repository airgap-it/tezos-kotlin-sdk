package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.Ed25519Seed
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class Ed25519SeedSamples {

    class Usage {

        @Test
        fun isValid() {
            val seed = "edsk3y6KhzrNyjtxdvJoraS1RtkFvht23rnesyhrYp81sGwWr5DuDb"
            assertTrue(Ed25519Seed.isValid(seed))

            val unknownSeed = "3y6KhzrNyjtxdvJoraS1RtkFvht23rnesyhrYp81sGwWr5DuDb"
            assertFalse(Ed25519Seed.isValid(unknownSeed))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val seed = Ed25519Seed("edsk3y6KhzrNyjtxdvJoraS1RtkFvht23rnesyhrYp81sGwWr5DuDb")
            assertContentEquals(hexToBytes("aabaa16f452feafe855aa943d41da0c9923048bb5c780dac3beca91315681c5c"), seed.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val seed = hexToBytes("aabaa16f452feafe855aa943d41da0c9923048bb5c780dac3beca91315681c5c")
            assertEquals(Ed25519Seed("edsk3y6KhzrNyjtxdvJoraS1RtkFvht23rnesyhrYp81sGwWr5DuDb"), Ed25519Seed.decodeFromBytes(seed))
        }
    }
}
            