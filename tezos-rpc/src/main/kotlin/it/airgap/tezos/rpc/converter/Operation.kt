package it.airgap.tezos.rpc.converter

import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.rpc.internal.context.TezosRpcContext.asHexString
import it.airgap.tezos.rpc.internal.context.TezosRpcContext.asTezosNatural
import it.airgap.tezos.rpc.internal.utils.isSigned
import it.airgap.tezos.rpc.internal.utils.signatureOrPlaceholder
import it.airgap.tezos.rpc.type.operation.RpcOperationContent
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

/**
 * Converts this [operation][Operation] to [its RPC runnable equivalent][RpcRunnableOperation].
 */
public fun Operation.asRunnable(chainId: ChainId): RpcRunnableOperation =
    RpcRunnableOperation(
        branch,
        contents.map { it.asRpc() },
        signatureOrPlaceholder,
        chainId,
    )

/**
 * Converts this [RPC runnable operation][RpcRunnableOperation] to [its non-RPC and non-runnable equivalent][Operation].
 */
public fun RpcRunnableOperation.asOperation(): Operation =
    Operation(
        contents.map { it.asOperationContent() },
        branch,
        if (isSigned) signature else null,
    )

/**
 * Converts this [operation content][OperationContent] to [its RPC equivalent][RpcOperationContent].
 */
public fun OperationContent.asRpc(): RpcOperationContent =
    when (this) {
        is OperationContent.ActivateAccount -> asRpc()
        is OperationContent.Ballot -> asRpc()
        is OperationContent.Endorsement -> asRpc()
        is OperationContent.Preendorsement -> asRpc()
        is OperationContent.DoubleBakingEvidence -> asRpc()
        is OperationContent.DoubleEndorsementEvidence -> asRpc()
        is OperationContent.DoublePreendorsementEvidence -> asRpc()
        is OperationContent.FailingNoop -> asRpc()
        is OperationContent.Delegation -> asRpc()
        is OperationContent.Origination -> asRpc()
        is OperationContent.RegisterGlobalConstant -> asRpc()
        is OperationContent.Reveal -> asRpc()
        is OperationContent.SetDepositsLimit -> asRpc()
        is OperationContent.Transaction -> asRpc()
        is OperationContent.Proposals -> asRpc()
        is OperationContent.SeedNonceRevelation -> asRpc()
    }


/**
 * Converts this [operation content][OperationContent.ActivateAccount] to [its RPC equivalent][RpcOperationContent.ActivateAccount].
 */
public fun OperationContent.ActivateAccount.asRpc(): RpcOperationContent.ActivateAccount = RpcOperationContent.ActivateAccount(pkh, secret.asString())

/**
 * Converts this [operation content][OperationContent.Ballot] to [its RPC equivalent][RpcOperationContent.Ballot].
 */
public fun OperationContent.Ballot.asRpc(): RpcOperationContent.Ballot = RpcOperationContent.Ballot(source, period, proposal, ballot.asRpc())

/**
 * Converts this [operation content][OperationContent.Endorsement] to [its RPC equivalent][RpcOperationContent.Endorsement].
 */
public fun OperationContent.Endorsement.asRpc(): RpcOperationContent.Endorsement = RpcOperationContent.Endorsement(slot, level, round, blockPayloadHash)

/**
 * Converts this [operation content][OperationContent.Preendorsement] to [its RPC equivalent][RpcOperationContent.Preendorsement].
 */
public fun OperationContent.Preendorsement.asRpc(): RpcOperationContent.Preendorsement = RpcOperationContent.Preendorsement(slot, level, round, blockPayloadHash)

/**
 * Converts this [operation content][OperationContent.DoubleBakingEvidence] to [its RPC equivalent][RpcOperationContent.DoubleBakingEvidence].
 */
public fun OperationContent.DoubleBakingEvidence.asRpc(): RpcOperationContent.DoubleBakingEvidence = RpcOperationContent.DoubleBakingEvidence(bh1.asRpc(), bh2.asRpc())

/**
 * Converts this [operation content][OperationContent.DoubleEndorsementEvidence] to [its RPC equivalent][RpcOperationContent.DoubleEndorsementEvidence].
 */
