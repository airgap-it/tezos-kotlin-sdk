package it.airgap.tezos.contract.internal.converter

import it.airgap.tezos.contract.entrypoint.ContractEntrypointArgument
import it.airgap.tezos.michelson.micheline.MichelineNode

// -- ContractEntrypointArgument -> MichelineNode

internal fun ContractEntrypointArgument.toMicheline(type: MichelineNode, entrypointArgumentToMichelineConverter: TypedConverter<ContractEntrypointArgument, MichelineNode>): MichelineNode =
    entrypointArgumentToMichelineConverter.convert(this, type)