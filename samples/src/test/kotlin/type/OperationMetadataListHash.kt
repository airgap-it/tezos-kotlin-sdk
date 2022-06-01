package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.OperationMetadataListHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class OperationMetadataListHashSamples {

    class Usage {

        @Test
        fun isValid() {
            val operationMetadataListHash = "Lr2riSwn72dXUq1ofwsnw1UFvHBGoK2zXKxkX9kVBfakDVUtC5Qb"
            assertTrue(OperationMetadataListHash.isValid(operationMetadataListHash))

            val unknownHash = "2riSwn72dXUq1ofwsnw1UFvHBGoK2zXKxkX9kVBfakDVUtC5Qb"
            assertFalse(OperationMetadataListHash.isValid(unknownHash))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val operationMetadataListHash = OperationMetadataListHash("Lr2riSwn72dXUq1ofwsnw1UFvHBGoK2zXKxkX9kVBfakDVUtC5Qb")
            assertContentEquals(hexToBytes("cc82a45dc573f6904bc21c8ec163c02cc5ab93744a1bd740d97d56523abfe945"), operationMetadataListHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val operationMetadataListHash = hexToBytes("cc82a45dc573f6904bc21c8ec163c02cc5ab93744a1bd740d97d56523abfe945")
            assertEquals(OperationMetadataListHash("Lr2riSwn72dXUq1ofwsnw1UFvHBGoK2zXKxkX9kVBfakDVUtC5Qb"), OperationMetadataListHash.decodeFromBytes(operationMetadataListHash))
        }
    }
}
            