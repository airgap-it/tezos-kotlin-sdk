package it.airgap.tezos.contract.storage

import io.mockk.*
import io.mockk.impl.annotations.MockK
import it.airgap.tezos.contract.internal.converter.MichelineToStorageEntryConverter
import it.airgap.tezos.contract.internal.storage.MetaContractStorage
import it.airgap.tezos.contract.internal.storage.MetaContractStorageEntry
import it.airgap.tezos.core.internal.base58.Base58
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.coder.encoded.*
import it.airgap.tezos.core.internal.coder.timestamp.TimestampBigIntCoder
import it.airgap.tezos.core.internal.coder.zarith.ZarithIntegerBytesCoder
import it.airgap.tezos.core.internal.coder.zarith.ZarithNaturalBytesCoder
import it.airgap.tezos.core.internal.converter.encoded.StringToAddressConverter
import it.airgap.tezos.core.internal.converter.encoded.StringToImplicitAddressConverter
import it.airgap.tezos.core.internal.converter.encoded.StringToPublicKeyConverter
import it.airgap.tezos.core.internal.converter.encoded.StringToSignatureConverter
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.michelson.internal.coder.MichelineBytesCoder
import it.airgap.tezos.michelson.internal.converter.*
import it.airgap.tezos.michelson.internal.packer.MichelinePacker
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*
import it.airgap.tezos.michelson.micheline.dsl.micheline
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.active.block.GetContractNormalizedStorageResponse
import it.airgap.tezos.rpc.internal.cache.Cached
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertEquals

class ContractStorageTest {

    private lateinit var michelsonToMichelineConverter: MichelsonToMichelineConverter
    private lateinit var michelineToStorageEntryConverter: MichelineToStorageEntryConverter

    @MockK
    private lateinit var crypto: Crypto

    @MockK
    private lateinit var blockRpc: Block

