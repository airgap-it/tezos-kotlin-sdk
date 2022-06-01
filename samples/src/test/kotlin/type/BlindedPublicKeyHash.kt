package type

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.BlindedPublicKeyHash
import it.airgap.tezos.core.type.encoded.BlindedPublicKeyHash
import it.airgap.tezos.core.type.encoded.Ed25519BlindedPublicKeyHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class BlindedPublicKeyHashSamples {

    class Usage {

        @Test
        fun isValid() {
            val tz1BlindedPublicKeyHash = "btz1YpGPF6AjQNdQJwUUjE5WPdwq5ivk9E5j3"
            assertTrue(BlindedPublicKeyHash.isValid(tz1BlindedPublicKeyHash))

            val unknownHash = "YpGPF6AjQNdQJwUUjE5WPdwq5ivk9E5j3"
            assertFalse(BlindedPublicKeyHash.isValid(unknownHash))
        }

        @Test
        fun create() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }
            val tz1BlindedPublicKeyHash = BlindedPublicKeyHash("btz1YpGPF6AjQNdQJwUUjE5WPdwq5ivk9E5j3")
            assertEquals(Ed25519BlindedPublicKeyHash("btz1YpGPF6AjQNdQJwUUjE5WPdwq5ivk9E5j3"), tz1BlindedPublicKeyHash)
        }
    }
}
            