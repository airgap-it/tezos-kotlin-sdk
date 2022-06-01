package type.encoded

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.SaplingAddress
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class SaplingAddressSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val zet1Address = "zet13cDGmHmJpCn2AXoA2T44rzLqTybVDnEKNsoNjdb1rMz3R1my9oxokWY8rVmwZuKUG"
            assertTrue(SaplingAddress.isValid(zet1Address))

            val unknownAddress = "3cDGmHmJpCn2AXoA2T44rzLqTybVDnEKNsoNjdb1rMz3R1my9oxokWY8rVmwZuKUG"
            assertFalse(SaplingAddress.isValid(unknownAddress))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val zet1Address = SaplingAddress("zet13cDGmHmJpCn2AXoA2T44rzLqTybVDnEKNsoNjdb1rMz3R1my9oxokWY8rVmwZuKUG")
            assertContentEquals(hexToBytes("919d19119a4059cca0c2ba9d58b4aac6eb5a4bca0fe6d85549b071a7e63cbab96b2f58fb822d997325aea9"), zet1Address.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val zet1Address = hexToBytes("919d19119a4059cca0c2ba9d58b4aac6eb5a4bca0fe6d85549b071a7e63cbab96b2f58fb822d997325aea9")
            assertEquals(SaplingAddress("zet13cDGmHmJpCn2AXoA2T44rzLqTybVDnEKNsoNjdb1rMz3R1my9oxokWY8rVmwZuKUG"), SaplingAddress.decodeFromBytes(zet1Address))
        }
    }
}
            