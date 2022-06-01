package type.encoded

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.Ed25519EncryptedSeed
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class Ed25519EncryptedSeedSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val encryptedSeed = "edesk1SDKxFk9jPwEbVffib3BeTxyBLEEFQL9LiUBGRzUigCbwR9Voa3fXBf7kisW5UWFBTWWaQTaHGBBqDkmxyD"
            assertTrue(Ed25519EncryptedSeed.isValid(encryptedSeed))

            val unknownSeed = "1SDKxFk9jPwEbVffib3BeTxyBLEEFQL9LiUBGRzUigCbwR9Voa3fXBf7kisW5UWFBTWWaQTaHGBBqDkmxyD"
            assertFalse(Ed25519EncryptedSeed.isValid(unknownSeed))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val encryptedSeed = Ed25519EncryptedSeed("edesk1SDKxFk9jPwEbVffib3BeTxyBLEEFQL9LiUBGRzUigCbwR9Voa3fXBf7kisW5UWFBTWWaQTaHGBBqDkmxyD")
            assertContentEquals(hexToBytes("39a022a9b12acbe6855576da045a34b80d0c5d9b2e5f8fb744d8967f5f79f1da78330d7bdbcd1a731cac74e1a919c9323956d8d97057d36b"), encryptedSeed.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val encryptedSeed = hexToBytes("39a022a9b12acbe6855576da045a34b80d0c5d9b2e5f8fb744d8967f5f79f1da78330d7bdbcd1a731cac74e1a919c9323956d8d97057d36b")
            assertEquals(Ed25519EncryptedSeed("edesk1SDKxFk9jPwEbVffib3BeTxyBLEEFQL9LiUBGRzUigCbwR9Voa3fXBf7kisW5UWFBTWWaQTaHGBBqDkmxyD"), Ed25519EncryptedSeed.decodeFromBytes(encryptedSeed))
        }
    }
}
            