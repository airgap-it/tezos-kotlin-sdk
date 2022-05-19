package it.airgap.tezos.contract.internal.converter

import it.airgap.tezos.contract.entrypoint.ContractEntrypointParameter
import it.airgap.tezos.michelson.micheline.MichelineNode

// -- ContractEntrypointArgument -> MichelineNode

internal fun ContractEntrypointParameter.toMicheline(type: MichelineNode, entrypointArgumentToMichelineConverter: TypedConverter<ContractEntrypointParameter, MichelineNode>): MichelineNode =
    entrypointArgumentToMichelineConverter.convert(this, type)