package ua.blackwind.limbushelper.domain.filter

import ua.blackwind.limbushelper.data.SinnerRepository
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.EgoSinResistType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.sinner.model.Ego
import javax.inject.Inject

class GetFilteredEgoUseCase @Inject constructor(
    private val repository: SinnerRepository
) {
    suspend operator fun invoke(filter: EgoFilter): List<Ego> {
        return repository.getAllEgo()
    }
}

data class EgoFilter(
    val skillFilterArg: FilterSkillArg,
    val resistSetArg: EgoFilterSinResistTypeArg,
    val priceSetArg: EgoFilterPriceSetArg,
    val effects: List<Effect>,
    val sinners: List<Int>
)

data class EgoFilterSinResistTypeArg(
    val resistList: List<Pair<EgoSinResistType, Sin>>
)

data class EgoFilterPriceSetArg(
    val priceList: List<Sin>
)