package com.example.limbushelper.domain.party.usecase

import com.example.limbushelper.domain.party.IPartyRepository
import javax.inject.Inject

class GetPartyUseCase @Inject constructor(private val repository: IPartyRepository) {
    operator fun invoke() = repository.getParty()
}