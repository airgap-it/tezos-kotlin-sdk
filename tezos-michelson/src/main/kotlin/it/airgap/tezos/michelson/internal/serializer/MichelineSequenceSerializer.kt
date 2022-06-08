package it.airgap.tezos.michelson.internal.serializer

import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelineSequence

internal object MichelineSequenceSerializer : KListSerializer<MichelineSequence, MichelineNode>(MichelineNode.serializer()) {
    override fun valueFromList(list: List<MichelineNode>): MichelineSequence = MichelineSequence(list)
    override fun valueToList(value: MichelineSequence): List<MichelineNode> = value.nodes
}