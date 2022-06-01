package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.OperationHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class OperationHashSamples {

    class Usage {

        @Test
        fun isValid() {
            val operationHash = "ooHhFMvgMEWYSpRJuZRCVtQkMCCGZSrgiLie3GYYn9fgV9pduvs"
            assertTrue(OperationHash.isValid(operationHash))

            val unknownHash = "oHhFMvgMEWYSpRJuZRCVtQkMCCGZSrgiLie3GYYn9fgV9pduvs"
            assertFalse(OperationHash.isValid(unknownHash))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val operationHash = OperationHash("ooHhFMvgMEWYSpRJuZRCVtQkMCCGZSrgiLie3GYYn9fgV9pduvs")
            assertContentEquals(hexToBytes("551bb8966fb693ad03a8262e199d2faaead0883a17aad31d6b98f96c65e52ab2"), operationHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val operationHash = hexToBytes("551bb8966fb693ad03a8262e199d2faaead0883a17aad31d6b98f96c65e52ab2")
            assertEquals(OperationHash("ooHhFMvgMEWYSpRJuZRCVtQkMCCGZSrgiLie3GYYn9fgV9pduvs"), OperationHash.decodeFromBytes(operationHash))
        }
    }
}
            