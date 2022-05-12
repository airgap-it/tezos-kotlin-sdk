package it.airgap.tezos.core.internal.converter

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.fromGenericSignature
import it.airgap.tezos.core.internal.base58.Base58
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.coder.SignatureBytesCoder
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.internal.di.ScopedDependencyRegistry
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.Secp256K1Signature
import it.airgap.tezos.core.type.encoded.Signature
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertEquals

class GenericSignatureToSecp256K1SignatureConverterTest {

    @MockK
    private lateinit var crypto: Crypto

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var genericSignatureToSecp256K1SignatureConverter: GenericSignatureToSecp256K1SignatureConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockTezosSdk(dependencyRegistry)

        every { crypto.hashSha256(any<ByteArray>()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        val base58Check = Base58Check(Base58(), crypto)
        val encodedBytesCoder = EncodedBytesCoder(base58Check)
        val signatureBytesCoder = SignatureBytesCoder(encodedBytesCoder)

        genericSignatureToSecp256K1SignatureConverter = GenericSignatureToSecp256K1SignatureConverter(signatureBytesCoder, encodedBytesCoder)

        every { dependencyRegistry.genericSignatureToSecp256K1SignatureConverter } returns genericSignatureToSecp256K1SignatureConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert GenericSignature to Secp256K1Signature`() {
        signaturesWithGenerics.forEach {
            assertEquals(it.first, genericSignatureToSecp256K1SignatureConverter.convert(it.second))
            assertEquals(it.first, Secp256K1Signature.fromGenericSignature(it.second))
            assertEquals(it.first, Secp256K1Signature.fromGenericSignature(it.second, genericSignatureToSecp256K1SignatureConverter))
        }
    }

    private val signaturesWithGenerics: List<Pair<Signature, GenericSignature>>
        get() = listOf(
            Secp256K1Signature("spsig1PptyKUAGumWN9qs9aW2MafGp8kXekDAzEPCpoJUUPjVzQwmKdNBti5CA3nMTVcUaM3dcS2JSQwUNGtbYvHbSeU5eTTK6Z") to GenericSignature("sigg1DeFruoZoMyyr7BRwshfytuJxxagaUWfFt419AfVonZMHx94HowabLTgDGQ6YcVdJUsUAg1GnkGzBV33c6XRvyAQ3tby"),
        )
}