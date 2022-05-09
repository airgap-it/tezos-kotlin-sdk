package it.airgap.tezos.contract.entrypoint

import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.zarith.ZarithNatural
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.contract.Entrypoint
import it.airgap.tezos.operation.contract.Parameters
import it.airgap.tezos.rpc.TezosRpc
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.cache.Cached

// -- ContractEntrypoint --

public class ContractEntrypoint internal constructor(
    public val name: String,
    private val contractAddress: ContractHash,
    private val block: Block,
    private val rpc: TezosRpc, // TODO: extract fee calculation logic to a separate class and use it here instead
    private val metaCached: Cached<MetaContractEntrypoint?>,
) {

    public suspend fun call(
        args: MichelineNode,
        source: ImplicitAddress,
        fee: Mutez? = null,
        headers: List<HttpHeader> = emptyList(),
    ): Operation.Unsigned {
        val branch = block.header.get(headers).header.hash
        val counter = block.context.contracts(source).counter.get(headers).counter

        val operation = Operation.Unsigned(
            branch,
            listOf(
                OperationContent.Transaction(
                    source = source,
                    fee = fee ?: Mutez(0U),
                    counter = counter?.let { ZarithNatural(it) } ?: ZarithNatural(0U),
                    gasLimit = ZarithNatural(0U),
                    storageLimit = ZarithNatural(0U),
                    amount = Mutez(0U),
                    destination = contractAddress,
                    parameters = Parameters(
                        Entrypoint.fromString(name),
                        args,
                    ),
                ),
            ),
        )

        return rpc.minFee(operation = operation, headers = headers).asUnsigned()
    }

    public suspend fun call(
        args: List<Pair<String, MichelineNode>>,
        source: ImplicitAddress,
        fee: Mutez? = null,
        headers: List<HttpHeader> = emptyList(),
    ): Operation.Unsigned {
        TODO()
    }

    private fun Operation.asUnsigned(): Operation.Unsigned =
        when (this) {
            is Operation.Unsigned -> this
            is Operation.Signed -> Operation.Unsigned(branch, contents)
        }
}

// -- MetaContractEntrypoint --

internal class MetaContractEntrypoint(type: MichelineNode)