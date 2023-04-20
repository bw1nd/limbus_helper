package ua.blackwind.limbushelper.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.ui.theme.addToPartyPlusIconColor
import ua.blackwind.limbushelper.ui.theme.removeFromPartyCheckIconColor
import ua.blackwind.limbushelper.ui.util.BannerShape

@Composable
fun InPartyCheckBox(
    checked: Boolean,
    size: Dp,
    modifier: Modifier = Modifier,
    onClick: (Boolean) -> Unit
) {
    val convertedSize = with(LocalDensity.current) {
        size.toPx()
    }
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }
    Surface(
        onClick = { multipleEventsCutter.processEvent { onClick(checked) } },
        shape = BannerShape(Size(convertedSize, convertedSize)),
        color = MaterialTheme.colorScheme.primary,
        tonalElevation = 2.dp,
        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.onPrimaryContainer),
        modifier = modifier
            .size(size)
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Spacer(modifier = Modifier.height(size * .1f))
            if (checked) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Outlined.Check),
                    tint = removeFromPartyCheckIconColor,
                    contentDescription = null,
                    modifier = Modifier.size(size * .7f)
                )
            } else {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Default.Add),
                    tint = addToPartyPlusIconColor,
                    contentDescription = null,
                    modifier = Modifier.size(size * .7f)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewInPartyCheckBoxRemove() {
    Box {
        InPartyCheckBox(
            true,
            40.dp,
            Modifier.align(Alignment.TopCenter),
            {})
    }
}

@Preview
@Composable
fun PreviewInPartyCheckBoxAdd() {
    Box {
        InPartyCheckBox(
            false,
            40.dp,
            Modifier.align(Alignment.TopCenter),
            {})
    }
}