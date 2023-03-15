package ua.blackwind.limbushelper.domain.party.usecase

import ua.blackwind.limbushelper.domain.party.IPartyRepository
import javax.inject.Inject

class AddIdentityToPartyUseCase @Inject constructor(
    private val repository: IPartyRepository
) {
    suspend operator fun invoke(id: Int, isActive: Boolean) {
        repository.addIdentityToParty(
            Pair(id, isActive)
        )
    }
}