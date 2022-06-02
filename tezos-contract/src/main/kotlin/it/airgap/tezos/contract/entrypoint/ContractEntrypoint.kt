package it.airgap.tezos.contract.entrypoint

import it.airgap.tezos.contract.internal.context.TezosContractContext.asTezosNatural
import it.airgap.tezos.contract.internal.context.TezosContractContext.normalized
import it.airgap.tezos.contract.internal.entrypoint.LazyMetaContractEntrypoint
import it.airgap.tezos.contract.internal.entrypoint.MetaContractEntrypoint
import it.airgap.tezos.contract.internal.estimator.withMinFee
import it.airgap.tezos.contract.internal.utils.failWithContractException
import it.airgap.tezos.contract.type.ContractCode
import it.airgap.tezos.contract.type.LazyContractCode
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.normalizer.Normalizer
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ContractHash
import it.airgap.tezos.core.type.encoded.ImplicitAddress
import it.airgap.tezos.core.type.number.TezosNatural
import it.airgap.tezos.core.type.tez.Mutez
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.comparator.isPrim
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.operation.Operation
import it.airgap.tezos.operation.OperationContent
import it.airgap.tezos.operation.contract.Entrypoint
import it.airgap.tezos.operation.contract.Parameters
import it.airgap.tezos.rpc.active.block.Block
import it.airgap.tezos.rpc.active.block.GetContractEntrypointsResponse
import it.airgap.tezos.rpc.http.HttpHeader
import it.airgap.tezos.rpc.internal.cache.Cached
import it.airgap.tezos.rpc.internal.estimator.FeeEstimator
import it.airgap.tezos.rpc.type.limits.OperationLimits

/**
 * Contract entrypoint handler.
 * Its main purpose is to create parametrized calls to the contract entrypoint specified by the [name].
 *
 * @property name The entrypoint's name.
 */
public class ContractEntrypoint internal constructor(
    public val name: String,
    private val contractAddress: ContractHash,
    private val block: Block,
    private val operationFeeEstimator: FeeEstimator<Operation>,
    internal val michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
    private val meta: LazyMetaContractEntrypoint,
) {

    /**
     * Creates an unsigned contract entrypoint call from the [source] address.
     * Configures the resulting [Operation.Unsigned] with [parameters] and optional [branch], [fee], [counter],
     * [operation limits][limits], [amount], [HTTP headers][headers].
     */
    public suspend fun call(
        parameters: MichelineNode,
        source: ImplicitAddress,
        branch: BlockHash? = null,
        fee: Mutez? = null,
        counter: String? = null,
        limits: OperationLimits? = null,
        amount: Mutez? = null,
        headers: List<HttpHeader> = emptyList(),
    ): Operation.Unsigned {
        val branch = branch ?: block.header.get(headers).header.hash
        val counter = counter ?: block.context.contracts(source).counter.get(headers).counter

        val operation = Operation.Unsigned(
            branch,
            listOf(
                OperationContent.Transaction(
                    source = source,
                    fee = fee ?: Mutez(0),
                    counter = counter?.asTezosNatural() ?: TezosNatural(0U),
                    gasLimit = limits?.gas?.asTezosNatural() ?: TezosNatural(0U),
                    storageLimit = limits?.gas?.asTezosNatural() ?: TezosNatural(0U),
                    amount = amount ?: Mutez(0),
                    destination = contractAddress,
                    parameters = Parameters(
                        Entrypoint(name),
                        parameters,
                    ),
                ),
            ),
        )

        return if (fee != null && limits != null) operation
        else operation.withMinFee(operationFeeEstimator, headers)
    }

    /**
     * Creates an unsigned contract entrypoint call from the [source] address.
     * Configures the resulting [Operation.Unsigned] with [parameters] and optional [branch], [fee], [counter],
     * [operation limits][limits], [amount], [HTTP headers][headers].
     */
    public suspend fun call(
        parameters: ContractEntrypointParameter,
        source: ImplicitAddress,
        branch: BlockHash? = null,
        fee: Mutez? = null,
        counter: String? = null,
        limits: OperationLimits? = null,
        amount: Mutez? = null,
        headers: List<HttpHeader> = emptyList(),
    ): Operation.Unsigned {
        val meta = meta.get(headers)
        val value = meta.valueFrom(parameters)

        return call(value, source, branch, fee, counter, limits, amount, headers)
    }

    internal class Factory(
        private val meta: MetaContractEntrypoint.Factory,
        private val contractAddress: ContractHash,
        private val block: Block,
        private val contract: Block.Context.Contracts.Contract,
        private val operationFeeEstimator: FeeEstimator<Operation>,
        private val michelineNormalizer: Normalizer<MichelineNode>,
        private val michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
    ) {

        private val contractEntrypointsCached: Cached<Map<String, MetaContractEntrypoint>> = Cached { headers -> contract.entrypoints.get(headers).toMetaContractEntrypoint() }

        fun create(code: LazyContractCode, name: String): ContractEntrypoint {
            val meta = contractEntrypointsCached.combine(code).map { (entrypoints, code) -> entrypoints[name] ?: code.findEntrypoint(name) ?: failWithUnknownEntrypoint(name) }
            return ContractEntrypoint(name, contractAddress, block, operationFeeEstimator, michelsonToMichelineConverter, meta)
        }

        private fun GetContractEntrypointsResponse.toMetaContractEntrypoint(): Map<String, MetaContractEntrypoint> =
            entrypoints.mapValues { meta.create(it.value.normalized(michelineNormalizer)) }

        private fun ContractCode.findEntrypoint(name: String): MetaContractEntrypoint? {
            if (name != Entrypoint.Default.value) return null
            if (!parameter.isPrim(MichelsonType.Parameter) || parameter.args.size != 1) failWithUnknownCodeType()

            return meta.create(parameter.args.first())
        }

        private fun failWithUnknownCodeType(): Nothing = failWithContractException("Unknown contract code type.")
        private fun failWithUnknownEntrypoint(name: String): Nothing = failWithContractException("Entrypoint $name could not be found.")
    }
}

