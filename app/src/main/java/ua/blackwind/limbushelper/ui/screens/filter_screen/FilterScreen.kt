package ua.blackwind.limbushelper.ui.screens.filter_screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.ui.common.IdentityItem
import ua.blackwind.limbushelper.ui.util.getDamageTypeIcon
import ua.blackwind.limbushelper.ui.util.getSinColor

@RootNavGraph(start = true)
@Destination
@Composable
fun FilterScreen() {
    val viewModel = hiltViewModel<FilterScreenViewModel>()
    val identities by viewModel.filteredIdentities.collectAsState()
    val filterSkillState by viewModel.filterSkillsState.collectAsState()
    val filterResistState by viewModel.filterResistState.collectAsState()
    val labels = FilterResistButtonLabels(
        stringResource(R.string.res_ineff),
        stringResource(R.string.res_normal),
        stringResource(R.string.res_fatal)
    )

    FilterScreenUi(
        identities,
        filterSkillState,
        filterResistState,
        labels,
        viewModel::onFilterModeSwitch,
        viewModel::onFilterSkillButtonClick,
        viewModel::onFilterResistButtonClick
    )
}

@Composable
fun FilterScreenUi(
    identities: List<Identity>,
    skillState: FilterSkillBlockState,
    resistState: FilterResistBlockState,
    resistLabels: FilterResistButtonLabels,
    onSwitchChange: (Boolean) -> Unit,
    onSkillButtonClick: (Int) -> Unit,
    onResistButtonClick: (Int) -> Unit

) {
    Box(contentAlignment = Alignment.BottomCenter) {
        Surface(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                items(identities.size) {
                    IdentityItem(identity = identities[it])
                }
            }
        }
        FilterDrawerSheet(
            skillState = skillState,
            resistState = resistState,
            resistLabels = resistLabels,
            onSwitchChange = onSwitchChange,
            onSkillButtonClick = onSkillButtonClick,
            onResistButtonClick = onResistButtonClick
        )
    }
}

@Composable
fun FilterDrawerSheet(
    skillState: FilterSkillBlockState,
    resistState: FilterResistBlockState,
    resistLabels: FilterResistButtonLabels,
    onSwitchChange: (Boolean) -> Unit,
    onSkillButtonClick: (Int) -> Unit,
    onResistButtonClick: (Int) -> Unit
) {
    var drawerState by remember { mutableStateOf<FilterDrawerState>(FilterDrawerState.Open) }
    var height by remember { mutableStateOf(200.dp) }

    Surface(
        modifier = Modifier
            .animateContentSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.size(400.dp, height)
        ) {
            IconButton(onClick = {
                if (drawerState == FilterDrawerState.Open) {
                    height = 200.dp
                    drawerState = FilterDrawerState.Closed
                } else {
                    height = 400.dp
                    drawerState = FilterDrawerState.Open
                }
            }) {
                Icon(
                    Icons.Filled.KeyboardArrowUp,
                    contentDescription = null
                )
            }
            FilterBlock(
                skillState = skillState,
                resistState = resistState,
                resistLabels = resistLabels,
                onSwitchChange = onSwitchChange,
                onSkillButtonClick = onSkillButtonClick,
                onResistButtonClick = onResistButtonClick
            )
        }
    }
}

@Composable
fun FilterBlock(
    skillState: FilterSkillBlockState,
    resistState: FilterResistBlockState,
    resistLabels: FilterResistButtonLabels,
    onSwitchChange: (Boolean) -> Unit,
    onSkillButtonClick: (Int) -> Unit,
    onResistButtonClick: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Skills/Resistances")
            Spacer(modifier = Modifier.width(10.dp))
            Switch(checked = false, onCheckedChange = onSwitchChange)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Effects")
        }
        FilterSkillBlock(state = skillState, onButtonClick = onSkillButtonClick)
        Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary)
        FilterResistBlock(labels = resistLabels, state = resistState, onResistButtonClick)

    }
}

@Composable
fun FilterSkillBlock(state: FilterSkillBlockState, onButtonClick: (Int) -> Unit) {
    Row() {
        FilterSkillButton(id = 1, state = state.first, onClick = onButtonClick)
        FilterSkillButton(id = 2, state = state.second, onClick = onButtonClick)
        FilterSkillButton(id = 3, state = state.third, onClick = onButtonClick)
    }
}

data class FilterSkillBlockState(
    val first: FilterSkillButtonState,
    val second: FilterSkillButtonState,
    val third: FilterSkillButtonState
)

@Composable
fun FilterSkillButton(id: Int, state: FilterSkillButtonState, onClick: (Int) -> Unit) {
    Surface(
        shape = CircleShape,
        color = state.sin?.let { getSinColor(it) } ?: Color.White
    ) {
        IconButton(onClick = { onClick(id) }) {
            Icon(painter = painterResource(id = state.type?.let {
                getDamageTypeIcon(
                    state.type
                )
            } ?: R.drawable.charge_ic
            ), contentDescription = null)
        }
    }
}

@Composable
fun FilterResistBlock(
    labels: FilterResistButtonLabels, state: FilterResistBlockState,
    onButtonClick: (Int) -> Unit
) {
    Row() {
        FilterResistButton(
            1,
            label = labels.ineffective,
            state = state.ineffective,
            onClick = onButtonClick
        )
        FilterResistButton(
            2,
            label = labels.normal,
            state = state.normal,
            onClick = onButtonClick
        )
        FilterResistButton(
            3,
            label = labels.fatal,
            state = state.fatal,
            onClick = onButtonClick
        )
    }
}

@Composable
fun FilterResistButton(id: Int, label: String, state: DamageType?, onClick: (Int) -> Unit) {
    Column() {
        IconButton(onClick = { onClick(id) }) {
            Icon(painter = painterResource(id = state?.let {
                getDamageTypeIcon(it)
            } ?: R.drawable.charge_ic
            ), contentDescription = null)
        }
        Text(text = label)
    }
}

data class FilterResistBlockState(
    val ineffective: DamageType?,
    val normal: DamageType?,
    val fatal: DamageType?
)

data class FilterResistButtonLabels(
    val ineffective: String,
    val normal: String,
    val fatal: String
)

data class FilterSkillButtonState(val type: DamageType?, val sin: Sin?)

private sealed class FilterDrawerState() {
    object Open: FilterDrawerState()
    object Closed: FilterDrawerState()
}

@Preview
@Composable
fun PreviewFilterBlock() {
    FilterBlock(
        FilterSkillBlockState(
            FilterSkillButtonState(DamageType.BLUNT, Sin.LUST),
            FilterSkillButtonState(DamageType.SLASH, null),
            FilterSkillButtonState(null, null)
        ),
        FilterResistBlockState(null, DamageType.BLUNT, null),
        FilterResistButtonLabels("Ineff.", "Normal", "Fatal"),
        {}, {}, {}
    )
}

//@Preview(showSystemUi = true, widthDp = 412, heightDp = 846)
//@Composable
//fun PreviewFilterScreen() {
//    FilterScreenUi(identities = listOf(previewIdentity, previewIdentity, previewIdentity))
//}