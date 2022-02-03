package com.wonrax.mybk.ui.component

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.material.Icon as MaterialIcon

@Composable
fun Icon(
    resourceId: Int,
    modifier: Modifier = Modifier,
    tint: androidx.compose.ui.graphics.Color = MaterialTheme.colors.primary
) {
    MaterialIcon(
        painter = painterResource(id = resourceId),
        contentDescription = "",
        modifier = modifier,
        tint = tint
    )
}
