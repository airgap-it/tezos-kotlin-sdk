package type.encoded

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.OperationMetadataHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class OperationMetadataHashSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val operationMetadataHash = "r45tEedxqzRQ7fxec9iiqFDJFRyYgSanM45xWq1P6TqmYyM6em8"
            assertTrue(OperationMetadataHash.isValid(operationMetadataHash))

            val unknownHash = "45tEedxqzRQ7fxec9iiqFDJFRyYgSanM45xWq1P6TqmYyM6em8"
            assertFalse(OperationMetadataHash.isValid(unknownHash))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val operationMetadataHash = OperationMetadataHash("r45tEedxqzRQ7fxec9iiqFDJFRyYgSanM45xWq1P6TqmYyM6em8")
            assertContentEquals(hexToBytes("9e7abdcb9a0e3f360375d629901fe584b2bb8fb818ffaf2ab43dc485ee2578f0"), operationMetadataHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val operationMetadataHash = hexToBytes("9e7abdcb9a0e3f360375d629901fe584b2bb8fb818ffaf2ab43dc485ee2578f0")
            assertEquals(OperationMetadataHash("r45tEedxqzRQ7fxec9iiqFDJFRyYgSanM45xWq1P6TqmYyM6em8"), OperationMetadataHash.decodeFromBytes(operationMetadataHash))
        }
    }
}
            