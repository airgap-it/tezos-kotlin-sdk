package it.airgap.tezos.operation.internal.coder

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.core
import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.zarith.ZarithNatural
import it.airgap.tezos.michelson.internal.michelson
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.contract.Entrypoint
import it.airgap.tezos.operation.contract.Parameters
import it.airgap.tezos.operation.contract.Script
import it.airgap.tezos.operation.header.BlockHeader
import it.airgap.tezos.operation.inlined.InlinedEndorsement
import it.airgap.tezos.operation.inlined.InlinedPreendorsement
import it.airgap.tezos.operation.internal.operation
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OperationContentBytesCoderTest {

    private lateinit var tezos: Tezos
    private lateinit var operationContentBytesCoder: OperationContentBytesCoder

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        operationContentBytesCoder = OperationContentBytesCoder(
            tezos.core().dependencyRegistry.encodedBytesCoder,
            tezos.core().dependencyRegistry.addressBytesCoder,
            tezos.core().dependencyRegistry.publicKeyBytesCoder,
            tezos.core().dependencyRegistry.implicitAddressBytesCoder,
            tezos.core().dependencyRegistry.signatureBytesCoder,
            tezos.core().dependencyRegistry.zarithNaturalBytesCoder,
            tezos.core().dependencyRegistry.mutezBytesCoder,
            tezos.michelson().dependencyRegistry.michelineBytesCoder,
            tezos.core().dependencyRegistry.timestampBigIntCoder,
            tezos.operation().dependencyRegistry.tagToOperationContentKindConverter,
        )
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should encode Operation to bytes`() {
        operationsWithBytes.forEach {
            assertContentEquals(it.second, operationContentBytesCoder.encode(it.first))
        }
    }

    @Test
    fun `should decode Operation from bytes`() {
        operationsWithBytes.forEach {
            assertEquals(it.first, operationContentBytesCoder.decode(it.second))
            assertEquals(it.first, operationContentBytesCoder.decodeConsuming(it.second.toMutableList()))
        }
    }

    @Test
    fun `should fail to decode Operation from invalid bytes`() {
        invalidBytes.forEach {
            assertFailsWith<IllegalArgumentException> { operationContentBytesCoder.decode(it) }
            assertFailsWith<IllegalArgumentException> { operationContentBytesCoder.decodeConsuming(it.toMutableList()) }
        }
    }

    private val operationsWithBytes: List<Pair<OperationContent, ByteArray>>
        get() = listOf(
            OperationContent.SeedNonceRevelation(
                1,
                "6cdaf9367e551995a670a5c642a9396290f8c9d17e6bc3c1555bfaa910d92214".asHexString()
            ) to "01000000016cdaf9367e551995a670a5c642a9396290f8c9d17e6bc3c1555bfaa910d92214".asHexString().toByteArray(),
            OperationContent.DoubleEndorsementEvidence(
                InlinedEndorsement(
                    BlockHash("BLT3XKN3vFqWnWfuuLenQiyVgEgKcJttnGGdCcQbmE95xz9y7S5"),
                    OperationContent.Endorsement(
                        1U,
                        1,
                        1,
                        BlockPayloadHash("vh2cHpyeaHQhF7g3RFh8usyYmTTpt882UsRyXECuBwPiB3TcsKNd"),
                    ),
                    GenericSignature("sigdV5DNZRBLBDDEkbWcqefBuMZevanVyjotoazkkLbk7jXR8oZUmnxt6n3hkQtTe9WbLEkcCUWw1Ey7Ybby5z35nHKqpndn"),
                ),
                InlinedEndorsement(
                    BlockHash("BLZS5mP4BufHrZfvzrvw1ReWnj1L2zcQ4mM6Jywoaxe4mHbiCNn"),
                    OperationContent.Endorsement(
                        2U,
                        2,
                        2,
                        BlockPayloadHash("vh2rXj5TAG8p1HKiMyaWDdYrRL2rTBPyFLkVorgzEEBqqd4sgsXG"),
                    ),
                    GenericSignature("sigff9imsFxGwyQ8nEpXUR8ZFwTqZWjMJAgKGwub6Mn9Cnu4VvBppTRt84VPp1fRwqpx8JTrLHg76guTGzkm9ETKwFNCzniY"),
                ),
            ) to "020000008b611895c74249d0a90db97644942543d9a9f9efdf48f6fae039f1f72b07ad9ed415000100000001000000017afe70591b8fce15d79383d3b2d1215e11d49672901d733842d6221562a98324767251a73e10b6bbe72a662576abb35bb3161f9a662ead7207e26ca95dbd1c0a3b086470822e83160f916415e00f07840cecfb897e61945255c3ab943bebc1e60000008b6f9a5a686491dc1af62fe3f0c3b2d8d6e1f5883f50592029980d55864a6b24b015000200000002000000029b53a37d056c73de29fef1e17abfaab06876147aa7083b52b0ef6ba92bf5a50c870fd592cf831578551c230a5cc324c7d26c67e5185f071b3fdb797ef89f3be013d51b0f3cf181cb842f13bf35c29a2343908b348b7b5db2e38caa505d5dfc34".asHexString().toByteArray(),
            OperationContent.DoubleBakingEvidence(
                BlockHeader(
                    1,
                    1U,
                    BlockHash("BKsP8FYgikDmqbUiVxfgXVjWuay5LQZY6LP4EvcsFK8uuqj4wQD"),
                    Timestamp.Rfc3339("1970-01-01T00:00:00.001Z"),
                    1U,
                    OperationListListHash("LLoaLP6mc6nVzG2Rp3fSrHFvvGpUvkbHCjLASVduN7GzQAKnPctrR"),
                    emptyList(),
                    ContextHash("CoWKSZnE72uMLBeh3Fmj3LSXjJmeCEmYBMxAig15g3LPjTP4rHmR"),
                    BlockPayloadHash("vh2cJrNF6FCXo1bfnM9hj66NDQSGQCBxTtqkxkMLzkTeeDnZjrvD"),
                    1,
                    "d4d34b5686c98ae1".asHexString(),
                    null,
                    true,
                    GenericSignature("sigiaEd9dHEGKgccx3JBBDw4eb6WVxGH3MvyziYbQqWQRMmyecdo5VuSkYWkgZvcQXshB4vV2qkTb6AxbKruaNPfnMg4u2EA"),
                ),
                BlockHeader(
                    2,
                    2U,
                    BlockHash("BMaBxGyVhtTiMKd7KA8HXJnbTK4e1TzffNc94G18op55HGQYVRk"),
                    Timestamp.Rfc3339("1970-01-01T00:00:00.002Z"),
                    2U,
                    OperationListListHash("LLoaNF9sd5z2SZtSmUopYNX6qs77QAUJqrnd5ei378H4bcJhQcPt5"),
                    emptyList(),
                    ContextHash("CoVj5HxwnPHpC1SgCC6pgqVPgw2vqFEqaC2bF5STqcbyX6giVrGn"),
                    BlockPayloadHash("vh2MHqgJtw8v7CDrZKYWtLmqGJtjzkRvs9yUeHNQqdgDJyCYm21q"),
                    2,
                    "336ebf95efce0475".asHexString(),
                    NonceHash("nceUeUCJRZ4M7FCSBsAUZU6dmxePdH7irje9Gfj9zWwCdfWd5B4Ee"),
                    false,
                    GenericSignature("sigRsUhHqaFVBeV4qzyCZ6Y9TvoKajyNwyPQQCW3SbgPYY99MrpTqR2FopjzZEHMWoJG7LaTaHu7bnieKQRKqCRLA7hB7Ekp"),
                ),
            ) to "03000000e0000000010114a98b361825acd1997319b0b01069908d1103df26a5646bf998cd6df80b95c60000000000000001018539ef2bf06ca139c6aeda9edc16c853f2b09ff232fab97d7a15150a602ea36500000000dc8d5cafd036ba185119ba904aefbdefd6d30de1f5e4a49fb20b0997ea2cdc357b08b37679350e62ea1bff3287d151c79156f0160b296bdade0ffa7f16f26b6300000001d4d34b5686c98ae100ff9d584824e3bf8b4817abdce782d94d93df6c60581e581990767cb8c0c07c577c328cddebd2da2433736411e17c2cfb282c8067e89c5a3e48246f50eca5e7525f000001000000000202f5043ad9d3aeea868db43f2abda52e1b7f176f928742964ce1db62d8f48cd67f0000000000000002028974da4dc7fcb31faab671f35d065db1d699a2b7d97bb830330977b8650591b0000000008e84ab5712175f8ab1ce14bcf5185d712c472a4e6abf51093a06c7e9042e59d258ef5ec7e36bb4004a4e7f10cb94032d59b65f8a86450c20a63d802ad749546200000002336ebf95efce0475ff37ad10c119adb450d7456104f3971536fb486124a262549c00d3310cd93e6820001dad11dad4d16f110476a24734b1414725506b354e01de4e54a4fdcec01604fda840b53f2cac4109c32680fe58600d96749b1d2891a0aa22b222ba36c864f001".asHexString().toByteArray(),
            OperationContent.DoubleBakingEvidence(
                BlockHeader(
                    1,
                    1U,
                    BlockHash("BKsP8FYgikDmqbUiVxfgXVjWuay5LQZY6LP4EvcsFK8uuqj4wQD"),
                    Timestamp.Rfc3339("1970-01-01T00:00:00.001Z"),
                    1U,
                    OperationListListHash("LLoaLP6mc6nVzG2Rp3fSrHFvvGpUvkbHCjLASVduN7GzQAKnPctrR"),
                    listOf(HexString("00000001000000000100000001")),
                    ContextHash("CoWKSZnE72uMLBeh3Fmj3LSXjJmeCEmYBMxAig15g3LPjTP4rHmR"),
                    BlockPayloadHash("vh2cJrNF6FCXo1bfnM9hj66NDQSGQCBxTtqkxkMLzkTeeDnZjrvD"),
                    1,
                    "d4d34b5686c98ae1".asHexString(),
                    null,
                    true,
                    GenericSignature("sigiaEd9dHEGKgccx3JBBDw4eb6WVxGH3MvyziYbQqWQRMmyecdo5VuSkYWkgZvcQXshB4vV2qkTb6AxbKruaNPfnMg4u2EA"),
                ),
                BlockHeader(
                    2,
                    2U,
                    BlockHash("BMaBxGyVhtTiMKd7KA8HXJnbTK4e1TzffNc94G18op55HGQYVRk"),
                    Timestamp.Rfc3339("1970-01-01T00:00:00.002Z"),
                    2U,
                    OperationListListHash("LLoaNF9sd5z2SZtSmUopYNX6qs77QAUJqrnd5ei378H4bcJhQcPt5"),
                    listOf(HexString("00000002ff000000020000000200000002"), HexString("00000002000000000200000002")),
                    ContextHash("CoVj5HxwnPHpC1SgCC6pgqVPgw2vqFEqaC2bF5STqcbyX6giVrGn"),
                    BlockPayloadHash("vh2MHqgJtw8v7CDrZKYWtLmqGJtjzkRvs9yUeHNQqdgDJyCYm21q"),
                    2,
                    "336ebf95efce0475".asHexString(),
                    NonceHash("nceUeUCJRZ4M7FCSBsAUZU6dmxePdH7irje9Gfj9zWwCdfWd5B4Ee"),
                    false,
                    GenericSignature("sigRsUhHqaFVBeV4qzyCZ6Y9TvoKajyNwyPQQCW3SbgPYY99MrpTqR2FopjzZEHMWoJG7LaTaHu7bnieKQRKqCRLA7hB7Ekp"),
                ),
            ) to "03000000f1000000010114a98b361825acd1997319b0b01069908d1103df26a5646bf998cd6df80b95c60000000000000001018539ef2bf06ca139c6aeda9edc16c853f2b09ff232fab97d7a15150a602ea365000000110000000d00000001000000000100000001dc8d5cafd036ba185119ba904aefbdefd6d30de1f5e4a49fb20b0997ea2cdc357b08b37679350e62ea1bff3287d151c79156f0160b296bdade0ffa7f16f26b6300000001d4d34b5686c98ae100ff9d584824e3bf8b4817abdce782d94d93df6c60581e581990767cb8c0c07c577c328cddebd2da2433736411e17c2cfb282c8067e89c5a3e48246f50eca5e7525f000001260000000202f5043ad9d3aeea868db43f2abda52e1b7f176f928742964ce1db62d8f48cd67f0000000000000002028974da4dc7fcb31faab671f35d065db1d699a2b7d97bb830330977b8650591b0000000260000001100000002ff0000000200000002000000020000000d000000020000000002000000028e84ab5712175f8ab1ce14bcf5185d712c472a4e6abf51093a06c7e9042e59d258ef5ec7e36bb4004a4e7f10cb94032d59b65f8a86450c20a63d802ad749546200000002336ebf95efce0475ff37ad10c119adb450d7456104f3971536fb486124a262549c00d3310cd93e6820001dad11dad4d16f110476a24734b1414725506b354e01de4e54a4fdcec01604fda840b53f2cac4109c32680fe58600d96749b1d2891a0aa22b222ba36c864f001".asHexString().toByteArray(),
            OperationContent.ActivateAccount(
                Ed25519PublicKeyHash("tz1PokEhtiBGCmekQrcN87pCDmqy99TjaLuN"),
                "7b27ba02550e6834b50173c8c506de42d901c606".asHexString(),
            ) to "042db6ed2d71e8f22ce348c1b7b2e7f08892bd50ef7b27ba02550e6834b50173c8c506de42d901c606".asHexString().toByteArray(),
            OperationContent.Proposals(
                Ed25519PublicKeyHash("tz1QVzD6eV73LhtzhNKs94fKbvTg7VjKjEcE"),
                1,
                listOf(ProtocolHash("PtYnGfhwjiRjtA7VZriogYL6nwFgaAL9ZuVWE6UahXCMn6BoJPv")),
            ) to "050035533a79b20d6ea4dc8b92ab1cf33b448b93c78f0000000100000020f0e14a6c55f809a0ac08dc9bba0596b0daac1944520dfa9b8e2ce4e1a102a203".asHexString().toByteArray(),
            OperationContent.Proposals(
                Ed25519PublicKeyHash("tz1MRrtJC9sk1o1D57LPWev6DDVjMgra5pXb"),
                1,
                listOf(ProtocolHash("PtARwRL7jEGtzoCCWDBXe6ZJ4ZJiWtDgBC2a5WwnHYYyKPdmwrb"), ProtocolHash("Ps6NdX1CpeF3kHV5CVQZMLZKZwAN8NYN9crdL3GzEg4uNg7f3DY")),
            ) to "050013a312c56ed0eb53799ce6ef3eabfc1102f73b940000000100000040be2160f0cad3ca52a8e1a2f9e6fb25e748a769267dad2964550e6d946d0a03c23138e6c4f4e5e47064cbd0cd36b2a09ad01d0709b9737b1f8622a43448de01d5".asHexString().toByteArray(),
            OperationContent.Ballot(
                Ed25519PublicKeyHash("tz1eNhmMTYsti2quW46a5CBJbs4Fde4KGg4F"),
                1,
                ProtocolHash("PsjL76mH8vo3fTfUN4qKrdkPvRfXw7KJPWf87isNAxzh1vqdFQv"),
                OperationContent.Ballot.BallotType.Yay,
            ) to "0600cd8459db8668d3ae6a4f49cb8fe3c5bbd6c76956000000018522ef9f87cef2f745984cdbfe4a723acfbe7979c6f24ebc04a86d786b1c038500".asHexString().toByteArray(),
            OperationContent.Ballot(
                Ed25519PublicKeyHash("tz1eNhmMTYsti2quW46a5CBJbs4Fde4KGg4F"),
                1,
                ProtocolHash("PsjL76mH8vo3fTfUN4qKrdkPvRfXw7KJPWf87isNAxzh1vqdFQv"),
                OperationContent.Ballot.BallotType.Nay,
            ) to "0600cd8459db8668d3ae6a4f49cb8fe3c5bbd6c76956000000018522ef9f87cef2f745984cdbfe4a723acfbe7979c6f24ebc04a86d786b1c038501".asHexString().toByteArray(),
            OperationContent.Ballot(
                Ed25519PublicKeyHash("tz1eNhmMTYsti2quW46a5CBJbs4Fde4KGg4F"),
                1,
                ProtocolHash("PsjL76mH8vo3fTfUN4qKrdkPvRfXw7KJPWf87isNAxzh1vqdFQv"),
                OperationContent.Ballot.BallotType.Pass,
            ) to "0600cd8459db8668d3ae6a4f49cb8fe3c5bbd6c76956000000018522ef9f87cef2f745984cdbfe4a723acfbe7979c6f24ebc04a86d786b1c038502".asHexString().toByteArray(),
            OperationContent.DoublePreendorsementEvidence(
                InlinedPreendorsement(
                    BlockHash("BLT3XKN3vFqWnWfuuLenQiyVgEgKcJttnGGdCcQbmE95xz9y7S5"),
                    OperationContent.Preendorsement(
                        1U,
                        1,
                        1,
                        BlockPayloadHash("vh2cHpyeaHQhF7g3RFh8usyYmTTpt882UsRyXECuBwPiB3TcsKNd"),
                    ),
                    GenericSignature("sigdV5DNZRBLBDDEkbWcqefBuMZevanVyjotoazkkLbk7jXR8oZUmnxt6n3hkQtTe9WbLEkcCUWw1Ey7Ybby5z35nHKqpndn"),
                ),
                InlinedPreendorsement(
                    BlockHash("BLZS5mP4BufHrZfvzrvw1ReWnj1L2zcQ4mM6Jywoaxe4mHbiCNn"),
                    OperationContent.Preendorsement(
                        2U,
                        2,
                        2,
                        BlockPayloadHash("vh2rXj5TAG8p1HKiMyaWDdYrRL2rTBPyFLkVorgzEEBqqd4sgsXG"),
                    ),
                    GenericSignature("sigff9imsFxGwyQ8nEpXUR8ZFwTqZWjMJAgKGwub6Mn9Cnu4VvBppTRt84VPp1fRwqpx8JTrLHg76guTGzkm9ETKwFNCzniY"),
                ),
            ) to "070000008b611895c74249d0a90db97644942543d9a9f9efdf48f6fae039f1f72b07ad9ed414000100000001000000017afe70591b8fce15d79383d3b2d1215e11d49672901d733842d6221562a98324767251a73e10b6bbe72a662576abb35bb3161f9a662ead7207e26ca95dbd1c0a3b086470822e83160f916415e00f07840cecfb897e61945255c3ab943bebc1e60000008b6f9a5a686491dc1af62fe3f0c3b2d8d6e1f5883f50592029980d55864a6b24b014000200000002000000029b53a37d056c73de29fef1e17abfaab06876147aa7083b52b0ef6ba92bf5a50c870fd592cf831578551c230a5cc324c7d26c67e5185f071b3fdb797ef89f3be013d51b0f3cf181cb842f13bf35c29a2343908b348b7b5db2e38caa505d5dfc34".asHexString().toByteArray(),
            OperationContent.FailingNoop("cc7e647be422e432a3291ec8a2ee6f5e2210c51825b753758a99e266a0c65b15".asHexString()) to "1100000020cc7e647be422e432a3291ec8a2ee6f5e2210c51825b753758a99e266a0c65b15".asHexString().toByteArray(),
            OperationContent.Preendorsement(
                1U,
                1,
                1,
                BlockPayloadHash("vh2KDvhtt44Lyq187SnZjSDyRH1LNXbMj3T9G57miWK9QvqH3fhv"),
            ) to "1400010000000100000001543d9791df12f3237de836314a45a348e5d608c80a6a411246dfc67ef1a08d0a".asHexString().toByteArray(),
            OperationContent.Endorsement(
                1U,
                1,
                1,
                BlockPayloadHash("vh2WtVuY9PK3mDsnfdzA6iXc4pocgUff8hgamWwXw19r5kDYHVS5"),
            ) to "15000100000001000000016eba3d57f131a71eab0692e333e889cbafe523c675e588ace92bb5056cbcb889".asHexString().toByteArray(),
            OperationContent.Reveal(
                Ed25519PublicKeyHash("tz1SZ2CmbQB7MMXgcMSmyyVXpya1rkb9UGUE"),
                Mutez("135675"),
                ZarithNatural("154"),
                ZarithNatural("23675"),
                ZarithNatural("34152"),
                Ed25519PublicKey("edpkuaARNJPQygG82X1xed6Z2kDutT8XjL3Fmv1XPBbca1uARirj55"),
            ) to "6b004bd66485632a18d61068fc940772dec8add5ff93fba3089a01fbb801e88a02007a79d89acb296dd9ec2be8fba817702dc41adf19e28bb250a337f840eb263c69".asHexString().toByteArray(),
            OperationContent.Transaction(
                Ed25519PublicKeyHash("tz1i8xLzLPQHknc5jmeFc3qxijar2HLG2W4Z"),
                Mutez("135675"),
                ZarithNatural("154"),
                ZarithNatural("23675"),
                ZarithNatural("34152"),
                Mutez("763243"),
                Ed25519PublicKeyHash("tz1YbTdYqmpLatAqLb1sm67qqXMXyRB3UYiz"),
            ) to "6c00f6cb338e136f281d17a2657437f090daf84b42affba3089a01fbb801e88a02ebca2e00008e1d34730fcd7e8282b0efe7b09b3c57543e59c800".asHexString().toByteArray(),
            OperationContent.Transaction(
                Ed25519PublicKeyHash("tz1i8xLzLPQHknc5jmeFc3qxijar2HLG2W4Z"),
                Mutez("135675"),
                ZarithNatural("154"),
                ZarithNatural("23675"),
                ZarithNatural("34152"),
                Mutez("763243"),
                Ed25519PublicKeyHash("tz1YbTdYqmpLatAqLb1sm67qqXMXyRB3UYiz"),
                Parameters(Entrypoint.Default, MichelineSequence()),
            ) to "6c00f6cb338e136f281d17a2657437f090daf84b42affba3089a01fbb801e88a02ebca2e00008e1d34730fcd7e8282b0efe7b09b3c57543e59c8ff00000000050200000000".asHexString().toByteArray(),
            OperationContent.Transaction(
                Ed25519PublicKeyHash("tz1i8xLzLPQHknc5jmeFc3qxijar2HLG2W4Z"),
                Mutez("135675"),
                ZarithNatural("154"),
                ZarithNatural("23675"),
                ZarithNatural("34152"),
                Mutez("763243"),
                Ed25519PublicKeyHash("tz1YbTdYqmpLatAqLb1sm67qqXMXyRB3UYiz"),
                Parameters(Entrypoint.Root, MichelineSequence()),
            ) to "6c00f6cb338e136f281d17a2657437f090daf84b42affba3089a01fbb801e88a02ebca2e00008e1d34730fcd7e8282b0efe7b09b3c57543e59c8ff01000000050200000000".asHexString().toByteArray(),
            OperationContent.Transaction(
                Ed25519PublicKeyHash("tz1i8xLzLPQHknc5jmeFc3qxijar2HLG2W4Z"),
                Mutez("135675"),
                ZarithNatural("154"),
                ZarithNatural("23675"),
                ZarithNatural("34152"),
                Mutez("763243"),
                Ed25519PublicKeyHash("tz1YbTdYqmpLatAqLb1sm67qqXMXyRB3UYiz"),
                Parameters(Entrypoint.Do, MichelineSequence()),
            ) to "6c00f6cb338e136f281d17a2657437f090daf84b42affba3089a01fbb801e88a02ebca2e00008e1d34730fcd7e8282b0efe7b09b3c57543e59c8ff02000000050200000000".asHexString().toByteArray(),
            OperationContent.Transaction(
                Ed25519PublicKeyHash("tz1i8xLzLPQHknc5jmeFc3qxijar2HLG2W4Z"),
                Mutez("135675"),
                ZarithNatural("154"),
                ZarithNatural("23675"),
                ZarithNatural("34152"),
                Mutez("763243"),
                Ed25519PublicKeyHash("tz1YbTdYqmpLatAqLb1sm67qqXMXyRB3UYiz"),
                Parameters(Entrypoint.SetDelegate, MichelineSequence()),
            ) to "6c00f6cb338e136f281d17a2657437f090daf84b42affba3089a01fbb801e88a02ebca2e00008e1d34730fcd7e8282b0efe7b09b3c57543e59c8ff03000000050200000000".asHexString().toByteArray(),
            OperationContent.Transaction(
                Ed25519PublicKeyHash("tz1i8xLzLPQHknc5jmeFc3qxijar2HLG2W4Z"),
                Mutez("135675"),
                ZarithNatural("154"),
                ZarithNatural("23675"),
                ZarithNatural("34152"),
                Mutez("763243"),
                Ed25519PublicKeyHash("tz1YbTdYqmpLatAqLb1sm67qqXMXyRB3UYiz"),
                Parameters(Entrypoint.RemoveDelegate, MichelineSequence()),
            ) to "6c00f6cb338e136f281d17a2657437f090daf84b42affba3089a01fbb801e88a02ebca2e00008e1d34730fcd7e8282b0efe7b09b3c57543e59c8ff04000000050200000000".asHexString().toByteArray(),
            OperationContent.Transaction(
                Ed25519PublicKeyHash("tz1i8xLzLPQHknc5jmeFc3qxijar2HLG2W4Z"),
                Mutez("135675"),
                ZarithNatural("154"),
                ZarithNatural("23675"),
                ZarithNatural("34152"),
                Mutez("763243"),
                Ed25519PublicKeyHash("tz1YbTdYqmpLatAqLb1sm67qqXMXyRB3UYiz"),
                Parameters(Entrypoint.Named("named"), MichelineSequence()),
            ) to "6c00f6cb338e136f281d17a2657437f090daf84b42affba3089a01fbb801e88a02ebca2e00008e1d34730fcd7e8282b0efe7b09b3c57543e59c8ffff056e616d6564000000050200000000".asHexString().toByteArray(),
            OperationContent.Origination(
                Ed25519PublicKeyHash("tz1LdF7qHCJg8Efa6Cx4LZrRPkvbh61H8tZq"),
                Mutez("135675"),
                ZarithNatural("154"),
                ZarithNatural("23675"),
                ZarithNatural("34152"),
                Mutez("763243"),
                null,
                Script(MichelineSequence(), MichelineSequence())
            ) to "6d000ad2152600ac6bb16b5512e43a337dd562dc2cccfba3089a01fbb801e88a02ebca2e00000000050200000000000000050200000000".asHexString().toByteArray(),
            OperationContent.Origination(
                Ed25519PublicKeyHash("tz1LdF7qHCJg8Efa6Cx4LZrRPkvbh61H8tZq"),
                Mutez("135675"),
                ZarithNatural("154"),
                ZarithNatural("23675"),
                ZarithNatural("34152"),
                Mutez("763243"),
                Ed25519PublicKeyHash("tz1RY8er4ybXszZBbhtQDrYhA5AYY3VQXiKn"),
                Script(MichelineSequence(), MichelineSequence())
            ) to "6d000ad2152600ac6bb16b5512e43a337dd562dc2cccfba3089a01fbb801e88a02ebca2eff0040b33c1a35d72f3c85747f605b1902d36fc8c9a3000000050200000000000000050200000000".asHexString().toByteArray(),
            OperationContent.Delegation(
                Ed25519PublicKeyHash("tz1QVAraV1JDRsPikcqJVE4VccvW7vDWCJHy"),
                Mutez("135675"),
                ZarithNatural("154"),
                ZarithNatural("23675"),
                ZarithNatural("34152"),
            ) to "6e00352bb30ffdb72d101083a4fc5cd156f007705f5dfba3089a01fbb801e88a0200".asHexString().toByteArray(),
            OperationContent.Delegation(
                Ed25519PublicKeyHash("tz1QVAraV1JDRsPikcqJVE4VccvW7vDWCJHy"),
                Mutez("135675"),
                ZarithNatural("154"),
                ZarithNatural("23675"),
                ZarithNatural("34152"),
                Ed25519PublicKeyHash("tz1dStZpfk5bWsvYvuktDJgDEbpuqDc7ipvi"),
            ) to "6e00352bb30ffdb72d101083a4fc5cd156f007705f5dfba3089a01fbb801e88a02ff00c356e7cb9943f6ef4168bea7915c7f88152e6c37".asHexString().toByteArray(),
            OperationContent.RegisterGlobalConstant(
                Ed25519PublicKeyHash("tz1brHnNaHcpxqHDhqwmAXDq1i4F2A4Xaepz"),
                Mutez("135675"),
                ZarithNatural("154"),
                ZarithNatural("23675"),
                ZarithNatural("34152"),
                MichelineSequence(),
            ) to "6f00b1d399df432bbbdbd45cb6b454699ea96d77dabffba3089a01fbb801e88a02000000050200000000".asHexString().toByteArray(),
            OperationContent.SetDepositsLimit(
                Ed25519PublicKeyHash("tz1gxabEuUaCKk15qUKnhASJJoXhm9A7DVLM"),
                Mutez("135675"),
                ZarithNatural("154"),
                ZarithNatural("23675"),
                ZarithNatural("34152"),
            ) to "7000e9dcc1a4a82c49aeec327b15e9ed457dc22a1ebcfba3089a01fbb801e88a0200".asHexString().toByteArray(),
            OperationContent.SetDepositsLimit(
                Ed25519PublicKeyHash("tz1gxabEuUaCKk15qUKnhASJJoXhm9A7DVLM"),
                Mutez("135675"),
                ZarithNatural("154"),
                ZarithNatural("23675"),
                ZarithNatural("34152"),
                ZarithNatural("634"),
            ) to "7000e9dcc1a4a82c49aeec327b15e9ed457dc22a1ebcfba3089a01fbb801e88a02fffa04".asHexString().toByteArray(),
        )

    private val invalidBytes: List<ByteArray>
        get() = listOf(
            "000000016cdaf9367e551995a670a5c642a9396290f8c9d17e6bc3c1555bfaa910d92214".asHexString().toByteArray(),
        )
}