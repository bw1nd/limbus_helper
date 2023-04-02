package ua.blackwind.limbushelper.domain.sinner.model

data class Passive(
    val id: Int,
    val identityId: Int,
    val cost: SinCost,
    val text: String
)