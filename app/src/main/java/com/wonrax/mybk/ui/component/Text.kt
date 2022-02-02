package com.wonrax.mybk.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.wonrax.mybk.ui.theme.Inter
import com.wonrax.mybk.ui.theme.MybkColors
import androidx.compose.material.Text as MaterialText

enum class FontWeight {
    Regular,
    Bold
}

enum class FontSize {
    Small,
    Body,
    Large,
    Heading
}

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Regular,
    fontSize: FontSize = FontSize.Body,
    color: Color = MybkColors.Dark,
) {

    val fw = when (fontWeight) {
        FontWeight.Regular -> androidx.compose.ui.text.font.FontWeight(400)
        FontWeight.Bold -> androidx.compose.ui.text.font.FontWeight(700)
    }

    val fs = when (fontSize) {
        FontSize.Small -> 12.sp
        FontSize.Body -> 14.sp
        FontSize.Large -> 18.sp
        FontSize.Heading -> 36.sp
    }

    MaterialText(
        text = text,
        fontFamily = Inter,
        fontWeight = fw,
        fontSize = fs,
        modifier = modifier,
        color = color
    )
}
