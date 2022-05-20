package it.airgap.tezos.contract.internal.utils

import it.airgap.tezos.contract.exception.ContractException

internal fun failWithContractException(message: String? = null, cause: Throwable? = null): Nothing =
    throw ContractException(message, cause)