internal fun hexToBytes(string: String): ByteArray =
    string.chunked(2).map { it.toInt(16).toByte() }.toByteArray()