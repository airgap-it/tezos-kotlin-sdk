package type

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.P256PublicKeyHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class P256PublicKeyHashSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val publicKeyHash = "tz3buLBuYxg4wLxEMxarJ2BbjWHfBF3DXup3"
            assertTrue(P256PublicKeyHash.isValid(publicKeyHash))

            val unknownHash = "buLBuYxg4wLxEMxarJ2BbjWHfBF3DXup3"
            assertFalse(P256PublicKeyHash.isValid(unknownHash))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val publicKeyHash = P256PublicKeyHash("tz3buLBuYxg4wLxEMxarJ2BbjWHfBF3DXup3")
            assertContentEquals(hexToBytes("aad572b64954fc49a55172aa73b82cd38439d936"), publicKeyHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val publicKeyHash = hexToBytes("aad572b64954fc49a55172aa73b82cd38439d936")
            assertEquals(P256PublicKeyHash("tz3buLBuYxg4wLxEMxarJ2BbjWHfBF3DXup3"), P256PublicKeyHash.decodeFromBytes(publicKeyHash))
        }
    }
}
            