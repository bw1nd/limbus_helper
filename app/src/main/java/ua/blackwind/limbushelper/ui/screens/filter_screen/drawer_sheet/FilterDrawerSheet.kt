package ua.blackwind.limbushelper.ui.screens.filter_screen.drawer_sheet

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.common.TypeHolder
import ua.blackwind.limbushelper.ui.common.FilterDrawerTabSegmentedButton
import ua.blackwind.limbushelper.ui.common.FilterModeSegmentedButton
import ua.blackwind.limbushelper.ui.screens.filter_screen.drawer_sheet.*
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterSinnerModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.*
import ua.blackwind.limbushelper.ui.util.*

private const val FILTER_BLOCK_WIDTH_DP = 400
private const val FILTER_BLOCK_HEIGHT_DP = 170

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilterDrawerSheet(
    tab: FilterSheetTab,
    filterState: FilterDrawerSheetState,
    filterMode: FilterMode,
    onFilterModeChanged: (Int) -> Unit,
    sinPickerState: SinPickerState,
    filterElementsMethods: FilterDrawerSheetMethods
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            Surface(
                color = MaterialTheme.colorScheme.primary,
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimary),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .size(60.dp, 30.dp)
                    .combinedClickable(
                        enabled = true,
                        onClick = {},
                        onLongClick = { filterElementsMethods.onClearFilterButtonPress() },
                    )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Clear",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.weight(.1f))
            FilterDrawerTabSegmentedButton(
                state = tab,
                color = MaterialTheme.colorScheme.onPrimary,
                onItemSelection = filterElementsMethods.onSwitchChange
            )
            Spacer(modifier = Modifier.weight(.6f))
        }
        FilterBlock(
            mode = tab,
            state = filterState,
            sinPickerState = sinPickerState,
            onSkillButtonClick = filterElementsMethods.onSkillButtonClick,
            onSkillButtonLongPress = filterElementsMethods.onSkillButtonLongPress,
            onIdentityResistButtonClick = filterElementsMethods.onIdentityResistButtonClick,
            onEgoResistButtonClick = filterElementsMethods.onEgoResistButtonClick,
            onEgoResistButtonLongPress = filterElementsMethods.onEgoResistButtonLongPress,
            onPriceButtonLongPress = filterElementsMethods.onEgoPriceButtonLongPress,
            onSinPickerClick = filterElementsMethods.onSinPickerClick,
            onEffectCheckedChange = filterElementsMethods.onEffectCheckedChange,
            onSinnerCheckedChange = filterElementsMethods.onSinnerCheckedChange
        )
        FilterModeSegmentedButton(
            state = filterMode,
            color = MaterialTheme.colorScheme.onPrimary,
            onItemSelection = onFilterModeChanged
        )
    }
}

