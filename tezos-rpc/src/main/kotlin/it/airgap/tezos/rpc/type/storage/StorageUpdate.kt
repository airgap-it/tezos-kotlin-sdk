package it.airgap.tezos.rpc.type.storage

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.rpc.type.sapling.RpcSaplingCiphertext
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcStorageBigMapUpdate --

@Serializable
public data class RpcStorageBigMapUpdate(
    @SerialName("key_hash") public val keyHash: @Contextual ScriptExprHash,
    public val key: Micheline,
    public val value: Micheline? = null,
)

// -- RpcStorageStaplingStateUpdate --

@Serializable
public data class RpcStorageSaplingStateUpdate(
    @SerialName("commitments_and_ciphertexts") public val commitmentsAndCiphertexts: List<@Contextual Pair<@Contextual HexString, RpcSaplingCiphertext>>,
    public val nullifiers: List<@Contextual HexString>,
)