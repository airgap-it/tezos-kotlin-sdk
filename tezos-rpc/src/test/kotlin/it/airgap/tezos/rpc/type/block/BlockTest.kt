package it.airgap.tezos.rpc.type.block

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.michelson.MichelsonData
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.rpc.internal.rpcModule
import it.airgap.tezos.rpc.internal.utils.encodeToString
import it.airgap.tezos.rpc.type.bigmap.RpcBigMapDiff
import it.airgap.tezos.rpc.type.chain.RpcTestChainStatus
import it.airgap.tezos.rpc.type.contract.RpcParameters
import it.airgap.tezos.rpc.type.operation.*
import it.airgap.tezos.rpc.type.storage.RpcLazyStorageDiff
import it.airgap.tezos.rpc.type.storage.RpcStorageBigMapDiff
import it.airgap.tezos.rpc.type.storage.RpcStorageBigMapUpdate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mockTezos
import normalizeWith
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class BlockTest {

    private lateinit var json: Json

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        val tezos = mockTezos()
        json = Json(from = tezos.rpcModule.dependencyRegistry.json) {
            prettyPrint = true
        }
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should serialize RpcBlock`() {
        objectsWithJson.forEach { (value, expected) ->
            val actual = json.encodeToString(value)
            assertEquals(expected.normalizeWith(json), actual)
        }
    }

    @Test
    fun `should deserialize RpcBigMapDiff`() {
        objectsWithJson.forEach { (expected, string) ->
            val actual = json.decodeFromString<RpcBlock>(string)
            assertEquals(expected, actual)
        }
    }

    private val objectsWithJson: List<Pair<RpcBlock, String>>
        get() = listOf(
            RpcBlock(
                protocol = ProtocolHash("Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A"),
                chainId = ChainId("NetXnHfVqm9iesp"),
                hash = BlockHash("BLRLm2B4A6Ri4BX7eHdZeMiWWxgFmx1KgADRbp7tdwyQh5bvkFt"),
                header = RpcBlockHeader(
                    level = 444051,
                    proto = 2U,
                    predecessor = BlockHash("BLznWUsgQuUMKXKgAcxUFZwq9Y9KpZevDVruQ8V1So3jjHF68WG"),
                    timestamp = Timestamp.Rfc3339("2022-04-26T07:07:25Z"),
                    validationPass = 4U,
                    operationsHash = OperationListListHash("LLoas3tyzRCjGKfkaCok681RFTjHQSJ5dipgJZkVSALHxZiBs2xUB"),
                    fitness = listOf(
                        "02",
                        "0006c693",
                        "",
                        "fffffffe",
                        "00000000",
                    ),
                    context = ContextHash("CoW338xEFzvo23cA9zec83VVj3qNYEL6H5DghaDXbLmo9o7r4CMi"),
                    payloadHash = BlockPayloadHash("vh2svwjdP6sYvyDWgbrofirgaxSJ1tRghv1eue9Nw7SxQ4cm9YNn"),
                    payloadRound = 0,
                    proofOfWorkNonce = "6e2037c91e600200",
                    liquidityBakingEscapeVote = false,
                    signature = GenericSignature("sigdKuy9ifmD4bSmJW9bausXsN8y9jhzDBBMoXaNngyyV6miaXXD4X3srtiGvjD8Ahapgtbw3Zjp4kqcPnbpsDm1CAa9wKBN"),
                ),
                metadata = RpcBlockHeaderMetadata(
                    protocol = ProtocolHash("Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A"),
                    nextProtocol = ProtocolHash("Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A"),
                    testChainStatus = RpcTestChainStatus.NotRunning,
                    maxOperationsTtl = 120,
                    maxOperationDataLength = 32768,
                    maxBlockHeaderLength = 289,
                    maxOperationListLength = listOf(
                        RpcOperationListMetadata(maxSize = 4194304, maxOperations = 2048),
                        RpcOperationListMetadata(maxSize = 32768),
                        RpcOperationListMetadata(maxSize = 135168, maxOperations = 132),
                        RpcOperationListMetadata(maxSize = 524288),
                    ),
                    proposer = Ed25519PublicKeyHash("tz1aWXP237BLwNHJcCD4b3DutCevhqq2T1Z9"),
                    baker = Ed25519PublicKeyHash("tz1aWXP237BLwNHJcCD4b3DutCevhqq2T1Z9"),
                    levelInfo = RpcLevelInfo(
                        level = 444051,
                        levelPosition = 444050,
                        cycle = 108,
                        cyclePosition = 1682,
                        expectedCommitment = false,
                    ),
                    votingPeriodInfo = RpcVotingPeriodInfo(
                        votingPeriod = RpcVotingPeriod(
                            index = 21,
                            kind = RpcVotingPeriod.Kind.Proposal,
                            startPosition = 430080,
                        ),
                        position = 13970,
                        remaining = 6509,
                    ),
                    nonceHash = null,
                    consumedGas = "6323000",
                    deactivated = listOf(
                        Ed25519PublicKeyHash("tz1ZzX2LBzRNhLxGvwWBzc6F4ENXtqy29iBm"),
                        Secp256K1PublicKeyHash("tz2QpBYDrBfTuAgiGeyRhuoDaV1fMjHRSBLE"),
                        P256PublicKeyHash("tz3Tqpw6ZsR3KgwwP2eHvxfJ1jV9tvJuS2De"),
                    ),
                    balanceUpdates = listOf(
                        RpcBalanceUpdate.BakingRewards(
                            change = -5000000,
                            origin = RpcBalanceUpdate.Origin.Block,
                        ),
                        RpcBalanceUpdate.BakingBonuses(
                            change = -2112998,
                            origin = RpcBalanceUpdate.Origin.Block,
                        ),
                    ),
                    liquidityBakingEscapeEma = 0,
                    implicitOperationsResults = listOf(
                        RpcSuccessfulManagerOperationResult.Transaction(
                            storage = MichelineSequence(
                                MichelineLiteral.Integer(1),
                            ),
                            balanceUpdates = listOf(
                                RpcBalanceUpdate.LiquidityBakingSubsidies(
                                    change = -2500000,
                                    origin = RpcBalanceUpdate.Origin.Subsidy,
                                ),
                                RpcBalanceUpdate.Contract(
                                    ContractHash("KT1TxqZ8QtKvLu3V3JH7Gx58n7Co8pgtpQU5"),
                                    change = 2500000,
                                    origin = RpcBalanceUpdate.Origin.Subsidy,
                                )
                            ),
                            consumedGas = "225",
                            consumedMilligas = "224048",
                            storageSize = "4632",
                        ),
                    ),
                ),
                operations = listOf(
                    listOf(),
                    listOf(),
                    listOf(),
                    listOf(
                        RpcOperation(
                            protocol = ProtocolHash("Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A"),
                            chainId = ChainId("NetXnHfVqm9iesp"),
                            hash = OperationHash("oooBPYgkmNRn7FdD9fSBZ4EsDg1Bocv3Qu9HQCtHGfzH3ByuZnq"),
                            branch = BlockHash("BM9CrVmdNLdR8CFXaAqdRkJd9NaKPDY4rnbRU1Vha91qHVDbpDS"),
                            contents = listOf(
                                RpcOperationContent.Transaction(
                                    source = Ed25519PublicKeyHash("tz1P1jbxpCDGaan94zbjJqU5sqA9u98ntmPf"),
                                    fee = Mutez("976"),
                                    counter = "10300306",
                                    gasLimit = "6323",
                                    storageLimit = "20",
                                    amount = Mutez("0"),
                                    destination = ContractHash("KT1LFTPiBzNWfuvvN4CaAahQMBM86wG8pBaM"),
                                    parameters = RpcParameters(
                                        entrypoint = "transfer",
                                        value = MichelineSequence(
                                            MichelineSequence(
                                                MichelineLiteral.Bytes("0x00002503436f4f750efd7592e5c66912640bdfe01f16"),
                                                MichelineSequence(
                                                    MichelineSequence(
                                                        MichelineLiteral.Bytes("0x0000c686e38ffd631a501fd6f74194a907fd0e451457"),
                                                        MichelineLiteral.Integer(0),
                                                        MichelineLiteral.Integer(0),
                                                    ),
                                                ),
                                            ),
                                        ),
                                    ),
                                    metadata = RpcOperationMetadata.Transaction(
                                        balanceUpdates = listOf(
                                            RpcBalanceUpdate.Contract(
                                                Ed25519PublicKeyHash("tz1P1jbxpCDGaan94zbjJqU5sqA9u98ntmPf"),
                                                change = -976,
                                                origin = RpcBalanceUpdate.Origin.Block,
                                            ),
                                            RpcBalanceUpdate.BlockFees(
                                                change = 976,
                                                origin = RpcBalanceUpdate.Origin.Block,
                                            ),
                                        ),
                                        operationResult = RpcOperationResult.Transaction.Applied(
                                            storage = MichelinePrimitiveApplication(
                                                prim = MichelsonData.Pair,
                                                args = listOf(
                                                    MichelineLiteral.Integer(53931),
                                                    MichelinePrimitiveApplication(
                                                        prim = MichelsonData.Pair,
                                                        args = listOf(
                                                            MichelineLiteral.Integer(53932),
                                                            MichelineLiteral.Integer(53933),
                                                        ),
                                                    ),
                                                ),
                                            ),
                                            bigMapDiff = listOf(
                                                RpcBigMapDiff.Update(
                                                    bigMap = "53931",
                                                    keyHash = ScriptExprHash("exprv7mQJQT4bqEoqQ6zwLe7itEfjonfCMvW4qgrkbw7eeeu862NZ6"),
                                                    key = MichelinePrimitiveApplication(
                                                        prim = MichelsonData.Pair,
                                                        args = listOf(
                                                            MichelineLiteral.Bytes("0x0000c686e38ffd631a501fd6f74194a907fd0e451457"),
                                                            MichelineLiteral.Integer(0),
                                                        ),
                                                    ),
                                                    value = MichelineLiteral.Integer(10),
                                                ),
                                            ),
                                            consumedGas = "6223",
                                            consumedMilligas = "6222479",
                                            storageSize = "3485",
                                            lazyStorageDiff = listOf(
                                                RpcLazyStorageDiff.BigMap(
                                                    id = "53933",
                                                    diff = RpcStorageBigMapDiff.Update(
                                                        updates = listOf(),
                                                    ),
                                                ),
                                                RpcLazyStorageDiff.BigMap(
                                                    id = "53932",
                                                    diff = RpcStorageBigMapDiff.Update(
                                                        updates = listOf(),
                                                    ),
                                                ),
                                                RpcLazyStorageDiff.BigMap(
                                                    id = "53931",
                                                    diff = RpcStorageBigMapDiff.Update(
                                                        updates = listOf(
                                                            RpcStorageBigMapUpdate(
                                                                keyHash = ScriptExprHash("exprv7mQJQT4bqEoqQ6zwLe7itEfjonfCMvW4qgrkbw7eeeu862NZ6"),
                                                                key = MichelinePrimitiveApplication(
                                                                    prim = MichelsonData.Pair,
                                                                    args = listOf(
                                                                        MichelineLiteral.Bytes("0x0000c686e38ffd631a501fd6f74194a907fd0e451457"),
                                                                        MichelineLiteral.Integer(0),
                                                                    ),
                                                                ),
                                                                value = MichelineLiteral.Integer(10),
                                                            ),
                                                        ),
                                                    ),
                                                ),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                            signature = GenericSignature("sigq9pFUczeR9eytgTcw52mQLAQvZURovnjn9R5LTAeefeZDifyM4t2UENmuVkMkSqh4JBEyzFEJXDGrntMYrSQjkjLEAsHs"),
                        ),
                    ),
                ),
            ) to """
                {
                    "protocol": "Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A",
                    "chain_id": "NetXnHfVqm9iesp",
                    "hash": "BLRLm2B4A6Ri4BX7eHdZeMiWWxgFmx1KgADRbp7tdwyQh5bvkFt",
                    "header": {
                        "level": 444051,
                        "proto": 2,
                        "predecessor": "BLznWUsgQuUMKXKgAcxUFZwq9Y9KpZevDVruQ8V1So3jjHF68WG",
                        "timestamp": "2022-04-26T07:07:25Z",
                        "validation_pass": 4,
                        "operations_hash": "LLoas3tyzRCjGKfkaCok681RFTjHQSJ5dipgJZkVSALHxZiBs2xUB",
                        "fitness": [
                            "02",
                            "0006c693",
                            "",
                            "fffffffe",
                            "00000000"
                        ],
                        "context": "CoW338xEFzvo23cA9zec83VVj3qNYEL6H5DghaDXbLmo9o7r4CMi",
                        "payload_hash": "vh2svwjdP6sYvyDWgbrofirgaxSJ1tRghv1eue9Nw7SxQ4cm9YNn",
                        "payload_round": 0,
                        "proof_of_work_nonce": "6e2037c91e600200",
                        "liquidity_baking_escape_vote": false,
                        "signature": "sigdKuy9ifmD4bSmJW9bausXsN8y9jhzDBBMoXaNngyyV6miaXXD4X3srtiGvjD8Ahapgtbw3Zjp4kqcPnbpsDm1CAa9wKBN"
                    },
                    "metadata": {
                        "protocol": "Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A",
                        "next_protocol": "Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A",
                        "test_chain_status": {
                            "status": "not_running"
                        },
                        "max_operations_ttl": 120,
                        "max_operation_data_length": 32768,
                        "max_block_header_length": 289,
                        "max_operation_list_length": [
                            {
                                "max_size": 4194304,
                                "max_op": 2048
                            },
                            {
                                "max_size": 32768
                            },
                            {
                                "max_size": 135168,
                                "max_op": 132
                            },
                            {
                                "max_size": 524288
                            }
                        ],
                        "proposer": "tz1aWXP237BLwNHJcCD4b3DutCevhqq2T1Z9",
                        "baker": "tz1aWXP237BLwNHJcCD4b3DutCevhqq2T1Z9",
                        "level_info": {
                            "level": 444051,
                            "level_position": 444050,
                            "cycle": 108,
                            "cycle_position": 1682,
                            "expected_commitment": false
                        },
                        "voting_period_info": {
                            "voting_period": {
                                "index": 21,
                                "kind": "proposal",
                                "start_position": 430080
                            },
                            "position": 13970,
                            "remaining": 6509
                        },
                        "nonce_hash": null,
                        "consumed_gas": "6323000",
                        "deactivated": [
                            "tz1ZzX2LBzRNhLxGvwWBzc6F4ENXtqy29iBm",
                            "tz2QpBYDrBfTuAgiGeyRhuoDaV1fMjHRSBLE",
                            "tz3Tqpw6ZsR3KgwwP2eHvxfJ1jV9tvJuS2De"
                        ],
                        "balance_updates": [
                            {
                                "kind": "minted",
                                "category": "baking rewards",
                                "change": "-5000000",
                                "origin": "block"
                            },
                            {
                                "kind": "minted",
                                "category": "baking bonuses",
                                "change": "-2112998",
                                "origin": "block"
                            }
                        ],
                        "liquidity_baking_escape_ema": 0,
                        "implicit_operations_results": [
                            {
                                "kind": "transaction",
                                "storage": [
                                    {
                                        "int": "1"
                                    }
                                ],
                                "balance_updates": [
                                    {
                                        "kind": "minted",
                                        "category": "subsidy",
                                        "change": "-2500000",
                                        "origin": "subsidy"
                                    },
                                    {
                                        "kind": "contract",
                                        "contract": "KT1TxqZ8QtKvLu3V3JH7Gx58n7Co8pgtpQU5",
                                        "change": "2500000",
                                        "origin": "subsidy"
                                    }
                                ],
                                "consumed_gas": "225",
                                "consumed_milligas": "224048",
                                "storage_size": "4632"
                            }
                        ]
                    },
                    "operations": [
                        [],
                        [],
                        [],
                        [
                            {
                                "protocol": "Psithaca2MLRFYargivpo7YvUr7wUDqyxrdhC5CQq78mRvimz6A",
                                "chain_id": "NetXnHfVqm9iesp",
                                "hash": "oooBPYgkmNRn7FdD9fSBZ4EsDg1Bocv3Qu9HQCtHGfzH3ByuZnq",
                                "branch": "BM9CrVmdNLdR8CFXaAqdRkJd9NaKPDY4rnbRU1Vha91qHVDbpDS",
                                "contents": [
                                    {
                                        "kind": "transaction",
                                        "source": "tz1P1jbxpCDGaan94zbjJqU5sqA9u98ntmPf",
                                        "fee": "976",
                                        "counter": "10300306",
                                        "gas_limit": "6323",
                                        "storage_limit": "20",
                                        "amount": "0",
                                        "destination": "KT1LFTPiBzNWfuvvN4CaAahQMBM86wG8pBaM",
                                        "parameters": {
                                            "entrypoint": "transfer",
                                            "value": [
                                                [
                                                    {
                                                        "bytes": "00002503436f4f750efd7592e5c66912640bdfe01f16"
                                                    },
                                                    [
                                                        [
                                                            {
                                                                "bytes": "0000c686e38ffd631a501fd6f74194a907fd0e451457"
                                                            },
                                                            {
                                                                "int": "0"
                                                            },
                                                            {
                                                                "int": "0"
                                                            }
                                                        ]
                                                    ]
                                                ]
                                            ]
                                        },
                                        "metadata": {
                                            "balance_updates": [
                                                {
                                                    "kind": "contract",
                                                    "contract": "tz1P1jbxpCDGaan94zbjJqU5sqA9u98ntmPf",
                                                    "change": "-976",
                                                    "origin": "block"
                                                },
                                                {
                                                    "kind": "accumulator",
                                                    "category": "block fees",
                                                    "change": "976",
                                                    "origin": "block"
                                                }
                                            ],
                                            "operation_result": {
                                                "status": "applied",
                                                "storage": {
                                                    "prim": "Pair",
                                                    "args": [
                                                        {
                                                            "int": "53931"
                                                        },
                                                        {
                                                            "prim": "Pair",
                                                            "args": [
                                                                {
                                                                    "int": "53932"
                                                                },
                                                                {
                                                                    "int": "53933"
                                                                }
                                                            ]
                                                        }
                                                    ]
                                                },
                                                "big_map_diff": [
                                                    {
                                                        "action": "update",
                                                        "big_map": "53931",
                                                        "key_hash": "exprv7mQJQT4bqEoqQ6zwLe7itEfjonfCMvW4qgrkbw7eeeu862NZ6",
                                                        "key": {
                                                            "prim": "Pair",
                                                            "args": [
                                                                {
                                                                    "bytes": "0000c686e38ffd631a501fd6f74194a907fd0e451457"
                                                                },
                                                                {
                                                                    "int": "0"
                                                                }
                                                            ]
                                                        },
                                                        "value": {
                                                            "int": "10"
                                                        }
                                                    }
                                                ],
                                                "consumed_gas": "6223",
                                                "consumed_milligas": "6222479",
                                                "storage_size": "3485",
                                                "lazy_storage_diff": [
                                                    {
                                                        "kind": "big_map",
                                                        "id": "53933",
                                                        "diff": {
                                                            "action": "update",
                                                            "updates": []
                                                        }
                                                    },
                                                    {
                                                        "kind": "big_map",
                                                        "id": "53932",
                                                        "diff": {
                                                            "action": "update",
                                                            "updates": []
                                                        }
                                                    },
                                                    {
                                                        "kind": "big_map",
                                                        "id": "53931",
                                                        "diff": {
                                                            "action": "update",
                                                            "updates": [
                                                                {
                                                                    "key_hash": "exprv7mQJQT4bqEoqQ6zwLe7itEfjonfCMvW4qgrkbw7eeeu862NZ6",
                                                                    "key": {
                                                                        "prim": "Pair",
                                                                        "args": [
                                                                            {
                                                                                "bytes": "0000c686e38ffd631a501fd6f74194a907fd0e451457"
                                                                            },
                                                                            {
                                                                                "int": "0"
                                                                            }
                                                                        ]
                                                                    },
                                                                    "value": {
                                                                        "int": "10"
                                                                    }
                                                                }
                                                            ]
                                                        }
                                                    }
                                                ]
                                            }
                                        }
                                    }
                                ],
                                "signature": "sigq9pFUczeR9eytgTcw52mQLAQvZURovnjn9R5LTAeefeZDifyM4t2UENmuVkMkSqh4JBEyzFEJXDGrntMYrSQjkjLEAsHs"
                            }
                        ]
                    ]
                }
            """.trimIndent()
        )
}