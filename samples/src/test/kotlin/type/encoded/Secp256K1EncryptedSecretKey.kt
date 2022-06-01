package type.encoded

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.Secp256K1EncryptedSecretKey
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class Secp256K1EncryptedSecretKeySamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val encryptedSecretKey = "spesk1epWduEX66sZmujK9ppt6New4a92uLryP1Zcc6EGHphqCEPGknc4HkEiQuW1Up5iiMPnCdGJcV1poGq3cTy"
            assertTrue(Secp256K1EncryptedSecretKey.isValid(encryptedSecretKey))

            val unknownSkey = "1epWduEX66sZmujK9ppt6New4a92uLryP1Zcc6EGHphqCEPGknc4HkEiQuW1Up5iiMPnCdGJcV1poGq3cTy"
            assertFalse(Secp256K1EncryptedSecretKey.isValid(unknownSkey))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val encryptedSecretKey = Secp256K1EncryptedSecretKey("spesk1epWduEX66sZmujK9ppt6New4a92uLryP1Zcc6EGHphqCEPGknc4HkEiQuW1Up5iiMPnCdGJcV1poGq3cTy")
            assertContentEquals(hexToBytes("495a60a93f2c17f18c332a83e7254db3ef19ec28118388eee2a2683c2c6cc4e85907c2807824218c60c1d88a1b21889228e11ae6812674cc"), encryptedSecretKey.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val encryptedSecretKey = hexToBytes("495a60a93f2c17f18c332a83e7254db3ef19ec28118388eee2a2683c2c6cc4e85907c2807824218c60c1d88a1b21889228e11ae6812674cc")
            assertEquals(Secp256K1EncryptedSecretKey("spesk1epWduEX66sZmujK9ppt6New4a92uLryP1Zcc6EGHphqCEPGknc4HkEiQuW1Up5iiMPnCdGJcV1poGq3cTy"), Secp256K1EncryptedSecretKey.decodeFromBytes(encryptedSecretKey))
        }
    }
}
            