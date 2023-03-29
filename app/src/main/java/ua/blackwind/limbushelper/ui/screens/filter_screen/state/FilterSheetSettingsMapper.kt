package ua.blackwind.limbushelper.ui.screens.filter_screen.state

import androidx.datastore.core.CorruptionException
import ua.blackwind.limbus_helper.FilterSettings

import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.ui.util.StateType

class FilterSheetSettingsMapper {
    fun mapFilterSheetDataStoreSettingsToState(
        settings: FilterSettings.FilterDrawerSheetSettings
    ): FilterDrawerSheetState {
        return FilterDrawerSheetState(
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
            if (settings.effectsState.effectsCount > 1) {
                FilterEffectBlockState(
                    settings.effectsState.effectsMap.mapKeys { (key, _) -> Effect.valueOf(key) }
                )
            } else {
                emptyFilterEffectBlockState()
            }
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
            .setEffectsState(
                FilterSettings.FilterDrawerSheetSettings.FilterEffectBlockState.newBuilder()
                    .putAllEffects(
                        state.effectsState.effects.mapKeys { (key, _) -> key.toString() }
                            .toMutableMap()
                    )
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

    companion object {
        private const val EMPTY_STATE_VALUE = "Empty"
    }
}