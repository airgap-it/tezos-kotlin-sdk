package type

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.P256EncryptedSecretKey
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class P256EncryptedSecretKeySamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val encryptedSecretKey = "p2esk2CjoU93r446nJPKo1SaWii9FffCgeDW3MLLTuyTLGFgTYDit4z73nzhHw3EFgbyz2TxD7Fj3mkyKAEhji1u"
            assertTrue(P256EncryptedSecretKey.isValid(encryptedSecretKey))

            val unknownKey = "2CjoU93r446nJPKo1SaWii9FffCgeDW3MLLTuyTLGFgTYDit4z73nzhHw3EFgbyz2TxD7Fj3mkyKAEhji1u"
            assertFalse(P256EncryptedSecretKey.isValid(unknownKey))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val encryptedSecretKey = P256EncryptedSecretKey("p2esk2CjoU93r446nJPKo1SaWii9FffCgeDW3MLLTuyTLGFgTYDit4z73nzhHw3EFgbyz2TxD7Fj3mkyKAEhji1u")
            assertContentEquals(hexToBytes("93431ca4a6f7ca21aba718887a1c4b4e91fe4261c8c435dfc158e343fa22d112f719176c5e3484a7d46e67205dc901d4f387086a905cbe7f"), encryptedSecretKey.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val encryptedSecretKey = hexToBytes("93431ca4a6f7ca21aba718887a1c4b4e91fe4261c8c435dfc158e343fa22d112f719176c5e3484a7d46e67205dc901d4f387086a905cbe7f")
            assertEquals(P256EncryptedSecretKey("p2esk2CjoU93r446nJPKo1SaWii9FffCgeDW3MLLTuyTLGFgTYDit4z73nzhHw3EFgbyz2TxD7Fj3mkyKAEhji1u"), P256EncryptedSecretKey.decodeFromBytes(encryptedSecretKey))
        }
    }
}
            