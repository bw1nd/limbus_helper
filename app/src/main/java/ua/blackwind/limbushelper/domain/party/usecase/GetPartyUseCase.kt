package ua.blackwind.limbushelper.domain.party.usecase

import ua.blackwind.limbushelper.domain.party.IPartyRepository
import javax.inject.Inject

class GetPartyUseCase @Inject constructor(private val repository: IPartyRepository) {
    operator fun invoke() = repository.getParty(DEFAULT_PARTY_ID)

    companion object {
        private const val DEFAULT_PARTY_ID = 1
    }
}