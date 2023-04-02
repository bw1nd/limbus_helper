package ua.blackwind.limbushelper.domain.sinner.model

data class Support(
    val id: Int,
    val identityId: Int,
    val cost: SinCost,
    val text: String
)
