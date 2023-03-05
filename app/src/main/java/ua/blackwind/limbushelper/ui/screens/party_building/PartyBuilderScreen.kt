package ua.blackwind.limbushelper.ui.screens.party_building

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun PartyBuilderScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box() {
            Text(
                "This is party building screen",
                modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
            )
        }
    }

}



