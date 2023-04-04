package ua.blackwind.limbushelper.ui.screens.filter_screen.drawer_sheet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.EgoSinResistType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.*
import ua.blackwind.limbushelper.ui.util.HexagonShape
import ua.blackwind.limbushelper.ui.util.StateType
import ua.blackwind.limbushelper.ui.util.getSinColor

@Composable
fun IdentityFilterResistBlock(
    labels: FilterResistButtonLabels, state: FilterDamageStateBundle,
    onButtonClick: (SelectedSkillButtonPosition) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        FilterResistButton(
            SelectedSkillButtonPosition.First,
            label = labels.ineffective,
            state = state.first,
            onClick = onButtonClick
        )
        FilterResistButton(
            SelectedSkillButtonPosition.Second,
            label = labels.normal,
            state = state.second,
            onClick = onButtonClick
        )
        FilterResistButton(
            SelectedSkillButtonPosition.Third,
            label = labels.fatal,
            state = state.third,
            onClick = onButtonClick
        )
    }
}

@Composable
fun EgoFilterResistBlock(
    labels: EgoResistButtonLabels,
    state: EgoFilterResistBlockState,
    onButtonClick: (SelectedResistButtonPosition) -> Unit,
    onButtonLongPress: (SelectedResistButtonPosition) -> Unit,
) {
    //TODO still need to implement changing resistance potency
    val (first, second, third, fourth) = state
    Row() {
        listOf(
            SelectedResistButtonPosition.First to first,
            SelectedResistButtonPosition.Second to second,
            SelectedResistButtonPosition.Third to third,
            SelectedResistButtonPosition.Fourth to fourth
        ).forEach { (id, current) ->
            EgoFilterResistButton(
                id = id,
                label = when (current.resist) {
                    StateType.Empty -> "None"
                    is StateType.Value -> when (current.resist.value) {
                        EgoSinResistType.INEFFECTIVE -> "Ineff."
                        EgoSinResistType.ENDURE -> "Endure"
                        EgoSinResistType.FATAL -> "Fatal"
                        EgoSinResistType.NORMAL -> "Normal"
                    }
                },
                state = current,
                onClick = onButtonClick,
                onLongPress = onButtonLongPress
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EgoFilterResistButton(
    id: SelectedResistButtonPosition, label: String,
    state: EgoFilterResistArg,
    onClick: (SelectedResistButtonPosition) -> Unit,
    onLongPress: (SelectedResistButtonPosition) -> Unit
) {
    require(id !is SelectedResistButtonPosition.None)
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.combinedClickable(
            onClick = { onClick(id) },
            onLongClick = { onLongPress(id) }
        )) {
        val size = with(LocalDensity.current) {
            60.dp.toPx()
        }
        Box() {
            Surface(
                shape = HexagonShape(Size(size, size)),
                color = when (state.sin) {
                    StateType.Empty -> MaterialTheme.colorScheme.secondary
                    is StateType.Value -> getSinColor(state.sin.value)
                },
                modifier = Modifier
                    .size(60.dp)
            ) {}
            Image(
                painter = painterResource(id = R.drawable.ego_resist_ic),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
        }
        Text(text = label, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun FilterResistButton(
    id: SelectedSkillButtonPosition,
    label: String,
    state: StateType<DamageType>,
    onClick: (SelectedSkillButtonPosition) -> Unit
) {
    require(id !is SelectedSkillButtonPosition.None)
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