package it.airgap.tezos.rpc.shell.monitor

import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.utils.Constants

/**
 * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/monitor`
 */
public interface Monitor {
    public val activeChains: ActiveChains
    public val bootstrapped: Bootstrapped
    public val heads: Heads
    public val protocols: Protocols
    public val validBlocks: ValidBlocks

    /**
     * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/monitor/active_chains`
     */
    public interface ActiveChains {
        public suspend fun get(
            headers: List<HttpHeader> = emptyList(),
            requestTimeout: Long? = null,
            connectionTimeout: Long? = null,
        ): MonitorActiveChainsResponse
    }

    /**
     * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/monitor/bootstrapped`
     */
    public interface Bootstrapped {
        public suspend fun get(
            headers: List<HttpHeader> = emptyList(),
            requestTimeout: Long? = null,
            connectionTimeout: Long? = null,
        ): MonitorBootstrappedResponse
    }

    /**
     * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/monitor/heads`
     */
    public interface Heads {
        public val main: Head
            get() = invoke(Constants.Chain.MAIN)

        public val test: Head
            get() = invoke(Constants.Chain.TEST)

        public operator fun invoke(chainId: String): Head
        public operator fun invoke(chainId: ChainId): Head = invoke(chainId.base58)

        public interface Head {
            public suspend fun get(
                nextProtocol: ProtocolHash? = null,
                headers: List<HttpHeader> = emptyList(),
                requestTimeout: Long? = null,
                connectionTimeout: Long? = null,
            ): MonitorHeadResponse
        }
    }

    /**
     * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/monitor/protocols`
     */
    public interface Protocols {
        public suspend fun get(
            headers: List<HttpHeader> = emptyList(),
            requestTimeout: Long? = null,
            connectionTimeout: Long? = null,
        ): MonitorProtocolsResponse
    }

    /**
     * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/monitor/valid_blocks`
     */
    public interface ValidBlocks {
        public suspend fun get(
            protocol: ProtocolHash? = null,
            nextProtocol: ProtocolHash? = null,
            chain: String? = null,
            headers: List<HttpHeader> = emptyList(),
            requestTimeout: Long? = null,
            connectionTimeout: Long? = null,
        ): MonitorValidBlocksResponse
        public suspend fun get(
            protocol: ProtocolHash? = null,
            nextProtocol: ProtocolHash? = null,
            chain: ChainId,
            headers: List<HttpHeader> = emptyList(),
            requestTimeout: Long? = null,
            connectionTimeout: Long? = null,
        ): MonitorValidBlocksResponse =
            get(protocol, nextProtocol, chain.base58, headers)
    }
}