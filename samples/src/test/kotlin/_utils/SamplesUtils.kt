package _utils

internal fun hexToBytes(string: String): ByteArray =
    string.chunked(2).map { it.toInt(16).toByte() }.toByteArray()

internal fun url(baseUrl: String, endpoint: String) =
    "${baseUrl.trimEnd('/')}/${endpoint.trimEnd('/')}".trimEnd('/')

private val messages: MutableSet<String> = mutableSetOf()
internal fun printlnOnce(message: String) {
    if (!messages.contains(message)) {
        messages.add(message)
        println(message)
    }
}