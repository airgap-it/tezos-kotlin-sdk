package type

import _utils.SamplesBase
import _utils.hexToBytes
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

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val blockMetadataHash = "bm2uGR9MDRyRbyDnEbz98QepwrFG8mh2GAMfVokRynvmhHoxRUTY"
            assertTrue(BlockMetadataHash.isValid(blockMetadataHash))

            val unknownHash = "2uGR9MDRyRbyDnEbz98QepwrFG8mh2GAMfVokRynvmhHoxRUTY"
            assertFalse(BlockMetadataHash.isValid(unknownHash))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val blockMetadataHash = BlockMetadataHash("bm2uGR9MDRyRbyDnEbz98QepwrFG8mh2GAMfVokRynvmhHoxRUTY")
            assertContentEquals(hexToBytes("2b67b63e7cf806ced7410e04f8d6102a06bb7da3aac39a4a5244318e357e9f69"), blockMetadataHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val blockMetadataHash = hexToBytes("2b67b63e7cf806ced7410e04f8d6102a06bb7da3aac39a4a5244318e357e9f69")
            assertEquals(BlockMetadataHash("bm2uGR9MDRyRbyDnEbz98QepwrFG8mh2GAMfVokRynvmhHoxRUTY"), BlockMetadataHash.decodeFromBytes(blockMetadataHash))
        }
    }
}
            