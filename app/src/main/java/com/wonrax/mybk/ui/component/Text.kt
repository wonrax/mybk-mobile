package com.wonrax.mybk.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.ui.theme.Inter
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
    color: androidx.compose.ui.graphics.Color = Color.Dark,
    textAlign: TextAlign? = null,
    softWrap: Boolean = true
) {

    val fw = when (fontWeight) {
        FontWeight.Regular -> androidx.compose.ui.text.font.FontWeight(400)
        FontWeight.Bold -> androidx.compose.ui.text.font.FontWeight(700)
    }

    val fs = when (fontSize) {
        FontSize.Small -> 12.sp
        FontSize.Body -> 14.sp
        FontSize.Large -> 16.sp
        FontSize.Heading -> 28.sp
    }

    val ls = when (fontSize) {
        FontSize.Heading -> (-0.8).sp
        else -> 0.sp
    }

    val ta = textAlign ?: TextAlign.Left

    MaterialText(
        text = text,
        fontFamily = Inter,
        fontWeight = fw,
        fontSize = fs,
        modifier = modifier,
        color = color,
        letterSpacing = ls,
        textAlign = ta,
        softWrap = softWrap
    )
}
