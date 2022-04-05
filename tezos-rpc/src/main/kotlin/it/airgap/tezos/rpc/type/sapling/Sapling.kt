package it.airgap.tezos.rpc.type.sapling

import it.airgap.tezos.core.type.HexString
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// -- RpcSaplingCiphertext

@Serializable
public data class RpcSaplingCiphertext(
    public val cv: @Contextual HexString,
    public val epk: @Contextual HexString,
    @SerialName("payload_enc") public val payloadEnc: @Contextual HexString,
    @SerialName("nonce_enc") public val nonceEnc: @Contextual HexString,
    @SerialName("payload_out") public val payloadOut: @Contextual HexString,
    @SerialName("nonce_out") public val nonceOut: @Contextual HexString,
)