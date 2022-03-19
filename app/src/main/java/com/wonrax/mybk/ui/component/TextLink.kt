package com.wonrax.mybk.ui.component

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.viewmodel.openBrowser

// TODO consider removing this
@Composable
fun TextLink(context: Context, title: String, url: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                openBrowser(context, url)
            }
    ) {
        Text(title, fontWeight = FontWeight.Medium, color = Color.Primary)
    }
}
