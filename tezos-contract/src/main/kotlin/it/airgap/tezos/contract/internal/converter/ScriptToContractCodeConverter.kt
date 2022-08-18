package it.airgap.tezos.contract.internal.converter

import it.airgap.tezos.contract.internal.utils.failWithContractException
import it.airgap.tezos.contract.type.ContractCode
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.comparator.isPrim
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.operation.contract.Script
import kotlin.reflect.KClass

internal class ScriptToContractCodeConverter : Converter<Script, ContractCode> {
    override fun convert(value: Script): ContractCode {
        val contractCode = value.code as? MichelineSequence ?: failWithInvalidMichelineType(MichelineSequence::class, value.code::class)
        if (contractCode.nodes.size != 3) failWithUnknownCodeType()

        val parameter = contractCode.nodes[0].takeIf { it.isPrim(MichelsonType.Parameter) } ?: failWithUnknownCodeType()
        val storage = contractCode.nodes[1].takeIf { it.isPrim(MichelsonType.Storage) } ?: failWithUnknownCodeType()
        val code = contractCode.nodes[2].takeIf { it.isPrim(MichelsonType.Code) } ?: failWithUnknownCodeType()

        return ContractCode(parameter, storage, code)
    }

    private fun failWithInvalidMichelineType(expected: KClass<out Micheline>, actual: KClass<out Micheline>): Nothing =
        failWithContractException("Invalid Micheline type, expected `$expected` but got `$actual`.")

    private fun failWithUnknownCodeType(): Nothing =
        failWithContractException("Unknown contract code type.")
}