package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.BlockMetadataHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class BlockMetadataHashSamples {

    class Usage {

        @Test
        fun isValid() {
            val blockMetadataHash = "bm3LV5fYgzx7P7V9E2nuJeGKzeonnu1FbRB2Bk5SMwvWmzUcQZpc"
            assertTrue(BlockMetadataHash.isValid(blockMetadataHash))

            val unknownHash = "3LV5fYgzx7P7V9E2nuJeGKzeonnu1FbRB2Bk5SMwvWmzUcQZpc"
            assertFalse(BlockMetadataHash.isValid(unknownHash))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            val tezos = Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val blockMetadataHash = BlockMetadataHash("bm3LV5fYgzx7P7V9E2nuJeGKzeonnu1FbRB2Bk5SMwvWmzUcQZpc")
            assertContentEquals(hexToBytes("64aa9d9b3acf43f64a1c975b6298cf47f5aa557c86bd8a07645f49f486f64398"), blockMetadataHash.encodeToBytes())
            assertContentEquals(hexToBytes("64aa9d9b3acf43f64a1c975b6298cf47f5aa557c86bd8a07645f49f486f64398"), blockMetadataHash.encodeToBytes(tezos))
        }

        @Test
        fun fromBytes() {
            val tezos = Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val blockMetadataHash = hexToBytes("64aa9d9b3acf43f64a1c975b6298cf47f5aa557c86bd8a07645f49f486f64398")
            assertEquals(BlockMetadataHash("bm3LV5fYgzx7P7V9E2nuJeGKzeonnu1FbRB2Bk5SMwvWmzUcQZpc"), BlockMetadataHash.decodeFromBytes(blockMetadataHash))
            assertEquals(BlockMetadataHash("bm3LV5fYgzx7P7V9E2nuJeGKzeonnu1FbRB2Bk5SMwvWmzUcQZpc"), BlockMetadataHash.decodeFromBytes(blockMetadataHash, tezos))
        }
    }
}
