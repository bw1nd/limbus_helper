package ua.blackwind.limbushelper.domain.party.usecase

import ua.blackwind.limbushelper.data.party.PartyRepository
import javax.inject.Inject

class ClearPartyByIdUseCase @Inject constructor(private val repository: PartyRepository) {
    suspend operator fun invoke(partyId: Int) {
        (1..12).forEach { sinnerId ->
            repository.changeSinnerActiveIdentityForParty(partyId, sinnerId, 0)
        }
        repository.removeAllIdentityFromParty(partyId)
        repository.removeAllEgoFromParty(partyId)
    }
}