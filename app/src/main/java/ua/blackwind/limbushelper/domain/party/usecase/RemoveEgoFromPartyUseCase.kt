package ua.blackwind.limbushelper.domain.party.usecase

import ua.blackwind.limbushelper.data.party.PartyRepository
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.sinner.model.Ego
import javax.inject.Inject

class RemoveEgoFromPartyUseCase @Inject constructor(private val repository: PartyRepository) {
    suspend operator fun invoke(ego: Ego, party: Party) {
        repository.removeEgoFromParty(party.id, ego)
    }
}