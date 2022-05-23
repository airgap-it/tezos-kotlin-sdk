package it.airgap.tezos.rpc.converter

import it.airgap.tezos.operation.contract.Entrypoint
import it.airgap.tezos.operation.contract.Parameters
import it.airgap.tezos.operation.contract.Script
import it.airgap.tezos.rpc.type.contract.RpcParameters
import it.airgap.tezos.rpc.type.contract.RpcScript

// -- Script -> RpcScript --

public fun Script.asRpc(): RpcScript = RpcScript(code, storage)

// -- RpcScript -> Script --

public fun RpcScript.asScript(): Script = Script(code, storage)

// -- Parameters -> RpcParameters --

public fun Parameters.asRpc(): RpcParameters = RpcParameters(entrypoint.value, value)

// -- RpcParameters -> Parameters --

public fun RpcParameters.asParameters(): Parameters = Parameters(Entrypoint(entrypoint), value)