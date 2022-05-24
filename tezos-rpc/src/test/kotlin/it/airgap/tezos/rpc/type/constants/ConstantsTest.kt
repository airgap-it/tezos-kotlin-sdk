package it.airgap.tezos.rpc.type.constants

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import it.airgap.tezos.rpc.internal.rpcModule
import it.airgap.tezos.rpc.internal.utils.encodeToString
import it.airgap.tezos.rpc.type.primitive.RpcRatio
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mockTezos
import normalizeWith
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ConstantsTest {

    private lateinit var json: Json

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        val tezos = mockTezos()
        json = Json(from = tezos.rpcModule.dependencyRegistry.json) {
            prettyPrint = true
        }
    }

    @After
    fun clean() {
        unmockkAll()
    }

    @Test
    fun `should serialize RpcConstants`() {
        objectsWithJson.forEach { (value, expected) ->
            val actual = json.encodeToString(value)
            assertEquals(expected.normalizeWith(json), actual)
        }
    }

    @Test
    fun `should deserialize RpcConstants`() {
        objectsWithJson.forEach { (expected, string) ->
            val actual = json.decodeFromString<RpcConstants>(string)
            assertEquals(expected, actual)
        }
    }

    private val objectsWithJson: List<Pair<RpcConstants, String>>
        get() = listOf(
            RpcConstants.Active(
                proofOfWorkNonceSize = 8U,
                nonceLength = 32U,
                maxAnonOpsPerBlock = 132U,
                maxOperationDataLength = 32768,
                maxProposalsPerDelegate = 20U,
                maxMichelineNodeCount = 50000,
                maxMichelineBytesLimit = 50000,
                maxAllowedGlobalConstantsDepth = 10000,
                cacheLayout = listOf(
                    100000000,
                    240000,
                    2560,
                ),
                michelsonMaximumTypeSize = 2001U,
                preservedCycles = 5U,
                blocksPerCycle = 8192,
                blocksPerCommitment = 64,
                blocksPerStakeSnapshot = 512,
                blocksPerVotingPeriod = 40960,
                hardGasLimitPerOperation = "1040000",
                hardGasLimitPerBlock = "5200000",
                proofOfWorkThreshold = 70368744177663,
                tokensPerRoll = "6000000000",
                seedNonceRevelationTip = "125000",
                originationSize = 257,
                bakingRewardFixedPortion = "10000000",
                bakingRewardBonusPerSlot = "4286",
                endorsingRewardPerSlot = "2857",
                costPerByte = "250",
                hardStorageLimitPerOperation = "60000",
                quorumMin = 2000,
                quorumMax = 7000,
                minProposalQuorum = 500,
                liquidityBakingSubsidy = "2500000",
                liquidityBakingSunsetLevel = 3063809,
                liquidityBakingEscapeEmaThreshold = 666667,
                maxOperationsTimeToLive = 120,
                minimalBlockDelay = 30,
                delayIncrementPerRound = 15,
                consensusCommitteeSize = 7000,
                consensusThreshold = 4667,
                minimalParticipationRatio = RpcRatio(numerator = 2U, denominator = 3U),
                maxSlashingPeriod = 2,
                frozenDepositsPercentage = 10,
                doubleBakingPunishment = "640000000",
                ratioOfFrozenDepositsSlashedPerDoubleEndorsement = RpcRatio(numerator = 1U, denominator = 2U),
            ) to """
                {
                    "proof_of_work_nonce_size": 8,
                    "nonce_length": 32,
                    "max_anon_ops_per_block": 132,
                    "max_operation_data_length": 32768,
                    "max_proposals_per_delegate": 20,
                    "max_micheline_node_count": 50000,
                    "max_micheline_bytes_limit": 50000,
                    "max_allowed_global_constants_depth": 10000,
                    "cache_layout": [
                        "100000000",
                        "240000",
                        "2560"
                    ],
                    "michelson_maximum_type_size": 2001,
                    "preserved_cycles": 5,
                    "blocks_per_cycle": 8192,
                    "blocks_per_commitment": 64,
                    "blocks_per_stake_snapshot": 512,
                    "blocks_per_voting_period": 40960,
                    "hard_gas_limit_per_operation": "1040000",
                    "hard_gas_limit_per_block": "5200000",
                    "proof_of_work_threshold": "70368744177663",
                    "tokens_per_roll": "6000000000",
                    "seed_nonce_revelation_tip": "125000",
                    "origination_size": 257,
                    "baking_reward_fixed_portion": "10000000",
                    "baking_reward_bonus_per_slot": "4286",
                    "endorsing_reward_per_slot": "2857",
                    "cost_per_byte": "250",
                    "hard_storage_limit_per_operation": "60000",
                    "quorum_min": 2000,
                    "quorum_max": 7000,
                    "min_proposal_quorum": 500,
                    "liquidity_baking_subsidy": "2500000",
                    "liquidity_baking_sunset_level": 3063809,
                    "liquidity_baking_escape_ema_threshold": 666667,
                    "max_operations_time_to_live": 120,
                    "minimal_block_delay": "30",
                    "delay_increment_per_round": "15",
                    "consensus_committee_size": 7000,
                    "consensus_threshold": 4667,
                    "minimal_participation_ratio": {
                        "numerator": 2,
                        "denominator": 3
                    },
                    "max_slashing_period": 2,
                    "frozen_deposits_percentage": 10,
                    "double_baking_punishment": "640000000",
                    "ratio_of_frozen_deposits_slashed_per_double_endorsement": {
                        "numerator": 1,
                        "denominator": 2
                    }
                }
            """.trimIndent(),
        )
}