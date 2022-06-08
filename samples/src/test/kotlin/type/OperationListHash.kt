package type

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.OperationListHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class OperationListHashSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val operationListHash = "LowncBNMso4gKeiK3ahMFccR1xBATLRYs9qLaLCYiuEYw7enAx8e"
            assertTrue(OperationListHash.isValid(operationListHash))

            val unknownHash = "wncBNMso4gKeiK3ahMFccR1xBATLRYs9qLaLCYiuEYw7enAx8e"
            assertFalse(OperationListHash.isValid(unknownHash))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val operationListHash = OperationListHash("LowncBNMso4gKeiK3ahMFccR1xBATLRYs9qLaLCYiuEYw7enAx8e")
            assertContentEquals(hexToBytes("83f473d438840fc37eae06117cb953a14c9fa36c4cc71afc89b89bbc68ace41b"), operationListHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val operationListHash = hexToBytes("83f473d438840fc37eae06117cb953a14c9fa36c4cc71afc89b89bbc68ace41b")
            assertEquals(OperationListHash("LowncBNMso4gKeiK3ahMFccR1xBATLRYs9qLaLCYiuEYw7enAx8e"), OperationListHash.decodeFromBytes(operationListHash))
        }
    }
}
            