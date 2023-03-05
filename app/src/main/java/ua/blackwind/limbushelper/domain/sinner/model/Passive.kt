package ua.blackwind.limbushelper.domain.sinner.model

import ua.blackwind.limbushelper.domain.Sin

data class Passive(
    val id: Int,
    val identityId: Int,
    val cost: SinCost,
    val text: String
)