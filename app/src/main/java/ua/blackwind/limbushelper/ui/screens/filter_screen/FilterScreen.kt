package ua.blackwind.limbushelper.ui.screens.filter_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import ua.blackwind.limbushelper.ui.common.IdentityItem
import ua.blackwind.limbushelper.ui.previewIdentity

@RootNavGraph(start = true)
@Destination
@Composable
fun FilterScreen() {
    FilterScreenUi(
        identities =
        listOf(
            previewIdentity,
            previewIdentity,
            previewIdentity,
            previewIdentity,
            previewIdentity,

        )
    )
}

@Composable
fun FilterScreenUi(identities: List<ua.blackwind.limbushelper.domain.sinner.model.Identity>) {
    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(identities.size) {
                IdentityItem(identity = identities[it])
            }
        }
    }
}

@Preview(showSystemUi = true, widthDp = 412, heightDp = 846)
@Composable
fun PreviewFilterScreen() {
    FilterScreenUi(identities = listOf(previewIdentity, previewIdentity, previewIdentity))
}