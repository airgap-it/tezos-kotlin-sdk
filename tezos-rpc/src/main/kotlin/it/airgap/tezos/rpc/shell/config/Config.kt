package it.airgap.tezos.rpc.shell.config

import it.airgap.tezos.rpc.http.HttpHeader

public interface Config {
    public val historyMode: HistoryMode
    public val logging: Logging
    public val network: Network

    public interface HistoryMode {
        public suspend fun get(headers: List<HttpHeader> = emptyList()): GetHistoryModeResponse
    }

    public interface Logging {
        public suspend fun put(activeSinks: List<String>, headers: List<HttpHeader> = emptyList()): SetLoggingResponse
    }

    public interface Network {
        public val userActivatedProtocolOverrides: UserActivatedProtocolOverrides
        public val userActivatedUpgrades: UserActivatedUpgrades

        public interface UserActivatedProtocolOverrides {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetUserActivatedProtocolOverridesResponse
        }

        public interface UserActivatedUpgrades {
            public suspend fun get(headers: List<HttpHeader> = emptyList()): GetUserActivatedUpgradesResponse
        }
    }
}