public fun OperationContent.DoubleEndorsementEvidence.asRpc(): RpcOperationContent.DoubleEndorsementEvidence = RpcOperationContent.DoubleEndorsementEvidence(op1.asRpc(), op2.asRpc())

/**
 * Converts this [operation content][OperationContent.DoublePreendorsementEvidence] to [its RPC equivalent][RpcOperationContent.DoublePreendorsementEvidence].
 */
public fun OperationContent.DoublePreendorsementEvidence.asRpc(): RpcOperationContent.DoublePreendorsementEvidence = RpcOperationContent.DoublePreendorsementEvidence(op1.asRpc(), op2.asRpc())

/**
 * Converts this [operation content][OperationContent.FailingNoop] to [its RPC equivalent][RpcOperationContent.FailingNoop].
 */
public fun OperationContent.FailingNoop.asRpc(): RpcOperationContent.FailingNoop = RpcOperationContent.FailingNoop(arbitrary.asString())

/**
 * Converts this [operation content][OperationContent.Delegation] to [its RPC equivalent][RpcOperationContent.Delegation].
 */
public fun OperationContent.Delegation.asRpc(): RpcOperationContent.Delegation = RpcOperationContent.Delegation(source, fee, counter.nat, gasLimit.nat, storageLimit.nat, delegate)

/**
 * Converts this [operation content][OperationContent.Origination] to [its RPC equivalent][RpcOperationContent.Origination].
 */
public fun OperationContent.Origination.asRpc(): RpcOperationContent.Origination = RpcOperationContent.Origination(source, fee, counter.nat, gasLimit.nat, storageLimit.nat, balance, delegate, script)

/**
 * Converts this [operation content][OperationContent.RegisterGlobalConstant] to [its RPC equivalent][RpcOperationContent.RegisterGlobalConstant].
 */
public fun OperationContent.RegisterGlobalConstant.asRpc(): RpcOperationContent.RegisterGlobalConstant = RpcOperationContent.RegisterGlobalConstant(source, fee, counter.nat, gasLimit.nat, storageLimit.nat, value)

/**
 * Converts this [operation content][OperationContent.Reveal] to [its RPC equivalent][RpcOperationContent.Reveal].
 */
public fun OperationContent.Reveal.asRpc(): RpcOperationContent.Reveal = RpcOperationContent.Reveal(source, fee, counter.nat, gasLimit.nat, storageLimit.nat, publicKey)

/**
 * Converts this [operation content][OperationContent.SetDepositsLimit] to [its RPC equivalent][RpcOperationContent.SetDepositsLimit].
 */
public fun OperationContent.SetDepositsLimit.asRpc(): RpcOperationContent.SetDepositsLimit = RpcOperationContent.SetDepositsLimit(source, fee, counter.nat, gasLimit.nat, storageLimit.nat, limit)

/**
 * Converts this [operation content][OperationContent.Transaction] to [its RPC equivalent][RpcOperationContent.Transaction].
 */
public fun OperationContent.Transaction.asRpc(): RpcOperationContent.Transaction = RpcOperationContent.Transaction(source, fee, counter.nat, gasLimit.nat, storageLimit.nat, amount, destination, parameters)

/**
 * Converts this [operation content][OperationContent.Proposals] to [its RPC equivalent][RpcOperationContent.Proposals].
 */
public fun OperationContent.Proposals.asRpc(): RpcOperationContent.Proposals = RpcOperationContent.Proposals(source, period, proposals)

/**
 * Converts this [operation content][OperationContent.SeedNonceRevelation] to [its RPC equivalent][RpcOperationContent.SeedNonceRevelation].
 */
public fun OperationContent.SeedNonceRevelation.asRpc(): RpcOperationContent.SeedNonceRevelation = RpcOperationContent.SeedNonceRevelation(level, nonce.asString())

/**
 * Converts this [RPC operation content][RpcOperationContent] to [its non-RPC equivalent][OperationContent].
 */
