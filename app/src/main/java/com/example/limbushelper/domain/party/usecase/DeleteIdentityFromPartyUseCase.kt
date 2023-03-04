package com.example.limbushelper.domain.party.usecase

import com.example.limbushelper.domain.party.IPartyRepository
import com.example.limbushelper.domain.sinner.model.Identity
import javax.inject.Inject

class DeleteIdentityFromPartyUseCase @Inject constructor(
    private val repository: IPartyRepository
) {
    suspend operator fun invoke(identity: Identity, isActive: Boolean) {
        repository.deleteIdentityFromParty(Pair(identity.id, isActive))
    }
}