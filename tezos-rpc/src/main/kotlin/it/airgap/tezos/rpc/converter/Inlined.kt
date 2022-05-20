package it.airgap.tezos.rpc.converter

import it.airgap.tezos.operation.inlined.InlinedEndorsement
import it.airgap.tezos.operation.inlined.InlinedPreendorsement
import it.airgap.tezos.rpc.type.operation.RpcInlinedEndorsement
import it.airgap.tezos.rpc.type.operation.RpcInlinedPreendorsement

// -- InlinedEndorsement -> RpcInlinedEndorsement --

public fun InlinedEndorsement.asRpc(): RpcInlinedEndorsement = RpcInlinedEndorsement(branch, operations.asRpc(), signature)

// -- RpcInlinedEndorsement -> InlinedEndorsement --

public fun RpcInlinedEndorsement.asInlinedEndorsement(): InlinedEndorsement = InlinedEndorsement(branch, operations.asEndorsement(), signature)

// -- InlinedPreendorsement -> RpcInlinedPreendorsement --

public fun InlinedPreendorsement.asRpc(): RpcInlinedPreendorsement = RpcInlinedPreendorsement(branch, operations.asRpc(), signature)

// -- RpcInlinedPreendorsement -> InlinedPreendorsement --

public fun RpcInlinedPreendorsement.asInlinedPreendorsement(): InlinedPreendorsement = InlinedPreendorsement(branch, operations.asPreendorsement(), signature)