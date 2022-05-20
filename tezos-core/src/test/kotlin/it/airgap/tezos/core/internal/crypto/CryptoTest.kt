package it.airgap.tezos.core.internal.crypto

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.padStartEven
import it.airgap.tezos.core.internal.utils.splitAt
import it.airgap.tezos.core.internal.utils.toHexString
import org.bouncycastle.crypto.digests.Blake2bDigest
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.params.*
import org.bouncycastle.crypto.signers.ECDSASigner
import org.bouncycastle.crypto.signers.Ed25519Signer
import org.bouncycastle.crypto.signers.HMacDSAKCalculator
import org.bouncycastle.jce.ECNamedCurveTable
import org.junit.Before
import org.junit.Test
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.min
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class CryptoTest {
    @MockK
    private lateinit var cryptoProvider: CryptoProvider

    private lateinit var crypto: Crypto

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { cryptoProvider.sha256(any()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        every { cryptoProvider.blake2b(any(), any()) } answers {
            val message = firstArg<ByteArray>()
            val size = secondArg<Int>()
            val blake2bDigest = Blake2bDigest(min(size * 8, 512))

            ByteArray(blake2bDigest.digestSize).let {
                blake2bDigest.update(message, 0, message.size)
                blake2bDigest.doFinal(it, 0)

                it.sliceArray(0 until size)
            }
        }

        every { cryptoProvider.signEd25519(any(), any()) } answers {
            val message = firstArg<ByteArray>()
            val key = secondArg<ByteArray>()

            val signer = Ed25519Signer().apply { init(true, Ed25519PrivateKeyParameters(key.sliceArray(0 until 32))) }
            val signature = signer.run {
                update(message, 0, message.size)
                generateSignature()
            }

            signature
        }

        every { cryptoProvider.verifyEd25519(any(), any(), any()) } answers {
            val message = firstArg<ByteArray>()
            val signature = secondArg<ByteArray>()
            val key = thirdArg<ByteArray>()

            val signer = Ed25519Signer().apply { init(false, Ed25519PublicKeyParameters(key)) }
            val verified = signer.run {
                update(message, 0, message.size)
                verifySignature(signature)
            }

            verified
        }

        every { cryptoProvider.signSecp256K1(any(), any()) } answers {
            val message = firstArg<ByteArray>()
            val key = secondArg<ByteArray>()

            val spec = ECNamedCurveTable.getParameterSpec("secp256K1")
            val domainParameters = ECDomainParameters(spec.curve, spec.g, spec.n, spec.h)
            val d = BigInteger(key)

            val signer = ECDSASigner(HMacDSAKCalculator(SHA256Digest())).apply {
                init(true, ECPrivateKeyParameters(d, domainParameters))
            }
            val signature = signer.generateSignature(message).let { (r, s) ->
                val s = if (s <= spec.n.shiftRight(1)) s else spec.n - s
                r.toString(16) + s.toString(16)
            }

            signature.asHexString().toByteArray()
        }

        every { cryptoProvider.verifySecp256K1(any(), any(), any()) } answers {
            val message = firstArg<ByteArray>()
            val signature = secondArg<ByteArray>()
            val key = thirdArg<ByteArray>()

            val spec = ECNamedCurveTable.getParameterSpec("secp256K1")
            val domainParameters = ECDomainParameters(spec.curve, spec.g, spec.n, spec.h)
            val q = spec.curve.decodePoint(key)

            val signer = ECDSASigner(HMacDSAKCalculator(SHA256Digest())).apply {
                init(false, ECPublicKeyParameters(q, domainParameters))
            }
            val (r, s) = signature.splitAt(32).let { (r, s) ->
                r.toHexString().asString() to s.toHexString().asString()
            }
            val verified = signer.verifySignature(message, BigInteger(r, 16), BigInteger(s, 16))

            verified
        }

        every { cryptoProvider.signP256(any(), any()) } answers {
            val message = firstArg<ByteArray>()
            val key = secondArg<ByteArray>()

            val spec = ECNamedCurveTable.getParameterSpec("secp256r1")
            val domainParameters = ECDomainParameters(spec.curve, spec.g, spec.n, spec.h)
            val d = BigInteger(key)

            val signer = ECDSASigner(HMacDSAKCalculator(SHA256Digest())).apply {
                init(true, ECPrivateKeyParameters(d, domainParameters))
            }
            val signature = signer.generateSignature(message).let { (r, s) ->
                val s = if (s <= spec.n.shiftRight(1)) s else spec.n - s
                r.toString(16).padStartEven('0') + s.toString(16).padStartEven('0')
            }

            signature.asHexString().toByteArray()
        }

        every { cryptoProvider.verifyP256(any(), any(), any()) } answers {
            val message = firstArg<ByteArray>()
            val signature = secondArg<ByteArray>()
            val key = thirdArg<ByteArray>()

            val spec = ECNamedCurveTable.getParameterSpec("secp256r1")
            val domainParameters = ECDomainParameters(spec.curve, spec.g, spec.n, spec.h)
            val q = spec.curve.decodePoint(key)

            val signer = ECDSASigner(HMacDSAKCalculator(SHA256Digest())).apply {
                init(false, ECPublicKeyParameters(q, domainParameters))
            }
            val (r, s) = signature.splitAt(32).let { (r, s) ->
                r.toHexString().asString() to s.toHexString().asString()
            }
            val verified = signer.verifySignature(message, BigInteger(r, 16), BigInteger(s, 16))

            verified
        }

        crypto = Crypto(cryptoProvider)
    }

    @Test
    fun `creates SHA-256 hash from HexString message`() {
        val messagesWithExpected = listOf(
            "05" to "e77b9a9ae9e30b0dbdb6f510a264ef9de781501d7b6b92ae89eb059c5ab743db",
            "e8" to "e6f207509afa3908da116ce61a7576954248d9fe64a3c652b493cca57ce36e2e",
            "c61942f42ed2df" to "29a6301663a3afb934eaa4f9e4caa7f220fac102b9eb6b2e58a945efc6b4c441",
            "5b992df7bc5fd0592963" to "dbabf308f33f8dc06ae4acd497cb2214342fa110f593d6738decccda4cfb54ab",
            "d8c4c50a33c3fd6688e1928df9" to "bba994ecac057f70017f80ba02f8ceb84d0318edac6067625288baff41fcc3a9",
            "a20acc96fd827e8a6e76fd7608552d6a" to "bfad18f5c02229b8de2595e661e35f57a8c4e595a80b08742d801fae57c001c2",
            "08ed2c535272820eb78453586f6c5cd92876b0" to "3e13b76586a0933f39010cca6d55ec61bc99e73971c85788d05f89e0eaccf82c",
        )

        messagesWithExpected.forEach {
            val message = it.first.asHexString()
            val expected = it.second.asHexString().toByteArray()

            assertContentEquals(expected, crypto.hashSha256(message))
        }
    }

    @Test
    fun `creates SHA-256 hash from ByteArray message`() {
        val messagesWithExpected = listOf(
            "05" to "e77b9a9ae9e30b0dbdb6f510a264ef9de781501d7b6b92ae89eb059c5ab743db",
            "e8" to "e6f207509afa3908da116ce61a7576954248d9fe64a3c652b493cca57ce36e2e",
            "c61942f42ed2df" to "29a6301663a3afb934eaa4f9e4caa7f220fac102b9eb6b2e58a945efc6b4c441",
            "5b992df7bc5fd0592963" to "dbabf308f33f8dc06ae4acd497cb2214342fa110f593d6738decccda4cfb54ab",
            "d8c4c50a33c3fd6688e1928df9" to "bba994ecac057f70017f80ba02f8ceb84d0318edac6067625288baff41fcc3a9",
            "a20acc96fd827e8a6e76fd7608552d6a" to "bfad18f5c02229b8de2595e661e35f57a8c4e595a80b08742d801fae57c001c2",
            "08ed2c535272820eb78453586f6c5cd92876b0" to "3e13b76586a0933f39010cca6d55ec61bc99e73971c85788d05f89e0eaccf82c",
        )

        messagesWithExpected.forEach {
            val message = it.first.asHexString().toByteArray()
            val expected = it.second.asHexString().toByteArray()

            assertContentEquals(expected, crypto.hashSha256(message))
        }
    }

    @Test
    fun `creates hash from HexString message`() {
        val messagesWithExpected = listOf(
            Pair("05", 14) to "83c3adbe3df356621ae564f8bbc9",
            Pair("e8", 15) to "ede34bef1d2c6eea55d71e32274ea6",
            Pair("c61942f42ed2df", 20) to "64784d8512a767216a6e1c49da6c1a802f2230fa",
            Pair("5b992df7bc5fd0592963", 7) to "878fd55e5ff578",
            Pair("d8c4c50a33c3fd6688e1928df9", 1) to "fd",
            Pair("a20acc96fd827e8a6e76fd7608552d6a", 32) to "95425e29f8a2b001afa3297b75f5034800c2e73c2f07d47a765f0b621f8ccc40",
            Pair("08ed2c535272820eb78453586f6c5cd92876b0", 64) to "cf2462cfcbb7369d6f044051c3fb3dc1892ce41d4c672e74023880556cfa87c1b6e5a62b0e82cc539ac48f91780dd25d9367ad5888e224cf76c3e2173c461229",
        )

        messagesWithExpected.forEach {
            val message = it.first.first.asHexString()
            val size = it.first.second
            val expected = it.second.asHexString().toByteArray()

            assertContentEquals(expected, crypto.hash(message, size))
        }
    }

    @Test
    fun `creates hash from ByteArray message`() {
        val messagesWithExpected = listOf(
            Pair("05", 14) to "83c3adbe3df356621ae564f8bbc9",
            Pair("e8", 15) to "ede34bef1d2c6eea55d71e32274ea6",
            Pair("c61942f42ed2df", 20) to "64784d8512a767216a6e1c49da6c1a802f2230fa",
            Pair("5b992df7bc5fd0592963", 7) to "878fd55e5ff578",
            Pair("d8c4c50a33c3fd6688e1928df9", 1) to "fd",
            Pair("a20acc96fd827e8a6e76fd7608552d6a", 32) to "95425e29f8a2b001afa3297b75f5034800c2e73c2f07d47a765f0b621f8ccc40",
            Pair("08ed2c535272820eb78453586f6c5cd92876b0", 64) to "cf2462cfcbb7369d6f044051c3fb3dc1892ce41d4c672e74023880556cfa87c1b6e5a62b0e82cc539ac48f91780dd25d9367ad5888e224cf76c3e2173c461229",
        )

        messagesWithExpected.forEach {
            val message = it.first.first.asHexString().toByteArray()
            val size = it.first.second
            val expected = it.second.asHexString().toByteArray()

            assertContentEquals(expected, crypto.hash(message, size))
        }
    }

    @Test
    fun `signs HexString message with HexString Ed25519 secret key`() {
        val messagesWithExpected = listOf(
            "bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69" to "f3d7c25a6dd553ee9a558b470e3ebfe607b8e725da946f6984e3a2a9e18050a04fb45dfc27558763a9339e841af21714c9d038b710830975ad92a28fb4b33205",
            "a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0" to "581a028d8e637423e32b50b96ff8636e0c0a68d32652c493a4ff3721f50d017b36ead9cf1ac6830fae78981a67a3e509638c308a02b0effd17442eb7690c1a0d",
        )

        val secretKey = ed25519KeyPair.first.toHexString()

        messagesWithExpected.forEach {
            val message = it.first.asHexString()
            val expected = it.second.asHexString().toByteArray()

            assertContentEquals(expected, crypto.signEd25519(message, secretKey))
        }
    }

    @Test
    fun `signs ByteArray message with ByteArray Ed25519 secret key`() {
        val messagesWithExpected = listOf(
            "bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69" to "f3d7c25a6dd553ee9a558b470e3ebfe607b8e725da946f6984e3a2a9e18050a04fb45dfc27558763a9339e841af21714c9d038b710830975ad92a28fb4b33205",
            "a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0" to "581a028d8e637423e32b50b96ff8636e0c0a68d32652c493a4ff3721f50d017b36ead9cf1ac6830fae78981a67a3e509638c308a02b0effd17442eb7690c1a0d",
        )

        val secretKey = ed25519KeyPair.first

        messagesWithExpected.forEach {
            val message = it.first.asHexString().toByteArray()
            val expected = it.second.asHexString().toByteArray()

            assertContentEquals(expected, crypto.signEd25519(message, secretKey))
        }
    }

    @Test
    fun `verifies HexString message and signature with HexString Ed25519 public key`() {
        val messagesAndSignaturesWithExpected = listOf(
            Pair("bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69", "f3d7c25a6dd553ee9a558b470e3ebfe607b8e725da946f6984e3a2a9e18050a04fb45dfc27558763a9339e841af21714c9d038b710830975ad92a28fb4b33205") to true,
            Pair("a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0", "581a028d8e637423e32b50b96ff8636e0c0a68d32652c493a4ff3721f50d017b36ead9cf1ac6830fae78981a67a3e509638c308a02b0effd17442eb7690c1a0d") to true,
            Pair("bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69", "581a028d8e637423e32b50b96ff8636e0c0a68d32652c493a4ff3721f50d017b36ead9cf1ac6830fae78981a67a3e509638c308a02b0effd17442eb7690c1a0d") to false,
            Pair("a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0", "f3d7c25a6dd553ee9a558b470e3ebfe607b8e725da946f6984e3a2a9e18050a04fb45dfc27558763a9339e841af21714c9d038b710830975ad92a28fb4b33205") to false,
        )

        val publicKey = ed25519KeyPair.second.toHexString()

        messagesAndSignaturesWithExpected.forEach {
            val message = it.first.first.asHexString()
            val signature = it.first.second.asHexString()
            val expected = it.second

            assertEquals(expected, crypto.verifyEd25519(message, signature, publicKey))
        }
    }

    @Test
    fun `verifies ByteArray message and signature with ByteArray Ed25519 public key`() {
        val messagesAndSignaturesWithExpected = listOf(
            Pair("bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69", "f3d7c25a6dd553ee9a558b470e3ebfe607b8e725da946f6984e3a2a9e18050a04fb45dfc27558763a9339e841af21714c9d038b710830975ad92a28fb4b33205") to true,
            Pair("a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0", "581a028d8e637423e32b50b96ff8636e0c0a68d32652c493a4ff3721f50d017b36ead9cf1ac6830fae78981a67a3e509638c308a02b0effd17442eb7690c1a0d") to true,
            Pair("bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69", "581a028d8e637423e32b50b96ff8636e0c0a68d32652c493a4ff3721f50d017b36ead9cf1ac6830fae78981a67a3e509638c308a02b0effd17442eb7690c1a0d") to false,
            Pair("a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0", "f3d7c25a6dd553ee9a558b470e3ebfe607b8e725da946f6984e3a2a9e18050a04fb45dfc27558763a9339e841af21714c9d038b710830975ad92a28fb4b33205") to false,
        )

        val publicKey = ed25519KeyPair.second

        messagesAndSignaturesWithExpected.forEach {
            val message = it.first.first.asHexString().toByteArray()
            val signature = it.first.second.asHexString().toByteArray()
            val expected = it.second

            assertEquals(expected, crypto.verifyEd25519(message, signature, publicKey))
        }
    }

    @Test
    fun `signs HexString message with HexString secp256k1 secret key`() {
        val messagesWithExpected = listOf(
            "bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69" to "c272b5c8f4aba4cbac9a40020639429edb211a3386bf927799ec6324a54239c77583b003b59a7bbc7111230f0530c05d544d570de7bc5ef302dddcbd62281cbe",
            "a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0" to "25984dc84e5c1d33ae85ca11b1da32938f03f6fe4c6a8ae5c3cb95a62d6fe4371773e48413458afd3c1a417078e48155037fa64ebbe14d8c5fa62b692fd7fa9b",
        )

        val secretKey = secp256k1KeyPair.first.toHexString()

        messagesWithExpected.forEach {
            val message = it.first.asHexString()
            val expected = it.second.asHexString().toByteArray()

            assertContentEquals(expected, crypto.signSecp256K1(message, secretKey))
        }
    }

    @Test
    fun `signs ByteArray message with ByteArray secp256k1 secret key`() {
        val messagesWithExpected = listOf(
            "bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69" to "c272b5c8f4aba4cbac9a40020639429edb211a3386bf927799ec6324a54239c77583b003b59a7bbc7111230f0530c05d544d570de7bc5ef302dddcbd62281cbe",
            "a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0" to "25984dc84e5c1d33ae85ca11b1da32938f03f6fe4c6a8ae5c3cb95a62d6fe4371773e48413458afd3c1a417078e48155037fa64ebbe14d8c5fa62b692fd7fa9b",
        )

        val secretKey = secp256k1KeyPair.first

        messagesWithExpected.forEach {
            val message = it.first.asHexString().toByteArray()
            val expected = it.second.asHexString().toByteArray()

            assertContentEquals(expected, crypto.signSecp256K1(message, secretKey))
        }
    }

    @Test
    fun `verifies HexString message and signature with HexString secp256k1 public key`() {
        val messagesAndSignaturesWithExpected = listOf(
            Pair("bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69", "c272b5c8f4aba4cbac9a40020639429edb211a3386bf927799ec6324a54239c77583b003b59a7bbc7111230f0530c05d544d570de7bc5ef302dddcbd62281cbe") to true,
            Pair("a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0", "25984dc84e5c1d33ae85ca11b1da32938f03f6fe4c6a8ae5c3cb95a62d6fe4371773e48413458afd3c1a417078e48155037fa64ebbe14d8c5fa62b692fd7fa9b") to true,
            Pair("bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69", "25984dc84e5c1d33ae85ca11b1da32938f03f6fe4c6a8ae5c3cb95a62d6fe4371773e48413458afd3c1a417078e48155037fa64ebbe14d8c5fa62b692fd7fa9b") to false,
            Pair("a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0", "c272b5c8f4aba4cbac9a40020639429edb211a3386bf927799ec6324a54239c77583b003b59a7bbc7111230f0530c05d544d570de7bc5ef302dddcbd62281cbe") to false,
        )

        val publicKey = secp256k1KeyPair.second.toHexString()

        messagesAndSignaturesWithExpected.forEach {
            val message = it.first.first.asHexString()
            val signature = it.first.second.asHexString()
            val expected = it.second

            assertEquals(expected, crypto.verifySecp256K1(message, signature, publicKey))
        }
    }

    @Test
    fun `verifies ByteArray message and signature with ByteArray secp256k1 public key`() {
        val messagesAndSignaturesWithExpected = listOf(
            Pair("bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69", "c272b5c8f4aba4cbac9a40020639429edb211a3386bf927799ec6324a54239c77583b003b59a7bbc7111230f0530c05d544d570de7bc5ef302dddcbd62281cbe") to true,
            Pair("a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0", "25984dc84e5c1d33ae85ca11b1da32938f03f6fe4c6a8ae5c3cb95a62d6fe4371773e48413458afd3c1a417078e48155037fa64ebbe14d8c5fa62b692fd7fa9b") to true,
            Pair("bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69", "25984dc84e5c1d33ae85ca11b1da32938f03f6fe4c6a8ae5c3cb95a62d6fe4371773e48413458afd3c1a417078e48155037fa64ebbe14d8c5fa62b692fd7fa9b") to false,
            Pair("a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0", "c272b5c8f4aba4cbac9a40020639429edb211a3386bf927799ec6324a54239c77583b003b59a7bbc7111230f0530c05d544d570de7bc5ef302dddcbd62281cbe") to false,
        )

        val publicKey = secp256k1KeyPair.second

        messagesAndSignaturesWithExpected.forEach {
            val message = it.first.first.asHexString().toByteArray()
            val signature = it.first.second.asHexString().toByteArray()
            val expected = it.second

            assertEquals(expected, crypto.verifySecp256K1(message, signature, publicKey))
        }
    }

    @Test
    fun `signs HexString message with HexString P256 secret key`() {
        val messagesWithExpected = listOf(
            "bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69" to "e0fdb186b140de795f73c30ed8c269a2a71ba4c062502c8befe85d38044fd9ec4a3246b6ba7fb06045a2d121e4f6c8e2aa90be01b5a96502e8e53407bbb13bc2",
            "a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0" to "3c1c7c51f8ceddc31e6c81a46b3147b52cfa34bf9639af0b541f7eb94ea9356e729ef2d57c8dbe686a87b55695643d8fa4af50e17fb32808e0e9c94a315a763a",
        )

        val secretKey = p256KeyPair.first.toHexString()

        messagesWithExpected.forEach {
            val message = it.first.asHexString()
            val expected = it.second.asHexString().toByteArray()

            assertContentEquals(expected, crypto.signP256(message, secretKey))
        }
    }

    @Test
    fun `signs ByteArray message with ByteArray P256 secret key`() {
        val messagesWithExpected = listOf(
            "bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69" to "e0fdb186b140de795f73c30ed8c269a2a71ba4c062502c8befe85d38044fd9ec4a3246b6ba7fb06045a2d121e4f6c8e2aa90be01b5a96502e8e53407bbb13bc2",
            "a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0" to "3c1c7c51f8ceddc31e6c81a46b3147b52cfa34bf9639af0b541f7eb94ea9356e729ef2d57c8dbe686a87b55695643d8fa4af50e17fb32808e0e9c94a315a763a",
        )

        val secretKey = p256KeyPair.first

        messagesWithExpected.forEach {
            val message = it.first.asHexString().toByteArray()
            val expected = it.second.asHexString().toByteArray()

            assertContentEquals(expected, crypto.signP256(message, secretKey))
        }
    }

    @Test
    fun `verifies HexString message and signature with HexString P256 public key`() {
        val messagesAndSignaturesWithExpected = listOf(
            Pair("bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69", "e0fdb186b140de795f73c30ed8c269a2a71ba4c062502c8befe85d38044fd9ec4a3246b6ba7fb06045a2d121e4f6c8e2aa90be01b5a96502e8e53407bbb13bc2") to true,
            Pair("a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0", "3c1c7c51f8ceddc31e6c81a46b3147b52cfa34bf9639af0b541f7eb94ea9356e729ef2d57c8dbe686a87b55695643d8fa4af50e17fb32808e0e9c94a315a763a") to true,
            Pair("bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69", "3c1c7c51f8ceddc31e6c81a46b3147b52cfa34bf9639af0b541f7eb94ea9356e729ef2d57c8dbe686a87b55695643d8fa4af50e17fb32808e0e9c94a315a763a") to false,
            Pair("a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0", "e0fdb186b140de795f73c30ed8c269a2a71ba4c062502c8befe85d38044fd9ec4a3246b6ba7fb06045a2d121e4f6c8e2aa90be01b5a96502e8e53407bbb13bc2") to false,
        )

        val publicKey = p256KeyPair.second.toHexString()

        messagesAndSignaturesWithExpected.forEach {
            val message = it.first.first.asHexString()
            val signature = it.first.second.asHexString()
            val expected = it.second

            assertEquals(expected, crypto.verifyP256(message, signature, publicKey))
        }
    }

    @Test
    fun `verifies ByteArray message and signature with ByteArray P256 public key`() {
        val messagesAndSignaturesWithExpected = listOf(
            Pair("bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69", "e0fdb186b140de795f73c30ed8c269a2a71ba4c062502c8befe85d38044fd9ec4a3246b6ba7fb06045a2d121e4f6c8e2aa90be01b5a96502e8e53407bbb13bc2") to true,
            Pair("a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0", "3c1c7c51f8ceddc31e6c81a46b3147b52cfa34bf9639af0b541f7eb94ea9356e729ef2d57c8dbe686a87b55695643d8fa4af50e17fb32808e0e9c94a315a763a") to true,
            Pair("bb67a3ba9ac64fb89ab480f634755f0d92c263f980b8705dbb24b3010a3b1e69", "3c1c7c51f8ceddc31e6c81a46b3147b52cfa34bf9639af0b541f7eb94ea9356e729ef2d57c8dbe686a87b55695643d8fa4af50e17fb32808e0e9c94a315a763a") to false,
            Pair("a25f6cf295585d7f4802ed61cb4df44d4af5dc11c4ae86f61a2cab8fdcdbffc0", "e0fdb186b140de795f73c30ed8c269a2a71ba4c062502c8befe85d38044fd9ec4a3246b6ba7fb06045a2d121e4f6c8e2aa90be01b5a96502e8e53407bbb13bc2") to false,
        )

        val publicKey = p256KeyPair.second

        messagesAndSignaturesWithExpected.forEach {
            val message = it.first.first.asHexString().toByteArray()
            val signature = it.first.second.asHexString().toByteArray()
            val expected = it.second

            assertEquals(expected, crypto.verifyP256(message, signature, publicKey))
        }
    }

    private val ed25519KeyPair: Pair<ByteArray, ByteArray>
        get() = Pair(
            "8a56c92b7df4841ea1a79b2daa478b9a629ff359e00fd344e0c5fbd139e9114f208c1d0dec92b417baf72bed39357fa7062c59336a5981fa239c1f426841ea83".asHexString().toByteArray(),
            "208c1d0dec92b417baf72bed39357fa7062c59336a5981fa239c1f426841ea83".asHexString().toByteArray()
        )

    private val secp256k1KeyPair: Pair<ByteArray, ByteArray>
        get() = Pair(
            "02fb68cbc677ffb43368a610f2d5782a159ae2783aad34a8f353b94d63738c58".asHexString().toByteArray(),
            "02434e3529cd6a192e865d28d60cf516adb58bb074b9c17dfd3c45d9c697b83333".asHexString().toByteArray()
        )

    private val p256KeyPair: Pair<ByteArray, ByteArray>
        get() = Pair(
            "4372f787773fb0669c4ba453768573c61e20ad09616043df3cf16ab7e6bfb94b".asHexString().toByteArray(),
            "0397e0c76ca850349cfb7684121c5fc7516f7ff3300bf047631cc8e6b155b56758".asHexString().toByteArray()
        )
}