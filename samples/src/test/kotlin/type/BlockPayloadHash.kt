package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.BlockPayloadHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class BlockPayloadHashSamples {

    class Usage {

        @Test
        fun isValid() {
            val blockPayloadHash = "vh2rvgiNNmaBn3tW62KVqNLphShiC8X8Ao1GxC1gPfmTDz5SWWQG"
            assertTrue(BlockPayloadHash.isValid(blockPayloadHash))

            val unknownHash = "2rvgiNNmaBn3tW62KVqNLphShiC8X8Ao1GxC1gPfmTDz5SWWQG"
            assertFalse(BlockPayloadHash.isValid(unknownHash))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            val tezos = Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val blockPayloadHash = BlockPayloadHash("vh2rvgiNNmaBn3tW62KVqNLphShiC8X8Ao1GxC1gPfmTDz5SWWQG")
            assertContentEquals(hexToBytes("9c39bccfb8b3fc02c94ee78f7c0d372def7a956a3e9bec3aec7906f2d07b7d90"), blockPayloadHash.encodeToBytes())
            assertContentEquals(hexToBytes("9c39bccfb8b3fc02c94ee78f7c0d372def7a956a3e9bec3aec7906f2d07b7d90"), blockPayloadHash.encodeToBytes(tezos))
        }

        @Test
        fun fromBytes() {
            val tezos = Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val blockPayloadHash = hexToBytes("9c39bccfb8b3fc02c94ee78f7c0d372def7a956a3e9bec3aec7906f2d07b7d90")
            assertEquals(BlockPayloadHash("vh2rvgiNNmaBn3tW62KVqNLphShiC8X8Ao1GxC1gPfmTDz5SWWQG"), BlockPayloadHash.decodeFromBytes(blockPayloadHash))
            assertEquals(BlockPayloadHash("vh2rvgiNNmaBn3tW62KVqNLphShiC8X8Ao1GxC1gPfmTDz5SWWQG"), BlockPayloadHash.decodeFromBytes(blockPayloadHash, tezos))
        }
    }
}
