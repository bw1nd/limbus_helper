package ua.blackwind.limbushelper.ui.screens.filter_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Effect
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.ui.common.IdentityItem
import ua.blackwind.limbushelper.ui.common.SegmentedButton
import ua.blackwind.limbushelper.ui.util.*

@RootNavGraph(start = true)
@Destination
@Composable
fun FilterScreen() {
    val viewModel = hiltViewModel<FilterScreenViewModel>()
    val identities by viewModel.filteredIdentities.collectAsState()
    val filterSheetMode by viewModel.filterSheetMode.collectAsState()
    val filterSkillState by viewModel.filterSkillsState.collectAsState()
    val filterResistState by viewModel.filterResistState.collectAsState()
    val sinPickerVisible by viewModel.sinPickerVisible.collectAsState()
    val labels = FilterResistButtonLabels(
        stringResource(R.string.res_ineff),
        stringResource(R.string.res_normal),
        stringResource(R.string.res_fatal)
    )

    FilterScreenUi(
        identities,
        filterSheetMode,
        filterSkillState,
        filterResistState,
        labels,
        sinPickerVisible,
        viewModel::onFilterModeSwitch,
        viewModel::onFilterButtonClick,
        viewModel::onFilterSkillButtonClick,
        viewModel::onFilterSkillButtonLongPress,
        viewModel::onFilterSinPickerPress,
        viewModel::onFilterResistButtonClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreenUi(
    identities: List<Identity>,
    filterSheetMode: FilterSheetMode,
    skillState: FilterSkillBlockState,
    resistState: FilterDamageStateBundle,
    resistLabels: FilterResistButtonLabels,
    sinPickerVisible: Boolean,
    onSwitchChange: (Int) -> Unit,
    onFilterButtonClick: () -> Unit,
    onSkillButtonClick: (Int) -> Unit,
    onSkillButtonLongPress: (Int) -> Unit,
    onSinPickerClick: (StateType<Sin>) -> Unit,
    onResistButtonClick: (Int) -> Unit
) {
    BottomSheetScaffold(
        scaffoldState = rememberBottomSheetScaffoldState(),
        sheetContent = {
            FilterDrawerSheet(
                filterSheetMode = filterSheetMode,
                skillState = skillState,
                resistState = resistState,
                resistLabels = resistLabels,
                sinPickerVisible = sinPickerVisible,
                onSwitchChange = onSwitchChange,
                onFilterButtonClick = onFilterButtonClick,
                onSkillButtonClick = onSkillButtonClick,
                onSkillButtonLongPress = onSkillButtonLongPress,
                onResistButtonClick = onResistButtonClick,
                onSinPickerClick = onSinPickerClick
            )
        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(padding)
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    contentPadding = PaddingValues(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                ) {
                    items(identities.size) {
                        IdentityItem(identity = identities[it])
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun FilterDrawerSheet(
    filterSheetMode: FilterSheetMode,
    skillState: FilterSkillBlockState,
    resistState: FilterDamageStateBundle,
    resistLabels: FilterResistButtonLabels,
    sinPickerVisible: Boolean,
    onSwitchChange: (Int) -> Unit,
    onFilterButtonClick: () -> Unit,
    onSkillButtonClick: (Int) -> Unit,
    onSkillButtonLongPress: (Int) -> Unit,
    onSinPickerClick: (StateType<Sin>) -> Unit,
    onResistButtonClick: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            SegmentedButton(
                items = listOf("Type", "Effects"),
                onItemSelection = onSwitchChange,
                color = MaterialTheme.colorScheme.primary
            )
        }
        FilterBlock(
            filterSheetMode,
            skillState = skillState,
            resistState = resistState,
            resistLabels = resistLabels,
            sinPickerVisible = sinPickerVisible,
            onSkillButtonClick = onSkillButtonClick,
            onSkillButtonLongPress = onSkillButtonLongPress,
            onResistButtonClick = onResistButtonClick,
            onSinPickerClick = onSinPickerClick
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
    resistLabels: FilterResistButtonLabels,
    sinPickerVisible: Boolean,
    onSkillButtonClick: (Int) -> Unit,
    onSkillButtonLongPress: (Int) -> Unit,
    onSinPickerClick: (StateType<Sin>) -> Unit,
    onResistButtonClick: (Int) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(400.dp)
            .padding(bottom = 10.dp)

    ) {
        when (filterSheetMode) {
            FilterSheetMode.Effects -> FilterEffectsBlock(onCheckedChange = { _, _ -> })
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
        modifier = Modifier.width(300.dp)
    )
    FilterResistBlock(labels = resistLabels, state = resistState, onResistButtonClick)
}

@Composable
fun FilterEffectsBlock(onCheckedChange: (Boolean, Effect) -> Unit) {
    val validEffects = listOf(
        Effect.BLEED,
        Effect.BURN,
        Effect.POISE,
        Effect.RUPTURE,
        Effect.PARALYSIS,
        Effect.BIND,
        Effect.SINKING,
        Effect.TREMOR
    )
    Column() {
        validEffects.chunked(3).forEach { triplet ->
            Row() {
                triplet.forEach { effect ->
                    EffectItem(effect = effect, checked = false, onCheckedChange = onCheckedChange)
                }
            }
        }
    }
}

@Composable
fun EffectItem(effect: Effect, checked: Boolean, onCheckedChange: (Boolean, Effect) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = { state -> onCheckedChange(state, effect) })
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
        color = MaterialTheme.colorScheme.primary,
        shape = CircleShape,
        modifier = Modifier
            .combinedClickable(
                enabled = true,
                onClick = { onClick(id) },
                onLongClick = { onButtonLongPress(id) }
            )
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                shape = CircleShape,
                color = when (sin) {
                    StateType.Empty -> Color.White
                    is StateType.Value -> getSinColor(sin.value)
                },
                modifier = Modifier
                    .size(60.dp)
            ) {}
            Image(
                painter = painterResource(id = R.drawable.border_ic),
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
            )
            Image(
                painter = painterResource(
                    id = when (damage) {
                        is StateType.Empty -> R.drawable.empty_ic
                        is StateType.Value<DamageType> -> getDamageTypeIcon(damage.value)
                    }
                ), contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
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
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable {
                onClick(id)
            }) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.resist_ic),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
            )
            Image(
                painter = painterResource(
                    id = when (state) {
                        is StateType.Empty -> R.drawable.empty_ic
                        is StateType.Value<DamageType> -> getDamageTypeIcon(state.value)
                    }
                ),
                contentDescription = null,


                )
        }
        Text(text = label)
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
                StateType.Empty -> R.drawable.empty_ic
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
        FilterResistButtonLabels("Ineff.", "Normal", "Fatal"), false,
        {}, {}, {}, {}, {}, {}
    )
}

@Preview(widthDp = 420)
@Composable
fun PreviewSinPicker() {
    SinPicker(onClick = {})
}

@Preview
@Composable
fun PreviewEffectSheet() {
    FilterEffectsBlock({ _, _ -> })
}
//@Preview(showSystemUi = true, widthDp = 412, heightDp = 846)
//@Composable
//fun PreviewFilterScreen() {
//    FilterScreenUi(identities = listOf(previewIdentity, previewIdentity, previewIdentity))
//}