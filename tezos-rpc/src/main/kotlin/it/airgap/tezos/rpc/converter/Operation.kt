package it.airgap.tezos.rpc.converter

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.core.internal.utils.asTezosNatural
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.rpc.internal.utils.isSigned
import it.airgap.tezos.rpc.internal.utils.signatureOrPlaceholder
import it.airgap.tezos.rpc.type.operation.RpcOperationContent
import it.airgap.tezos.rpc.type.operation.RpcRunnableOperation

// -- Operation -> RpcRunnableOperation --

public fun Operation.asRunnable(chainId: ChainId): RpcRunnableOperation =
    RpcRunnableOperation(
        chainId,
        branch,
        contents.map { it.asRpc() },
        signatureOrPlaceholder,
    )

// -- RpcRunnableOperation -> Operation --

public fun RpcRunnableOperation.asOperation(): Operation =
    Operation(
        contents.map { it.asOperationContent() },
        branch,
        if (isSigned) signature else null,
    )

// -- OperationContent -> RpcOperationContent --

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

public fun OperationContent.ActivateAccount.asRpc(): RpcOperationContent.ActivateAccount = RpcOperationContent.ActivateAccount(pkh, secret.asString())
public fun OperationContent.Ballot.asRpc(): RpcOperationContent.Ballot = RpcOperationContent.Ballot(source, period, proposal, ballot.asRpc())
public fun OperationContent.Endorsement.asRpc(): RpcOperationContent.Endorsement = RpcOperationContent.Endorsement(slot, level, round, blockPayloadHash)
public fun OperationContent.Preendorsement.asRpc(): RpcOperationContent.Preendorsement = RpcOperationContent.Preendorsement(slot, level, round, blockPayloadHash)
public fun OperationContent.DoubleBakingEvidence.asRpc(): RpcOperationContent.DoubleBakingEvidence = RpcOperationContent.DoubleBakingEvidence(bh1.asRpc(), bh2.asRpc())
public fun OperationContent.DoubleEndorsementEvidence.asRpc(): RpcOperationContent.DoubleEndorsementEvidence = RpcOperationContent.DoubleEndorsementEvidence(op1.asRpc(), op2.asRpc())
public fun OperationContent.DoublePreendorsementEvidence.asRpc(): RpcOperationContent.DoublePreendorsementEvidence = RpcOperationContent.DoublePreendorsementEvidence(op1.asRpc(), op2.asRpc())
public fun OperationContent.FailingNoop.asRpc(): RpcOperationContent.FailingNoop = RpcOperationContent.FailingNoop(arbitrary.asString())
public fun OperationContent.Delegation.asRpc(): RpcOperationContent.Delegation = RpcOperationContent.Delegation(source, fee, counter.int, gasLimit.int, storageLimit.int, delegate)
public fun OperationContent.Origination.asRpc(): RpcOperationContent.Origination = RpcOperationContent.Origination(source, fee, counter.int, gasLimit.int, storageLimit.int, balance, delegate, script.asRpc())
public fun OperationContent.RegisterGlobalConstant.asRpc(): RpcOperationContent.RegisterGlobalConstant = RpcOperationContent.RegisterGlobalConstant(source, fee, counter.int, gasLimit.int, storageLimit.int, value)
public fun OperationContent.Reveal.asRpc(): RpcOperationContent.Reveal = RpcOperationContent.Reveal(source, fee, counter.int, gasLimit.int, storageLimit.int, publicKey)
public fun OperationContent.SetDepositsLimit.asRpc(): RpcOperationContent.SetDepositsLimit = RpcOperationContent.SetDepositsLimit(source, fee, counter.int, gasLimit.int, storageLimit.int, limit?.int)
public fun OperationContent.Transaction.asRpc(): RpcOperationContent.Transaction = RpcOperationContent.Transaction(source, fee, counter.int, gasLimit.int, storageLimit.int, amount, destination, parameters?.asRpc())
public fun OperationContent.Proposals.asRpc(): RpcOperationContent.Proposals = RpcOperationContent.Proposals(source, period, proposals)
public fun OperationContent.SeedNonceRevelation.asRpc(): RpcOperationContent.SeedNonceRevelation = RpcOperationContent.SeedNonceRevelation(level, nonce.asString())

// -- RpcOperationContent -> OperationContent --

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

