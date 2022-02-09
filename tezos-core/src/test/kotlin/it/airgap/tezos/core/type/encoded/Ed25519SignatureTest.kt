package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.toHexString
import org.junit.Test
import kotlin.test.*

class Ed25519SignatureTest {

    @Test
    fun `should recognize valid and invalid Ed25519Signature strings`() {
        validStrings.forEach {
            assertTrue(Ed25519Signature.isValid(it), "Expected `$it` to be recognized as valid Ed25519Signature string.")
        }

        invalidStrings.forEach {
            assertFalse(Ed25519Signature.isValid(it), "Expected `$it` to be recognized as invalid Ed25519Signature string.")
        }
    }

    @Test
    fun `should recognize valid and invalid Ed25519Signature bytes`() {
        validBytes.forEach {
            assertTrue(Ed25519Signature.isValid(it), "Expected `$it` to be recognized as valid Ed25519Signature.")
            assertTrue(Ed25519Signature.isValid(it.toList()), "Expected `$it` to be recognized as valid Ed25519Signature.")
        }

        invalidBytes.forEach {
            assertFalse(Ed25519Signature.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid Ed25519Signature.")
            assertFalse(Ed25519Signature.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid Ed25519Signature.")
        }
    }

    @Test
    fun `should create Ed25519Signature from valid string`() {
        validStrings.forEach {
            assertEquals(it, Ed25519Signature.createValue(it).base58)
            assertEquals(it, Ed25519Signature.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create Ed25519Signature from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { Ed25519Signature.createValue(it) }
            assertNull(Ed25519Signature.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "edsigtcj68KJNNFUBUJLoY9EVx6oAzU47Cq94c7j2jHormN5EZHJ5v6ssT8hvs197ecMyt9Zr4pyF25yngDPvTUsDHooQkqB1hG",
            "edsigtssqEJvafMQrKdB8rehGVp73Cdng8VCeMeyJ6XDuwhgnfunwmRNYo2WXn2UDebhsWCWo4c4EdFBBNYz5YHkEuwtMZZRCaW",
            "edsigu6QnfFxMtLKfzkLXyH31v2fHS1rzHiv9L14mysgnMkSxVPiyZEtNoCnrPGwS52dzZFtxjFWSnLUET5aWuvqhYs8pNojebQ",
            "edsigtbaPSCUsDdwheRughW4H8MLrFcxD2Tdm2XQtsqm32dGRUfsbCazosP6E4zYSWvw7CUHBDX7x2U9xxyrA1TuibQXF72TGVH",
            "edsigtv334Uku9Mcbkr1gpWhXfC1mxewYMFc6kv5RqfQQLAiyP3xDJ14zYBNFnVo97cHYfmoNPwXSDyyLvZAM1wcmGwDgbQndLj",
            "edsigu15kqnbWKUEQFFNgevEMYmSCruTTeyjW4ajfLzcodGEM3kk55QxV42N8u7BCWCAVHjsfqMnbLaz91VDPjUhXiDReqSTLks",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "tcj68KJNNFUBUJLoY9EVx6oAzU47Cq94c7j2jHormN5EZHJ5v6ssT8hvs197ecMyt9Zr4pyF25yngDPvTUsDHooQkqB1hG",
            "edsigtssqEJvafMQrKdB8rehGVp73Cdng8VCeMeyJ6XDuwhgnfunwmRNYo2WXn2UDebhsWCWo4c4EdFBBNYz5YHkEuwtMZZ",
            "edsigu6QnfFxMtLKfzkLXyH31v2fHS1rzHiv9L14mysgnMkSxVPiyZEtNoCnrPGwS52dzZFtxjFWSnLUET5aWuvqhYs8pNojebQigtb",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "259a2373fc3b48309aa9046eb1734b5fb97115173c05d088fafb98c66f7bcc5c7c70bfb853323c7c71dcace6cb9cc7b7264de391b0d188ccf8e98ad3c52d40b1",
            "09f5cd8612259a2373fc3b48309aa9046eb1734b5fb97115173c05d088fafb98c66f7bcc5c7c70bfb853323c7c71dcace6cb9cc7b7264de391b0d188ccf8e98ad3c52d40b1",
            "996884920eb3f81807c8318c5a9eb61c5e22adbdbed563b35d18ac402b90ee1005970f904bb373f28e448f162d91c564546c23432343f116e3481b0bf1160e48",
            "09f5cd8612996884920eb3f81807c8318c5a9eb61c5e22adbdbed563b35d18ac402b90ee1005970f904bb373f28e448f162d91c564546c23432343f116e3481b0bf1160e48",
            "f9360151c5191ff113d7755f2903ba1445d7d90c447e898b4e230b56e34155cc348295d4d03c55396af4748c11f85945265f04462c66ac53d8ac584436ce313b",
            "09f5cd8612f9360151c5191ff113d7755f2903ba1445d7d90c447e898b4e230b56e34155cc348295d4d03c55396af4748c11f85945265f04462c66ac53d8ac584436ce313b",
            "1ccfcf7a32394792efb8a60d2fe28f6709d75547851d33ce4927608a0dc91e61c857e9950f6fbf70468c2f10e1559274b9c74e93d35d9aa46750f491b355be5a",
            "09f5cd86121ccfcf7a32394792efb8a60d2fe28f6709d75547851d33ce4927608a0dc91e61c857e9950f6fbf70468c2f10e1559274b9c74e93d35d9aa46750f491b355be5a",
            "a9e88e4d9b330bf59ab554222e23aedee252381c0e14716318919a1af2aae5ad4e6a8aaa8ffcad64bf1361943f7acc354d7dda1037dee5c28ccc5b3d77914f46",
            "09f5cd8612a9e88e4d9b330bf59ab554222e23aedee252381c0e14716318919a1af2aae5ad4e6a8aaa8ffcad64bf1361943f7acc354d7dda1037dee5c28ccc5b3d77914f46",
            "d07c22f541a4955e32c470a616053d0f1a2ce2c8d1debdb2507fdd0e8aa6688bc40ec6a537587ef5d33f04be489925af3c93c53e1e12d09b859992c1a026ef3a",
            "09f5cd8612d07c22f541a4955e32c470a616053d0f1a2ce2c8d1debdb2507fdd0e8aa6688bc40ec6a537587ef5d33f04be489925af3c93c53e1e12d09b859992c1a026ef3a",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "259a2373fc3b48309aa9046eb1734b5fb97115173c05d088fafb98c66f7bcc5c7c70bfb853323c7c71dcace6cb9cc7b7264de391b0d188ccf8e9",
            "996884920eb3f81807c8318c5a9eb61c5e22adbdbed563b35d18ac402b90ee1005970f904bb373f28e448f162d91c564546c23432343f116e3481b0bf1160e486884",
            "19f5cd8612d07c22f541a4955e32c470a616053d0f1a2ce2c8d1debdb2507fdd0e8aa6688bc40ec6a537587ef5d33f04be489925af3c93c53e1e12d09b859992c1a026ef3a",
        ).map { it.asHexString().toByteArray() }
}