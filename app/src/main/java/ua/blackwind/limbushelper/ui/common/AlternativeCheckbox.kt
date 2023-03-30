package ua.blackwind.limbushelper.ui.common

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.blackwind.limbushelper.ui.theme.LimbusHelperTheme

@Composable
fun AlternativeCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = CheckboxDefaults.colors(
            checkedColor = MaterialTheme.colorScheme.outline,
            checkmarkColor = MaterialTheme.colorScheme.primary,
            uncheckedColor = MaterialTheme.colorScheme.outline,
        ),
        modifier = modifier
    )
}

@Preview(name = "AlternativeCheckboxCheckedPreview", showBackground = true)
@Composable
private fun PreviewAlternativeCheckbox() = LimbusHelperTheme {
    AlternativeCheckbox(
        checked = true,
        onCheckedChange = {},
    )
}

@Preview(
    name = "AlternativeCheckboxUncheckedPreview",
    showBackground = true,
)
@Composable
private fun PreviewDarkAlternativeCheckbox() {
    AlternativeCheckbox(
        checked = false,
        onCheckedChange = {},
    )
}