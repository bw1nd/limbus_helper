package ua.blackwind.limbushelper.ui.screens.filter_screen.drawer_sheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterSinnerModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterSinnersBlockState
import ua.blackwind.limbushelper.ui.theme.selectedFilterItemBorderColor
import ua.blackwind.limbushelper.ui.util.getSinnerIconById

private const val NUMBER_OF_GRID_ROWS = 3

@Composable
fun FilterSinnersBlock(
    state: FilterSinnersBlockState,
    onSinnerCheckedChange: (FilterSinnerModel) -> Unit
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(NUMBER_OF_GRID_ROWS), userScrollEnabled = false,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        state.sinners.forEach { (sinner, checked) ->
            item {
                SinnerItem(
                    sinner = sinner,
                    checked = checked,
                    onSinnerCheckedChange = onSinnerCheckedChange
                )
            }
        }
    }
}

@Composable
fun SinnerItem(
    sinner: FilterSinnerModel,
    checked: Boolean,
    onSinnerCheckedChange: (FilterSinnerModel) -> Unit
) {
    Box(Modifier.padding(3.dp)) {
        Surface(
            shape = CircleShape,
            border = BorderStroke(
                2.dp, color =
                if (checked) selectedFilterItemBorderColor else MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.clickable { onSinnerCheckedChange(sinner) }
        ) {
            Image(
                painter = painterResource(id = getSinnerIconById(sinner.id)),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}