public fun RpcOperationContent.asOperationContent(): OperationContent =
    when (this) {
        is RpcOperationContent.ActivateAccount -> asActivateAccount()
        is RpcOperationContent.Ballot -> asBallot()
        is RpcOperationContent.Delegation -> asDelegation()
        is RpcOperationContent.DoubleBakingEvidence -> asDoubleBakingEvidence()
        is RpcOperationContent.DoubleEndorsementEvidence -> asDoubleEndorsementEvidence()
        is RpcOperationContent.DoublePreendorsementEvidence -> asDoublePreendorsementEvidence()
        is RpcOperationContent.Endorsement -> asEndorsement()
        is RpcOperationContent.FailingNoop -> asFailingNoop()
        is RpcOperationContent.Origination -> asOrigination()
        is RpcOperationContent.Preendorsement -> asPreendorsement()
        is RpcOperationContent.Proposals -> asProposals()
        is RpcOperationContent.RegisterGlobalConstant -> asRegisterGlobalConstant()
        is RpcOperationContent.Reveal -> asReveal()
        is RpcOperationContent.SeedNonceRevelation -> asSeedNonceRevelation()
        is RpcOperationContent.SetDepositsLimit -> asSetDepositsLimit()
        is RpcOperationContent.Transaction -> asTransaction()
    }


/**
 * Converts this [RPC operation content][RpcOperationContent.ActivateAccount] to [its non-RPC equivalent][OperationContent.ActivateAccount].
 */
public fun RpcOperationContent.ActivateAccount.asActivateAccount(): OperationContent.ActivateAccount = OperationContent.ActivateAccount(pkh, secret.asHexString())

/**
 * Converts this [RPC operation content][RpcOperationContent.Ballot] to [its non-RPC equivalent][OperationContent.Ballot].
 */
public fun RpcOperationContent.Ballot.asBallot(): OperationContent.Ballot = OperationContent.Ballot(source, period, proposal, ballot.asBallotType())

/**
 * Converts this [RPC operation content][RpcOperationContent.Delegation] to [its non-RPC equivalent][OperationContent.Delegation].
 */
public fun RpcOperationContent.Delegation.asDelegation(): OperationContent.Delegation = OperationContent.Delegation(source, fee, counter.asTezosNatural(), gasLimit.asTezosNatural(), storageLimit.asTezosNatural(), delegate)

/**
 * Converts this [RPC operation content][RpcOperationContent.DoubleBakingEvidence] to [its non-RPC equivalent][OperationContent.DoubleBakingEvidence].
 */
public fun RpcOperationContent.DoubleBakingEvidence.asDoubleBakingEvidence(): OperationContent.DoubleBakingEvidence = OperationContent.DoubleBakingEvidence(bh1.asBlockHeader(), bh2.asBlockHeader())

/**
 * Converts this [RPC operation content][RpcOperationContent.DoubleEndorsementEvidence] to [its non-RPC equivalent][OperationContent.DoubleEndorsementEvidence].
 */
public fun RpcOperationContent.DoubleEndorsementEvidence.asDoubleEndorsementEvidence(): OperationContent.DoubleEndorsementEvidence = OperationContent.DoubleEndorsementEvidence(op1.asInlinedEndorsement(), op2.asInlinedEndorsement())

/**
 * Converts this [RPC operation content][RpcOperationContent.DoublePreendorsementEvidence] to [its non-RPC equivalent][OperationContent.DoublePreendorsementEvidence].
 */
public fun RpcOperationContent.DoublePreendorsementEvidence.asDoublePreendorsementEvidence(): OperationContent.DoublePreendorsementEvidence = OperationContent.DoublePreendorsementEvidence(op1.asInlinedPreendorsement(), op2.asInlinedPreendorsement())

/**
 * Converts this [RPC operation content][RpcOperationContent.Endorsement] to [its non-RPC equivalent][OperationContent.Endorsement].
 */
public fun RpcOperationContent.Endorsement.asEndorsement(): OperationContent.Endorsement = OperationContent.Endorsement(slot, level, round, blockPayloadHash)

/**
 * Converts this [RPC operation content][RpcOperationContent.FailingNoop] to [its non-RPC equivalent][OperationContent.FailingNoop].
 */
public fun RpcOperationContent.FailingNoop.asFailingNoop(): OperationContent.FailingNoop = OperationContent.FailingNoop(arbitrary.asHexString())

/**
 * Converts this [RPC operation content][RpcOperationContent.Origination] to [its non-RPC equivalent][OperationContent.Origination].
 */
