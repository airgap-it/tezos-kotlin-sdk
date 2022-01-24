package it.airgap.tezos.core.internal.coder

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.base58.Base58
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.internal.utils.asHexString
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Base58BytesCoderTest {
    @MockK
    private lateinit var crypto: Crypto

    private lateinit var base58BytesCoder: Base58BytesCoder

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { crypto.hashSha256(any<ByteArray>()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        val base58Check = Base58Check(Base58(), crypto)
        base58BytesCoder = Base58BytesCoder(base58Check)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `encodes String`() {
        val expectedWithActual = (
                bytesWithBase58 + bytesWithBase58AndPrefix.map { it.first to it.second.first }
        ).map { it.first.asHexString().toByteArray() to it.second }

        expectedWithActual.forEach {
            assertContentEquals(it.first, base58BytesCoder.encode(it.second))
            assertContentEquals(it.first, base58BytesCoder.encode(it.second, prefix = null))
        }
    }

    @Test
    fun `encodes String with specified prefix`() {
        val expectedWithActual = bytesWithBase58AndPrefix.map { it.first.asHexString().toByteArray() to it.second }

        expectedWithActual.forEach {
            assertContentEquals(it.first, base58BytesCoder.encode(it.second.first, it.second.second))
        }
    }

    @Test
    fun `fails to encode String with specified prefix if value is invalid`() {
        val expectedWithActual = bytesWithBase58AndPrefix.map { it.second.first.substring(1) to it.second.second }

        expectedWithActual.forEach {
            assertFailsWith<IllegalArgumentException> {
                base58BytesCoder.encode(it.first, it.second)
            }
        }
    }

    @Test
    fun `decodes ByteArray`() {
        val expectedWithActual = bytesWithBase58.map { it.second to it.first.asHexString().toByteArray() }

        expectedWithActual.forEach {
            assertEquals(it.first, base58BytesCoder.decode(it.second))
            assertEquals(it.first, base58BytesCoder.decode(it.second, prefix = null))
        }
    }

    @Test
    fun `decodes ByteArray with specified prefix`() {
        val expectedWithActual = bytesWithBase58AndPrefix.map { it.second to it.first.asHexString().toByteArray() }

        expectedWithActual.forEach {
            assertEquals(it.first.first, base58BytesCoder.decode(it.second, it.first.second))
        }
    }

    @Test
    fun `fails to decode ByteArray with specified prefix if value is invalid`() {
        val expectedWithActual = bytesWithBase58AndPrefix.map { it.first.substring(2).asHexString().toByteArray() to it.second.second }

        expectedWithActual.forEach {
            assertFailsWith<IllegalArgumentException> {
                base58BytesCoder.decode(it.first, it.second)
            }
        }
    }

    private val bytesWithBase58: List<Pair<String, String>>
        get() = listOf(
            "00" to "1Wh4bh",
            "9434dc98" to "Rnnju5zumgo",
            "1b25632c" to "5YMLYNMq9ZC",
            "511e22c1" to "EZwirJx8Yjz",
            "fb1e19ce" to "j19zm4WDXKc",
            "260bf0ed" to "7N6onopA64Y",
            "2317951c" to "6sSNnuYWSuh",
            "07797720fbc2" to "RMeHkvFFSKtD6",
            "baf281bc0c9f" to "BWBDpE67R1vcie",
            "9f4ef22b48c8" to "9x7b6fEpiurKjL",
            "0063f15efd" to "1Hia7VA7ut1T",
            "000008d64e1eaa" to "117XQCFEaGaFjD",
            "0000000b1e92e6ac" to "1119D8LFdoz3aG5",
        )

    private val bytesWithBase58AndPrefix: List<Pair<String, Pair<String, Tezos.Prefix>>>
        get() = listOf(
            "a4712e4241cd45194876e5e3637afd5eb0de95d43909ee8a0300004bb54697f4" to Pair("BLxhnhJ8yRu5BjcK8STwLtDmSpWz2q5R5HRYir8kdexFjMVZiZ5", Tezos.Prefix.BlockHash),
            "9da5593331db4e8f496ae7588cb76404a16e77be46a0cfbd0a59749771613b9d" to Pair("ooqe8AAhnrPu9M9CZeDCjg6EDww3Y5zQGUvsujEN7dikvvprsXz", Tezos.Prefix.OperationHash),
            "bda74d5a706ca60449fadafcc5e2689576924e3c7aa2cf8df58ec2adc8830da1" to Pair("LoxE21kHxDzUHqS3ZL16UK7DCCuxykB3F8qoVqUBVQmZbHnYxSVs", Tezos.Prefix.OperationListHash),
            "340f6f139868a69cdf189072acb485ecb710e5dca94c79df6a7f0facd5e1b398" to Pair("LLoZidphsx8dm8Eg8H2yAw8NZ4TTGU7jLd3DzpGPXXqdgrGaiux2u", Tezos.Prefix.OperationListListHash),
            "ace3a899b148831c682105949cdea6cb4c033b3fc152883c7091ef8391dea87a" to Pair("Pt2qYBgju8RgUx1gXu1VFAi3FjzCdgMkdkLRwm77gdwAbxbaWpk", Tezos.Prefix.ProtocolHash),
            "80cd3c83957a801c336209c2418b476906fab82de4cd199252b2d5693146c166" to Pair("CoVd2vUg9onub8w2ooJR7PyinX7sfxYk8YWBzg77yvY2T64E4eKM", Tezos.Prefix.ContextHash),
            "64aa9d9b3acf43f64a1c975b6298cf47f5aa557c86bd8a07645f49f486f64398" to Pair("bm3LV5fYgzx7P7V9E2nuJeGKzeonnu1FbRB2Bk5SMwvWmzUcQZpc", Tezos.Prefix.BlockMetadataHash),
            "f946c89e94f8096064f56c12b06625b95fee240f39b895b8436b48ce37dce51f" to Pair("r4msXPFKwuDaQzGDz2pD3kSAjALi7LuNambGqMkxgtDuZmbaFVQ", Tezos.Prefix.OperationMetadataHash),
            "bf4c31452ac6d183b8f34cc1af6595cd1ee11f94c6b18d49452db01c568a6a45" to Pair("Lr2ktwv16TRusAF6KaoEW3r8Wh5M5UXjhvZmHzCvKd1Uj48tXwAF", Tezos.Prefix.OperationMetadataListHash),
            "e386359b2374a4eadd4d35617671a5fe0da38e9d01b43c0938a895a4a5ed03f9" to Pair("LLr2xFh9AdU2jMjG54jHeVsSUzWqXMqTMCysR5Jp95GprPbBm7gJA", Tezos.Prefix.OperationMetadataListListHash),

            "3f5908d325f3d4107a09f745dc372a08bccf95b7" to Pair("tz1RQyvRnGZUiUVVcJiiL7BwaNUxYaK9Gq99", Tezos.Prefix.Ed25519PublicKeyHash),
            "cca60dcb06a41c1fa052c74fe22233cfd9a4b57c" to Pair("tz2SyKS6VjvfEmmRayzYaKSNBiVUAV3wzYU7", Tezos.Prefix.Secp256K1PublicKeyHash),
            "81cc4cf88857d7a9ce867e361647639bbbf062c0" to Pair("tz3YAMXU4qbPHVCR1hx2Q1qQkqppbjMpXL4z", Tezos.Prefix.P256PublicKeyHash),
            "c2c0cd60707bd74cb985672e45c4eedb333b5d74" to Pair("KT1SLXd7g5YqT81PnH4R9K4hwz9kJpCwNkn2", Tezos.Prefix.ContractHash),

            "daceb7e6e75e421edad81355b5f29c49" to Pair("idtUTwDSLeHFCKR1JnCPSj68HXnLT5", Tezos.Prefix.CryptoboxPublicKeyHash),

            "475edf55f34f1e4b8664fd3fa218122c2a02da1fe746e7829e09d1bc9dc83499" to Pair("edsk3DLLzqVRjzPwCAptTETpT5JZapg81FH8YbFKL4dYRMaQpFo3Mu", Tezos.Prefix.Ed25519Seed),
            "8ad8ce61fdc72076be473f8b3959d1fff3975e6ce3f4bbe64542ef3a20017f10" to Pair("edpkuhNbSQLppKWQkihBb1awHP85Z7iSTUBMjC4ZRtSkqSDHYUP7ty", Tezos.Prefix.Ed25519PublicKey),
            "f72790ef43bcba94e51ea2c6546d9a60d8a1fdd6ec47d94c73108705ac8a7fb9" to Pair("spsk3JQutvdG5STEELfJRrC8wmh7EEndU1Jnu9TfATnMUtVFgHXAba", Tezos.Prefix.Secp256K1SecretKey),
            "b2b9db2841a31995522cb6010ba097302897a12c104beab1d02ce7c537c4ac04" to Pair("p2sk3hW82MtpmcJtziQqwyaMs48Ray9RuieSKgu7ePm9VXAHLnhgzB", Tezos.Prefix.P256SecretKey),

            "22d4dd44b2671a05cf265b7a81f3d94abe7dc2aa508d1c29bf2fe0728d90a09c6208d97d81359565517bf2614c95acd8d4ccb219ea3be014" to Pair("edesk1NB3aVLiqAeWz37PeEamUce1ms21SDh4BL8meLkKwHibhDkW7QWNzZCHAfexwCoAtMZofdzHiectcbNrrj9", Tezos.Prefix.Ed25519EncryptedSeed),
            "760186ea7117c2a74315582145260e5831fb0998e41c4bf1b36c2dc97d5f353df40489b0c0a104d8e2ca44c55c998a41a361c0f4793b1c72" to Pair("spesk1njTvUaKspW9sYcP942BwdEQkbbUdqHZuZSHirCdKGzhwsoXYmf8KhwaJeq6f55msU22JZsiKEASWnKLkgB", Tezos.Prefix.Secp256K1EncryptedSecretKey),
            "544cad98a610235587dcc6f3e8a3ce5939ceb13b3350559361ba1cf58614a80698a1210f1b039e943fbb5a0803140b7732d31faa40abf2ec" to Pair("p2esk21af5ekBhFu5HTUKxCmcspPb22JdBtmewiWrx9vSAreCSifUj4fkVQ1ucqidGR2cYUU2Je9Nq718qXebC6u", Tezos.Prefix.P256EncryptedSecretKey),

            "ae64e486a6b1a594eed395112540771c419c4f8c748402b6c530f54fa602c325e07a041503dcf7611e35bde59415617265e9b243ceb0b6b512b83346" to Pair("seesk8Fm9iE86ti3wJXSbCouKQn6cCP7WF3XUxgaVNue1rf9neZ2mG2NbC5ovihUzRqGGnY2Dr8yHP8koo3ELWHRad5Fx", Tezos.Prefix.Secp256K1EncryptedScalar),

            "c5d6449779370187578fd775dc3e55cbaa300426c7d258c76ce665bcb98da66e7c" to Pair("sppkE7zASd9wbU7WbBT37nCcMAQgfhBDsW6cEZYGz7huH78zEceGERh", Tezos.Prefix.Secp256K1PublicKey),
            "181872df40aa53eb52a1a353ee9de50d0f987758911f666100d61d08bc655810e2" to Pair("p2pk6oXJn3Da7CWkBauEP6zDFnJee5RbSKd9ajNDcrJUu6DVY41UsA7", Tezos.Prefix.P256PublicKey),
            "c321155281c8298fc6700533ec10aad996a186f4e0c1b7d0c3e02c4d9ea4554a" to Pair("SSp3BwHVZNR8323JnUBeHfRdnzXBHsLsq8omNGL5o5if26KU6rZik", Tezos.Prefix.Secp256K1Scalar),
            "a99a2d68d47c292ade55906ecece24e17af3451a957a5f0bb1f9ff86e7d9357988" to Pair("GSpE16ugxznfKSd2ckNWbUNCdXfUYauPPfYyRRugcg71LgFBkNKAv6", Tezos.Prefix.Secp256K1Element),

            "95e395d4c312e2f6a30e8bd691cf306d812476fd26068d428142321ea3a81a50047323c689287e9f02db8518c660c41effe068784a270512365c243d9cbb77e1" to Pair("edskRwd9D8hzH6S9dBxrWg72EdmCZ16uSHLUsqqzzuYujgqUGdJKWfxSKgaWdyvvqdwbK9nxyJ4CNL3RnCjEgXG2abJeqHdNUy", Tezos.Prefix.Ed25519SecretKey),
            "efea37c304317102ff02c82705e229ba601e5c141fcbfc063b52ea2ddfb367a3c63a564a97237d7e7504be043d45963ef9c1c724b666bcc1b0a59de1c2a72193" to Pair("edsigu5CFQXdbDySf2ZVSGBXAW2cC2WtG7e9PU9M3ZjhdpPGQPWgsQxz7ZyziWJ6nPt9fLQdcaZB2ZEFWU9nY9xtQU8nGcNY1Tz", Tezos.Prefix.Ed25519Signature),
            "c78b2861c35f15d266d2450b3b783615b63cfc01f49aa25c8ca4d06f495f7e9e1931eb6b0294328707ed79834fcc14a7e1944e36d9958e5b2f036eb6cb7150dd" to Pair("spsig1Xv7ujqGPuD4GAGoKm5M7xcmw1FcPVFKVfbAXCWDCXuYVHZdh6Wdz5p9ba31qxQF6WRmYrGgTGZ97G9QLxtZrCDEKPE3Eq", Tezos.Prefix.Secp256K1Signature),
            "534388e4213a518591622321aac51ff7ca36071c0b7872b5f5f2932862fdd6a6c597c329204d26531b0c4e48ac2bc53383134d049332ccab761875539cee6580" to Pair("p2sigYCKtMeoHN7UjSrr75D7va7GZjUqJxMiZPL5tJdXjn8RPVspt8bZ4R8jwx8EhAMJ31zikGu4cLw19xa7ofqvLT82ANBC8o", Tezos.Prefix.P256Signature),
            "39455b2e136efda425692cd38a49279d6597dd9db636723af217c9cae79aacd45e0fdff633611cbdb51bf034282d6e16a1e8a81747808267c70361932d44bc71" to Pair("sigVUsPZfih9CzgbD52wGbjGaCWeRkAipf7QwZAwtNZCT65rmHN1au2LVv5JUzffK8mnzaedVfk2jyMa775GjvcxGfknRZB2", Tezos.Prefix.GenericSignature),

            "23a9f05a" to Pair("NetXNy1E383jSrX", Tezos.Prefix.ChainId),

            "af453ddd281f2bf058c9b18d4a774bd3b98d51c1b2c02412deb526798f3d198510e4ef7e6c16cdc31ce3a8cbb21e643d3cfb356af15b7cd1b1eb9d861dd71f55e91a7baf9e081708bdf74245c807a2411d4dfb6aed8d5f31e940a750efedd2273449d5351c74c690b10ebf2ca13429500c44315ef9af69df029005423850b6e7e9201e35c2dace8a753f8ff6898757d92fc7e79cceca737caab16838d256f04c94012769ece782f8b6" to Pair("sask453Y2atFNQUwtPjN5M7Bzddnz29Q2tm2K6NWz2yPMTCGek9nVrUHK4GjH4F4bbvt5z5GpEuHmU4HoF9XrJsMGA8G6ogkhezKiFTcpTVBgkP6YoAr3rq7HgsFWGmaFTZkrUGB9u1rpRcCzK2ED2NGBxXnRJPEVqFSEjdKeGVnxyQ65DLNBwTA1CnfcCF3gAVMJnxEWPGwZCRZvRSx7DgoXDEBHz13tK9cK26Fwaod3Fjay", Tezos.Prefix.SaplingSpendingKey),

            "0cf5167bb9fe405489e0a9af41ddb213556d62109f6e069a9e54e50018015ab56c7ae197076daa551393f1" to Pair("zet12WGVjXxsaWaLJVjyhCp8Pt4oew3noTbS55znq9pwVScY2UFCfq5xbbDoB8ByBtyxP", Tezos.Prefix.SaplingAddress),
        )
}