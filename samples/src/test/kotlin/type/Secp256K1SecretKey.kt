package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.Secp256K1SecretKey
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class Secp256K1SecretKeySamples {

    class Usage {

        @Test
        fun isValid() {
            val secretKey = "spsk2v5UcA87vcQhFjKNR1dAeyiQuLhXVhV6eQVyeKLNvSS3H5KJhc"
            assertTrue(Secp256K1SecretKey.isValid(secretKey))

            val unknownKey = "2v5UcA87vcQhFjKNR1dAeyiQuLhXVhV6eQVyeKLNvSS3H5KJhc"
            assertFalse(Secp256K1SecretKey.isValid(unknownKey))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val secretKey = Secp256K1SecretKey("spsk2v5UcA87vcQhFjKNR1dAeyiQuLhXVhV6eQVyeKLNvSS3H5KJhc")
            assertContentEquals(hexToBytes("c470a1dd0a3d9bc35f85338e3f93f12ed9ffc9c9576f66d44096fe3f5a967b08"), secretKey.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val secretKey = hexToBytes("c470a1dd0a3d9bc35f85338e3f93f12ed9ffc9c9576f66d44096fe3f5a967b08")
            assertEquals(Secp256K1SecretKey("spsk2v5UcA87vcQhFjKNR1dAeyiQuLhXVhV6eQVyeKLNvSS3H5KJhc"), Secp256K1SecretKey.decodeFromBytes(secretKey))
        }
    }
}
            