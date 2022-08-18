package it.airgap.tezos.michelson.internal.serializer

import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.MichelineSequence

internal object MichelineSequenceSerializer : KListSerializer<MichelineSequence, Micheline>(Micheline.serializer()) {
    override fun valueFromList(list: List<Micheline>): MichelineSequence = MichelineSequence(list)
    override fun valueToList(value: MichelineSequence): List<Micheline> = value.nodes
}