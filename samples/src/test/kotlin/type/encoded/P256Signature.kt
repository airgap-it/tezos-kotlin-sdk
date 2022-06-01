package type.encoded

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.converter.encoded.fromGenericSignature
import it.airgap.tezos.core.converter.encoded.toGenericSignature
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.P256Signature
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class P256SignatureSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val signature = "p2sigczazEpsNTrsYK5upc6wtLwbGiXaFXGB89rkcgkj5W8Ssp1VTaovFxR1SzNeEwvoXA7x5FmMKBFioHMnUCMJYkU1Lykut9"
            assertTrue(P256Signature.isValid(signature))

            val unknownSignature = "czazEpsNTrsYK5upc6wtLwbGiXaFXGB89rkcgkj5W8Ssp1VTaovFxR1SzNeEwvoXA7x5FmMKBFioHMnUCMJYkU1Lykut9"
            assertFalse(P256Signature.isValid(unknownSignature))
        }

        @Test
        fun fromGeneric() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val genericSignature = GenericSignature("sigriu6eW2EbbAX1JqvKwzqmCKvbUVLTL36eisu7xR6m2rbPokn3zS55V4pUhL8EjNXKwWNnjii9LgX9u2Ta5HHeVZvj6kjP")
            assertEquals(P256Signature("p2sigr37wjbaL4XCywZcB3YXdGf7Jz8ibYkTK6VjHXQrPyV98w5Y6S3XJMfV7fdrMfvwqwy2Pryf3eHztYaxCAXUivBupwUEDK"), P256Signature.fromGenericSignature(genericSignature))
        }

        @Test
        fun toGeneric() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val p2Signature = P256Signature("p2sigr37wjbaL4XCywZcB3YXdGf7Jz8ibYkTK6VjHXQrPyV98w5Y6S3XJMfV7fdrMfvwqwy2Pryf3eHztYaxCAXUivBupwUEDK")
            assertEquals(GenericSignature("sigriu6eW2EbbAX1JqvKwzqmCKvbUVLTL36eisu7xR6m2rbPokn3zS55V4pUhL8EjNXKwWNnjii9LgX9u2Ta5HHeVZvj6kjP"), p2Signature.toGenericSignature())
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val signature = P256Signature("p2sigczazEpsNTrsYK5upc6wtLwbGiXaFXGB89rkcgkj5W8Ssp1VTaovFxR1SzNeEwvoXA7x5FmMKBFioHMnUCMJYkU1Lykut9")
            assertContentEquals(hexToBytes("77ef465fd5281bead9276e38352e1435498014eef3ffdfcc2cbbee19c3900434a39641759d9951d5ed9247135296dbdb20f756a839a0265880174bdc3d02e9b0"), signature.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val signature = hexToBytes("77ef465fd5281bead9276e38352e1435498014eef3ffdfcc2cbbee19c3900434a39641759d9951d5ed9247135296dbdb20f756a839a0265880174bdc3d02e9b0")
            assertEquals(P256Signature("p2sigczazEpsNTrsYK5upc6wtLwbGiXaFXGB89rkcgkj5W8Ssp1VTaovFxR1SzNeEwvoXA7x5FmMKBFioHMnUCMJYkU1Lykut9"), P256Signature.decodeFromBytes(signature))
        }
    }
}
            