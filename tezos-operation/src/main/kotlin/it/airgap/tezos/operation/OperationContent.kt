package it.airgap.tezos.operation

import it.airgap.tezos.core.internal.type.BytesTag
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.operation.contract.Parameters
import it.airgap.tezos.operation.contract.Script
import it.airgap.tezos.operation.header.FullHeader
import it.airgap.tezos.operation.inlined.InlinedEndorsement
import it.airgap.tezos.operation.inlined.InlinedPreendorsement

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
        public val nonce: String,
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
        public val bh1: FullHeader,
        public val bh2: FullHeader,
    ) : OperationContent {
        public companion object : Kind {
            override val tag: UByte = 3U
        }
    }

    public data class ActivateAccount(
        public val pkh: String,
        public val secret: String,
    ) : OperationContent {
        public companion object : Kind {
            override val tag: UByte = 4U
        }
    }

    public data class Proposals(
        public val source: String,
        public val period: Int,
        public val proposals: List<String>,
    ) : OperationContent {
        public companion object : Kind {
            override val tag: UByte = 5U
        }
    }

    public data class Ballot(
        public val source: String,
        public val period: Int,
        public val proposal: String,
        public val ballot: BallotType,
    ) : OperationContent {
        public enum class BallotType(override val value: ByteArray) : BytesTag {
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
        public val arbitrary: String,
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
        public val blockPayloadHash: String
    }

    public data class Preendorsement(
        override val slot: UShort,
        override val level: Int,
        override val round: Int,
        override val blockPayloadHash: String,
    ) : Consensus {
        public companion object : Kind {
            override val tag: UByte = 20U
        }
    }

    public data class Endorsement(
        override val slot: UShort,
        override val level: Int,
        override val round: Int,
        override val blockPayloadHash: String,
    ) : Consensus {
        public companion object : Kind {
            override val tag: UByte = 21U
        }
    }

    // -- manager --

    public sealed interface Manager : OperationContent {
        public val source: String
        public val fee: String
        public val counter: String
        public val gasLimit: String
        public val storageLimit: String
    }

    public data class Reveal(
        override val source: String,
        override val fee: String,
        override val counter: String,
        override val gasLimit: String,
        override val storageLimit: String,
        public val publicKey: String,
    ) : Manager {
        public companion object : Kind {
            override val tag: UByte = 107U
        }
    }

    public data class Transaction(
        override val source: String,
        override val fee: String,
        override val counter: String,
        override val gasLimit: String,
        override val storageLimit: String,
        public val amount: String,
        public val destination: String,
        public val parameters: Parameters? = null,
    ) : Manager {
        public companion object : Kind {
            override val tag: UByte = 108U
        }
    }

    public data class Origination(
        override val source: String,
        override val fee: String,
        override val counter: String,
        override val gasLimit: String,
        override val storageLimit: String,
        public val balance: String,
        public val delegate: String? = null,
        public val script: Script,
    ) : Manager {
        public companion object : Kind {
            override val tag: UByte = 109U
        }
    }

    public data class Delegation(
        override val source: String,
        override val fee: String,
        override val counter: String,
        override val gasLimit: String,
        override val storageLimit: String,
        public val delegate: String? = null,
    ) : Manager {
        public companion object : Kind {
            override val tag: UByte = 110U
        }
    }

    public data class RegisterGlobalConstant(
        override val source: String,
        override val fee: String,
        override val counter: String,
        override val gasLimit: String,
        override val storageLimit: String,
        public val value: MichelineNode,
    ) : Manager {
        public companion object : Kind {
            override val tag: UByte = 111U
        }
    }

    public data class SetDepositsLimit(
        override val source: String,
        override val fee: String,
        override val counter: String,
        override val gasLimit: String,
        override val storageLimit: String,
        public val limit: String? = null,
    ) : Manager {
        public companion object : Kind {
            override val tag: UByte = 112U
        }
    }

    public companion object {}
}