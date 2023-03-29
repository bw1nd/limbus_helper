package ua.blackwind.limbushelper.ui.screens.filter_screen.state

import androidx.datastore.core.CorruptionException
import ua.blackwind.limbus_helper.FilterSettings

import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.ui.util.StateType

object FilterSheetSettingsMapper {
    fun mapFilterSheetDataStoreSettingsToState(
        settings: FilterSettings.FilterDrawerSheetSettings
    ): FilterDrawerSheetState {

        return FilterDrawerSheetState(
            when (settings.filterSheetMode) {
                FilterSettings.FilterDrawerSheetSettings.FilterSheetMode.TYPE -> FilterSheetMode.Type
                FilterSettings.FilterDrawerSheetSettings.FilterSheetMode.EFFECTS -> FilterSheetMode.Effects
                FilterSettings.FilterDrawerSheetSettings.FilterSheetMode.UNRECOGNIZED -> throw CorruptionException(
                    "Filter drawer sheet settings corrupted with value ${settings.filterSheetMode}"
                )
            },
            FilterSkillBlockState(
                damage = FilterDamageStateBundle(
                    first = mapToDamageType(settings.skillState.damageBundle.first),
                    second = mapToDamageType(settings.skillState.damageBundle.second),
                    third = mapToDamageType(settings.skillState.damageBundle.third)
                ),
                sin = FilterSinStateBundle(
                    first = mapToSinType(settings.skillState.sinBundle.first),
                    second = mapToSinType(settings.skillState.sinBundle.second),
                    third = mapToSinType(settings.skillState.sinBundle.third)
                )
            ),
            resistState = FilterDamageStateBundle(
                first = mapToDamageType(settings.resistState.first),
                second = mapToDamageType(settings.resistState.second),
                third = mapToDamageType(settings.resistState.third)
            ),
            FilterEffectBlockState(
                emptyMap()
            )
        )
    }

    private fun mapToDamageType(input: String): StateType<DamageType> {
        if (input.isBlank()) return StateType.Empty
        try {
            return when (input) {
                EMPTY_STATE_VALUE -> StateType.Empty
                else -> StateType.Value(DamageType.valueOf(input))
            }
        } catch (e: java.lang.IllegalArgumentException) {
            throw CorruptionException("Filter Damage data type corrupted with value: $input")
        }
    }

    private fun mapToSinType(input: String): StateType<Sin> {
        if (input.isBlank()) return StateType.Empty
        return when (input) {
            EMPTY_STATE_VALUE -> StateType.Empty
            else -> StateType.Value(Sin.valueOf(input))
        }
    }

    fun mapStateToSettings(
        state: FilterDrawerSheetState,
        old: FilterSettings.FilterDrawerSheetSettings
    ): FilterSettings.FilterDrawerSheetSettings {
        return old.toBuilder()
            .setFilterSheetMode(
                when (state.filterSheetMode) {
                    FilterSheetMode.Effects -> FilterSettings.FilterDrawerSheetSettings.FilterSheetMode.EFFECTS
                    FilterSheetMode.Type -> FilterSettings.FilterDrawerSheetSettings.FilterSheetMode.TYPE
                }
            )
            .setSkillState(
                FilterSettings.FilterDrawerSheetSettings.FilterSkillBlockState.newBuilder()
                    .setDamageBundle(
                        FilterSettings.FilterDrawerSheetSettings.FilterDamageStateBundle.newBuilder()
                            .setFirst(damageSkillStateToSettings(state.skillState.damage.first))
                            .setSecond(damageSkillStateToSettings(state.skillState.damage.second))
                            .setThird(damageSkillStateToSettings(state.skillState.damage.third))
                    )
                    .setSinBundle(
                        FilterSettings.FilterDrawerSheetSettings.FilterSinStateBundle.newBuilder()
                            .setFirst(sinSkillStateToSettings(state.skillState.sin.first))
                            .setSecond(sinSkillStateToSettings(state.skillState.sin.second))
                            .setThird(sinSkillStateToSettings(state.skillState.sin.third))
                    )
            )
            .setResistState(
                FilterSettings.FilterDrawerSheetSettings.FilterDamageStateBundle.newBuilder()
                    .setFirst(damageSkillStateToSettings(state.resistState.first))
                    .setSecond(damageSkillStateToSettings(state.resistState.second))
                    .setThird(damageSkillStateToSettings(state.resistState.third))
            )
            .build()
    }

    private fun damageSkillStateToSettings(stateType: StateType<DamageType>): String {
        if (stateType is StateType.Empty) return EMPTY_STATE_VALUE
        return (stateType as StateType.Value<DamageType>).value.toString()
    }

    private fun sinSkillStateToSettings(stateType: StateType<Sin>): String {
        if (stateType is StateType.Empty) return EMPTY_STATE_VALUE
        return (stateType as StateType.Value<Sin>).value.toString()
    }

    private const val EMPTY_STATE_VALUE = "Empty"
}