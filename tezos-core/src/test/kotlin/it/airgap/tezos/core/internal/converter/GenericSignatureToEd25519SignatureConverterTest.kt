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
import it.airgap.tezos.core.type.encoded.Ed25519Signature
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.SignatureEncoded
import mockTezosSdk
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertEquals

class GenericSignatureToEd25519SignatureConverterTest {

    @MockK
    private lateinit var crypto: Crypto

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var genericSignatureToEd25519SignatureConverter: GenericSignatureToEd25519SignatureConverter

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

        genericSignatureToEd25519SignatureConverter = GenericSignatureToEd25519SignatureConverter(signatureBytesCoder, encodedBytesCoder)

        every { dependencyRegistry.genericSignatureToEd25519SignatureConverter } returns genericSignatureToEd25519SignatureConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert GenericSignature to Ed25519Signature`() {
        signaturesWithGenerics.forEach {
            assertEquals(it.first, genericSignatureToEd25519SignatureConverter.convert(it.second))
            assertEquals(it.first, Ed25519Signature.fromGenericSignature(it.second))
            assertEquals(it.first, Ed25519Signature.fromGenericSignature(it.second, genericSignatureToEd25519SignatureConverter))
        }
    }

    private val signaturesWithGenerics: List<Pair<SignatureEncoded<*>, GenericSignature>>
        get() = listOf(
            Ed25519Signature("edsigtczTq2EC9VQNRRT53gzcs25DJFg1iZeTzQxY7jBtjradZb8qqZaqzAYSbVWvg1abvqFpQCV8TgqotDwckJiTJ9fJ2eYESb") to GenericSignature("sigTAzhy1HsZDLNETmuf9RuinhXRb5jvmscjCoPPBujWZgFmCFLffku7JXYtu8aYQFVHnCUghmd4t39RuR6ANV76bCCYTR9u"),
        )
}