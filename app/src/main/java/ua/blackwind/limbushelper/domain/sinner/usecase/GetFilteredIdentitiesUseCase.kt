package ua.blackwind.limbushelper.domain.sinner.usecase

import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Effect
import ua.blackwind.limbushelper.domain.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.domain.sinner.ISinnerRepository
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Skill
import javax.inject.Inject

class GetFilteredIdentitiesUseCase @Inject constructor(private val repository: ISinnerRepository) {
    suspend operator fun invoke(filter: IdentityFilter): List<Identity> {
        var identities = repository.getAllIdentities()
        if (!filter.resist.isEmpty()) {
            identities = filterByResistance(identities, filter.resist)
        }
        if (identities.isNotEmpty() && !filter.skills.skillFilterIsEmpty()) {
            identities = filterBySkill(identities, filter.skills)
        }
        return identities
    }

    private fun filterBySkill(
        identities: List<Identity>,
        filterSkillsSetArg: FilterSkillsSetArg
    ): List<Identity> {
        return identities.filter { identity ->
            identityPassFilter(identity, filterSkillsSetArg)
        }
    }

    private fun identityPassFilter(identity: Identity, filter: FilterSkillsSetArg): Boolean {
        val identitySkills =
            listOf(identity.firstSkill, identity.secondSkill, identity.thirdSkill).toMutableList()
        val filterSkills = filter.toSkillList().toMutableList()
        filterSkills.toList().forEach { skillFilter ->
            if (skillFilter.isStrict()) {
                val correspondingSkill =
                    identitySkills.find {
                        it.dmgType == skillFilter.damageType.toDamageType()
                                && it.sin == skillFilter.sin.toSin()
                    }
                        ?: return false
                filterSkills.remove(skillFilter)
                identitySkills.remove(correspondingSkill)
            }
        }
        val skillsAfterStrictFilter = identitySkills.toList()
        val looseDamageFilter = filterSkills.map { it.damageType.toDamageType() }.toList()
        val looseSinFilter = filterSkills.map { it.sin.toSin() }.toList()
        return checkDamageImprint(
            skillsAfterStrictFilter,
            looseDamageFilter
        ) && checkSinImprint(skillsAfterStrictFilter, looseSinFilter)
    }

    private fun checkDamageImprint(
        skills: List<Skill>,
        filterImprint: List<DamageType?>
    ): Boolean {
        if (filterImprint.isEmpty()) return true
        val skillImprint = skills.map { it.dmgType }.toMutableList()
        filterImprint.forEach { filter ->
            if (filter != null && skillImprint.none { it == filter }) {
                return false
            }
            skillImprint.remove(filter)
        }
        return true
    }

    private fun checkSinImprint(
        sins: List<Skill>,
        filterImprint: List<Sin?>
    ): Boolean {
        if (filterImprint.isEmpty()) return true
        val sinImprint = sins.map { it.sin }.toMutableList()
        filterImprint.forEach { filter ->
            if (filter != null && sinImprint.none { it == filter }) {
                return false
            }
            sinImprint.remove(filter)
        }
        return true
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

fun FilterSkillsSetArg.toSkillList() = listOf(first, second, third)

fun FilterSkillsSetArg.skillFilterIsEmpty() =
    this.first.damageType == FilterDamageTypeArg.Empty
            && first.sin == FilterSinTypeArg.Empty
            && second.damageType == FilterDamageTypeArg.Empty
            && second.sin == FilterSinTypeArg.Empty
            && third.damageType == FilterDamageTypeArg.Empty
            && third.sin == FilterSinTypeArg.Empty

data class FilterSkillArg(
    val damageType: FilterDamageTypeArg,
    val sin: FilterSinTypeArg
)

fun FilterSkillArg.isStrict() = this.damageType !is FilterDamageTypeArg.Empty &&
        this.sin !is FilterSinTypeArg.Empty

sealed class FilterDamageTypeArg {
    object Empty: FilterDamageTypeArg()
    data class Type(
        val type: DamageType
    ): FilterDamageTypeArg()
}

fun FilterDamageTypeArg.toDamageType() = when (this) {
    is FilterDamageTypeArg.Empty -> null
    is FilterDamageTypeArg.Type -> this.type
}

sealed class FilterSinTypeArg {
    object Empty: FilterSinTypeArg()
    data class Type(
        val type: Sin
    ): FilterSinTypeArg()
}

fun FilterSinTypeArg.toSin() = when (this) {
    FilterSinTypeArg.Empty -> null
    is FilterSinTypeArg.Type -> this.type
}