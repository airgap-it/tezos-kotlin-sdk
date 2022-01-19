package it.airgap.tezos.michelson.internal.utils

import it.airgap.tezos.michelson.micheline.MichelineNode

internal fun List<MichelineNode>.second(): MichelineNode =
    if (size < 2) throw NoSuchElementException("List does not have enough elements.")
    else this[1]

internal fun List<MichelineNode>.third(): MichelineNode =
    if (size < 3) throw NoSuchElementException("List does not have enough elements.")
    else this[2]