package it.airgap.tezos.contract.entrypoint

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.contract.entrypoint.dsl.entrypointParameters
import it.airgap.tezos.contract.internal.converter.EntrypointParameterToMichelineConverter
import it.airgap.tezos.contract.internal.entrypoint.MetaContractEntrypoint
import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.core.type.encoded.Ed25519PublicKeyHash
import it.airgap.tezos.core.type.number.TezosNatural
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.contract.Entrypoint
import it.airgap.tezos.operation.contract.Parameters
import it.airgap.tezos.rpc.TezosRpc
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.internal.cache.Cached
import kotlinx.coroutines.runBlocking
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ContractEntrypointTest {

    private lateinit var tezos: Tezos
    private lateinit var entrypointParameterToMichelineConverter: EntrypointParameterToMichelineConverter

    @MockK
    private lateinit var blockRpc: Block

    @MockK
    private lateinit var tezosRpc: TezosRpc

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tezos = mockTezos()
        entrypointParameterToMichelineConverter = EntrypointParameterToMichelineConverter(
            tezos.michelsonModule.dependencyRegistry.michelineToCompactStringConverter,
            tezos.michelsonModule.dependencyRegistry.stringToMichelsonPrimConverter,
        )
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should create unsigned operation with raw parameters`() {
        val source = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")
        val branch = BlockHash("BLkKavdWXZdfEXmup3FQKCrPJXTrVGfrbLhgBE3UU3SvyztZHHh")
        val counter = "1"

        coEvery { blockRpc.header.get(any()).header.hash } returns branch
        coEvery { blockRpc.context.contracts(any()).counter.get(any()).counter } returns counter
        coEvery { tezosRpc.minFee(any<String>(), any(), any(), any()) } answers { secondArg() }

        entrypointTestCases.forEach { (name, contractAddress, type, value, _) ->
            val contractEntrypoint = ContractEntrypoint(
                name,
                contractAddress,
                blockRpc,
                tezosRpc,
                tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter,
                Cached { MetaContractEntrypoint(type, entrypointParameterToMichelineConverter) },
            )

            val actual = runBlocking { contractEntrypoint.invoke(value, source) }
            val expected = Operation.Unsigned(
                branch,
                listOf(
                    OperationContent.Transaction(
                        source = source,
                        fee = Mutez(0),
                        counter = TezosNatural(counter),
                        gasLimit = TezosNatural(0U),
                        storageLimit = TezosNatural(0U),
                        amount = Mutez(0),
                        destination = contractAddress,
                        parameters = Parameters(
                            Entrypoint(name),
                            value,
                        ),
                    )
                )
            )

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `should create unsigned operation with named parameters`() {
        val source = Ed25519PublicKeyHash("tz1ZSs43ujit1oRsVn67Asz3pTMF8R6CXWPi")
        val branch = BlockHash("BLkKavdWXZdfEXmup3FQKCrPJXTrVGfrbLhgBE3UU3SvyztZHHh")
        val counter = "1"

        coEvery { blockRpc.header.get(any()).header.hash } returns branch
        coEvery { blockRpc.context.contracts(any()).counter.get(any()).counter } returns counter
        coEvery { tezosRpc.minFee(any<String>(), any(), any(), any()) } answers { secondArg() }

        entrypointTestCases.forEach { (name, contractAddress, type, value, namedValue) ->
            val contractEntrypoint = ContractEntrypoint(
                name,
                contractAddress,
                blockRpc,
                tezosRpc,
                tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter,
                Cached { MetaContractEntrypoint(type, entrypointParameterToMichelineConverter) },
            )

            val actual = runBlocking { contractEntrypoint(namedValue, source) }
            val expected = Operation.Unsigned(
                branch,
                listOf(
                    OperationContent.Transaction(
                        source = source,
                        fee = Mutez(0),
                        counter = TezosNatural(counter),
                        gasLimit = TezosNatural(0U),
                        storageLimit = TezosNatural(0U),
                        amount = Mutez(0),
                        destination = contractAddress,
                        parameters = Parameters(
                            Entrypoint(name),
                            value,
                        ),
                    )
                )
            )

            assertEquals(expected, actual)
        }
    }

    private val entrypointTestCases: List<EntrypointTestCase>
        get() = listOf(
            EntrypointTestCase(
                name = "test_entrypoint1",
                contractAddress = ContractHash("KT1ScmSVNZoC73zdn8Vevkit6wzbTr4aXYtc"),
                type = micheline(tezos) {
                    pair {
                        arg {
                            pair {
                                arg {
                                    option {
                                        arg { address }
                                        annots("%address")
                                    }
                                }
                                arg {
                                    pair {
                                        arg { bytes annots "%label" }
                                        arg { address annots "%owner" }
                                    }
                                }
                            }
                        }
                        arg {
                            option {
                                arg {
                                    pair {
                                        arg { bytes annots "%parent" }
                                        arg { nat annots "%ttl" }
                                    }
                                }
                            }
                        }
                    }
                },
                value = micheline(tezos) {
                    Pair {
                        arg {
                            Pair {
                                arg {
                                    Some {
                                        arg { string("tz1gru9Tsz1X7GaYnsKR2YeGJLTVm4NwMhvb") }
                                    }
                                }
                                arg {
                                    Pair {
                                        arg { bytes("0000a7848de3b1fce76a7ffce2c7ce40e46be33aed7c") }
                                        arg { string("tz1b6wRXMA2PxATL6aoVGy9j7kSqXijW7VPq") }
                                    }
                                }
                            }
                        }
                        arg {
                            Some {
                                arg {
                                    Pair {
                                        arg { bytes("0b51b8ae90e19a079c9db469c4881871a5ba7778acf9773ac00c7dfcda0b1c87") }
                                        arg { int(1) }
                                    }
                                }
                            }
                        }
                    }
                },
                namedValue = ContractEntrypointParameter.Object(
                    ContractEntrypointParameter.Value(MichelineLiteral.String("tz1gru9Tsz1X7GaYnsKR2YeGJLTVm4NwMhvb"), "%address"),
                    ContractEntrypointParameter.Value(MichelineLiteral.Bytes("0x0000a7848de3b1fce76a7ffce2c7ce40e46be33aed7c"), "%label"),
                    ContractEntrypointParameter.Value(MichelineLiteral.String("tz1b6wRXMA2PxATL6aoVGy9j7kSqXijW7VPq"), "%owner"),
                    ContractEntrypointParameter.Value(MichelineLiteral.Bytes("0x0b51b8ae90e19a079c9db469c4881871a5ba7778acf9773ac00c7dfcda0b1c87"), "%parent"),
                    ContractEntrypointParameter.Value(MichelineLiteral.Integer(1), "%ttl"),
                )
            ),
            EntrypointTestCase(
                name = "test_entrypoint2",
                contractAddress = ContractHash("KT1ScmSVNZoC73zdn8Vevkit6wzbTr4aXYtc"),
                type = micheline(tezos) {
                    pair {
                        arg {
                            pair {
                                arg {
                                    pair {
                                        arg {
                                            option {
                                                arg { address }
                                                annots("%address")
                                            }
                                        }
                                        arg {
                                            map {
                                                key { string }
                                                value {
                                                    or {
                                                        lhs {
                                                            or {
                                                                lhs {
                                                                    or {
                                                                        lhs {
                                                                            or {
                                                                                lhs { address annots "%address" }
                                                                                rhs { bool annots "%bool" }
                                                                            }
                                                                        }
                                                                        rhs {
                                                                            or {
                                                                                lhs { bytes annots "%bytes" }
                                                                                rhs { int annots "%int" }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                rhs {
                                                                    or {
                                                                        lhs {
                                                                            or {
                                                                                lhs { key annots "%key" }
                                                                                rhs { keyHash annots "%key_hash" }
                                                                            }
                                                                        }
                                                                        rhs {
                                                                            or {
                                                                                lhs { nat annots "%nat" }
                                                                                rhs { signature annots "%signature" }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        rhs {
                                                            or {
                                                                lhs {
                                                                    or {
                                                                        lhs { string annots "%string" }
                                                                        rhs { mutez annots "%tez" }
                                                                    }
                                                                }
                                                                rhs { timestamp annots "%timestamp" }
                                                            }
                                                        }
                                                    }
                                                }
                                                annots("%data")
                                            }
                                        }
                                    }
                                }
                                arg {
                                    pair {
                                        arg { bytes annots "%label" }
                                        arg { address annots "%owner" }
                                    }
                                }
                            }
                        }
                        arg {
                            option {
                                arg {
                                    pair {
                                        arg { bytes annots "%parent" }
                                        arg { nat annots "%ttl" }
                                    }
                                }
                            }
                        }
                    }
                },
                value = micheline(tezos) {
                    Pair {
                        arg {
                            Pair {
                                arg {
                                    Pair {
                                        arg{
                                            Some {
                                                arg { string("tz1gru9Tsz1X7GaYnsKR2YeGJLTVm4NwMhvb") }
                                            }
                                        }
                                        arg {
                                            sequence {
                                                Elt {
                                                    key { string("key1") }
                                                    value {
                                                        Left {
                                                            arg {
                                                                Left {
                                                                    arg {
                                                                        Left {
                                                                            arg {
                                                                                Right {
                                                                                    arg { True }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                Elt {
                                                    key { string("key2") }
                                                    value {
                                                        Left {
                                                            arg {
                                                                Right {
                                                                    arg {
                                                                        Right {
                                                                            arg {
                                                                                Left {
                                                                                    arg { int(1) }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                arg {
                                    Pair {
                                        arg { bytes("0000a7848de3b1fce76a7ffce2c7ce40e46be33aed7c") }
                                        arg { string("tz1b6wRXMA2PxATL6aoVGy9j7kSqXijW7VPq") }
                                    }
                                }
                            }
                        }
                        arg {
                            Some {
                                arg {
                                    Pair {
                                        arg { bytes("0b51b8ae90e19a079c9db469c4881871a5ba7778acf9773ac00c7dfcda0b1c87") }
                                        arg { int(2) }
                                    }
                                }
                            }
                        }
                    }
                },
                namedValue = entrypointParameters(tezos) {
                    value("%address") { string("tz1gru9Tsz1X7GaYnsKR2YeGJLTVm4NwMhvb") }
                    map("%data") {
                        key { string("key1") } pointsTo value("%bool") { True }
                        key { string("key2") } pointsTo value("%nat") { int(1) }
                    }
                    value("%label") { bytes("0x0000a7848de3b1fce76a7ffce2c7ce40e46be33aed7c") }
                    value("%owner") { string("tz1b6wRXMA2PxATL6aoVGy9j7kSqXijW7VPq") }
                    value("%parent") { bytes("0x0b51b8ae90e19a079c9db469c4881871a5ba7778acf9773ac00c7dfcda0b1c87") }
                    value("%ttl") { int(2) }
                }
            ),
            EntrypointTestCase(
                name = "test_entrypoint3",
                contractAddress = ContractHash("KT1ScmSVNZoC73zdn8Vevkit6wzbTr4aXYtc"),
                type = micheline(tezos) { nat annots "%current_version" },
                value = micheline(tezos) { int(1) },
                namedValue = entrypointParameters(tezos) {
                    value("%current_version") { int(1) }
                }
            ),
            EntrypointTestCase(
                name = "test_entrypoint4",
                contractAddress = ContractHash("KT1ScmSVNZoC73zdn8Vevkit6wzbTr4aXYtc"),
                type = micheline(tezos) {
                    pair {
                        arg { nat annots "%value" }
                        arg {
                            pair {
                                arg { nat }
                                arg { nat }
                                annots("%data")
                            }
                        }
                    }
                },
                value = micheline(tezos) {
                     Pair {
                         arg { int(1) }
                         arg {
                             Pair {
                                 arg { int(2) }
                                 arg { int(3) }
                             }
                         }
                     }
                },
                namedValue = entrypointParameters(tezos) {
                    value("%value") { int(1) }
                    `object`("%data") {
                        value { int(2) }
                        value { int(3) }
                    }
                }
            ),
            EntrypointTestCase(
                name = "test_entrypoint5",
                contractAddress = ContractHash("KT1ScmSVNZoC73zdn8Vevkit6wzbTr4aXYtc"),
                type = micheline(tezos) {
                    list {
                        arg {
                            pair {
                                arg { nat annots "%first" }
                                arg { nat annots "%second" }
                            }
                        }
                    }
                },
                value = micheline(tezos) {
                    sequence {
                        Pair {
                            arg { int(1) }
                            arg { int(2) }
                        }
                        Pair {
                            arg { int(3) }
                            arg { int(4) }
                        }
                    }
                },
                namedValue = entrypointParameters(tezos) {
                    sequence {
                        `object` {
                            value("%first") { int(1) }
                            value("%second") { int(2) }
                        }
                        `object` {
                            value("%first") { int(3) }
                            value("%second") { int(4) }
                        }
                    }
                }
            ),
            EntrypointTestCase(
                name = "test_entrypoint6",
                contractAddress = ContractHash("KT1ScmSVNZoC73zdn8Vevkit6wzbTr4aXYtc"),
                type = micheline(tezos) {
                    map {
                        key { string }
                        value { nat }
                    }
                },
                value = micheline(tezos) {
                     sequence {
                         Elt {
                             key { string("first") }
                             value { int(1) }
                         }
                         Elt {
                             key { string("second") }
                             value { int(2) }
                         }
                     }
                },
                namedValue = entrypointParameters(tezos) {
                    map {
                        key { string("first") } pointsTo value { int(1) }
                        key { string("second") } pointsTo value { int(2) }
                    }
                }
            ),
        )

    private data class EntrypointTestCase(
        val name: String,
        val contractAddress: ContractHash,
        val type: Micheline,
        val value: Micheline,
        val namedValue: ContractEntrypointParameter,
    )
}