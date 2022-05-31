package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class BlockHashSamples {

    class Usage {

        @Test
        fun isValid() {
            val blockHash = "BLxhnhJ8yRu5BjcK8STwLtDmSpWz2q5R5HRYir8kdexFjMVZiZ5"
            assertTrue(BlockHash.isValid(blockHash))

            val unknownHash = "LxhnhJ8yRu5BjcK8STwLtDmSpWz2q5R5HRYir8kdexFjMVZiZ5"
            assertFalse(BlockHash.isValid(unknownHash))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            val tezos = Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val blockHash = BlockHash("BLxhnhJ8yRu5BjcK8STwLtDmSpWz2q5R5HRYir8kdexFjMVZiZ5")
            assertContentEquals(hexToBytes("a4712e4241cd45194876e5e3637afd5eb0de95d43909ee8a0300004bb54697f4"), blockHash.encodeToBytes())
            assertContentEquals(hexToBytes("a4712e4241cd45194876e5e3637afd5eb0de95d43909ee8a0300004bb54697f4"), blockHash.encodeToBytes(tezos))
        }

        @Test
        fun fromBytes() {
            val tezos = Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val blockHash = hexToBytes("a4712e4241cd45194876e5e3637afd5eb0de95d43909ee8a0300004bb54697f4")
            assertEquals(BlockHash("BLxhnhJ8yRu5BjcK8STwLtDmSpWz2q5R5HRYir8kdexFjMVZiZ5"), BlockHash.decodeFromBytes(blockHash))
            assertEquals(BlockHash("BLxhnhJ8yRu5BjcK8STwLtDmSpWz2q5R5HRYir8kdexFjMVZiZ5"), BlockHash.decodeFromBytes(blockHash, tezos))
        }
    }
}
