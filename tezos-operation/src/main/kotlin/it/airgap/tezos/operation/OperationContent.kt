package it.airgap.tezos.operation

import it.airgap.tezos.core.internal.type.BytesTag
import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.core.type.number.TezosNatural
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.operation.contract.Parameters
import it.airgap.tezos.operation.contract.Script
import it.airgap.tezos.operation.header.BlockHeader
import it.airgap.tezos.operation.inlined.InlinedEndorsement
import it.airgap.tezos.operation.inlined.InlinedPreendorsement

/**
 * Tezos operation content.
 *
 * See [P2P message format](https://tezos.gitlab.io/shell/p2p_api.html#alpha-operation-alpha-contents-determined-from-data-8-bit-tag)
 * for more details.
 */
public sealed interface OperationContent {
    public sealed interface Kind {
        public val tag: UByte

        public companion object {
            internal val values: List<Kind>
                get() = listOf(
                    SeedNonceRevelation,
                    DoubleEndorsementEvidence,
                    DoubleBakingEvidence,
                    ActivateAccount,
                    Proposals,
                    Ballot,
                    DoublePreendorsementEvidence,
                    FailingNoop,
                    Preendorsement,
                    Endorsement,
                    Reveal,
                    Transaction,
                    Origination,
                    Delegation,
                    RegisterGlobalConstant,
                    SetDepositsLimit,
                )
        }
    }

    public data class SeedNonceRevelation(
        public val level: Int,
        public val nonce: HexString,
    ) : OperationContent {
        public companion object : Kind {
            override val tag: UByte = 1U
        }
    }

    public data class DoubleEndorsementEvidence(
        public val op1: InlinedEndorsement,
        public val op2: InlinedEndorsement,
    ) : OperationContent  {
        public companion object : Kind {
            override val tag: UByte = 2U
        }
    }

    public data class DoubleBakingEvidence(
        public val bh1: BlockHeader,
        public val bh2: BlockHeader,
    ) : OperationContent {
        public companion object : Kind {
            override val tag: UByte = 3U
        }
    }

    public data class ActivateAccount(
        public val pkh: Ed25519PublicKeyHash,
        public val secret: HexString,
    ) : OperationContent {
        public companion object : Kind {
            override val tag: UByte = 4U
        }
    }

    public data class Proposals(
        public val source: ImplicitAddress,
        public val period: Int,
        public val proposals: List<ProtocolHash>,
    ) : OperationContent {
        public companion object : Kind {
            override val tag: UByte = 5U
        }
    }

    public data class Ballot(
        public val source: ImplicitAddress,
        public val period: Int,
        public val proposal: ProtocolHash,
        public val ballot: Type,
    ) : OperationContent {
        public enum class Type(override val value: ByteArray) : BytesTag {
            Yay(byteArrayOf(0)),
            Nay(byteArrayOf(1)),
            Pass(byteArrayOf(2));

            public companion object {}
        }

        public companion object : Kind {
            override val tag: UByte = 6U
        }
    }

    public data class DoublePreendorsementEvidence(
        public val op1: InlinedPreendorsement,
        public val op2: InlinedPreendorsement,
    ) : OperationContent {
        public companion object : Kind {
            override val tag: UByte = 7U
        }
    }

    public data class FailingNoop(
        public val arbitrary: HexString,
    ) : OperationContent {
        public companion object : Kind {
            override val tag: UByte = 17U
        }
    }

    // -- consensus --

    public sealed interface Consensus : OperationContent {
        public val slot: UShort
        public val level: Int
        public val round: Int
        public val blockPayloadHash: BlockPayloadHash
    }

    public data class Preendorsement(
        override val slot: UShort,
        override val level: Int,
        override val round: Int,
        override val blockPayloadHash: BlockPayloadHash,
    ) : Consensus {
        public companion object : Kind {
            override val tag: UByte = 20U
        }
    }

    public data class Endorsement(
        override val slot: UShort,
        override val level: Int,
        override val round: Int,
        override val blockPayloadHash: BlockPayloadHash,
    ) : Consensus {
        public companion object : Kind {
            override val tag: UByte = 21U
        }
    }

    // -- manager --

    public sealed interface Manager : OperationContent {
        public val source: ImplicitAddress
        public val fee: Mutez
        public val counter: TezosNatural
        public val gasLimit: TezosNatural
        public val storageLimit: TezosNatural
    }

    public data class Reveal(
        override val source: ImplicitAddress,
        override val fee: Mutez = Mutez(0),
        override val counter: TezosNatural,
        override val gasLimit: TezosNatural = TezosNatural(0U),
        override val storageLimit: TezosNatural = TezosNatural(0U),
        public val publicKey: PublicKey,
    ) : Manager {
        public companion object : Kind {
            override val tag: UByte = 107U
        }
    }

    public data class Transaction(
        override val source: ImplicitAddress,
        override val fee: Mutez = Mutez(0),
        override val counter: TezosNatural,
        override val gasLimit: TezosNatural = TezosNatural(0U),
        override val storageLimit: TezosNatural = TezosNatural(0U),
        public val amount: Mutez,
        public val destination: Address,
        public val parameters: Parameters? = null,
    ) : Manager {
        public companion object : Kind {
            override val tag: UByte = 108U
        }
    }

    public data class Origination(
        override val source: ImplicitAddress,
        override val fee: Mutez = Mutez(0),
        override val counter: TezosNatural,
        override val gasLimit: TezosNatural = TezosNatural(0U),
        override val storageLimit: TezosNatural = TezosNatural(0U),
        public val balance: Mutez,
        public val delegate: ImplicitAddress? = null,
        public val script: Script,
    ) : Manager {
        public companion object : Kind {
            override val tag: UByte = 109U
        }
    }

    public data class Delegation(
        override val source: ImplicitAddress,
        override val fee: Mutez = Mutez(0),
        override val counter: TezosNatural,
        override val gasLimit: TezosNatural = TezosNatural(0U),
        override val storageLimit: TezosNatural = TezosNatural(0U),
        public val delegate: ImplicitAddress? = null,
    ) : Manager {
        public companion object : Kind {
            override val tag: UByte = 110U
        }
    }

    public data class RegisterGlobalConstant(
        override val source: ImplicitAddress,
        override val fee: Mutez = Mutez(0),
        override val counter: TezosNatural,
        override val gasLimit: TezosNatural = TezosNatural(0U),
        override val storageLimit: TezosNatural = TezosNatural(0U),
        public val value: Micheline,
    ) : Manager {
        public companion object : Kind {
            override val tag: UByte = 111U
        }
    }

    public data class SetDepositsLimit(
        override val source: ImplicitAddress,
        override val fee: Mutez = Mutez(0),
        override val counter: TezosNatural,
        override val gasLimit: TezosNatural = TezosNatural(0U),
        override val storageLimit: TezosNatural = TezosNatural(0U),
        public val limit: Mutez? = null,
    ) : Manager {
        public companion object : Kind {
            override val tag: UByte = 112U
        }
    }

    public companion object {}
}