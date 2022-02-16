@file:OptIn(ExperimentalSerializationApi::class)

package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ChainId
import it.airgap.tezos.core.type.encoded.ProtocolHash
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer

@Serializer(forClass = HexString::class)
internal object HexStringSerializer

@Serializer(forClass = BlockHash::class)
internal object BlockHashSerializer

@Serializer(forClass = ChainId::class)
internal object ChainIdSerializer

@Serializer(forClass = ProtocolHash::class)
internal object ProtocolHashSerializer

