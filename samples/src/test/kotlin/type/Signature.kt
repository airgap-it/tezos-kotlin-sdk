package type

import _utils.SamplesBase
import _utils.hexToBytes
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.coder.encoded.encodeToBytes
import it.airgap.tezos.core.converter.encoded.Signature
import it.airgap.tezos.core.converter.encoded.toGenericSignature
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.crypto.bouncycastle.BouncyCastleCryptoProvider
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(Enclosed::class)
class SignatureSamples {

    class Usage : SamplesBase() {

        @Test
        fun isValid() {
            val edSignature = "edsigtxXiEKRsdooVzfiJJL9SLAmaYg233QfsmEjcLuauLFJwE2MChg48rZ544YLDQDWf7nzBmLECC3FT5CW2rEhAFJAitfYk2J"
            assertTrue(Signature.isValid(edSignature))

            val spSignature = "spsig1Gf2HJiQmpvjN8wdCUFU3wyfkn1aSwuuKsdLXHprzWqnWvZg9NwGHEzqMQ5hU2wuf7on32QmBPjKzaVEbLcehhhrJ8H7zU"
            assertTrue(Signature.isValid(spSignature))

            val p2Signature = "p2sigfk9FuipyQZTscb1v6bFBuoWYUriLnTs7bHujG86znKtKcuTRaLvu6pPmw9gBFgeX1JqE5Vi7i5cLEVwy1jrNRWZRMqaN1"
            assertTrue(Signature.isValid(p2Signature))

            val genericSignature = "sigNe1cpCJHAqBP8u8CZzFbXwJB8G6DTBsW6ekcAY5HDV7NvjUXfJ8FKtM21AiHqRScWdLABTZdtkoL8sR9XiJEpaU1iBZSD"
            assertTrue(Signature.isValid(genericSignature))

            val unknownSignature = "Ne1cpCJHAqBP8u8CZzFbXwJB8G6DTBsW6ekcAY5HDV7NvjUXfJ8FKtM21AiHqRScWdLABTZdtkoL8sR9XiJEpaU1iBZSD"
            assertFalse(Signature.isValid(unknownSignature))
        }

        @Test
        fun create() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val edSignature = Signature("edsigtxXiEKRsdooVzfiJJL9SLAmaYg233QfsmEjcLuauLFJwE2MChg48rZ544YLDQDWf7nzBmLECC3FT5CW2rEhAFJAitfYk2J")
            assertEquals(Ed25519Signature("edsigtxXiEKRsdooVzfiJJL9SLAmaYg233QfsmEjcLuauLFJwE2MChg48rZ544YLDQDWf7nzBmLECC3FT5CW2rEhAFJAitfYk2J"), edSignature)

            val spSignature = Signature("spsig1Gf2HJiQmpvjN8wdCUFU3wyfkn1aSwuuKsdLXHprzWqnWvZg9NwGHEzqMQ5hU2wuf7on32QmBPjKzaVEbLcehhhrJ8H7zU")
            assertEquals(Secp256K1Signature("spsig1Gf2HJiQmpvjN8wdCUFU3wyfkn1aSwuuKsdLXHprzWqnWvZg9NwGHEzqMQ5hU2wuf7on32QmBPjKzaVEbLcehhhrJ8H7zU"), spSignature)

            val p2Signature = Signature("p2sigfk9FuipyQZTscb1v6bFBuoWYUriLnTs7bHujG86znKtKcuTRaLvu6pPmw9gBFgeX1JqE5Vi7i5cLEVwy1jrNRWZRMqaN1")
            assertEquals(P256Signature("p2sigfk9FuipyQZTscb1v6bFBuoWYUriLnTs7bHujG86znKtKcuTRaLvu6pPmw9gBFgeX1JqE5Vi7i5cLEVwy1jrNRWZRMqaN1"), p2Signature)

            val genericSignature = Signature("sigNe1cpCJHAqBP8u8CZzFbXwJB8G6DTBsW6ekcAY5HDV7NvjUXfJ8FKtM21AiHqRScWdLABTZdtkoL8sR9XiJEpaU1iBZSD")
            assertEquals(GenericSignature("sigNe1cpCJHAqBP8u8CZzFbXwJB8G6DTBsW6ekcAY5HDV7NvjUXfJ8FKtM21AiHqRScWdLABTZdtkoL8sR9XiJEpaU1iBZSD"), genericSignature)
        }

        @Test
        fun toGeneric() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val edSignature = Signature("edsigtczTq2EC9VQNRRT53gzcs25DJFg1iZeTzQxY7jBtjradZb8qqZaqzAYSbVWvg1abvqFpQCV8TgqotDwckJiTJ9fJ2eYESb")
            assertEquals(GenericSignature("sigTAzhy1HsZDLNETmuf9RuinhXRb5jvmscjCoPPBujWZgFmCFLffku7JXYtu8aYQFVHnCUghmd4t39RuR6ANV76bCCYTR9u"), edSignature.toGenericSignature())

            val spSignature = Signature("spsig1PptyKUAGumWN9qs9aW2MafGp8kXekDAzEPCpoJUUPjVzQwmKdNBti5CA3nMTVcUaM3dcS2JSQwUNGtbYvHbSeU5eTTK6Z")
            assertEquals(GenericSignature("sigg1DeFruoZoMyyr7BRwshfytuJxxagaUWfFt419AfVonZMHx94HowabLTgDGQ6YcVdJUsUAg1GnkGzBV33c6XRvyAQ3tby"), spSignature.toGenericSignature())

