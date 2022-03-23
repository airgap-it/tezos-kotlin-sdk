package it.airgap.tezos.rpc.type.operation

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.SignatureEncoded
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

// -- RpcInlinedEndorsement --

@Serializable
public data class RpcInlinedEndorsement(
    public val branch: @Contextual BlockHash,
    public val operations: RpcOperationContent.Endorsement,
    public val signature: @Contextual SignatureEncoded,
)

// -- RpcInlinedPreendorsement --

@Serializable
public data class RpcInlinedPreendorsement(
    public val branch: @Contextual BlockHash,
    public val operations: RpcOperationContent.Preendorsement,
    public val signature: @Contextual SignatureEncoded,
)
