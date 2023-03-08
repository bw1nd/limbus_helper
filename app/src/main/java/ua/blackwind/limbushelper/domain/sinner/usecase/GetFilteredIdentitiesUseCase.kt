package ua.blackwind.limbushelper.domain.sinner.usecase

import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Effect
import ua.blackwind.limbushelper.domain.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.domain.sinner.ISinnerRepository
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Skill
import ua.blackwind.limbushelper.domain.sinner.model.skillList
import javax.inject.Inject

class GetFilteredIdentitiesUseCase @Inject constructor(private val repository: ISinnerRepository) {
    suspend operator fun invoke(filter: IdentityFilter): List<Identity> {
        var identities = repository.getAllIdentities()
        if (!filter.resist.isEmpty()) {
            identities = filterByResistance(identities, filter.resist)
        }
        if (identities.isNotEmpty() && !filter.skills.damageTypeIsEmpty()) {
            identities = filterBySkill(identities, filter.skills)
        }
        return identities
    }

    private fun filterBySkill(
        identities: List<Identity>,
        filterSkillsSetArg: FilterSkillsSetArg
    ): List<Identity> {
        return identities.filter { identity ->
            val skillList = identity.skillList()
            val first = skillList
                .any { skill -> checkIdentitySkill(skill, filterSkillsSetArg.first) }
            val second = skillList
                .any { skill -> checkIdentitySkill(skill, filterSkillsSetArg.second) }
            val third = skillList
                .any { skill -> checkIdentitySkill(skill, filterSkillsSetArg.third) }
            first && second && third
        }
    }

    private fun checkIdentitySkill(skill: Skill, filterArg: FilterSkillArg): Boolean {
        if (filterArg.damageType is FilterDamageTypeArg.Empty
            && filterArg.sin is FilterSinTypeArg.Empty
        ) return true
        if (filterArg.damageType
                    is FilterDamageTypeArg.Empty
        ) return skill.sin == (filterArg.sin as FilterSinTypeArg.Type).type
        if (filterArg.sin
                    is FilterSinTypeArg.Empty
        ) return skill.dmgType == (filterArg.damageType as FilterDamageTypeArg.Type).type
        return skill.dmgType == (filterArg.damageType as FilterDamageTypeArg.Type).type &&
                skill.sin == (filterArg.sin as FilterSinTypeArg.Type).type
    }

    private fun filterByResistance(
        identities: List<Identity>,
        filter: FilterResistSetArg
    ): List<Identity> {
        return identities.filter { identity ->
            val ineffective = checkIdentityResistance(
                identity,
                IdentityDamageResistType.INEFFECTIVE,
                filter.ineffective
            )
            val normal = checkIdentityResistance(
                identity,
                IdentityDamageResistType.NORMAL,
                filter.normal
            )
            val fatal = checkIdentityResistance(
                identity,
                IdentityDamageResistType.FATAL,
                filter.fatal
            )
            ineffective && normal && fatal
        }
    }

    private fun checkIdentityResistance(
        identity: Identity,
        resistanceType: IdentityDamageResistType,
        damageType: FilterDamageTypeArg
    ): Boolean {
        if (damageType is FilterDamageTypeArg.Empty) return true
        return when ((damageType as FilterDamageTypeArg.Type).type) {
            DamageType.SLASH -> identity.slashRes == resistanceType
            DamageType.PIERCE -> identity.pierceRes == resistanceType
            DamageType.BLUNT -> identity.bluntRes == resistanceType
        }
    }
}

data class IdentityFilter(
    val resist: FilterResistSetArg,
    val skills: FilterSkillsSetArg,
    val effects: List<Effect>
)

data class FilterResistSetArg(
    val ineffective: FilterDamageTypeArg,
    val normal: FilterDamageTypeArg,
    val fatal: FilterDamageTypeArg
)

fun FilterResistSetArg.isEmpty() =
    this.ineffective == FilterDamageTypeArg.Empty
            && this.normal == FilterDamageTypeArg.Empty
            && this.fatal == FilterDamageTypeArg.Empty

data class FilterSkillsSetArg(
    val first: FilterSkillArg,
    val second: FilterSkillArg,
    val third: FilterSkillArg
)

fun FilterSkillsSetArg.damageTypeIsEmpty() =
    this.first.damageType == FilterDamageTypeArg.Empty
            && this.second.damageType == FilterDamageTypeArg.Empty
            && this.third.damageType == FilterDamageTypeArg.Empty

data class FilterSkillArg(
    val damageType: FilterDamageTypeArg,
    val sin: FilterSinTypeArg
)

sealed class FilterDamageTypeArg {
    object Empty: FilterDamageTypeArg()
    data class Type(
        val type: DamageType
    ): FilterDamageTypeArg()
}

sealed class FilterSinTypeArg {
    object Empty: FilterSinTypeArg()
    data class Type(
        val type: Sin
    ): FilterSinTypeArg()
}