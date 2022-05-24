package it.airgap.tezos.rpc.type.network

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.rpc.internal.rpcModule
import kotlinx.serialization.json.Json
import mockTezos
import normalizeWith
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IPAddressTest {

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
    fun `should serialize RpcIPAddress`() {
        objectsWithJson.forEach { (value, expected) ->
            val actual = json.encodeToString(RpcIPAddress.serializer(), value)
            assertEquals(expected.normalizeWith(json), actual)
        }
    }

    @Test
    fun `should deserialize RpcIPAddress`() {
        objectsWithJson.forEach { (expected, string) ->
            val actual = json.decodeFromString(RpcIPAddress.serializer(), string)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `should create RpcIPAddress from String`() {
        assertEquals(RpcIPv4Address("127.0.0.1"), RpcIPAddress.fromString("127.0.0.1"))
        assertEquals(RpcIPv6Address("0000:0000:0000:0000:0000:0000:0000:0001"), RpcIPAddress.fromString("0000:0000:0000:0000:0000:0000:0000:0001"))
    }

    @Test
    fun `should check if RpcIPAddress is valid`() {
        assertTrue(RpcIPAddress.isValid("127.0.0.1"), "Expected \"127.0.0.1\" to be recognized as a valid IP address.")
        assertTrue(RpcIPAddress.isValid("0000:0000:0000:0000:0000:0000:0000:0001"), "Expected \"0000:0000:0000:0000:0000:0000:0000:0001\" to be recognized as a valid IP address.")
        assertTrue(RpcIPAddress.isValid("::1"), "Expected \"::1\" to be recognized as a valid IP address.")
        assertFalse(RpcIPAddress.isValid(""), "Expected \"\" to be recognized as an invalid IP address.")
        assertFalse(RpcIPAddress.isValid("532.0.1.432"), "Expected \"127\" to be recognized as an invalid IP address.")
    }

    private val objectsWithJson: List<Pair<RpcIPAddress, String>>
        get() = listOf(
            RpcIPv4Address("127.0.0.1") to "\"127.0.0.1\"",
            RpcIPv6Address("0000:0000:0000:0000:0000:0000:0000:0001") to "\"0000:0000:0000:0000:0000:0000:0000:0001\""
        )
}