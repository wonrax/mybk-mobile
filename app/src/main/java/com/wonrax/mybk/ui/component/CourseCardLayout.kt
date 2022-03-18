package com.wonrax.mybk.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.theme.Color

@Composable
fun CourseCardLayout(
    courseName: String? = null,
    courseId: String? = null,
    courseClassNumber: String? = null,
    courseCredit: Int? = null,
    onCourseClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Light)
            .fillMaxWidth()
    ) {
        var courseHeaderModifier = Modifier.fillMaxWidth()
        if (onCourseClick != null)
            courseHeaderModifier = courseHeaderModifier.then(
                Modifier.clickable(onClick = onCourseClick)
            )
        Row(
            courseHeaderModifier.then(Modifier.padding(24.dp, 16.dp, 24.dp, 12.dp)),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                if (courseName != null) {
                    Text(text = courseName, fontSize = FontSize.Large, fontWeight = FontWeight.Bold)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (courseId != null) {
                        Text(text = courseId, fontWeight = FontWeight.Bold)
                    }
                    if (courseClassNumber != null) {
                        Text(text = courseClassNumber)
                    }
                    if (courseCredit != null) {
                        Text(text = "$courseCredit tín chỉ")
                    }
                }
            }
            if (onCourseClick != null) Icon(icon = Icons.ArrowRightS)
        }
        Divider(color = Color.Grey10, width = 1.5.dp)
        Box(Modifier.padding(24.dp, 12.dp, 24.dp, 16.dp)) {
            content()
        }
    }
}
