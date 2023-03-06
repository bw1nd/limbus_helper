package ua.blackwind.limbushelper.ui.screens.filter_screen

import android.widget.ToggleButton
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Sin
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.ui.common.IdentityItem
import ua.blackwind.limbushelper.ui.previewIdentity
import ua.blackwind.limbushelper.ui.util.getDamageTypeIcon
import ua.blackwind.limbushelper.ui.util.getSinColor

@RootNavGraph(start = true)
@Destination
@Composable
fun FilterScreen() {
    val viewModel = hiltViewModel<FilterScreenViewModel>()
    val identities = viewModel.filteredIdentities.collectAsState()
    FilterScreenUi(
        identities.value
    )
}

@Composable
fun FilterScreenUi(identities: List<Identity>) {
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
        FilterDrawerSheet()
    }
}

@Composable
fun FilterDrawerSheet() {
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
            Text("there will be our filter")
        }
    }

}

@Composable
fun FilterBlock(
    skillState: FilterSkillBlockButtonsState,
    onSwitchChange: () -> Unit,
    onSkillButtonClick: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Skills/Resistances")
            Spacer(modifier = Modifier.width(10.dp))
            Switch(checked = false, onCheckedChange = { onSwitchChange() })
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Effects")
        }
        FilterSkillBlock(state = skillState, onButtonClick = onSkillButtonClick)
        Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary)

    }
}

@Composable
fun FilterSkillBlock(state: FilterSkillBlockButtonsState, onButtonClick: (Int) -> Unit) {
    Row() {
        FilterSkillButton(id = 1, state = state.first, onClick = onButtonClick)
        FilterSkillButton(id = 2, state = state.second, onClick = onButtonClick)
        FilterSkillButton(id = 3, state = state.third, onClick = onButtonClick)
    }
}

data class FilterSkillBlockButtonsState(
    val first: FilterSkillState,
    val second: FilterSkillState,
    val third: FilterSkillState
)

@Composable
fun FilterSkillButton(id: Int, state: FilterSkillState, onClick: (Int) -> Unit) {
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

data class FilterSkillState(val type: DamageType?, val sin: Sin?)

private sealed class FilterDrawerState() {
    object Open: FilterDrawerState()
    object Closed: FilterDrawerState()
}

@Preview
@Composable
fun PreviewFilterBlock() {
    FilterBlock(
        FilterSkillBlockButtonsState(
            FilterSkillState(DamageType.BLUNT, Sin.LUST),
            FilterSkillState(DamageType.SLASH, null),
            FilterSkillState(null, null)
        ),
        {}, {}
    )
}

//@Preview(showSystemUi = true, widthDp = 412, heightDp = 846)
//@Composable
//fun PreviewFilterScreen() {
//    FilterScreenUi(identities = listOf(previewIdentity, previewIdentity, previewIdentity))
//}