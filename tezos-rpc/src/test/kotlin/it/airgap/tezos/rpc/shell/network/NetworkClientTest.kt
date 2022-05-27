package it.airgap.tezos.rpc.shell.network

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.CryptoboxPublicKeyHash
import it.airgap.tezos.rpc.http.HttpClientProvider
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.http.HttpParameter
import it.airgap.tezos.rpc.internal.http.HttpClient
import it.airgap.tezos.rpc.internal.rpcModule
import it.airgap.tezos.rpc.type.network.*
import it.airgap.tezos.rpc.type.primitive.RpcUnistring
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import mockTezos
import normalizeWith
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class NetworkClientTest {

    @MockK
    private lateinit var httpClientProvider : HttpClientProvider

    private lateinit var json: Json
    private lateinit var httpClient: HttpClient

    private lateinit var networkClient: NetworkClient

    private val nodeUrl = "https://example.com"

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        val tezos = mockTezos(httpClientProvider = httpClientProvider)

        json = tezos.rpcModule.dependencyRegistry.json
        httpClient = tezos.rpcModule.dependencyRegistry.httpClient

        networkClient = NetworkClient(nodeUrl, httpClient, json)
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should call GET on 'network - connections'`() {
        val (_, expectedResponse, _, jsonResponse) = connectionsGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { networkClient.connections.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/network/connections", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/network/connections", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'network - connections - $peer_id'`() {
        val peerId = CryptoboxPublicKeyHash("idrY8gjTHV4HUVtEmZtypyC64nG1C4")

        val (_, expectedResponse, _, jsonResponse) = connectionsConnectionGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { networkClient.connections(peerId).get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/network/connections/${peerId.base58}", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/network/connections/${peerId.base58}", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call DELETE on 'network - connections - $peer_id'`() {
        data class Parameters(val wait: Boolean? = null)

        val peerId = CryptoboxPublicKeyHash("idrY8gjTHV4HUVtEmZtypyC64nG1C4")
        val parameters = listOf(
            Parameters(),
            Parameters(wait = true),
            Parameters(wait = false),
        )

        val (_, expectedResponse, _, jsonResponse) = connectionsConnectionDeleteRequestConfiguration
        coEvery { httpClientProvider.delete(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            parameters.forEach {  parameters ->
                val (wait) = parameters
                val expectedParameters = buildList<HttpParameter> {
                    wait?.takeIf { it }?.let { add("wait" to null) }
                }
                val response = runBlocking { networkClient.connections(peerId).delete(wait, headers = headers) }

                assertEquals(expectedResponse, response)

                coVerify { httpClient.delete("$nodeUrl/network/connections/${peerId.base58}", "/", headers = headers, parameters = expectedParameters) }
                coVerify { httpClientProvider.delete("$nodeUrl/network/connections/${peerId.base58}", "/", headers = headers, parameters = expectedParameters) }
            }
        }
    }

    @Test
    fun `should call DELETE on 'network - greylist'`() {
        val (_, expectedResponse, _, jsonResponse) = greylistDeleteRequestConfiguration
        coEvery { httpClientProvider.delete(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { networkClient.greylist.delete(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.delete("$nodeUrl/network/greylist", "/", headers = headers) }
            coVerify { httpClientProvider.delete("$nodeUrl/network/greylist", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'network - greylist - ips'`() {
        val (_, expectedResponse, _, jsonResponse) = greylistIpsGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { networkClient.greylist.ips.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/network/greylist/ips", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/network/greylist/ips", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'network - greylist - peers'`() {
        val (_, expectedResponse, _, jsonResponse) = greylistPeersGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { networkClient.greylist.peers.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/network/greylist/peers", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/network/greylist/peers", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'network - log'`() {
        val (_, expectedResponse, _, jsonResponse) = logGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { networkClient.log.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/network/log", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/network/log", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'network - peers'`() {
        data class Parameters(val filter: RpcPeerState? = null)

        val parameters = listOf(
            Parameters(),
            Parameters(filter = RpcPeerState.Running),
            Parameters(filter = RpcPeerState.Accepted),
            Parameters(filter = RpcPeerState.Disconnected),
        )

        val (_, expectedResponse, _, jsonResponse) = peersGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { networkClient.peers.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/network/peers", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/network/peers", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'network - peers - $peer_id'`() {
        val peerId = CryptoboxPublicKeyHash("idrY8gjTHV4HUVtEmZtypyC64nG1C4")

        val (_, expectedResponse, _, jsonResponse) = peersPeerGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { networkClient.peers(peerId).get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/network/peers/${peerId.base58}", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/network/peers/${peerId.base58}", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call PATCH on 'network - peers - $peer_id'`() {
        val peerId = CryptoboxPublicKeyHash("idrY8gjTHV4HUVtEmZtypyC64nG1C4")
        val acl = RpcAcl.Open

        val (expectedRequest, expectedResponse, jsonRequest, jsonResponse) = peersPeerPatchRequestConfiguration(acl)
        coEvery { httpClientProvider.patch(any(), any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { networkClient.peers(peerId).patch(acl, headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.patch("$nodeUrl/network/peers/${peerId.base58}", "/", headers = headers, request = expectedRequest) }
            coVerify { httpClientProvider.patch("$nodeUrl/network/peers/${peerId.base58}", "/", headers = headers, parameters = emptyList(), body = jsonRequest?.normalizeWith(json)) }
        }
    }

    @Test
    fun `should call GET on 'network - peers - $peer_id - banned'`() {
        val peerId = CryptoboxPublicKeyHash("idrY8gjTHV4HUVtEmZtypyC64nG1C4")

        val (_, expectedResponse, _, jsonResponse) = peersPeerBannedGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { networkClient.peers(peerId).banned.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/network/peers/${peerId.base58}/banned", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/network/peers/${peerId.base58}/banned", "/", headers = headers, parameters = emptyList()) }
        }
    }

    @Test
    fun `should call GET on 'network - peers - $peer_id - log'`() {
        val peerId = CryptoboxPublicKeyHash("idrY8gjTHV4HUVtEmZtypyC64nG1C4")

        val (_, expectedResponse, _, jsonResponse) = peersPeerLogGetRequestConfiguration
        coEvery { httpClientProvider.get(any(), any(), any(), any()) } returns jsonResponse

        headers.forEach { headers ->
            val response = runBlocking { networkClient.peers(peerId).log.get(headers = headers) }

            assertEquals(expectedResponse, response)

            coVerify { httpClient.get("$nodeUrl/network/peers/${peerId.base58}/log", "/", headers = headers) }
            coVerify { httpClientProvider.get("$nodeUrl/network/peers/${peerId.base58}/log", "/", headers = headers, parameters = emptyList()) }
        }
    }

    private val headers: List<List<HttpHeader>>
        get() = listOf(
            emptyList(),
            listOf("Authorization" to "Bearer")
        )

    private val connectionsGetRequestConfiguration: RequestConfiguration<Unit, GetConnectionsResponse>
        get() =
            RequestConfiguration(
                response = GetConnectionsResponse(
                    listOf(
                        RpcConnection(
                            incoming = true,
                            peerId = CryptoboxPublicKeyHash("idrY8gjTHV4HUVtEmZtypyC64nG1C4"),
                            idPoint = RpcConnectionId(
                                address = RpcIPv4Address("127.0.0.1"),
                                port = 65535U,
                            ),
                            remoteSocketPort = 65535U,
                            announcedVersion = RpcNetworkVersion(
                                chainName = RpcUnistring.PlainUtf8("chain"),
                                distributedDbVersion = 65535U,
                                p2pVersion = 65535U,
                            ),
                            private = true,
                            localMetadata = RpcConnectionMetadata(
                                disableMempool = true,
                                privateNode = true,
                            ),
                            remoteMetadata = RpcConnectionMetadata(
                                disableMempool = true,
                                privateNode = true,
                            ),
                        )
                    )
                ),
                jsonResponse = """
                    [
                        {
                            "incoming": true,
                            "peer_id": "idrY8gjTHV4HUVtEmZtypyC64nG1C4",
                            "id_point": {
                              "addr": "127.0.0.1",
                              "port": 65535
                            },
                            "remote_socket_port": 65535,
                            "announced_version": {
                              "chain_name": "chain",
                              "distributed_db_version": 65535,
                              "p2p_version": 65535
                            },
                            "private": true,
                            "local_metadata": {
                              "disable_mempool": true,
                              "private_node": true
                            },
                            "remote_metadata": {
                              "disable_mempool": true,
                              "private_node": true
                            }
                        }
                    ]
                """.trimIndent(),
            )

    private val connectionsConnectionGetRequestConfiguration: RequestConfiguration<Unit, GetConnectionResponse>
        get() = RequestConfiguration(
            response = GetConnectionResponse(
                RpcConnection(
                    incoming = true,
                    peerId = CryptoboxPublicKeyHash("idrY8gjTHV4HUVtEmZtypyC64nG1C4"),
                    idPoint = RpcConnectionId(
                        address = RpcIPv4Address("127.0.0.1"),
                        port = 65535U,
                    ),
                    remoteSocketPort = 65535U,
                    announcedVersion = RpcNetworkVersion(
                        chainName = RpcUnistring.PlainUtf8("chain"),
                        distributedDbVersion = 65535U,
                        p2pVersion = 65535U,
                    ),
                    private = true,
                    localMetadata = RpcConnectionMetadata(
                        disableMempool = true,
                        privateNode = true,
                    ),
                    remoteMetadata = RpcConnectionMetadata(
                        disableMempool = true,
                        privateNode = true,
                    ),
                )
            ),
            jsonResponse = """
                {
                    "incoming": true,
                    "peer_id": "idrY8gjTHV4HUVtEmZtypyC64nG1C4",
                    "id_point": {
                        "addr": "127.0.0.1",
                        "port": 65535
                    },
                    "remote_socket_port": 65535,
                    "announced_version": {
                        "chain_name": "chain",
                        "distributed_db_version": 65535,
                        "p2p_version": 65535
                    },
                    "private": true,
                    "local_metadata": {
                        "disable_mempool": true,
                        "private_node": true
                    },
                    "remote_metadata": {
                        "disable_mempool": true,
                        "private_node": true
                    }
                }
            """.trimIndent(),
        )

    private val connectionsConnectionDeleteRequestConfiguration: RequestConfiguration<Unit, CloseConnectionResponse>
        get() = RequestConfiguration(
            response = CloseConnectionResponse,
            jsonResponse = "",
        )

    private val greylistDeleteRequestConfiguration: RequestConfiguration<Unit, ClearGreylistResponse>
        get() = RequestConfiguration(
            response = ClearGreylistResponse,
            jsonResponse = "",
        )

    private val greylistIpsGetRequestConfiguration: RequestConfiguration<Unit, GetGreylistedIpsResponse>
        get() = RequestConfiguration(
            response = GetGreylistedIpsResponse(
                ips = listOf(
                    RpcIPv4Address("127.0.0.1"),
                ),
                notReliableSince = Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
            ),
            jsonResponse = """
                {
                    "ips": [
                        "127.0.0.1"
                    ],
                    "not_reliable_since": "2022-04-12T13:07:00Z"
                }
            """.trimIndent()
        )

    private val greylistPeersGetRequestConfiguration: RequestConfiguration<Unit, GetGreylistedPeersResponse>
        get() = RequestConfiguration(
            response = GetGreylistedPeersResponse(
                listOf(
                    CryptoboxPublicKeyHash("idrY8gjTHV4HUVtEmZtypyC64nG1C4"),
                )
            ),
            jsonResponse = """
                [ "idrY8gjTHV4HUVtEmZtypyC64nG1C4" ]
            """.trimIndent()
        )

    private val logGetRequestConfiguration: RequestConfiguration<Unit, GetLogResponse>
        get() = RequestConfiguration(
            response = GetLogResponse(RpcTooFewConnectionsEvent),
            jsonResponse = """
                {
                    "event": "too_few_connections"
                }
            """.trimIndent()
        )

    private val peersGetRequestConfiguration: RequestConfiguration<Unit, GetPeersResponse>
        get() = RequestConfiguration(
            jsonResponse = """
                [
                    [
                        "idrY8gjTHV4HUVtEmZtypyC64nG1C4",
                        {
                            "score": 0,
                            "trusted": true,
                            "conn_metadata": {
                                "disable_mempool": true,
                                "private_node": true
                            },
                            "peer_metadata": {
                                "responses": {
                                    "sent": {
                                        "branch": "1073741823",
                                        "head": "1073741823",
                                        "block_header": "1073741823",
                                        "operations": "1073741823",
                                        "protocols": "1073741823",
                                        "operation_hashes_for_block": "1073741823",
                                        "operations_for_block": "1073741823",
                                        "checkpoint": "1073741823",
                                        "protocol_branch": "1073741823",
                                        "predecessor_header": "1073741823",
                                        "other": "1073741823"
                                    },
                                    "failed": {
                                        "branch": "1073741823",
                                        "head": "1073741823",
                                        "block_header": "1073741823",
                                        "operations": "1073741823",
                                        "protocols": "1073741823",
                                        "operation_hashes_for_block": "1073741823",
                                        "operations_for_block": "1073741823",
                                        "checkpoint": "1073741823",
                                        "protocol_branch": "1073741823",
                                        "predecessor_header": "1073741823",
                                        "other": "1073741823"
                                    },
                                    "received": {
                                        "branch": "1073741823",
                                        "head": "1073741823",
                                        "block_header": "1073741823",
                                        "operations": "1073741823",
                                        "protocols": "1073741823",
                                        "operation_hashes_for_block": "1073741823",
                                        "operations_for_block": "1073741823",
                                        "checkpoint": "1073741823",
                                        "protocol_branch": "1073741823",
                                        "predecessor_header": "1073741823",
                                        "other": "1073741823"
                                    },
                                    "unexpected": "1073741823",
                                    "outdated": "1073741823"
                                },
                                "requests": {
                                    "sent": {
                                        "branch": "1073741823",
                                        "head": "1073741823",
                                        "block_header": "1073741823",
                                        "operations": "1073741823",
                                        "protocols": "1073741823",
                                        "operation_hashes_for_block": "1073741823",
                                        "operations_for_block": "1073741823",
                                        "checkpoint": "1073741823",
                                        "protocol_branch": "1073741823",
                                        "predecessor_header": "1073741823",
                                        "other": "1073741823"
                                    },
                                    "received": {
                                        "branch": "1073741823",
                                        "head": "1073741823",
                                        "block_header": "1073741823",
                                        "operations": "1073741823",
                                        "protocols": "1073741823",
                                        "operation_hashes_for_block": "1073741823",
                                        "operations_for_block": "1073741823",
                                        "checkpoint": "1073741823",
                                        "protocol_branch": "1073741823",
                                        "predecessor_header": "1073741823",
                                        "other": "1073741823"
                                    },
                                    "failed": {
                                        "branch": "1073741823",
                                        "head": "1073741823",
                                        "block_header": "1073741823",
                                        "operations": "1073741823",
                                        "protocols": "1073741823",
                                        "operation_hashes_for_block": "1073741823",
                                        "operations_for_block": "1073741823",
                                        "checkpoint": "1073741823",
                                        "protocol_branch": "1073741823",
                                        "predecessor_header": "1073741823",
                                        "other": "1073741823"
                                    },
                                    "scheduled": {
                                        "branch": "1073741823",
                                        "head": "1073741823",
                                        "block_header": "1073741823",
                                        "operations": "1073741823",
                                        "protocols": "1073741823",
                                        "operation_hashes_for_block": "1073741823",
                                        "operations_for_block": "1073741823",
                                        "checkpoint": "1073741823",
                                        "protocol_branch": "1073741823",
                                        "predecessor_header": "1073741823",
                                        "other": "1073741823"
                                    }
                                },
                                "valid_blocks": "1073741823",
                                "old_heads": "1073741823",
                                "prevalidator_results": {
                                    "cannot_download": "1073741823",
                                    "cannot_parse": "1073741823",
                                    "refused_by_prefilter": "1073741823",
                                    "refused_by_postfilter": "1073741823",
                                    "applied": "1073741823",
                                    "branch_delayed": "1073741823",
                                    "branch_refused": "1073741823",
                                    "refused": "1073741823",
                                    "duplicate": "1073741823",
                                    "outdated": "1073741823"
                                },
                                "unactivated_chains": "1073741823",
                                "inactive_chains": "1073741823",
                                "future_blocks_advertised": "1073741823",
                                "unadvertised": {
                                    "block": "1073741823",
                                    "operations": "1073741823",
                                    "protocol": "1073741823"
                                },
                                "advertisements": {
                                    "sent": {
                                        "head": "1073741823",
                                        "branch": "1073741823"
                                    },
                                    "received": {
                                        "head": "1073741823",
                                        "branch": "1073741823"
                                    }
                                }
                            },
                            "state": "running",
                            "reachable_at": {
                                "addr": "127.0.0.1",
                                "port": 65535
                            },
                            "stat": {
                                "total_sent": "9223372036854775807",
                                "total_recv": "9223372036854775807",
                                "current_inflow": 1073741823,
                                "current_outflow": 1073741823
                            },
                            "last_failed_connection": [
                                {
                                    "addr": "127.0.0.1",
                                    "port": 65535
                                },
                                "2022-04-12T13:07:00Z"
                            ],
                            "last_rejected_connection": [
                                {
                                    "addr": "127.0.0.1",
                                    "port": 65535
                                },
                                "2022-04-12T13:07:00Z"
                            ],
                            "last_established_connection": [
                                {
                                    "addr": "127.0.0.1",
                                    "port": 65535
                                },
                                "2022-04-12T13:07:00Z"
                            ],
                            "last_disconnection": [
                                {
                                    "addr": "127.0.0.1",
                                    "port": 65535
                                },
                                "2022-04-12T13:07:00Z"
                            ],
                            "last_seen": [
                                {
                                    "addr": "127.0.0.1",
                                    "port": 65535
                                },
                                "2022-04-12T13:07:00Z"
                            ],
                            "last_miss": [
                                {
                                    "addr": "127.0.0.1",
                                    "port": 65535
                                },
                                "2022-04-12T13:07:00Z"
                            ]
                        }
                    ]
                ]
            """.trimIndent(),
            response = GetPeersResponse(
                listOf(
                    CryptoboxPublicKeyHash("idrY8gjTHV4HUVtEmZtypyC64nG1C4") to RpcPeer(
                        score = 0,
                        trusted = true,
                        connectionMetadata = RpcConnectionMetadata(
                            disableMempool = true,
                            privateNode = true,
                        ),
                        peerMetadata = RpcPeerMetadata(
                            responses = RpcPeerMetadataResponses(
                                sent = RpcPeerMetadataEntry.V1(
                                    branch = "1073741823",
                                    head = "1073741823",
                                    blockHeader = "1073741823",
                                    operations = "1073741823",
                                    protocols = "1073741823",
                                    operationHashesForBlock = "1073741823",
                                    operationsForBlock = "1073741823",
                                    checkpoint = "1073741823",
                                    protocolBranch = "1073741823",
                                    predecessorHeader = "1073741823",
                                    other = "1073741823",
                                ),
                                failed = RpcPeerMetadataEntry.V1(
                                    branch = "1073741823",
                                    head = "1073741823",
                                    blockHeader = "1073741823",
                                    operations = "1073741823",
                                    protocols = "1073741823",
                                    operationHashesForBlock = "1073741823",
                                    operationsForBlock = "1073741823",
                                    checkpoint = "1073741823",
                                    protocolBranch = "1073741823",
                                    predecessorHeader = "1073741823",
                                    other = "1073741823",
                                ),
                                received = RpcPeerMetadataEntry.V1(
                                    branch = "1073741823",
                                    head = "1073741823",
                                    blockHeader = "1073741823",
                                    operations = "1073741823",
                                    protocols = "1073741823",
                                    operationHashesForBlock = "1073741823",
                                    operationsForBlock = "1073741823",
                                    checkpoint = "1073741823",
                                    protocolBranch = "1073741823",
                                    predecessorHeader = "1073741823",
                                    other = "1073741823",
                                ),
                                unexpected = "1073741823",
                                outdated = "1073741823",
                            ),
                            requests = RpcPeerMetadataRequests(
                                sent = RpcPeerMetadataEntry.V1(
                                    branch = "1073741823",
                                    head = "1073741823",
                                    blockHeader = "1073741823",
                                    operations = "1073741823",
                                    protocols = "1073741823",
                                    operationHashesForBlock = "1073741823",
                                    operationsForBlock = "1073741823",
                                    checkpoint = "1073741823",
                                    protocolBranch = "1073741823",
                                    predecessorHeader = "1073741823",
                                    other = "1073741823",
                                ),
                                received = RpcPeerMetadataEntry.V1(
                                    branch = "1073741823",
                                    head = "1073741823",
                                    blockHeader = "1073741823",
                                    operations = "1073741823",
                                    protocols = "1073741823",
                                    operationHashesForBlock = "1073741823",
                                    operationsForBlock = "1073741823",
                                    checkpoint = "1073741823",
                                    protocolBranch = "1073741823",
                                    predecessorHeader = "1073741823",
                                    other = "1073741823",
                                ),
                                failed = RpcPeerMetadataEntry.V1(
                                    branch = "1073741823",
                                    head = "1073741823",
                                    blockHeader = "1073741823",
                                    operations = "1073741823",
                                    protocols = "1073741823",
                                    operationHashesForBlock = "1073741823",
                                    operationsForBlock = "1073741823",
                                    checkpoint = "1073741823",
                                    protocolBranch = "1073741823",
                                    predecessorHeader = "1073741823",
                                    other = "1073741823",
                                ),
                                scheduled = RpcPeerMetadataEntry.V1(
                                    branch = "1073741823",
                                    head = "1073741823",
                                    blockHeader = "1073741823",
                                    operations = "1073741823",
                                    protocols = "1073741823",
                                    operationHashesForBlock = "1073741823",
                                    operationsForBlock = "1073741823",
                                    checkpoint = "1073741823",
                                    protocolBranch = "1073741823",
                                    predecessorHeader = "1073741823",
                                    other = "1073741823",
                                ),
                            ),
                            validBlocks = "1073741823",
                            oldHeads = "1073741823",
                            prevalidatorResults = RpcPrevalidatorResults(
                                cannotDownload = "1073741823",
                                cannotParse = "1073741823",
                                refusedByPrefilter = "1073741823",
                                refusedByPostfilter = "1073741823",
                                applied = "1073741823",
                                branchDelayed = "1073741823",
                                branchRefused = "1073741823",
                                refused = "1073741823",
                                duplicate = "1073741823",
                                outdated = "1073741823",
                            ),
                            unactivatedChains = "1073741823",
                            inactiveChains = "1073741823",
                            futureBlocksAdvertised = "1073741823",
                            unadvertised = RpcUnadvertised(
                                block = "1073741823",
                                operations = "1073741823",
                                protocol = "1073741823",
                            ),
                            advertisements = RpcAdvertisements(
                                sent = RpcAdvertisement(
                                    head = "1073741823",
                                    branch = "1073741823",
                                ),
                                received = RpcAdvertisement(
                                    head = "1073741823",
                                    branch = "1073741823",
                                ),
                            ),
                        ),
                        state = RpcPeerState.Running,
                        reachableAt = RpcConnectionId(
                            address = RpcIPv4Address("127.0.0.1"),
                            port = 65535U,
                        ),
                        stat = RpcNetworkStat(
                            totalSent = 9223372036854775807,
                            totalReceived = 9223372036854775807,
                            currentInflow = 1073741823,
                            currentOutflow = 1073741823,
                        ),
                        lastFailedConnection = Pair(
                            RpcConnectionId(
                                address = RpcIPv4Address("127.0.0.1"),
                                port = 65535U,
                            ),
                            Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                        ),
                        lastRejectedConnection = Pair(
                            RpcConnectionId(
                                address = RpcIPv4Address("127.0.0.1"),
                                port = 65535U,
                            ),
                            Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                        ),
                        lastEstablishedConnection = Pair(
                            RpcConnectionId(
                                address = RpcIPv4Address("127.0.0.1"),
                                port = 65535U,
                            ),
                            Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                        ),
                        lastDisconnection = Pair(
                            RpcConnectionId(
                                address = RpcIPv4Address("127.0.0.1"),
                                port = 65535U,
                            ),
                            Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                        ),
                        lastSeen = Pair(
                            RpcConnectionId(
                                address = RpcIPv4Address("127.0.0.1"),
                                port = 65535U,
                            ),
                            Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                        ),
                        lastMiss = Pair(
                            RpcConnectionId(
                                address = RpcIPv4Address("127.0.0.1"),
                                port = 65535U,
                            ),
                            Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                        ),
                    )
                )
            )
        )

    private val peersPeerGetRequestConfiguration: RequestConfiguration<Unit, GetPeerResponse>
        get() = RequestConfiguration(
            response = GetPeerResponse(
                RpcPeer(
                    score = 0,
                    trusted = true,
                    connectionMetadata = RpcConnectionMetadata(
                        disableMempool = true,
                        privateNode = true,
                    ),
                    peerMetadata = RpcPeerMetadata(
                        responses = RpcPeerMetadataResponses(
                            sent = RpcPeerMetadataEntry.V1(
                                branch = "1073741823",
                                head = "1073741823",
                                blockHeader = "1073741823",
                                operations = "1073741823",
                                protocols = "1073741823",
                                operationHashesForBlock = "1073741823",
                                operationsForBlock = "1073741823",
                                checkpoint = "1073741823",
                                protocolBranch = "1073741823",
                                predecessorHeader = "1073741823",
                                other = "1073741823",
                            ),
                            failed = RpcPeerMetadataEntry.V1(
                                branch = "1073741823",
                                head = "1073741823",
                                blockHeader = "1073741823",
                                operations = "1073741823",
                                protocols = "1073741823",
                                operationHashesForBlock = "1073741823",
                                operationsForBlock = "1073741823",
                                checkpoint = "1073741823",
                                protocolBranch = "1073741823",
                                predecessorHeader = "1073741823",
                                other = "1073741823",
                            ),
                            received = RpcPeerMetadataEntry.V1(
                                branch = "1073741823",
                                head = "1073741823",
                                blockHeader = "1073741823",
                                operations = "1073741823",
                                protocols = "1073741823",
                                operationHashesForBlock = "1073741823",
                                operationsForBlock = "1073741823",
                                checkpoint = "1073741823",
                                protocolBranch = "1073741823",
                                predecessorHeader = "1073741823",
                                other = "1073741823",
                            ),
                            unexpected = "1073741823",
                            outdated = "1073741823",
                        ),
                        requests = RpcPeerMetadataRequests(
                            sent = RpcPeerMetadataEntry.V1(
                                branch = "1073741823",
                                head = "1073741823",
                                blockHeader = "1073741823",
                                operations = "1073741823",
                                protocols = "1073741823",
                                operationHashesForBlock = "1073741823",
                                operationsForBlock = "1073741823",
                                checkpoint = "1073741823",
                                protocolBranch = "1073741823",
                                predecessorHeader = "1073741823",
                                other = "1073741823",
                            ),
                            received = RpcPeerMetadataEntry.V1(
                                branch = "1073741823",
                                head = "1073741823",
                                blockHeader = "1073741823",
                                operations = "1073741823",
                                protocols = "1073741823",
                                operationHashesForBlock = "1073741823",
                                operationsForBlock = "1073741823",
                                checkpoint = "1073741823",
                                protocolBranch = "1073741823",
                                predecessorHeader = "1073741823",
                                other = "1073741823",
                            ),
                            failed = RpcPeerMetadataEntry.V1(
                                branch = "1073741823",
                                head = "1073741823",
                                blockHeader = "1073741823",
                                operations = "1073741823",
                                protocols = "1073741823",
                                operationHashesForBlock = "1073741823",
                                operationsForBlock = "1073741823",
                                checkpoint = "1073741823",
                                protocolBranch = "1073741823",
                                predecessorHeader = "1073741823",
                                other = "1073741823",
                            ),
                            scheduled = RpcPeerMetadataEntry.V1(
                                branch = "1073741823",
                                head = "1073741823",
                                blockHeader = "1073741823",
                                operations = "1073741823",
                                protocols = "1073741823",
                                operationHashesForBlock = "1073741823",
                                operationsForBlock = "1073741823",
                                checkpoint = "1073741823",
                                protocolBranch = "1073741823",
                                predecessorHeader = "1073741823",
                                other = "1073741823",
                            ),
                        ),
                        validBlocks = "1073741823",
                        oldHeads = "1073741823",
                        prevalidatorResults = RpcPrevalidatorResults(
                            cannotDownload = "1073741823",
                            cannotParse = "1073741823",
                            refusedByPrefilter = "1073741823",
                            refusedByPostfilter = "1073741823",
                            applied = "1073741823",
                            branchDelayed = "1073741823",
                            branchRefused = "1073741823",
                            refused = "1073741823",
                            duplicate = "1073741823",
                            outdated = "1073741823",
                        ),
                        unactivatedChains = "1073741823",
                        inactiveChains = "1073741823",
                        futureBlocksAdvertised = "1073741823",
                        unadvertised = RpcUnadvertised(
                            block = "1073741823",
                            operations = "1073741823",
                            protocol = "1073741823",
                        ),
                        advertisements = RpcAdvertisements(
                            sent = RpcAdvertisement(
                                head = "1073741823",
                                branch = "1073741823",
                            ),
                            received = RpcAdvertisement(
                                head = "1073741823",
                                branch = "1073741823",
                            ),
                        ),
                    ),
                    state = RpcPeerState.Running,
                    reachableAt = RpcConnectionId(
                        address = RpcIPv4Address("127.0.0.1"),
                        port = 65535U,
                    ),
                    stat = RpcNetworkStat(
                        totalSent = 9223372036854775807,
                        totalReceived = 9223372036854775807,
                        currentInflow = 1073741823,
                        currentOutflow = 1073741823,
                    ),
                    lastFailedConnection = Pair(
                        RpcConnectionId(
                            address = RpcIPv4Address("127.0.0.1"),
                            port = 65535U,
                        ),
                        Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                    ),
                    lastRejectedConnection = Pair(
                        RpcConnectionId(
                            address = RpcIPv4Address("127.0.0.1"),
                            port = 65535U,
                        ),
                        Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                    ),
                    lastEstablishedConnection = Pair(
                        RpcConnectionId(
                            address = RpcIPv4Address("127.0.0.1"),
                            port = 65535U,
                        ),
                        Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                    ),
                    lastDisconnection = Pair(
                        RpcConnectionId(
                            address = RpcIPv4Address("127.0.0.1"),
                            port = 65535U,
                        ),
                        Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                    ),
                    lastSeen = Pair(
                        RpcConnectionId(
                            address = RpcIPv4Address("127.0.0.1"),
                            port = 65535U,
                        ),
                        Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                    ),
                    lastMiss = Pair(
                        RpcConnectionId(
                            address = RpcIPv4Address("127.0.0.1"),
                            port = 65535U,
                        ),
                        Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                    ),
                )
            ),
            jsonResponse = """
                {
                    "score": 0,
                    "trusted": true,
                    "conn_metadata": {
                        "disable_mempool": true,
                        "private_node": true
                    },
                    "peer_metadata": {
                        "responses": {
                            "sent": {
                                "branch": "1073741823",
                                "head": "1073741823",
                                "block_header": "1073741823",
                                "operations": "1073741823",
                                "protocols": "1073741823",
                                "operation_hashes_for_block": "1073741823",
                                "operations_for_block": "1073741823",
                                "checkpoint": "1073741823",
                                "protocol_branch": "1073741823",
                                "predecessor_header": "1073741823",
                                "other": "1073741823"
                            },
                            "failed": {
                                "branch": "1073741823",
                                "head": "1073741823",
                                "block_header": "1073741823",
                                "operations": "1073741823",
                                "protocols": "1073741823",
                                "operation_hashes_for_block": "1073741823",
                                "operations_for_block": "1073741823",
                                "checkpoint": "1073741823",
                                "protocol_branch": "1073741823",
                                "predecessor_header": "1073741823",
                                "other": "1073741823"
                            },
                            "received": {
                                "branch": "1073741823",
                                "head": "1073741823",
                                "block_header": "1073741823",
                                "operations": "1073741823",
                                "protocols": "1073741823",
                                "operation_hashes_for_block": "1073741823",
                                "operations_for_block": "1073741823",
                                "checkpoint": "1073741823",
                                "protocol_branch": "1073741823",
                                "predecessor_header": "1073741823",
                                "other": "1073741823"
                            },
                            "unexpected": "1073741823",
                            "outdated": "1073741823"
                        },
                        "requests": {
                            "sent": {
                                "branch": "1073741823",
                                "head": "1073741823",
                                "block_header": "1073741823",
                                "operations": "1073741823",
                                "protocols": "1073741823",
                                "operation_hashes_for_block": "1073741823",
                                "operations_for_block": "1073741823",
                                "checkpoint": "1073741823",
                                "protocol_branch": "1073741823",
                                "predecessor_header": "1073741823",
                                "other": "1073741823"
                            },
                            "received": {
                                "branch": "1073741823",
                                "head": "1073741823",
                                "block_header": "1073741823",
                                "operations": "1073741823",
                                "protocols": "1073741823",
                                "operation_hashes_for_block": "1073741823",
                                "operations_for_block": "1073741823",
                                "checkpoint": "1073741823",
                                "protocol_branch": "1073741823",
                                "predecessor_header": "1073741823",
                                "other": "1073741823"
                            },
                            "failed": {
                                "branch": "1073741823",
                                "head": "1073741823",
                                "block_header": "1073741823",
                                "operations": "1073741823",
                                "protocols": "1073741823",
                                "operation_hashes_for_block": "1073741823",
                                "operations_for_block": "1073741823",
                                "checkpoint": "1073741823",
                                "protocol_branch": "1073741823",
                                "predecessor_header": "1073741823",
                                "other": "1073741823"
                            },
                            "scheduled": {
                                "branch": "1073741823",
                                "head": "1073741823",
                                "block_header": "1073741823",
                                "operations": "1073741823",
                                "protocols": "1073741823",
                                "operation_hashes_for_block": "1073741823",
                                "operations_for_block": "1073741823",
                                "checkpoint": "1073741823",
                                "protocol_branch": "1073741823",
                                "predecessor_header": "1073741823",
                                "other": "1073741823"
                            }
                        },
                        "valid_blocks": "1073741823",
                        "old_heads": "1073741823",
                        "prevalidator_results": {
                            "cannot_download": "1073741823",
                            "cannot_parse": "1073741823",
                            "refused_by_prefilter": "1073741823",
                            "refused_by_postfilter": "1073741823",
                            "applied": "1073741823",
                            "branch_delayed": "1073741823",
                            "branch_refused": "1073741823",
                            "refused": "1073741823",
                            "duplicate": "1073741823",
                            "outdated": "1073741823"
                        },
                        "unactivated_chains": "1073741823",
                        "inactive_chains": "1073741823",
                        "future_blocks_advertised": "1073741823",
                        "unadvertised": {
                            "block": "1073741823",
                            "operations": "1073741823",
                            "protocol": "1073741823"
                        },
                        "advertisements": {
                            "sent": {
                                "head": "1073741823",
                                "branch": "1073741823"
                            },
                            "received": {
                                "head": "1073741823",
                                "branch": "1073741823"
                            }
                        }
                    },
                    "state": "running",
                    "reachable_at": {
                        "addr": "127.0.0.1",
                        "port": 65535
                    },
                    "stat": {
                        "total_sent": "9223372036854775807",
                        "total_recv": "9223372036854775807",
                        "current_inflow": 1073741823,
                        "current_outflow": 1073741823
                    },
                    "last_failed_connection": [
                        {
                            "addr": "127.0.0.1",
                            "port": 65535
                        },
                        "2022-04-12T13:07:00Z"
                    ],
                    "last_rejected_connection": [
                        {
                            "addr": "127.0.0.1",
                            "port": 65535
                        },
                        "2022-04-12T13:07:00Z"
                    ],
                    "last_established_connection": [
                        {
                            "addr": "127.0.0.1",
                            "port": 65535
                        },
                        "2022-04-12T13:07:00Z"
                    ],
                    "last_disconnection": [
                        {
                            "addr": "127.0.0.1",
                            "port": 65535
                        },
                        "2022-04-12T13:07:00Z"
                    ],
                    "last_seen": [
                        {
                            "addr": "127.0.0.1",
                            "port": 65535
                        },
                        "2022-04-12T13:07:00Z"
                    ],
                    "last_miss": [
                        {
                            "addr": "127.0.0.1",
                            "port": 65535
                        },
                        "2022-04-12T13:07:00Z"
                    ]
                }
            """.trimIndent()
        )

    private fun peersPeerPatchRequestConfiguration(acl: RpcAcl?): RequestConfiguration<ChangePeerPermissionRequest, ChangePeerPermissionResponse> =
        RequestConfiguration(
            request = ChangePeerPermissionRequest(acl),
            response = ChangePeerPermissionResponse(
                RpcPeer(
                    score = 0,
                    trusted = true,
                    connectionMetadata = RpcConnectionMetadata(
                        disableMempool = true,
                        privateNode = true,
                    ),
                    peerMetadata = RpcPeerMetadata(
                        responses = RpcPeerMetadataResponses(
                            sent = RpcPeerMetadataEntry.V1(
                                branch = "1073741823",
                                head = "1073741823",
                                blockHeader = "1073741823",
                                operations = "1073741823",
                                protocols = "1073741823",
                                operationHashesForBlock = "1073741823",
                                operationsForBlock = "1073741823",
                                checkpoint = "1073741823",
                                protocolBranch = "1073741823",
                                predecessorHeader = "1073741823",
                                other = "1073741823",
                            ),
                            failed = RpcPeerMetadataEntry.V1(
                                branch = "1073741823",
                                head = "1073741823",
                                blockHeader = "1073741823",
                                operations = "1073741823",
                                protocols = "1073741823",
                                operationHashesForBlock = "1073741823",
                                operationsForBlock = "1073741823",
                                checkpoint = "1073741823",
                                protocolBranch = "1073741823",
                                predecessorHeader = "1073741823",
                                other = "1073741823",
                            ),
                            received = RpcPeerMetadataEntry.V1(
                                branch = "1073741823",
                                head = "1073741823",
                                blockHeader = "1073741823",
                                operations = "1073741823",
                                protocols = "1073741823",
                                operationHashesForBlock = "1073741823",
                                operationsForBlock = "1073741823",
                                checkpoint = "1073741823",
                                protocolBranch = "1073741823",
                                predecessorHeader = "1073741823",
                                other = "1073741823",
                            ),
                            unexpected = "1073741823",
                            outdated = "1073741823",
                        ),
                        requests = RpcPeerMetadataRequests(
                            sent = RpcPeerMetadataEntry.V1(
                                branch = "1073741823",
                                head = "1073741823",
                                blockHeader = "1073741823",
                                operations = "1073741823",
                                protocols = "1073741823",
                                operationHashesForBlock = "1073741823",
                                operationsForBlock = "1073741823",
                                checkpoint = "1073741823",
                                protocolBranch = "1073741823",
                                predecessorHeader = "1073741823",
                                other = "1073741823",
                            ),
                            received = RpcPeerMetadataEntry.V1(
                                branch = "1073741823",
                                head = "1073741823",
                                blockHeader = "1073741823",
                                operations = "1073741823",
                                protocols = "1073741823",
                                operationHashesForBlock = "1073741823",
                                operationsForBlock = "1073741823",
                                checkpoint = "1073741823",
                                protocolBranch = "1073741823",
                                predecessorHeader = "1073741823",
                                other = "1073741823",
                            ),
                            failed = RpcPeerMetadataEntry.V1(
                                branch = "1073741823",
                                head = "1073741823",
                                blockHeader = "1073741823",
                                operations = "1073741823",
                                protocols = "1073741823",
                                operationHashesForBlock = "1073741823",
                                operationsForBlock = "1073741823",
                                checkpoint = "1073741823",
                                protocolBranch = "1073741823",
                                predecessorHeader = "1073741823",
                                other = "1073741823",
                            ),
                            scheduled = RpcPeerMetadataEntry.V1(
                                branch = "1073741823",
                                head = "1073741823",
                                blockHeader = "1073741823",
                                operations = "1073741823",
                                protocols = "1073741823",
                                operationHashesForBlock = "1073741823",
                                operationsForBlock = "1073741823",
                                checkpoint = "1073741823",
                                protocolBranch = "1073741823",
                                predecessorHeader = "1073741823",
                                other = "1073741823",
                            ),
                        ),
                        validBlocks = "1073741823",
                        oldHeads = "1073741823",
                        prevalidatorResults = RpcPrevalidatorResults(
                            cannotDownload = "1073741823",
                            cannotParse = "1073741823",
                            refusedByPrefilter = "1073741823",
                            refusedByPostfilter = "1073741823",
                            applied = "1073741823",
                            branchDelayed = "1073741823",
                            branchRefused = "1073741823",
                            refused = "1073741823",
                            duplicate = "1073741823",
                            outdated = "1073741823",
                        ),
                        unactivatedChains = "1073741823",
                        inactiveChains = "1073741823",
                        futureBlocksAdvertised = "1073741823",
                        unadvertised = RpcUnadvertised(
                            block = "1073741823",
                            operations = "1073741823",
                            protocol = "1073741823",
                        ),
                        advertisements = RpcAdvertisements(
                            sent = RpcAdvertisement(
                                head = "1073741823",
                                branch = "1073741823",
                            ),
                            received = RpcAdvertisement(
                                head = "1073741823",
                                branch = "1073741823",
                            ),
                        ),
                    ),
                    state = RpcPeerState.Running,
                    reachableAt = RpcConnectionId(
                        address = RpcIPv4Address("127.0.0.1"),
                        port = 65535U,
                    ),
                    stat = RpcNetworkStat(
                        totalSent = 9223372036854775807,
                        totalReceived = 9223372036854775807,
                        currentInflow = 1073741823,
                        currentOutflow = 1073741823,
                    ),
                    lastFailedConnection = Pair(
                        RpcConnectionId(
                            address = RpcIPv4Address("127.0.0.1"),
                            port = 65535U,
                        ),
                        Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                    ),
                    lastRejectedConnection = Pair(
                        RpcConnectionId(
                            address = RpcIPv4Address("127.0.0.1"),
                            port = 65535U,
                        ),
                        Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                    ),
                    lastEstablishedConnection = Pair(
                        RpcConnectionId(
                            address = RpcIPv4Address("127.0.0.1"),
                            port = 65535U,
                        ),
                        Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                    ),
                    lastDisconnection = Pair(
                        RpcConnectionId(
                            address = RpcIPv4Address("127.0.0.1"),
                            port = 65535U,
                        ),
                        Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                    ),
                    lastSeen = Pair(
                        RpcConnectionId(
                            address = RpcIPv4Address("127.0.0.1"),
                            port = 65535U,
                        ),
                        Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                    ),
                    lastMiss = Pair(
                        RpcConnectionId(
                            address = RpcIPv4Address("127.0.0.1"),
                            port = 65535U,
                        ),
                        Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                    ),
                )
            ),
            jsonRequest = """
                {
                    "acl": "${
                        when (acl) {
                            RpcAcl.Open -> "open"
                            RpcAcl.Trust -> "trust"
                            RpcAcl.Ban -> "ban"
                            null -> "null"
                        }
                    }"
                }
            """.trimIndent(),
            jsonResponse = """
                {
                    "score": 0,
                    "trusted": true,
                    "conn_metadata": {
                        "disable_mempool": true,
                        "private_node": true
                    },
                    "peer_metadata": {
                        "responses": {
                            "sent": {
                                "branch": "1073741823",
                                "head": "1073741823",
                                "block_header": "1073741823",
                                "operations": "1073741823",
                                "protocols": "1073741823",
                                "operation_hashes_for_block": "1073741823",
                                "operations_for_block": "1073741823",
                                "checkpoint": "1073741823",
                                "protocol_branch": "1073741823",
                                "predecessor_header": "1073741823",
                                "other": "1073741823"
                            },
                            "failed": {
                                "branch": "1073741823",
                                "head": "1073741823",
                                "block_header": "1073741823",
                                "operations": "1073741823",
                                "protocols": "1073741823",
                                "operation_hashes_for_block": "1073741823",
                                "operations_for_block": "1073741823",
                                "checkpoint": "1073741823",
                                "protocol_branch": "1073741823",
                                "predecessor_header": "1073741823",
                                "other": "1073741823"
                            },
                            "received": {
                                "branch": "1073741823",
                                "head": "1073741823",
                                "block_header": "1073741823",
                                "operations": "1073741823",
                                "protocols": "1073741823",
                                "operation_hashes_for_block": "1073741823",
                                "operations_for_block": "1073741823",
                                "checkpoint": "1073741823",
                                "protocol_branch": "1073741823",
                                "predecessor_header": "1073741823",
                                "other": "1073741823"
                            },
                            "unexpected": "1073741823",
                            "outdated": "1073741823"
                        },
                        "requests": {
                            "sent": {
                                "branch": "1073741823",
                                "head": "1073741823",
                                "block_header": "1073741823",
                                "operations": "1073741823",
                                "protocols": "1073741823",
                                "operation_hashes_for_block": "1073741823",
                                "operations_for_block": "1073741823",
                                "checkpoint": "1073741823",
                                "protocol_branch": "1073741823",
                                "predecessor_header": "1073741823",
                                "other": "1073741823"
                            },
                            "received": {
                                "branch": "1073741823",
                                "head": "1073741823",
                                "block_header": "1073741823",
                                "operations": "1073741823",
                                "protocols": "1073741823",
                                "operation_hashes_for_block": "1073741823",
                                "operations_for_block": "1073741823",
                                "checkpoint": "1073741823",
                                "protocol_branch": "1073741823",
                                "predecessor_header": "1073741823",
                                "other": "1073741823"
                            },
                            "failed": {
                                "branch": "1073741823",
                                "head": "1073741823",
                                "block_header": "1073741823",
                                "operations": "1073741823",
                                "protocols": "1073741823",
                                "operation_hashes_for_block": "1073741823",
                                "operations_for_block": "1073741823",
                                "checkpoint": "1073741823",
                                "protocol_branch": "1073741823",
                                "predecessor_header": "1073741823",
                                "other": "1073741823"
                            },
                            "scheduled": {
                                "branch": "1073741823",
                                "head": "1073741823",
                                "block_header": "1073741823",
                                "operations": "1073741823",
                                "protocols": "1073741823",
                                "operation_hashes_for_block": "1073741823",
                                "operations_for_block": "1073741823",
                                "checkpoint": "1073741823",
                                "protocol_branch": "1073741823",
                                "predecessor_header": "1073741823",
                                "other": "1073741823"
                            }
                        },
                        "valid_blocks": "1073741823",
                        "old_heads": "1073741823",
                        "prevalidator_results": {
                            "cannot_download": "1073741823",
                            "cannot_parse": "1073741823",
                            "refused_by_prefilter": "1073741823",
                            "refused_by_postfilter": "1073741823",
                            "applied": "1073741823",
                            "branch_delayed": "1073741823",
                            "branch_refused": "1073741823",
                            "refused": "1073741823",
                            "duplicate": "1073741823",
                            "outdated": "1073741823"
                        },
                        "unactivated_chains": "1073741823",
                        "inactive_chains": "1073741823",
                        "future_blocks_advertised": "1073741823",
                        "unadvertised": {
                            "block": "1073741823",
                            "operations": "1073741823",
                            "protocol": "1073741823"
                        },
                        "advertisements": {
                            "sent": {
                                "head": "1073741823",
                                "branch": "1073741823"
                            },
                            "received": {
                                "head": "1073741823",
                                "branch": "1073741823"
                            }
                        }
                    },
                    "state": "running",
                    "reachable_at": {
                        "addr": "127.0.0.1",
                        "port": 65535
                    },
                    "stat": {
                        "total_sent": "9223372036854775807",
                        "total_recv": "9223372036854775807",
                        "current_inflow": 1073741823,
                        "current_outflow": 1073741823
                    },
                    "last_failed_connection": [
                        {
                            "addr": "127.0.0.1",
                            "port": 65535
                        },
                        "2022-04-12T13:07:00Z"
                    ],
                    "last_rejected_connection": [
                        {
                            "addr": "127.0.0.1",
                            "port": 65535
                        },
                        "2022-04-12T13:07:00Z"
                    ],
                    "last_established_connection": [
                        {
                            "addr": "127.0.0.1",
                            "port": 65535
                        },
                        "2022-04-12T13:07:00Z"
                    ],
                    "last_disconnection": [
                        {
                            "addr": "127.0.0.1",
                            "port": 65535
                        },
                        "2022-04-12T13:07:00Z"
                    ],
                    "last_seen": [
                        {
                            "addr": "127.0.0.1",
                            "port": 65535
                        },
                        "2022-04-12T13:07:00Z"
                    ],
                    "last_miss": [
                        {
                            "addr": "127.0.0.1",
                            "port": 65535
                        },
                        "2022-04-12T13:07:00Z"
                    ]
                }
            """.trimIndent()
        )

    private val peersPeerBannedGetRequestConfiguration: RequestConfiguration<Unit, GetPeerBannedStatusResponse>
        get() = RequestConfiguration(
            response = GetPeerBannedStatusResponse(isBanned = true),
            jsonResponse = """
                true
            """.trimIndent()
        )

    private val peersPeerLogGetRequestConfiguration: RequestConfiguration<Unit, GetPeerEventsResponse>
        get() = RequestConfiguration(
            response = GetPeerEventsResponse(
                listOf(
                    RpcPeerPoolEvent(
                        kind = RpcPeerPoolEvent.Kind.RejectingRequest,
                        timestamp = Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
                        address = RpcIPv4Address("127.0.0.1"),
                        port = 65535U,
                    )
                )
            ),
            jsonResponse = """
                [
                    {
                        "kind": "rejecting_request",
                        "timestamp": "2022-04-12T13:07:00Z",
                        "addr": "127.0.0.1",
                        "port": 65535
                    }
                ]
            """.trimIndent()
        )

    private data class RequestConfiguration<Req, Res>(val request: Req, val response: Res, val jsonRequest: String?, val jsonResponse: String)
    private fun <Res> RequestConfiguration(response: Res, jsonResponse: String): RequestConfiguration<Unit, Res> = RequestConfiguration(Unit, response, null, jsonResponse)
}