package ua.blackwind.limbushelper.ui.screens.filter_screen.state

import ua.blackwind.limbushelper.domain.common.*
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterSinnerModel

private const val FIRST_SINNER_ID = 1
private const val LAST_SINNER_ID = 12

sealed interface FilterDrawerSheetState {
    data class IdentityMode(
        val skillState: FilterSkillBlockState,
        val resistState: FilterDamageStateBundle,
        val effectsState: FilterEffectBlockState,
        val sinnersState: FilterSinnersBlockState
    ): FilterDrawerSheetState {
        companion object {
            fun getDefaultState() =
                IdentityMode(
                    emptyFilterSkillBlockState(),
                    emptyFilterResistClockState(),
                    emptyFilterEffectBlockState(),
                    emptyFilterSinnerBlockState()
                )
        }
    }

    data class EgoMode(
        val skillState: EgoFilterSkillBlockState,
        val priceState: EgoFilterPriceState,
        val resistState: EgoFilterResistBlockState,
        val effectsState: FilterEffectBlockState,
        val sinnersState: FilterSinnersBlockState
    ): FilterDrawerSheetState {
        companion object {
            fun getDefaultState() =
                EgoMode(
                    EgoFilterSkillBlockState(TypeHolder.Empty, TypeHolder.Empty),
                    emptyEgoFilterPriceBlockState(),
                    emptyEgoFilterResistBlockState(),
                    emptyFilterEffectBlockState(),
                    emptyFilterSinnerBlockState()
                )


        }
    }
}

sealed interface SinPickerState {
    object Gone: SinPickerState
    object SkillSelected: SinPickerState
    object EgoResistSelected: SinPickerState
    object EgoPriceSelected: SinPickerState
}

data class EgoFilterSkillBlockState(
    val damageType: TypeHolder<DamageType>,
    val sinType: TypeHolder<Sin>
)

data class EgoFilterResistBlockState(
    val first: EgoFilterResistArg,
    val second: EgoFilterResistArg,
    val third: EgoFilterResistArg,
    val fourth: EgoFilterResistArg
)

//TODO this looks kinda sus, mb there is better way
fun EgoFilterResistBlockState.toFilterArg() =
    listOf(
        first.resist to first.sin,
        second.resist to second.sin,
        third.resist to third.sin,
        third.resist to third.sin
    ).filter { it.second !is TypeHolder.Empty }
        .associate { it.first to (it.second as? TypeHolder.Value<Sin>)?.value as Sin }


data class EgoFilterResistArg(
    val sin: TypeHolder<Sin>,
    val resist: EgoSinResistType
)

data class EgoFilterPriceState(
    val first: TypeHolder<Sin>,
    val second: TypeHolder<Sin>,
    val third: TypeHolder<Sin>
)

fun EgoFilterPriceState.toFilterArg() =
    listOf(first, second, third, first).filter { it !is TypeHolder.Empty }
        .map { (it as TypeHolder.Value<Sin>).value }

data class FilterDrawerSheetMethods(
    val onSwitchChange: (Int) -> Unit,
    val onFilterButtonClick: () -> Unit,
    val onClearFilterButtonPress: () -> Unit,
    val onSkillButtonClick: (FilterSheetButtonPosition) -> Unit,
    val onSkillButtonLongPress: (FilterSheetButtonPosition) -> Unit,
    val onSinPickerClick: (TypeHolder<Sin>) -> Unit,
    val onIdentityResistButtonClick: (FilterSheetButtonPosition) -> Unit,
    val onEgoResistButtonClick: (FilterSheetButtonPosition) -> Unit,
    val onEgoResistButtonLongPress: (FilterSheetButtonPosition) -> Unit,
    val onEgoPriceButtonLongPress: (FilterSheetButtonPosition) -> Unit,
    val onEffectCheckedChange: (Boolean, Effect) -> Unit,
    val onSinnerCheckedChange: (FilterSinnerModel) -> Unit
)

sealed class FilterSheetTab(val index: Int) {
    object Type: FilterSheetTab(0)
    object Effects: FilterSheetTab(1)
    object Sinners: FilterSheetTab(2)
}

/**
 * State holder representing state of skill filter block.
 */
data class FilterSkillBlockState(
    val damage: FilterDamageStateBundle,
    val sin: FilterSinStateBundle
)

/**
 * State holder representing damage type state of skills or resistances
 * in order they appear on screen.
 */
data class FilterDamageStateBundle(
    val first: TypeHolder<DamageType>,
    val second: TypeHolder<DamageType>,
    val third: TypeHolder<DamageType>,
)

fun FilterDamageStateBundle.isUnique() = run {
    val filtered = listOf(first, second, third).filter { it !is TypeHolder.Empty }
    filtered.size == filtered.toSet().size
}

/**
 * State holder representing sin type state of skills
 * in order they appear on screen.
 */
data class FilterSinStateBundle(
    val first: TypeHolder<Sin>,
    val second: TypeHolder<Sin>,
    val third: TypeHolder<Sin>,
)

data class FilterEffectBlockState(
    val effects: Map<Effect, Boolean>
)

data class FilterSinnersBlockState(
    val sinners: Map<FilterSinnerModel, Boolean>
)

sealed class FilterSheetButtonPosition {
    object None: FilterSheetButtonPosition()
    object First: FilterSheetButtonPosition()
    object Second: FilterSheetButtonPosition()
    object Third: FilterSheetButtonPosition()
}

private fun emptyFilterSkillBlockState() = FilterSkillBlockState(
    FilterDamageStateBundle(TypeHolder.Empty, TypeHolder.Empty, TypeHolder.Empty),
    FilterSinStateBundle(TypeHolder.Empty, TypeHolder.Empty, TypeHolder.Empty)
)

private fun emptyEgoFilterPriceBlockState(): EgoFilterPriceState =
    EgoFilterPriceState(TypeHolder.Empty, TypeHolder.Empty, TypeHolder.Empty)

private fun emptyFilterResistClockState() =
    FilterDamageStateBundle(TypeHolder.Empty, TypeHolder.Empty, TypeHolder.Empty)

fun emptyFilterEffectBlockState() = FilterEffectBlockState(
    Effect.values().associateWith { false }
)

fun emptyFilterSinnerBlockState() = FilterSinnersBlockState(
    (FIRST_SINNER_ID..LAST_SINNER_ID).associate { index -> FilterSinnerModel(index) to false }
)

fun emptyEgoFilterResistBlockState() = EgoFilterResistBlockState(
    first = EgoFilterResistArg(TypeHolder.Empty, EgoSinResistType.NORMAL),
    second = EgoFilterResistArg(TypeHolder.Empty, EgoSinResistType.NORMAL),
    third = EgoFilterResistArg(TypeHolder.Empty, EgoSinResistType.NORMAL),
    fourth = EgoFilterResistArg(TypeHolder.Empty, EgoSinResistType.NORMAL)
)

