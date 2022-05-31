package it.airgap.tezos.michelson.normalizer

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.normalizer.Normalizer
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

// -- MichelineNode --

public fun <T : MichelineNode> T.normalized(tezos: Tezos = Tezos.Default): MichelineNode = withTezosContext {
    normalized(tezos.michelsonModule.dependencyRegistry.michelineNormalizer)
}

// -- MichelinePrimitiveApplication --

public fun MichelinePrimitiveApplication.normalized(tezos: Tezos = Tezos.Default): MichelinePrimitiveApplication = withTezosContext {
    normalized(tezos.michelsonModule.dependencyRegistry.michelinePrimitiveApplicationNormalizer)
}

// -- MichelineSequence --

public fun MichelineSequence.normalized(tezos: Tezos = Tezos.Default): MichelineSequence = withTezosContext {
    normalized(tezos.michelsonModule.dependencyRegistry.michelineSequenceNormalizer)
}

@InternalTezosSdkApi
public interface MichelineNormalizerContext {
    public fun <T : MichelineNode> T.normalized(michelineToNormalizedConverter: Normalizer<MichelineNode>): MichelineNode =
        michelineToNormalizedConverter.normalize(this)

    public fun MichelinePrimitiveApplication.normalized(michelinePrimitiveApplicationToNormalizedConverter: Normalizer<MichelinePrimitiveApplication>): MichelinePrimitiveApplication =
        michelinePrimitiveApplicationToNormalizedConverter.normalize(this)

    public fun MichelineSequence.normalized(michelineSequenceToNormalizedConverter: Normalizer<MichelineSequence>): MichelineSequence =
        michelineSequenceToNormalizedConverter.normalize(this)
}