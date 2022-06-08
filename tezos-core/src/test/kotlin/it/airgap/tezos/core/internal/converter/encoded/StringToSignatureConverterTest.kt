package it.airgap.tezos.core.internal.converter.encoded

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.converter.encoded.Signature
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.type.encoded.*
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StringToSignatureConverterTest {

    private lateinit var tezos: Tezos
    private lateinit var stringToSignatureConverter: StringToSignatureConverter

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        stringToSignatureConverter = StringToSignatureConverter()
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should convert string to SignatureEncoded`() = withTezosContext {
        signaturesWithStrings.forEach {
            assertEquals(it.first, stringToSignatureConverter.convert(it.second))
            assertEquals(it.first, Signature(it.second, tezos))
            assertEquals(it.first, Signature.fromString(it.second, stringToSignatureConverter))
        }
    }

    @Test
    fun `should fail to convert invalid string to SignatureEncoded`() = withTezosContext {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { stringToSignatureConverter.convert(it) }
            assertFailsWith<IllegalArgumentException> { Signature(it, tezos) }
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