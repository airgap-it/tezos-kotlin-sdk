package type.encoded

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.Secp256K1EncryptedScalar
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class Secp256K1EncryptedScalarSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val encryptedScalar = "seesk95gJ4Z1VsaiggVaZH1sGb36M9o9HRthHbD8QoZmLTB9sk8gjij2d59GEvZEsLhsgdHPDXEyAh57Mk4Hi3U96pvnG"
            assertTrue(Secp256K1EncryptedScalar.isValid(encryptedScalar))

            val unknownScalar = "95gJ4Z1VsaiggVaZH1sGb36M9o9HRthHbD8QoZmLTB9sk8gjij2d59GEvZEsLhsgdHPDXEyAh57Mk4Hi3U96pvnG"
            assertFalse(Secp256K1EncryptedScalar.isValid(unknownScalar))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val encryptedScalar = Secp256K1EncryptedScalar("seesk95gJ4Z1VsaiggVaZH1sGb36M9o9HRthHbD8QoZmLTB9sk8gjij2d59GEvZEsLhsgdHPDXEyAh57Mk4Hi3U96pvnG")
            assertContentEquals(hexToBytes("d7b71d2f79373c34f216e40f57656b9eafdb4a258de6c8d06e322c575013a00d37c0c04af579bc9ab2caea231f68f0b63363fe138e18e451bd5d392c"), encryptedScalar.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val encryptedScalar = hexToBytes("d7b71d2f79373c34f216e40f57656b9eafdb4a258de6c8d06e322c575013a00d37c0c04af579bc9ab2caea231f68f0b63363fe138e18e451bd5d392c")
            assertEquals(Secp256K1EncryptedScalar("seesk95gJ4Z1VsaiggVaZH1sGb36M9o9HRthHbD8QoZmLTB9sk8gjij2d59GEvZEsLhsgdHPDXEyAh57Mk4Hi3U96pvnG"), Secp256K1EncryptedScalar.decodeFromBytes(encryptedScalar))
        }
    }
}
            