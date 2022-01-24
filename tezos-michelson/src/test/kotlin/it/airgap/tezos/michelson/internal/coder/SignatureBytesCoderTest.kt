package it.airgap.tezos.michelson.internal.coder

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.internal.base58.Base58
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.coder.Base58BytesCoder
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.internal.utils.asHexString
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SignatureBytesCoderTest {

    @MockK
    private lateinit var crypto: Crypto

    private lateinit var signatureBytesCoder: SignatureBytesCoder

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { crypto.hashSha256(any<ByteArray>()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        val base58Check = Base58Check(Base58(), crypto)
        val base58BytesCoder = Base58BytesCoder(base58Check)

        signatureBytesCoder = SignatureBytesCoder(base58BytesCoder)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode signature to bytes`() {
        signaturesWithBytes.forEach {
            assertContentEquals(it.second, signatureBytesCoder.encode(it.first))
        }
    }

    @Test
    fun `should fail to encode invalid signature to bytes`() {
        invalidSignatures.forEach {
            assertFailsWith<IllegalArgumentException> {
                signatureBytesCoder.encode(it)
            }
        }
    }

    @Test
    fun `should decode signature from bytes`() {
        bytesWithSignatures.forEach {
            assertEquals(it.second, signatureBytesCoder.decode(it.first))
        }
    }

    @Test
    fun `should fail to decode signature from invalid bytes`() {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> {
                signatureBytesCoder.decode(it)
            }
        }
    }

    private val signaturesWithBytes: List<Pair<String, ByteArray>>
        get() = listOf(
            "edsigtxXiEKRsdooVzfiJJL9SLAmaYg233QfsmEjcLuauLFJwE2MChg48rZ544YLDQDWf7nzBmLECC3FT5CW2rEhAFJAitfYk2J" to "bcf9811419cb597b55df14881b8b67f2ea2d17351feae37d652d0f79f6651a9668a6a9c513f4c47c2361514ab1c6524c96601b06bcb2288b15dbc22b02566368".asHexString().toByteArray(),
            "edsigtafnpWiNw8aLQ4tgsrQZuuTtiMxHXiGWyvD7QUaTHmJ8xiBS522J4TxKJVvt6oyE9rvSr21cbj3WgfcA8aJMCF762PwGNT" to "15e156a579e57146f934f5f38727eb007dec5e64f7cc270a8e92e043a968b19b2af414174f49307f3810efac23a5931902b24c11c484a2de80cec4ae1b0b0ea1".asHexString().toByteArray(),
            "edsigu2tCpHKXcYGAzVmepaaYjDJHvuRThiNvkN7mBt7Gi4XpXvfXxQuivHyphSwqrWdLTJYsvfoyqQLSxB8WAHZiTgoNwQxwBf" to "de3feb5456afb4097a4b44409016e5747a228df848646e12a88a94393d48179238f995b19680b4ce292e7f5c8b6a7c33bb738237671514aa206f67a1ccf5cdbc".asHexString().toByteArray(),
            "spsig1Gf2HJiQmpvjN8wdCUFU3wyfkn1aSwuuKsdLXHprzWqnWvZg9NwGHEzqMQ5hU2wuf7on32QmBPjKzaVEbLcehhhrJ8H7zU" to "52e66a828f7db21ec7435c93a0f46e6686c1a121c5cbc7b705fc3a268b570fe5ca94ed94157a32ce0a9cf1d2f762c62372d729259bfb0122dcd2409dd2ba40df".asHexString().toByteArray(),
            "spsig1UjgZitzkc5rKDEyrdB68LYqEpSefHhhDwTE2aXQq3mEn3tWRiF7Vte1QxHuw6T68xE2wAi1JKrHzQQQmxt68e6iYhxEBz" to "af3cc2bf94a4f71c476c3385f1f1b3decef72da3ad064faa8c6c62b8d7151c2f573233b8b29248b81a9054ca2885b3250e2bd9c898823fed2e45ed052e721d4a".asHexString().toByteArray(),
            "spsig1dHVcZRNRmBbMghTDHUxfpZrjQ2G2w8NWT62ckS1RkmhugLkGUzSv82SvQQ9rgsZpsDMrAhP1GgcZT7gj7RKyskrEZQsz9" to "f094115329046587d61f907958aa809851d5015baa5bb7bd3a014c52791d276879c7ba7ac69eecd09bc30870efef5ca525bcaa8c22ab4f06e3e130ecc44feb78".asHexString().toByteArray(),
            "p2sigfk9FuipyQZTscb1v6bFBuoWYUriLnTs7bHujG86znKtKcuTRaLvu6pPmw9gBFgeX1JqE5Vi7i5cLEVwy1jrNRWZRMqaN1" to "8cf644cdca70303a044e6dcb70387850fd0e5edf3cc866b3daeb87ec5aff1479b5cdf694a479fa11115c3e2d546eb45632a2303c8034ae842e1bc8457eb1f3e5".asHexString().toByteArray(),
            "p2sigccU6ebPoLH3EUAcBhadv66w24hQJpmYoHFpFXrzAnBoAmLM3RX5Rp9r4y4ofHbwoqNxDiXVpwrwNyNa7Mbp5g9hwaWqnu" to "75050c4372b16a5965aa5841acf5b633bfbc3fc7b6ef25836b8d34bb660f2ee2ff1566377557a9b1426559ac4b4e49bb589b95074e5d0de8c2f7e09eee1b3b3a".asHexString().toByteArray(),
            "p2sigoYJdwgDGmkyD1rLPhvzKamhBjMcndf1LxEGSbfXU541FgRuwwGF7VyGv2ff1PBRr32dATwh7JJQmZ1r4BKz1LbWKHpuvm" to "c88cf6725ca7a5757a674ab367a1520a0c5b683c72506f7dae325a37c18e88ac35a1fa33d28785b76b8798ddbbf3ff24cc18dd4146f95a8ff708460ddcc64462".asHexString().toByteArray(),
            "sigNe1cpCJHAqBP8u8CZzFbXwJB8G6DTBsW6ekcAY5HDV7NvjUXfJ8FKtM21AiHqRScWdLABTZdtkoL8sR9XiJEpaU1iBZSD" to "04f87416eccc6127e7271dc9df48a49cb29d9054171ebd8344a5da3924c79771af30ba3d8e95cf4357f0bd015d558f6069ebda09b292a5022e159598b0c3561e".asHexString().toByteArray(),
            "sigaFzui8Rne4zKyNEfQ1XuDCZmPTc8PYBEdZSNZa6ocpo46QVgygoGqS4ZiGgRwqhC88GiNNDScKZHWc62VaFEFCST2EvkV" to "5dcaf2f26fde1f8f67d6e62a6190c1b2c511886b988a8ffb643f2a06ee8256e37f3e162f20812c2a0906c5769f368f7029457d693d5c966a1f310dafa363d48d".asHexString().toByteArray(),
            "sigVqdm9m4wgMVpA8ohYbZaaEgUg9D1h1JKfSHYA82txA6wkT2eefVh92f5HNrhAgXDdHRj6gN9mf9cyP1m6cZKoAKJZFzM3" to "3c01e957560eba18f19acb4fea78468c2d8c72ff5313acfaa3a84800545ee16f144b36d7526e05936916dbee3fdf94d2556923836c6ed2201a2d7bb9cd19fbd8".asHexString().toByteArray(),
        )

    private val bytesWithSignatures: List<Pair<ByteArray, String>>
        get() = listOf(
            "61a0d3372d23ad3bdd6bb01a94afedfd34981fd12e9ea33383c940730367f3db212f364d8a616ca145ba9feef447e80eb0c755444cd36ee883de868e289de7b9".asHexString().toByteArray() to "sigam6vGztkR17zFCMSUUh4Jr8YyJ92VppSj4C8rT7hhVf5Lp41XHciua81J3MstkuhPofUdT5X9GymkDb7SeFdVuJSL1y6g",
            "6286a35a791569ead2bd995aabedd91e9347a68abb99240a4cb4342e712bdd3f6e2815cb3b74aecd44b3fdb76ac06775bed41537cdd20770079b2d94dec98392".asHexString().toByteArray() to "sigasv1LKpPRjxUo5Bz6nCAowqUbAW6srqdrjLUmJSnRA4oWmhhhDYa5y6PgAhbAJTtKmThDFjfLAUXPZg9NjhriJBu8s9Vf",
            "e8c6797c265baf3c72906be5b3a2d07c125bd3b92cd4ae27494a8607732127581899dcbd04e89e110f5957a1304ed10c379e2e1d14d6795848f227677afca9bc".asHexString().toByteArray() to "sigtSc61uwo6TVy8mtA8mfHpQj5npF4eeChmG9p12fsCaKRWbS1KqkbY8LnJLdBeNx8MyKBaXhVSrcd1GNxXTkxSXo4FsEt1",
            "9965a778ac399ca7e010936017492e05e344e0799465410ea6fe5273f361606acbd1e70d82fa8fe3aea66a7422b8c905975b86afbf31decab3eb62826a5ba065".asHexString().toByteArray() to "sigi4HBiJWYAj84M5cBwYARX9FHmwAJZ3ZoqBm3XKea2CEbq1r5Qmqgoe4i1RfiAvKbzebmpegHgYHAveN8AKjCXjUb2w1Hc",
            "2dca06d0fa6c13c6b6914be800f3dddbdcbe457ac7f4c9aef20bf298de3683a1123cbf041d38ad198c812118ab1a6182de31ad59fd8058a2cd52a7e9fd80d396".asHexString().toByteArray() to "sigTykBudsqZf7F4n9YR5dKBqWQ21PMdAY3YT7p7HB5dvyqdAcDKY7fRviR9vPLgxzKdziPh4DBtL9icjpABMRq6rxafmtav",
            "e584e6c038be755ff1e53b0e461756cfb7654ea308551e5219b5f762097b3c5f3aa898dc24ac3a478ae38721c068f306e7fe5b81af0a8e24538d58e7aa450ef6".asHexString().toByteArray() to "sigt1u2zAy9kuEsTMwbRbP2gD5RVoArx5kkhRm3Ez9gYhM6CJhp9pXjLtN7KDKfkGSSvzP1u2145N59R1cy9keySDzCNdHh8",
        )

    private val invalidSignatures: List<String>
        get() = listOf(
            "",
            "invalidAddress",
            "edsitxXiEKRsdooVzfiJJL9SLAmaYg233QfsmEjcLuauLFJwE2MChg48rZ544YLDQDWf7nzBmLECC3FT5CW2rEhAFJAitfYk2J",
            "edsigtafnpWiNw8aLQ4tgsrQZuuTtiMxHXiGWyvD7QUaTHmJ8xiBS522J4TxKJVvt6oyE9rvSr21cbj3WgfcA8aJMCF762PwG",
            "edsigu2tCpHKXcYGAzVmepaaYjDJHvuRThiNvkN7mBt7Gi4XpXvfXxQuivHyphSwqrWdLTJYsvfoyqQLSxB8WAHZiTgoNwQxwBf24",
            "spig1Gf2HJiQmpvjN8wdCUFU3wyfkn1aSwuuKsdLXHprzWqnWvZg9NwGHEzqMQ5hU2wuf7on32QmBPjKzaVEbLcehhhrJ8H7zU",
            "spsig1UjgZitzkc5rKDEyrdB68LYqEpSefHhhDwTE2aXQq3mEn3tWRiF7Vte1QxHuw6T68xE2wAi1JKrHzQQQmxt68e6",
            "spsig1dHVcZRNRmBbMghTDHUxfpZrjQ2G2w8NWT62ckS1RkmhugLkGUzSv82SvQQ9rgsZpsDMrAhP1GgcZT7gj7RKyskrEZQsz9fd6",
            "2sigfk9FuipyQZTscb1v6bFBuoWYUriLnTs7bHujG86znKtKcuTRaLvu6pPmw9gBFgeX1JqE5Vi7i5cLEVwy1jrNRWZRMqaN1",
            "p2sigccU6ebPoLH3EUAcBhadv66w24hQJpmYoHFpFXrzAnBoAmLM3RX5Rp9r4y4ofHbwoqNxDiXVpwrwNyNa7Mbp5g9hwaW",
            "p2sigoYJdwgDGmkyD1rLPhvzKamhBjMcndf1LxEGSbfXU541FgRuwwGF7VyGv2ff1PBRr32dATwh7JJQmZ1r4BKz1LbWKHpuvm84gf",
            "sNe1cpCJHAqBP8u8CZzFbXwJB8G6DTBsW6ekcAY5HDV7NvjUXfJ8FKtM21AiHqRScWdLABTZdtkoL8sR9XiJEpaU1iBZSD",
            "sigaFzui8Rne4zKyNEfQ1XuDCZmPTc8PYBEdZSNZa6ocpo46QVgygoGqS4ZiGgRwqhC88GiN",
            "sigVqdm9m4wgMVpA8ohYbZaaEgUg9D1h1JKfSHYA82txA6wkT2eefVh92f5HNrhAgXDdHRj6gN9mf9cyP1m6cZKoAKJZFzM3987f1hr",
            "sppk7a3otu8KgoNYvRZxWgMg52ZuKoghT4CumjS3dj7X8sb78Ho6qhQ",
            "tz2Mqgq7nrEXD51JkMe85w3H2WT3qMwz3Emt",
            "p2pk6zAUidUniZuM9NcFopvt59SWGzYaY7MW6GwYSmC2ya3D21HL7XN",
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "".asHexString().toByteArray(),
            "61a0d3372d23ad3bdd6bb01a94afedfd34981fd12e9ea33383c940730367f3db212f364d8a616ca145ba9feef447e80e".asHexString().toByteArray(),
            "6286a35a791569ead2bd995aabedd91e9347a68abb99240a4cb4342e712bdd3f6e2815cb3b74aecd44b3fdb76ac06775bed41537cdd20770079b2d94dec9839273".asHexString().toByteArray(),
            "e8c6797c265baf3c72906be5b3a2d07c125bd3b92cd4ae27494a8607732127581899dcbd04e89e110f5957a1304ed10c379e2e1d14d6795848f227677af3".asHexString().toByteArray(),
            "9965a778ac399ca7e010936017492e05e344e0799465410ea6fe5273f361606acbd1e70d82fa8fe3aea66a7422b8c905975b86afbf31deca".asHexString().toByteArray(),
            "2dca06d0fa6c13c6b6914be800f3dddbdcbe457ac7f4c9aef20bf298de3683a1123cbf041d38ad198c812118ab1a6182de31ad59fd8058a2cd52a7e9fd80d396a5b0".asHexString().toByteArray(),
        )
}