package it.airgap.tezos.contract

import it.airgap.tezos.core.decodeFromBytes
import it.airgap.tezos.core.internal.coder.EncodedBytesCoder
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.internal.converter.MichelineToCompactStringConverter
import it.airgap.tezos.michelson.internal.packer.MichelinePacker
import it.airgap.tezos.michelson.isPrim
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.packToBytes
import it.airgap.tezos.michelson.toCompactExpression
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

        return meta.valueFrom(value)
    }
}

// -- ContractStorageEntry --

public sealed interface ContractStorageEntry {
    public val value: MichelineNode
    public val type: MichelineNode
    public val names: Set<String>

    public data class Value internal constructor(
        override val value: MichelineNode,
        override val type: MichelineNode,
        override val names: Set<String>,
    ) : ContractStorageEntry {
        internal constructor(value: MichelineNode, type: MichelineNode, names: List<MichelinePrimitiveApplication.Annotation>) : this(value, type, names.map { it.value }.toSet())
    }

    public data class Object internal constructor(
        override val value: MichelineNode,
        override val type: MichelineNode,
        override val names: Set<String>,
        internal val elements: List<ContractStorageEntry>,
    ) : ContractStorageEntry, kotlin.collections.Map<String, ContractStorageEntry> {
        internal constructor(
            value: MichelineNode,
            type: MichelineNode,
            names: List<MichelinePrimitiveApplication.Annotation>,
            entries: List<ContractStorageEntry>,
        ) : this(value, type, names.map { it.value }.toSet(), entries)

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
    }

    public data class Sequence internal constructor(
        override val value: MichelineNode,
        override val type: MichelineNode,
        override val names: Set<String>,
        internal val elements: List<ContractStorageEntry>,
    ) : ContractStorageEntry, List<ContractStorageEntry> by elements {
        internal constructor(
            value: MichelineNode,
            type: MichelineNode,
            names: List<MichelinePrimitiveApplication.Annotation>,
            entries: List<ContractStorageEntry>,
        ) : this(value, type, names.map { it.value }.toSet(), entries)
    }

    public data class Map internal constructor(
        override val value: MichelineNode,
        override val type: MichelineNode,
        override val names: Set<String>,
        internal val map: kotlin.collections.Map<ContractStorageEntry, ContractStorageEntry>,
    ) : ContractStorageEntry, kotlin.collections.Map<ContractStorageEntry, ContractStorageEntry> by map {
        internal constructor(
            value: MichelineNode,
            type: MichelineNode,
            names: List<MichelinePrimitiveApplication.Annotation>,
            map: kotlin.collections.Map<ContractStorageEntry, ContractStorageEntry>
        ) : this(value, type, names.map { it.value }.toSet(), map)

        public operator fun get(key: MichelineNode): ContractStorageEntry? =
            entries.find { entry -> entry.key.value == key }?.value
    }

    public class BigMap internal constructor(
        public val id: String,
        override val value: MichelineNode,
        override val type: MichelineNode,
        override val names: Set<String>,
        private val rpc: Block.Context.BigMaps,
        private val encodedBytesCoder: EncodedBytesCoder,
        private val michelinePacker: MichelinePacker,
        private val meta: MetaContractStorage,
    ) : ContractStorageEntry {
        internal constructor(
            id: String,
            value: MichelineNode,
            type: MichelineNode,
            names: List<MichelinePrimitiveApplication.Annotation>,
            rpc: Block.Context.BigMaps,
            encodedBytesCoder: EncodedBytesCoder,
            michelinePacker: MichelinePacker,
            meta: MetaContractStorage,
        ) : this(id, value, type, names.map { it.value }.toSet(), rpc, encodedBytesCoder, michelinePacker, meta)

        public fun copy(
            id: String = this.id,
            value: MichelineNode = this.value,
            type: MichelineNode = this.type,
            names: Set<String> = this.names,
        ): BigMap = BigMap(id, value, type, names, rpc, encodedBytesCoder, michelinePacker, meta)

        public suspend fun get(
            key: MichelineNode,
            headers: List<HttpHeader> = emptyList(),
        ): ContractStorageEntry? {
            if (type !is MichelinePrimitiveApplication) return null // TODO: throw an error?

            val keyType = type.args.first()
            val keyBytes = key.packToBytes(keyType, michelinePacker)
            val scriptExpr = ScriptExprHash.decodeFromBytes(keyBytes, encodedBytesCoder)

            val value = rpc(id)(scriptExpr).get(headers).value

            return value?.let { meta.valueFrom(it) }
        }

        override fun equals(other: Any?): Boolean =
            when {
                this === other -> true
                other is BigMap -> id == other.id && value == other.value && type == other.type && names == other.names
                else -> false
            }

        override fun hashCode(): Int = listOf(id, value, type, names).fold(0) { acc, v -> (31 * acc) + v.hashCode() }
    }
}

// -- MetaContractStorage --

internal class MetaContractStorage(
    private val type: MichelineNode,
    private val rpc: Block,
    private val encodedBytesCoder: EncodedBytesCoder,
    private val michelinePacker: MichelinePacker,
    private val michelineToCompactStringConverter: MichelineToCompactStringConverter
) {
    fun valueFrom(value: MichelineNode): ContractStorageEntry = value.toStorageEntry(type)

    private fun MichelineNode.toStorageEntry(type: MichelineNode): ContractStorageEntry =
        when (type) {
            is MichelinePrimitiveApplication -> type.createStorageEntry(this)
            else -> failWithTypeValueMismatch(type, this)
        }

    private fun MichelinePrimitiveApplication.createStorageEntry(value: MichelineNode): ContractStorageEntry =
        // TODO: better type safety
        when {
            isPrim(MichelsonType.BigMap) && value is MichelineLiteral.Integer -> {
                ContractStorageEntry.BigMap(value.int, value, this, annots, rpc.context.bigMaps, encodedBytesCoder, michelinePacker, this@MetaContractStorage)
            }
            (isPrim(MichelsonType.List) || isPrim(MichelsonType.Set)) && value is MichelineSequence -> ContractStorageEntry.Sequence(
                value,
                this,
                annots,
                value.nodes.map { it.toStorageEntry(args.first()) }
            )
            isPrim(MichelsonType.Lambda) && value is MichelineSequence -> ContractStorageEntry.Sequence(
                value,
                this,
                annots,
                value.nodes.map { it.toStorageEntry(this) }
            )
            isPrim(MichelsonType.Map) && value is MichelineSequence -> ContractStorageEntry.Map(
                value,
                this,
                annots,
                value.nodes.associate {
                    it.toStorageEntry(args[0]) to it.toStorageEntry(args[1])
                },
            )
            args.isEmpty() -> ContractStorageEntry.Value(value, this, annots)
            value is MichelinePrimitiveApplication -> ContractStorageEntry.Object(
                value,
                this,
                annots.map { it.value }.toSet(),
                value.args.zip(args).map { (v, t) -> v.toStorageEntry(t) }
            )
            else -> failWithTypeValueMismatch(this, value)
        }

    private fun failWithTypeValueMismatch(type: MichelineNode, value: MichelineNode): Nothing =
        failWithIllegalArgument("Type ${type.toCompactExpression(michelineToCompactStringConverter)} and value ${value.toCompactExpression(michelineToCompactStringConverter)} mismatch.")
}