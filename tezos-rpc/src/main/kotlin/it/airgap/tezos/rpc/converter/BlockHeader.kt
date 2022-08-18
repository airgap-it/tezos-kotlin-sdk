package it.airgap.tezos.rpc.converter

import it.airgap.tezos.operation.header.BlockHeader
import it.airgap.tezos.operation.header.LiquidityBakingToggleVote
import it.airgap.tezos.rpc.internal.context.TezosRpcContext.asHexString
import it.airgap.tezos.rpc.type.block.RpcBlockHeader
import it.airgap.tezos.rpc.type.block.RpcLiquidityBakingToggleVote

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
        liquidityBakingToggleVote.asRpc(),
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
        liquidityBakingToggleVote.asLiquidityBakingToggleVote(),
        signature,
    )

private fun LiquidityBakingToggleVote.asRpc(): RpcLiquidityBakingToggleVote =
    when (this) {
        LiquidityBakingToggleVote.Off -> RpcLiquidityBakingToggleVote.Off
        LiquidityBakingToggleVote.On -> RpcLiquidityBakingToggleVote.On
        LiquidityBakingToggleVote.Pass -> RpcLiquidityBakingToggleVote.Pass
    }

private fun RpcLiquidityBakingToggleVote.asLiquidityBakingToggleVote(): LiquidityBakingToggleVote =
    when (this) {
        RpcLiquidityBakingToggleVote.Off -> LiquidityBakingToggleVote.Off
        RpcLiquidityBakingToggleVote.On -> LiquidityBakingToggleVote.On
        RpcLiquidityBakingToggleVote.Pass -> LiquidityBakingToggleVote.Pass
    }