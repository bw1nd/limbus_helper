package ua.blackwind.limbushelper.data.datastore

import androidx.datastore.core.CorruptionException
import ua.blackwind.limbus_helper.EgoFilterSettings.EgoSettings
import ua.blackwind.limbushelper.domain.common.*
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterSinnerModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.*
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
            EgoFilterPriceState(
                mapToSinType(settings.priceState.first),
                mapToSinType(settings.priceState.second),
                mapToSinType(settings.priceState.third)
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
                    .setDamageType(damageStateToSettings(state.skillState.damageType))
                    .setSinType(sinStateToSettings(state.skillState.sinType))
            ).setResistState(
                EgoSettings.FilterResistStateBundle.newBuilder()
                    .setFirst(
                        EgoSettings.FilterResistStateArg.newBuilder()
                            .setSin(sinStateToSettings(state.resistState.first.sin))
                            .setResist(resistStateTypeToSettings(state.resistState.first.resist))
                    )
                    .setSecond(
                        EgoSettings.FilterResistStateArg.newBuilder()
                            .setSin(sinStateToSettings(state.resistState.second.sin))
                            .setResist(resistStateTypeToSettings(state.resistState.second.resist))
                    )
                    .setThird(
                        EgoSettings.FilterResistStateArg.newBuilder()
                            .setSin(sinStateToSettings(state.resistState.third.sin))
                            .setResist(resistStateTypeToSettings(state.resistState.third.resist))
                    )
                    .setFourth(
                        EgoSettings.FilterResistStateArg.newBuilder()
                            .setSin(sinStateToSettings(state.resistState.fourth.sin))
                            .setResist(resistStateTypeToSettings(state.resistState.fourth.resist))
                    )
            )
            .setPriceState(
                EgoSettings.EgoFilterPriceState.newBuilder()
                    .setFirst(sinStateToSettings(state.priceState.first))
                    .setSecond(sinStateToSettings(state.priceState.second))
                    .setThird(sinStateToSettings(state.priceState.third))
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

    private fun mapToEgoResistType(input: String): EgoSinResistType {
        if (input.isBlank()) return EgoSinResistType.NORMAL
        try {
            return EgoSinResistType.valueOf(input)
        } catch (e: IllegalArgumentException) {
            throw CorruptionException("Ego filter resist data type corrupted with value: $input")
        }
    }

    private fun mapToDamageType(input: String): TypeHolder<DamageType> {
        if (input.isBlank()) return TypeHolder.Empty
        try {
            return when (input) {
                EMPTY_STATE_VALUE -> TypeHolder.Empty
                else -> TypeHolder.Value(DamageType.valueOf(input))
            }
        } catch (e: IllegalArgumentException) {
            throw CorruptionException("Ego filter Damage data type corrupted with value: $input")
        }
    }

    private fun mapToSinType(input: String): TypeHolder<Sin> {
        if (input.isBlank()) return TypeHolder.Empty
        try {
            return when (input) {
                EMPTY_STATE_VALUE -> TypeHolder.Empty
                else -> TypeHolder.Value(Sin.valueOf(input))
            }
        } catch (e: IllegalArgumentException) {
            throw CorruptionException("Ego filter Sin data type corrupted with value: $input")
        }
    }

    private fun resistStateTypeToSettings(stateType: EgoSinResistType): String {
        return stateType.name
    }

    private fun damageStateToSettings(typeHolder: TypeHolder<DamageType>): String {
        if (typeHolder is TypeHolder.Empty) return EMPTY_STATE_VALUE
        return (typeHolder as TypeHolder.Value<DamageType>).value.toString()
    }

    private fun sinStateToSettings(typeHolder: TypeHolder<Sin>): String {
        if (typeHolder is TypeHolder.Empty) return EMPTY_STATE_VALUE
        return (typeHolder as TypeHolder.Value<Sin>).value.toString()
    }

    companion object {
        private const val EMPTY_STATE_VALUE = "Empty"
    }
}