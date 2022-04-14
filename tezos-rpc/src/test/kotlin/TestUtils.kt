import it.airgap.tezos.rpc.internal.utils.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

// -- extensions --

internal fun String.normalizeWith(json: Json): String = json.encodeToString(json.decodeFromString<JsonElement>(this))