package ua.blackwind.limbushelper.domain.filter

import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.IdentityDamageResistType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.sinner.ISinnerRepository
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.domain.sinner.model.Sinner
import ua.blackwind.limbushelper.domain.sinner.model.Skill
import javax.inject.Inject

class GetFilteredIdentitiesUseCase @Inject constructor(private val repository: ISinnerRepository) {
    suspend operator fun invoke(filter: IdentityFilter): List<Identity> {
        val identities = repository.getAllIdentities()
        if (filter.isEmpty()) return identities

        return identities.filter { identity ->
            val bySinner = {
                filter.sinners.isEmpty() || identityPassSinnerFilter(identity, filter.sinners)
            }
            val byResist = {
                filter.resist.isEmpty() || identityPassResistanceFilter(
                    identity,
                    filter.resist
                )
            }
            val bySkill = {
                filter.skills.isEmpty() || identityPassSkillFilter(
                    identity,
                    filter.skills
                )
            }
            val byEffect = {
                filter.effects.isEmpty() || identityPassEffectsFilter(
                    identity,
                    filter.effects
                )
            }
            bySinner() && byResist() && bySkill() && byEffect()
        }
    }

    private fun identityPassSinnerFilter(identity: Identity, filter: List<Sinner>): Boolean {
        return filter.any { it.id == identity.sinnerId }
    }

    private fun identityPassSkillFilter(identity: Identity, filter: FilterSkillsSetArg): Boolean {
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

    /**
     * Performs loose filtering by skill damage
     */
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

    /**
     * Performs loose filtering by skill sin
     */
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

    private fun identityPassResistanceFilter(
        identity: Identity,
        filter: FilterResistSetArg
    ): Boolean {

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
        return ineffective && normal && fatal

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

    private fun identityPassEffectsFilter(identity: Identity, filter: List<Effect>): Boolean {
        return listOf(
            identity.firstSkill.effects,
            identity.secondSkill.effects,
            identity.thirdSkill.effects
        ).flatten().containsAll(filter)
    }
}

data class IdentityFilter(
    val resist: FilterResistSetArg,
    val skills: FilterSkillsSetArg,
    val effects: List<Effect>,
    val sinners: List<Sinner>
)

fun IdentityFilter.isEmpty() =
    resist.isEmpty() && skills.isEmpty() && effects.isEmpty() && sinners.isEmpty()

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

fun FilterSkillsSetArg.isEmpty() =
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