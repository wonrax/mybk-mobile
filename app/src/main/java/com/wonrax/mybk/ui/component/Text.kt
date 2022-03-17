package com.wonrax.mybk.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.wonrax.mybk.ui.theme.Color
import com.wonrax.mybk.ui.theme.Inter
import androidx.compose.material.Text as MaterialText

enum class FontWeight {
    Regular,
    Medium,
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
    softWrap: Boolean = true,
    letterSpacing: TextUnit? = null,
) {

    val fw = when (fontWeight) {
        FontWeight.Regular -> androidx.compose.ui.text.font.FontWeight(400)
        FontWeight.Medium -> androidx.compose.ui.text.font.FontWeight(500)
        FontWeight.Bold -> androidx.compose.ui.text.font.FontWeight(700)
    }

    val fs = when (fontSize) {
        FontSize.Small -> 12.sp
        FontSize.Body -> 14.sp
        FontSize.Large -> 18.sp
        FontSize.Heading -> 28.sp
    }

    val ls = letterSpacing
        ?: when (fontSize) {
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
        lineHeight = fs * 1.35,
        softWrap = softWrap
    )
}
