package ua.blackwind.limbushelper.ui.screens.filter_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.blackwind.limbushelper.ui.common.AlternativeCheckbox
import ua.blackwind.limbushelper.ui.common.egoItemCore
import ua.blackwind.limbushelper.ui.common.identityItemCore
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterDataModel
import ua.blackwind.limbushelper.ui.screens.filter_screen.model.FilterItemTypeModel
import ua.blackwind.limbushelper.ui.util.previewIdentity


private const val IDENTITY_PORTRAIT_WIDTH = 60

//TODO need better naming for this
@Composable
fun FilterListItem(
    listItem: FilterDataModel,
    onInPartyChecked: (FilterDataModel) -> Unit,
    onInPartyUnChecked: (FilterDataModel) -> Unit
) {
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimaryContainer),
        shape = CutCornerShape(topStart = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        modifier = Modifier.width(380.dp).height(100.dp)
    ) {
        val item = listItem.item
        Row(verticalAlignment = Alignment.CenterVertically) {
            when (item) {
                is FilterItemTypeModel.IdentityType ->
                    Row(
                        content = identityItemCore(
                            item.identity,
                            IDENTITY_PORTRAIT_WIDTH
                        )
                    )
                is FilterItemTypeModel.EgoType -> Row(
                    content = egoItemCore(
                        ego = item.ego,
                        portraitWidthDp = IDENTITY_PORTRAIT_WIDTH
                    )
                )
            }

            AlternativeCheckbox(
                checked = listItem.inParty,
                onCheckedChange = { checked ->
                    if (checked) onInPartyChecked(listItem) else onInPartyUnChecked(listItem)
                },
            )
        }
    }
}

@Preview
@Composable
private fun PreviewFilterIdentityItem() {
    FilterListItem(listItem = FilterDataModel(
        FilterItemTypeModel.IdentityType(
            previewIdentity,
        ), true
    ), { }, {})
}