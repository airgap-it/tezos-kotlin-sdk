package it.airgap.tezos.rpc.type.delegate

import it.airgap.tezos.core.type.encoded.PublicKey
import it.airgap.tezos.rpc.internal.serializer.RpcDelegateSelectionSerializer
import it.airgap.tezos.rpc.internal.serializer.RpcRandomDelegateSelectionSerializer
import it.airgap.tezos.rpc.internal.serializer.RpcRoundRobinOverDelegateSelectionSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

// -- RpcDelegateSelection --

@Serializable(with = RpcDelegateSelectionSerializer::class)
public sealed class RpcDelegateSelection {

    @Serializable(with = RpcRandomDelegateSelectionSerializer::class)
    public object Random : RpcDelegateSelection()

    @Serializable(with = RpcRoundRobinOverDelegateSelectionSerializer::class)
    public data class RoundRobinOver(public val publicKeys: List<List<@Contextual PublicKey>>) : RpcDelegateSelection()
}