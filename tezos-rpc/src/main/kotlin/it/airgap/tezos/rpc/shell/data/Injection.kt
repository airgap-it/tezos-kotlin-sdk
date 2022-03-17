package it.airgap.tezos.rpc.shell.data

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.OperationHash
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.type.RpcProtocolComponent
import it.airgap.tezos.rpc.type.operation.RpcOperation
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ==== /injection ====

// -- /block --

@Serializable
public data class InjectBlockRequest(
    public val data: @Contextual HexString,
    public val operations: List<List<RpcOperation>>,
)

@Serializable
@JvmInline
public value class InjectBlockResponse(public val hash: @Contextual BlockHash)

// -- /operation --

@Serializable
@JvmInline
public value class InjectOperationRequest(public val data: @Contextual HexString)

@Serializable
@JvmInline
public value class InjectOperationResponse(public val hash: @Contextual OperationHash)

// -- /protocol --

@Serializable
public data class InjectProtocolRequest(
    @SerialName("expected_env_version") public val expectedEnvVersion: UShort,
    public val components: List<RpcProtocolComponent>,
)

@Serializable
@JvmInline
public value class InjectProtocolResponse(public val hash: @Contextual ProtocolHash)