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
    ArrowUp,
    ArrowDown,
    ArrowLeft,
    ArrowRightS,
    EyeOff,
    Paper,
    PaperBold,
    Chart,
    ChartBold,
    Message,
    Logout,
    Info,
    ExternalLink,
    ToggleEnabled,
    ToggleDisabled,
}

val mapIconToResourceId: Map<Icons, Int> = mapOf(
    Icons.Calendar to R.drawable.ic_calendar,
    Icons.CalendarBold to R.drawable.ic_calendar_bold,
    Icons.Location to R.drawable.ic_location,
    Icons.Profile to R.drawable.ic_profile,
    Icons.ProfileBold to R.drawable.ic_profile_bold,
    Icons.TimeCircle to R.drawable.ic_timecircle,
    Icons.ArrowDown to R.drawable.ic_arrowdown,
    Icons.ArrowUp to R.drawable.ic_arrowup,
    Icons.EyeOff to R.drawable.ic_eyeoff,
    Icons.ArrowLeft to R.drawable.ic_arrowleft,
    Icons.ArrowRightS to R.drawable.ic_arrowrights,
    Icons.Paper to R.drawable.ic_paper,
    Icons.PaperBold to R.drawable.ic_paper_bold,
    Icons.Chart to R.drawable.ic_chart,
    Icons.ChartBold to R.drawable.ic_chart_bold,
    Icons.Message to R.drawable.ic_message,
    Icons.Info to R.drawable.ic_info,
    Icons.Logout to R.drawable.ic_logout,
    Icons.ExternalLink to R.drawable.ic_externallink,
    Icons.ToggleDisabled to R.drawable.ic_toggledisabled,
    Icons.ToggleEnabled to R.drawable.ic_toggleenabled
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
                contentDescription = "app icon",
                modifier = modifier,
                tint = tint
            )
        }
    }
}
