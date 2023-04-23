package ua.blackwind.limbushelper.ui.screens.filter_screen.drawer_sheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterEffectBlockState
import ua.blackwind.limbushelper.ui.theme.selectedFilterItemBorderColor
import ua.blackwind.limbushelper.ui.util.getEffectIcon
import kotlin.math.ceil

private const val NUMBER_OF_EFFECTS_IN_COLUMN = 3

@Composable
fun FilterEffectsUiContainer(
    state: FilterEffectBlockState,
    onEffectCheckedChange: (Boolean, Effect) -> Unit
) {
    val chunked = state.effects.keys.chunked(
        NUMBER_OF_EFFECTS_IN_COLUMN
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(
            ceil(
                (state.effects.size.toDouble() / NUMBER_OF_EFFECTS_IN_COLUMN)
            ).toInt()
        ) { index ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxHeight()
            ) {
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
fun EffectItem(
    effect: Effect,
    checked: Boolean,
    onEffectCheckedChange: (Boolean, Effect) -> Unit,
) {
    Box(contentAlignment = Alignment.Center) {
        Surface(
            shape = CircleShape,
            border = BorderStroke(
                2.dp,
                color = if (checked) selectedFilterItemBorderColor else MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Image(painter = painterResource(id = getEffectIcon(effect)),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(3.dp)
                    .clickable { onEffectCheckedChange(checked, effect) }
            )
        }
    }
}