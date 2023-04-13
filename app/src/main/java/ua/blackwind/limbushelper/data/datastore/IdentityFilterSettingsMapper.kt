package ua.blackwind.limbushelper.data.datastore

import androidx.datastore.core.CorruptionException
import ua.blackwind.limbus_helper.IdentityFilterSettings

import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.common.TypeHolder
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterSinnerModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IdentityFilterSettingsMapper @Inject constructor() {
    companion object {
        private const val EMPTY_STATE_VALUE = "Empty"
    }

    fun mapFilterSheetDataStoreSettingsToState(
        settings: IdentityFilterSettings.IdentitySettings
    ): FilterDrawerSheetState.IdentityMode =
        FilterDrawerSheetState.IdentityMode(
            skillState = FilterSkillBlockState(
                damage = FilterDamageStateBundle(
                    first = mapToDamageType(settings.skillState.damageBundle.first),
                    second = mapToDamageType(settings.skillState.damageBundle.second),
                    third = mapToDamageType(settings.skillState.damageBundle.third)
                ),
                sin = FilterSinStateBundle(
                    first = mapToSinType(settings.skillState.sinBundle.first),
                    second = mapToSinType(settings.skillState.sinBundle.second),
                    third = mapToSinType(settings.skillState.sinBundle.third)
                ),
                thirdSkillIsCounter = settings.skillState.thirdIsCounter
            ),
            resistState = FilterDamageStateBundle(
                first = mapToDamageType(settings.resistState.first),
                second = mapToDamageType(settings.resistState.second),
                third = mapToDamageType(settings.resistState.third),
            ),
            effectsState = if (settings.effectsState.effectsCount > 1) {
                FilterEffectBlockState(
                    settings.effectsState.effectsMap.mapKeys { (key, _) -> Effect.valueOf(key) }
                )
            } else {
                emptyFilterEffectBlockState()
            },
            sinnersState = if (settings.sinnersState.sinnersCount > 1) {
                FilterSinnersBlockState(
                    settings.sinnersState.sinnersMap.mapKeys { (key, _) -> FilterSinnerModel(key) }
                )
            } else {
                emptyFilterSinnerBlockState()
            }
        )


    private fun mapToDamageType(input: String): TypeHolder<DamageType> {
        if (input.isBlank()) return TypeHolder.Empty
        try {
            return when (input) {
                EMPTY_STATE_VALUE -> TypeHolder.Empty
                else -> TypeHolder.Value(DamageType.valueOf(input))
            }
        } catch (e: java.lang.IllegalArgumentException) {
            throw CorruptionException("Filter Damage data type corrupted with value: $input")
        }
    }

    private fun mapToSinType(input: String): TypeHolder<Sin> {
        if (input.isBlank()) return TypeHolder.Empty
        return when (input) {
            EMPTY_STATE_VALUE -> TypeHolder.Empty
            else -> TypeHolder.Value(Sin.valueOf(input))
        }
    }

    fun mapStateToSettings(
        state: FilterDrawerSheetState.IdentityMode,
        old: IdentityFilterSettings.IdentitySettings
    ): IdentityFilterSettings.IdentitySettings {
        return old.toBuilder()
            .setSkillState(
                IdentityFilterSettings.IdentitySettings.FilterSkillBlockState.newBuilder()
                    .setDamageBundle(
                        IdentityFilterSettings.IdentitySettings.FilterDamageStateBundle.newBuilder()
                            .setFirst(damageSkillStateToSettings(state.skillState.damage.first))
                            .setSecond(damageSkillStateToSettings(state.skillState.damage.second))
                            .setThird(damageSkillStateToSettings(state.skillState.damage.third))
                    )
                    .setSinBundle(
                        IdentityFilterSettings.IdentitySettings.FilterSinStateBundle.newBuilder()
                            .setFirst(sinSkillStateToSettings(state.skillState.sin.first))
                            .setSecond(sinSkillStateToSettings(state.skillState.sin.second))
                            .setThird(sinSkillStateToSettings(state.skillState.sin.third))
                    )
                    .setThirdIsCounter(state.skillState.thirdSkillIsCounter)
            )
            .setResistState(
                IdentityFilterSettings.IdentitySettings.FilterDamageStateBundle.newBuilder()
                    .setFirst(damageSkillStateToSettings(state.resistState.first))
                    .setSecond(damageSkillStateToSettings(state.resistState.second))
                    .setThird(damageSkillStateToSettings(state.resistState.third))
            )
            .setEffectsState(
                IdentityFilterSettings.IdentitySettings.FilterEffectBlockState.newBuilder()
                    .putAllEffects(
                        state.effectsState.effects.mapKeys { (key, _) -> key.toString() }
                            .toMutableMap()
                    )
            )
            .setSinnersState(
                IdentityFilterSettings.IdentitySettings.FilterSinnersBlockState.newBuilder()
                    .putAllSinners(
                        state.sinnersState.sinners.mapKeys { (key, _) -> key.id }
                            .toMutableMap()
                    )
            )
            .build()
    }

    private fun damageSkillStateToSettings(typeHolder: TypeHolder<DamageType>): String {
        if (typeHolder is TypeHolder.Empty) return EMPTY_STATE_VALUE
        return (typeHolder as TypeHolder.Value<DamageType>).value.toString()
    }

    private fun sinSkillStateToSettings(typeHolder: TypeHolder<Sin>): String {
        if (typeHolder is TypeHolder.Empty) return EMPTY_STATE_VALUE
        return (typeHolder as TypeHolder.Value<Sin>).value.toString()
    }


}