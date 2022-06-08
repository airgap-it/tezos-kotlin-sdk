package it.airgap.tezos.rpc.shell.config

import it.airgap.tezos.rpc.http.HttpHeader

/**
 * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/config`
 */
public interface Config {
    public val historyMode: HistoryMode
    public val logging: Logging
    public val network: Network

    /**
     * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/config/history_mode`
     */
    public interface HistoryMode {
        public suspend fun get(headers: List<HttpHeader> = emptyList()): GetHistoryModeResponse
    }

    /**
     * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/config/logging`
     */
    public interface Logging {
        public suspend fun put(activeSinks: List<String>, headers: List<HttpHeader> = emptyList()): SetLoggingResponse
    }

    /**
     * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/config/network`
     */
    public interface Network {
        public val userActivatedProtocolOverrides: UserActivatedProtocolOverrides
        public val userActivatedUpgrades: UserActivatedUpgrades

        /**
         * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/config/network/user_activated_protocol_overrides`
         */
        public interface UserActivatedProtocolOverrides {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetUserActivatedProtocolOverridesResponse
        }

        /**
         * [Shell RPCs](https://tezos.gitlab.io/shell/rpc.html): `/config/network/user_activated_upgrades`
         */
        public interface UserActivatedUpgrades {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetUserActivatedUpgradesResponse
        }
    }
}