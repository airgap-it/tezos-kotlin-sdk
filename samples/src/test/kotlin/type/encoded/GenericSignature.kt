package type.encoded

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class GenericSignatureSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val signature = "sigsRk6NwxVrJpBRk9yBjWAH2RdQr5NaaXz7mwkvsPfSZxosdNCN9UQLp5TTFYCuriFBSupYPxdepf6rN9Tpr6yCoptyATqj"
            assertTrue(GenericSignature.isValid(signature))

            val unknownSignature = "sRk6NwxVrJpBRk9yBjWAH2RdQr5NaaXz7mwkvsPfSZxosdNCN9UQLp5TTFYCuriFBSupYPxdepf6rN9Tpr6yCoptyATqj"
            assertFalse(GenericSignature.isValid(unknownSignature))
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val signature = GenericSignature("sigsRk6NwxVrJpBRk9yBjWAH2RdQr5NaaXz7mwkvsPfSZxosdNCN9UQLp5TTFYCuriFBSupYPxdepf6rN9Tpr6yCoptyATqj")
            assertContentEquals(hexToBytes("e104a23cd216f6e4aa3039edcc4a2c48b01695caed6aeedf77402096a3b1e2bd33cee241465b2192b2eeeccd004fd09aa9321751c3e16a019a7ab16983ee8f56"), signature.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val signature = hexToBytes("e104a23cd216f6e4aa3039edcc4a2c48b01695caed6aeedf77402096a3b1e2bd33cee241465b2192b2eeeccd004fd09aa9321751c3e16a019a7ab16983ee8f56")
            assertEquals(GenericSignature("sigsRk6NwxVrJpBRk9yBjWAH2RdQr5NaaXz7mwkvsPfSZxosdNCN9UQLp5TTFYCuriFBSupYPxdepf6rN9Tpr6yCoptyATqj"), GenericSignature.decodeFromBytes(signature))
        }
    }
}
            