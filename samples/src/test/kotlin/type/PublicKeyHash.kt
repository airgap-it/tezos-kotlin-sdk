package type

import _utils.SamplesBase
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.PublicKeyHash
import it.airgap.tezos.core.type.encoded.Ed25519PublicKeyHash
import it.airgap.tezos.core.type.encoded.P256PublicKeyHash
import it.airgap.tezos.core.type.encoded.PublicKeyHash
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKeyHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class PublicKeyHashSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val tz1PublicKeyHash = "tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e"
            assertTrue(PublicKeyHash.isValid(tz1PublicKeyHash))

            val tz2PublicKeyHash = "tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"
            assertTrue(PublicKeyHash.isValid(tz2PublicKeyHash))

            val tz3PublicKeyHash = "tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B"
            assertTrue(PublicKeyHash.isValid(tz3PublicKeyHash))

            val unknownHash = "tzhByhpsNVUsEHndaAaqk6ikEwpeCKQdUZe"
            assertFalse(PublicKeyHash.isValid(unknownHash))
        }

        @Test
        fun create() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val tz1PublicKeyHash = PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e")
            assertEquals(Ed25519PublicKeyHash("tz1fJGtrdmckD3VkiDxqUEci5h4gGcvocw6e"), tz1PublicKeyHash)

            val tz2PublicKeyHash = PublicKeyHash("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy")
            assertEquals(Secp256K1PublicKeyHash("tz2AjVPbMHdDF1XwHVhUrTg6ZvqY83AYhJEy"), tz2PublicKeyHash)

            val tz3PublicKeyHash = PublicKeyHash("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B")
            assertEquals(P256PublicKeyHash("tz3Nk25g51knuzFZZz2DeA5PveaQYmCtV68B"), tz3PublicKeyHash)
        }
    }
}
            