/**
 * Types of contract entrypoint parameters that can be included in a contract call.
 */
public sealed interface ContractEntrypointParameter {

    /**
     * The name of the parameter. It should match the annotation specified in the entrypoint definition.
     * For example, to provide a value for such an entrypoint:
     * ```json
     * {
     *      "entrypoint": {
     *          "prim": "pair",
     *          "args": [
     *              {
     *                  "prim": "nat",
     *                  "annots": [
     *                      "%current_version
     *                  ]
     *              },
     *              {
     *                  "prim": "nat",
     *                  "annots": [
     *                      "%new_version
     *                  ]
     *              }
     *         ]
     *      }
     * }
     * ```
     * the following [ContractEntrypointParameter] should be created:
     * ```kotlin
     * val entrypointParameter = entrypointParameters {
     *      value("%current_version") { int(1) },
     *      value("%new_version") { int(2) },
     * }
     * ```
     *
     * The name is optional. If omitted, the parameters will be placed in order.
     * Mixing of unnamed and named parameters in the same scope is not yet supported.
     * All parameters in the same scope should either have a name set or have no name at all.
     */
    public val name: String?

    /**
     * Property that represents a [MichelineNode] value that should be placed in the specified
     * spot (determined either by [name] or by order) while constructing the final Micheline parameter value.
     *
     * Example:
     * Given such an entrypoint:
     * ```json
     * {
     *      "entrypoint": {
     *          {
     *              "prim": "nat",
     *              "annots": [
     *                  "%current_version
     *              ]
     *          }
     *      }
     * }
     * ```
     * and [ContractEntrypointParameter]:
     * ```kotlin
     * val entrypointParameter = entrypointParameters {
     *      value("%current_version") { int(1) },
     * }
     * ```
     * the following Micheline value will be constructed:
     * ```json
     * {
     *      "int": "1"
     * }
     * ```
     *
     * The name is optional. If omitted, the parameter will be placed by its order.
     * Mixing of unnamed and named parameters in the same scope is not yet supported.
     * All parameters in the same scope should either have a name set or have no name at all.
     *
     * @property value The Micheline value.
     * @property name The name of the parameter. It should match the annotation specified in the entrypoint definition.
     */
    public class Value(public val value: MichelineNode?, override val name: String? = null) : ContractEntrypointParameter {
        public companion object {}
    }

    /**
     * Group of properties aggregated in the form of an object. The properties will be placed in the specified
     * spots (determined either by [name] or by order) while constructing the final Micheline parameter value.
     *
     * Example:
     * Given such an entrypoint:
     * ```json
     * {
     *      "entrypoint": {
     *          "prim": "pair",
     *          "args": [
     *              {
     *                  "prim": "nat",
     *                  "annots": [
     *                      "%value
     *                  ]
     *              },
     *              {
     *                  "prim": "pair",
     *                  "args": [
     *                      {
     *                          "prim": "nat"
     *                      },
     *                      {
     *                          "prim": "nat"
     *                      }
     *                  ],
     *                  "annots": [
     *                      "%data"
     *                  ]
     *              }
     *          ]
     *      }
     * }
     * ```
     * and [ContractEntrypointParameter]:
     * ```kotlin
     * val entrypointParameter = entrypointParameters {
     *      value("%value") { int(1) },
     *      `object`("%data") {
     *          value { int(2) }
     *          value { int(3) }
     *      },
     * }
     * ```
     * the following Micheline value will be constructed:
     * ```json
     * {
     *      "prim": "Pair",
     *      "args": [
     *          {
     *              "int": "1"
     *          },
     *          {
     *              "prim": "Pair",
     *              "args": [
     *                  {
     *                      "int": "2",
     *                      "int": "3"
     *                  }
     *              ]
     *          }
     *      ]
     * }
     * ```
     *
     * The name is optional. If omitted, the parameter will be placed by its order.
     * Mixing of unnamed and named parameters in the same scope is not yet supported.
     * All parameters in the same scope should either have a name set or have no name at all.
     *
     * @property name The name of the parameter. It should match the annotation specified in the entrypoint definition.
     */
    public class Object(internal val elements: MutableList<ContractEntrypointParameter>, override val name: String? = null) : ContractEntrypointParameter {

