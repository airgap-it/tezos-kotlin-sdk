package it.airgap.tezos.rpc.type.chain

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.core.type.Timestamp
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.ProtocolHash
import it.airgap.tezos.rpc.internal.rpcModule
import it.airgap.tezos.rpc.internal.utils.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mockTezos
import normalizeWith
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ChainTest {

    private lateinit var json: Json

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        val tezos = mockTezos()
        json = Json(from = tezos.rpcModule.dependencyRegistry.json) {
            prettyPrint = true
        }
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should serialize RpcActiveChain`() {
        objectsWithJson.forEach { (value, expected) ->
            val actual = json.encodeToString(value)
            assertEquals(expected.normalizeWith(json), actual)
        }
    }

    @Test
    fun `should deserialize RpcActiveChain`() {
        objectsWithJson.forEach { (expected, string) ->
            val actual = json.decodeFromString<RpcActiveChain>(string)
            assertEquals(expected, actual)
        }
    }

    private val objectsWithJson: List<Pair<RpcActiveChain, String>>
        get() = listOf(
            RpcActiveChain.Main(ChainId("NetXdQprcVkpaWU")) to """
                {
                    "chain_id": "NetXdQprcVkpaWU"
                }
            """.trimIndent(),
            RpcActiveChain.Test(
                chainId = ChainId("NetXdQprcVkpaWU"),
                testProtocol = ProtocolHash("PsYLVpVvgbLhAhoqAkMFUo6gudkJ9weNXhUYCiLDzcUpFpkk8Wt"),
                expirationDate = Timestamp.Rfc3339("2022-04-12T13:07:00Z"),
            ) to """
                {
                    "chain_id": "NetXdQprcVkpaWU",
                    "test_protocol": "PsYLVpVvgbLhAhoqAkMFUo6gudkJ9weNXhUYCiLDzcUpFpkk8Wt",
                    "expiration_date": "2022-04-12T13:07:00Z"
                }
            """.trimIndent(),
            RpcActiveChain.Stopping(ChainId("NetXdQprcVkpaWU")) to """
                {
                    "stopping": "NetXdQprcVkpaWU"
                }
            """.trimIndent()
        )
}