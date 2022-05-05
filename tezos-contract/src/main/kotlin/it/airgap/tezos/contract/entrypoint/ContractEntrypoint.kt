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
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.cache.Cached

// -- ContractEntrypoint --

public class ContractEntrypoint internal constructor(
    public val name: String,
    private val contractAddress: ContractHash,
    private val rpc: Block,
    private val metaCached: Cached<MetaContractEntrypoint?>,
) {
    public suspend fun call(
        args: MichelineNode,
        source: ImplicitAddress,
        fee: Mutez? = null,
        headers: List<HttpHeader> = emptyList(),
    ): Operation.Unsigned {
        val branch = rpc.header.get(headers).header.hash
        val counter = rpc.context.contracts(source).counter.get(headers).counter

        return Operation.Unsigned(
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
    }

    public suspend fun call(args: List<Pair<String, MichelineNode>>, headers: List<HttpHeader> = emptyList()): Operation.Unsigned {
        TODO()
    }
}

// -- MetaContractEntrypoint --

internal class MetaContractEntrypoint(type: MichelineNode)