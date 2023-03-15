package ua.blackwind.limbushelper.domain.party.usecase

import ua.blackwind.limbushelper.domain.party.IPartyRepository
import ua.blackwind.limbushelper.domain.party.model.Party
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import javax.inject.Inject

class DeleteIdentityFromPartyUseCase @Inject constructor(
    private val repository: IPartyRepository
) {
    suspend operator fun invoke(identity: Identity, party: Party) {
//        val isActive = party.identityList.find { it.first == identity.id }?.second
//        repository.deleteIdentityFromParty(Pair(identity.id, isActive!!))
    }
}