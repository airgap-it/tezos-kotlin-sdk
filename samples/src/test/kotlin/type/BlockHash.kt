package type

import _utils.SamplesBase
import _utils.hexToBytes
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

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val blockHash = "BLrUSnmhoWczorTYG8utWTLcD8yup6MX1MCehXG8f8QWew8t1N8"
            assertTrue(BlockHash.isValid(blockHash))

            val unknownHash = "LrUSnmhoWczorTYG8utWTLcD8yup6MX1MCehXG8f8QWew8t1N8"
            assertFalse(BlockHash.isValid(unknownHash))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val blockHash = BlockHash("BLrUSnmhoWczorTYG8utWTLcD8yup6MX1MCehXG8f8QWew8t1N8")
            assertContentEquals(hexToBytes("964bc74a926eaa6c1ef858f526beb7b107c9bb28e38c9d8e4e11e41314699868"), blockHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val blockHash = hexToBytes("964bc74a926eaa6c1ef858f526beb7b107c9bb28e38c9d8e4e11e41314699868")
            assertEquals(BlockHash("BLrUSnmhoWczorTYG8utWTLcD8yup6MX1MCehXG8f8QWew8t1N8"), BlockHash.decodeFromBytes(blockHash))
        }
    }
}
            