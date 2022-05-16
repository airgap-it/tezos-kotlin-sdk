package it.airgap.tezos.michelson.internal.di

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.delegate.lazyWeak
import it.airgap.tezos.core.internal.di.CoreDependencyRegistry
import it.airgap.tezos.core.internal.normalizer.Normalizer
import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.internal.coder.MichelineBytesCoder
import it.airgap.tezos.michelson.internal.coder.MichelineJsonCoder
import it.airgap.tezos.michelson.internal.converter.*
import it.airgap.tezos.michelson.internal.normalizer.MichelineToNormalizedConverter
import it.airgap.tezos.michelson.internal.packer.MichelinePacker
import it.airgap.tezos.michelson.internal.packer.Packer
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import kotlinx.serialization.json.JsonElement

@InternalTezosSdkApi
public class MichelsonDependencyRegistry(core: CoreDependencyRegistry) {

    // -- coder --

    public val michelineBytesCoder: ConsumingBytesCoder<MichelineNode> by lazy {
        MichelineBytesCoder(
            stringToMichelsonPrimConverter,
            tagToMichelsonPrimConverter,
            michelineToCompactStringConverter,
            core.zarithIntegerBytesCoder,
        )
    }
    public val michelineJsonCoder: Coder<MichelineNode, JsonElement> by lazy { Static.michelineJsonCoder }

    // -- converter --

    public val michelineToMichelsonConverter: Converter<MichelineNode, Michelson> by lazy { Static.michelineToMichelsonConverter }

    public val michelineToStringConverter: Converter<MichelineNode, String> by lazy { Static.michelineToStringConverter }
    public val michelineToCompactStringConverter: Converter<MichelineNode, String> by lazy { Static.michelineToCompactStringConverter }

    public val michelsonToMichelineConverter: Converter<Michelson, MichelineNode> by lazy { Static.michelsonToMichelineConverter }

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

    public val michelineNormalizer: Normalizer<MichelineNode> by lazy { Static.michelineNormalizer }
    public val michelinePrimitiveApplicationNormalizer: Normalizer<MichelinePrimitiveApplication> by lazy { Static.michelinePrimitiveApplicationNormalizer }
    public val michelineSequenceNormalizer: Normalizer<MichelineSequence> by lazy { Static.michelineSequenceNormalizer }

    // -- packer --

    public val michelinePacker: Packer<MichelineNode> by lazy {
        MichelinePacker(
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

    private object Static {

        // -- coder --

        val michelineJsonCoder: Coder<MichelineNode, JsonElement> = MichelineJsonCoder()

        // -- converter --

        val michelineToMichelsonConverter: Converter<MichelineNode, Michelson> by lazy { MichelineToMichelsonConverter(stringToMichelsonPrimConverter, michelineToCompactStringConverter) }

        val michelineToStringConverter: Converter<MichelineNode, String> by lazyWeak { MichelineToStringConverter() }
        val michelineToCompactStringConverter: Converter<MichelineNode, String> by lazyWeak { MichelineToCompactStringConverter() }

        val michelsonToMichelineConverter: Converter<Michelson, MichelineNode> by lazyWeak { MichelsonToMichelineConverter() }

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

        val michelineNormalizer: MichelineToNormalizedConverter by lazyWeak { MichelineToNormalizedConverter() }
        val michelinePrimitiveApplicationNormalizer: Normalizer<MichelinePrimitiveApplication> get() = michelineNormalizer.primitiveApplicationToNormalizedConverter
        val michelineSequenceNormalizer: Normalizer<MichelineSequence> get() = michelineNormalizer.sequenceToNormalizedConverter
    }
}
