package it.airgap.tezos.michelson.converter

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.MichelineNode

// -- Michelson -> Micheline --

public fun <T : Michelson> T.toMicheline(tezos: Tezos = Tezos.Default): MichelineNode =
    toMicheline(tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter)

@InternalTezosSdkApi
public fun <T : Michelson> T.toMicheline(michelsonToMichelineConverter: Converter<Michelson, MichelineNode>): MichelineNode = michelsonToMichelineConverter.convert(this)

// -- Michelson.Prim <- String --

public fun Michelson.Prim.Companion.fromStringOrNull(value: String, tezos: Tezos = Tezos.Default): Michelson.Prim? =
    fromStringOrNull(value, tezos.michelsonModule.dependencyRegistry.stringToMichelsonPrimConverter)

public fun MichelsonData.Prim.Companion.fromStringOrNull(value: String, tezos: Tezos = Tezos.Default): MichelsonData.Prim? =
    fromStringOrNull(value, tezos.michelsonModule.dependencyRegistry.stringToMichelsonDataPrimConverter)

public fun MichelsonInstruction.Prim.Companion.fromStringOrNull(value: String, tezos: Tezos = Tezos.Default): MichelsonInstruction.Prim? =
    fromStringOrNull(value, tezos.michelsonModule.dependencyRegistry.stringToMichelsonInstructionPrimConverter)

public fun MichelsonType.Prim.Companion.fromStringOrNull(value: String, tezos: Tezos = Tezos.Default): MichelsonType.Prim? =
    fromStringOrNull(value, tezos.michelsonModule.dependencyRegistry.stringToMichelsonTypePrimConverter)

public fun MichelsonComparableType.Prim.Companion.fromStringOrNull(value: String, tezos: Tezos = Tezos.Default): MichelsonComparableType.Prim? =
    fromStringOrNull(value, tezos.michelsonModule.dependencyRegistry.stringToMichelsonComparableTypePrimConverter)

@InternalTezosSdkApi
public fun Michelson.Prim.Companion.fromStringOrNull(value: String, stringToMichelsonPrimConverter: Converter<String, Michelson.Prim>): Michelson.Prim? =
    runCatching { stringToMichelsonPrimConverter.convert(value) }.getOrNull()

@InternalTezosSdkApi
public fun MichelsonData.Prim.Companion.fromStringOrNull(value: String, stringToMichelsonDataPrimConverter: Converter<String, MichelsonData.Prim>): MichelsonData.Prim? =
    runCatching { stringToMichelsonDataPrimConverter.convert(value) }.getOrNull()

@InternalTezosSdkApi
public fun MichelsonInstruction.Prim.Companion.fromStringOrNull(value: String, stringToMichelsonInstructionPrimConverter: Converter<String, MichelsonInstruction.Prim>): MichelsonInstruction.Prim? =
    runCatching { stringToMichelsonInstructionPrimConverter.convert(value) }.getOrNull()

@InternalTezosSdkApi
public fun MichelsonType.Prim.Companion.fromStringOrNull(value: String, stringToMichelsonTypePrimConverter: Converter<String, MichelsonType.Prim>): MichelsonType.Prim? =
    runCatching { stringToMichelsonTypePrimConverter.convert(value) }.getOrNull()

@InternalTezosSdkApi
public fun MichelsonComparableType.Prim.Companion.fromStringOrNull(value: String, stringToMichelsonComparableTypePrimConverter: Converter<String, MichelsonComparableType.Prim>): MichelsonComparableType.Prim? =
    runCatching { stringToMichelsonComparableTypePrimConverter.convert(value) }.getOrNull()

// -- Michelson.Prim <- ByteArray --

public fun Michelson.Prim.Companion.fromTagOrNull(value: ByteArray, tezos: Tezos = Tezos.Default): Michelson.Prim? =
    fromTagOrNull(value, tezos.michelsonModule.dependencyRegistry.tagToMichelsonPrimConverter)

public fun MichelsonData.Prim.Companion.fromTagOrNull(value: ByteArray, tezos: Tezos = Tezos.Default): MichelsonData.Prim? =
    fromTagOrNull(value, tezos.michelsonModule.dependencyRegistry.tagToMichelsonDataPrimConverter)

public fun MichelsonInstruction.Prim.Companion.fromTagOrNull(value: ByteArray, tezos: Tezos = Tezos.Default): MichelsonInstruction.Prim? =
    fromTagOrNull(value, tezos.michelsonModule.dependencyRegistry.tagToMichelsonInstructionPrimConverter)

public fun MichelsonType.Prim.Companion.fromTagOrNull(value: ByteArray, tezos: Tezos = Tezos.Default): MichelsonType.Prim? =
    fromTagOrNull(value, tezos.michelsonModule.dependencyRegistry.tagToMichelsonTypePrimConverter)

public fun MichelsonComparableType.Prim.Companion.fromTagOrNull(value: ByteArray, tezos: Tezos = Tezos.Default): MichelsonComparableType.Prim? =
    fromTagOrNull(value, tezos.michelsonModule.dependencyRegistry.tagToMichelsonComparableTypePrimConverter)

@InternalTezosSdkApi
public fun Michelson.Prim.Companion.fromTagOrNull(value: ByteArray, tagToMichelsonPrimConverter: Converter<ByteArray, Michelson.Prim>): Michelson.Prim? =
    runCatching { tagToMichelsonPrimConverter.convert(value) }.getOrNull()

@InternalTezosSdkApi
public fun MichelsonData.Prim.Companion.fromTagOrNull(value: ByteArray, tagToMichelsonDataPrimConverter: Converter<ByteArray, MichelsonData.Prim>): MichelsonData.Prim? =
    runCatching { tagToMichelsonDataPrimConverter.convert(value) }.getOrNull()

@InternalTezosSdkApi
public fun MichelsonInstruction.Prim.Companion.fromTagOrNull(value: ByteArray, tagToMichelsonInstructionPrimConverter: Converter<ByteArray, MichelsonInstruction.Prim>): MichelsonInstruction.Prim? =
    runCatching { tagToMichelsonInstructionPrimConverter.convert(value) }.getOrNull()

@InternalTezosSdkApi
public fun MichelsonType.Prim.Companion.fromTagOrNull(value: ByteArray, tagToMichelsonTypePrimConverter: Converter<ByteArray, MichelsonType.Prim>): MichelsonType.Prim? =
    runCatching { tagToMichelsonTypePrimConverter.convert(value) }.getOrNull()

@InternalTezosSdkApi
public fun MichelsonComparableType.Prim.Companion.fromTagOrNull(value: ByteArray, tagToMichelsonComparableTypePrimConverter: Converter<ByteArray, MichelsonComparableType.Prim>): MichelsonComparableType.Prim? =
    runCatching { tagToMichelsonComparableTypePrimConverter.convert(value) }.getOrNull()