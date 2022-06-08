package it.airgap.tezos.core.type.encoded

import it.airgap.tezos.core.internal.context.TezosCoreContext.asHexString
import org.junit.Test
import kotlin.test.assertEquals

class MetaEncodedKindTest {

    @Test
    fun `should recognize Encoded Kind`() {
        val stringsAndBytesWithExpected = listOf(
            Pair("BLjbLd6p8tkw5WReHceuFTrL1avm6rZ6xAmUfLQTFFwaqB7Qiig", "013486abe97e79c8eebe63b687ce84d3e706f54c7c9d45f35bf4db198b335e05a6a5") to BlockHash,
            Pair("ooQYz3vvAY1CDYKxWDgTuL1SFFxPtDQ8bH8AMSX581E2kC9PV2P", "057464addab259052618d9f187206c0d95536ebfa9e8bae77eea7f75d57fdadb20b8") to OperationHash,
            Pair("LowiHWCp2hvkregkj6vieF14g27jin4LHoJXuRKDq7Xto4U8sn7M", "85e97a242bb5a286ebf72120be9631d97882896693026e9d3d3bb2ae3a9e85d42dc9") to OperationListHash,
            Pair("LLoZyjqTUBTeuW5ysniv7b8hVN7SzvBbtJEDmpe7kfxtWDFikZ1yb", "1d9f6d565ae2c7a78b34ba24dc67d9546951607ec91fd091c3a6d10777a31369cf7909") to OperationListListHash,
            Pair("Ps6CQpXXQAr8euS7Bj1xMJ3YDso9GNmnM6digghAWKUrRC5paP9", "02aa30d27c9bc2169138e73351b942c493a9f7c0e04e142039004946122e9dcbfcf6") to ProtocolHash,
            Pair("CoVHQXiqbAoyKHyHCinbft2Y9crCcbgA23D6ipnhJoTngvSwNF7y", "4fc7543c340696a1f28703000f460a5a0eed0ea087f9abdfb49acfc9de7379692d9c") to ContextHash,
            Pair("bm3MRDCNKrLihbvE3CvmWxs7HaiVfzMfdt5aiuuiFdwYBhL7x5ff", "eaf966c91badc1766e9ff9389c851adee06fb46c0f795198781c658e418ec6239d05") to BlockMetadataHash,
            Pair("r3REtBxVWHv4MzYL6Gu3A5vfq4BjwtPrDHXehiGFupmYL31Gfn3", "05b746bbc69deda87058d8fb7a946ef852c530e18e3c742a40de4ad190e211caef25") to OperationMetadataHash,
            Pair("Lr2DUrPZj3mFNhgYkBXqJiyWzRfECiDfLiRraagA5DeoYANfH94a", "862777f70cb2dc51a7a15352968a07f010a87400a2ec78717e2a55c8815e8f97b33a") to OperationMetadataListHash,
            Pair("LLr1as5ndFpfFqq1Vfy8DzwAQv54XwK7iqxVNcFi1JrEhNqkp6MnA", "1d9fb62f428c9108315d87759f93d458d0bce03393d0fb4f3f3292b0acdc0c8a4790d0") to OperationMetadataListListHash,
            Pair("tz1TxKgn2BcCezRSG5z3jmcyfduD5ECVhs9f", "06a19f5b36adf68bd40a2e9e3cd7b7d76a07f4a72f62be") to Ed25519PublicKeyHash,
            Pair("tz2Suj2zmH7x4MHxsg8ZhJaBcNm9hR3zFh19", "06a1a1cbf8177ea1ec0e3256ed27559390cbfa107cca9b") to Secp256K1PublicKeyHash,
            Pair("tz3REBdhZEfQwnCr4aJYk8HFmu7vnsQ6gYzc", "06a1a435bcd37e91c86aa4bba0b38362a541a999fd2e6e") to P256PublicKeyHash,
            Pair("KT1UKEcRw1NsWMDSAiM9W3pag3k9sUR2bAWw", "025a79d8726fe686b8537e945ecaa8aa7873be9ded8011") to ContractHash,
            Pair("idsvnMCNqjLxTUY4davVn9myrn1fME", "9967b39927ea35bd081f84c4a4062fe1c7b4") to CryptoboxPublicKeyHash,
            Pair("edsk3sD3hMk5F8Zv1nRg1oZVHuv4ZrzB6ZYBJpaqHaCpVmYZQ29Q7e", "0d0f3a079d5e57ad6205a50991904e39da85a119390d69f6b1cc1f004eea575701c426d8") to Ed25519Seed,
            Pair("edpkusfm3Emi7V8MsNv9UPzWciLMgwDyee96bx4umsx46qkqeknzzY", "0d0f25d9a239a21d017585ced06ce5445c7914ced52d0837cc05b0812ef8db971370bcb1") to Ed25519PublicKey,
            Pair("spsk35xaySazKFHVChzjujUpaoTzXRfWdYeYVvFNYSY9MpXmCoP3dy", "11a2e0c9dae05f08973e015d0a7579a365bf5ea73cf11f4ad212825150f34a6d4db7dec4") to Secp256K1SecretKey,
            Pair("p2sk2sbDh4p5bLLdvDK7mo39QvAXHnssrCFUHfYaD6bsBNgDzZgdSf", "1051eebd45ef89d612e5ee3ac331520b58aa7abc1a6afbf14dbfe0ac21938309c20d1848") to P256SecretKey,
            Pair("vh1vtG8g6VqJwVjEAwmP9iB5xMX1TbfgjYDfdrwXH63cxvgET8PF", "016af221845ce4357aaa5cda4681e239dbb6abf8576cb2bc67d852414d3a4c4bd3e0ce") to BlockPayloadHash,
            Pair("nceUUEvMSyU7gmBdkpiyAFwQhHVncDzKCdj7XcyPZjzkDZMAPWoLn", "45dca920733ad680a5ec55b37ae0460fcbca760fd8a048a544415b92c20f128f49187c") to NonceHash,
            Pair("edesk1vbN8jF4kyLEghX2pvGx52WG8hRB6K3zDddSAa1DAfoEF38HAk2QbdxYUmBeh3aec3P4Jw1g7HUuVtrCLzY", "075a3cb329d9c60041f836e63fb79d53818f44fdcff3bb798a8a3d99ae1ad75adbaac162045976b347139c3bf16e284a0ee1885f98b6789934dcffa22c") to Ed25519EncryptedSeed,
            Pair("spesk26cGSiaSb4Uz7ZVCdLzHvQVDE2w5eZBF2yBZtBfDae9xLD7JAo9DPn9BWBBWLwHtnm6jpJgSwUGqihcfKDv", "09edf1ae96dae12949a16b47aa1c24b9acaace85de8a91c6baf58e44521bcf89a4802722c745c46149fadbf66acc7470dab58302f35400bccbec0d1044") to Secp256K1EncryptedSecretKey,
            Pair("p2esk1vbL476YEFBSbKSpy5x1BUhfycN7FtgHwmegUp2BHg6s7LPXeE4sBky4oCNke4yNNRxsZ5w3skPFpd7itYY", "09303973ab3826621a35a915716a9758faa542fa4702930ab5606efc5bef66d9039e437c08eeb6dcff4f8bc9cad3dbb5c9d9c30ded6c3a05e546940d56") to P256EncryptedSecretKey,
            Pair("seesk7snVMDckZXhXYLuvEMJ3X9y6uyzJQGwF3EANU5Wsx77UuV4wqjnwwTswNJ1Dsqa6tp9YJvXeZBbsC32aAbncpXF7", "01832456f89b712a2960a80721e51887e5a80f52e570b707cfeeda1c8d498367ef5e12df784aefff9684cf64e6c1876b3bbfed31d27114cf944527c8b49961bdb9") to Secp256K1EncryptedScalar,
            Pair("sppkAGzofkA2EyMwboVfMUftSokngqfDWjrikf4W7GjhLmtLcQ7CHra", "03fee256531f7efc4eb1285555503893b1bfebe75df9e3c38a61d60aceb41b6bd16a6699b6") to Secp256K1PublicKey,
            Pair("p2pk9ndAyrRwpAw6b1hD6Duui7bcqntq3t1jcbcekbk68MfGX71efHZ", "03b28b7f7125311510c396c20221d3c22fb6870376a07bcf1aaa27913b6ce8915d4b2eb7f5") to P256PublicKey,
            Pair("SSp2fxSVCS582m87jQQj35CcUQTNy1DTpPbpRiGWkpF3JJpYJq5HG", "26f8887f0e47a797ed1de2d381fcd84bd90afccf07e0bfa75702e3d8ddc4d80d90c776") to Secp256K1Scalar,
            Pair("GSpGB9VURhERK4wGJ9gMh3Avw2oHwYGWgdbDH24bJVUHRVyZrUdrNs", "055c00ea71bfcd4336a32e5071a4c83edf737b2a555909466988039ea387f16900f449c7") to Secp256K1Element,
            Pair("edskRmyjdpsaC1ThNvvCyAepuzqv2vTrLvj3FQEu5JypC7zkuoAYs7jvMs5dFFSBX5YcFu4jkvU8Vjdvq2zfzoXMh5DyrE1Kuc", "2bf64e074c2abfb5f351987736efa44d973da95c35c950a3bb7398fc30915dcd50ebdf8c2e2213d82c1327615a92fd9bb603e5fd729e24a275a67b24bf03261c8321464a") to Ed25519SecretKey,
            Pair("edsigtp3hpiSjkUjz5bVXsvp2s8puVuQAeqdj85VA4E2gFmy4Bm2rrb6nALRKqSYLFcZsFRdj9DPwZPZjNX8cdSw5yVtKeUVz1j", "09f5cd86127c228a214de9a77afff826782bf42774fddf2915ac31b004318f27cbf9c31ba05c1b0539d055972110609c3404afaf7d0420a501f279962860297e0012931e62") to Ed25519Signature,
            Pair("spsig1GhgjYutwcqYcFcfbWnywJKs8gXz5e6JNd4RrceiiSthB2MQGYPwApudKWcfXVYd9VkRha7ViCnVX5BMGtoo8pYoMcbt2X", "0d7365133f534041ea552b5c4f39cc06964c61390b0d5d5314b9ba7c0c4cc03a05d7fde5852721b05ffaaa01d5d5bb170df32dc669ff78a493fb1e4c99d1c8ce83e6cdb5e0") to Secp256K1Signature,
            Pair("p2sigT9A3ZDQJbVXLwB39bjNrdPWa2dLEZbAiNjpFk3E6MgKLA8F8bBMC96Lzt6JjQXKSTdHt5w1XuHbcf9S9wWy9MFmKU4ej8", "36f02c342ca0cba0a83fabae791b7c6ee6779a5ec1fa6647be809cbe44920841aa595fe633cd4c2c07a24bb42782cab01189e8ab1f086cb690e102d71cdfa40ec7dcbf4c") to P256Signature,
            Pair("sigYfEeo85wodMjbaJhosP3FMwKeSitdD4jEtPUF2HzQmFvUkRe2fN8FZ5Q9cA1cNj1FALBHEPyav6azDvvuhMNhzUBuwLuz", "04822b519160e4c7125b6961169a9e9c166c3034d1b4df27b06a6ff64d430767848ce351a7bb722b3533c23e8e82677d1c191d049e11f9e76bb3121ac2936bb2d0d2fb") to GenericSignature,
            Pair("NetXpidbE4MV6DC", "575200bda04869") to ChainId,
            Pair("sask4VbBX4NNecwdcYM64rBxoF8D11yx3WP8LTMPNjbkHdd8kzRDU7tPz9mpjY6SiyYCgkExjyW7mSNimQf6wBCc4TXWtrnt8cWhMgsXMrpFUuuLBgUeBzPJworfvwDh8RE36KLnHurmE5E5SS8om1WVa445YV8vAmrLthTEkPvwmsWxxi7EWh1Mxv6ByXPo8UP3NWnC32Qewq3UxKwXahjEQkcKQqKV4xFsaxJioBgvERwdx", "0bed145cd52396ae568ca77faa96ba070d5caad820ee7ddb250708689855e933f6b85a5cc85caba1a2bec0324bf359ca62a32c7c92ce6218a2f13f8c36d3dea679ad6b103e4a3e3d7f779865c7ae28edd172f88494fc6cbe6fa42b8e9086b6465ddd0105a6866e82cf823596441d62c16880d739cb3a02de98950dea1266f236030ff2a4d52e86c93ef356b903dbf62a06ba95333333e870bc53e8b28cd608eeef45ee8cf03ff47072d4fe9653") to SaplingSpendingKey,
            Pair("zet12nBbRYmBenW7DiDStbKEXkX1KpxJZCkt2zne8ct8uqWmKuaK6LLp7ChmXmcCTdeGa", "124728df2df995aff643af801a95ffb6f6d80b2c46c3f0bce2e0c100d76eda475b7366661ae952cdc82c997c24302c") to SaplingAddress,
        )

        stringsAndBytesWithExpected.forEach {
            val string = it.first.first
            val bytes = it.first.second.asHexString().toByteArray()
            val kind = it.second

            assertEquals(kind, MetaEncoded.Kind.recognize(string))
            assertEquals(kind, MetaEncoded.Kind.recognize(bytes))
            assertEquals(kind, MetaEncoded.Kind.recognize(bytes.toList()))
        }
    }
}