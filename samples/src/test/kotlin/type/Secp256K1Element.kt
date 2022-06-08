package type

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.Secp256K1Element
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class Secp256K1ElementSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val element = "GSp9TfVdy4CUCamsd5MH5ekRgHG3mi8PotTduB3ahV5mzJYTvanxao"
            assertTrue(Secp256K1Element.isValid(element))

            val unknownElement = "9TfVdy4CUCamsd5MH5ekRgHG3mi8PotTduB3ahV5mzJYTvanxao"
            assertFalse(Secp256K1Element.isValid(unknownElement))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val element = Secp256K1Element("GSp9TfVdy4CUCamsd5MH5ekRgHG3mi8PotTduB3ahV5mzJYTvanxao")
            assertContentEquals(hexToBytes("221465ac6c4227d0b757a7bba0c07c1b4410c061d2cf223b0353cebedd88093b82"), element.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val element = hexToBytes("221465ac6c4227d0b757a7bba0c07c1b4410c061d2cf223b0353cebedd88093b82")
            assertEquals(Secp256K1Element("GSp9TfVdy4CUCamsd5MH5ekRgHG3mi8PotTduB3ahV5mzJYTvanxao"), Secp256K1Element.decodeFromBytes(element))
        }
    }
}
            