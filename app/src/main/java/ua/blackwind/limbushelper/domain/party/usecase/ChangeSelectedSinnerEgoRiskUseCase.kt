package ua.blackwind.limbushelper.domain.party.usecase

import ua.blackwind.limbushelper.data.party.PartyRepository
import ua.blackwind.limbushelper.domain.common.RiskLevel
import javax.inject.Inject

class ChangeSelectedSinnerEgoRiskUseCase @Inject constructor(private val repository: PartyRepository) {
    suspend operator fun invoke(partyId: Int, sinnerId: Int, risk: RiskLevel?) {
        repository.changeSinnerSelectedEgoRisk(
            partyId = partyId, sinnerId = sinnerId, riskLevel = risk
        )
    }
}