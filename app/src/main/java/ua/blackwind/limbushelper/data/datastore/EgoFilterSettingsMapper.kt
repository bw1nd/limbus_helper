package ua.blackwind.limbushelper.data.datastore

import androidx.datastore.core.CorruptionException
import ua.blackwind.limbus_helper.EgoFilterSettings.EgoSettings
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.EgoSinResistType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterSinnerModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.*
import ua.blackwind.limbushelper.ui.util.StateType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EgoFilterSettingsMapper @Inject constructor() {
    fun mapFilterSheetDataStoreSettingsToState(settings: EgoSettings): FilterDrawerSheetState.EgoMode =
        FilterDrawerSheetState.EgoMode(
            EgoFilterSkillBlockState(
                mapToDamageType(settings.skillState.damageType),
                mapToSinType(settings.skillState.sinType)
            ),
            EgoFilterResistBlockState(
                EgoFilterResistArg(
                    mapToSinType(settings.resistState.first.sin),
                    mapToEgoResistType(settings.resistState.first.resist)
                ),
                EgoFilterResistArg(
                    mapToSinType(settings.resistState.second.sin),
                    mapToEgoResistType(settings.resistState.second.resist)
                ),
                EgoFilterResistArg(
                    mapToSinType(settings.resistState.third.sin),
                    mapToEgoResistType(settings.resistState.third.resist)
                ),
                EgoFilterResistArg(
                    mapToSinType(settings.resistState.fourth.sin),
                    mapToEgoResistType(settings.resistState.fourth.resist)
                )
            ),
            if (settings.effectsState.effectsCount > 1) {
                FilterEffectBlockState(
                    settings.effectsState.effectsMap.mapKeys { (key, _) -> Effect.valueOf(key) }
                )
            } else {
                emptyFilterEffectBlockState()
            },
            if (settings.sinnersState.sinnersCount > 1) {
                FilterSinnersBlockState(
                    settings.sinnersState.sinnersMap.mapKeys { (key, _) -> FilterSinnerModel(key) }
                )
            } else {
                emptyFilterSinnerBlockState()
            }

        )

    fun mapStateToSettings(
        state: FilterDrawerSheetState.EgoMode,
        old: EgoSettings
    ): EgoSettings {
        return old.toBuilder()
            .setSkillState(
                EgoSettings.FilterSkillState.newBuilder()
                    .setDamageType(damageSkillStateToSettings(state.skillState.damageType))
                    .setSinType(sinSkillStateToSettings(state.skillState.sinType))
                    .build()
            ).setResistState(
                EgoSettings.FilterResistStateBundle.newBuilder()
                    .setFirst(
                        EgoSettings.FilterResistStateArg.newBuilder()
                            .setSin(sinSkillStateToSettings(state.resistState.first.sin))
                            .setResist(resistStateTypeToSettings(state.resistState.first.resist))
                    )
                    .setSecond(
                        EgoSettings.FilterResistStateArg.newBuilder()
                            .setSin(sinSkillStateToSettings(state.resistState.second.sin))
                            .setResist(resistStateTypeToSettings(state.resistState.second.resist))
                    )
                    .setThird(
                        EgoSettings.FilterResistStateArg.newBuilder()
                            .setSin(sinSkillStateToSettings(state.resistState.third.sin))
                            .setResist(resistStateTypeToSettings(state.resistState.third.resist))
                    )
                    .setFourth(
                        EgoSettings.FilterResistStateArg.newBuilder()
                            .setSin(sinSkillStateToSettings(state.resistState.fourth.sin))
                            .setResist(resistStateTypeToSettings(state.resistState.fourth.resist))
                    ).build()
            )
            .setEffectsState(
                EgoSettings.FilterEffectBlockState.newBuilder()
                    .putAllEffects(state.effectsState.effects.mapKeys { (key, _) -> key.toString() }
                        .toMutableMap())
            )
            .setSinnersState(
                EgoSettings.FilterSinnersBlockState.newBuilder()
                    .putAllSinners(state.sinnersState.sinners.mapKeys { (key, _) -> key.id }
                        .toMutableMap())
            )
            .build()


    }

    private fun mapToEgoResistType(input: String): StateType<EgoSinResistType> {
        if (input.isBlank()) return StateType.Empty
        try {
            return when (input) {
                EMPTY_STATE_VALUE -> StateType.Empty
                else -> StateType.Value(EgoSinResistType.valueOf(input))
            }
        } catch (e: IllegalArgumentException) {
            throw CorruptionException("Ego filter resist data type corrupted with value: $input")
        }
    }

    private fun mapToDamageType(input: String): StateType<DamageType> {
        if (input.isBlank()) return StateType.Empty
        try {
            return when (input) {
                EMPTY_STATE_VALUE -> StateType.Empty
                else -> StateType.Value(DamageType.valueOf(input))
            }
        } catch (e: IllegalArgumentException) {
            throw CorruptionException("Ego filter Damage data type corrupted with value: $input")
        }
    }

    private fun mapToSinType(input: String): StateType<Sin> {
        if (input.isBlank()) return StateType.Empty
        try {
            return when (input) {
                EMPTY_STATE_VALUE -> StateType.Empty
                else -> StateType.Value(Sin.valueOf(input))
            }
        } catch (e: IllegalArgumentException) {
            throw CorruptionException("Ego filter Sin data type corrupted with value: $input")
        }
    }

    private fun resistStateTypeToSettings(stateType: StateType<EgoSinResistType>): String {
        if (stateType is StateType.Empty) return EMPTY_STATE_VALUE
        return (stateType as StateType.Value<EgoSinResistType>).value.toString()
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