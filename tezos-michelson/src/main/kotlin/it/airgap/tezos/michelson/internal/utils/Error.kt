package it.airgap.tezos.michelson.internal.utils

import it.airgap.tezos.core.internal.utils.failWithIllegalState
import it.airgap.tezos.michelson.Michelson
import kotlin.reflect.KClass

@PublishedApi
internal fun failWithUnexpectedMichelsonType(type: KClass<out Michelson>, caller: String): Nothing =
    failWithIllegalState("Unexpected Michelson type (${type}) for `$caller`.")

@PublishedApi
internal fun failWithUnexpectedMichelsonGrammarType(type: KClass<out Michelson.GrammarType>, caller: String): Nothing =
    failWithIllegalState("Unexpected Michelson Grammar type (${type}) for `$caller`.")