    @MockK
    private lateinit var contractRpc: Block.Context.Contracts.Contract

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { crypto.hashSha256(any<ByteArray>()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        val base58Check = Base58Check(Base58(), crypto)

        val stringToMichelsonPrimConverter = StringToMichelsonPrimConverter()
        val michelineToNormalizedConverter = MichelineToNormalizedConverter()
        val michelinePrimitiveApplicationToNormalizedConverter = MichelinePrimitiveApplicationToNormalizedConverter(michelineToNormalizedConverter)
        val tagToMichelsonPrimConverter = TagToMichelsonPrimConverter()
        val michelineToCompactStringConverter = MichelineToCompactStringConverter()

        val encodedBytesCoder = EncodedBytesCoder(base58Check)
        val implicitAddressBytesCoder = ImplicitAddressBytesCoder(encodedBytesCoder)
        val publicKeyBytesCoder = PublicKeyBytesCoder(encodedBytesCoder)
        val signatureBytesCoder = SignatureBytesCoder(encodedBytesCoder)
        val timestampBigIntCoder = TimestampBigIntCoder()
        val addressBytesCoder = AddressBytesCoder(implicitAddressBytesCoder, encodedBytesCoder)
        val zarithIntegerBytesCoder = ZarithIntegerBytesCoder(ZarithNaturalBytesCoder())

        val stringToAddressConverter = StringToAddressConverter()
        val stringToImplicitAddressConverter = StringToImplicitAddressConverter()
        val stringToPublicKeyConverter = StringToPublicKeyConverter()
        val stringToSignatureConverter = StringToSignatureConverter()

        val michelineBytesCoder = MichelineBytesCoder(stringToMichelsonPrimConverter, tagToMichelsonPrimConverter, michelineToCompactStringConverter, zarithIntegerBytesCoder)

        val michelinePacker = MichelinePacker(
            michelineBytesCoder,
            stringToMichelsonPrimConverter,
            michelinePrimitiveApplicationToNormalizedConverter,
            michelineToCompactStringConverter,
            encodedBytesCoder,
            addressBytesCoder,
            publicKeyBytesCoder,
            implicitAddressBytesCoder,
            signatureBytesCoder,
            timestampBigIntCoder,
            stringToAddressConverter,
            stringToImplicitAddressConverter,
            stringToPublicKeyConverter,
            stringToSignatureConverter,
        )

        michelsonToMichelineConverter = MichelsonToMichelineConverter()
        michelineToStorageEntryConverter = MichelineToStorageEntryConverter(
            blockRpc,
            encodedBytesCoder,
            michelinePacker,
            michelineToCompactStringConverter,
            stringToMichelsonPrimConverter,
        )
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should create Map from storage values`() {
        every { blockRpc.context.bigMaps } returns mockk()

        storageTestCases.forEach { (type, value, expected) ->
            coEvery { contractRpc.storage.normalized.post(any(), any()) } returns GetContractNormalizedStorageResponse(value)

            val contractStorage = ContractStorage(
                Cached { MetaContractStorage(type, michelineToStorageEntryConverter) },
                contractRpc,
            )

            val actual = runBlocking { contractStorage.get() }

            assertEquals(expected, actual)
        }
    }

    private val storageTestCases: List<StorageTestCase>
        get() = listOf(
            StorageTestCase {
                val type = micheline(michelsonToMichelineConverter) {
                    pair {
                        arg {
                            bigMap {
                                key { address }
                                value { nat annots ":balance" }
                                annots("%ledger")
                            }
                        }
                        arg {
                            pair {
                                arg {
                                    bigMap {
                                        key {
                                            pair {
                                                arg { address annots ":owner" }
                                                arg { address annots ":spender" }
                                            }
                                        }
                                        value { nat }
                                        annots("%approvals")
                                    }
                                }
                                arg {
                                    pair {
                                        arg { address annots "%admin" }
                                        arg {
                                            pair {
                                                arg { bool annots "%paused" }
                                                arg { nat annots "%totalSupply" }
                                            }
                                        }
                                        annots("%fields")
                                    }
                                }
                            }
                        }

                    }
                }

                val value = micheline(michelsonToMichelineConverter) {
                    Pair {
                        arg { int(51296) }
                        arg {
                            Pair {
                                arg { int(51297) }
                                arg {
                                    Pair {
                                        arg { bytes("0000a7848de3b1fce76a7ffce2c7ce40e46be33aed7c") }
                                        arg {
                                            Pair {
                                                arg { True }
                                                arg { int(20) }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                val entry = ContractStorageEntry.Object(
                    value,
                    MetaContractStorageEntry.Basic(type),
                    listOf(
                        ContractStorageEntry.BigMap(
                            "51296",
                            value.args[0],
                            MetaContractStorageEntry.BigMap(
                                type.args[0],
                                mockk(),
                                mockk(),
                                mockk(),
                                mockk(),
                            ),
                            mockk(),
                        ),
                        ContractStorageEntry.BigMap(
                            "51297",
                            value.args[1].args[0],
                            MetaContractStorageEntry.BigMap(
                                type.args[1].args[0],
                                mockk(),
                                mockk(),
                                mockk(),
                                mockk(),
                            ),
                            mockk(),
                        ),
                        ContractStorageEntry.Object(
                            value.args[1].args[1],
                            MetaContractStorageEntry.Basic(type.args[1].args[1]),
                            listOf(
                                ContractStorageEntry.Value(
                                    value.args[1].args[1].args[0],
                                    MetaContractStorageEntry.Basic(type.args[1].args[1].args[0]),
                                ),
                                ContractStorageEntry.Value(
                                    value.args[1].args[1].args[1].args[0],
                                     MetaContractStorageEntry.Basic(type.args[1].args[1].args[1].args[0]),
                                ),
                                ContractStorageEntry.Value(
                                    value.args[1].args[1].args[1].args[1],
                                     MetaContractStorageEntry.Basic(type.args[1].args[1].args[1].args[1]),
                                ),
                            ),
                        ),
                    ),
                )

                Triple(type, value, entry)
            }
        )

    private data class StorageTestCase(
        val type: MichelineNode,
        val value: MichelineNode,
        val entry: ContractStorageEntry,
    )

    private fun StorageTestCase(builder: () -> Triple<MichelineNode, MichelineNode, ContractStorageEntry>): StorageTestCase {
        val (type, value, entry) = builder()
        return StorageTestCase(type, value, entry)
    }

    private val MichelineNode.args: List<MichelineNode>
        get() = if (this is MichelinePrimitiveApplication) args else emptyList()
}