package com.geosid.aitalks.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ScrollToBottomButton(itemsSize: Int, onClick: () -> Unit) {
    SmallFloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
    ) {
        //Icon(Icons.Rounded.KeyboardArrowDown, stringResource(R.string.scroll_to_bottom_icon))
        Text(text =  ""+ itemsSize)
    }
}
