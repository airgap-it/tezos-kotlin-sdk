package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.Secp256K1Scalar
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class Secp256K1ScalarSamples {

    class Usage {

        @Test
        fun isValid() {
            val scalar = "SSp3FyCpPyYfNTKun9pkeqy5Rnktn38avhdh5J5ba5KAsoVF31gVF"
            assertTrue(Secp256K1Scalar.isValid(scalar))

            val unknownScalar = "3FyCpPyYfNTKun9pkeqy5Rnktn38avhdh5J5ba5KAsoVF31gVF"
            assertFalse(Secp256K1Scalar.isValid(unknownScalar))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val scalar = Secp256K1Scalar("SSp3FyCpPyYfNTKun9pkeqy5Rnktn38avhdh5J5ba5KAsoVF31gVF")
            assertContentEquals(hexToBytes("cc496fd3cd59a1d71a650fa29af11a177ef5d56adbac86206e06dcef133355c6"), scalar.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val scalar = hexToBytes("cc496fd3cd59a1d71a650fa29af11a177ef5d56adbac86206e06dcef133355c6")
            assertEquals(Secp256K1Scalar("SSp3FyCpPyYfNTKun9pkeqy5Rnktn38avhdh5J5ba5KAsoVF31gVF"), Secp256K1Scalar.decodeFromBytes(scalar))
        }
    }
}
            