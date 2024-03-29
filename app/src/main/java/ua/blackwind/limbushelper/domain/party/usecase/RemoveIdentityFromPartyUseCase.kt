package ua.blackwind.limbushelper.domain.party.usecase

import ua.blackwind.limbushelper.domain.party.IPartyRepository
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import javax.inject.Inject

class RemoveIdentityFromPartyUseCase @Inject constructor(
    private val repository: IPartyRepository
) {
    suspend operator fun invoke(identity: Identity, party: Party) {
        repository.deleteIdentityFromParty(party.id, identity)
    }
}