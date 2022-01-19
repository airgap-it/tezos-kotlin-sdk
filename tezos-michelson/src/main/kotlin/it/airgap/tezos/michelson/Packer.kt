package it.airgap.tezos.michelson

import it.airgap.tezos.core.TezosSdk
import it.airgap.tezos.core.internal.utils.toHexString
import it.airgap.tezos.michelson.internal.di.scoped
import it.airgap.tezos.michelson.internal.packer.MichelinePacker
import it.airgap.tezos.michelson.micheline.MichelineNode

// -- Micheline: ByteArray --

public fun <T : MichelineNode> T.packToBytes(schema: MichelineNode? = null): ByteArray = packToBytes(TezosSdk.instance.dependencyRegistry.scoped().michelinePacker, schema)
internal fun <T : MichelineNode> T.packToBytes(michelinePacker: MichelinePacker, schema: MichelineNode? = null): ByteArray = michelinePacker.pack(this, schema)

// -- Micheline: String --

public fun <T : MichelineNode> T.packToString(withHexPrefix: Boolean = false, schema: MichelineNode? = null): String = packToString(TezosSdk.instance.dependencyRegistry.scoped().michelinePacker, withHexPrefix, schema)
internal fun <T : MichelineNode> T.packToString(michelinePacker: MichelinePacker, withHexPrefix: Boolean = false, schema: MichelineNode? = null): String = michelinePacker.pack(this, schema).toHexString().asString(withPrefix = withHexPrefix)
