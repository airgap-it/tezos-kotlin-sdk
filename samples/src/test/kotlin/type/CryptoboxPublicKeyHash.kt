package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class CryptoboxPublicKeyHashSamples {

    class Usage {

        @Test
        fun isValid() {
            val publicKeyHash = "idtZdcjc9xN8mRHBrvJBkoukggGFxN"
            assertTrue(CryptoboxPublicKeyHash.isValid(publicKeyHash))

            val unknownHash = "tZdcjc9xN8mRHBrvJBkoukggGFxN"
            assertFalse(CryptoboxPublicKeyHash.isValid(unknownHash))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val publicKeyHash = CryptoboxPublicKeyHash("idtZdcjc9xN8mRHBrvJBkoukggGFxN")
            assertContentEquals(hexToBytes("e133afbeca8bcb12526f047309dbe229"), publicKeyHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val publicKeyHash = hexToBytes("e133afbeca8bcb12526f047309dbe229")
            assertEquals(CryptoboxPublicKeyHash("idtZdcjc9xN8mRHBrvJBkoukggGFxN"), CryptoboxPublicKeyHash.decodeFromBytes(publicKeyHash))
        }
    }
}
            