package ua.blackwind.limbushelper.domain.party.usecase

import ua.blackwind.limbushelper.domain.party.IPartyRepository
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import javax.inject.Inject

class AddIdentityToPartyUseCase @Inject constructor(
    private val repository: IPartyRepository
) {
    suspend operator fun invoke(identity: Identity, isActive: Boolean) {
        repository.addIdentityToParty(
            Pair(identity.id, isActive)
        )
    }
}