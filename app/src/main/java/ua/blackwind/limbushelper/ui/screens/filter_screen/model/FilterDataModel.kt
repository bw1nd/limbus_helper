package ua.blackwind.limbushelper.ui.screens.filter_screen.model

import ua.blackwind.limbushelper.domain.sinner.model.Ego
import ua.blackwind.limbushelper.domain.sinner.model.Identity


data class FilterDataModel(
    val item: FilterItemTypeModel,
    val inParty: Boolean
)

sealed class FilterItemTypeModel {
    data class IdentityType(val identity: Identity): FilterItemTypeModel()
    data class EgoType(val ego: Ego): FilterItemTypeModel()
}
