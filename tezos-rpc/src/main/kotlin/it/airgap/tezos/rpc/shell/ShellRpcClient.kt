package it.airgap.tezos.rpc.shell

import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.*
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.http.HttpClient
import it.airgap.tezos.rpc.shell.data.*
import it.airgap.tezos.rpc.type.*
import it.airgap.tezos.rpc.type.p2p.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class ShellRpcClient(
    private val nodeUrl: String,
    private val httpClient: HttpClient,
    private val encodedBytesCoder: EncodedBytesCoder,
    private val json: Json,
) : ShellRpc {

    // ==== RPC (https://tezos.gitlab.io/shell/rpc.html) ====

    // -- /chains --

    override suspend fun setBootstrapped(
        chainId: String,
        bootstrapped: Boolean,
        headers: List<HttpHeader>,
    ): SetBootstrappedResponse =
        httpClient.patch(nodeUrl, "/chains/$chainId", headers, request = SetBootstrappedRequest(bootstrapped))

    override suspend fun getBlocks(
        chainId: String,
        length: UInt?,
        head: BlockHash?,
        minDate: String?,
        headers: List<HttpHeader>,
    ): GetBlocksResponse =
        httpClient.get<GetBlocksTransitionalResponse>(
            nodeUrl,
            "/chains/$chainId",
            headers,
            parameters = buildList {
                length?.let { add("length" to length.toString()) }
                head?.let { add("head" to head.base58) }
                minDate?.let { add("min_date" to minDate) }
            }
        ).toFinal()

    override suspend fun getChainId(chainId: String, headers: List<HttpHeader>): GetChainIdResponse =
        httpClient.get<GetChainIdTransitionalResponse>(nodeUrl, "/chains/$chainId/chain_id", headers).toFinal()

    override suspend fun getInvalidBlocks(chainId: String, headers: List<HttpHeader>): GetInvalidBlocksResponse =
        httpClient.get<GetInvalidBlocksTransitionalResponse>(nodeUrl, "/chains/$chainId/invalid_blocks", headers).toFinal()

    override suspend fun getInvalidBlock(chainId: String, blockHash: BlockHash, headers: List<HttpHeader>): GetInvalidBlockResponse =
        httpClient.get<GetInvalidBlockTransitionalResponse>(nodeUrl, "/chains/$chainId/invalid_blocks/${blockHash.base58}", headers).toFinal()

    override suspend fun deleteInvalidBlock(chainId: String, blockHash: BlockHash, headers: List<HttpHeader>): DeleteInvalidBlockResponse =
        httpClient.delete(nodeUrl, "/chains/${chainId}/invalid_blocks/${blockHash.base58}", headers)

    override suspend fun isBootstrapped(chainId: String, headers: List<HttpHeader>): IsBootstrappedResponse =
        httpClient.get(nodeUrl, "/chains/${chainId}/is_bootstrapped", headers)

    override suspend fun getCaboose(chainId: String, headers: List<HttpHeader>): GetCabooseResponse =
        httpClient.get<GetCabooseTransitionalResponse>(nodeUrl, "/chains/${chainId}/levels/caboose", headers).toFinal()

    override suspend fun getCheckpoint(chainId: String, headers: List<HttpHeader>): GetCheckpointResponse =
        httpClient.get<GetCheckpointTransitionalResponse>(nodeUrl, "/chains/${chainId}/levels/checkpoint", headers).toFinal()

    override suspend fun getSavepoint(chainId: String, headers: List<HttpHeader>): GetSavepointResponse =
        httpClient.get<GetSavepointTransitionalResponse>(nodeUrl, "/chains/${chainId}/levels/savepoint", headers).toFinal()

    // -- /config --

    override suspend fun getHistoryMode(headers: List<HttpHeader>): GetHistoryModeResponse =
        httpClient.get(nodeUrl, "/config/history_mode", headers)

    override suspend fun setLogging(activeSinks: String, headers: List<HttpHeader>): SetLoggingResponse =
        httpClient.put(nodeUrl, "/config/logging", headers, request = SetLoggingRequest(activeSinks))

    override suspend fun getUserActivatedProtocolOverrides(headers: List<HttpHeader>): GetUserActivatedProtocolOverridesResponse =
        httpClient.get<GetUserActivatedProtocolOverridesTransitionalResponse>(nodeUrl, "/config/network/user_activated_protocol_overrides", headers).toFinal()

    override suspend fun getUserActivatedUpgrades(headers: List<HttpHeader>): GetUserActivatedUpgradesResponse =
        httpClient.get<GetUserActivatedUpgradesTransitionalResponse>(nodeUrl, "/config/network/user_activated_upgrades", headers).toFinal()

    // -- /injection --

    override suspend fun injectBlock(
        data: HexString,
        operations: List<List<RpcOperation>>,
        async: Boolean?,
        force: Boolean?,
        chain: ChainId?,
        headers: List<HttpHeader>,
    ): InjectBlockResponse =
        httpClient.post<InjectBlockRequest, InjectBlockTransitionalResponse>(
            nodeUrl,
            "/injection/block",
            headers,
            parameters = buildList {
                async?.takeIf { it }?.let { add("async" to null) }
                force?.takeIf { it }?.let { add("force" to null) }
                chain?.let { add("chain" to chain.base58) }
            },
            request = InjectBlockRequest(data, operations),
        ).toFinal()

    override suspend fun injectOperation(
        data: HexString,
        async: Boolean?,
        chain: ChainId?,
        headers: List<HttpHeader>,
    ): InjectOperationResponse =
        httpClient.post<InjectOperationRequest, InjectOperationTransitionalResponse>(
            nodeUrl,
            "/injection/operation",
            headers,
            parameters = buildList {
                async?.takeIf { it }?.let { add("async" to null) }
                chain?.let { add("chain" to chain.base58) }
            },
            request = InjectOperationRequest(data),
        ).toFinal()

    override suspend fun injectProtocol(
        expectedEnvVersion: UShort,
        components: List<RpcProtocolComponent>,
        async: Boolean?,
        headers: List<HttpHeader>,
    ): InjectProtocolResponse =
        httpClient.post<InjectProtocolRequest, InjectProtocolTransitionalResponse>(
            nodeUrl,
            "/injection/protocol",
            headers,
            parameters = buildList {
                async?.takeIf { it }?.let { add("async" to null) }
            },
            request = InjectProtocolRequest(expectedEnvVersion, components),
        ).toFinal()

    // -- /monitor --

    override suspend fun monitorActiveChains(headers: List<HttpHeader>): MonitorActiveChainsResponse =
        httpClient.get<MonitorActiveChainsTransitionalResponse>(nodeUrl, "/monitor/active_chains", headers).toFinal()

    override suspend fun monitorBootstrapped(headers: List<HttpHeader>): MonitorBootstrappedResponse =
        httpClient.get<MonitorBootstrappedTransitionalResponse>(nodeUrl, "/monitor/bootstrapped", headers).toFinal()

    override suspend fun monitorHeads(
        chainId: ChainId,
        nextProtocol: ProtocolHash?,
        headers: List<HttpHeader>,
    ): MonitorHeadsResponse =
        httpClient.get<MonitorHeadsTransitionalResponse>(
            nodeUrl,
            "/monitor/heads/$chainId",
            headers,
            parameters = buildList {
                nextProtocol?.let { add("next_protocol" to it.base58) }
            },
        ).toFinal()

    override suspend fun monitorProtocols(headers: List<HttpHeader>): MonitorProtocolsResponse =
        httpClient.get<MonitorProtocolsTransitionalResponse>(nodeUrl, "/monitor/protocols", headers).toFinal()

    override suspend fun monitorValidBlocks(
        protocol: ProtocolHash?,
        nextProtocol: ProtocolHash?,
        chain: ChainId?,
        headers: List<HttpHeader>,
    ): MonitorValidBlocksResponse =
        httpClient.get<MonitorValidBlocksTransitionalResponse>(
            nodeUrl,
            "/monitor/valid_blocks",
            headers,
            parameters = buildList {
                protocol?.let { add("protocol" to it.base58) }
                nextProtocol?.let { add("next_protocol" to it.base58) }
                chain?.let { add("chain" to it.base58) }
            },
        ).toFinal()

    // -- /network --
    override suspend fun getConnections(headers: List<HttpHeader>): GetConnectionsResponse =
        httpClient.get<GetConnectionsTransitionalResponse>(
            nodeUrl,
            "/network/connections",
            headers,
        ).toFinal()

    override suspend fun getConnection(
        peerId: CryptoboxPublicKeyHash,
        headers: List<HttpHeader>,
    ): GetConnectionResponse =
        httpClient.get<GetConnectionTransitionalResponse>(
            nodeUrl,
            "/network/connections/${peerId.base58}",
            headers,
        ).toFinal()

    override suspend fun closeConnection(peerId: CryptoboxPublicKeyHash, wait: Boolean?, headers: List<HttpHeader>): CloseConnectionResponse =
        httpClient.delete(
            nodeUrl,
            "/network/connections/${peerId.base58}",
            headers,
            parameters = buildList {
                wait?.takeIf { it }?.let { add("wait" to null) }
            },
        )

    override suspend fun clearGreylist(headers: List<HttpHeader>): ClearGreylistResponse =
        httpClient.delete(nodeUrl, "/network/greylist", headers, )

    override suspend fun getGreylistedIPs(headers: List<HttpHeader>): GetGreylistedIPsResponse =
        httpClient.get<GetGreylistedIPsTransitionalResponse>(
            nodeUrl,
            "/network/greylist/ips",
            headers,
        ).toFinal()


    override suspend fun getLastGreylistedPeers(headers: List<HttpHeader>): GetLastGreylistedPeersResponse =
        httpClient.get<GetLastGreylistedPeersTransitionalResponse>(
            nodeUrl,
            "/network/greylist/peers",
            headers,
        ).toFinal()

    override suspend fun getLogs(headers: List<HttpHeader>): GetLogResponse =
        httpClient.get<GetLogTransitionalResponse>(
            nodeUrl,
            "/network/log",
            headers,
        ).toFinal()

    override suspend fun getPeers(filter: RpcPeerState?, headers: List<HttpHeader>): GetPeersResponse =
        httpClient.get<GetPeersTransitionalResponse>(
            nodeUrl,
            "/network/peers",
            headers,
            parameters = buildList {
                filter?.let { add("filter" to json.encodeToString(it)) }
            },
        ).toFinal()

    override suspend fun getPeer(peerId: CryptoboxPublicKeyHash, headers: List<HttpHeader>): GetPeerResponse =
        httpClient.get<GetPeerTransitionalResponse>(
            nodeUrl,
            "/network/peers/${peerId.base58}",
            headers,
        ).toFinal()

    override suspend fun changePeerPermissions(
        peerId: CryptoboxPublicKeyHash,
        acl: RpcAcl,
        headers: List<HttpHeader>,
    ): ChangePeerPermissionResponse =
        httpClient.patch<ChangePeerPermissionRequest, ChangePeerPermissionTransitionalResponse>(
            nodeUrl,
            "/network/peers/${peerId.base58}",
            headers,
            request = ChangePeerPermissionRequest(acl),
        ).toFinal()

    override suspend fun isPeerBanned(peerId: CryptoboxPublicKeyHash, headers: List<HttpHeader>): BannedPeerResponse =
        httpClient.get(nodeUrl, "/network/peers/${peerId.base58}/banned", headers)

    override suspend fun getPeerEvents(
        peerId: CryptoboxPublicKeyHash,
        monitor: Boolean?,
        headers: List<HttpHeader>,
    ): GetPeerEventsResponse =
        httpClient.get<GetPeerEventsTransitionalResponse>(
            nodeUrl,
            "/network/peers/${peerId.base58}/log",
            headers,
            parameters = buildList {
                monitor?.takeIf { it }?.let { add("monitor" to null) }
            }
        ).toFinal()

    // ==== converters ====

    // -- /chains --

    private fun GetBlocksTransitionalResponse.toFinal(): GetBlocksResponse =
        GetBlocksResponse(
            blocks.map { inner ->
                inner.map { it.toEncodedBlockHash() }
            }
        )

    private fun GetChainIdTransitionalResponse.toFinal(): GetChainIdResponse =
        GetChainIdResponse(chainId.toEncodedChainId())

    private fun GetInvalidBlocksTransitionalResponse.toFinal(): GetInvalidBlocksResponse =
        GetInvalidBlocksResponse(blocks.map { it.toFinal() })

    private fun GetInvalidBlockTransitionalResponse.toFinal(): GetInvalidBlockResponse =
        GetInvalidBlockResponse(block.toFinal())

    private fun TransitionalRpcInvalidBlock.toFinal(): RpcInvalidBlock =
        RpcInvalidBlock(block.toEncodedBlockHash(), level, errors)

    private fun GetCabooseTransitionalResponse.toFinal(): GetCabooseResponse =
        GetCabooseResponse(blockHash.toEncodedBlockHash(), level)

    private fun GetCheckpointTransitionalResponse.toFinal(): GetCheckpointResponse =
        GetCheckpointResponse(blockHash.toEncodedBlockHash(), level)

    private fun GetSavepointTransitionalResponse.toFinal(): GetSavepointResponse =
        GetSavepointResponse(blockHash.toEncodedBlockHash(), level)

    // -- /config --

    private fun GetUserActivatedProtocolOverridesTransitionalResponse.toFinal(): GetUserActivatedProtocolOverridesResponse =
        GetUserActivatedProtocolOverridesResponse(overrides.map { it.toFinal() })

    private fun GetUserActivatedUpgradesTransitionalResponse.toFinal(): GetUserActivatedUpgradesResponse =
        GetUserActivatedUpgradesResponse(upgrades.map { it.toFinal() })

    private fun TransitionalRpcUserActivatedProtocolOverride.toFinal(): RpcUserActivatedProtocolOverride =
        RpcUserActivatedProtocolOverride(replacedProtocol.toEncodedProtocolHash(), replacementProtocol.toEncodedProtocolHash())

    private fun TransitionalRpcUserActivatedUpgrade.toFinal(): RpcUserActivatedUpgrade =
        RpcUserActivatedUpgrade(level, replacementProtocol.toEncodedProtocolHash())

    // -- /injection --

    private fun InjectBlockTransitionalResponse.toFinal(): InjectBlockResponse =
        InjectBlockResponse(hash.toEncodedBlockHash())

    private fun InjectOperationTransitionalResponse.toFinal(): InjectOperationResponse =
        InjectOperationResponse(hash.toEncodedOperationHash())

    private fun InjectProtocolTransitionalResponse.toFinal(): InjectProtocolResponse =
        InjectProtocolResponse(hash.toEncodedProtocolHash())

    // -- /monitor --

    private fun MonitorActiveChainsTransitionalResponse.toFinal(): MonitorActiveChainsResponse =
        MonitorActiveChainsResponse(chains.map { it.toFinal() })

    private fun MonitorBootstrappedTransitionalResponse.toFinal(): MonitorBootstrappedResponse =
        MonitorBootstrappedResponse(block.toEncodedBlockHash(), timestamp.toTimestamp())

    private fun MonitorHeadsTransitionalResponse.toFinal(): MonitorHeadsResponse =
        MonitorHeadsResponse(blockHeader.toFinal())

    private fun MonitorProtocolsTransitionalResponse.toFinal(): MonitorProtocolsResponse =
        MonitorProtocolsResponse(hash.toEncodedProtocolHash())

    private fun MonitorValidBlocksTransitionalResponse.toFinal(): MonitorValidBlocksResponse =
        MonitorValidBlocksResponse(blockHeader.toFinal())

    private fun TransitionalRpcActiveChain.toFinal(): RpcActiveChain =
        when (this) {
            is TransitionalRpcMainChain -> RpcMainChain(chainId.toEncodedChainId())
            is TransitionalRpcTestChain -> RpcTestChain(chainId.toEncodedChainId(), testProtocol.toEncodedProtocolHash(), expirationDate.toTimestamp())
            is TransitionalRpcStoppingChain -> RpcStoppingChain(stopping.toEncodedChainId())
        }

    private fun TransitionalRpcBlockHeader.toFinal(): RpcBlockHeader =
        RpcBlockHeader(
            hash.toEncodedBlockHash(),
            level,
            proto,
            predecessor.toEncodedBlockHash(),
            timestamp.toTimestamp(),
            validationPass,
            operationsHash.toEncodedOperationListListHash(),
            fitness,
            context.toEncodedContextHash(),
            protocolData,
        )

    // -- /network --

    private fun GetConnectionsTransitionalResponse.toFinal(): GetConnectionsResponse =
        GetConnectionsResponse(connections.map { it.toFinal() })

    private fun GetConnectionTransitionalResponse.toFinal(): GetConnectionResponse =
        GetConnectionResponse(connection.toFinal())

    private fun GetGreylistedIPsTransitionalResponse.toFinal(): GetGreylistedIPsResponse =
        GetGreylistedIPsResponse(ips.map { it.toRpcIPAddress() }, notReliableSince.toTimestamp())

    private fun GetLastGreylistedPeersTransitionalResponse.toFinal(): GetLastGreylistedPeersResponse =
        GetLastGreylistedPeersResponse(peers.map { it.toEncodedCryptoboxPublicKeyHash() })

    private fun GetLogTransitionalResponse.toFinal(): GetLogResponse =
        GetLogResponse(events.map { it.toFinal() })

    private fun GetPeersTransitionalResponse.toFinal(): GetPeersResponse =
        GetPeersResponse(peers.map { it.first.toEncodedCryptoboxPublicKeyHash() to it.second.toFinal() })

    private fun GetPeerTransitionalResponse.toFinal(): GetPeerResponse =
        GetPeerResponse(peer.toFinal())

    private fun ChangePeerPermissionTransitionalResponse.toFinal(): ChangePeerPermissionResponse =
        ChangePeerPermissionResponse(peer.toFinal())

    private fun GetPeerEventsTransitionalResponse.toFinal(): GetPeerEventsResponse =
        GetPeerEventsResponse(events.map { it.toFinal() })

    private fun TransitionalRpcConnection.toFinal(): RpcConnection =
        RpcConnection(
            incoming,
            peerId.toEncodedCryptoboxPublicKeyHash(),
            idPoint.toFinal(),
            remoteSocketPort,
            announcedVersion,
            private,
            localMetadata,
            remoteMetadata,
        )

    private fun TransitionalRpcConnectionId.toFinal(): RpcConnectionId =
        RpcConnectionId(address.toRpcIPAddress(), port)

    private fun TransitionalRpcConnectionPoolEvent.toFinal(): RpcConnectionPoolEvent =
        when (this) {
            RpcTooFewConnectionsEvent -> RpcTooFewConnectionsEvent
            RpcTooManyConnectionsEvent -> RpcTooManyConnectionsEvent
            is TransitionalRpcNewPointEvent -> RpcNewPointEvent(point.toConnectionPointId())
            is TransitionalRpcNewPeerEvent -> RpcNewPeerEvent(peerId.toEncodedCryptoboxPublicKeyHash())
            is TransitionalRpcIncomingConnectionEvent -> RpcIncomingConnectionEvent(point.toConnectionPointId())
            is TransitionalRpcOutgoingConnectionEvent -> RpcOutgoingConnectionEvent(point.toConnectionPointId())
            is TransitionalRpcAuthenticationFailedEvent -> RpcAuthenticationFailedEvent(point.toConnectionPointId())
            is TransitionalRpcAcceptingRequestEvent -> RpcAcceptingRequestEvent(point.toConnectionPointId(), idPoint.toFinal(), peerId.toEncodedCryptoboxPublicKeyHash())
            is TransitionalRpcRejectingRequestEvent -> RpcRejectingRequestEvent(point.toConnectionPointId(), idPoint.toFinal(), peerId.toEncodedCryptoboxPublicKeyHash())
            is TransitionalRpcRequestRejectedEvent -> RpcRequestRejectedEvent(point.toConnectionPointId(), identity?.run { first.toFinal() to second.toEncodedCryptoboxPublicKeyHash() })
            is TransitionalRpcConnectionEstablishedEvent -> RpcConnectionEstablishedEvent(idPoint.toFinal(), peerId.toEncodedCryptoboxPublicKeyHash())
            is TransitionalRpcDisconnectionEvent -> RpcDisconnectionEvent(peerId.toEncodedCryptoboxPublicKeyHash())
            is TransitionalRpcExternalDisconnectionEvent -> RpcExternalDisconnectionEvent(peerId.toEncodedCryptoboxPublicKeyHash())
            RpcGcPointsEvent -> RpcGcPointsEvent
            RpcGcPeerIdsEvent -> RpcGcPeerIdsEvent
            is TransitionalRpcSwapRequestReceivedEvent -> RpcSwapRequestReceivedEvent(source.toEncodedCryptoboxPublicKeyHash())
            is TransitionalRpcSwapAckReceivedEvent -> RpcSwapAckReceivedEvent(source.toEncodedCryptoboxPublicKeyHash())
            is TransitionalRpcSwapRequestSentEvent -> RpcSwapRequestSentEvent(source.toEncodedCryptoboxPublicKeyHash())
            is TransitionalRpcSwapAckSentEvent -> RpcSwapAckSentEvent(source.toEncodedCryptoboxPublicKeyHash())
            is TransitionalRpcSwapRequestIgnoredEvent -> RpcSwapRequestIgnoredEvent(source.toEncodedCryptoboxPublicKeyHash())
            is TransitionalRpcSwapSuccessEvent -> RpcSwapSuccessEvent(source.toEncodedCryptoboxPublicKeyHash())
            is TransitionalRpcSwapFailureEvent -> RpcSwapFailureEvent(source.toEncodedCryptoboxPublicKeyHash())
            is TransitionalRpcBootstrapSentEvent -> RpcBootstrapSentEvent(source.toEncodedCryptoboxPublicKeyHash())
            is TransitionalRpcBootstrapReceivedEvent -> RpcBootstrapReceivedEvent(source.toEncodedCryptoboxPublicKeyHash())
            is TransitionalRpcAdvertiseSentEvent -> RpcAdvertiseSentEvent(source.toEncodedCryptoboxPublicKeyHash())
            is TransitionalRpcAdvertiseReceivedEvent -> RpcAdvertiseReceivedEvent(source.toEncodedCryptoboxPublicKeyHash())
        }

    private fun TransitionalRpcPeer.toFinal(): RpcPeer =
        RpcPeer(
            score,
            trusted,
            connectionMetadata,
            peerMetadata,
            state,
            reachableAt?.toFinal(),
            stat,
            lastFailedConnection?.run { first.toFinal() to second.toTimestamp() },
            lastRejectedConnection?.run { first.toFinal() to second.toTimestamp() },
            lastEstablishedConnection?.run { first.toFinal() to second.toTimestamp() },
            lastDisconnection?.run { first.toFinal() to second.toTimestamp() },
            lastMiss?.run { first.toFinal() to second.toTimestamp() },
            lastSeen?.run { first.toFinal() to second.toTimestamp() },
        )

    private fun TransitionalRpcPeerPoolEvent.toFinal(): RpcPeerPoolEvent =
        RpcPeerPoolEvent(kind, timestamp.toTimestamp(), address.toRpcIPAddress(), port)

    // -- common --

    private fun TransitionalRpcBlockHash.toEncodedBlockHash(): BlockHash = toEncoded(BlockHash)
    private fun TransitionalRpcChainId.toEncodedChainId(): ChainId = toEncoded(ChainId)
    private fun TransitionalRpcContextHash.toEncodedContextHash(): ContextHash = toEncoded(ContextHash)
    private fun TransitionalRpcCryptoboxPublicKeyHash.toEncodedCryptoboxPublicKeyHash(): CryptoboxPublicKeyHash = toEncoded(CryptoboxPublicKeyHash)
    private fun TransitionalRpcOperationHash.toEncodedOperationHash(): OperationHash = toEncoded(OperationHash)
    private fun TransitionalRpcOperationListListHash.toEncodedOperationListListHash(): OperationListListHash = toEncoded(OperationListListHash)
    private fun TransitionalRpcProtocolHash.toEncodedProtocolHash(): ProtocolHash = toEncoded(ProtocolHash)

    private fun TransitionalRpcTimestamp.toTimestamp(): Timestamp =
        when (this) {
            is Unistring.PlainUtf8 -> Timestamp.Rfc3339(string)
            is Unistring.InvalidUtf8 -> failWithIllegalArgument("Invalid Timestamp.") // TODO: improve error
        }

    private fun TransitionalRpcIPAddress.toRpcIPAddress(): RpcIPAddress =
        when (this) {
            is Unistring.PlainUtf8 -> RpcIPAddress.fromString(string)
            is Unistring.InvalidUtf8 -> failWithIllegalArgument("Invalid IP address.") // TODO: improve error
        }

    private fun TransitionalRpcConnectionPointId.toConnectionPointId(): RpcConnectionPointId =
        when (this) {
            is Unistring.PlainUtf8 -> RpcConnectionPointId(string)
            is Unistring.InvalidUtf8 -> failWithIllegalArgument("Invalid Point ID.") // TODO: improve error
        }

    private fun <E : Encoded<E>, K : Encoded.Kind<E>> Unistring.toEncoded(kind: K): E =
        when (this) {
            is Unistring.PlainUtf8 -> kind.createValue(string)
            is Unistring.InvalidUtf8 -> failWithIllegalArgument("Invalid value.") // TODO: improve error
        }
}