package com.wonrax.mybk.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.wonrax.mybk.R

val Inter = FontFamily(
    Font(R.font.inter_regular),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_medium, FontWeight.Medium)
)
val Typography = Typography(
    defaultFontFamily = Inter
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)