        /**
         * Creates
         */
        public constructor(vararg elements: ContractEntrypointParameter, name: String? = null) : this(elements.toMutableList(), name)

        internal val fields: Set<String> = elements.mapNotNull { it.name }.toSet()

        public companion object {}
    }

    /**
     * Group of properties aggregated in the form of a list. The properties will be placed in the specified
     * spots (determined either by [name] or by order) while constructing the final Micheline parameter value.
     *
     * Example:
     * Given such an entrypoint:
     * ```json
     * {
     *      "entrypoint": {
     *          "prim": "list",
     *          "args": [
     *              {
     *                  "prim": "pair",
     *                  "args": [
     *                      {
     *                          "prim": "nat",
     *                          "annots": [
     *                              "%first"
     *                          }
     *                      },
     *                      {
     *                          "prim": "nat",
     *                          "annots": [
     *                              "%second"
     *                          }
     *                      }
     *                  ]
     *              }
     *          ]
     *      }
     * }
     * ```
     * and [ContractEntrypointParameter]:
     * ```kotlin
     * val entrypointParameter = entrypointParameters {
     *      sequence {
     *          `object` {
     *              value("%first") { int(1) }
     *              value("%second") { int(2) }
     *          }
     *          `object` {
     *              value("%first") { int(3) }
     *              value("%second") { int(4) }
     *          }
     *      }
     * }
     * ```
     * the following Micheline value will be constructed:
     * ```json
     * [
     *      {
     *          "prim": "Pair",
     *          "args": [
     *              {
     *                  "int": "1"
     *              },
     *              {
     *                  "int": "2"
     *              }
     *          ]
     *      },
     *      {
     *          "prim": "Pair",
     *          "args": [
     *              {
     *                  "int": "3"
     *              },
     *              {
     *                  "int": "4"
     *              }
     *          ]
     *      }
     * ]
     * ```
     *
     * The name is optional. If omitted, the parameter will be placed by its order.
     * Mixing of unnamed and named parameters in the same scope is not yet supported.
     * All parameters in the same scope should either have a name set or have no name at all.
     *
     * @property name The name of the parameter. It should match the annotation specified in the entrypoint definition.
     */
    public class Sequence internal constructor(internal val elements: List<ContractEntrypointParameter>, override val name: String? = null) : ContractEntrypointParameter {

        /**
         * Constructs [ContractEntrypointParameter.Sequence] from [a sequence of parameters][elements] and optional [name].
         */
        public constructor(vararg elements: ContractEntrypointParameter, name: String? = null) : this(elements.toList(), name)

        public companion object {}
    }

    /**
     * Group of properties aggregated in the form of a map. The properties will be placed in the specified
     * spots (determined either by [name] or by order) while constructing the final Micheline parameter value.
     *
     * Example:
     * Given such an entrypoint:
     * ```json
     * {
     *      "entrypoint": {
     *          "prim": "map",
     *          "args": [
     *              {
     *                  "prim": "string"
     *              },
     *              {
     *                  "prim": "nat"
     *              }
     *         ]
     *      }
     * }
     * ```
     * and [ContractEntrypointParameter]:
     * ```kotlin
     * val entrypointParameter = entrypointParameters {
     *      map {
     *          key { string("first") } pointsTo value { int(1) }
     *          key { string("second") } pointsTo value { int(2) }
     *      }
     * }
     * ```
     * the following Micheline value will be constructed:
     * ```json
     * [
     *      {
     *          "prim": "Elt",
     *          "args": [
     *              {
     *                  "string": "first"
     *              },
     *              {
     *                  "int": "1"
     *              }
     *          ]
     *      },
     *      {
     *          "prim": "Elt",
     *          "args": [
     *              {
     *                  "string": "second"
     *              },
     *              {
     *                  "int": "2"
     *              }
     *          ]
     *      }
     * ]
     * ```
     *
     * The name is optional. If omitted, the parameter will be placed by its order.
     * Mixing of unnamed and named parameters in the same scope is not yet supported.
     * All parameters in the same scope should either have a name set or have no name at all.
     *
     * @property name The name of the parameter. It should match the annotation specified in the entrypoint definition.
     */
    public class Map internal constructor(internal val map: kotlin.collections.Map<ContractEntrypointParameter, ContractEntrypointParameter>, override val name: String? = null) : ContractEntrypointParameter {

        /**
         * Constructs [ContractEntrypointParameter.Map] from [a sequence of key-value pairs][elements] and optional [name].
         */
        public constructor(vararg elements: Pair<ContractEntrypointParameter, ContractEntrypointParameter>, name: String? = null) : this(elements.toMap().toMutableMap(), name)

        public companion object {}
    }

    public companion object {}
}
