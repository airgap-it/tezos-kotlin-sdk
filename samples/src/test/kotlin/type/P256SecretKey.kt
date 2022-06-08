package type

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.P256SecretKey
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class P256SecretKeySamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val secretKey = "p2sk2tEQ1Wi6GEFNKq3q6MsZ8YhUWo1WAx81NUnwQm5xLAA86TcVxu"
            assertTrue(P256SecretKey.isValid(secretKey))

            val unknownKey = "2tEQ1Wi6GEFNKq3q6MsZ8YhUWo1WAx81NUnwQm5xLAA86TcVxu"
            assertFalse(P256SecretKey.isValid(unknownKey))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val secretKey = P256SecretKey("p2sk2tEQ1Wi6GEFNKq3q6MsZ8YhUWo1WAx81NUnwQm5xLAA86TcVxu")
            assertContentEquals(hexToBytes("4764232efdf0fbb3717ab562fa91acaa7d8c99b2dc36118fc15afca98317866a"), secretKey.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val secretKey = hexToBytes("4764232efdf0fbb3717ab562fa91acaa7d8c99b2dc36118fc15afca98317866a")
            assertEquals(P256SecretKey("p2sk2tEQ1Wi6GEFNKq3q6MsZ8YhUWo1WAx81NUnwQm5xLAA86TcVxu"), P256SecretKey.decodeFromBytes(secretKey))
        }
    }
}
            