public fun RpcOperationContent.ActivateAccount.asActivateAccount(): OperationContent.ActivateAccount = OperationContent.ActivateAccount(pkh, secret.asHexString())
public fun RpcOperationContent.Ballot.asBallot(): OperationContent.Ballot = OperationContent.Ballot(source, period, proposal, ballot.asBallotType())
public fun RpcOperationContent.Delegation.asDelegation(): OperationContent.Delegation = OperationContent.Delegation(source, fee, counter.asTezosNatural(), gasLimit.asTezosNatural(), storageLimit.asTezosNatural(), delegate)
public fun RpcOperationContent.DoubleBakingEvidence.asDoubleBakingEvidence(): OperationContent.DoubleBakingEvidence = OperationContent.DoubleBakingEvidence(bh1.asBlockHeader(), bh2.asBlockHeader())
public fun RpcOperationContent.DoubleEndorsementEvidence.asDoubleEndorsementEvidence(): OperationContent.DoubleEndorsementEvidence = OperationContent.DoubleEndorsementEvidence(op1.asInlinedEndorsement(), op2.asInlinedEndorsement())
public fun RpcOperationContent.DoublePreendorsementEvidence.asDoublePreendorsementEvidence(): OperationContent.DoublePreendorsementEvidence = OperationContent.DoublePreendorsementEvidence(op1.asInlinedPreendorsement(), op2.asInlinedPreendorsement())
public fun RpcOperationContent.Endorsement.asEndorsement(): OperationContent.Endorsement = OperationContent.Endorsement(slot, level, round, blockPayloadHash)
public fun RpcOperationContent.FailingNoop.asFailingNoop(): OperationContent.FailingNoop = OperationContent.FailingNoop(arbitrary.asHexString())
public fun RpcOperationContent.Origination.asOrigination(): OperationContent.Origination = OperationContent.Origination(source, fee, counter.asTezosNatural(), gasLimit.asTezosNatural(), storageLimit.asTezosNatural(), balance, delegate, script.asScript())
public fun RpcOperationContent.Preendorsement.asPreendorsement(): OperationContent.Preendorsement = OperationContent.Preendorsement(slot, level, round, blockPayloadHash)
public fun RpcOperationContent.Proposals.asProposals(): OperationContent.Proposals = OperationContent.Proposals(source, period, proposals)
public fun RpcOperationContent.RegisterGlobalConstant.asRegisterGlobalConstant(): OperationContent.RegisterGlobalConstant = OperationContent.RegisterGlobalConstant(source, fee, counter.asTezosNatural(), gasLimit.asTezosNatural(), storageLimit.asTezosNatural(), value)
public fun RpcOperationContent.Reveal.asReveal(): OperationContent.Reveal = OperationContent.Reveal(source, fee, counter.asTezosNatural(), gasLimit.asTezosNatural(), storageLimit.asTezosNatural(), publicKey)
public fun RpcOperationContent.SeedNonceRevelation.asSeedNonceRevelation(): OperationContent.SeedNonceRevelation = OperationContent.SeedNonceRevelation(level, nonce.asHexString())
public fun RpcOperationContent.SetDepositsLimit.asSetDepositsLimit(): OperationContent.SetDepositsLimit = OperationContent.SetDepositsLimit(source, fee, counter.asTezosNatural(), gasLimit.asTezosNatural(), storageLimit.asTezosNatural(), limit?.asTezosNatural())
public fun RpcOperationContent.Transaction.asTransaction(): OperationContent.Transaction = OperationContent.Transaction(source, fee, counter.asTezosNatural(), gasLimit.asTezosNatural(), storageLimit.asTezosNatural(), amount, destination, parameters?.asParameters())

// -- OperationContent.Ballot.BallotType -> RpcOperationContent.Ballot.BallotType --

public fun OperationContent.Ballot.BallotType.asRpc(): RpcOperationContent.Ballot.BallotType =
    when (this) {
        OperationContent.Ballot.BallotType.Yay ->  RpcOperationContent.Ballot.BallotType.Yay
        OperationContent.Ballot.BallotType.Nay ->  RpcOperationContent.Ballot.BallotType.Nay
        OperationContent.Ballot.BallotType.Pass ->  RpcOperationContent.Ballot.BallotType.Pass
    }

// -- RpcOperationContent.Ballot.BallotType -> OperationContent.Ballot.BallotType --

public fun RpcOperationContent.Ballot.BallotType.asBallotType(): OperationContent.Ballot.BallotType =
    when (this) {
        RpcOperationContent.Ballot.BallotType.Yay -> OperationContent.Ballot.BallotType.Yay
        RpcOperationContent.Ballot.BallotType.Nay -> OperationContent.Ballot.BallotType.Nay
        RpcOperationContent.Ballot.BallotType.Pass -> OperationContent.Ballot.BallotType.Pass
    }