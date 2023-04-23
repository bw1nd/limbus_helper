package ua.blackwind.limbushelper.ui.screens.filter_screen.drawer_sheet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.domain.common.TypeHolder
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.EgoFilterSkillBlockState
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterSheetButtonPosition
import ua.blackwind.limbushelper.ui.screens.filter_screen.state.FilterSkillBlockState
import ua.blackwind.limbushelper.ui.util.HexagonShape
import ua.blackwind.limbushelper.ui.util.getSinColor
import ua.blackwind.limbushelper.ui.util.getSinIcon

@Composable
fun IdentityFilterSkillUiContainer(
    state: FilterSkillBlockState,
    onButtonClick: (FilterSheetButtonPosition) -> Unit,
    onButtonLongPress: (FilterSheetButtonPosition) -> Unit
) {
    Row {
        FilterSkillButton(
            id = FilterSheetButtonPosition.First,
            damage = state.damage.first,
            sin = state.sin.first,
            onClick = onButtonClick,
            onButtonLongPress = onButtonLongPress,
            showCounter = false
        )
        FilterSkillButton(
            id = FilterSheetButtonPosition.Second,
            damage = state.damage.second,
            sin = state.sin.second,
            onClick = onButtonClick,
            onButtonLongPress = onButtonLongPress,
            showCounter = false
        )
        FilterSkillButton(
            id = FilterSheetButtonPosition.Third,
            damage = state.damage.third,
            showCounter = state.thirdSkillIsCounter,
            sin = state.sin.third,
            onClick = onButtonClick,
            onButtonLongPress = onButtonLongPress
        )
    }
}

@Composable
fun EgoFilterSkillUiContainer(
    state: EgoFilterSkillBlockState,
    onButtonClick: () -> Unit,
    onButtonLongPress: () -> Unit
) {
    FilterSkillButton(
        id = FilterSheetButtonPosition.First,
        damage = state.damageType,
        sin = state.sinType,
        onClick = { onButtonClick() },
        onButtonLongPress = { onButtonLongPress() },
        showCounter = false
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilterSkillButton(
    id: FilterSheetButtonPosition,
    damage: TypeHolder<DamageType>,
    showCounter: Boolean,
    sin: TypeHolder<Sin>,
    onClick: (FilterSheetButtonPosition) -> Unit,
    onButtonLongPress: (FilterSheetButtonPosition) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.primary
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier

        ) {
            val size = with(LocalDensity.current) {
                70.dp.toPx()
            }
            Surface(
                shape = HexagonShape(Size(size, size)),
                color = when (sin) {
                    TypeHolder.Empty -> MaterialTheme.colorScheme.secondary
                    is TypeHolder.Value -> getSinColor(sin.value)
                },
                modifier = Modifier
                    .size(70.dp)
            ) {}
            Image(
                painter = painterResource(
                    id = if (showCounter) {
                        R.drawable.counter_100_ic
                    } else {
                        when (damage) {
                            is TypeHolder.Empty -> R.drawable.empty_big_ic
                            is TypeHolder.Value<DamageType> -> when (damage.value) {
                                DamageType.SLASH -> R.drawable.slash_big_ic
                                DamageType.PIERCE -> R.drawable.pierce_big_ic
                                DamageType.BLUNT -> R.drawable.blunt_big_ic
                            }
                        }
                    }
                ), contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .combinedClickable(
                        enabled = true,
                        onClick = { onClick(id) },
                        onLongClick = { onButtonLongPress(id) }
                    )
            )
        }
    }
}


@Composable
fun SinPicker(onClick: (TypeHolder<Sin>) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        SinPickerButton(state = TypeHolder.Empty, onClick = onClick)
        Sin.values().forEach { sin ->
            SinPickerButton(state = TypeHolder.Value(sin), onClick = onClick)
        }
    }
}


@Composable
fun SinPickerButton(state: TypeHolder<Sin>, onClick: (TypeHolder<Sin>) -> Unit) {
    Image(
        painter = painterResource(
            id = when (state) {
                TypeHolder.Empty -> R.drawable.sin_empty_ic
                is TypeHolder.Value -> getSinIcon(state.value)
            }
        ), contentDescription = null,
        modifier = Modifier
            .size(40.dp)
            .clickable { onClick(state) }
    )
}

@Preview(widthDp = 420)
@Composable
fun PreviewSinPicker() {
    SinPicker(onClick = {})
}