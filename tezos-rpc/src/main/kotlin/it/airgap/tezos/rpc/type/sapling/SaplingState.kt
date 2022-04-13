package it.airgap.tezos.rpc.type.sapling

import it.airgap.tezos.core.type.HexString
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcSaplingStateDiff --

@Serializable
public data class RpcSaplingStateDiff(
    public val root: @Contextual HexString,
    @SerialName("commitments_and_ciphertexts") public val commitmentsAndCiphertexts: List<@Contextual Pair<@Contextual HexString, RpcSaplingCiphertext>>,
    public val nullifiers: List<@Contextual HexString>,
)