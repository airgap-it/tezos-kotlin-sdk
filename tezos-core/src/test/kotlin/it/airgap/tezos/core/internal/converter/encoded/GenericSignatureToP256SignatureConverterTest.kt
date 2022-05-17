package it.airgap.tezos.core.internal.converter.encoded

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.fromGenericSignature
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.P256Signature
import it.airgap.tezos.core.type.encoded.Signature
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GenericSignatureToP256SignatureConverterTest {

    private lateinit var tezos: Tezos
    private lateinit var genericSignatureToP256SignatureConverter: GenericSignatureToP256SignatureConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        genericSignatureToP256SignatureConverter = GenericSignatureToP256SignatureConverter(
            tezos.core().dependencyRegistry.signatureBytesCoder,
            tezos.core().dependencyRegistry.encodedBytesCoder,
        )
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert GenericSignature to P256Signature`() {
        signaturesWithGenerics.forEach {
            assertEquals(it.first, genericSignatureToP256SignatureConverter.convert(it.second))
            assertEquals(it.first, P256Signature.fromGenericSignature(it.second, tezos))
            assertEquals(it.first, P256Signature.fromGenericSignature(it.second, genericSignatureToP256SignatureConverter))
        }
    }

    private val signaturesWithGenerics: List<Pair<Signature, GenericSignature>>
        get() = listOf(
            P256Signature("p2sigr37wjbaL4XCywZcB3YXdGf7Jz8ibYkTK6VjHXQrPyV98w5Y6S3XJMfV7fdrMfvwqwy2Pryf3eHztYaxCAXUivBupwUEDK") to GenericSignature("sigriu6eW2EbbAX1JqvKwzqmCKvbUVLTL36eisu7xR6m2rbPokn3zS55V4pUhL8EjNXKwWNnjii9LgX9u2Ta5HHeVZvj6kjP"),
        )
}