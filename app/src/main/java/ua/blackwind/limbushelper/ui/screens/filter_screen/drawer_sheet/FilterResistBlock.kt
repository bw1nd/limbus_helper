package ua.blackwind.limbushelper.ui.screens.filter_screen.drawer_sheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterDamageStateBundle
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterResistButtonLabels
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.SelectedButtonPosition
import ua.blackwind.limbushelper.ui.util.StateType

@Composable
fun FilterResistBlock(
    labels: FilterResistButtonLabels, state: FilterDamageStateBundle,
    onButtonClick: (SelectedButtonPosition) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        FilterResistButton(
            SelectedButtonPosition.First,
            label = labels.ineffective,
            state = state.first,
            onClick = onButtonClick
        )
        FilterResistButton(
            SelectedButtonPosition.Second,
            label = labels.normal,
            state = state.second,
            onClick = onButtonClick
        )
        FilterResistButton(
            SelectedButtonPosition.Third,
            label = labels.fatal,
            state = state.third,
            onClick = onButtonClick
        )
    }
}

@Composable
fun FilterResistButton(
    id: SelectedButtonPosition,
    label: String,
    state: StateType<DamageType>,
    onClick: (SelectedButtonPosition) -> Unit
) {
    require(id !is SelectedButtonPosition.None)
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