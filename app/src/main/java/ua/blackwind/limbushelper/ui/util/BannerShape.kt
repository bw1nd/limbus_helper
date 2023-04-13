package ua.blackwind.limbushelper.ui.util

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class BannerShape(size: Size): Shape {

    private val path = Path().apply {
        moveTo(size.width * .3f, 0f)
        lineTo(size.width, 0f)
        lineTo(size.width, size.height)
        lineTo(size.width * .3f, size.height * .85f)
        close()
    }

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(path)
    }
}

@Preview
@Composable
private fun PreviewTriangleShape() {
    val size = with(LocalDensity.current) {
        100.dp.toPx()
    }
    Surface(
        color = Color.Black,
        shape = BannerShape(Size(size, size)),
        modifier = Modifier.size(100.dp)
    ) {

    }
}