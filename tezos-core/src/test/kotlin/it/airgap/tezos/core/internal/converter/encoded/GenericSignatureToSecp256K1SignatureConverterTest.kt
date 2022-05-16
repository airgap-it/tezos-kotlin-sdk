package it.airgap.tezos.core.internal.converter.encoded

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.fromGenericSignature
import it.airgap.tezos.core.crypto.CryptoProvider
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.Secp256K1Signature
import it.airgap.tezos.core.type.encoded.Signature
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertEquals

class GenericSignatureToSecp256K1SignatureConverterTest {

    @MockK
    private lateinit var cryptoProvider: CryptoProvider

    private lateinit var tezos: Tezos

    private lateinit var genericSignatureToSecp256K1SignatureConverter: GenericSignatureToSecp256K1SignatureConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { cryptoProvider.sha256(any()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        tezos = mockTezos(cryptoProvider)
        genericSignatureToSecp256K1SignatureConverter = GenericSignatureToSecp256K1SignatureConverter(
            tezos.core().dependencyRegistry.signatureBytesCoder,
            tezos.core().dependencyRegistry.encodedBytesCoder,
        )
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert GenericSignature to Secp256K1Signature`() {
        signaturesWithGenerics.forEach {
            assertEquals(it.first, genericSignatureToSecp256K1SignatureConverter.convert(it.second))
            assertEquals(it.first, Secp256K1Signature.fromGenericSignature(it.second, tezos))
            assertEquals(it.first, Secp256K1Signature.fromGenericSignature(it.second, genericSignatureToSecp256K1SignatureConverter))
        }
    }

    private val signaturesWithGenerics: List<Pair<Signature, GenericSignature>>
        get() = listOf(
            Secp256K1Signature("spsig1PptyKUAGumWN9qs9aW2MafGp8kXekDAzEPCpoJUUPjVzQwmKdNBti5CA3nMTVcUaM3dcS2JSQwUNGtbYvHbSeU5eTTK6Z") to GenericSignature("sigg1DeFruoZoMyyr7BRwshfytuJxxagaUWfFt419AfVonZMHx94HowabLTgDGQ6YcVdJUsUAg1GnkGzBV33c6XRvyAQ3tby"),
        )
}