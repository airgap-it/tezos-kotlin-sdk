package type

import hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.ContextHash
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class ContextHashSamples {

    class Usage {

        @Test
        fun isValid() {
            val contextHash = "CoVfW92gHqxyY5m5vMFR79mrBYmsyK3swKurvFafasqHHWzhmX4Q"
            assertTrue(ContextHash.isValid(contextHash))

            val unknownHash = "VfW92gHqxyY5m5vMFR79mrBYmsyK3swKurvFafasqHHWzhmX4Q"
            assertFalse(ContextHash.isValid(unknownHash))
        }
    }
    
    class Coding {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val contextHash = ContextHash("CoVfW92gHqxyY5m5vMFR79mrBYmsyK3swKurvFafasqHHWzhmX4Q")
            assertContentEquals(hexToBytes("86688f36f6ba4b3907829802776b49f69d3b023061464ff1ee1785fcb256db7a"), contextHash.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val contextHash = hexToBytes("86688f36f6ba4b3907829802776b49f69d3b023061464ff1ee1785fcb256db7a")
            assertEquals(ContextHash("CoVfW92gHqxyY5m5vMFR79mrBYmsyK3swKurvFafasqHHWzhmX4Q"), ContextHash.decodeFromBytes(contextHash))
        }
    }
}
            