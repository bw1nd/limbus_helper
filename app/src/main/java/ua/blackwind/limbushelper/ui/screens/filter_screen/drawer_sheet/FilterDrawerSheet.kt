package ua.blackwind.limbushelper.ui.screens.filter_screen.drawer_sheet

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.ui.common.FilterDrawerTabSegmentedButton
import ua.blackwind.limbushelper.ui.screens.filter_screen.drawer_sheet.*
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterSinnerModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.*
import ua.blackwind.limbushelper.ui.util.*

private const val FILTER_BLOCK_WIDTH_DP = 400
private const val FILTER_BLOCK_HEIGHT_DP = 170

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilterDrawerSheet(
    mode: FilterSheetTab,
    filterState: FilterDrawerSheetState,
    skillSinPickerVisible: Boolean,
    resistSinPickerVisible: Boolean,
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
            FilterDrawerTabSegmentedButton(
                state = mode,
                color = MaterialTheme.colorScheme.onPrimary,
                onItemSelection = methods.onSwitchChange
            )
            Spacer(modifier = Modifier.weight(.6f))
        }
        FilterBlock(
            mode = mode,
            state = filterState,
            skillSinPickerVisible = skillSinPickerVisible,
            resistSinPickerVisible = resistSinPickerVisible,
            onSkillButtonClick = methods.onSkillButtonClick,
            onSkillButtonLongPress = methods.onSkillButtonLongPress,
            onIdentityResistButtonClick = methods.onIdentityResistButtonClick,
            onEgoResistButtonClick = methods.onEgoResistButtonClick,
            onEgoResistButtonLongPress = methods.onEgoResistButtonLongPress,
            onSinPickerClick = methods.onSinPickerClick,
            onEffectCheckedChange = methods.onEffectCheckedChange,
            onSinnerCheckedChange = methods.onSinnerCheckedChange
        )
        Box {
            val interactionEmitter = remember {
                MutableInteractionSource()
            }
            val isPressed by interactionEmitter.collectIsPressedAsState()
            val scale by animateFloatAsState(targetValue = if (isPressed) .93f else 1f)
            OutlinedButton(
                onClick = { methods.onFilterButtonClick() },
                interactionSource = interactionEmitter,
                modifier = Modifier
                    .wrapContentSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
            ) {
                Text("FILTER", color = MaterialTheme.colorScheme.onPrimary)
            }
        }

    }
}

@Composable
fun FilterBlock(
    mode: FilterSheetTab,
    state: FilterDrawerSheetState,
    skillSinPickerVisible: Boolean,
    resistSinPickerVisible: Boolean,
    onSkillButtonClick: (SelectedSkillButtonPosition) -> Unit,
    onSkillButtonLongPress: (SelectedSkillButtonPosition) -> Unit,
    onSinPickerClick: (StateType<Sin>) -> Unit,
    onIdentityResistButtonClick: (SelectedSkillButtonPosition) -> Unit,
    onEgoResistButtonClick: (SelectedResistButtonPosition) -> Unit,
    onEgoResistButtonLongPress: (SelectedResistButtonPosition) -> Unit,
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
                FilterDamageStateBundle(StateType.Empty, StateType.Empty, StateType.Empty),
                FilterSinStateBundle(StateType.Empty, StateType.Empty, StateType.Empty)
            )
            is FilterDrawerSheetState.IdentityMode -> state.skillState
        }

        val resistState = when (state) {
            is FilterDrawerSheetState.EgoMode -> FilterDamageStateBundle(
                StateType.Empty, StateType.Empty, StateType.Empty
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
                        skillSinPickerVisible = skillSinPickerVisible,
                        resistSinPickerVisible = resistSinPickerVisible,
                        onSinPickerClick = onSinPickerClick,
                        skillState = state.skillState,
                        resistState = state.resistState,
                        onSkillButtonClick = { onSkillButtonClick(SelectedSkillButtonPosition.First) },
                        onSkillButtonLongPress = {
                            onSkillButtonLongPress(
                                SelectedSkillButtonPosition.First
                            )
                        },
                        onResistButtonClick = onEgoResistButtonClick,
                        onResistButtonLongPress = onEgoResistButtonLongPress
                    )
                    is FilterDrawerSheetState.IdentityMode -> IdentityFilterTypeBlock(
                        skillSinPickerVisible,
                        onSinPickerClick,
                        skillState,
                        onSkillButtonClick,
                        onSkillButtonLongPress,
                        resistState,
                        onIdentityResistButtonClick
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
    skillSinPickerVisible: Boolean,
    resistSinPickerVisible: Boolean,
    onSinPickerClick: (StateType<Sin>) -> Unit,
    skillState: EgoFilterSkillBlockState,
    resistState: EgoFilterResistBlockState,
    onSkillButtonClick: () -> Unit,
    onSkillButtonLongPress: () -> Unit,
    onResistButtonClick: (SelectedResistButtonPosition) -> Unit,
    onResistButtonLongPress: (SelectedResistButtonPosition) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .requiredHeight(75.dp)
    ) {
        if (skillSinPickerVisible) {
            SinPicker(onClick = onSinPickerClick)
        } else {
            EgoFilterSkillBlock(
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
    if (resistSinPickerVisible) {
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
    sinPickerVisible: Boolean,
    onSinPickerClick: (StateType<Sin>) -> Unit,
    skillState: FilterSkillBlockState,
    onSkillButtonClick: (SelectedSkillButtonPosition) -> Unit,
    onSkillButtonLongPress: (SelectedSkillButtonPosition) -> Unit,
    resistState: FilterDamageStateBundle,
    onResistButtonClick: (SelectedSkillButtonPosition) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .requiredHeight(75.dp)
    ) {
        if (sinPickerVisible) {
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