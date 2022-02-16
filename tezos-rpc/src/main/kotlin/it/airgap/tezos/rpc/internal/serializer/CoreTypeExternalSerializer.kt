@file:OptIn(ExperimentalSerializationApi::class)

package it.airgap.tezos.rpc.internal.serializer

import it.airgap.tezos.core.type.HexString
import it.airgap.tezos.core.type.encoded.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer

@Serializer(forClass = HexString::class)
internal object HexStringSerializer

@Serializer(forClass = BlockHash::class)
internal object BlockHashSerializer

@Serializer(forClass = ChainId::class)
internal object ChainIdSerializer

@Serializer(forClass = ContextHash::class)
internal object ContextHashSerializer

@Serializer(forClass = OperationHash::class)
internal object OperationHashSerializer

@Serializer(forClass = OperationListListHash::class)
internal object OperationListListHashSerializer

@Serializer(forClass = ProtocolHash::class)
internal object ProtocolHashSerializer
