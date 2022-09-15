package it.airgap.tezos.michelson.internal.di

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.di.CoreDependencyRegistry
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.normalizer.Normalizer
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.internal.coder.MichelineBytesCoder
import it.airgap.tezos.michelson.internal.coder.MichelineJsonCoder
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.lazyWeak
import it.airgap.tezos.michelson.internal.converter.*
import it.airgap.tezos.michelson.internal.normalizer.MichelineNormalizer
import it.airgap.tezos.michelson.internal.packer.BytesPacker
import it.airgap.tezos.michelson.internal.packer.MichelineToBytesPacker
import it.airgap.tezos.michelson.internal.packer.MichelineToScriptExprHashPacker
import it.airgap.tezos.michelson.internal.packer.Packer
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import kotlinx.serialization.json.JsonElement

@InternalTezosSdkApi
public class MichelsonDependencyRegistry internal constructor(
    global: DependencyRegistry,
    core: CoreDependencyRegistry,
) {

    // -- coder --

    public val michelineBytesCoder: ConsumingBytesCoder<Micheline> by lazy {
        MichelineBytesCoder(
            stringToMichelsonPrimConverter,
            tagToMichelsonPrimConverter,
            michelineToCompactStringConverter,
            core.tezosIntegerBytesCoder,
        )
    }
    public val michelineJsonCoder: Coder<Micheline, JsonElement> by lazy { Static.michelineJsonCoder }

    // -- converter --

    public val michelineToMichelsonConverter: Converter<Micheline, Michelson> by lazy { Static.michelineToMichelsonConverter }

    public val michelineToStringConverter: Converter<Micheline, String> by lazy { Static.michelineToStringConverter }
    public val michelineToCompactStringConverter: Converter<Micheline, String> by lazy { Static.michelineToCompactStringConverter }

    public val michelsonToMichelineConverter: Converter<Michelson, Micheline> by lazy { Static.michelsonToMichelineConverter }

    public val stringToMichelsonPrimConverter: Converter<String, Michelson.Prim> by lazy { Static.stringToMichelsonPrimConverter }
    public val stringToMichelsonDataPrimConverter: Converter<String, MichelsonData.Prim> by lazy { Static.stringToMichelsonDataPrimConverter }
    public val stringToMichelsonInstructionPrimConverter: Converter<String, MichelsonInstruction.Prim> by lazy { Static.stringToMichelsonInstructionPrimConverter }
    public val stringToMichelsonTypePrimConverter: Converter<String, MichelsonType.Prim> by lazy { Static.stringToMichelsonTypePrimConverter }
    public val stringToMichelsonComparableTypePrimConverter: Converter<String, MichelsonComparableType.Prim> by lazy { Static.stringToMichelsonComparableTypePrimConverter }

    public val tagToMichelsonPrimConverter: Converter<ByteArray, Michelson.Prim> by lazy { Static.tagToMichelsonPrimConverter }
    public val tagToMichelsonDataPrimConverter: Converter<ByteArray, MichelsonData.Prim> by lazy { Static.tagToMichelsonDataPrimConverter }
    public val tagToMichelsonInstructionPrimConverter: Converter<ByteArray, MichelsonInstruction.Prim> by lazy { Static.tagToMichelsonInstructionPrimConverter }
    public val tagToMichelsonTypePrimConverter: Converter<ByteArray, MichelsonType.Prim> by lazy { Static.tagToMichelsonTypePrimConverter }
    public val tagToMichelsonComparableTypePrimConverter: Converter<ByteArray, MichelsonComparableType.Prim> by lazy { Static.tagToMichelsonComparableTypePrimConverter }

    // -- normalizer --

    public val michelineNormalizer: Normalizer<Micheline> by lazy { Static.michelineNormalizer }
    public val michelinePrimitiveApplicationNormalizer: Normalizer<MichelinePrimitiveApplication> by lazy { Static.michelinePrimitiveApplicationNormalizer }
    public val michelineSequenceNormalizer: Normalizer<MichelineSequence> by lazy { Static.michelineSequenceNormalizer }

    // -- packer --

    public val michelineToBytesPacker: BytesPacker<Micheline> by lazy {
        MichelineToBytesPacker(
            michelineBytesCoder,
            michelinePrimitiveApplicationNormalizer,
            stringToMichelsonPrimConverter,
            michelineToCompactStringConverter,
            core.encodedBytesCoder,
            core.addressBytesCoder,
            core.publicKeyBytesCoder,
            core.implicitAddressBytesCoder,
            core.signatureBytesCoder,
            core.timestampBigIntCoder,
            core.stringToAddressConverter,
            core.stringToImplicitAddressConverter,
            core.stringToPublicKeyConverter,
            core.stringToSignatureConverter,
        )
    }

    public val michelineToScriptExprHashPacker: Packer<Micheline, ScriptExprHash> by lazy {
        MichelineToScriptExprHashPacker(
            global.crypto,
            core.encodedBytesCoder,
            michelineToBytesPacker,
        )
    }

    private object Static {

        // -- coder --

        val michelineJsonCoder: Coder<Micheline, JsonElement> = MichelineJsonCoder()

        // -- converter --

        val michelineToMichelsonConverter: Converter<Micheline, Michelson> by lazyWeak { MichelineToMichelsonConverter(stringToMichelsonPrimConverter, michelineToCompactStringConverter) }

        val michelineToStringConverter: Converter<Micheline, String> by lazyWeak { MichelineToStringConverter() }
        val michelineToCompactStringConverter: Converter<Micheline, String> by lazyWeak { MichelineToCompactStringConverter() }

        val michelsonToMichelineConverter: Converter<Michelson, Micheline> by lazyWeak { MichelsonToMichelineConverter() }

        val stringToMichelsonPrimConverter: Converter<String, Michelson.Prim> by lazyWeak { StringToMichelsonPrimConverter() }
        val stringToMichelsonDataPrimConverter: Converter<String, MichelsonData.Prim> by lazyWeak { StringToMichelsonDataPrimConverter() }
        val stringToMichelsonInstructionPrimConverter: Converter<String, MichelsonInstruction.Prim> by lazyWeak { StringToMichelsonInstructionPrimConverter() }
        val stringToMichelsonTypePrimConverter: Converter<String, MichelsonType.Prim> by lazyWeak { StringToMichelsonTypePrimConverter() }
        val stringToMichelsonComparableTypePrimConverter: Converter<String, MichelsonComparableType.Prim> by lazyWeak { StringToMichelsonComparableTypePrimConverter() }

        val tagToMichelsonPrimConverter: Converter<ByteArray, Michelson.Prim> by lazyWeak { TagToMichelsonPrimConverter() }
        val tagToMichelsonDataPrimConverter: Converter<ByteArray, MichelsonData.Prim> by lazyWeak { TagToMichelsonDataPrimConverter() }
        val tagToMichelsonInstructionPrimConverter: Converter<ByteArray, MichelsonInstruction.Prim> by lazyWeak { TagToMichelsonInstructionPrimConverter() }
        val tagToMichelsonTypePrimConverter: Converter<ByteArray, MichelsonType.Prim> by lazyWeak { TagToMichelsonTypePrimConverter() }
        val tagToMichelsonComparableTypePrimConverter: Converter<ByteArray, MichelsonComparableType.Prim> by lazyWeak { TagToMichelsonComparableTypePrimConverter() }

        // -- normalizer --

        val michelineNormalizer: MichelineNormalizer by lazyWeak { MichelineNormalizer() }
        val michelinePrimitiveApplicationNormalizer: Normalizer<MichelinePrimitiveApplication> get() = michelineNormalizer.primitiveApplicationToNormalizedConverter
        val michelineSequenceNormalizer: Normalizer<MichelineSequence> get() = michelineNormalizer.sequenceToNormalizedConverter
    }
}
