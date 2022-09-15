package it.airgap.tezos.michelson.internal.packer

import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.crypto.Crypto
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.decodeFromBytes
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.packToBytes
import it.airgap.tezos.michelson.micheline.Micheline

internal class MichelineToScriptExprHashPacker(
    private val crypto: Crypto,
    private val encodedBytesCoder: EncodedBytesCoder,
    private val michelinePacker: BytesPacker<Micheline>,
) : Packer<Micheline, ScriptExprHash> {

    override fun pack(value: Micheline, schema: Micheline?): ScriptExprHash {
        val exprBytes = value.packToBytes(schema, michelinePacker)
        val exprHash = crypto.hash(exprBytes, 32)

        return ScriptExprHash.decodeFromBytes(exprHash, encodedBytesCoder)
    }
}