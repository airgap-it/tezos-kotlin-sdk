package it.airgap.tezos.michelson

import it.airgap.tezos.core.TezosSdk
import it.airgap.tezos.michelson.internal.converter.*
import it.airgap.tezos.michelson.internal.di.michelson
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

// -- Michelson -> Micheline --

public fun <T : Michelson> T.toMicheline(
    michelsonToMichelineConverter: MichelsonToMichelineConverter = TezosSdk.instance.dependencyRegistry.michelson().michelsonToMichelineConverter,
): MichelineNode = michelsonToMichelineConverter.convert(this)

// -- Michelson.Prim <- String --

public fun Michelson.Prim.Companion.fromStringOrNull(
    value: String,
    stringToMichelsonPrimConverter: StringToMichelsonPrimConverter = TezosSdk.instance.dependencyRegistry.michelson().stringToMichelsonPrimConverter,
): Michelson.Prim? = runCatching { stringToMichelsonPrimConverter.convert(value) }.getOrNull()

public fun MichelsonData.Prim.Companion.fromStringOrNull(
    value: String,
    stringToMichelsonDataPrimConverter: StringToMichelsonDataPrimConverter = TezosSdk.instance.dependencyRegistry.michelson().stringToMichelsonDataPrimConverter,
): MichelsonData.Prim? = runCatching { stringToMichelsonDataPrimConverter.convert(value) }.getOrNull()

public fun MichelsonInstruction.Prim.Companion.fromStringOrNull(
    value: String,
    stringToMichelsonInstructionPrimConverter: StringToMichelsonInstructionPrimConverter = TezosSdk.instance.dependencyRegistry.michelson().stringToMichelsonInstructionPrimConverter
): MichelsonInstruction.Prim? = runCatching { stringToMichelsonInstructionPrimConverter.convert(value) }.getOrNull()

public fun MichelsonType.Prim.Companion.fromStringOrNull(
    value: String,
    stringToMichelsonTypePrimConverter: StringToMichelsonTypePrimConverter = TezosSdk.instance.dependencyRegistry.michelson().stringToMichelsonTypePrimConverter,
): MichelsonType.Prim? = runCatching { stringToMichelsonTypePrimConverter.convert(value) }.getOrNull()

public fun MichelsonComparableType.Prim.Companion.fromStringOrNull(
    value: String,
    stringToMichelsonComparableTypePrimConverter: StringToMichelsonComparableTypePrimConverter = TezosSdk.instance.dependencyRegistry.michelson().stringToMichelsonComparableTypePrimConverter,
): MichelsonComparableType.Prim? = runCatching { stringToMichelsonComparableTypePrimConverter.convert(value) }.getOrNull()

// -- Michelson.Prim <- ByteArray --

public fun Michelson.Prim.Companion.fromTagOrNull(
    value: ByteArray,
    tagToMichelsonPrimConverter: TagToMichelsonPrimConverter = TezosSdk.instance.dependencyRegistry.michelson().tagToMichelsonPrimConverter,
): Michelson.Prim? = runCatching { tagToMichelsonPrimConverter.convert(value) }.getOrNull()

public fun MichelsonData.Prim.Companion.fromTagOrNull(
    value: ByteArray,
    tagToMichelsonDataPrimConverter: TagToMichelsonDataPrimConverter = TezosSdk.instance.dependencyRegistry.michelson().tagToMichelsonDataPrimConverter,
): MichelsonData.Prim? = runCatching { tagToMichelsonDataPrimConverter.convert(value) }.getOrNull()

public fun MichelsonInstruction.Prim.Companion.fromTagOrNull(
    value: ByteArray,
    tagToMichelsonInstructionPrimConverter: TagToMichelsonInstructionPrimConverter = TezosSdk.instance.dependencyRegistry.michelson().tagToMichelsonInstructionPrimConverter,
): MichelsonInstruction.Prim? = runCatching { tagToMichelsonInstructionPrimConverter.convert(value) }.getOrNull()

public fun MichelsonType.Prim.Companion.fromTagOrNull(
    value: ByteArray,
    tagToMichelsonTypePrimConverter: TagToMichelsonTypePrimConverter = TezosSdk.instance.dependencyRegistry.michelson().tagToMichelsonTypePrimConverter,
): MichelsonType.Prim? = runCatching { tagToMichelsonTypePrimConverter.convert(value) }.getOrNull()

public fun MichelsonComparableType.Prim.Companion.fromTagOrNull(
    value: ByteArray,
    tagToMichelsonComparableTypePrimConverter: TagToMichelsonComparableTypePrimConverter = TezosSdk.instance.dependencyRegistry.michelson().tagToMichelsonComparableTypePrimConverter,
): MichelsonComparableType.Prim? = runCatching { tagToMichelsonComparableTypePrimConverter.convert(value) }.getOrNull()

// -- Micheline -> Michelson --

public fun <T : MichelineNode> T.toMichelson(
    michelineToMichelsonConverter: MichelineToMichelsonConverter = TezosSdk.instance.dependencyRegistry.michelson().michelineToMichelsonConverter,
): Michelson = michelineToMichelsonConverter.convert(this)

// -- Micheline -> String

public fun <T : MichelineNode> T.toExpression(
    michelineToStringConverter: MichelineToStringConverter = TezosSdk.instance.dependencyRegistry.michelson().michelineToStringConverter,
): String = michelineToStringConverter.convert(this)

public fun <T : MichelineNode> T.toCompactExpression(
    michelineToCompactStringConverter: MichelineToCompactStringConverter = TezosSdk.instance.dependencyRegistry.michelson().michelineToCompactStringConverter,
): String = michelineToCompactStringConverter.convert(this)

// -- Micheline -> normalized --

public fun <T : MichelineNode> T.normalized(
    michelineToNormalizedConverter: MichelineToNormalizedConverter = TezosSdk.instance.dependencyRegistry.michelson().michelineToNormalizedConverter,
): MichelineNode = michelineToNormalizedConverter.convert(this)

public fun MichelinePrimitiveApplication.normalized(
    michelinePrimitiveApplicationToNormalizedConverter: MichelinePrimitiveApplicationToNormalizedConverter = TezosSdk.instance.dependencyRegistry.michelson().michelinePrimitiveApplicationToNormalizedConverter,
): MichelinePrimitiveApplication = michelinePrimitiveApplicationToNormalizedConverter.convert(this)

public fun MichelineSequence.normalized(
    michelineSequenceToNormalizedConverter: MichelineSequenceToNormalizedConverter = TezosSdk.instance.dependencyRegistry.michelson().michelineSequenceToNormalizedConverter,
): MichelineSequence = michelineSequenceToNormalizedConverter.convert(this)