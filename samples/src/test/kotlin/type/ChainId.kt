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
            val chainId = "NetXNy1E383jSrX"
            assertTrue(ChainId.isValid(chainId))

            val unknownId = "XNy1E383jSrX"
            assertFalse(ChainId.isValid(unknownId))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            val tezos = Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val chainId = ChainId("NetXNy1E383jSrX")
            assertContentEquals(hexToBytes("23a9f05a"), chainId.encodeToBytes())
            assertContentEquals(hexToBytes("23a9f05a"), chainId.encodeToBytes(tezos))
        }

        @Test
        fun fromBytes() {
            val tezos = Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val chainId = hexToBytes("23a9f05a")
            assertEquals(ChainId("NetXNy1E383jSrX"), ChainId.decodeFromBytes(chainId))
            assertEquals(ChainId("NetXNy1E383jSrX"), ChainId.decodeFromBytes(chainId, tezos))
        }
    }
}
