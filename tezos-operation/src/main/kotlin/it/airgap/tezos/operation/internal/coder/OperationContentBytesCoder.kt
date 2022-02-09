package it.airgap.tezos.operation.internal.coder

import it.airgap.tezos.core.decodeConsumingFromBytes
import it.airgap.tezos.core.encodeToBytes
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.*
import it.airgap.tezos.core.internal.utils.*
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.core.type.zarith.ZarithNatural
import it.airgap.tezos.michelson.decodeFromBytes
import it.airgap.tezos.michelson.encodeToBytes
import it.airgap.tezos.michelson.internal.coder.MichelineBytesCoder
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.contract.Entrypoint
import it.airgap.tezos.operation.contract.Parameters
import it.airgap.tezos.operation.contract.Script
import it.airgap.tezos.operation.fromTagOrNull
import it.airgap.tezos.operation.header.Fitness
import it.airgap.tezos.operation.header.FullHeader
import it.airgap.tezos.operation.inlined.InlinedEndorsement
import it.airgap.tezos.operation.inlined.InlinedPreendorsement
import it.airgap.tezos.operation.internal.converter.TagToOperationContentKindConverter

@InternalTezosSdkApi
public class OperationContentBytesCoder(
    private val encodedBytesCoder: EncodedBytesCoder,
    private val addressBytesCoder: AddressBytesCoder,
    private val publicKeyBytesCoder: PublicKeyBytesCoder,
    private val implicitAddressBytesCoder: ImplicitAddressBytesCoder,
    private val signatureBytesCoder: SignatureBytesCoder,
    private val zarithNaturalBytesCoder: ZarithNaturalBytesCoder,
    private val michelineBytesCoder: MichelineBytesCoder,
    private val tagToOperationContentKindConverter: TagToOperationContentKindConverter,
) : ConsumingBytesCoder<OperationContent> {
    override fun encode(value: OperationContent): ByteArray =
        when (value) {
            is OperationContent.SeedNonceRevelation -> encodeSeedNonceRevelation(value)
            is OperationContent.DoubleEndorsementEvidence -> encodeDoubleEndorsementEvidence(value)
            is OperationContent.DoubleBakingEvidence -> encodeDoubleBakingEvidence(value)
            is OperationContent.ActivateAccount -> encodeActivateAccount(value)
            is OperationContent.Proposals -> encodeProposals(value)
            is OperationContent.Ballot -> encodeBallot(value)
            is OperationContent.DoublePreendorsementEvidence -> encodeDoublePreendorsementEvidence(value)
            is OperationContent.FailingNoop -> encodeFailingNoop(value)
            is OperationContent.Preendorsement -> encodePreendorsement(value)
            is OperationContent.Endorsement -> encodeEndorsement(value)
            is OperationContent.Reveal -> encodeReveal(value)
            is OperationContent.Transaction -> encodeTransaction(value)
            is OperationContent.Origination -> encodeOrigination(value)
            is OperationContent.Delegation -> encodeDelegation(value)
            is OperationContent.RegisterGlobalConstant -> encodeRegisterGlobalConstant(value)
            is OperationContent.SetDepositsLimit -> encodeSetDepositsLimit(value)
        }

    override fun decode(value: ByteArray): OperationContent = decodeConsuming(value.toMutableList())
    override fun decodeConsuming(value: MutableList<Byte>): OperationContent =
        when (OperationContent.Kind.recognize(value)) {
            OperationContent.SeedNonceRevelation -> decodeSeedNonceRevelation(value)
            OperationContent.DoubleEndorsementEvidence -> decodeDoubleEndorsementEvidence(value)
            OperationContent.DoubleBakingEvidence -> decodeDoubleBakingEvidence(value)
            OperationContent.ActivateAccount -> decodeActivateAccount(value)
            OperationContent.Proposals -> decodeProposals(value)
            OperationContent.Ballot -> decodeBallot(value)
            OperationContent.DoublePreendorsementEvidence -> decodeDoublePreendorsementEvidence(value)
            OperationContent.FailingNoop -> decodeFailingNoop(value)
            OperationContent.Preendorsement -> decodePreendorsement(value)
            OperationContent.Endorsement -> decodeEndorsement(value)
            OperationContent.Reveal -> decodeReveal(value)
            OperationContent.Transaction -> decodeTransaction(value)
            OperationContent.Origination -> decodeOrigination(value)
            OperationContent.Delegation -> decodeDelegation(value)
            OperationContent.RegisterGlobalConstant -> decodeRegisterGlobalConstant(value)
            OperationContent.SetDepositsLimit -> decodeSetDepositsLimit(value)
            else -> failWithInvalidEncodedOperationContent()
        }

    private fun encodeSeedNonceRevelation(content: OperationContent.SeedNonceRevelation): ByteArray = with(content) {
        val levelBytes = encodeInt32ToBytes(level)
        val nonceBytes = encodeHexStringToBytes(nonce)

        OperationContent.SeedNonceRevelation.tag + levelBytes + nonceBytes
    }

    private fun encodeDoubleEndorsementEvidence(content: OperationContent.DoubleEndorsementEvidence): ByteArray = with(content) {
        val op1Bytes = encodeInlinedEndorsement(op1)
        val op1Length = encodeInt32ToBytes(op1Bytes.size)

        val op2Bytes = encodeInlinedEndorsement(op2)
        val op2Length = encodeInt32ToBytes(op2Bytes.size)

        OperationContent.DoubleEndorsementEvidence.tag + op1Length + op1Bytes + op2Length + op2Bytes
    }

    private fun encodeDoubleBakingEvidence(content: OperationContent.DoubleBakingEvidence): ByteArray = with(content) {
        val bh1Bytes = encodeFullHeader(bh1)
        val bh1Length = encodeInt32ToBytes(bh1Bytes.size)

        val bh2Bytes = encodeFullHeader(bh2)
        val bh2Length = encodeInt32ToBytes(bh2Bytes.size)

        OperationContent.DoubleBakingEvidence.tag + bh1Length + bh1Bytes + bh2Length + bh2Bytes
    }

    private fun encodeActivateAccount(content: OperationContent.ActivateAccount): ByteArray = with(content) {
        val pkhBytes = pkh.encodeToBytes(encodedBytesCoder)
        val secretBytes = encodeHexStringToBytes(secret)

        OperationContent.ActivateAccount.tag + pkhBytes + secretBytes
    }

    private fun encodeProposals(content: OperationContent.Proposals): ByteArray = with(content) {
        val sourceBytes = source.encodeToBytes(implicitAddressBytesCoder)
        val periodBytes = encodeInt32ToBytes(period)

        val proposalsBytes = encodeListToBytes(proposals) { it.encodeToBytes(encodedBytesCoder) }
        val proposalsLength = encodeInt32ToBytes(proposalsBytes.size)

        OperationContent.Proposals.tag + sourceBytes + periodBytes + proposalsLength + proposalsBytes
    }

    private fun encodeBallot(content: OperationContent.Ballot): ByteArray = with(content) {
        val sourceBytes = source.encodeToBytes(implicitAddressBytesCoder)
        val periodBytes = encodeInt32ToBytes(period)
        val proposalBytes = proposal.encodeToBytes(encodedBytesCoder)
        val ballotBytes = ballot.value

        OperationContent.Ballot.tag + sourceBytes + periodBytes + proposalBytes + ballotBytes
    }

    private fun encodeDoublePreendorsementEvidence(content: OperationContent.DoublePreendorsementEvidence): ByteArray = with(content) {
        val op1Bytes = encodeInlinedPreendorsement(op1)
        val op1Length = encodeInt32ToBytes(op1Bytes.size)

        val op2Bytes = encodeInlinedPreendorsement(op2)
        val op2Length = encodeInt32ToBytes(op2Bytes.size)

        OperationContent.DoublePreendorsementEvidence.tag + op1Length + op1Bytes + op2Length + op2Bytes
    }

    private fun encodeFailingNoop(content: OperationContent.FailingNoop): ByteArray = with(content) {
        val bytes = encodeHexStringToBytes(arbitrary)
        val length = encodeInt32ToBytes(bytes.size)

        OperationContent.FailingNoop.tag + length + bytes
    }

    private fun encodePreendorsement(content: OperationContent.Preendorsement): ByteArray =
        OperationContent.Preendorsement.tag + encodeConsensusOperation(content)

    private fun encodeEndorsement(content: OperationContent.Endorsement): ByteArray =
        OperationContent.Endorsement.tag + encodeConsensusOperation(content)

    private fun encodeReveal(content: OperationContent.Reveal): ByteArray = with(content) {
        val publicKeyBytes = publicKey.encodeToBytes(publicKeyBytesCoder)

        OperationContent.Reveal.tag + encodeManagerOperation(this) + publicKeyBytes
    }

    private fun encodeTransaction(content: OperationContent.Transaction): ByteArray  = with(content) {
        val amountBytes = amount.encodeToBytes(zarithNaturalBytesCoder)
        val destinationBytes = destination.encodeToBytes(addressBytesCoder)

        val parametersBytes = parameters?.let { encodeParameters(it) } ?: byteArrayOf()
        val parametersPresence = encodeBooleanToBytes(parametersBytes.isNotEmpty())

        OperationContent.Transaction.tag + encodeManagerOperation(this) + amountBytes + destinationBytes + parametersPresence + parametersBytes
    }

    private fun encodeOrigination(content: OperationContent.Origination): ByteArray = with(content) {
        val balanceBytes = balance.encodeToBytes(zarithNaturalBytesCoder)

        val delegateBytes = delegate?.encodeToBytes(implicitAddressBytesCoder) ?: byteArrayOf()
        val delegatePresence = encodeBooleanToBytes(delegateBytes.isNotEmpty())

        val scriptBytes = encodeScript(script)

        OperationContent.Origination.tag + encodeManagerOperation(this) + balanceBytes + delegatePresence + delegateBytes + scriptBytes
    }

    private fun encodeDelegation(content: OperationContent.Delegation): ByteArray = with(content) {
        val delegateBytes = delegate?.encodeToBytes(implicitAddressBytesCoder) ?: byteArrayOf()
        val delegatePresence = encodeBooleanToBytes(delegateBytes.isNotEmpty())

        OperationContent.Delegation.tag + encodeManagerOperation(content) + delegatePresence + delegateBytes
    }

    private fun encodeRegisterGlobalConstant(content: OperationContent.RegisterGlobalConstant): ByteArray = with(content) {
        val valueBytes = value.encodeToBytes(michelineBytesCoder)
        val valueLength = encodeInt32ToBytes(valueBytes.size)

        OperationContent.RegisterGlobalConstant.tag + encodeManagerOperation(this) + valueLength + valueBytes
    }

    private fun encodeSetDepositsLimit(content: OperationContent.SetDepositsLimit): ByteArray = with(content) {
        val limitBytes = limit?.encodeToBytes(zarithNaturalBytesCoder) ?: byteArrayOf()
        val limitPresence = encodeBooleanToBytes(limitBytes.isNotEmpty())

        OperationContent.SetDepositsLimit.tag + encodeManagerOperation(this) + limitPresence + limitBytes
    }

    private fun encodeConsensusOperation(content: OperationContent.Consensus): ByteArray = with(content) {
        val slotBytes = encodeUInt16ToBytes(slot)
        val levelBytes = encodeInt32ToBytes(level)
        val roundBytes = encodeInt32ToBytes(round)
        val blockPayloadHashBytes = blockPayloadHash.encodeToBytes(encodedBytesCoder)

        slotBytes + levelBytes + roundBytes + blockPayloadHashBytes
    }

    private fun encodeManagerOperation(content: OperationContent.Manager): ByteArray = with(content) {
        val sourceBytes = source.encodeToBytes(implicitAddressBytesCoder)
        val feeBytes = fee.encodeToBytes(zarithNaturalBytesCoder)
        val counterBytes = counter.encodeToBytes(zarithNaturalBytesCoder)
        val gasLimitBytes = gasLimit.encodeToBytes(zarithNaturalBytesCoder)
        val storageLimitBytes = storageLimit.encodeToBytes(zarithNaturalBytesCoder)

        sourceBytes + feeBytes + counterBytes + gasLimitBytes + storageLimitBytes
    }

    private fun encodeInlinedEndorsement(inlined: InlinedEndorsement): ByteArray = with(inlined) {
        val branchBytes = branch.encodeToBytes(encodedBytesCoder)
        val operationsBytes = encodeEndorsement(operations)
        val signatureBytes = signature.encodeToBytes(signatureBytesCoder)

        branchBytes + operationsBytes + signatureBytes
    }

    private fun encodeFullHeader(header: FullHeader): ByteArray = with(header) {
        val levelBytes = encodeInt32ToBytes(level)
        val protoBytes = encodeUInt8ToBytes(proto)
        val predecessorBytes = predecessor.encodeToBytes(encodedBytesCoder)
        val timestampBytes = encodeInt64ToBytes(timestamp)
        val validationPassBytes = encodeUInt8ToBytes(validationPass)
        val operationsHashBytes = operationsHash.encodeToBytes(encodedBytesCoder)

        val fitnessBytes = encodeFitness(fitness)
        val fitnessLength = encodeInt32ToBytes(fitnessBytes.size)

        val contextBytes = context.encodeToBytes(encodedBytesCoder)
        val payloadHashBytes = payloadHash.encodeToBytes(encodedBytesCoder)
        val payloadRoundBytes = encodeInt32ToBytes(payloadRound)
        val proofOfWorkNonceBytes = encodeHexStringToBytes(proofOfWorkNonce)

        val seedNonceHashBytes = seedNonceHash?.encodeToBytes(encodedBytesCoder) ?: byteArrayOf()
        val seedNonceHashPresence = encodeBooleanToBytes(seedNonceHashBytes.isNotEmpty())

        val liquidityBankingEscapeVoteBytes = encodeBooleanToBytes(liquidityBakingEscapeVote)
        val signatureBytes = signatureBytesCoder.encode(signature)

        levelBytes +
                protoBytes +
                predecessorBytes +
                timestampBytes +
                validationPassBytes +
                operationsHashBytes +
                fitnessLength +
                fitnessBytes +
                contextBytes +
                payloadHashBytes +
                payloadRoundBytes +
                proofOfWorkNonceBytes +
                seedNonceHashPresence +
                seedNonceHashBytes +
                liquidityBankingEscapeVoteBytes +
                signatureBytes
    }

    private fun encodeInlinedPreendorsement(inlined: InlinedPreendorsement): ByteArray = with(inlined) {
        val branchBytes = branch.encodeToBytes(encodedBytesCoder)
        val operationsBytes = encodePreendorsement(operations)
        val signatureBytes = signature.encodeToBytes(signatureBytesCoder)

        branchBytes + operationsBytes + signatureBytes
    }

    private fun encodeParameters(parameters: Parameters): ByteArray = with(parameters) {
        val entrypointBytes = encodeEntrypoint(parameters.entrypoint)

        val valueBytes = value.encodeToBytes(michelineBytesCoder)
        val valueLength = encodeInt32ToBytes(valueBytes.size)

        entrypointBytes + valueLength + valueBytes
    }

    private fun encodeScript(script: Script): ByteArray = with(script) {
        val codeBytes = code.encodeToBytes(michelineBytesCoder)
        val codeLength = encodeInt32ToBytes(codeBytes.size)

        val storageBytes = storage.encodeToBytes(michelineBytesCoder)
        val storageLength = encodeInt32ToBytes(storageBytes.size)

        codeLength + codeBytes + storageLength + storageBytes
    }

    private fun encodeFitness(fitness: List<Fitness>): ByteArray =
        encodeListToBytes(fitness) { fitness ->
            val levelBytes = encodeInt32ToBytes(fitness.level)

            val lockedRoundBytes = fitness.lockedRound?.let { encodeInt32ToBytes(it) } ?: byteArrayOf()
            val lockedRoundPresence = encodeBooleanToBytes(lockedRoundBytes.isNotEmpty())

            val predecessorRoundBytes = encodeInt32ToBytes(fitness.predecessorRound)
            val roundBytes = encodeInt32ToBytes(fitness.round)

            levelBytes + lockedRoundPresence + lockedRoundBytes + predecessorRoundBytes + roundBytes
        }

    private fun encodeEntrypoint(entrypoint: Entrypoint): ByteArray = with(entrypoint) {
        when (this) {
            Entrypoint.Default -> Entrypoint.Default.tag
            Entrypoint.Root -> Entrypoint.Root.tag
            Entrypoint.Do -> Entrypoint.Do.tag
            Entrypoint.SetDelegate -> Entrypoint.SetDelegate.tag
            Entrypoint.RemoveDelegate -> Entrypoint.RemoveDelegate.tag
            is Entrypoint.Named -> {
                val bytes = encodeStringToBytes(value)
                val length = encodeUInt8ToBytes(bytes.size.toUByte())

                Entrypoint.Named.tag + length + bytes
            }
        }
    }

    private fun decodeSeedNonceRevelation(bytes: MutableList<Byte>): OperationContent.SeedNonceRevelation {
        requireConsumingKind(OperationContent.SeedNonceRevelation, bytes)

        val level = decodeConsumingInt32FromBytes(bytes)
        val nonce = decodeConsumingHexStringFromBytes(bytes, 32)

        return OperationContent.SeedNonceRevelation(level, nonce)
    }

    private fun decodeDoubleEndorsementEvidence(bytes: MutableList<Byte>): OperationContent.DoubleEndorsementEvidence {
        requireConsumingKind(OperationContent.DoubleEndorsementEvidence, bytes)

        val op1Length = decodeConsumingInt32FromBytes(bytes)
        val op1 = decodeInlinedEndorsement(bytes.consumeUntil(op1Length))

        val op2Length = decodeConsumingInt32FromBytes(bytes)
        val op2 = decodeInlinedEndorsement(bytes.consumeUntil(op2Length))

        return OperationContent.DoubleEndorsementEvidence(op1, op2)
    }

    private fun decodeDoubleBakingEvidence(bytes: MutableList<Byte>): OperationContent.DoubleBakingEvidence {
        requireConsumingKind(OperationContent.DoubleBakingEvidence, bytes)

        val bh1Length = decodeConsumingInt32FromBytes(bytes)
        val bh1 = decodeFullHeader(bytes.consumeUntil(bh1Length))

        val bh2Length = decodeConsumingInt32FromBytes(bytes)
        val bh2 = decodeFullHeader(bytes.consumeUntil(bh2Length))

        return OperationContent.DoubleBakingEvidence(bh1, bh2)
    }

    private fun decodeActivateAccount(bytes: MutableList<Byte>): OperationContent.ActivateAccount {
        requireConsumingKind(OperationContent.ActivateAccount, bytes)

        val pkh = Ed25519PublicKeyHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)
        val secret = decodeConsumingHexStringFromBytes(bytes, 20)

        return OperationContent.ActivateAccount(pkh, secret)
    }

    private fun decodeProposals(bytes: MutableList<Byte>): OperationContent.Proposals {
        requireConsumingKind(OperationContent.Proposals, bytes)

        val source = ImplicitAddress.decodeConsumingFromBytes(bytes, implicitAddressBytesCoder)
        val period = decodeConsumingInt32FromBytes(bytes)

        val proposalsLength = decodeConsumingInt32FromBytes(bytes)
        val proposals = decodeConsumingListFromBytes(bytes.consumeUntil(proposalsLength)) { ProtocolHash.decodeConsumingFromBytes(it, encodedBytesCoder) }

        return OperationContent.Proposals(source, period, proposals)
    }

    private fun decodeBallot(bytes: MutableList<Byte>): OperationContent.Ballot {
        requireConsumingKind(OperationContent.Ballot, bytes)

        val source = ImplicitAddress.decodeConsumingFromBytes(bytes, implicitAddressBytesCoder)
        val period = decodeConsumingInt32FromBytes(bytes)
        val proposal = ProtocolHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)
        val ballot = OperationContent.Ballot.BallotType.recognizeConsuming(bytes) ?: failWithInvalidEncodedOperationContent()

        return OperationContent.Ballot(source, period, proposal, ballot)
    }

    private fun decodeDoublePreendorsementEvidence(bytes: MutableList<Byte>): OperationContent.DoublePreendorsementEvidence {
        requireConsumingKind(OperationContent.DoublePreendorsementEvidence, bytes)

        val op1Length = decodeConsumingInt32FromBytes(bytes)
        val op1 = decodeInlinedPreendorsement(bytes.consumeUntil(op1Length))

        val op2Length = decodeConsumingInt32FromBytes(bytes)
        val op2 = decodeInlinedPreendorsement(bytes.consumeUntil(op2Length))

        return OperationContent.DoublePreendorsementEvidence(op1, op2)
    }

    private fun decodeFailingNoop(bytes: MutableList<Byte>): OperationContent.FailingNoop {
        requireConsumingKind(OperationContent.FailingNoop, bytes)

        val length = decodeConsumingInt32FromBytes(bytes)
        val arbitrary = decodeConsumingHexStringFromBytes(bytes, length)

        return OperationContent.FailingNoop(arbitrary)
    }

    private fun decodePreendorsement(bytes: MutableList<Byte>): OperationContent.Preendorsement {
        requireConsumingKind(OperationContent.Preendorsement, bytes)

        return decodeConsensusOperation(bytes) { slot, level, round, blockPayloadHash, _ ->
            OperationContent.Preendorsement(slot, level, round, blockPayloadHash)
        }
    }

    private fun decodeEndorsement(bytes: MutableList<Byte>): OperationContent.Endorsement {
        requireConsumingKind(OperationContent.Endorsement, bytes)

        return decodeConsensusOperation(bytes) { slot, level, round, blockPayloadHash, _ ->
            OperationContent.Endorsement(slot, level, round, blockPayloadHash)
        }
    }

    private fun decodeReveal(bytes: MutableList<Byte>): OperationContent.Reveal {
        requireConsumingKind(OperationContent.Reveal, bytes)

        return decodeManagerOperation(bytes) { source, fee, counter, gasLimit, storageLimit, bytes ->
            val publicKey = PublicKeyEncoded.decodeConsumingFromBytes(bytes, publicKeyBytesCoder)

            OperationContent.Reveal(
                source,
                fee,
                counter,
                gasLimit,
                storageLimit,
                publicKey,
            )
        }
    }

    private fun decodeTransaction(bytes: MutableList<Byte>): OperationContent.Transaction {
        requireConsumingKind(OperationContent.Transaction, bytes)

        return decodeManagerOperation(bytes) { source, fee, counter, gasLimit, storageLimit, bytes ->
            val amount = ZarithNatural.decodeConsumingFromBytes(bytes, zarithNaturalBytesCoder)
            val destination = Address.decodeConsumingFromBytes(bytes, addressBytesCoder)

            val parametersPresence = decodeBoolean(bytes)
            val parameters = if (parametersPresence) decodeParameters(bytes) else null

            OperationContent.Transaction(
                source,
                fee,
                counter,
                gasLimit,
                storageLimit,
                amount,
                destination,
                parameters,
            )
        }
    }

    private fun decodeOrigination(bytes: MutableList<Byte>): OperationContent.Origination {
        requireConsumingKind(OperationContent.Origination, bytes)

        return decodeManagerOperation(bytes) { source, fee, counter, gasLimit, storageLimit, bytes ->
            val balance = ZarithNatural.decodeConsumingFromBytes(bytes, zarithNaturalBytesCoder)

            val delegatePresence = decodeBoolean(bytes)
            val delegate = if (delegatePresence) ImplicitAddress.decodeConsumingFromBytes(bytes, implicitAddressBytesCoder) else null

            val script = decodeScript(bytes)

            OperationContent.Origination(
                source,
                fee,
                counter,
                gasLimit,
                storageLimit,
                balance,
                delegate,
                script,
            )
        }
    }

    private fun decodeDelegation(bytes: MutableList<Byte>): OperationContent.Delegation {
        requireConsumingKind(OperationContent.Delegation, bytes)

        return decodeManagerOperation(bytes) { source, fee, counter, gasLimit, storageLimit, bytes ->
            val delegatePresence = decodeBoolean(bytes)
            val delegate = if (delegatePresence) ImplicitAddress.decodeConsumingFromBytes(bytes, implicitAddressBytesCoder) else null

            OperationContent.Delegation(
                source,
                fee,
                counter,
                gasLimit,
                storageLimit,
                delegate,
            )
        }
    }

    private fun decodeRegisterGlobalConstant(bytes: MutableList<Byte>): OperationContent.RegisterGlobalConstant {
        requireConsumingKind(OperationContent.RegisterGlobalConstant, bytes)

        return decodeManagerOperation(bytes) { source, fee, counter, gasLimit, storageLimit, bytes ->
            val valueLength = decodeConsumingInt32FromBytes(bytes)
            val value = MichelineNode.decodeFromBytes(bytes.consumeUntil(valueLength).toByteArray(), michelineBytesCoder)
            OperationContent.RegisterGlobalConstant(
                source,
                fee,
                counter,
                gasLimit,
                storageLimit,
                value,
            )
        }
    }

    private fun decodeSetDepositsLimit(value: MutableList<Byte>): OperationContent.SetDepositsLimit {
        requireConsumingKind(OperationContent.SetDepositsLimit, value)

        return decodeManagerOperation(value) { source, fee, counter, gasLimit, storageLimit, bytes ->
            val limitPresence = decodeBoolean(bytes)
            val limit = if (limitPresence) ZarithNatural.decodeConsumingFromBytes(bytes, zarithNaturalBytesCoder) else null

            OperationContent.SetDepositsLimit(
                source,
                fee,
                counter,
                gasLimit,
                storageLimit,
                limit,
            )
        }
    }

    private inline fun <reified T : OperationContent.Consensus> decodeConsensusOperation(
        bytes: MutableList<Byte>,
        create: (slot: UShort, level: Int, round: Int, blockPayloadHash: BlockPayloadHash, MutableList<Byte>) -> T
    ): T {
        val slot = decodeConsumingUInt16FromBytes(bytes)
        val level = decodeConsumingInt32FromBytes(bytes)
        val round = decodeConsumingInt32FromBytes(bytes)
        val blockPayloadHash = BlockPayloadHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)

        return create(slot, level, round, blockPayloadHash, bytes)
    }

    private fun <T : OperationContent.Manager> decodeManagerOperation(
        bytes: MutableList<Byte>,
        create: (source: ImplicitAddress<*>, fee: ZarithNatural, counter: ZarithNatural, gasLimit: ZarithNatural, storageLimit: ZarithNatural, MutableList<Byte>) -> T
    ): T {
        val source = ImplicitAddress.decodeConsumingFromBytes(bytes, implicitAddressBytesCoder)
        val fee = ZarithNatural.decodeConsumingFromBytes(bytes, zarithNaturalBytesCoder)
        val counter = ZarithNatural.decodeConsumingFromBytes(bytes, zarithNaturalBytesCoder)
        val gasLimit = ZarithNatural.decodeConsumingFromBytes(bytes, zarithNaturalBytesCoder)
        val storageLimit = ZarithNatural.decodeConsumingFromBytes(bytes, zarithNaturalBytesCoder)

        return create(source, fee, counter, gasLimit, storageLimit, bytes)
    }

    private fun decodeInlinedEndorsement(bytes: MutableList<Byte>): InlinedEndorsement {
        val branch = BlockHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)
        val operations = decodeEndorsement(bytes)
        val signature = signatureBytesCoder.decodeConsuming(bytes)

        return InlinedEndorsement(branch, operations, signature)
    }

    private fun decodeFullHeader(bytes: MutableList<Byte>): FullHeader {
        val level = decodeConsumingInt32FromBytes(bytes)
        val proto = decodeUInt8(bytes)
        val predecessor = BlockHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)
        val timestamp = decodeConsumingInt64FromBytes(bytes)
        val validationPass = decodeUInt8(bytes)
        val operationsHash = OperationHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)

        val fitnessLength = decodeConsumingInt32FromBytes(bytes)
        val fitness = decodeFitness(bytes.consumeUntil(fitnessLength))

        val context = ContextHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)
        val payloadHash = BlockPayloadHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)
        val payloadRound = decodeConsumingInt32FromBytes(bytes)
        val proofOfWorkNonce = decodeConsumingHexStringFromBytes(bytes, 8)

        val seedNonceHashPresence = decodeBoolean(bytes)
        val seedNonceHash = if (seedNonceHashPresence) NonceHash.decodeConsumingFromBytes(bytes, encodedBytesCoder) else null

        val liquidityBankingEscapeVote = decodeBoolean(bytes)
        val signature = signatureBytesCoder.decodeConsuming(bytes)

        return FullHeader(
            level,
            proto,
            predecessor,
            timestamp,
            validationPass,
            operationsHash,
            fitness,
            context,
            payloadHash,
            payloadRound,
            proofOfWorkNonce,
            seedNonceHash,
            liquidityBankingEscapeVote,
            signature,
        )
    }

    private fun decodeInlinedPreendorsement(bytes: MutableList<Byte>): InlinedPreendorsement {
        val branch = BlockHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)
        val operations = decodePreendorsement(bytes)
        val signature = signatureBytesCoder.decodeConsuming(bytes)

        return InlinedPreendorsement(branch, operations, signature)
    }

    private fun decodeParameters(bytes: MutableList<Byte>): Parameters {
        val entrypoint = decodeEntrypoint(bytes)

        val valueLength = decodeConsumingInt32FromBytes(bytes)
        val value = MichelineNode.decodeFromBytes(bytes.consumeUntil(valueLength).toByteArray(), michelineBytesCoder)

        return Parameters(entrypoint, value)
    }

    private fun decodeScript(bytes: MutableList<Byte>): Script {
        val codeLength = decodeConsumingInt32FromBytes(bytes)
        val code = MichelineNode.decodeFromBytes(bytes.consumeUntil(codeLength).toByteArray(), michelineBytesCoder)

        val storageLength = decodeConsumingInt32FromBytes(bytes)
        val storage = MichelineNode.decodeFromBytes(bytes.consumeUntil(storageLength).toByteArray(), michelineBytesCoder)

        return Script(code, storage)
    }

    private tailrec fun decodeFitness(bytes: MutableList<Byte>, decoded: List<Fitness> = emptyList()): List<Fitness> {
        if (bytes.isEmpty()) return decoded

        val level = decodeConsumingInt32FromBytes(bytes)

        val lockedRoundPresence = decodeBoolean(bytes)
        val lockedRound = if (lockedRoundPresence) decodeConsumingInt32FromBytes(bytes) else null

        val predecessorRound = decodeConsumingInt32FromBytes(bytes)
        val round = decodeConsumingInt32FromBytes(bytes)

        return decodeFitness(bytes, decoded + Fitness(level, lockedRound, predecessorRound, round))
    }

    private fun decodeEntrypoint(bytes: MutableList<Byte>): Entrypoint =
        when (Entrypoint.Kind.recognizeConsuming(bytes)) {
            Entrypoint.Default -> Entrypoint.Default
            Entrypoint.Root -> Entrypoint.Root
            Entrypoint.Do -> Entrypoint.Do
            Entrypoint.SetDelegate -> Entrypoint.SetDelegate
            Entrypoint.RemoveDelegate -> Entrypoint.RemoveDelegate
            Entrypoint.Named -> {
                val length = decodeUInt8(bytes)
                val value = decodeConsumingStringFromBytes(bytes, length.toInt())

                Entrypoint.Named(value)
            }
            else -> failWithInvalidEncodedOperationContent()
        }

    private fun decodeUInt8(bytes: MutableList<Byte>): UByte = decodeConsumingUInt8FromBytes(bytes) ?: failWithInvalidEncodedOperationContent()
    private fun decodeBoolean(bytes: MutableList<Byte>): Boolean = decodeConsumingBooleanFromBytes(bytes) ?: failWithInvalidEncodedOperationContent()

    private operator fun UByte.plus(bytes: ByteArray): ByteArray = byteArrayOf(toByte()) + bytes

    private fun OperationContent.Kind.Companion.recognize(bytes: MutableList<Byte>): OperationContent.Kind? =
        if (bytes.isEmpty()) null
        else fromTagOrNull(bytes.first().toUByte(), tagToOperationContentKindConverter)

    private fun requireConsumingKind(expected: OperationContent.Kind, bytes: MutableList<Byte>) {
        if (OperationContent.Kind.recognize(bytes) != expected) failWithInvalidTag(expected)
        bytes.consumeAt(0)
    }

    private fun Entrypoint.Kind.Companion.recognizeConsuming(bytes: MutableList<Byte>): Entrypoint.Kind? =
        if (bytes.isEmpty()) null
        else values.find { bytes.startsWith(it.tag) }?.also { bytes.consumeUntil(it.tag.size) }

    private fun OperationContent.Ballot.BallotType.Companion.recognizeConsuming(bytes: MutableList<Byte>): OperationContent.Ballot.BallotType? =
        if (bytes.isEmpty()) null
        else OperationContent.Ballot.BallotType.values().find { bytes.startsWith(it.value) }?.also { bytes.consumeUntil(it.value.size) }

    private fun failWithInvalidEncodedOperationContent(): Nothing = failWithIllegalArgument("Invalid encoded OperationContent value.")
    private fun failWithInvalidTag(expected: OperationContent.Kind): Nothing =
        failWithIllegalArgument("Invalid tag, encoded value is not ${expected::class.qualifiedName?.removeSuffix(".Companion")?.substringAfterLast(".")}")
}