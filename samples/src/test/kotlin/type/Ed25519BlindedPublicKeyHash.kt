package type

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.Ed25519BlindedPublicKeyHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class Ed25519BlindedPublicKeyHashSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val blindedPublicKeyHash = "btz1YpGPF6AjQNdQJwUUjE5WPdwq5ivk9E5j3"
            assertTrue(Ed25519BlindedPublicKeyHash.isValid(blindedPublicKeyHash))

            val unknownHash = "YpGPF6AjQNdQJwUUjE5WPdwq5ivk9E5j3"
            assertFalse(Ed25519BlindedPublicKeyHash.isValid(unknownHash))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val blindedPublicKeyHash = Ed25519BlindedPublicKeyHash("btz1YpGPF6AjQNdQJwUUjE5WPdwq5ivk9E5j3")
            assertContentEquals(hexToBytes("890c2d910da329047a4d8e1907a8a605c4237f5d"), blindedPublicKeyHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val blindedPublicKeyHash = hexToBytes("890c2d910da329047a4d8e1907a8a605c4237f5d")
            assertEquals(Ed25519BlindedPublicKeyHash("btz1YpGPF6AjQNdQJwUUjE5WPdwq5ivk9E5j3"), Ed25519BlindedPublicKeyHash.decodeFromBytes(blindedPublicKeyHash))
        }
    }
}
            