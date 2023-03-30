package ua.blackwind.limbushelper.ui.screens.filter_screen.drawer_sheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterEffectBlockState
import ua.blackwind.limbushelper.ui.util.getEffectIcon

private const val NUMBER_OF_EFFECTS_IN_COLUMN = 3

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