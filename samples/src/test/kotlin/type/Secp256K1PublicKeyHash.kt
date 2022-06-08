package type

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKeyHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class Secp256K1PublicKeyHashSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val publicKeyHash = "tz2L5pd18PfqZ3c27AqfWvACFWK3zBSzPouc"
            assertTrue(Secp256K1PublicKeyHash.isValid(publicKeyHash))

            val unknownHash = "L5pd18PfqZ3c27AqfWvACFWK3zBSzPouc"
            assertFalse(Secp256K1PublicKeyHash.isValid(unknownHash))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val publicKeyHash = Secp256K1PublicKeyHash("tz2L5pd18PfqZ3c27AqfWvACFWK3zBSzPouc")
            assertContentEquals(hexToBytes("811808bca4b7abb9438b41fccf869df74affe7e8"), publicKeyHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val publicKeyHash = hexToBytes("811808bca4b7abb9438b41fccf869df74affe7e8")
            assertEquals(Secp256K1PublicKeyHash("tz2L5pd18PfqZ3c27AqfWvACFWK3zBSzPouc"), Secp256K1PublicKeyHash.decodeFromBytes(publicKeyHash))
        }
    }
}
            