package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.Ed25519SecretKey
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class Ed25519SecretKeySamples {

    class Usage {

        @Test
        fun isValid() {
            val secretKey = "edskRyfkFKDuat4nfnSeLWZhrHM8AdgEbvpT4fnSDn9oMRcA9Te2MWc3rh5PaiSUMwATMAAQcg5zQ37jVHhzb1dgnmZwFSqwR2"
            assertTrue(Ed25519SecretKey.isValid(secretKey))

            val unknownKey = "RyfkFKDuat4nfnSeLWZhrHM8AdgEbvpT4fnSDn9oMRcA9Te2MWc3rh5PaiSUMwATMAAQcg5zQ37jVHhzb1dgnmZwFSqwR2"
            assertFalse(Ed25519SecretKey.isValid(unknownKey))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val secretKey = Ed25519SecretKey("edskRyfkFKDuat4nfnSeLWZhrHM8AdgEbvpT4fnSDn9oMRcA9Te2MWc3rh5PaiSUMwATMAAQcg5zQ37jVHhzb1dgnmZwFSqwR2")
            assertContentEquals(hexToBytes("a584f628fd018a37ac2eb1ef201712f3e2647b49b36edd0efc2b0956b0f0f40d9eeacc860c83e7f19192a83340b09839c4979039a80a4ac06e625d67913c667d"), secretKey.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val secretKey = hexToBytes("a584f628fd018a37ac2eb1ef201712f3e2647b49b36edd0efc2b0956b0f0f40d9eeacc860c83e7f19192a83340b09839c4979039a80a4ac06e625d67913c667d")
            assertEquals(Ed25519SecretKey("edskRyfkFKDuat4nfnSeLWZhrHM8AdgEbvpT4fnSDn9oMRcA9Te2MWc3rh5PaiSUMwATMAAQcg5zQ37jVHhzb1dgnmZwFSqwR2"), Ed25519SecretKey.decodeFromBytes(secretKey))
        }
    }
}
            