package it.airgap.tezos.contract.internal.utils

import it.airgap.tezos.contract.internal.context.TezosContractContext.allIsInstance

internal inline fun <reified T: Any> Collection<*>.allIsInstance(): Boolean = allIsInstance(T::class)
