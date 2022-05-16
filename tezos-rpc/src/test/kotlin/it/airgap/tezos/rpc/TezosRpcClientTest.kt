package it.airgap.tezos.rpc

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.internal.base58.Base58
import it.airgap.tezos.core.internal.base58.Base58Check
import it.airgap.tezos.core.internal.coder.encoded.*
import it.airgap.tezos.core.internal.coder.tez.MutezBytesCoder
import it.airgap.tezos.core.internal.coder.timestamp.TimestampBigIntCoder
import it.airgap.tezos.core.internal.coder.zarith.ZarithIntegerBytesCoder
import it.airgap.tezos.core.internal.coder.zarith.ZarithNaturalBytesCoder
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.Ed25519PublicKeyHash
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.zarith.ZarithNatural
import it.airgap.tezos.michelson.internal.coder.MichelineBytesCoder
import it.airgap.tezos.michelson.internal.converter.MichelineToCompactStringConverter
import it.airgap.tezos.michelson.internal.converter.StringToMichelsonPrimConverter
import it.airgap.tezos.michelson.internal.converter.TagToMichelsonPrimConverter
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.fee
import it.airgap.tezos.operation.internal.coder.OperationContentBytesCoder
import it.airgap.tezos.operation.internal.converter.TagToOperationContentKindConverter
import it.airgap.tezos.operation.limits
import it.airgap.tezos.operation.type.OperationLimits
import it.airgap.tezos.rpc.active.ActiveSimplifiedRpc
import it.airgap.tezos.rpc.active.block.RunOperationResponse
import it.airgap.tezos.rpc.shell.ShellSimplifiedRpc
import it.airgap.tezos.rpc.shell.chains.Chains
import it.airgap.tezos.rpc.shell.config.Config
import it.airgap.tezos.rpc.shell.injection.Injection
import it.airgap.tezos.rpc.shell.monitor.Monitor
import it.airgap.tezos.rpc.shell.network.Network
import it.airgap.tezos.rpc.type.operation.RpcBalanceUpdate
import it.airgap.tezos.rpc.type.operation.RpcOperationContent
import it.airgap.tezos.rpc.type.operation.RpcOperationMetadata
import it.airgap.tezos.rpc.type.operation.RpcOperationResult
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.security.MessageDigest
import kotlin.test.assertEquals

class TezosRpcClientTest {

    @MockK
    private lateinit var shellRpc: ShellSimplifiedRpc

    @MockK
    private lateinit var activeRpc: ActiveSimplifiedRpc

    @MockK
    private lateinit var chains: Chains

    @MockK
    private lateinit var config: Config

    @MockK
    private lateinit var injection: Injection

    @MockK
    private lateinit var monitor: Monitor

    @MockK
    private lateinit var network: Network

    @MockK
    private lateinit var crypto: Crypto

    private lateinit var tezosRpcClient: TezosRpcClient

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { crypto.hashSha256(any<ByteArray>()) } answers {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.digest(firstArg())
        }

        val base58Check = Base58Check(Base58(), crypto)
        val encodedBytesCoder = EncodedBytesCoder(base58Check)
        val implicitAddressBytesCoder = ImplicitAddressBytesCoder(encodedBytesCoder)
        val publicKeyBytesCoder = PublicKeyBytesCoder(encodedBytesCoder)
        val signatureBytesCoder = SignatureBytesCoder(encodedBytesCoder)
        val addressBytesCoder = AddressBytesCoder(implicitAddressBytesCoder, encodedBytesCoder)
        val zarithNaturalBytesCoder = ZarithNaturalBytesCoder()
        val mutezBytesCoder = MutezBytesCoder(zarithNaturalBytesCoder)
        val michelineBytesCoder = MichelineBytesCoder(
            StringToMichelsonPrimConverter(),
            TagToMichelsonPrimConverter(),
            MichelineToCompactStringConverter(),
            ZarithIntegerBytesCoder(zarithNaturalBytesCoder),
        )

        val timestampBigIntCoder = TimestampBigIntCoder()
        val tagToOperationContentKindConverter = TagToOperationContentKindConverter()

        val operationContentBytesCoder = OperationContentBytesCoder(
            encodedBytesCoder,
            addressBytesCoder,
            publicKeyBytesCoder,
            implicitAddressBytesCoder,
            signatureBytesCoder,
            zarithNaturalBytesCoder,
            mutezBytesCoder,
            michelineBytesCoder,
            timestampBigIntCoder,
            tagToOperationContentKindConverter,
        )

        tezosRpcClient = TezosRpcClient(shellRpc, activeRpc, chains, config, injection, monitor, network, operationContentBytesCoder)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should calculate minimum fee with default limits`() {
        val chainId = ChainId("NetXdQprcVkpaWU")
        val branch = BlockHash("BKuka2aVwcjNkZrDzFHJMvdCz43RoMt1kFfjKnipNnGsERSAUEn")

        val operation = Operation(
            OperationContent.Transaction(
                source = Ed25519PublicKeyHash("tz1gru9Tsz1X7GaYnsKR2YeGJLTVm4NwMhvb"),
                fee = Mutez(0U),
                counter = ZarithNatural(8680641U),
                gasLimit = ZarithNatural(1030000U),
                storageLimit = ZarithNatural(50000U),
                amount = Mutez(1000U),
                destination = Ed25519PublicKeyHash("tz1gru9Tsz1X7GaYnsKR2YeGJLTVm4NwMhvb"),
            ),
            branch = branch,
        )

        coEvery { chains(any<String>()).blocks.head.helpers.scripts.runOperation.post(any(), any()) } returns RunOperationResponse(
            listOf(
                RpcOperationContent.Transaction(
                    source = Ed25519PublicKeyHash("tz1gru9Tsz1X7GaYnsKR2YeGJLTVm4NwMhvb"),
                    fee = Mutez(0U),
                    counter = "8680641",
                    gasLimit = "1040000",
                    storageLimit = "60000",
                    amount = Mutez(1000U),
                    destination = Ed25519PublicKeyHash("tz1gru9Tsz1X7GaYnsKR2YeGJLTVm4NwMhvb"),
                    metadata = RpcOperationMetadata.Transaction(
                        balanceUpdates = emptyList(),
                        operationResult = RpcOperationResult.Transaction.Applied(
                            balanceUpdates = listOf(
                                RpcBalanceUpdate.Contract(
                                    contract = Ed25519PublicKeyHash("tz1gru9Tsz1X7GaYnsKR2YeGJLTVm4NwMhvb"),
                                    change = -1000,
                                    origin = RpcBalanceUpdate.Origin.Block,
                                ),
                                RpcBalanceUpdate.Contract(
                                    contract = Ed25519PublicKeyHash("tz1gru9Tsz1X7GaYnsKR2YeGJLTVm4NwMhvb"),
                                    change = 1000,
                                    origin = RpcBalanceUpdate.Origin.Block,
                                ),
                            ),
                            consumedGas = "1421",
                            consumedMilligas = "1420040",
                        ),
                    ),
                )
            )
        )

        val updatedOperation = runBlocking { tezosRpcClient.minFee(chainId, operation) }

        assertEquals(Mutez(507U), updatedOperation.fee)
        assertEquals(
            OperationLimits(
                gas = BigInt.valueOf(1521),
                storage = BigInt.valueOf(100),
            ),
            updatedOperation.limits,
        )
    }
}