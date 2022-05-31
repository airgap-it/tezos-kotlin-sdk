package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import it.airgap.tezos.core.internal.context.TezosCoreContext.toHexString
import org.junit.Test
import kotlin.test.*

class P256EncryptedSecretKeyTest {

    @Test
    fun `should recognize valid and invalid P256EncryptedSecretKey strings`() {
        validStrings.forEach {
            assertTrue(P256EncryptedSecretKey.isValid(it), "Expected `$it` to be recognized as valid P256EncryptedSecretKey string.")
        }

        invalidStrings.forEach {
            assertFalse(P256EncryptedSecretKey.isValid(it), "Expected `$it` to be recognized as invalid P256EncryptedSecretKey string.")
        }
    }

    @Test
    fun `should recognize valid and invalid P256EncryptedSecretKey bytes`() {
        validBytes.forEach {
            assertTrue(P256EncryptedSecretKey.isValid(it), "Expected `$it` to be recognized as valid P256EncryptedSecretKey.")
            assertTrue(P256EncryptedSecretKey.isValid(it.toList()), "Expected `$it` to be recognized as valid P256EncryptedSecretKey.")
        }

        invalidBytes.forEach {
            assertFalse(P256EncryptedSecretKey.isValid(it), "Expected `${it.toHexString().asString()}` to be recognized as invalid P256EncryptedSecretKey.")
            assertFalse(P256EncryptedSecretKey.isValid(it.toList()), "Expected `${it.toHexString().asString()}` to be recognized as invalid P256EncryptedSecretKey.")
        }
    }

    @Test
    fun `should create P256EncryptedSecretKey from valid string`() {
        validStrings.forEach {
            assertEquals(it, P256EncryptedSecretKey.createValue(it).base58)
            assertEquals(it, P256EncryptedSecretKey.createValueOrNull(it)?.base58)
        }
    }

    @Test
    fun `fails to create P256EncryptedSecretKey from invalid string`() {
        invalidStrings.forEach {
            assertFailsWith<IllegalArgumentException> { P256EncryptedSecretKey.createValue(it) }
            assertNull(P256EncryptedSecretKey.createValueOrNull(it))
        }
    }

    private val validStrings: List<String>
        get() = listOf(
            "p2esk2ApeXs9hu6wa1ES2n2qsUqNx8KKXsSUDcPvCFWYLW6d91W2UFS899zeUquJXrA415LkG4Qrnk7oruRTXzDr",
            "p2esk2HEGMsKhhThxg4MzXNSER2NCoUKqBgv4tQtpCNbS97s3CQyK3FsEm9nYJBocF25X7PQ5CuxmwJLAdQAZsyQ",
            "p2esk2MCiovTXp53iHuMyAmsma7w4kyr19Q8ycXC1z8TcXztkVMXbTGdsi4Roo7qAqDuXWPfbgGwMbpdAd4svVw6",
            "p2esk1twr7pKKa97C5q4pAjVGKDNcAfiLPsBbSjZujtDY3J9MAQxrCrKjsjB6KZr12nKz3GZnYePPZDVtXYXWbBb",
            "p2esk2A9oU4hNDbVZHovBCzyvVG1kAd5hfz6gXJm2WdyMq1TpCLbAyUk168KmUs1KifEcZ9Byb5tZAw7XxWbeRb1",
            "p2esk1vFjhs6mac9kmSk58Lf3keHPZHqEh76LLU4m1FRpYP93q7ScHoP8UGLaC7Bt4nJuqYzgGkfKSMZGSR6TxVn",
        )

    private val invalidStrings: List<String>
        get() = listOf(
            "",
            "abc",
            "2ApeXs9hu6wa1ES2n2qsUqNx8KKXsSUDcPvCFWYLW6d91W2UFS899zeUquJXrA415LkG4Qrnk7oruRTXzDr",
            "p2esk2HEGMsKhhThxg4MzXNSER2NCoUKqBgv4tQtpCNbS97s3CQyK3FsEm9nYJBocF25X7PQ5CuxmwJLAdQA",
            "p2esk2MCiovTXp53iHuMyAmsma7w4kyr19Q8ycXC1z8TcXztkVMXbTGdsi4Roo7qAqDuXWPfbgGwMbpdAd4svVw6sk1t",
        )

    private val validBytes: List<ByteArray>
        get() = listOf(
            "887298dc1e27afa806fa8a59c33f17071e0099f865db7615196eb327b26533d126aa2934dfeaaba91398bd94a3ca62155aa930dfc5368b32",
            "09303973ab887298dc1e27afa806fa8a59c33f17071e0099f865db7615196eb327b26533d126aa2934dfeaaba91398bd94a3ca62155aa930dfc5368b32",
            "ac9a7b3204d5b20015cc88f6a0ae7548b470da5834488b814b42bc6112aebe4f635f7fde8dec9dcbe3fc3dc3d8c19170b1d73dfb375a89d6",
            "09303973abac9a7b3204d5b20015cc88f6a0ae7548b470da5834488b814b42bc6112aebe4f635f7fde8dec9dcbe3fc3dc3d8c19170b1d73dfb375a89d6",
            "c306734a1d56a5d9cc0a78b20ee19cd0160322dc62f5209e2865a35201e5b97326ae2a249c0af56f5c6cca5b5c299bdcfadc137cae595550",
            "09303973abc306734a1d56a5d9cc0a78b20ee19cd0160322dc62f5209e2865a35201e5b97326ae2a249c0af56f5c6cca5b5c299bdcfadc137cae595550",
            "2edc38a09314ce83111a0d447210e5a0aa3e2dc8078a86a3602898f9ae628206dfcdfbeb4ad34c573e8e8c8ef3709a72144eae11a4574cb0",
            "09303973ab2edc38a09314ce83111a0d447210e5a0aa3e2dc8078a86a3602898f9ae628206dfcdfbeb4ad34c573e8e8c8ef3709a72144eae11a4574cb0",
            "84ab0f887a966038fb893f73358360ff1b210dbc769a9a5dc8f3cd54d74ac2ca48b7a6360941eede92539474fef86c24c03a83af9ca422e6",
            "09303973ab84ab0f887a966038fb893f73358360ff1b210dbc769a9a5dc8f3cd54d74ac2ca48b7a6360941eede92539474fef86c24c03a83af9ca422e6",
            "363e664c88adc5bdee6a8d706e8a7871d0a88c2fdc8af2f53e838a0d66cf0a038d70bc11a640acadb40ca63a7696f2d1a68b87d1d434e224",
            "09303973ab363e664c88adc5bdee6a8d706e8a7871d0a88c2fdc8af2f53e838a0d66cf0a038d70bc11a640acadb40ca63a7696f2d1a68b87d1d434e224",
        ).map { it.asHexString().toByteArray() }

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "",
            "00",
            "887298dc1e27afa806fa8a59c33f17071e0099f865db7615196eb327b26533d126aa2934dfeaaba91398bd94a3ca62155aa9",
            "ac9a7b3204d5b20015cc88f6a0ae7548b470da5834488b814b42bc6112aebe4f635f7fde8dec9dcbe3fc3dc3d8c19170b1d73dfb375a89d69a7b",
            "19303973ab363e664c88adc5bdee6a8d706e8a7871d0a88c2fdc8af2f53e838a0d66cf0a038d70bc11a640acadb40ca63a7696f2d1a68b87d1d434e224",
        ).map { it.asHexString().toByteArray() }
}
