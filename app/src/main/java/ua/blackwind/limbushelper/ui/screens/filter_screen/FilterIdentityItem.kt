package ua.blackwind.limbushelper.ui.screens.filter_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.ui.common.identityItemCore
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterIdentityModel

@Composable
fun FilterIdentityItem(viewIdentity: FilterIdentityModel) {
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier.width(380.dp)
    ) {
        val identity = viewIdentity.identity
        Row(content = identityItemCore(identity))
    }
}