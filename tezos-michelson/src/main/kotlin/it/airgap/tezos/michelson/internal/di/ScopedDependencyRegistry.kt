package it.airgap.tezos.michelson.internal.di

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.di.DependencyRegistry
import it.airgap.tezos.core.internal.di.findScoped
import it.airgap.tezos.michelson.internal.coder.MichelineBytesCoder
import it.airgap.tezos.michelson.internal.coder.MichelineJsonCoder
import it.airgap.tezos.michelson.internal.converter.*
import it.airgap.tezos.michelson.internal.packer.MichelinePacker

public interface ScopedDependencyRegistry : DependencyRegistry {

    // -- coder --

    public val michelineBytesCoder: MichelineBytesCoder
    public val michelineJsonCoder: MichelineJsonCoder

    // -- converter --

    public val michelineToMichelsonConverter: MichelineToMichelsonConverter

    public val michelineToNormalizedConverter: MichelineToNormalizedConverter
    public val michelinePrimitiveApplicationToNormalizedConverter: MichelinePrimitiveApplicationToNormalizedConverter
    public val michelineSequenceToNormalizedConverter: MichelineSequenceToNormalizedConverter

    public val michelineToStringConverter: MichelineToStringConverter
    public val michelineToCompactStringConverter: MichelineToCompactStringConverter

    public val michelsonToMichelineConverter: MichelsonToMichelineConverter

    public val stringToMichelsonPrimConverter: StringToMichelsonPrimConverter
    public val stringToMichelsonDataPrimConverter: StringToMichelsonDataPrimConverter
    public val stringToMichelsonInstructionPrimConverter: StringToMichelsonInstructionPrimConverter
    public val stringToMichelsonTypePrimConverter: StringToMichelsonTypePrimConverter
    public val stringToMichelsonComparableTypePrimConverter: StringToMichelsonComparableTypePrimConverter

    public val tagToMichelsonPrimConverter: TagToMichelsonPrimConverter
    public val tagToMichelsonDataPrimConverter: TagToMichelsonDataPrimConverter
    public val tagToMichelsonInstructionPrimConverter: TagToMichelsonInstructionPrimConverter
    public val tagToMichelsonTypePrimConverter: TagToMichelsonTypePrimConverter
    public val tagToMichelsonComparableTypePrimConverter: TagToMichelsonComparableTypePrimConverter

    // -- packer --

    public val michelinePacker: MichelinePacker
}

@InternalTezosSdkApi
public fun DependencyRegistry.michelson(): ScopedDependencyRegistry =
    if (this is ScopedDependencyRegistry) this
    else findScoped() ?: MichelsonScopedDependencyRegistry(this).also { addScoped(it) }