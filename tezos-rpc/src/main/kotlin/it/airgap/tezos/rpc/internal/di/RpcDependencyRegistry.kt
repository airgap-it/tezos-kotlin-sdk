package it.airgap.tezos.rpc.internal.di

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.di.CoreDependencyRegistry
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.internal.di.OperationDependencyRegistry
import it.airgap.tezos.rpc.TezosRpc
import it.airgap.tezos.rpc.TezosRpcClient
import it.airgap.tezos.rpc.active.ActiveSimplifiedRpc
import it.airgap.tezos.rpc.active.ActiveSimplifiedRpcClient
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.internal.context.TezosRpcContext.getOrPutWeak
import it.airgap.tezos.rpc.internal.estimator.FeeEstimator
import it.airgap.tezos.rpc.internal.estimator.OperationFeeEstimator
import it.airgap.tezos.rpc.internal.http.HttpClient
import it.airgap.tezos.rpc.internal.serializer.rpcJson
import it.airgap.tezos.rpc.shell.ShellSimplifiedRpc
import it.airgap.tezos.rpc.shell.ShellSimplifiedRpcClient
import it.airgap.tezos.rpc.shell.chains.Chains
import it.airgap.tezos.rpc.shell.chains.ChainsClient
import it.airgap.tezos.rpc.shell.config.Config
import it.airgap.tezos.rpc.shell.config.ConfigClient
import it.airgap.tezos.rpc.shell.injection.Injection
import it.airgap.tezos.rpc.shell.injection.InjectionClient
import it.airgap.tezos.rpc.shell.monitor.Monitor
import it.airgap.tezos.rpc.shell.monitor.MonitorClient
import it.airgap.tezos.rpc.shell.network.Network
import it.airgap.tezos.rpc.shell.network.NetworkClient
import kotlinx.serialization.json.Json
import java.lang.ref.WeakReference

@InternalTezosSdkApi
public class RpcDependencyRegistry internal constructor(
    httpClientProvider: HttpClientProvider,
    private val core: CoreDependencyRegistry,
    private val operation: OperationDependencyRegistry,
) {

    // -- serialization --

    public val json: Json by lazy {
        val rpcJson = rpcJson(
            core.stringToAddressConverter,
            core.stringToImplicitAddressConverter,
            core.stringToPublicKeyConverter,
            core.stringToPublicKeyHashConverter,
            core.stringToBlindedPublicKeyHashConverter,
            core.stringToSignatureConverter,
        )

        Json(from = rpcJson) {
            prettyPrint = false
        }
    }

    // -- network --

    public val httpClient: HttpClient by lazy { HttpClient(httpClientProvider, json) }

    // -- rpc --

    private val tezosRpcs: MutableMap<String, WeakReference<TezosRpc>> = mutableMapOf()
    public fun tezosRpc(nodeUrl: String): TezosRpc = tezosRpcs.getOrPutWeak(nodeUrl) {
        TezosRpcClient(
            shellRpc(nodeUrl),
            activeRpc(nodeUrl),
            operationFeeEstimator(nodeUrl),
            chains(nodeUrl),
            config(nodeUrl),
            injection(nodeUrl),
            monitor(nodeUrl),
            network(nodeUrl),
        )
    }

    private val shellRpcs: MutableMap<String, WeakReference<ShellSimplifiedRpc>> = mutableMapOf()
    internal fun shellRpc(nodeUrl: String): ShellSimplifiedRpc = shellRpcs.getOrPutWeak(nodeUrl) { ShellSimplifiedRpcClient(chains(nodeUrl), injection(nodeUrl)) }

    private val activeRpcs: MutableMap<String, WeakReference<ActiveSimplifiedRpc>> = mutableMapOf()
    internal fun activeRpc(nodeUrl: String): ActiveSimplifiedRpc = activeRpcs.getOrPutWeak(nodeUrl) { ActiveSimplifiedRpcClient(chains(nodeUrl)) }

    private val chains: MutableMap<String, WeakReference<Chains>> = mutableMapOf()
    internal fun chains(nodeUrl: String): Chains = chains.getOrPutWeak(nodeUrl) { ChainsClient(nodeUrl, httpClient) }

    private val configs: MutableMap<String, WeakReference<Config>> = mutableMapOf()
    internal fun config(nodeUrl: String): Config = configs.getOrPutWeak(nodeUrl) { ConfigClient(nodeUrl, httpClient) }

    private val injections: MutableMap<String, WeakReference<Injection>> = mutableMapOf()
    internal fun injection(nodeUrl: String): Injection = injections.getOrPutWeak(nodeUrl) { InjectionClient(nodeUrl, httpClient) }

    private val monitors: MutableMap<String, WeakReference<Monitor>> = mutableMapOf()
    internal fun monitor(nodeUrl: String): Monitor = monitors.getOrPutWeak(nodeUrl) { MonitorClient(nodeUrl, httpClient) }

    private val networks: MutableMap<String, WeakReference<Network>> = mutableMapOf()
    internal fun network(nodeUrl: String): Network = networks.getOrPutWeak(nodeUrl) { NetworkClient(nodeUrl, httpClient, json) }

    // -- estimator --

    public fun operationFeeEstimator(nodeUrl: String): FeeEstimator<Operation> =
        OperationFeeEstimator(chains(nodeUrl), operation.operationContentBytesCoder)
}
