package ua.blackwind.limbushelper.domain.filter

import ua.blackwind.limbushelper.data.SinnerRepository
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.EgoSinResistType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.common.TypeHolder
import ua.blackwind.limbushelper.domain.sinner.model.Ego
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
            val byResist = {
                filter.resistSetArg.isEmpty() || egoPassResistFilter(
                    ego,
                    filter.resistSetArg
                )
            }
            val byPrice = {
                filter.priceSetArg.isEmpty() || egoPassPriceFilter(
                    ego,
                    filter.priceSetArg
                )
            }
            bySinner() && byEffect() && bySkill() && byResist() && byPrice()
        }
    }

    private fun egoPassPriceFilter(ego: Ego, filter: List<Sin>): Boolean =
        filter.all { it in ego.resourceCost.keys }

    private fun egoPassResistFilter(ego: Ego, filter: Map<EgoSinResistType, Sin>): Boolean {
        return filter.all { (resist, sin) ->
            if (resist == EgoSinResistType.NORMAL)
                ego.sinResistances.none { it.key == sin } else
                ego.sinResistances.any { it.value == resist && it.key == sin }
        }
    }

    private fun egoPassSkillFilter(ego: Ego, filter: FilterSkillArg): Boolean {
        val (egoDamage, egoSin) = ego.awakeningSkill.dmgType to ego.awakeningSkill.sin
        val filterDamage =
            if (filter.damageType is TypeHolder.Value) filter.damageType.value else null
        val filterSin = if (filter.sin is TypeHolder.Value) filter.sin.value else null
        if (filter.isStrict()) {
            return egoDamage == filterDamage && egoSin == filterSin
        }
        return egoDamage == filterDamage || egoSin == filterSin
    }

    private fun egoPassSinnerFilter(ego: Ego, filter: List<Int>) =
        filter.any { it == ego.sinnerId }

    private fun egoPassEffectFilter(ego: Ego, filter: List<Effect>) =
        ego.awakeningSkill.effects.containsAll(filter)

}