public fun RpcOperationContent.Origination.asOrigination(): OperationContent.Origination = OperationContent.Origination(source, fee, counter.asTezosNatural(), gasLimit.asTezosNatural(), storageLimit.asTezosNatural(), balance, delegate, script)

/**
 * Converts this [RPC operation content][RpcOperationContent.Preendorsement] to [its non-RPC equivalent][OperationContent.Preendorsement].
 */
public fun RpcOperationContent.Preendorsement.asPreendorsement(): OperationContent.Preendorsement = OperationContent.Preendorsement(slot, level, round, blockPayloadHash)

/**
 * Converts this [RPC operation content][RpcOperationContent.Proposals] to [its non-RPC equivalent][OperationContent.Proposals].
 */
public fun RpcOperationContent.Proposals.asProposals(): OperationContent.Proposals = OperationContent.Proposals(source, period, proposals)

/**
 * Converts this [RPC operation content][RpcOperationContent.RegisterGlobalConstant] to [its non-RPC equivalent][OperationContent.RegisterGlobalConstant].
 */
public fun RpcOperationContent.RegisterGlobalConstant.asRegisterGlobalConstant(): OperationContent.RegisterGlobalConstant = OperationContent.RegisterGlobalConstant(source, fee, counter.asTezosNatural(), gasLimit.asTezosNatural(), storageLimit.asTezosNatural(), value)

/**
 * Converts this [RPC operation content][RpcOperationContent.Reveal] to [its non-RPC equivalent][OperationContent.Reveal].
 */
public fun RpcOperationContent.Reveal.asReveal(): OperationContent.Reveal = OperationContent.Reveal(source, fee, counter.asTezosNatural(), gasLimit.asTezosNatural(), storageLimit.asTezosNatural(), publicKey)

/**
 * Converts this [RPC operation content][RpcOperationContent.SeedNonceRevelation] to [its non-RPC equivalent][OperationContent.SeedNonceRevelation].
 */
public fun RpcOperationContent.SeedNonceRevelation.asSeedNonceRevelation(): OperationContent.SeedNonceRevelation = OperationContent.SeedNonceRevelation(level, nonce.asHexString())

/**
 * Converts this [RPC operation content][RpcOperationContent.SetDepositsLimit] to [its non-RPC equivalent][OperationContent.SetDepositsLimit].
 */
public fun RpcOperationContent.SetDepositsLimit.asSetDepositsLimit(): OperationContent.SetDepositsLimit = OperationContent.SetDepositsLimit(source, fee, counter.asTezosNatural(), gasLimit.asTezosNatural(), storageLimit.asTezosNatural(), limit)

/**
 * Converts this [RPC operation content][RpcOperationContent.Transaction] to [its non-RPC equivalent][OperationContent.Transaction].
 */
public fun RpcOperationContent.Transaction.asTransaction(): OperationContent.Transaction = OperationContent.Transaction(source, fee, counter.asTezosNatural(), gasLimit.asTezosNatural(), storageLimit.asTezosNatural(), amount, destination, parameters)

/**
 * Converts this [ballot type][OperationContent.Ballot.Type] to [its RPC equivalent][RpcOperationContent.Ballot.Type].
 */
public fun OperationContent.Ballot.Type.asRpc(): RpcOperationContent.Ballot.Type =
    when (this) {
        OperationContent.Ballot.Type.Yay ->  RpcOperationContent.Ballot.Type.Yay
        OperationContent.Ballot.Type.Nay ->  RpcOperationContent.Ballot.Type.Nay
        OperationContent.Ballot.Type.Pass ->  RpcOperationContent.Ballot.Type.Pass
    }

/**
 * Converts this [RPC ballot type][RpcOperationContent.Ballot.Type] to [its non-RPC equivalent][OperationContent.Ballot.Type].
 */
public fun RpcOperationContent.Ballot.Type.asBallotType(): OperationContent.Ballot.Type =
    when (this) {
        RpcOperationContent.Ballot.Type.Yay -> OperationContent.Ballot.Type.Yay
        RpcOperationContent.Ballot.Type.Nay -> OperationContent.Ballot.Type.Nay
        RpcOperationContent.Ballot.Type.Pass -> OperationContent.Ballot.Type.Pass
    }