package it.airgap.tezos.contract.internal.converter

import it.airgap.tezos.contract.entrypoint.ContractEntrypointParameter
import it.airgap.tezos.michelson.micheline.Micheline

// -- ContractEntrypointArgument -> MichelineNode

internal fun ContractEntrypointParameter.toMicheline(type: Micheline, entrypointArgumentToMichelineConverter: TypedConverter<ContractEntrypointParameter, Micheline>): Micheline =
    entrypointArgumentToMichelineConverter.convert(this, type)