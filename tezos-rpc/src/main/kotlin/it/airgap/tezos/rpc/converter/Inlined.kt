package it.airgap.tezos.rpc.converter

import it.airgap.tezos.operation.inlined.InlinedEndorsement
import it.airgap.tezos.operation.inlined.InlinedPreendorsement
import it.airgap.tezos.rpc.type.operation.RpcInlinedEndorsement
import it.airgap.tezos.rpc.type.operation.RpcInlinedPreendorsement

/**
 * Converts this [inlined endorsement][InlinedEndorsement] to [its RPC equivalent][RpcInlinedEndorsement].
 */
public fun InlinedEndorsement.asRpc(): RpcInlinedEndorsement = RpcInlinedEndorsement(branch, operations.asRpc(), signature)

/**
 * Converts this [RPC inlined endorsement][RpcInlinedEndorsement] to [its non-RPC equivalent][InlinedEndorsement].
 */
public fun RpcInlinedEndorsement.asInlinedEndorsement(): InlinedEndorsement = InlinedEndorsement(branch, operations.asEndorsement(), signature)

/**
 * Converts this [inlined preendorsement][InlinedPreendorsement] to [its RPC equivalent][RpcInlinedPreendorsement].
 */
public fun InlinedPreendorsement.asRpc(): RpcInlinedPreendorsement = RpcInlinedPreendorsement(branch, operations.asRpc(), signature)

/**
 * Converts this [RPC inlined preendorsement][RpcInlinedPreendorsement] to [its non-RPC equivalent][InlinedPreendorsement].
 */
public fun RpcInlinedPreendorsement.asInlinedPreendorsement(): InlinedPreendorsement = InlinedPreendorsement(branch, operations.asPreendorsement(), signature)