package it.airgap.tezos.core.internal.converter.encoded

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.fromGenericSignature
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Ed25519Signature
import it.airgap.tezos.core.type.encoded.GenericSignature
import it.airgap.tezos.core.type.encoded.Signature
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GenericSignatureToEd25519SignatureConverterTest {

    private lateinit var tezos: Tezos
    private lateinit var genericSignatureToEd25519SignatureConverter: GenericSignatureToEd25519SignatureConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        genericSignatureToEd25519SignatureConverter = GenericSignatureToEd25519SignatureConverter(
            tezos.coreModule.dependencyRegistry.signatureBytesCoder,
            tezos.coreModule.dependencyRegistry.encodedBytesCoder,
        )
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert GenericSignature to Ed25519Signature`() = withTezosContext {
        signaturesWithGenerics.forEach {
            assertEquals(it.first, genericSignatureToEd25519SignatureConverter.convert(it.second))
            assertEquals(it.first, Ed25519Signature.fromGenericSignature(it.second, tezos))
            assertEquals(it.first, Ed25519Signature.fromGenericSignature(it.second, genericSignatureToEd25519SignatureConverter))
        }
    }

    private val signaturesWithGenerics: List<Pair<Signature, GenericSignature>>
        get() = listOf(
            Ed25519Signature("edsigtczTq2EC9VQNRRT53gzcs25DJFg1iZeTzQxY7jBtjradZb8qqZaqzAYSbVWvg1abvqFpQCV8TgqotDwckJiTJ9fJ2eYESb") to GenericSignature("sigTAzhy1HsZDLNETmuf9RuinhXRb5jvmscjCoPPBujWZgFmCFLffku7JXYtu8aYQFVHnCUghmd4t39RuR6ANV76bCCYTR9u"),
        )
}