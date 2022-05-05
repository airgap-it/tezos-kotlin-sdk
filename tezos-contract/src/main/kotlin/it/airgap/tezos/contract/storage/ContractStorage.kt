package it.airgap.tezos.contract.storage

import it.airgap.tezos.contract.internal.storage.MetaContractStorage
import it.airgap.tezos.contract.internal.storage.MetaContractStorageEntry
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.cache.Cached
import it.airgap.tezos.rpc.type.contract.RpcScriptParsing

// -- ContractStorage --

public class ContractStorage internal constructor(
    private val metaCached: Cached<MetaContractStorage>,
    private val rpc: Block.Context.Contracts.Contract,
) {
    public suspend fun get(headers: List<HttpHeader> = emptyList()): ContractStorageEntry? {
        val value = rpc.storage.normalized.post(RpcScriptParsing.OptimizedLegacy, headers).storage ?: return null
        val meta = metaCached.get(headers)

        return meta.entryFrom(value)
    }
}

// -- ContractStorageEntry --

public sealed interface ContractStorageEntry {
    public val value: MichelineNode
    public val type: MichelineNode

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int

    public val names: Set<String>
        get() {
            val type = type as? MichelinePrimitiveApplication ?: return emptySet()
            return type.annots.map { it.value }.toSet()
        }

    public class Value internal constructor(
        override val value: MichelineNode,
        internal val meta: MetaContractStorageEntry.Basic,
    ) : ContractStorageEntry {

        override val type: MichelineNode
            get() = meta.type

        override fun equals(other: Any?): Boolean =
            when {
                this === other -> true
                other is Value -> value == other.value && type == other.type
                else -> false
            }

        override fun hashCode(): Int = listOf(value, type).fold(0) { acc, v -> (31 * acc) + v.hashCode() }
    }

    public class Object internal constructor(
        override val value: MichelineNode,
        internal val meta: MetaContractStorageEntry.Basic,
        internal val elements: List<ContractStorageEntry>,
    ) : ContractStorageEntry, kotlin.collections.Map<String, ContractStorageEntry> {

        override val type: MichelineNode
            get() = meta.type

        private val map: kotlin.collections.Map<String, ContractStorageEntry> =
            elements.flatMap { element ->
                element.names.map { it to element }
            }.toMap()

        override val entries: Set<kotlin.collections.Map.Entry<String, ContractStorageEntry>>
            get() = map.entries

        override val keys: Set<String>
            get() = map.keys

        override val size: Int
            get() = elements.size

        override val values: Collection<ContractStorageEntry>
            get() = elements

        override fun containsKey(key: String): Boolean = map.containsKey(key)
        override fun containsValue(value: ContractStorageEntry): Boolean = elements.contains(value)

        override fun get(key: String): ContractStorageEntry? = map[key]
        public operator fun get(name: MichelinePrimitiveApplication.Annotation): ContractStorageEntry? = get(name.value)
        public operator fun get(index: Int): ContractStorageEntry? = elements.getOrNull(index)

        override fun isEmpty(): Boolean = map.isEmpty() && elements.isEmpty()

        override fun equals(other: Any?): Boolean =
            when {
                this === other -> true
                other is Object -> value == other.value && type == other.type && elements == other.elements
                else -> false
            }

        override fun hashCode(): Int = listOf(value, type, elements).fold(0) { acc, v -> (31 * acc) + v.hashCode() }
    }

    public class Sequence internal constructor(
        override val value: MichelineNode,
        internal val meta: MetaContractStorageEntry.Basic,
        internal val elements: List<ContractStorageEntry>,
    ) : ContractStorageEntry, List<ContractStorageEntry> by elements {
        override val type: MichelineNode
            get() = meta.type

        override fun equals(other: Any?): Boolean =
            when {
                this === other -> true
                other is Sequence -> value == other.value && type == other.type && elements == other.elements
                else -> false
            }

        override fun hashCode(): Int = listOf(value, type, elements).fold(0) { acc, v -> (31 * acc) + v.hashCode() }
    }

    public class Map internal constructor(
        override val value: MichelineNode,
        internal val meta: MetaContractStorageEntry,
        internal val map: kotlin.collections.Map<ContractStorageEntry, ContractStorageEntry>,
    ) : ContractStorageEntry, kotlin.collections.Map<ContractStorageEntry, ContractStorageEntry> by map {

        override val type: MichelineNode
            get() = meta.type

        public operator fun get(key: MichelineNode): ContractStorageEntry? =
            entries.find { entry -> entry.key.value == key }?.value

        override fun equals(other: Any?): Boolean =
            when {
                this === other -> true
                other is Map -> value == other.value && type == other.type && map == other.map
                else -> false
            }

        override fun hashCode(): Int = listOf(value, type, map).fold(0) { acc, v -> (31 * acc) + v.hashCode() }
    }

    public class BigMap internal constructor(
        public val id: String,
        override val value: MichelineNode,
        internal val meta: MetaContractStorageEntry.BigMap,
        internal val rpc: Block.Context.BigMaps,
    ) : ContractStorageEntry {

        override val type: MichelineNode
            get() = meta.type

        public suspend fun get(key: MichelineNode, headers: List<HttpHeader> = emptyList()): ContractStorageEntry? {
            val scriptExpr = meta.scriptExpr(key)
            val value = rpc(id)(scriptExpr).get(headers).value

            return value?.let { meta.entryFrom(it) }
        }

        override fun equals(other: Any?): Boolean =
            when {
                this === other -> true
                other is BigMap -> id == other.id && value == other.value && type == other.type
                else -> false
            }

        override fun hashCode(): Int = listOf(id, value, type).fold(0) { acc, v -> (31 * acc) + v.hashCode() }
    }
}
