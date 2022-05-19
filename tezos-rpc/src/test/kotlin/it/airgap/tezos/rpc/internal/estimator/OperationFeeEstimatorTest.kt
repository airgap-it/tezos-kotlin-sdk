package it.airgap.tezos.rpc.internal.estimator

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.Ed25519PublicKeyHash
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.zarith.ZarithNatural
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.internal.operationModule
import it.airgap.tezos.rpc.active.block.RunOperationResponse
import it.airgap.tezos.rpc.internal.type.OperationLimits
import it.airgap.tezos.rpc.internal.utils.fee
import it.airgap.tezos.rpc.internal.utils.limits
import it.airgap.tezos.rpc.shell.chains.Chains
import it.airgap.tezos.rpc.type.operation.RpcBalanceUpdate
import it.airgap.tezos.rpc.type.operation.RpcOperationContent
import it.airgap.tezos.rpc.type.operation.RpcOperationMetadata
import it.airgap.tezos.rpc.type.operation.RpcOperationResult
import kotlinx.coroutines.runBlocking
import mockTezos
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class TezosRpcClientTest {

    @MockK
    private lateinit var chains: Chains

    private lateinit var operationFeeEstimator: OperationFeeEstimator

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        val tezos = mockTezos()
        operationFeeEstimator = OperationFeeEstimator(chains, tezos.operationModule.dependencyRegistry.operationContentBytesCoder)
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

        val updatedOperation = runBlocking { operationFeeEstimator.minFee(chainId, operation) }

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