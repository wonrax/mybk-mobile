package com.wonrax.bkstinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.wonrax.bkstinfo.network.Cookuest
import com.wonrax.bkstinfo.network.Response
import com.wonrax.bkstinfo.ui.theme.BKSTINFOTheme
import okhttp3.FormBody
import okhttp3.RequestBody
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BKSTINFOTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

private const val username: String = ""
private const val password: String = ""

@Composable
fun Greeting(name: String) {
    var displayText by remember { mutableStateOf("Hellow, pls login") }
    Column() {
        Text(displayText)
        Button(
            onClick = { signIn { displayText = it.body } },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = Color.White
            )
        ) {
            Text("Sign in")
        }
    }
}

private fun signIn(onLoggedIn: (Response) -> Unit) {
    Cookuest.get(
        "https://sso.hcmut.edu.vn/cas/login",
        onResponse = { response: Response ->

            val checkLoggedIn = checkAlreadyLoggedIn(response)

            val getToken = {
                getMybkToken { response ->
                    onLoggedIn(response)
                    val token = getHtmlElementValue(
                        tag = "meta",
                        name = "_token",
                        input = response.body,
                        attr = "content"
                    )
                    print("Mybk Token: ")
                    println(token)
                }
            }

            if (!checkLoggedIn.isLoggedIn) {
                // Prepare form body containing required credentials to login
                val body: RequestBody = FormBody.Builder().apply {
                    add("_eventId", "submit")
                    add("execution", checkLoggedIn.execution)
                    add("lt", checkLoggedIn.lt)
                    add("username", username)
                    add("password", password)
                }.build()
                Cookuest.post(
                    "https://sso.hcmut.edu.vn/cas/login",
                    requestBody = body,
                    onResponse = { response ->
                        getToken()
                    }
                )
            } else getToken()
        }
    )
}

private val checkAlreadyLoggedIn = { response: Response ->
    // Parse 'lt' and 'execution' in HTML form rendered in the request. We'll need this for
    // submitting along login credentials
    val htmlStringContent = response.body
    val lt: String = getHtmlElementValue(name = "lt", input = htmlStringContent) ?: ""
    val execution: String = getHtmlElementValue(name = "execution", input = htmlStringContent) ?: ""
    var isLoggedIn = false

    if (lt == "" || execution == "") {
        isLoggedIn = true
    }

    object {
        var isLoggedIn = isLoggedIn
        val lt = lt
        val execution = execution
    }
}

private fun getMybkToken(callback: ((Response) -> Unit)?) {
    Cookuest.get(
        "https://sso.hcmut.edu.vn/cas/login?service=http%3A%2F%2Fmybk.hcmut.edu.vn%2Fstinfo%2F",
        onResponse = { response ->
            // Normally, OkHttp will follow the redirect automatically, but newer android sdks
            // doesn't permit HTTP traffic, so we've got to do the manual way: converting the
            // HTTP URLs to HTTPS URLs and then redirect.
            // This takes more work but also more secure (I suppose).
            if (response.code == 302) {
                val redirect: String =
                    response.headers["Location"] ?: throw Exception("Possibly expired stinfo token")
                val url: String = httpToHttpsURL(redirect)
                // Verify ticket
                Cookuest.get(
                    url,
                    onResponse = { response ->
                        if (response.code == 302) {
                            val redirect: String =
                                response.headers["Location"]
                                    ?: throw Exception("Possibly expired stinfo token")
                            val url: String = httpToHttpsURL(redirect)
                            // Get access tokens (response's Set-Cookie headers)
                            Cookuest.get(
                                url,
                                onResponse = { response ->
                                    if (callback != null) callback(response)
                                }
                            )
                        }
                    }
                )
            }
        }
    )
}

private fun httpToHttpsURL(url: String): String {
    return url.substring(0, 4) + 's' + url.substring(4)
}

private fun getHtmlElementValue(
    tag: String = "",
    name: String,
    input: String,
    attr: String = "value"
): String? {
    val r: Pattern = Pattern.compile("<$tag.*?name=\"${name}\".*?$attr=\"([^\"]*)\"[^>]*?/>")
    val m: Matcher = r.matcher(input)
    if (m.find()) {
        return m.group(1)
    }
    return null
}
