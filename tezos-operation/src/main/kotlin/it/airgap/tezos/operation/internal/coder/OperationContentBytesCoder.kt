package it.airgap.tezos.operation.internal.coder

import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.type.BigInt
import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.core.type.number.TezosNatural
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.contract.Entrypoint
import it.airgap.tezos.operation.contract.Parameters
import it.airgap.tezos.operation.contract.Script
import it.airgap.tezos.operation.header.BlockHeader
import it.airgap.tezos.operation.header.LiquidityBakingToggleVote
import it.airgap.tezos.operation.inlined.InlinedEndorsement
import it.airgap.tezos.operation.inlined.InlinedPreendorsement
import it.airgap.tezos.operation.internal.context.TezosOperationContext.consumeAt
import it.airgap.tezos.operation.internal.context.TezosOperationContext.consumeUntil
import it.airgap.tezos.operation.internal.context.TezosOperationContext.decodeConsumingBoolean
import it.airgap.tezos.operation.internal.context.TezosOperationContext.decodeConsumingFromBytes
import it.airgap.tezos.operation.internal.context.TezosOperationContext.decodeConsumingHexString
import it.airgap.tezos.operation.internal.context.TezosOperationContext.decodeConsumingInt32
import it.airgap.tezos.operation.internal.context.TezosOperationContext.decodeConsumingInt64
import it.airgap.tezos.operation.internal.context.TezosOperationContext.decodeConsumingList
import it.airgap.tezos.operation.internal.context.TezosOperationContext.decodeConsumingString
import it.airgap.tezos.operation.internal.context.TezosOperationContext.decodeConsumingUInt16
import it.airgap.tezos.operation.internal.context.TezosOperationContext.decodeConsumingUInt8
import it.airgap.tezos.operation.internal.context.TezosOperationContext.decodeFromBytes
import it.airgap.tezos.operation.internal.context.TezosOperationContext.encodeToBytes
import it.airgap.tezos.operation.internal.context.TezosOperationContext.failWithIllegalArgument
import it.airgap.tezos.operation.internal.context.TezosOperationContext.fromTagOrNull
import it.airgap.tezos.operation.internal.context.TezosOperationContext.startsWith

