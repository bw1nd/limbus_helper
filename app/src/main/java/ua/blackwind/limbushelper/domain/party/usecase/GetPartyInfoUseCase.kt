package ua.blackwind.limbushelper.domain.party.usecase

import ua.blackwind.limbushelper.data.SinnerRepository
import ua.blackwind.limbushelper.domain.common.*
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.party.model.PartyIdentity
import ua.blackwind.limbushelper.domain.party.model.PartyInfoData
import ua.blackwind.limbushelper.ui.screens.party_building_screen.model.InfoPanelDamageResist
import javax.inject.Inject

private const val NORMAL_DAMAGE_SCALE_FROM_100 = 100
private const val ENDURE_DAMAGE_SCALE_FROM_100 = 75
private const val INEFF_DAMAGE_SCALE_FROM_100 = 50
private const val FATAL_DAMAGE_SCALE_FROM_100 = 200

private const val LOWER_BOUND = 0
private const val PERFECT_UPPER_BOUND = 60
private const val GOOD_UPPER_BOUND = 90
private const val NORMAL_UPPER_BOUND = 120
private const val POOR_UPPER_BOUND = 160
private const val UPPER_BOUND = 200

//TODO needs unit tests and potency calculation calibration.
class GetPartyInfoUseCase @Inject constructor(
    private val repository: SinnerRepository
) {
    suspend operator fun invoke(party: Party): PartyInfoData {
        val identities = party.sinners.map { it.identities }.flatten()
        val activeList = identities.filter { it.isActive }
        val egoList = formEgoList(activeList, party)

        val attackByDamage = DamageType.values().associateWith { 0 }.toMutableMap()
        val resistByDamage = DamageType.values().associateWith { 0 }.toMutableMap()
        val attackBySin = Sin.values().associateWith { 0 }.toMutableMap()
        val resistBySin = Sin.values().associateWith { 0 }.toMutableMap()

        activeList.forEach { pIdentity ->
            val identity = pIdentity.identity

            listOf(
                identity.firstSkill,
                identity.secondSkill,
                identity.thirdSkill
            ).forEach { skill ->
                attackByDamage[skill.dmgType] =
                    attackByDamage[skill.dmgType]!! + skill.copiesCount
                attackBySin[skill.sin] = attackBySin[skill.sin]!! + skill.copiesCount
            }

            resistByDamage[DamageType.SLASH] =
                resistByDamage[DamageType.SLASH]!! + enumerateDamageResistType(identity.slashRes)
            resistByDamage[DamageType.PIERCE] =
                resistByDamage[DamageType.PIERCE]!! + enumerateDamageResistType(identity.pierceRes)
            resistByDamage[DamageType.BLUNT] =
                resistByDamage[DamageType.BLUNT]!! + enumerateDamageResistType(identity.bluntRes)
        }

        egoList.forEach { ego ->
            Sin.values().forEach { sin ->
                resistBySin[sin] = resistBySin[sin]!! + enumerateSinResistType(
                    ego.sinResistances[sin] ?: EgoSinResistType.NORMAL
                )
            }
        }

        return PartyInfoData(
            attackByDamage.toMap(),
            attackBySin.toMap(),
            resistByDamage.map { (type, value) ->
                type to calculateDamageResistPotency(value, activeList.size)
            }.toMap(),
            resistBySin.map { (sin, value) ->
                sin to calculateDamageResistPotency(value, activeList.size)
            }.toMap(),
            activeList.size,
            identities.size
        )
    }

    private suspend fun formEgoList(
        activeList: List<PartyIdentity>,
        party: Party
    ) = repository.getAllSinners()
        .filter { sinner -> activeList.any { it.identity.sinnerId == sinner.id } }
        .map { sinner ->
            party.sinners.find { it.sinner.id == sinner.id }?.let { partySinner ->
                if (partySinner.selectedRiskLevel != null)
                    partySinner.ego.find { it.risk == partySinner.selectedRiskLevel } else
                    partySinner.ego.find { it.risk == RiskLevel.ZAYIN }
            } ?: repository.getEgoById(sinner.id)
        }

    private fun enumerateDamageResistType(resist: IdentityDamageResistType): Int {
        return when (resist) {
            IdentityDamageResistType.NORMAL -> NORMAL_DAMAGE_SCALE_FROM_100
            IdentityDamageResistType.INEFF -> INEFF_DAMAGE_SCALE_FROM_100
            IdentityDamageResistType.FATAL -> FATAL_DAMAGE_SCALE_FROM_100
        }
    }

    private fun enumerateSinResistType(resist: EgoSinResistType): Int {
        return when (resist) {
            EgoSinResistType.NORMAL -> NORMAL_DAMAGE_SCALE_FROM_100
            EgoSinResistType.INEFF -> INEFF_DAMAGE_SCALE_FROM_100
            EgoSinResistType.ENDURE -> ENDURE_DAMAGE_SCALE_FROM_100
            EgoSinResistType.FATAL -> FATAL_DAMAGE_SCALE_FROM_100
        }
    }

    private fun calculateDamageResistPotency(
        value: Int,
        identityCount: Int
    ): InfoPanelDamageResist {
        if (identityCount == 0) return InfoPanelDamageResist.NA
        return when (value / identityCount) {
            in LOWER_BOUND..PERFECT_UPPER_BOUND -> InfoPanelDamageResist.Perfect
            in PERFECT_UPPER_BOUND until GOOD_UPPER_BOUND -> InfoPanelDamageResist.Good
            in GOOD_UPPER_BOUND until NORMAL_UPPER_BOUND -> InfoPanelDamageResist.Normal
            in NORMAL_UPPER_BOUND until POOR_UPPER_BOUND -> InfoPanelDamageResist.Poor
            in POOR_UPPER_BOUND..UPPER_BOUND -> InfoPanelDamageResist.Bad
            else -> throw java.lang.IllegalArgumentException("Pair value: $value/count: $identityCount outside calculation range")
        }
    }
}