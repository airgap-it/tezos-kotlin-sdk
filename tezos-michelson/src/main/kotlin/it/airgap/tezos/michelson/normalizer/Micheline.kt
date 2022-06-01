package it.airgap.tezos.michelson.normalizer

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.normalizer.Normalizer
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

/**
 * Normalizes [MichelineNode] to a uniform form.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * Currently, only pairs are subjected to normalization.
 *
 * For example:
 * ```json
 * {
 *      "prim": "Pair",
 *      "args": [
 *          {
 *              "int": "1"
 *          },
 *          {
 *              "int": "2"
 *          },
 *          {
 *              "int": "3"
 *          }
 *      ]
 * }
 * ```
 * will be normalized to:
 *```json
 * {
 *      "prim": "Pair",
 *      "args": [
 *          {
 *              "int": "1"
 *          },
 *          {
 *              "prim": "Pair",
 *              "args": [
 *                  {
 *                      "int": "2"
 *                  },
 *                  {
 *                      "int": "3"
 *                  }
 *             ]
 *          }
 *      ]
 * }
 * ```
 *
 * See `samples/src/test/kotlin/michelson/Micheline/MichelineSamples.Usage#normalize` for a sample usage.
 */
public fun <T : MichelineNode> T.normalized(tezos: Tezos = Tezos.Default): MichelineNode = withTezosContext {
    normalized(tezos.michelsonModule.dependencyRegistry.michelineNormalizer)
}

/**
 * Normalizes [MichelinePrimitiveApplication] to a uniform form.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/michelson/Micheline/MichelineSamples.Usage#normalize` for a sample usage.
 *
 * @see [MichelineNode.normalized]
 */
public fun MichelinePrimitiveApplication.normalized(tezos: Tezos = Tezos.Default): MichelinePrimitiveApplication = withTezosContext {
    normalized(tezos.michelsonModule.dependencyRegistry.michelinePrimitiveApplicationNormalizer)
}

/**
 * Normalizes [MichelineSequence] to a uniform form.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/michelson/Micheline/MichelineSamples.Usage#normalize` for a sample usage.
 *
 * @see [MichelineNode.normalized]
 */
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