package ua.blackwind.limbushelper.ui.screens.filter_screen.drawer_sheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.common.TypeHolder
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.EgoFilterPriceState
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterSheetButtonPosition
import ua.blackwind.limbushelper.ui.util.getSinIcon


@Composable
fun EgoFilterPriceBlock(
    state: EgoFilterPriceState,
    onItemLongPress: (FilterSheetButtonPosition) -> Unit
) {
    Row() {
        EgoFilterPriceButton(
            position = FilterSheetButtonPosition.First,
            state = state.first,
            onItemClick = onItemLongPress
        )
        EgoFilterPriceButton(
            position = FilterSheetButtonPosition.Second,
            state = state.second,
            onItemClick = onItemLongPress
        )
        EgoFilterPriceButton(
            position = FilterSheetButtonPosition.Third,
            state = state.third,
            onItemClick = onItemLongPress
        )
    }
}

@Composable
fun EgoFilterPriceButton(
    state: TypeHolder<Sin>,
    position: FilterSheetButtonPosition,
    onItemClick: (FilterSheetButtonPosition) -> Unit
) {
    Image(
        painter = painterResource(
            id = when (state) {
                TypeHolder.Empty -> R.drawable.sin_empty_ic
                is TypeHolder.Value -> getSinIcon(state.value)
            }
        ), contentDescription = null,
        modifier = Modifier
            .size(40.dp)
            .clickable { onItemClick(position) }
    )
}