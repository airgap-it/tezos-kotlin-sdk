package type

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.SaplingSpendingKey
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class SaplingSpendingKeySamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val spendingKey = "sask3AKmEBvhRUyn3ssLU6pDqAkHtYcXNms5myxMsgBcGnDeGhanGEoieyoyp5qhCh5LcRSmVTkJTyiVWEfRgXhESzXWNyxWvygZ7CxXGMmrYEZxeGoy1S563DvmNPhKNJdUFbteWp2Kw3DciSqVS3wkYRG3ec1WEyBQMrvZoPNVv1Jc68dsDA2EhdVthL39yXksqGo18ZEMg27dw95osYiRPS7bcc6zgPYEdxVyj4WLV12P2"
            assertTrue(SaplingSpendingKey.isValid(spendingKey))

            val unknownKey = "3AKmEBvhRUyn3ssLU6pDqAkHtYcXNms5myxMsgBcGnDeGhanGEoieyoyp5qhCh5LcRSmVTkJTyiVWEfRgXhESzXWNyxWvygZ7CxXGMmrYEZxeGoy1S563DvmNPhKNJdUFbteWp2Kw3DciSqVS3wkYRG3ec1WEyBQMrvZoPNVv1Jc68dsDA2EhdVthL39yXksqGo18ZEMg27dw95osYiRPS7bcc6zgPYEdxVyj4WLV12P2"
            assertFalse(SaplingSpendingKey.isValid(unknownKey))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val spendingKey = SaplingSpendingKey("sask3AKmEBvhRUyn3ssLU6pDqAkHtYcXNms5myxMsgBcGnDeGhanGEoieyoyp5qhCh5LcRSmVTkJTyiVWEfRgXhESzXWNyxWvygZ7CxXGMmrYEZxeGoy1S563DvmNPhKNJdUFbteWp2Kw3DciSqVS3wkYRG3ec1WEyBQMrvZoPNVv1Jc68dsDA2EhdVthL39yXksqGo18ZEMg27dw95osYiRPS7bcc6zgPYEdxVyj4WLV12P2")
            assertContentEquals(hexToBytes("5def48671093f52c415bd96c7706bcb96080f5e6b393ab00cc3bc4b20f2e85d072118a6062c3f7894199e15578aeee1473e9c05ed2a3706d4d30e94eae65778c8c0dfe0c7a69af5cf2d0e7dc595144700f120581f52fdd49afdd5df5472811d9df34fd8a0968aa7c398026a7fba12a111d0d2e8d0b06004b5e3297bdf84fa3b124cb0a9b21cca4d7b410bdb04f23650f242b0090f067ffc7c538881eacda4f8d79a8d1741bc14b402b"), spendingKey.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val spendingKey = hexToBytes("5def48671093f52c415bd96c7706bcb96080f5e6b393ab00cc3bc4b20f2e85d072118a6062c3f7894199e15578aeee1473e9c05ed2a3706d4d30e94eae65778c8c0dfe0c7a69af5cf2d0e7dc595144700f120581f52fdd49afdd5df5472811d9df34fd8a0968aa7c398026a7fba12a111d0d2e8d0b06004b5e3297bdf84fa3b124cb0a9b21cca4d7b410bdb04f23650f242b0090f067ffc7c538881eacda4f8d79a8d1741bc14b402b")
            assertEquals(SaplingSpendingKey("sask3AKmEBvhRUyn3ssLU6pDqAkHtYcXNms5myxMsgBcGnDeGhanGEoieyoyp5qhCh5LcRSmVTkJTyiVWEfRgXhESzXWNyxWvygZ7CxXGMmrYEZxeGoy1S563DvmNPhKNJdUFbteWp2Kw3DciSqVS3wkYRG3ec1WEyBQMrvZoPNVv1Jc68dsDA2EhdVthL39yXksqGo18ZEMg27dw95osYiRPS7bcc6zgPYEdxVyj4WLV12P2"), SaplingSpendingKey.decodeFromBytes(spendingKey))
        }
    }
}
            