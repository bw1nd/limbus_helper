package ua.blackwind.limbushelper.ui.screens.filter_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination
@Composable
fun FilterScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box() {
            Text(
                "This is filter screen",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}