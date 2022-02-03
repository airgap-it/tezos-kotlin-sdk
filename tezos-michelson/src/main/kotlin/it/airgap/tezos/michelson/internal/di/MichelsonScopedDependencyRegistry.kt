package it.airgap.tezos.michelson.internal.di

import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.di.core
import it.airgap.tezos.michelson.internal.coder.MichelineBytesCoder
import it.airgap.tezos.michelson.internal.coder.MichelineJsonCoder
import it.airgap.tezos.michelson.internal.converter.*
import it.airgap.tezos.michelson.internal.packer.MichelinePacker

internal class MichelsonScopedDependencyRegistry(dependencyRegistry: DependencyRegistry) : ScopedDependencyRegistry, DependencyRegistry by dependencyRegistry {

    // -- coder --

    override val michelineBytesCoder: MichelineBytesCoder by lazy {
        MichelineBytesCoder(
            stringToMichelsonPrimConverter,
            tagToMichelsonPrimConverter,
            michelineToCompactStringConverter,
            core().zarithIntegerBytesCoder,
        )
    }
    override val michelineJsonCoder: MichelineJsonCoder = MichelineJsonCoder()

    // -- converter --

    override val michelineToMichelsonConverter: MichelineToMichelsonConverter by lazy { MichelineToMichelsonConverter(stringToMichelsonPrimConverter, michelineToCompactStringConverter) }

    override val michelineToNormalizedConverter: MichelineToNormalizedConverter = MichelineToNormalizedConverter()
    override val michelinePrimitiveApplicationToNormalizedConverter: MichelinePrimitiveApplicationToNormalizedConverter get() = michelineToNormalizedConverter.primitiveApplicationToNormalizedConverter
    override val michelineSequenceToNormalizedConverter: MichelineSequenceToNormalizedConverter get() = michelineToNormalizedConverter.sequenceToNormalizedConverter

    override val michelineToStringConverter: MichelineToStringConverter = MichelineToStringConverter()
    override val michelineToCompactStringConverter: MichelineToCompactStringConverter = MichelineToCompactStringConverter()

    override val michelsonToMichelineConverter: MichelsonToMichelineConverter = MichelsonToMichelineConverter()

    override val stringToMichelsonPrimConverter: StringToMichelsonPrimConverter = StringToMichelsonPrimConverter()
    override val stringToMichelsonDataPrimConverter: StringToMichelsonDataPrimConverter = StringToMichelsonDataPrimConverter()
    override val stringToMichelsonInstructionPrimConverter: StringToMichelsonInstructionPrimConverter = StringToMichelsonInstructionPrimConverter()
    override val stringToMichelsonTypePrimConverter: StringToMichelsonTypePrimConverter = StringToMichelsonTypePrimConverter()
    override val stringToMichelsonComparableTypePrimConverter: StringToMichelsonComparableTypePrimConverter = StringToMichelsonComparableTypePrimConverter()

    override val tagToMichelsonPrimConverter: TagToMichelsonPrimConverter = TagToMichelsonPrimConverter()
    override val tagToMichelsonDataPrimConverter: TagToMichelsonDataPrimConverter = TagToMichelsonDataPrimConverter()
    override val tagToMichelsonInstructionPrimConverter: TagToMichelsonInstructionPrimConverter = TagToMichelsonInstructionPrimConverter()
    override val tagToMichelsonTypePrimConverter: TagToMichelsonTypePrimConverter = TagToMichelsonTypePrimConverter()
    override val tagToMichelsonComparableTypePrimConverter: TagToMichelsonComparableTypePrimConverter = TagToMichelsonComparableTypePrimConverter()

    // -- packer --

    override val michelinePacker: MichelinePacker by lazy {
        MichelinePacker(
            michelineBytesCoder,
            stringToMichelsonPrimConverter,
            michelinePrimitiveApplicationToNormalizedConverter,
            michelineToCompactStringConverter,
            core().encodedBytesCoder,
            core().addressBytesCoder,
            core().publicKeyBytesCoder,
            core().implicitAddressBytesCoder,
            core().signatureBytesCoder,
            timestampBigIntCoder,
            core().stringToAddressConverter,
            core().stringToImplicitAddressConverter,
            core().stringToPublicKeyEncodedConverter,
            core().stringToSignatureEncodedConverter,
        )
    }
}