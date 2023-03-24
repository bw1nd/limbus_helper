package ua.blackwind.limbushelper.ui.screens.filter_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.R

import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Effect
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.ui.common.SegmentedButton
import ua.blackwind.limbushelper.ui.util.*

private const val NUMBER_OF_EFFECTS_IN_COLUMN = 3

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilterDrawerSheet(
    filterSheetMode: FilterSheetMode,
    skillState: FilterSkillBlockState,
    resistState: FilterDamageStateBundle,
    effectsState: FilterEffectBlockState,
    resistLabels: FilterResistButtonLabels,
    sinPickerVisible: Boolean,
    onSwitchChange: (Int) -> Unit,
    onFilterButtonClick: () -> Unit,
    onClearFilterButtonPress: () -> Unit,
    onSkillButtonClick: (Int) -> Unit,
    onSkillButtonLongPress: (Int) -> Unit,
    onSinPickerClick: (StateType<Sin>) -> Unit,
    onResistButtonClick: (Int) -> Unit,
    onEffectCheckedChange: (Boolean, Effect) -> Unit
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
                        onLongClick = { onClearFilterButtonPress() },
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
                items = listOf("Type", "Effects"),
                onItemSelection = onSwitchChange,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.weight(.6f))
        }
        FilterBlock(
            filterSheetMode = filterSheetMode,
            skillState = skillState,
            resistState = resistState,
            effectsState = effectsState,
            resistLabels = resistLabels,
            sinPickerVisible = sinPickerVisible,
            onSkillButtonClick = onSkillButtonClick,
            onSkillButtonLongPress = onSkillButtonLongPress,
            onResistButtonClick = onResistButtonClick,
            onSinPickerClick = onSinPickerClick,
            onEffectCheckedChange = onEffectCheckedChange
        )
        OutlinedButton(onClick = { onFilterButtonClick() }) {
            Text("FILTER", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun FilterBlock(
    filterSheetMode: FilterSheetMode,
    skillState: FilterSkillBlockState,
    resistState: FilterDamageStateBundle,
    effectsState: FilterEffectBlockState,
    resistLabels: FilterResistButtonLabels,
    sinPickerVisible: Boolean,
    onSkillButtonClick: (Int) -> Unit,
    onSkillButtonLongPress: (Int) -> Unit,
    onSinPickerClick: (StateType<Sin>) -> Unit,
    onResistButtonClick: (Int) -> Unit,
    onEffectCheckedChange: (Boolean, Effect) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(400.dp)
            .height(170.dp)
            .padding(bottom = 5.dp)
    ) {
        when (filterSheetMode) {
            FilterSheetMode.Effects -> FilterEffectsBlock(
                effectsState,
                onEffectCheckedChange
            )
            FilterSheetMode.Type -> FilterTypeBlock(
                sinPickerVisible,
                onSinPickerClick,
                skillState,
                onSkillButtonClick,
                onSkillButtonLongPress,
                resistLabels,
                resistState,
                onResistButtonClick
            )
        }
    }
}

@Composable
private fun FilterTypeBlock(
    sinPickerVisible: Boolean,
    onSinPickerClick: (StateType<Sin>) -> Unit,
    skillState: FilterSkillBlockState,
    onSkillButtonClick: (Int) -> Unit,
    onSkillButtonLongPress: (Int) -> Unit,
    resistLabels: FilterResistButtonLabels,
    resistState: FilterDamageStateBundle,
    onResistButtonClick: (Int) -> Unit
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

@Composable
fun FilterEffectsBlock(
    state: FilterEffectBlockState,
    onEffectCheckedChange: (Boolean, Effect) -> Unit
) {
    val chunked = state.effects.keys.chunked(
        NUMBER_OF_EFFECTS_IN_COLUMN
    )
    LazyRow {
        items(state.effects.size / NUMBER_OF_EFFECTS_IN_COLUMN) { index ->
            Column {
                chunked[index].forEach { effect ->
                    EffectItem(
                        effect = effect,
                        checked = state.effects[effect] ?: false,
                        onEffectCheckedChange = onEffectCheckedChange
                    )
                }
            }
        }
    }
}

@Composable
fun EffectItem(effect: Effect, checked: Boolean, onEffectCheckedChange: (Boolean, Effect) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = { state -> onEffectCheckedChange(state, effect) })
        Image(painter = painterResource(id = getEffectIcon(effect)), contentDescription = null)
    }
}

