package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.NonceHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class NonceHashSamples {

    class Usage {

        @Test
        fun isValid() {
            val nonceHash = "nceVfSgRRamurzg5PZNunEkbmwQWtFWQj93bXkHu4sKb9HBu4WSah"
            assertTrue(NonceHash.isValid(nonceHash))

            val unknownHash = "VfSgRRamurzg5PZNunEkbmwQWtFWQj93bXkHu4sKb9HBu4WSah"
            assertFalse(NonceHash.isValid(unknownHash))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val nonceHash = NonceHash("nceVfSgRRamurzg5PZNunEkbmwQWtFWQj93bXkHu4sKb9HBu4WSah")
            assertContentEquals(hexToBytes("bd955c322ffde62d9eecab96beb6f15587d2a456cd2b1975cf164868c08b0d39"), nonceHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val nonceHash = hexToBytes("bd955c322ffde62d9eecab96beb6f15587d2a456cd2b1975cf164868c08b0d39")
            assertEquals(NonceHash("nceVfSgRRamurzg5PZNunEkbmwQWtFWQj93bXkHu4sKb9HBu4WSah"), NonceHash.decodeFromBytes(nonceHash))
        }
    }
}
            