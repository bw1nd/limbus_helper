package ua.blackwind.limbushelper.ui.util

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp


class HexagonShape(size: Size): Shape {
    private val x = size.center.x
    private val y = size.center.y
    private val radius = size.width / 2

    private val path = Path().apply {
        moveTo(x, y - radius * VERTICAL_EDGE_OFFSET)
        lineTo(x + radius * HORIZONTAL_OFFSET, y - radius * VERTICAL_MIDDLE_OFFSET)
        lineTo(x + radius * HORIZONTAL_OFFSET, y + radius * VERTICAL_MIDDLE_OFFSET)
        lineTo(x, y + radius * VERTICAL_EDGE_OFFSET)
        lineTo(x - radius * HORIZONTAL_OFFSET, y + radius * VERTICAL_MIDDLE_OFFSET)
        lineTo(x - radius * HORIZONTAL_OFFSET, y - radius * VERTICAL_MIDDLE_OFFSET)
        close()
    }

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(path)
    }

    companion object {
        private const val VERTICAL_MIDDLE_OFFSET = .47f
        private const val VERTICAL_EDGE_OFFSET = .9f
        private const val HORIZONTAL_OFFSET = .8f
    }
}

@Preview
@Composable
private fun HexagonalShapePreview() {
    val size = with(LocalDensity.current) {
        100.dp.toPx()
    }
    Surface(
        color = Color.Black,
        shape = HexagonShape(Size(size, size)),
        modifier = Modifier.size(100.dp)
    ) {

    }
}