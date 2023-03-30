package ua.blackwind.limbushelper.ui.screens.filter_screen.drawer_sheet

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.ui.common.SegmentedButton
import ua.blackwind.limbushelper.ui.screens.filter_screen.drawer_sheet.*
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterSinnerModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.*
import ua.blackwind.limbushelper.ui.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilterDrawerSheet(
    mode: FilterSheetMode,
    filterState: FilterDrawerSheetState,
    sinPickerVisible: Boolean,
    resistLabels: FilterResistButtonLabels,
    methods: FilterDrawerSheetMethods
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
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
                shape = CircleShape,
                modifier = Modifier
                    .size(30.dp)
                    .combinedClickable(
                        enabled = true,
                        onClick = {},
                        onLongClick = { methods.onClearFilterButtonPress() },
                    )
            ) {
                Icon(
                    Icons.Rounded.Close,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null,
                    modifier = Modifier

                )
            }
            Spacer(modifier = Modifier.weight(.4f))
            SegmentedButton(
                items = listOf("Type", "Effect", "Sinner"),
                onItemSelection = methods.onSwitchChange,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.weight(.6f))
        }
        FilterBlock(
            mode = mode,
            state = filterState,
            sinPickerVisible = sinPickerVisible,
            resistLabels = resistLabels,
            onSkillButtonClick = methods.onSkillButtonClick,
            onSkillButtonLongPress = methods.onSkillButtonLongPress,
            onResistButtonClick = methods.onResistButtonClick,
            onSinPickerClick = methods.onSinPickerClick,
            onEffectCheckedChange = methods.onEffectCheckedChange,
            onSinnerCheckedChange = methods.onSinnerCheckedChange
        )
        OutlinedButton(onClick = { methods.onFilterButtonClick() }) {
            Text("FILTER", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun FilterBlock(
    mode: FilterSheetMode,
    state: FilterDrawerSheetState,
    sinPickerVisible: Boolean,
    resistLabels: FilterResistButtonLabels,
    onSkillButtonClick: (SelectedButtonPosition) -> Unit,
    onSkillButtonLongPress: (SelectedButtonPosition) -> Unit,
    onSinPickerClick: (StateType<Sin>) -> Unit,
    onResistButtonClick: (SelectedButtonPosition) -> Unit,
    onEffectCheckedChange: (Boolean, Effect) -> Unit,
    onSinnerCheckedChange: (FilterSinnerModel) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(400.dp)
            .height(170.dp)
            .padding(bottom = 5.dp)
    ) {
        when (mode) {
            FilterSheetMode.Effects -> FilterEffectsBlock(
                state.effectsState,
                onEffectCheckedChange
            )
            FilterSheetMode.Type -> FilterTypeBlock(
                sinPickerVisible,
                onSinPickerClick,
                state.skillState,
                onSkillButtonClick,
                onSkillButtonLongPress,
                resistLabels,
                state.resistState,
                onResistButtonClick
            )
            FilterSheetMode.Sinners -> FilterSinnersBlock(
                state = state.sinnersState,
                onSinnerCheckedChange = onSinnerCheckedChange
            )
        }
    }
}

@Composable
private fun FilterTypeBlock(
    sinPickerVisible: Boolean,
    onSinPickerClick: (StateType<Sin>) -> Unit,
    skillState: FilterSkillBlockState,
    onSkillButtonClick: (SelectedButtonPosition) -> Unit,
    onSkillButtonLongPress: (SelectedButtonPosition) -> Unit,
    resistLabels: FilterResistButtonLabels,
    resistState: FilterDamageStateBundle,
    onResistButtonClick: (SelectedButtonPosition) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .requiredHeight(75.dp)
    ) {
        if (sinPickerVisible) {
            SinPicker(onClick = onSinPickerClick)
        } else {
            FilterSkillBlock(
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
    FilterResistBlock(labels = resistLabels, state = resistState, onResistButtonClick)
}

@Preview
@Composable
private fun PreviewFilterBlock() {
    FilterDrawerSheet(
        FilterSheetMode.Effects,
        FilterDrawerSheetState(
            FilterSkillBlockState(
                FilterDamageStateBundle(
                    StateType.Value(DamageType.BLUNT),
                    StateType.Value(DamageType.SLASH),
                    StateType.Value(DamageType.PIERCE)
                ),
                FilterSinStateBundle(
                    StateType.Value(Sin.LUST),
                    StateType.Value(Sin.GLOOM),
                    StateType.Empty
                )
            ),
            FilterDamageStateBundle(
                StateType.Value(DamageType.BLUNT),
                StateType.Value(DamageType.BLUNT),
                StateType.Empty
            ),
            FilterEffectBlockState(mapOf(Effect.BLEED to false)),
            emptyFilterSinnerBlockState()
        ),
        false,
        FilterResistButtonLabels(
            "Ineff.", "Normal", "Fatal"
        ),
        FilterDrawerSheetMethods(
            {}, {}, {}, {}, {}, {}, {}, { _, _ -> }, {  }
        )
    )
}