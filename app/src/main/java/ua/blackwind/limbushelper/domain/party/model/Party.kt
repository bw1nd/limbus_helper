package ua.blackwind.limbushelper.domain.party.model

import ua.blackwind.limbushelper.domain.sinner.model.Identity


data class Party(
    val id: Int,
    val name: String,
    val identityList: List<Identity>
)
