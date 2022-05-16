package it.airgap.tezos.core.internal.converter.encoded

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.converter.encoded.fromString
import it.airgap.tezos.core.internal.TezosCore
import it.airgap.tezos.core.internal.di.ScopedDependencyRegistry
import it.airgap.tezos.core.type.encoded.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StringToSignatureConverterTest {

    @MockK
    private lateinit var dependencyRegistry: ScopedDependencyRegistry

    private lateinit var stringToSignatureConverter: StringToSignatureConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        stringToSignatureConverter = StringToSignatureConverter()

        every { dependencyRegistry.stringToSignatureConverter } returns stringToSignatureConverter
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert string to SignatureEncoded`() {
        signaturesWithStrings.forEach {
            assertEquals(it.first, stringToSignatureConverter.convert(it.second))
            assertEquals(it.first, Signature.fromString(it.second, TezosCore(dependencyRegistry)))
            assertEquals(it.first, Signature.fromString(it.second, stringToSignatureConverter))
        }
    }

    @Test
    fun `should fail to convert invalid string to SignatureEncoded`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { stringToSignatureConverter.convert(it) }
            assertFailsWith<IllegalArgumentException> { Signature.fromString(it, TezosCore(dependencyRegistry)) }
            assertFailsWith<IllegalArgumentException> { Signature.fromString(it, stringToSignatureConverter) }
        }
    }

    private val signaturesWithStrings: List<Pair<Signature, String>>
        get() = listOf(
            Ed25519Signature("edsigu69u68mAZ4KhBDPWshZVCDhSgrzkc5Mgihxakzh7FceNgwdQqQYQM3pjN3b4gjrLVo5Dxre41mzCDt8JFtFytWLLnMmHfz") to "edsigu69u68mAZ4KhBDPWshZVCDhSgrzkc5Mgihxakzh7FceNgwdQqQYQM3pjN3b4gjrLVo5Dxre41mzCDt8JFtFytWLLnMmHfz",
            Secp256K1Signature("spsig1UdNgjqzaZxkbNyqVo3wm4Stoc4RXf6nybVGzANnKNHxTYBY8XGB2pgiVp5BYaYkLkmyaxMCq5xCsqCMDaFQWCeFBG2NNM") to "spsig1UdNgjqzaZxkbNyqVo3wm4Stoc4RXf6nybVGzANnKNHxTYBY8XGB2pgiVp5BYaYkLkmyaxMCq5xCsqCMDaFQWCeFBG2NNM",
            P256Signature("p2sign2yJ9dujTvBpkFXAeY8SWEAmtE2fCdVqcPvonSKp7phjDxX9QzENKXJxFZ9ZiEXWtcF13CuEmG8ZihkXiSUPtZVHniizN") to "p2sign2yJ9dujTvBpkFXAeY8SWEAmtE2fCdVqcPvonSKp7phjDxX9QzENKXJxFZ9ZiEXWtcF13CuEmG8ZihkXiSUPtZVHniizN",
            GenericSignature("sigorP5a3d9pUue323PENRLsrLzEMwTJfKudbjCTUBG7qAuELEt8nzm8FLC2kRwWfuUfFXfKu2PpfJPsT6RZcDA2AWnLTQQ9") to "sigorP5a3d9pUue323PENRLsrLzEMwTJfKudbjCTUBG7qAuELEt8nzm8FLC2kRwWfuUfFXfKu2PpfJPsT6RZcDA2AWnLTQQ9",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "00",
            "edsigu69u68mAZ4KhBDPWshZVCDhSgrzkc5Mgihxakzh7FceNgwdQqQYQM3pjN3b4gjrLVo5Dxre41mzCDt8JFtFy",
            "UdNgjqzaZxkbNyqVo3wm4Stoc4RXf6nybVGzANnKNHxTYBY8XGB2pgiVp5BYaYkLkmyaxMCq5xCsqCMDaFQWCeFBG2NNM",
            "p2n2yJ9dujTvBpkFXAeY8SWEAmtE2fCdVqcPvonSKp7phjDxX9QzENKXJxFZ9ZiEXWtcF13CuEmG8ZihkXiSUPtZVHniizN",
            "sigorP5a3d9pUue323PENRLsrLzEMwTJfKudbjCTUBG7qAuELEt8nzm8FLC2kRwWfuUfFXfKu2PpfJPsT6RZcDA2AWnLTQQ9TuBz",
        )
}