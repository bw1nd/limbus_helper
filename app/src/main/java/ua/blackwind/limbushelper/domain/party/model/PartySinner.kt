package ua.blackwind.limbushelper.domain.party.model

import ua.blackwind.limbushelper.domain.common.RiskLevel
import ua.blackwind.limbushelper.domain.sinner.model.Ego
import ua.blackwind.limbushelper.domain.sinner.model.Sinner

data class PartySinner(
    val sinner: Sinner,
    val identities: List<PartyIdentity>,
    val ego: List<Ego>,
    val selectedRiskLevel: RiskLevel?
)