            val p2Signature = Signature("p2sigr37wjbaL4XCywZcB3YXdGf7Jz8ibYkTK6VjHXQrPyV98w5Y6S3XJMfV7fdrMfvwqwy2Pryf3eHztYaxCAXUivBupwUEDK")
            assertEquals(GenericSignature("sigriu6eW2EbbAX1JqvKwzqmCKvbUVLTL36eisu7xR6m2rbPokn3zS55V4pUhL8EjNXKwWNnjii9LgX9u2Ta5HHeVZvj6kjP"), p2Signature.toGenericSignature())

            val genericSignature = Signature("sigNe1cpCJHAqBP8u8CZzFbXwJB8G6DTBsW6ekcAY5HDV7NvjUXfJ8FKtM21AiHqRScWdLABTZdtkoL8sR9XiJEpaU1iBZSD")
            assertEquals(GenericSignature("sigNe1cpCJHAqBP8u8CZzFbXwJB8G6DTBsW6ekcAY5HDV7NvjUXfJ8FKtM21AiHqRScWdLABTZdtkoL8sR9XiJEpaU1iBZSD"), genericSignature.toGenericSignature())
        }
    }
    
    class Coding : SamplesBase() {

        @Test
        fun toBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val edSignature = Signature("edsigtxXiEKRsdooVzfiJJL9SLAmaYg233QfsmEjcLuauLFJwE2MChg48rZ544YLDQDWf7nzBmLECC3FT5CW2rEhAFJAitfYk2J")
            assertContentEquals(hexToBytes("bcf9811419cb597b55df14881b8b67f2ea2d17351feae37d652d0f79f6651a9668a6a9c513f4c47c2361514ab1c6524c96601b06bcb2288b15dbc22b02566368"), edSignature.encodeToBytes())

            val spSignature = Signature("spsig1Gf2HJiQmpvjN8wdCUFU3wyfkn1aSwuuKsdLXHprzWqnWvZg9NwGHEzqMQ5hU2wuf7on32QmBPjKzaVEbLcehhhrJ8H7zU")
            assertContentEquals(hexToBytes("52e66a828f7db21ec7435c93a0f46e6686c1a121c5cbc7b705fc3a268b570fe5ca94ed94157a32ce0a9cf1d2f762c62372d729259bfb0122dcd2409dd2ba40df"), spSignature.encodeToBytes())

            val p2Signature = Signature("p2sigfk9FuipyQZTscb1v6bFBuoWYUriLnTs7bHujG86znKtKcuTRaLvu6pPmw9gBFgeX1JqE5Vi7i5cLEVwy1jrNRWZRMqaN1")
            assertContentEquals(hexToBytes("8cf644cdca70303a044e6dcb70387850fd0e5edf3cc866b3daeb87ec5aff1479b5cdf694a479fa11115c3e2d546eb45632a2303c8034ae842e1bc8457eb1f3e5"), p2Signature.encodeToBytes())

            val genericSignature = Signature("sigNe1cpCJHAqBP8u8CZzFbXwJB8G6DTBsW6ekcAY5HDV7NvjUXfJ8FKtM21AiHqRScWdLABTZdtkoL8sR9XiJEpaU1iBZSD")
            assertContentEquals(hexToBytes("04f87416eccc6127e7271dc9df48a49cb29d9054171ebd8344a5da3924c79771af30ba3d8e95cf4357f0bd015d558f6069ebda09b292a5022e159598b0c3561e"), genericSignature.encodeToBytes())
        }

        @Test
        fun fromBytes() {
            Tezos {
                isDefault = true
                cryptoProvider = BouncyCastleCryptoProvider()
            }

            val edSignature = hexToBytes("bcf9811419cb597b55df14881b8b67f2ea2d17351feae37d652d0f79f6651a9668a6a9c513f4c47c2361514ab1c6524c96601b06bcb2288b15dbc22b02566368")
            assertEquals(Ed25519Signature("edsigtxXiEKRsdooVzfiJJL9SLAmaYg233QfsmEjcLuauLFJwE2MChg48rZ544YLDQDWf7nzBmLECC3FT5CW2rEhAFJAitfYk2J"), Ed25519Signature.decodeFromBytes(edSignature))

            val spSignature = hexToBytes("52e66a828f7db21ec7435c93a0f46e6686c1a121c5cbc7b705fc3a268b570fe5ca94ed94157a32ce0a9cf1d2f762c62372d729259bfb0122dcd2409dd2ba40df")
            assertEquals(Secp256K1Signature("spsig1Gf2HJiQmpvjN8wdCUFU3wyfkn1aSwuuKsdLXHprzWqnWvZg9NwGHEzqMQ5hU2wuf7on32QmBPjKzaVEbLcehhhrJ8H7zU"), Secp256K1Signature.decodeFromBytes(spSignature))

            val p2Signature = hexToBytes("8cf644cdca70303a044e6dcb70387850fd0e5edf3cc866b3daeb87ec5aff1479b5cdf694a479fa11115c3e2d546eb45632a2303c8034ae842e1bc8457eb1f3e5")
            assertEquals(P256Signature("p2sigfk9FuipyQZTscb1v6bFBuoWYUriLnTs7bHujG86znKtKcuTRaLvu6pPmw9gBFgeX1JqE5Vi7i5cLEVwy1jrNRWZRMqaN1"), P256Signature.decodeFromBytes(p2Signature))

            val genericSignature = hexToBytes("04f87416eccc6127e7271dc9df48a49cb29d9054171ebd8344a5da3924c79771af30ba3d8e95cf4357f0bd015d558f6069ebda09b292a5022e159598b0c3561e")
            assertEquals(GenericSignature("sigNe1cpCJHAqBP8u8CZzFbXwJB8G6DTBsW6ekcAY5HDV7NvjUXfJ8FKtM21AiHqRScWdLABTZdtkoL8sR9XiJEpaU1iBZSD"), GenericSignature.decodeFromBytes(genericSignature))
        }
    }
}
            