internal class OperationContentBytesCoder(
    private val encodedBytesCoder: EncodedBytesCoder,
    private val addressBytesCoder: ConsumingBytesCoder<Address>,
    private val publicKeyBytesCoder: ConsumingBytesCoder<PublicKey>,
    private val implicitAddressBytesCoder: ConsumingBytesCoder<ImplicitAddress>,
    private val signatureBytesCoder: ConsumingBytesCoder<Signature>,
    private val tezosNaturalBytesCoder: ConsumingBytesCoder<TezosNatural>,
    private val mutezBytesCoder: ConsumingBytesCoder<Mutez>,
    private val michelineBytesCoder: ConsumingBytesCoder<Micheline>,
    private val timestampBigIntCoder: Coder<Timestamp, BigInt>,
    private val tagToOperationContentKindConverter: Converter<UByte, OperationContent.Kind>,
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
        val levelBytes = level.encodeToBytes()
        val nonceBytes = nonce.encodeToBytes()

        OperationContent.SeedNonceRevelation.tag + levelBytes + nonceBytes
    }

    private fun encodeDoubleEndorsementEvidence(content: OperationContent.DoubleEndorsementEvidence): ByteArray =
        with(content) {
            val op1Bytes = op1.encodeToBytes()
            val op1Length = op1Bytes.size.encodeToBytes()

            val op2Bytes = op2.encodeToBytes()
            val op2Length = op2Bytes.size.encodeToBytes()

            OperationContent.DoubleEndorsementEvidence.tag + op1Length + op1Bytes + op2Length + op2Bytes
        }

    private fun encodeDoubleBakingEvidence(content: OperationContent.DoubleBakingEvidence): ByteArray = with(content) {
        val bh1Bytes = bh1.encodeToBytes()
        val bh1Length = bh1Bytes.size.encodeToBytes()

        val bh2Bytes = bh2.encodeToBytes()
        val bh2Length = bh2Bytes.size.encodeToBytes()

        OperationContent.DoubleBakingEvidence.tag + bh1Length + bh1Bytes + bh2Length + bh2Bytes
    }

    private fun encodeActivateAccount(content: OperationContent.ActivateAccount): ByteArray = with(content) {
        val pkhBytes = pkh.encodeToBytes(encodedBytesCoder)
        val secretBytes = secret.encodeToBytes()

        OperationContent.ActivateAccount.tag + pkhBytes + secretBytes
    }

    private fun encodeProposals(content: OperationContent.Proposals): ByteArray = with(content) {
        val sourceBytes = source.encodeToBytes(implicitAddressBytesCoder)
        val periodBytes = period.encodeToBytes()

        val proposalsBytes = proposals.encodeToBytes { it.encodeToBytes(encodedBytesCoder) }
        val proposalsLength = proposalsBytes.size.encodeToBytes()

        OperationContent.Proposals.tag + sourceBytes + periodBytes + proposalsLength + proposalsBytes
    }

    private fun encodeBallot(content: OperationContent.Ballot): ByteArray = with(content) {
        val sourceBytes = source.encodeToBytes(implicitAddressBytesCoder)
        val periodBytes = period.encodeToBytes()
        val proposalBytes = proposal.encodeToBytes(encodedBytesCoder)
        val ballotBytes = ballot.value

        OperationContent.Ballot.tag + sourceBytes + periodBytes + proposalBytes + ballotBytes
    }

    private fun encodeDoublePreendorsementEvidence(content: OperationContent.DoublePreendorsementEvidence): ByteArray =
        with(content) {
            val op1Bytes = op1.encodeToBytes()
            val op1Length = op1Bytes.size.encodeToBytes()

            val op2Bytes = op2.encodeToBytes()
            val op2Length = op2Bytes.size.encodeToBytes()

            OperationContent.DoublePreendorsementEvidence.tag + op1Length + op1Bytes + op2Length + op2Bytes
        }

    private fun encodeFailingNoop(content: OperationContent.FailingNoop): ByteArray = with(content) {
        val bytes = arbitrary.encodeToBytes()
        val length = bytes.size.encodeToBytes()

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

    private fun encodeTransaction(content: OperationContent.Transaction): ByteArray = with(content) {
        val amountBytes = amount.encodeToBytes(mutezBytesCoder)
        val destinationBytes = destination.encodeToBytes(addressBytesCoder)

        val parametersBytes = parameters?.encodeToBytes() ?: byteArrayOf()
        val parametersPresence = parametersBytes.isNotEmpty().encodeToBytes()

        OperationContent.Transaction.tag + encodeManagerOperation(this) + amountBytes + destinationBytes + parametersPresence + parametersBytes
    }

    private fun encodeOrigination(content: OperationContent.Origination): ByteArray = with(content) {
        val balanceBytes = balance.encodeToBytes(mutezBytesCoder)

        val delegateBytes = delegate?.encodeToBytes(implicitAddressBytesCoder) ?: byteArrayOf()
        val delegatePresence = delegateBytes.isNotEmpty().encodeToBytes()

        val scriptBytes = script.encodeToBytes()

        OperationContent.Origination.tag + encodeManagerOperation(this) + balanceBytes + delegatePresence + delegateBytes + scriptBytes
    }

    private fun encodeDelegation(content: OperationContent.Delegation): ByteArray = with(content) {
        val delegateBytes = delegate?.encodeToBytes(implicitAddressBytesCoder) ?: byteArrayOf()
        val delegatePresence = delegateBytes.isNotEmpty().encodeToBytes()

        OperationContent.Delegation.tag + encodeManagerOperation(content) + delegatePresence + delegateBytes
    }

    private fun encodeRegisterGlobalConstant(content: OperationContent.RegisterGlobalConstant): ByteArray =
        with(content) {
            val valueBytes = value.encodeToBytes(michelineBytesCoder)
            val valueLength = valueBytes.size.encodeToBytes()

            OperationContent.RegisterGlobalConstant.tag + encodeManagerOperation(this) + valueLength + valueBytes
        }

    private fun encodeSetDepositsLimit(content: OperationContent.SetDepositsLimit): ByteArray = with(content) {
        val limitBytes = limit?.encodeToBytes(mutezBytesCoder) ?: byteArrayOf()
        val limitPresence = limitBytes.isNotEmpty().encodeToBytes()

        OperationContent.SetDepositsLimit.tag + encodeManagerOperation(this) + limitPresence + limitBytes
    }

    private fun encodeConsensusOperation(content: OperationContent.Consensus): ByteArray = with(content) {
        val slotBytes = slot.encodeToBytes()
        val levelBytes = level.encodeToBytes()
        val roundBytes = round.encodeToBytes()
        val blockPayloadHashBytes = blockPayloadHash.encodeToBytes(encodedBytesCoder)

        slotBytes + levelBytes + roundBytes + blockPayloadHashBytes
    }

    private fun encodeManagerOperation(content: OperationContent.Manager): ByteArray = with(content) {
        val sourceBytes = source.encodeToBytes(implicitAddressBytesCoder)
        val feeBytes = fee.encodeToBytes(mutezBytesCoder)
        val counterBytes = counter.encodeToBytes(tezosNaturalBytesCoder)
        val gasLimitBytes = gasLimit.encodeToBytes(tezosNaturalBytesCoder)
        val storageLimitBytes = storageLimit.encodeToBytes(tezosNaturalBytesCoder)

        sourceBytes + feeBytes + counterBytes + gasLimitBytes + storageLimitBytes
    }

    private fun InlinedEndorsement.encodeToBytes(): ByteArray {
        val branchBytes = branch.encodeToBytes(encodedBytesCoder)
        val operationsBytes = encodeEndorsement(operations)
        val signatureBytes = signature.encodeToBytes(signatureBytesCoder)

        return branchBytes + operationsBytes + signatureBytes
    }

    private fun BlockHeader.encodeToBytes(): ByteArray {
        val levelBytes = level.encodeToBytes()
        val protoBytes = proto.encodeToBytes()
        val predecessorBytes = predecessor.encodeToBytes(encodedBytesCoder)
        val timestampBytes = encodeTimestamp(timestamp)
        val validationPassBytes = validationPass.encodeToBytes()
        val operationsHashBytes = operationsHash.encodeToBytes(encodedBytesCoder)

        val fitnessBytes = fitness.encodeToBytes()
        val fitnessLength = fitnessBytes.size.encodeToBytes()

        val contextBytes = context.encodeToBytes(encodedBytesCoder)
        val payloadHashBytes = payloadHash.encodeToBytes(encodedBytesCoder)
        val payloadRoundBytes = payloadRound.encodeToBytes()
        val proofOfWorkNonceBytes = proofOfWorkNonce.encodeToBytes()

        val seedNonceHashBytes = seedNonceHash?.encodeToBytes(encodedBytesCoder) ?: byteArrayOf()
        val seedNonceHashPresence = seedNonceHashBytes.isNotEmpty().encodeToBytes()

        val liquidityBankingToggleVoteBytes = encodeLiquidityBakingToggleVote(liquidityBakingToggleVote)
        val signatureBytes = signature.encodeToBytes(signatureBytesCoder)

        return levelBytes +
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
                liquidityBankingToggleVoteBytes +
                signatureBytes
    }

    private fun InlinedPreendorsement.encodeToBytes(): ByteArray {
        val branchBytes = branch.encodeToBytes(encodedBytesCoder)
        val operationsBytes = encodePreendorsement(operations)
        val signatureBytes = signature.encodeToBytes(signatureBytesCoder)

        return branchBytes + operationsBytes + signatureBytes
    }

    private fun Parameters.encodeToBytes(): ByteArray {
        val entrypointBytes = entrypoint.encodeToBytes()

        val valueBytes = value.encodeToBytes(michelineBytesCoder)
        val valueLength = valueBytes.size.encodeToBytes()

        return entrypointBytes + valueLength + valueBytes
    }

    private fun Script.encodeToBytes(): ByteArray {
        val codeBytes = code.encodeToBytes(michelineBytesCoder)
        val codeLength = codeBytes.size.encodeToBytes()

        val storageBytes = storage.encodeToBytes(michelineBytesCoder)
        val storageLength = storageBytes.size.encodeToBytes()

        return codeLength + codeBytes + storageLength + storageBytes
    }

    private fun List<HexString>.encodeToBytes(): ByteArray =
        this.encodeToBytes {
            val bytes = it.encodeToBytes()
            val length = bytes.size.encodeToBytes()

            length + bytes
        }

    private fun Entrypoint.encodeToBytes(): ByteArray =
        when (this) {
            Entrypoint.Default -> Entrypoint.Default.tag
            Entrypoint.Root -> Entrypoint.Root.tag
            Entrypoint.Do -> Entrypoint.Do.tag
            Entrypoint.SetDelegate -> Entrypoint.SetDelegate.tag
            Entrypoint.RemoveDelegate -> Entrypoint.RemoveDelegate.tag
            is Entrypoint.Named -> {
                val bytes = value.encodeToBytes()
                val length = bytes.size.toUByte().encodeToBytes()

                Entrypoint.Named.tag + length + bytes
            }
        }

    private fun encodeTimestamp(timestamp: Timestamp): ByteArray {
        val long = timestampBigIntCoder.encode(timestamp).toLongExact()
        return long.encodeToBytes()
    }

    private fun encodeLiquidityBakingToggleVote(toggleVote: LiquidityBakingToggleVote): ByteArray =
        toggleVote.value

    private fun decodeSeedNonceRevelation(bytes: MutableList<Byte>): OperationContent.SeedNonceRevelation {
        requireConsumingKind(OperationContent.SeedNonceRevelation, bytes)

        val level = bytes.decodeConsumingInt32()
        val nonce = bytes.decodeConsumingHexString(32)

        return OperationContent.SeedNonceRevelation(level, nonce)
    }

    private fun decodeDoubleEndorsementEvidence(bytes: MutableList<Byte>): OperationContent.DoubleEndorsementEvidence {
        requireConsumingKind(OperationContent.DoubleEndorsementEvidence, bytes)

        val op1Length = bytes.decodeConsumingInt32()
        val op1 = decodeInlinedEndorsement(bytes.consumeUntil(op1Length))

        val op2Length = bytes.decodeConsumingInt32()
        val op2 = decodeInlinedEndorsement(bytes.consumeUntil(op2Length))

        return OperationContent.DoubleEndorsementEvidence(op1, op2)
    }

    private fun decodeDoubleBakingEvidence(bytes: MutableList<Byte>): OperationContent.DoubleBakingEvidence {
        requireConsumingKind(OperationContent.DoubleBakingEvidence, bytes)

        val bh1Length = bytes.decodeConsumingInt32()
        val bh1 = decodeBlockHeader(bytes.consumeUntil(bh1Length))

        val bh2Length = bytes.decodeConsumingInt32()
        val bh2 = decodeBlockHeader(bytes.consumeUntil(bh2Length))

        return OperationContent.DoubleBakingEvidence(bh1, bh2)
    }

    private fun decodeActivateAccount(bytes: MutableList<Byte>): OperationContent.ActivateAccount {
        requireConsumingKind(OperationContent.ActivateAccount, bytes)

        val pkh = Ed25519PublicKeyHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)
        val secret = bytes.decodeConsumingHexString(20)

        return OperationContent.ActivateAccount(pkh, secret)
    }

    private fun decodeProposals(bytes: MutableList<Byte>): OperationContent.Proposals {
        requireConsumingKind(OperationContent.Proposals, bytes)

        val source = ImplicitAddress.decodeConsumingFromBytes(bytes, implicitAddressBytesCoder)
        val period = bytes.decodeConsumingInt32()

        val proposalsLength = bytes.decodeConsumingInt32()
        val proposals = bytes.consumeUntil(proposalsLength).decodeConsumingList { ProtocolHash.decodeConsumingFromBytes(it, encodedBytesCoder) }

        return OperationContent.Proposals(source, period, proposals)
    }

    private fun decodeBallot(bytes: MutableList<Byte>): OperationContent.Ballot {
        requireConsumingKind(OperationContent.Ballot, bytes)

        val source = ImplicitAddress.decodeConsumingFromBytes(bytes, implicitAddressBytesCoder)
        val period = bytes.decodeConsumingInt32()
        val proposal = ProtocolHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)
        val ballot = OperationContent.Ballot.Type.recognizeConsuming(bytes) ?: failWithInvalidEncodedOperationContent()

        return OperationContent.Ballot(source, period, proposal, ballot)
    }

    private fun decodeDoublePreendorsementEvidence(bytes: MutableList<Byte>): OperationContent.DoublePreendorsementEvidence {
        requireConsumingKind(OperationContent.DoublePreendorsementEvidence, bytes)

        val op1Length = bytes.decodeConsumingInt32()
        val op1 = decodeInlinedPreendorsement(bytes.consumeUntil(op1Length))

        val op2Length = bytes.decodeConsumingInt32()
        val op2 = decodeInlinedPreendorsement(bytes.consumeUntil(op2Length))

        return OperationContent.DoublePreendorsementEvidence(op1, op2)
    }

    private fun decodeFailingNoop(bytes: MutableList<Byte>): OperationContent.FailingNoop {
        requireConsumingKind(OperationContent.FailingNoop, bytes)

        val length = bytes.decodeConsumingInt32()
        val arbitrary = bytes.decodeConsumingHexString(length)

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
            val publicKey = PublicKey.decodeConsumingFromBytes(bytes, publicKeyBytesCoder)

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
            val amount = Mutez.decodeConsumingFromBytes(bytes, mutezBytesCoder)
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
            val balance = Mutez.decodeConsumingFromBytes(bytes, mutezBytesCoder)

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
            val valueLength = bytes.decodeConsumingInt32()
            val value = Micheline.decodeFromBytes(bytes.consumeUntil(valueLength).toByteArray(), michelineBytesCoder)
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
            val limit = if (limitPresence) Mutez.decodeConsumingFromBytes(bytes, mutezBytesCoder) else null

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
        val slot = bytes.decodeConsumingUInt16()
        val level = bytes.decodeConsumingInt32()
        val round = bytes.decodeConsumingInt32()
        val blockPayloadHash = BlockPayloadHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)

        return create(slot, level, round, blockPayloadHash, bytes)
    }

    private fun <T : OperationContent.Manager> decodeManagerOperation(
        bytes: MutableList<Byte>,
        create: (source: ImplicitAddress, fee: Mutez, counter: TezosNatural, gasLimit: TezosNatural, storageLimit: TezosNatural, MutableList<Byte>) -> T
    ): T {
        val source = ImplicitAddress.decodeConsumingFromBytes(bytes, implicitAddressBytesCoder)
        val fee = Mutez.decodeConsumingFromBytes(bytes, mutezBytesCoder)
        val counter = TezosNatural.decodeConsumingFromBytes(bytes, tezosNaturalBytesCoder)
        val gasLimit = TezosNatural.decodeConsumingFromBytes(bytes, tezosNaturalBytesCoder)
        val storageLimit = TezosNatural.decodeConsumingFromBytes(bytes, tezosNaturalBytesCoder)

        return create(source, fee, counter, gasLimit, storageLimit, bytes)
    }

    private fun decodeInlinedEndorsement(bytes: MutableList<Byte>): InlinedEndorsement {
        val branch = BlockHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)
        val operations = decodeEndorsement(bytes)
        val signature = Signature.decodeConsumingFromBytes(bytes, signatureBytesCoder)

        return InlinedEndorsement(branch, operations, signature)
    }

    private fun decodeBlockHeader(bytes: MutableList<Byte>): BlockHeader {
        val level = bytes.decodeConsumingInt32()
        val proto = decodeUInt8(bytes)
        val predecessor = BlockHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)
        val timestamp = decodeTimestamp(bytes)
        val validationPass = decodeUInt8(bytes)
        val operationsHash = OperationListListHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)

        val fitnessLength = bytes.decodeConsumingInt32()
        val fitness = decodeFitness(bytes.consumeUntil(fitnessLength))

        val context = ContextHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)
        val payloadHash = BlockPayloadHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)
        val payloadRound = bytes.decodeConsumingInt32()
        val proofOfWorkNonce = bytes.decodeConsumingHexString(8)

        val seedNonceHashPresence = decodeBoolean(bytes)
        val seedNonceHash = if (seedNonceHashPresence) NonceHash.decodeConsumingFromBytes(bytes, encodedBytesCoder) else null

        val liquidityBakingToggleVote = decodeLiquidityBakingToggleVote(bytes)
        val signature = Signature.decodeConsumingFromBytes(bytes, signatureBytesCoder)

        return BlockHeader(
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
            liquidityBakingToggleVote,
            signature,
        )
    }

    private fun decodeInlinedPreendorsement(bytes: MutableList<Byte>): InlinedPreendorsement {
        val branch = BlockHash.decodeConsumingFromBytes(bytes, encodedBytesCoder)
        val operations = decodePreendorsement(bytes)
        val signature = Signature.decodeConsumingFromBytes(bytes, signatureBytesCoder)

        return InlinedPreendorsement(branch, operations, signature)
    }

    private fun decodeParameters(bytes: MutableList<Byte>): Parameters {
        val entrypoint = decodeEntrypoint(bytes)

        val valueLength = bytes.decodeConsumingInt32()
        val value = Micheline.decodeFromBytes(bytes.consumeUntil(valueLength).toByteArray(), michelineBytesCoder)

        return Parameters(entrypoint, value)
    }

    private fun decodeScript(bytes: MutableList<Byte>): Script {
        val codeLength = bytes.decodeConsumingInt32()
        val code = Micheline.decodeFromBytes(bytes.consumeUntil(codeLength).toByteArray(), michelineBytesCoder)

        val storageLength = bytes.decodeConsumingInt32()
        val storage = Micheline.decodeFromBytes(bytes.consumeUntil(storageLength).toByteArray(), michelineBytesCoder)

        return Script(code, storage)
    }

    private tailrec fun decodeFitness(bytes: MutableList<Byte>, decoded: List<HexString> = emptyList()): List<HexString> {
        if (bytes.isEmpty()) return decoded

        val length = bytes.decodeConsumingInt32()
        val fitness = bytes.decodeConsumingHexString(length)

        return decodeFitness(bytes, decoded + fitness)
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
                val value = bytes.decodeConsumingString(length.toInt())

                Entrypoint.Named(value)
            }
            else -> failWithInvalidEncodedOperationContent()
        }

    private fun decodeTimestamp(bytes: MutableList<Byte>): Timestamp {
        val bigInt = BigInt.valueOf(bytes.decodeConsumingInt64())
        return timestampBigIntCoder.decode(bigInt)
    }

    private fun decodeLiquidityBakingToggleVote(bytes: MutableList<Byte>): LiquidityBakingToggleVote {
        val toggleVote = if (bytes.isEmpty()) null else LiquidityBakingToggleVote.values().find { bytes.startsWith(it.value) }?.also { bytes.consumeUntil(it.value.size) }
        return toggleVote ?: failWithInvalidEncodedOperationContent()
    }

    private fun decodeUInt8(bytes: MutableList<Byte>): UByte = bytes.decodeConsumingUInt8() ?: failWithInvalidEncodedOperationContent()
    private fun decodeBoolean(bytes: MutableList<Byte>): Boolean = bytes.decodeConsumingBoolean() ?: failWithInvalidEncodedOperationContent()

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

    private fun OperationContent.Ballot.Type.Companion.recognizeConsuming(bytes: MutableList<Byte>): OperationContent.Ballot.Type? =
        if (bytes.isEmpty()) null
        else OperationContent.Ballot.Type.values().find { bytes.startsWith(it.value) }?.also { bytes.consumeUntil(it.value.size) }

    private fun failWithInvalidEncodedOperationContent(): Nothing = failWithIllegalArgument("Invalid encoded OperationContent value.")
    private fun failWithInvalidTag(expected: OperationContent.Kind): Nothing =
        failWithIllegalArgument("Invalid tag, encoded value is not ${expected::class.qualifiedName?.removeSuffix(".Companion")?.substringAfterLast(".")}")
}