package ua.blackwind.limbushelper.domain.party.usecase

import ua.blackwind.limbushelper.domain.party.IPartyRepository
import javax.inject.Inject

class GetPartyUseCase @Inject constructor(private val repository: IPartyRepository) {
    operator fun invoke() = repository.getParty(1)
}