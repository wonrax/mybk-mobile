package com.wonrax.mybk.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.wonrax.mybk.R
import com.wonrax.mybk.ui.theme.Color
import androidx.compose.material.Icon as MaterialIcon

enum class Icons {
    Calendar,
    CalendarBold,
    Location,
    Profile,
    ProfileBold,
    TimeCircle,
    TimeCircleBold,
    Transcript,
    TranscriptBold,
    ArrowUp,
    ArrowDown,
    EyeOff
}

val mapIconToResourceId: Map<Icons, Int> = mapOf(
    Icons.Calendar to R.drawable.ic_calendar,
    Icons.CalendarBold to R.drawable.ic_calendar_bold,
    Icons.Location to R.drawable.ic_location,
    Icons.Profile to R.drawable.ic_profile,
    Icons.ProfileBold to R.drawable.ic_profile_bold,
    Icons.TimeCircle to R.drawable.ic_timecircle,
    Icons.TimeCircleBold to R.drawable.ic_timecircle_bold,
    Icons.Transcript to R.drawable.ic_transcript,
    Icons.TranscriptBold to R.drawable.ic_transcript_bold,
    Icons.ArrowDown to R.drawable.ic_arrowdown,
    Icons.ArrowUp to R.drawable.ic_arrowup,
    Icons.EyeOff to R.drawable.ic_eyeoff
)

@Composable
fun Icon(
    icon: Icons,
    modifier: Modifier = Modifier,
    tint: androidx.compose.ui.graphics.Color = Color.Dark
) {
    if (mapIconToResourceId[icon] == null) {
        throw Exception("No defined icon available.")
    } else {
        mapIconToResourceId[icon]?.let { painterResource(id = it) }?.let {
            MaterialIcon(
                painter = it,
                contentDescription = "icon", // TODO meaningful description
                modifier = modifier,
                tint = tint
            )
        }
    }
}
