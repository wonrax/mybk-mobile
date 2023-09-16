package com.wonrax.mybk.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonrax.mybk.ui.component.FontSize
import com.wonrax.mybk.ui.component.FontWeight
import com.wonrax.mybk.ui.component.Text

val paragraphs = listOf(
    """
        Mybk Mobile là ứng dụng mã nguồn mở dành cho sinh viên trường Đại học Bách Khoa - ĐHQG
        TP.HCM. Khi nhấn nút Đăng nhập, bạn đã đồng ý với các điều khoản sau:
    """.trimIndent().replace("\n", " "),
    """
        1. Mybk Mobile không phải là ứng dụng chính thức của trường Đại học Bách Khoa - ĐHQG TP.HCM
        và không có bất cứ liên quan gì đến ban quản lý, cán bộ trường. Mybk Mobile không chịu
        trách nhiệm cho việc bạn trễ giờ học, quên đi học hay trễ giờ thi, quên đi thi.
    """.trimIndent().replace("\n", " "),
    """
        2. Mybk Mobile không lưu trữ bất kỳ thông tin nào của bạn ở trên internet. Mọi dữ liệu của
        bạn đều được lưu trữ ở trên điện thoại và chỉ ở trên điện thoại. Thông tin đăng nhập của
        bạn được mã hóa và bảo vệ bởi hệ điều hành. Tuy nhiên, Mybk Mobile không chịu trách nhiệm
        cho các sự cố liên quan về tài khoản Mybk của bạn (chẳng hạn như bị mất tài khoản do
        hacker, hay do cài đặt và sử dụng các phiên bản Mybk Mobile đã bị chỉnh sửa, v.v.).
    """.trimIndent().replace("\n", " ")
)

@Composable
fun PolicyScreen(upAction: () -> Unit) {
    TitleBarScreen(title = "Điều khoản sử dụng", upAction = upAction) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Điều khoản sử dụng",
                fontSize = FontSize.Heading,
                fontWeight = FontWeight.Medium
            )
            paragraphs.forEach { Text(it, lineHeight = 1.5) }
        }
    }
}
