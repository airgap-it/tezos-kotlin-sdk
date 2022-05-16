package it.airgap.tezos.michelson.normalizer

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.normalizer.Normalizer
import it.airgap.tezos.michelson.internal.michelson
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

// -- MichelineNode --

public fun <T : MichelineNode> T.normalized(tezos: Tezos = Tezos.Default): MichelineNode =
    normalized(tezos.michelson().dependencyRegistry.michelineNormalizer)

@InternalTezosSdkApi
public fun <T : MichelineNode> T.normalized(michelineToNormalizedConverter: Normalizer<MichelineNode>): MichelineNode =
    michelineToNormalizedConverter.normalize(this)

// -- MichelinePrimitiveApplication --

public fun MichelinePrimitiveApplication.normalized(tezos: Tezos = Tezos.Default): MichelinePrimitiveApplication =
    normalized(tezos.michelson().dependencyRegistry.michelinePrimitiveApplicationNormalizer)

@InternalTezosSdkApi
public fun MichelinePrimitiveApplication.normalized(michelinePrimitiveApplicationToNormalizedConverter: Normalizer<MichelinePrimitiveApplication>): MichelinePrimitiveApplication =
    michelinePrimitiveApplicationToNormalizedConverter.normalize(this)

// -- MichelineSequence --

public fun MichelineSequence.normalized(tezos: Tezos = Tezos.Default): MichelineSequence =
    normalized(tezos.michelson().dependencyRegistry.michelineSequenceNormalizer)

@InternalTezosSdkApi
public fun MichelineSequence.normalized(michelineSequenceToNormalizedConverter: Normalizer<MichelineSequence>): MichelineSequence =
    michelineSequenceToNormalizedConverter.normalize(this)