package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class ChainIdSamples {

    class Usage {

        @Test
        fun isValid() {
            val chainId = "NetXPduhFKtb9SG"
            assertTrue(ChainId.isValid(chainId))

            val unknownId = "XPduhFKtb9SG"
            assertFalse(ChainId.isValid(unknownId))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val chainId = ChainId("NetXPduhFKtb9SG")
            assertContentEquals(hexToBytes("27ac9165"), chainId.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val chainId = hexToBytes("27ac9165")
            assertEquals(ChainId("NetXPduhFKtb9SG"), ChainId.decodeFromBytes(chainId))
        }
    }
}
            