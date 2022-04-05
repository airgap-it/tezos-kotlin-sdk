package it.airgap.tezos.rpc.type

import it.airgap.tezos.rpc.internal.serializer.RpcErrorKindSerializer
import it.airgap.tezos.rpc.internal.serializer.RpcErrorSerializer
import kotlinx.serialization.Serializable

// -- RpcError --

@Serializable(with = RpcErrorSerializer::class)
public data class RpcError(
    public val kind: Kind,
    public val id: String,
    public val details: Map<String, String>? = null,
) {

    @Serializable(RpcErrorKindSerializer::class)
    public sealed class Kind {
        public object Branch : Kind()
        public object Temporary : Kind()
        public object Permanent : Kind()
        public data class Unknown(public val name: String) : Kind()
    }
}