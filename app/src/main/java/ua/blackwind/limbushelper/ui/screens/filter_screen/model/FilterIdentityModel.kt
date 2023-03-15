package ua.blackwind.limbushelper.ui.screens.filter_screen.model

import ua.blackwind.limbushelper.domain.sinner.model.Identity


data class FilterIdentityModel(
    val identity: Identity,
    val inParty: Boolean
)
