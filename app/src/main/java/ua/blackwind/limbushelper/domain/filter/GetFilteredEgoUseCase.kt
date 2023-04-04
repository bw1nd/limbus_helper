package ua.blackwind.limbushelper.domain.filter

import ua.blackwind.limbushelper.data.SinnerRepository
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.EgoSinResistType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.sinner.model.Ego
import ua.blackwind.limbushelper.domain.sinner.model.Sinner
import javax.inject.Inject

class GetFilteredEgoUseCase @Inject constructor(
    private val repository: SinnerRepository
) {
    suspend operator fun invoke(filter: EgoFilter): List<Ego> {
        return repository.getAllEgo().filter { ego ->
            val bySinner = { filter.sinners.isEmpty() || egoPassSinnerFilter(ego, filter.sinners) }
            val byEffect = { filter.effects.isEmpty() || egoPassEffectFilter(ego, filter.effects) }
            val bySkill = {
                filter.skillFilterArg.isEmpty() || egoPassSkillFilter(
                    ego,
                    filter.skillFilterArg
                )
            }
            bySinner() && byEffect() && bySkill()
        }
    }

    private fun egoPassSkillFilter(ego: Ego, filter: FilterSkillArg): Boolean {
        val (egoDamage, egoSin) = ego.awakeningSkill.dmgType to ego.awakeningSkill.sin
        val (filterDamage, filterSin) = filter.damageType.toDamageType() to filter.sin.toSin()
        if (filter.isStrict()) {
            return egoDamage == filterDamage && egoSin == filterSin
        }
        return egoDamage == filterDamage || egoSin == filterSin
    }

    private fun egoPassSinnerFilter(ego: Ego, filter: List<Int>) =
        filter.any { it == ego.sinnerId }

    private fun egoPassEffectFilter(ego: Ego, filter: List<Effect>) =
        filter.any { it in ego.awakeningSkill.effects }

}
