package it.airgap.tezos.rpc.internal.utils

import it.airgap.tezos.core.toMutez
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.core.type.tez.Nanotez
import it.airgap.tezos.operation.type.OperationLimits

private const val FEE_BASE_MUTEZ = 100U

private const val FEE_PER_GAS_UNIT_NANOTEZ = 100U
private const val FEE_PER_STORAGE_BYTE_NANOTEZ = 1000U

private const val FEE_SAFETY_MARGIN_MUTEZ = 100U

internal fun fee(operationSize: Int, limits: OperationLimits): Mutez {
    val gasFee = Nanotez(limits.gas * FEE_PER_GAS_UNIT_NANOTEZ.toInt()).toMutez()
    val storageFee = Nanotez(operationSize.toUInt() * FEE_PER_STORAGE_BYTE_NANOTEZ).toMutez()

    return Mutez(FEE_BASE_MUTEZ) + gasFee + storageFee + Mutez(FEE_SAFETY_MARGIN_MUTEZ)
}
