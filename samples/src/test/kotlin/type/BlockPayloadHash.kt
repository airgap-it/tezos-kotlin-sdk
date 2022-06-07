package type

import _utils.SamplesBase
import _utils.hexToBytes
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

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val blockPayloadHash = "vh1gFjfx5rV2WyrgXZCv5UrToDEgcVvfjjwuocbmFJm6h75kDfCm"
            assertTrue(BlockPayloadHash.isValid(blockPayloadHash))

            val unknownHash = "1gFjfx5rV2WyrgXZCv5UrToDEgcVvfjjwuocbmFJm6h75kDfCm"
            assertFalse(BlockPayloadHash.isValid(unknownHash))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val blockPayloadHash = BlockPayloadHash("vh1gFjfx5rV2WyrgXZCv5UrToDEgcVvfjjwuocbmFJm6h75kDfCm")
            assertContentEquals(hexToBytes("004c65194060da5531f8b7b0e966eee2481ae0ad17f729edaf6310b6c53d8cc7"), blockPayloadHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val blockPayloadHash = hexToBytes("004c65194060da5531f8b7b0e966eee2481ae0ad17f729edaf6310b6c53d8cc7")
            assertEquals(BlockPayloadHash("vh1gFjfx5rV2WyrgXZCv5UrToDEgcVvfjjwuocbmFJm6h75kDfCm"), BlockPayloadHash.decodeFromBytes(blockPayloadHash))
        }
    }
}
            