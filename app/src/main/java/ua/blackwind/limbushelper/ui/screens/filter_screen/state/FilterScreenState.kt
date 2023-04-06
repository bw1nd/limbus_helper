package ua.blackwind.limbushelper.ui.screens.filter_screen.state

const val IDENTITY_MODE_INDEX = 0
const val EGO_MODE_INDEX = 1

const val IDENTITY_MODE_LABEL = "Identity"
const val EGO_MODE_LABEL = "Ego"

sealed class FilterMode(val index: Int, val label: String) {
    object Identity: FilterMode(IDENTITY_MODE_INDEX, IDENTITY_MODE_LABEL)
    object Ego: FilterMode(EGO_MODE_INDEX, EGO_MODE_LABEL)
}