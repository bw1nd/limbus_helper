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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.EgoSinResistType
import ua.blackwind.limbushelper.domain.common.TypeHolder
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.*
import ua.blackwind.limbushelper.ui.util.HexagonShape
import ua.blackwind.limbushelper.ui.util.getSinColor

@Composable
fun IdentityFilterResistBlock(
    state: FilterDamageStateBundle,
    onButtonClick: (FilterSheetButtonPosition) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        FilterResistButton(
            FilterSheetButtonPosition.First,
            label = stringResource(id = R.string.res_ineff),
            state = state.first,
            onClick = onButtonClick
        )
        FilterResistButton(
            FilterSheetButtonPosition.Second,
            label = stringResource(id = R.string.res_normal),
            state = state.second,
            onClick = onButtonClick
        )
        FilterResistButton(
            FilterSheetButtonPosition.Third,
            label = stringResource(id = R.string.res_fatal),
            state = state.third,
            onClick = onButtonClick
        )
    }
}

@Composable
fun EgoFilterResistBlock(
    state: EgoFilterResistBlockState,
    onButtonClick: (FilterSheetButtonPosition) -> Unit,
    onButtonLongPress: (FilterSheetButtonPosition) -> Unit,
) {
    val (first, second, third) = state
    Row() {
        listOf(
            FilterSheetButtonPosition.First to first,
            FilterSheetButtonPosition.Second to second,
            FilterSheetButtonPosition.Third to third,
        ).forEach { (id, current) ->
            EgoFilterResistButton(
                id = id,
                label = when (current.resist) {
                    EgoSinResistType.INEFF -> stringResource(id = R.string.res_ineff)
                    EgoSinResistType.ENDURE -> stringResource(id = R.string.res_endure)
                    EgoSinResistType.FATAL -> stringResource(id = R.string.res_fatal)
                    EgoSinResistType.NORMAL -> stringResource(id = R.string.res_normal)
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
    id: FilterSheetButtonPosition, label: String,
    state: EgoFilterResistArg,
    onClick: (FilterSheetButtonPosition) -> Unit,
    onLongPress: (FilterSheetButtonPosition) -> Unit
) {
    require(id !is FilterSheetButtonPosition.None)
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
                    TypeHolder.Empty -> MaterialTheme.colorScheme.secondary
                    is TypeHolder.Value -> getSinColor(state.sin.value)
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
    id: FilterSheetButtonPosition,
    label: String,
    state: TypeHolder<DamageType>,
    onClick: (FilterSheetButtonPosition) -> Unit
) {
    require(id !is FilterSheetButtonPosition.None)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(
                    id = when (state) {
                        is TypeHolder.Empty -> R.drawable.def_empty_ic
                        is TypeHolder.Value<DamageType> -> when (state.value) {
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