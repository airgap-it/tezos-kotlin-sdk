package _utils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

// -- constants --

internal val TEZOS_NODE = "https://testnet-tezos.giganode.io"

// -- util functions --

internal fun hexToBytes(string: String): ByteArray =
    string.removePrefix("0x").chunked(2).map { it.toInt(16).toByte() }.toByteArray()

internal fun url(baseUrl: String, endpoint: String) =
    "${baseUrl.trimEnd('/')}/${endpoint.trimEnd('/')}".trimEnd('/')

private val messages: MutableSet<String> = mutableSetOf()
internal fun printlnOnce(message: String) {
    if (!messages.contains(message)) {
        messages.add(message)
        println(message)
    }
}

// -- extensions --

internal fun String.toJson(): JsonElement = Json.decodeFromString(this)