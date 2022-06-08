package it.airgap.tezos.rpc.converter

import it.airgap.tezos.operation.header.BlockHeader
import it.airgap.tezos.rpc.internal.context.TezosRpcContext.asHexString
import it.airgap.tezos.rpc.type.block.RpcBlockHeader

/**
 * Converts this [block header][BlockHeader] to [its RPC equivalent][RpcBlockHeader].
 */
public fun BlockHeader.asRpc(): RpcBlockHeader =
    RpcBlockHeader(
        level,
        proto,
        predecessor,
        timestamp,
        validationPass,
        operationsHash,
        fitness.map { it.asString() },
        context,
        payloadHash,
        payloadRound,
        proofOfWorkNonce.asString(),
        seedNonceHash,
        liquidityBakingEscapeVote,
        signature,
    )

/**
 * Converts this [RPC block header][RpcBlockHeader] to [its non-RPC equivalent][BlockHeader].
 */
public fun RpcBlockHeader.asBlockHeader(): BlockHeader =
    BlockHeader(
        level,
        proto,
        predecessor,
        timestamp,
        validationPass,
        operationsHash,
        fitness.map { it.asHexString() },
        context,
        payloadHash,
        payloadRound,
        proofOfWorkNonce.asHexString(),
        seedNonceHash,
        liquidityBakingEscapeVote,
        signature,
    )