package it.airgap.tezos.rpc

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.rpc.active.ActiveSimplifiedRpc
import it.airgap.tezos.rpc.internal.estimator.FeeEstimator
import it.airgap.tezos.rpc.internal.rpcModule
import it.airgap.tezos.rpc.shell.ShellSimplifiedRpc
import it.airgap.tezos.rpc.shell.chains.Chains
import it.airgap.tezos.rpc.shell.config.Config
import it.airgap.tezos.rpc.shell.injection.Injection
import it.airgap.tezos.rpc.shell.monitor.Monitor
import it.airgap.tezos.rpc.shell.network.Network

/**
 * Tezos RPCs, its main purpose is to interact with a Tezos node.
 * It combines the [shell](https://tezos.gitlab.io/shell/rpc.html) and [active](https://tezos.gitlab.io/active/rpc.html) interfaces.
 *
 * Examples:
 * ```kotlin
 * // get a contract's balance
 *
 * val tezosRpc = TezosRpc("...")
 * val address = Address("tz1...")
 *
 * val balance = runBlocking {
 *     tezosRpc.getBalance(contractId = address)
 * }
 * ```
 *
 * @property chains The `/chains` endpoint service.
 * @property config The `/config` endpoint service.
 * @property injection The `/injection` endpoint service.
 * @property monitor The `/monitor` endpoint service.
 * @property network The `/network` endpoint service.
 *
 * @see ShellSimplifiedRpc
 * @see ActiveSimplifiedRpc
 */
public interface TezosRpc : ShellSimplifiedRpc, ActiveSimplifiedRpc, FeeEstimator<Operation> {
    public val chains: Chains
    public val config: Config
    public val injection: Injection
    public val monitor: Monitor
    public val network: Network
}

/**
 * Creates a [Tezos RPC client][TezosRpc] that will connect with the node specified with the [nodeUrl].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun TezosRpc(nodeUrl: String, tezos: Tezos = Tezos.Default): TezosRpc =
    tezos.rpcModule.dependencyRegistry.tezosRpc(nodeUrl.trimEnd('/'))