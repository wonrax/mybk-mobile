package com.wonrax.mybk.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.wonrax.mybk.ui.theme.Color
import androidx.compose.material.DropdownMenu as MaterialDropdownMenu

@Composable
fun <T> DropdownMenu(
    items: Array<T>,
    itemToStringRepresentation: (T) -> String?,
    selectedItem: T? = null,
    onSelectItem: ((T) -> Unit)? = null
) {

    var expanded by remember { mutableStateOf(false) }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    Column {
        Surface(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                }
                .clip(RoundedCornerShape(32.dp))
                .clickable { expanded = !expanded }
                .background(Color.Light)
                .padding(24.dp, 12.dp)
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (selectedItem != null)
                    itemToStringRepresentation(selectedItem)?.let { Text(it, Modifier.weight(1f)) }
                else
                    Text("Chưa chọn")
                Icon(Icons.ArrowDown, Modifier.padding(start = 8.dp))
            }
        }
        MaterialDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() }),
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    if (onSelectItem != null) onSelectItem(item)
                }) {
                    itemToStringRepresentation(item)?.let { Text(it) }
                }
            }
        }
    }
}
