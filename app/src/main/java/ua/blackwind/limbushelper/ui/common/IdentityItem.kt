package ua.blackwind.limbushelper.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ua.blackwind.limbushelper.domain.sinner.model.Identity
import ua.blackwind.limbushelper.ui.previewIdentity

@Composable
fun IdentityItem(identity: Identity) {

}

@Preview
@Composable
private fun IdentityItemPreview() {
    IdentityItem(
        identity =
        previewIdentity
    )
}