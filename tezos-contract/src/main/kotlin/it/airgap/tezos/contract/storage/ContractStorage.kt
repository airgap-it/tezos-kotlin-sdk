package it.airgap.tezos.contract.storage

import it.airgap.tezos.contract.internal.context.TezosContractContext.micheline
import it.airgap.tezos.contract.internal.context.TezosContractContext.normalized
import it.airgap.tezos.contract.internal.storage.LazyMetaContractStorage
import it.airgap.tezos.contract.internal.storage.MetaContractStorage
import it.airgap.tezos.contract.internal.storage.MetaContractStorageEntry
import it.airgap.tezos.contract.internal.utils.failWithContractException
import it.airgap.tezos.contract.type.ContractCode
import it.airgap.tezos.contract.type.LazyContractCode
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.normalizer.Normalizer
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.MichelineMichelsonExpressionBuilder
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.type.contract.RpcScriptParsing

/**
 * Contract storage handler.
 * Its main purpose is to fetch content of the contract storage.
 */
public class ContractStorage internal constructor(
    private val meta: LazyMetaContractStorage,
    private val contract: Block.Context.Contracts.Contract,
    private val michelineNormalizer: Normalizer<Micheline>,
) {

    /**
     * Fetches the storage content from the node.
     * Can be configured with optional [HTTP headers][headers] to customize the request.
     */
    public suspend fun get(headers: List<HttpHeader> = emptyList()): ContractStorageEntry? {
        val value = contract.storage.getNormalized(headers) ?: return null
        val meta = meta.get(headers)

        return meta.entryFrom(value)
    }

    private suspend fun Block.Context.Contracts.Contract.Storage.getNormalized(headers: List<HttpHeader>): Micheline? =
        try {
            normalized.post(RpcScriptParsing.OptimizedLegacy, headers).storage
        } catch (e: Exception) {
            get(headers).storage?.normalized(michelineNormalizer)
        }

    internal class Factory(
        private val metaFactory: MetaContractStorage.Factory,
        private val contract: Block.Context.Contracts.Contract,
        private val michelineNormalizer: Normalizer<Micheline>,
    ) {
        fun create(code: LazyContractCode): ContractStorage {
            val meta = code.map { it.toMetaContractStorage() }
            return ContractStorage(meta, contract, michelineNormalizer)
        }

        private fun ContractCode.toMetaContractStorage(): MetaContractStorage {
            if (storage !is MichelinePrimitiveApplication || storage.args.size != 1) failWithUnknownStorageType()
            val type = storage.args.first().normalized(michelineNormalizer)

            return metaFactory.create(type)
        }

        private fun failWithUnknownStorageType(): Nothing = failWithContractException("Unknown contract storage type.")
    }
}

/**
 * Types of contract storage entries that represent the content of the storage.
 */
public sealed interface ContractStorageEntry {

    /**
     * The Micheline value of the entry.
     */
    public val value: Micheline

    /**
     * The entry's type definition.
     */
    public val type: Micheline

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int

    /**
     * All known annotations of the entry.
     */
    public val names: Set<String>
        get() {
            val type = type as? MichelinePrimitiveApplication ?: return emptySet()
            return type.annots.map { it.value }.toSet()
        }

    /**
     * Entry that represents a [Micheline] value.
     */
    public class Value internal constructor(
        override val value: Micheline,
        internal val meta: MetaContractStorageEntry.Basic,
    ) : ContractStorageEntry {

        /**
         * The entry's type definition.
         */
        override val type: Micheline
            get() = meta.type

        override fun equals(other: Any?): Boolean =
            when {
                this === other -> true
                other is Value -> value == other.value && type == other.type
                else -> false
            }
        override fun hashCode(): Int = listOf(value, type).fold(0) { acc, v -> (31 * acc) + v.hashCode() }
    }

    /**
     * Group of entries aggregated in the form of an object.
     */
    public class Object internal constructor(
        override val value: Micheline,
        internal val meta: MetaContractStorageEntry.Basic,
        internal val elements: List<ContractStorageEntry>,
    ) : ContractStorageEntry, kotlin.collections.Map<String, ContractStorageEntry> {

        /**
         * The entry's type definition.
         */
        override val type: Micheline
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

    /**
     * Group of entries aggregated in the form of a list.
     */
    public class Sequence internal constructor(
        override val value: Micheline,
        internal val meta: MetaContractStorageEntry.Basic,
        internal val elements: List<ContractStorageEntry>,
    ) : ContractStorageEntry, List<ContractStorageEntry> by elements {

        /**
         * The entry's type definition.
         */
        override val type: Micheline
            get() = meta.type

        override fun equals(other: Any?): Boolean =
            when {
                this === other -> true
                other is Sequence -> value == other.value && type == other.type && elements == other.elements
                else -> false
            }

        override fun hashCode(): Int = listOf(value, type, elements).fold(0) { acc, v -> (31 * acc) + v.hashCode() }
    }

    /**
     * Group of entries aggregated in the form of a map.
     */
    public class Map internal constructor(
        override val value: Micheline,
        internal val meta: MetaContractStorageEntry,
        internal val map: kotlin.collections.Map<ContractStorageEntry, ContractStorageEntry>,
    ) : ContractStorageEntry, kotlin.collections.Map<ContractStorageEntry, ContractStorageEntry> by map {

        /**
         * The entry's type definition.
         */
        override val type: Micheline
            get() = meta.type

        /**
         * Returns the value corresponding to the given [key,] or null if such a key is not present in the map.
         */
        public operator fun get(key: Micheline): ContractStorageEntry? =
            entries.find { entry -> entry.key.value == key }?.value

        override fun equals(other: Any?): Boolean =
            when {
                this === other -> true
                other is Map -> value == other.value && type == other.type && map == other.map
                else -> false
            }

        override fun hashCode(): Int = listOf(value, type, map).fold(0) { acc, v -> (31 * acc) + v.hashCode() }
    }

    /**
     * Big map handler.
     * Should be used to fetch a big map values.
     */
    public class BigMap internal constructor(
        public val id: String,
        override val value: Micheline,
        internal val meta: MetaContractStorageEntry.BigMap,
        internal val rpc: Block.Context.BigMaps,
        private val michelsonToMichelineConverter: Converter<Michelson, Micheline>,
    ) : ContractStorageEntry {

        /**
         * The entry's type definition.
         */
        override val type: Micheline
            get() = meta.type

        /**
         * Fetches the value corresponding to the given [key], or null if such a key is not present in the map.
         * Can be configured with optional [HTTP headers][headers] to customize the request.
         */
        public suspend fun get(key: Micheline, headers: List<HttpHeader> = emptyList()): ContractStorageEntry? {
            val scriptExpr = meta.scriptExpr(key)
            val value = rpc(id)(scriptExpr).get(headers).value

            return value?.let { meta.entryFrom(it) }
        }

        /**
         * Fetches the value corresponding to the [created key][createKeyExpression], or null if such a key is not present in the map.
         * Can be configured with optional [HTTP headers][headers] to customize the request.
         */
        public suspend fun get(headers: List<HttpHeader> = emptyList(), createKeyExpression: MichelineMichelsonExpressionBuilder.() -> Unit): ContractStorageEntry? {
            val key = micheline(michelsonToMichelineConverter, createKeyExpression)
            return get(key, headers)
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
