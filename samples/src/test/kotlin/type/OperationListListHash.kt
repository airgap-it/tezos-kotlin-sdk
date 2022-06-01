package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.OperationListListHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class OperationListListHashSamples {

    class Usage {

        @Test
        fun isValid() {
            val operationListListHash = "LLoZUSo4D2yakzTPkvodNPgsPphQ5hQm7TnuDh5P3zUPQHnembKyF"
            assertTrue(OperationListListHash.isValid(operationListListHash))

            val unknownHash = "ZUSo4D2yakzTPkvodNPgsPphQ5hQm7TnuDh5P3zUPQHnembKyF"
            assertFalse(OperationListListHash.isValid(unknownHash))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val operationListListHash = OperationListListHash("LLoZUSo4D2yakzTPkvodNPgsPphQ5hQm7TnuDh5P3zUPQHnembKyF")
            assertContentEquals(hexToBytes("13d6feeca20235fa0c80ab22471ede71f09019a26994e2e523982a36403b5210"), operationListListHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val operationListListHash = hexToBytes("13d6feeca20235fa0c80ab22471ede71f09019a26994e2e523982a36403b5210")
            assertEquals(OperationListListHash("LLoZUSo4D2yakzTPkvodNPgsPphQ5hQm7TnuDh5P3zUPQHnembKyF"), OperationListListHash.decodeFromBytes(operationListListHash))
        }
    }
}
            