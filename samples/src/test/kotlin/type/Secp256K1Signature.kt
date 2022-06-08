package type

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.converter.encoded.fromGenericSignature
import it.airgap.tezos.core.converter.encoded.toGenericSignature
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.Secp256K1Signature
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class Secp256K1SignatureSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val signature = "spsig1C66is755BWVGX56tTWwRZWVdDrQDdp7u48XJ1pku6B5jmP8gSciNfAjwmVaqFZQeHKpoNJBffjN4UHVtZRrZdmYAw9Sx3"
            assertTrue(Secp256K1Signature.isValid(signature))

            val unknownSignature = "C66is755BWVGX56tTWwRZWVdDrQDdp7u48XJ1pku6B5jmP8gSciNfAjwmVaqFZQeHKpoNJBffjN4UHVtZRrZdmYAw9Sx3"
            assertFalse(Secp256K1Signature.isValid(unknownSignature))
        }

        @Test
        fun fromGeneric() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val genericSignature = GenericSignature("sigg1DeFruoZoMyyr7BRwshfytuJxxagaUWfFt419AfVonZMHx94HowabLTgDGQ6YcVdJUsUAg1GnkGzBV33c6XRvyAQ3tby")
            assertEquals(Secp256K1Signature("spsig1PptyKUAGumWN9qs9aW2MafGp8kXekDAzEPCpoJUUPjVzQwmKdNBti5CA3nMTVcUaM3dcS2JSQwUNGtbYvHbSeU5eTTK6Z"), Secp256K1Signature.fromGenericSignature(genericSignature))
        }

        @Test
        fun toGeneric() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val spSignature = Secp256K1Signature("spsig1PptyKUAGumWN9qs9aW2MafGp8kXekDAzEPCpoJUUPjVzQwmKdNBti5CA3nMTVcUaM3dcS2JSQwUNGtbYvHbSeU5eTTK6Z")
            assertEquals(GenericSignature("sigg1DeFruoZoMyyr7BRwshfytuJxxagaUWfFt419AfVonZMHx94HowabLTgDGQ6YcVdJUsUAg1GnkGzBV33c6XRvyAQ3tby"), spSignature.toGenericSignature())
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val signature = Secp256K1Signature("spsig1C66is755BWVGX56tTWwRZWVdDrQDdp7u48XJ1pku6B5jmP8gSciNfAjwmVaqFZQeHKpoNJBffjN4UHVtZRrZdmYAw9Sx3")
            assertContentEquals(hexToBytes("2ffca0990cfbe315aae82223d9470d314f87d30199390c8b05be6b70a4f443835c29262d06ad5395c6a90d83477491c445c08dfa11dcfb26e62b516fa115f2fc"), signature.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val signature = hexToBytes("2ffca0990cfbe315aae82223d9470d314f87d30199390c8b05be6b70a4f443835c29262d06ad5395c6a90d83477491c445c08dfa11dcfb26e62b516fa115f2fc")
            assertEquals(Secp256K1Signature("spsig1C66is755BWVGX56tTWwRZWVdDrQDdp7u48XJ1pku6B5jmP8gSciNfAjwmVaqFZQeHKpoNJBffjN4UHVtZRrZdmYAw9Sx3"), Secp256K1Signature.decodeFromBytes(signature))
        }
    }
}
            