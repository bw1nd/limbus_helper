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