@Composable
fun FilterBlock(
    mode: FilterSheetTab,
    state: FilterDrawerSheetState,
    sinPickerState: SinPickerState,
    onSkillButtonClick: (FilterSheetButtonPosition) -> Unit,
    onSkillButtonLongPress: (FilterSheetButtonPosition) -> Unit,
    onSinPickerClick: (TypeHolder<Sin>) -> Unit,
    onIdentityResistButtonClick: (FilterSheetButtonPosition) -> Unit,
    onEgoResistButtonClick: (FilterSheetButtonPosition) -> Unit,
    onEgoResistButtonLongPress: (FilterSheetButtonPosition) -> Unit,
    onPriceButtonLongPress: (FilterSheetButtonPosition) -> Unit,
    onEffectCheckedChange: (Boolean, Effect) -> Unit,
    onSinnerCheckedChange: (FilterSinnerModel) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(FILTER_BLOCK_WIDTH_DP.dp)
            .height(FILTER_BLOCK_HEIGHT_DP.dp)
            .padding(bottom = 5.dp)
    ) {
        val effectsState = when (state) {
            is FilterDrawerSheetState.EgoMode -> state.effectsState
            is FilterDrawerSheetState.IdentityMode -> state.effectsState
        }
        val skillState = when (state) {
            is FilterDrawerSheetState.EgoMode -> FilterSkillBlockState(
                FilterDamageStateBundle(TypeHolder.Empty, TypeHolder.Empty, TypeHolder.Empty),
                FilterSinStateBundle(TypeHolder.Empty, TypeHolder.Empty, TypeHolder.Empty)
            )
            is FilterDrawerSheetState.IdentityMode -> state.skillState
        }

        val resistState = when (state) {
            is FilterDrawerSheetState.EgoMode -> FilterDamageStateBundle(
                TypeHolder.Empty, TypeHolder.Empty, TypeHolder.Empty
            )
            is FilterDrawerSheetState.IdentityMode -> state.resistState
        }
        val sinnerState = when (state) {
            is FilterDrawerSheetState.EgoMode -> state.sinnersState
            is FilterDrawerSheetState.IdentityMode -> state.sinnersState
        }
        when (mode) {
            FilterSheetTab.Effects -> FilterEffectsBlock(
                effectsState,
                onEffectCheckedChange
            )
            FilterSheetTab.Type -> {
                when (state) {
                    is FilterDrawerSheetState.EgoMode -> EgoFilterTypeBlock(
                        sinPickerState = sinPickerState,
                        onSinPickerClick = onSinPickerClick,
                        skillState = state.skillState,
                        resistState = state.resistState,
                        priceState = state.priceState,
                        onSkillButtonClick = { onSkillButtonClick(FilterSheetButtonPosition.First) },
                        onSkillButtonLongPress = {
                            onSkillButtonLongPress(
                                FilterSheetButtonPosition.First
                            )
                        },
                        onResistButtonClick = onEgoResistButtonClick,
                        onResistButtonLongPress = onEgoResistButtonLongPress,
                        onPriceButtonLongPress = onPriceButtonLongPress
                    )
                    is FilterDrawerSheetState.IdentityMode -> IdentityFilterTypeBlock(
                        sinPickerState = sinPickerState,
                        onSinPickerClick = onSinPickerClick,
                        skillState = skillState,
                        onSkillButtonClick = onSkillButtonClick,
                        onSkillButtonLongPress = onSkillButtonLongPress,
                        resistState = resistState,
                        onResistButtonClick = onIdentityResistButtonClick
                    )
                }
            }
            FilterSheetTab.Sinners -> FilterSinnersBlock(
                state = sinnerState,
                onSinnerCheckedChange = onSinnerCheckedChange
            )
        }
    }
}

@Composable
fun EgoFilterTypeBlock(
    sinPickerState: SinPickerState,
    onSinPickerClick: (TypeHolder<Sin>) -> Unit,
    skillState: EgoFilterSkillBlockState,
    resistState: EgoFilterResistBlockState,
    priceState: EgoFilterPriceState,
    onSkillButtonClick: () -> Unit,
    onSkillButtonLongPress: () -> Unit,
    onResistButtonClick: (FilterSheetButtonPosition) -> Unit,
    onResistButtonLongPress: (FilterSheetButtonPosition) -> Unit,
    onPriceButtonLongPress: (FilterSheetButtonPosition) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .requiredHeight(75.dp)
    ) {
        if (sinPickerState is SinPickerState.SkillSelected
            || sinPickerState is SinPickerState.EgoPriceSelected
        ) {
            SinPicker(onClick = onSinPickerClick)
        } else {
            EgoFilterSkillBlock(
                state = skillState,
                onButtonClick = onSkillButtonClick,
                onButtonLongPress = onSkillButtonLongPress
            )
            Spacer(modifier = Modifier.width(10.dp))
            Divider(
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(3.dp, 70.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.height(100.dp)
            ) {
                EgoFilterPriceBlock(state = priceState, onItemLongPress = onPriceButtonLongPress)
                Text(text = "Resources", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
    Divider(
        thickness = 2.dp,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .width(180.dp)
            .padding(5.dp)
    )
    if (sinPickerState is SinPickerState.EgoResistSelected) {
        SinPicker(onClick = onSinPickerClick)
    } else {
        EgoFilterResistBlock(
            state = resistState,
            onResistButtonClick,
            onResistButtonLongPress
        )
    }
}

@Composable
private fun IdentityFilterTypeBlock(
    sinPickerState: SinPickerState,
    onSinPickerClick: (TypeHolder<Sin>) -> Unit,
    skillState: FilterSkillBlockState,
    onSkillButtonClick: (FilterSheetButtonPosition) -> Unit,
    onSkillButtonLongPress: (FilterSheetButtonPosition) -> Unit,
    resistState: FilterDamageStateBundle,
    onResistButtonClick: (FilterSheetButtonPosition) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .requiredHeight(75.dp)
    ) {
        if (sinPickerState is SinPickerState.SkillSelected) {
            SinPicker(onClick = onSinPickerClick)
        } else {
            IdentityFilterSkillBlock(
                state = skillState,
                onButtonClick = onSkillButtonClick,
                onButtonLongPress = onSkillButtonLongPress
            )
        }
    }
    Divider(
        thickness = 2.dp,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .width(300.dp)
            .padding(5.dp)
    )
    IdentityFilterResistBlock(state = resistState, onResistButtonClick)
}