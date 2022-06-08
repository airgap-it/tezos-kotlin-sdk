package it.airgap.tezos.rpc.shell.injection

import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.OperationHash
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.type.operation.RpcInjectableOperation
import it.airgap.tezos.rpc.type.protocol.RpcProtocolComponent
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- /injection/block --

/**
 * Request for [`POST /injection/block?[async]&[force]&[chain=<chain_id>]`](https://tezos.gitlab.io/shell/rpc.html#post-injection-block).
 */
@Serializable
public data class InjectBlockRequest(
    public val data: String,
    public val operations: List<List<RpcInjectableOperation>>,
)


/**
 * Response from [`POST /injection/block?[async]&[force]&[chain=<chain_id>]`](https://tezos.gitlab.io/shell/rpc.html#post-injection-block).
 */
@Serializable
@JvmInline
public value class InjectBlockResponse(public val hash: @Contextual BlockHash)

// -- /injection/operation --

/**
 * Request for [`POST /injection/operation?[async]&[chain=<chain_id>]`](https://tezos.gitlab.io/shell/rpc.html#post-injection-operation).
 */
@Serializable
@JvmInline
public value class InjectOperationRequest(public val data: String)

/**
 * Response from [`POST /injection/operation?[async]&[chain=<chain_id>]`](https://tezos.gitlab.io/shell/rpc.html#post-injection-operation).
 */
@Serializable
@JvmInline
public value class InjectOperationResponse(public val hash: @Contextual OperationHash)

// -- /injection/protocol --

/**
 * Request for [`POST /injection/protocol?[async]`](https://tezos.gitlab.io/shell/rpc.html#post-injection-protocol).
 */
@Serializable
public data class InjectProtocolRequest(
    @SerialName("expected_env_version") public val expectedEnvVersion: UShort,
    public val components: List<RpcProtocolComponent>,
)

/**
 * Response from [`POST /injection/protocol?[async]`](https://tezos.gitlab.io/shell/rpc.html#post-injection-protocol).
 */
@Serializable
@JvmInline
public value class InjectProtocolResponse(public val hash: @Contextual ProtocolHash)