@Composable
fun FilterSkillBlock(
    state: FilterSkillBlockState,
    onButtonClick: (Int) -> Unit,
    onButtonLongPress: (Int) -> Unit
) {
    Row {
        FilterSkillButton(
            id = 1,
            damage = state.damage.first,
            sin = state.sin.first,
            onClick = onButtonClick,
            onButtonLongPress = onButtonLongPress
        )
        FilterSkillButton(
            id = 2,
            damage = state.damage.second,
            sin = state.sin.second,
            onClick = onButtonClick,
            onButtonLongPress = onButtonLongPress
        )
        FilterSkillButton(
            id = 3,
            damage = state.damage.third,
            sin = state.sin.third,
            onClick = onButtonClick,
            onButtonLongPress = onButtonLongPress
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilterSkillButton(
    id: Int,
    damage: StateType<DamageType>,
    sin: StateType<Sin>,
    onClick: (Int) -> Unit,
    onButtonLongPress: (Int) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.primary
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier

        ) {
            val size = with(LocalDensity.current) {
                70.dp.toPx()
            }
            Surface(
                shape = HexagonShape(Size(size, size)),
                color = when (sin) {
                    StateType.Empty -> MaterialTheme.colorScheme.secondary
                    is StateType.Value -> getSinColor(sin.value)
                },
                modifier = Modifier
                    .size(70.dp)
            ) {}
            Image(
                painter = painterResource(
                    id = when (damage) {
                        is StateType.Empty -> R.drawable.empty_big_ic
                        is StateType.Value<DamageType> -> when (damage.value) {
                            DamageType.SLASH -> R.drawable.slash_big_ic
                            DamageType.PIERCE -> R.drawable.pierce_big_ic
                            DamageType.BLUNT -> R.drawable.blunt_big_ic
                        }
                    }
                ), contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .combinedClickable(
                        enabled = true,
                        onClick = { onClick(id) },
                        onLongClick = { onButtonLongPress(id) }
                    )
            )
        }
    }
}

@Composable
fun FilterResistBlock(
    labels: FilterResistButtonLabels, state: FilterDamageStateBundle,
    onButtonClick: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        FilterResistButton(
            1,
            label = labels.ineffective,
            state = state.first,
            onClick = onButtonClick
        )
        FilterResistButton(
            2,
            label = labels.normal,
            state = state.second,
            onClick = onButtonClick
        )
        FilterResistButton(
            3,
            label = labels.fatal,
            state = state.third,
            onClick = onButtonClick
        )
    }
}

@Composable
fun FilterResistButton(
    id: Int,
    label: String,
    state: StateType<DamageType>,
    onClick: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(
                    id = when (state) {
                        is StateType.Empty -> R.drawable.def_empty_ic
                        is StateType.Value<DamageType> -> when (state.value) {
                            DamageType.SLASH -> R.drawable.def_slash_ic
                            DamageType.PIERCE -> R.drawable.def_pierce_ic
                            DamageType.BLUNT -> R.drawable.def_blunt_ic
                        }
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        onClick(id)
                    }
            )
        }
        Text(text = label, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun SinPicker(onClick: (StateType<Sin>) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        SinPickerButton(state = StateType.Empty, onClick = onClick)
        Sin.values().forEach { sin ->
            SinPickerButton(state = StateType.Value(sin), onClick = onClick)
        }
    }
}


@Composable
fun SinPickerButton(state: StateType<Sin>, onClick: (StateType<Sin>) -> Unit) {
    Image(
        painter = painterResource(
            id = when (state) {
                StateType.Empty -> R.drawable.sin_empty_ic
                is StateType.Value -> getSinIcon(state.value)
            }
        ), contentDescription = null,
        modifier = Modifier
            .size(40.dp)
            .clickable { onClick(state) }
    )
}


@Preview
@Composable
fun PreviewFilterBlock() {
    FilterDrawerSheet(
        FilterSheetMode.Effects,
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
        FilterResistButtonLabels(
            "Ineff.", "Normal", "Fatal"
        ), false,
        {}, {}, {}, {}, {}, {}, {}, { _, _ -> }
    )
}

@Preview(widthDp = 420)
@Composable
fun PreviewSinPicker() {
    SinPicker(onClick = {})
}