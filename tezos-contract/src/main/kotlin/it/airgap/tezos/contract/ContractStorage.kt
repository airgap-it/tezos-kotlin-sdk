package it.airgap.tezos.contract

import it.airgap.tezos.rpc.active.block.Block

// -- ContractStorage --

public class ContractStorage internal constructor(private val type: MetaContractStorage, private val rpc: Block.Context.Contracts.Contract) {

}

// -- MetaContractStorage --

internal class MetaContractStorage {}