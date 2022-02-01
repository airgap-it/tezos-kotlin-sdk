package it.airgap.tezos.operation.internal.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.coder.*
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.internal.utils.*
import it.airgap.tezos.michelson.decodeFromBytes
import it.airgap.tezos.michelson.encodeToBytes
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

internal class OperationContentBytesCoder(
    private val base58BytesCoder: Base58BytesCoder,
    private val addressBytesCoder: AddressBytesCoder,
    private val keyBytesCoder: KeyBytesCoder,
    private val keyHashBytesCoder: KeyHashBytesCoder,
    private val signatureBytesCoder: SignatureBytesCoder,
    private val zarithNaturalNumberBytesCoder: ZarithNaturalNumberBytesCoder,
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
        val levelBytes = encodeInt32(level)
        val nonceBytes = encodeHexString(nonce)

        OperationContent.SeedNonceRevelation.tag + levelBytes + nonceBytes
    }

    private fun encodeDoubleEndorsementEvidence(content: OperationContent.DoubleEndorsementEvidence): ByteArray = with(content) {
        val op1Bytes = encodeInlinedEndorsement(op1)
        val op1Length = encodeInt32(op1Bytes.size)

        val op2Bytes = encodeInlinedEndorsement(op2)
        val op2Length = encodeInt32(op2Bytes.size)

        OperationContent.DoubleEndorsementEvidence.tag + op1Length + op1Bytes + op2Length + op2Bytes
    }

    private fun encodeDoubleBakingEvidence(content: OperationContent.DoubleBakingEvidence): ByteArray = with(content) {
        val bh1Bytes = encodeFullHeader(bh1)
        val bh1Length = encodeInt32(bh1Bytes.size)

        val bh2Bytes = encodeFullHeader(bh2)
        val bh2Length = encodeInt32(bh2Bytes.size)

        OperationContent.DoubleBakingEvidence.tag + bh1Length + bh1Bytes + bh2Length + bh2Bytes
    }

    private fun encodeActivateAccount(content: OperationContent.ActivateAccount): ByteArray = with(content) {
        val pkhBytes = base58BytesCoder.encode(pkh, Tezos.Prefix.Ed25519PublicKeyHash)
        val secretBytes = encodeHexString(secret)

        OperationContent.ActivateAccount.tag + pkhBytes + secretBytes
    }

    private fun encodeProposals(content: OperationContent.Proposals): ByteArray = with(content) {
        val sourceBytes = keyHashBytesCoder.encode(source)
        val periodBytes = encodeInt32(period)

        val proposalsBytes = encodeProposalsList(proposals)
        val proposalsLength = encodeInt32(proposalsBytes.size)

        OperationContent.Proposals.tag + sourceBytes + periodBytes + proposalsLength + proposalsBytes
    }

    private fun encodeBallot(content: OperationContent.Ballot): ByteArray = with(content) {
        val sourceBytes = keyHashBytesCoder.encode(source)
        val periodBytes = encodeInt32(period)
        val proposalBytes = base58BytesCoder.encode(proposal, Tezos.Prefix.ProtocolHash)
        val ballotBytes = ballot.value

        OperationContent.Ballot.tag + sourceBytes + periodBytes + proposalBytes + ballotBytes
    }

    private fun encodeDoublePreendorsementEvidence(content: OperationContent.DoublePreendorsementEvidence): ByteArray = with(content) {
        val op1Bytes = encodeInlinedPreendorsement(op1)
        val op1Length = encodeInt32(op1Bytes.size)

        val op2Bytes = encodeInlinedPreendorsement(op2)
        val op2Length = encodeInt32(op2Bytes.size)

        OperationContent.DoublePreendorsementEvidence.tag + op1Length + op1Bytes + op2Length + op2Bytes
    }

    private fun encodeFailingNoop(content: OperationContent.FailingNoop): ByteArray = with(content) {
        val bytes = encodeHexString(arbitrary)
        val length = encodeInt32(bytes.size)

        OperationContent.FailingNoop.tag + length + bytes
    }

    private fun encodePreendorsement(content: OperationContent.Preendorsement): ByteArray =
        OperationContent.Preendorsement.tag + encodeConsensusOperation(content)

    private fun encodeEndorsement(content: OperationContent.Endorsement): ByteArray =
        OperationContent.Endorsement.tag + encodeConsensusOperation(content)

    private fun encodeReveal(content: OperationContent.Reveal): ByteArray = with(content) {
        val publicKeyBytes = keyBytesCoder.encode(publicKey)

        OperationContent.Reveal.tag + encodeManagerOperation(this) + publicKeyBytes
    }

    private fun encodeTransaction(content: OperationContent.Transaction): ByteArray  = with(content) {
        val amountBytes = zarithNaturalNumberBytesCoder.encode(BigInt.valueOf(amount))
        val destinationBytes = addressBytesCoder.encode(destination)

        val parametersBytes = parameters?.let { encodeParameters(it) } ?: byteArrayOf()
        val parametersPresence = encodeBoolean(parametersBytes.isNotEmpty())

        OperationContent.Transaction.tag + encodeManagerOperation(this) + amountBytes + destinationBytes + parametersPresence + parametersBytes
    }

    private fun encodeOrigination(content: OperationContent.Origination): ByteArray = with(content) {
        val balanceBytes = zarithNaturalNumberBytesCoder.encode(BigInt.valueOf(balance))

        val delegateBytes = delegate?.let { keyHashBytesCoder.encode(it) } ?: byteArrayOf()
        val delegatePresence = encodeBoolean(delegateBytes.isNotEmpty())

        val scriptBytes = encodeScript(script)

        OperationContent.Origination.tag + encodeManagerOperation(this) + balanceBytes + delegatePresence + delegateBytes + scriptBytes
    }

    private fun encodeDelegation(content: OperationContent.Delegation): ByteArray = with(content) {
        val delegateBytes = delegate?.let { keyHashBytesCoder.encode(it) } ?: byteArrayOf()
        val delegatePresence = encodeBoolean(delegateBytes.isNotEmpty())

        OperationContent.Delegation.tag + encodeManagerOperation(content) + delegatePresence + delegateBytes
    }

    private fun encodeRegisterGlobalConstant(content: OperationContent.RegisterGlobalConstant): ByteArray = with(content) {
        val valueBytes = value.encodeToBytes()
        val valueLength = encodeInt32(valueBytes.size)

        OperationContent.RegisterGlobalConstant.tag + encodeManagerOperation(this) + valueLength + valueBytes
    }

    private fun encodeSetDepositsLimit(content: OperationContent.SetDepositsLimit): ByteArray = with(content) {
        val limitBytes = limit?.let { zarithNaturalNumberBytesCoder.encode(BigInt.valueOf(it)) } ?: byteArrayOf()
        val limitPresence = encodeBoolean(limitBytes.isNotEmpty())

        OperationContent.SetDepositsLimit.tag + encodeManagerOperation(this) + limitPresence + limitBytes
    }

    private fun encodeConsensusOperation(content: OperationContent.Consensus): ByteArray = with(content) {
        val slotBytes = encodeUInt16(slot)
        val levelBytes = encodeInt32(level)
        val roundBytes = encodeInt32(round)
        val blockPayloadHashBytes = base58BytesCoder.encode(blockPayloadHash, Tezos.Prefix.BlockPayloadHash)

        slotBytes + levelBytes + roundBytes + blockPayloadHashBytes
    }

    private fun encodeManagerOperation(content: OperationContent.Manager): ByteArray = with(content) {
        val sourceBytes = keyHashBytesCoder.encode(source)
        val feeBytes = zarithNaturalNumberBytesCoder.encode(BigInt.valueOf(fee, radix = 10))
        val counterBytes = zarithNaturalNumberBytesCoder.encode(BigInt.valueOf(counter, radix = 10))
        val gasLimitBytes = zarithNaturalNumberBytesCoder.encode(BigInt.valueOf(gasLimit, radix = 10))
        val storageLimitBytes = zarithNaturalNumberBytesCoder.encode(BigInt.valueOf(storageLimit, radix = 10))

        sourceBytes + feeBytes + counterBytes + gasLimitBytes + storageLimitBytes
    }

    private fun encodeInlinedEndorsement(inlined: InlinedEndorsement): ByteArray = with(inlined) {
        val branchBytes = base58BytesCoder.encode(branch, Tezos.Prefix.BlockHash)
        val operationsBytes = encodeEndorsement(operations)
        val signatureBytes = signatureBytesCoder.encode(signature)

        branchBytes + operationsBytes + signatureBytes
    }

    private fun encodeFullHeader(header: FullHeader): ByteArray = with(header) {
        val levelBytes = encodeInt32(level)
        val protoBytes = encodeUInt8(proto)
        val predecessorBytes = base58BytesCoder.encode(predecessor, Tezos.Prefix.BlockHash)
        val timestampBytes = encodeInt64(timestamp)
        val validationPassBytes = encodeUInt8(validationPass)
        val operationsHashBytes = base58BytesCoder.encode(operationsHash, Tezos.Prefix.OperationHash)

        val fitnessBytes = encodeFitness(fitness)
        val fitnessLength = encodeInt32(fitnessBytes.size)

        val contextBytes = base58BytesCoder.encode(context, Tezos.Prefix.ContextHash)
        val payloadHashBytes = base58BytesCoder.encode(payloadHash, Tezos.Prefix.BlockPayloadHash)
        val payloadRoundBytes = encodeInt32(payloadRound)
        val proofOfWorkNonceBytes = encodeHexString(proofOfWorkNonce)

        val seedNonceHashBytes = seedNonceHash?.let { base58BytesCoder.encode(it, Tezos.Prefix.NonceHash) } ?: byteArrayOf()
        val seedNonceHashPresence = encodeBoolean(seedNonceHashBytes.isNotEmpty())

        val liquidityBankingEscapeVoteBytes = encodeBoolean(liquidityBakingEscapeVote)
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

    private fun encodeProposalsList(proposals: List<String>): ByteArray =
        proposals.map { base58BytesCoder.encode(it, Tezos.Prefix.ProtocolHash) }.fold(byteArrayOf()) { acc, bytes -> acc + bytes }

    private fun encodeInlinedPreendorsement(inlined: InlinedPreendorsement): ByteArray = with(inlined) {
        val branchBytes = base58BytesCoder.encode(branch, Tezos.Prefix.BlockHash)
        val operationsBytes = encodePreendorsement(operations)
        val signatureBytes = signatureBytesCoder.encode(signature)

        branchBytes + operationsBytes + signatureBytes
    }

    private fun encodeParameters(parameters: Parameters): ByteArray = with(parameters) {
        val entrypointBytes = encodeEntrypoint(parameters.entrypoint)

        val valueBytes = value.encodeToBytes()
        val valueLength = encodeInt32(valueBytes.size)

        entrypointBytes + valueLength + valueBytes
    }

    private fun encodeScript(script: Script): ByteArray = with(script) {
        val codeBytes = code.encodeToBytes()
        val codeLength = encodeInt32(codeBytes.size)

        val storageBytes = storage.encodeToBytes()
        val storageLength = encodeInt32(storageBytes.size)

        codeLength + codeBytes + storageLength + storageBytes
    }

    private fun encodeFitness(fitness: List<Fitness>): ByteArray =
        fitness.fold(byteArrayOf()) { acc, fitness ->
        val levelBytes = encodeInt32(fitness.level)

        val lockedRoundBytes = fitness.lockedRound?.let { encodeInt32(it) } ?: byteArrayOf()
        val lockedRoundPresence = encodeBoolean(lockedRoundBytes.isNotEmpty())

        val predecessorRoundBytes = encodeInt32(fitness.predecessorRound)
        val roundBytes = encodeInt32(fitness.round)

        acc + levelBytes + lockedRoundPresence + lockedRoundBytes + predecessorRoundBytes + roundBytes
    }

    private fun encodeEntrypoint(entrypoint: Entrypoint): ByteArray = with(entrypoint) {
        when (this) {
            Entrypoint.Default -> Entrypoint.Default.tag
            Entrypoint.Root -> Entrypoint.Root.tag
            Entrypoint.Do -> Entrypoint.Do.tag
            Entrypoint.SetDelegate -> Entrypoint.SetDelegate.tag
            Entrypoint.RemoveDelegate -> Entrypoint.RemoveDelegate.tag
            is Entrypoint.Named -> {
                val bytes = encodeString(value)
                val length = encodeUInt8(bytes.size.toUByte())

                Entrypoint.Named.tag + length + bytes
            }
        }
    }

    private fun decodeSeedNonceRevelation(bytes: MutableList<Byte>): OperationContent.SeedNonceRevelation {
        requireConsumingKind(OperationContent.SeedNonceRevelation, bytes)

        val level = decodeInt32(bytes)
        val nonce = decodeHexString(bytes, 32)

        return OperationContent.SeedNonceRevelation(level, nonce)
    }

    private fun decodeDoubleEndorsementEvidence(bytes: MutableList<Byte>): OperationContent.DoubleEndorsementEvidence {
        requireConsumingKind(OperationContent.DoubleEndorsementEvidence, bytes)

        val op1Length = decodeInt32(bytes)
        val op1 = decodeInlinedEndorsement(bytes.consumeAt(0 until op1Length))

        val op2Length = decodeInt32(bytes)
        val op2 = decodeInlinedEndorsement(bytes.consumeAt(0 until op2Length))

        return OperationContent.DoubleEndorsementEvidence(op1, op2)
    }

    private fun decodeDoubleBakingEvidence(bytes: MutableList<Byte>): OperationContent.DoubleBakingEvidence {
        requireConsumingKind(OperationContent.DoubleBakingEvidence, bytes)

        val bh1Length = decodeInt32(bytes)
        val bh1 = decodeFullHeader(bytes.consumeAt(0 until bh1Length))

        val bh2Length = decodeInt32(bytes)
        val bh2 = decodeFullHeader(bytes.consumeAt(0 until bh2Length))

        return OperationContent.DoubleBakingEvidence(bh1, bh2)
    }

    private fun decodeActivateAccount(bytes: MutableList<Byte>): OperationContent.ActivateAccount {
        requireConsumingKind(OperationContent.ActivateAccount, bytes)

        val pkh = base58BytesCoder.decodeConsuming(bytes, Tezos.Prefix.Ed25519PublicKeyHash)
        val secret = decodeHexString(bytes, 20)

        return OperationContent.ActivateAccount(pkh, secret)
    }

    private fun decodeProposals(bytes: MutableList<Byte>): OperationContent.Proposals {
        requireConsumingKind(OperationContent.Proposals, bytes)

        val source = keyHashBytesCoder.decodeConsuming(bytes)
        val period = decodeInt32(bytes)

        val proposalsLength = decodeInt32(bytes)
        val proposals = decodeProposalsList(bytes.consumeAt(0 until proposalsLength))

        return OperationContent.Proposals(source, period, proposals)
    }

    private fun decodeBallot(bytes: MutableList<Byte>): OperationContent.Ballot {
        requireConsumingKind(OperationContent.Ballot, bytes)

        val source = keyHashBytesCoder.decodeConsuming(bytes)
        val period = decodeInt32(bytes)
        val proposal = base58BytesCoder.decodeConsuming(bytes, Tezos.Prefix.ProtocolHash)
        val ballot = OperationContent.Ballot.BallotType.recognizeConsuming(bytes) ?: failWithInvalidEncodedOperationContent()

        return OperationContent.Ballot(source, period, proposal, ballot)
    }

    private fun decodeDoublePreendorsementEvidence(bytes: MutableList<Byte>): OperationContent.DoublePreendorsementEvidence {
        requireConsumingKind(OperationContent.DoublePreendorsementEvidence, bytes)

        val op1Length = decodeInt32(bytes)
        val op1 = decodeInlinedPreendorsement(bytes.consumeAt(0 until op1Length))

        val op2Length = decodeInt32(bytes)
        val op2 = decodeInlinedPreendorsement(bytes.consumeAt(0 until op2Length))

        return OperationContent.DoublePreendorsementEvidence(op1, op2)
    }

    private fun decodeFailingNoop(bytes: MutableList<Byte>): OperationContent.FailingNoop {
        requireConsumingKind(OperationContent.FailingNoop, bytes)

        val length = decodeInt32(bytes)
        val arbitrary = decodeHexString(bytes, length)

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
            val publicKey = keyBytesCoder.decodeConsuming(bytes)

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
            val amount = zarithNaturalNumberBytesCoder.decodeConsuming(bytes).toString(10)
            val destination = addressBytesCoder.decodeConsuming(bytes)

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
            val balance = zarithNaturalNumberBytesCoder.decodeConsuming(bytes).toString(10)

            val delegatePresence = decodeBoolean(bytes)
            val delegate = if (delegatePresence) keyHashBytesCoder.decodeConsuming(bytes) else null

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
            val delegate = if (delegatePresence) keyHashBytesCoder.decodeConsuming(bytes) else null

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
            val valueLength = decodeInt32(bytes)
            val value = MichelineNode.decodeFromBytes(bytes.consumeAt(0 until valueLength).toByteArray())

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
            val limit = if (limitPresence) zarithNaturalNumberBytesCoder.decodeConsuming(bytes).toString(10) else null

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
        create: (slot: UShort, level: Int, round: Int, blockPayloadHash: String, MutableList<Byte>) -> T
    ): T {
        val slot = decodeUInt16(bytes)
        val level = decodeInt32(bytes)
        val round = decodeInt32(bytes)
        val blockPayloadHash = base58BytesCoder.decodeConsuming(bytes, Tezos.Prefix.BlockPayloadHash)

        return create(slot, level, round, blockPayloadHash, bytes)
    }

    private fun <T : OperationContent.Manager> decodeManagerOperation(
        bytes: MutableList<Byte>,
        create: (source: String, fee: String, counter: String, gasLimit: String, storageLimit: String, MutableList<Byte>) -> T
    ): T {
        val source = keyHashBytesCoder.decodeConsuming(bytes)
        val fee = zarithNaturalNumberBytesCoder.decodeConsuming(bytes).toString(10)
        val counter = zarithNaturalNumberBytesCoder.decodeConsuming(bytes).toString(10)
        val gasLimit = zarithNaturalNumberBytesCoder.decodeConsuming(bytes).toString(10)
        val storageLimit = zarithNaturalNumberBytesCoder.decodeConsuming(bytes).toString(10)

        return create(source, fee, counter, gasLimit, storageLimit, bytes)
    }

    private fun decodeInlinedEndorsement(bytes: MutableList<Byte>): InlinedEndorsement {
        val branch = base58BytesCoder.decodeConsuming(bytes, Tezos.Prefix.BlockHash)
        val operations = decodeEndorsement(bytes)
        val signature = signatureBytesCoder.decodeConsuming(bytes)

        return InlinedEndorsement(branch, operations, signature)
    }

    private fun decodeFullHeader(bytes: MutableList<Byte>): FullHeader {
        val level = decodeInt32(bytes)
        val proto = decodeUInt8(bytes)
        val predecessor = base58BytesCoder.decodeConsuming(bytes, Tezos.Prefix.BlockHash)
        val timestamp = decodeInt64(bytes)
        val validationPass = decodeUInt8(bytes)
        val operationsHash = base58BytesCoder.decodeConsuming(bytes, Tezos.Prefix.OperationHash)

        val fitnessLength = decodeInt32(bytes)
        val fitness = decodeFitness(bytes.consumeAt(0 until fitnessLength))

        val context = base58BytesCoder.decodeConsuming(bytes, Tezos.Prefix.ContextHash)
        val payloadHash = base58BytesCoder.decodeConsuming(bytes, Tezos.Prefix.BlockPayloadHash)
        val payloadRound = decodeInt32(bytes)
        val proofOfWorkNonce = decodeHexString(bytes, 8)

        val seedNonceHashPresence = decodeBoolean(bytes)
        val seedNonceHash = if (seedNonceHashPresence) base58BytesCoder.decodeConsuming(bytes, Tezos.Prefix.NonceHash) else null

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

    private tailrec fun decodeProposalsList(bytes: MutableList<Byte>, decoded: List<String> = emptyList()): List<String> {
        if (bytes.isEmpty()) return decoded
        val proposal = base58BytesCoder.decodeConsuming(bytes, Tezos.Prefix.ProtocolHash)

        return decodeProposalsList(bytes, decoded + proposal)
    }

    private fun decodeInlinedPreendorsement(bytes: MutableList<Byte>): InlinedPreendorsement {
        val branch = base58BytesCoder.decodeConsuming(bytes, Tezos.Prefix.BlockHash)
        val operations = decodePreendorsement(bytes)
        val signature = signatureBytesCoder.decodeConsuming(bytes)

        return InlinedPreendorsement(branch, operations, signature)
    }

    private fun decodeParameters(bytes: MutableList<Byte>): Parameters {
        val entrypoint = decodeEntrypoint(bytes)

        val valueLength = decodeInt32(bytes)
        val value = MichelineNode.decodeFromBytes(bytes.consumeAt(0 until valueLength).toByteArray())

        return Parameters(entrypoint, value)
    }

    private fun decodeScript(bytes: MutableList<Byte>): Script {
        val codeLength = decodeInt32(bytes)
        val code = MichelineNode.decodeFromBytes(bytes.consumeAt(0 until codeLength).toByteArray())

        val storageLength = decodeInt32(bytes)
        val storage = MichelineNode.decodeFromBytes(bytes.consumeAt(0 until storageLength).toByteArray())

        return Script(code, storage)
    }

    private tailrec fun decodeFitness(bytes: MutableList<Byte>, decoded: List<Fitness> = emptyList()): List<Fitness> {
        if (bytes.isEmpty()) return decoded

        val level = decodeInt32(bytes)

        val lockedRoundPresence = decodeBoolean(bytes)
        val lockedRound = if (lockedRoundPresence) decodeInt32(bytes) else null

        val predecessorRound = decodeInt32(bytes)
        val round = decodeInt32(bytes)

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
                val value = decodeString(bytes, length.toInt())

                Entrypoint.Named(value)
            }
            else -> failWithInvalidEncodedOperationContent()
        }

    private fun encodeString(value: String): ByteArray = value.toByteArray(charset = Charsets.UTF_8)
    private fun encodeHexString(value: String): ByteArray = value.asHexString().toByteArray()

    private fun encodeUInt8(value: UByte): ByteArray = byteArrayOf(value.toByte())
    private fun encodeUInt16(value: UShort): ByteArray = BigInt.valueOf(value.toLong()).toByteArray().asInt16Encoded()
    private fun encodeInt32(value: Int): ByteArray = BigInt.valueOf(value).toByteArray().asInt32Encoded()
    private fun encodeInt64(value: Long): ByteArray = BigInt.valueOf(value).toByteArray().asInt64Encoded()

    private fun encodeBoolean(value: Boolean): ByteArray = if (value) byteArrayOf((255).toByte()) else byteArrayOf(0)

    private fun decodeString(bytes: MutableList<Byte>, size: Int): String = bytes.consumeAt(0 until size).toByteArray().decodeToString()
    private fun decodeHexString(bytes: MutableList<Byte>, size: Int): String = bytes.consumeAt(0 until size).toHexString().asString()

    private fun decodeUInt8(bytes: MutableList<Byte>): UByte = bytes.consumeAt(0)?.toUByte() ?: failWithInvalidEncodedOperationContent()
    private fun decodeUInt16(bytes: MutableList<Byte>): UShort = BigInt.valueOf(bytes.consumeAt(0 until 2).toByteArray()).toShortExact().toUShort()
    private fun decodeInt32(bytes: MutableList<Byte>): Int = BigInt.valueOf(bytes.consumeAt(0 until 4).toByteArray()).toIntExact()
    private fun decodeInt64(bytes: MutableList<Byte>): Long = BigInt.valueOf(bytes.consumeAt(0 until 8).toByteArray()).toLongExact()

    private fun decodeBoolean(bytes: MutableList<Byte>): Boolean = when (bytes.consumeAt(0)?.toUByte()) {
        (255).toUByte() -> true
        (0).toUByte() -> false
        else -> failWithInvalidEncodedOperationContent()
    }

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
        else values.find { bytes.startsWith(it.tag) }?.also { bytes.consumeAt(0 until it.tag.size) }

    private fun OperationContent.Ballot.BallotType.Companion.recognizeConsuming(bytes: MutableList<Byte>): OperationContent.Ballot.BallotType? =
        if (bytes.isEmpty()) null
        else OperationContent.Ballot.BallotType.values().find { bytes.startsWith(it.value) }?.also { bytes.consumeAt(0 until it.value.size) }

    private fun failWithInvalidEncodedOperationContent(): Nothing = failWithIllegalArgument("Invalid encoded OperationContent value.")
    private fun failWithInvalidTag(expected: OperationContent.Kind): Nothing = failWithIllegalArgument("Invalid tag, encoded value is not ${expected::class.qualifiedName}")
}