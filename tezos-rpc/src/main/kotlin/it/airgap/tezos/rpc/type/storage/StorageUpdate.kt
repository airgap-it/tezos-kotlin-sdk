package it.airgap.tezos.rpc.type.storage

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.rpc.type.sapling.RpcSaplingCiphertext
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

// -- RpcStorageBigMapUpdate --

@Serializable
public data class RpcStorageBigMapUpdate(
    public val keyHash: @Contextual ScriptExprHash,
    public val key: MichelineNode,
    public val value: MichelineNode? = null,
)

// -- RpcStorageStaplingStateUpdate --

@Serializable
public data class RpcStorageStaplingStateUpdate(
    public val commitmentsAndCiphertexts: List<Pair<@Contextual HexString, RpcSaplingCiphertext>>,
    public val nullifiers: List<@Contextual HexString>,
)