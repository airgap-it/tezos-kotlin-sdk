package type.encoded

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.Ed25519PublicKeyHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class Ed25519PublicKeyHashSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val publicKeyHash = "tz1hWxmVjMqaEWTU5Hme6NWcinnf8VKifRPT"
            assertTrue(Ed25519PublicKeyHash.isValid(publicKeyHash))

            val unknownHash = "hWxmVjMqaEWTU5Hme6NWcinnf8VKifRPT"
            assertFalse(Ed25519PublicKeyHash.isValid(unknownHash))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val publicKeyHash = Ed25519PublicKeyHash("tz1hWxmVjMqaEWTU5Hme6NWcinnf8VKifRPT")
            assertContentEquals(hexToBytes("effc94586537a722308b0e9038825cfd5d0d8065"), publicKeyHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val publicKeyHash = hexToBytes("effc94586537a722308b0e9038825cfd5d0d8065")
            assertEquals(Ed25519PublicKeyHash("tz1hWxmVjMqaEWTU5Hme6NWcinnf8VKifRPT"), Ed25519PublicKeyHash.decodeFromBytes(publicKeyHash))
        }
    }
}
            