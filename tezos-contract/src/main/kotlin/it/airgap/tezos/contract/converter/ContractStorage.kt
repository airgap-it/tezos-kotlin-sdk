package it.airgap.tezos.contract.converter

import it.airgap.tezos.contract.internal.context.TezosContractContext
import it.airgap.tezos.contract.storage.ContractStorageEntry
import kotlin.reflect.KClass

/**
 * Convenience method to get the current entry as [ContractStorageEntry.Value].
 *
 * @throws IllegalArgumentException if the current entry is not a [ContractStorageEntry.Value].
 */
public val ContractStorageEntry?.valueEntry: ContractStorageEntry.Value
    get() = this as? ContractStorageEntry.Value
        ?: failWithInvalidStorageEntryType(ContractStorageEntry.Value::class)

/**
 * Convenience method to get the current entry as [ContractStorageEntry.Object].
 *
 * @throws IllegalArgumentException if the current entry is not a [ContractStorageEntry.Object].
 */
public val ContractStorageEntry?.objectEntry: ContractStorageEntry.Object
    get() = this as? ContractStorageEntry.Object
        ?: failWithInvalidStorageEntryType(ContractStorageEntry.Object::class)

/**
 * Convenience method to get the current entry as [ContractStorageEntry.Sequence].
 *
 * @throws IllegalArgumentException if the current entry is not a [ContractStorageEntry.Sequence].
 */
public val ContractStorageEntry?.sequenceEntry: ContractStorageEntry.Sequence
    get() = this as? ContractStorageEntry.Sequence
        ?: failWithInvalidStorageEntryType(ContractStorageEntry.Sequence::class)

/**
 * Convenience method to get the current entry as [ContractStorageEntry.Map].
 *
 * @throws IllegalArgumentException if the current entry is not a [ContractStorageEntry.Map].
 */
public val ContractStorageEntry?.mapEntry: ContractStorageEntry.Map
    get() = this as? ContractStorageEntry.Map ?: failWithInvalidStorageEntryType(ContractStorageEntry.Map::class)

/**
 * Convenience method to get the current entry as [ContractStorageEntry.BigMap].
 *
 * @throws IllegalArgumentException if the current entry is not a [ContractStorageEntry.BigMap].
 */
public val ContractStorageEntry?.bigMapEntry: ContractStorageEntry.BigMap
    get() = this as? ContractStorageEntry.BigMap
        ?: failWithInvalidStorageEntryType(ContractStorageEntry.BigMap::class)

private fun ContractStorageEntry?.failWithInvalidStorageEntryType( expected: KClass<out ContractStorageEntry>): Nothing =
    TezosContractContext.failWithIllegalArgument("Contract storage entry ${this?.let { it::class } ?: "null" } is not $expected.")