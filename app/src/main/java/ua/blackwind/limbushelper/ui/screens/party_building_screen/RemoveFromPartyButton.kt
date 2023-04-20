package ua.blackwind.limbushelper.ui.screens.party_building_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.ui.common.MultipleEventsCutter
import ua.blackwind.limbushelper.ui.common.get
import ua.blackwind.limbushelper.ui.theme.removeFromPartyCrossIconColor
import ua.blackwind.limbushelper.ui.util.BannerShape

@Composable
fun RemoveFromPartyButton(
    size: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val convertedSize = with(LocalDensity.current) {
        size.toPx()
    }
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }
    Surface(
        onClick = { multipleEventsCutter.processEvent { onClick() } },
        shape = BannerShape(Size(convertedSize, convertedSize)),
        color = MaterialTheme.colorScheme.primary,
        tonalElevation = 2.dp,
        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.onPrimaryContainer),
        modifier = modifier
            .size(size)
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Spacer(modifier = Modifier.height(size * .1f))
            Icon(
                painter = rememberVectorPainter(image = Icons.Outlined.Close),
                tint = removeFromPartyCrossIconColor,
                contentDescription = null,
                modifier = Modifier.size(size * .7f)
            )
        }
    }
}