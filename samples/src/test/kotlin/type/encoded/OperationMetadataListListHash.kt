package type.encoded

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.OperationMetadataListListHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class OperationMetadataListListHashSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val operationMetadataListListHash = "LLr2ByxtFXWG7gDjo11oihCDgCnrdjww4N1KT3HFaAzDwEwxMEkJN"
            assertTrue(OperationMetadataListListHash.isValid(operationMetadataListListHash))

            val unknownHash = "2ByxtFXWG7gDjo11oihCDgCnrdjww4N1KT3HFaAzDwEwxMEkJN"
            assertFalse(OperationMetadataListListHash.isValid(unknownHash))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val operationMetadataListListHash = OperationMetadataListListHash("LLr2ByxtFXWG7gDjo11oihCDgCnrdjww4N1KT3HFaAzDwEwxMEkJN")
            assertContentEquals(hexToBytes("7f00496b41f1d56715a06c6810362f6f285db1200a055b25a7dd353bc9c9ad94"), operationMetadataListListHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val operationMetadataListListHash = hexToBytes("7f00496b41f1d56715a06c6810362f6f285db1200a055b25a7dd353bc9c9ad94")
            assertEquals(OperationMetadataListListHash("LLr2ByxtFXWG7gDjo11oihCDgCnrdjww4N1KT3HFaAzDwEwxMEkJN"), OperationMetadataListListHash.decodeFromBytes(operationMetadataListListHash))
        }
    }
}
            