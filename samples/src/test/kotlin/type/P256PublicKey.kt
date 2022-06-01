package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.P256PublicKey
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class P256PublicKeySamples {

    class Usage {

        @Test
        fun isValid() {
            val publicKey = "p2pkAszRmP1BETKhmTkSVNMELHnjg2o9XZamjUQgcBaedQNRdrYdBis"
            assertTrue(P256PublicKey.isValid(publicKey))

            val unknownKey = "AszRmP1BETKhmTkSVNMELHnjg2o9XZamjUQgcBaedQNRdrYdBis"
            assertFalse(P256PublicKey.isValid(unknownKey))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val publicKey = P256PublicKey("p2pkAszRmP1BETKhmTkSVNMELHnjg2o9XZamjUQgcBaedQNRdrYdBis")
            assertContentEquals(hexToBytes("91be4ebfb33637848b7f0f89020afed820c19f4753803761d3ed8d6e03976eeb2b"), publicKey.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val publicKey = hexToBytes("91be4ebfb33637848b7f0f89020afed820c19f4753803761d3ed8d6e03976eeb2b")
            assertEquals(P256PublicKey("p2pkAszRmP1BETKhmTkSVNMELHnjg2o9XZamjUQgcBaedQNRdrYdBis"), P256PublicKey.decodeFromBytes(publicKey))
        }
    }
}
            