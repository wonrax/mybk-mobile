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
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.wonrax.bkstinfo.network.Cookuest
import com.wonrax.bkstinfo.network.Response
import com.wonrax.bkstinfo.network.utils.HtmlUtils.getHtmlElementValue
import com.wonrax.bkstinfo.network.utils.HttpUtils.httpToHttpsURL
import com.wonrax.bkstinfo.ui.theme.BKSTINFOTheme
import okhttp3.FormBody
import okhttp3.RequestBody

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BKSTINFOTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    var displayLoginStatus by remember { mutableStateOf("Hellow, pls login") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )
        Button(
            onClick = {
                signIn(username, password) { loginStatus: LoginStatus ->
                    displayLoginStatus =
                        when (loginStatus) {
                            LoginStatus.WRONG_PASSWORD -> "Wrong password, please try again"
                            LoginStatus.LOGGED_IN -> "Login successfully"
                            LoginStatus.UNKNOWN -> "Something went wrong on our side, please try again later"
                            else -> displayLoginStatus
                        }
                }
            },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = Color.White
            )
        ) {
            Text("Sign in")
        }
        Text(displayLoginStatus)
    }
}

/**
 * Sign in function, will login into SSO with provided credentials and get the access token
 * from mybk if the credentials are valid. The callback is called to reflect UI change.
 * @param username Username of the user's credential
 * @param password Password of the user's credential
 * @param onLoggedIn The callback which is called when there's an login status event
 */
private fun signIn(username: String, password: String, onLoggedIn: ((LoginStatus) -> Unit)?) {
    Cookuest.get(
        "https://sso.hcmut.edu.vn/cas/login",
        onResponse = { ssoResponse: Response ->

            val getToken = {
                getMybkToken { response ->
                    val token = getHtmlElementValue(
                        tag = "meta",
                        name = "_token",
                        input = response.body,
                        attr = "content"
                    )
                    if (token == null)
                        onLoggedIn?.invoke(LoginStatus.UNKNOWN)
                    else
                        onLoggedIn?.invoke(LoginStatus.LOGGED_IN)
                    print("Mybk Token: ")
                    println(token)
                }
            }

            val checkLoggedIn = checkLoginStatus(ssoResponse)

            when (checkLoggedIn.loginStatus) {
                LoginStatus.UNAUTHORIZED -> {
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
                        onResponse = { ssoWithCredentialResponse ->
                            val loginStatus =
                                checkLoginStatus(ssoWithCredentialResponse).loginStatus
                            onLoggedIn?.invoke(loginStatus)
                            when (loginStatus) {
                                LoginStatus.LOGGED_IN -> {
                                    getToken()
                                }
                                else -> {
                                }
                            }
                        }
                    )
                }

                LoginStatus.LOGGED_IN -> {
                    onLoggedIn?.invoke(LoginStatus.LOGGED_IN)
                    getToken()
                }

                else -> {
                }
            }
        }
    )
}

enum class LoginStatus {
    LOGGED_IN, WRONG_PASSWORD, UNAUTHORIZED, UNKNOWN
}

/**
 * Check login status based on the response HTML from SSO.
 *
 * Lambda params: response - the response HTML from SSO
 */
private val checkLoginStatus = { response: Response ->
    // Parse 'lt' and 'execution' in HTML form rendered in the request. We'll need this for
    // submitting along login credentials
    val htmlStringContent = response.body
    val lt: String = getHtmlElementValue(name = "lt", input = htmlStringContent) ?: ""
    val execution: String = getHtmlElementValue(name = "execution", input = htmlStringContent) ?: ""
    val loginStatus =
        if (htmlStringContent.contains("The credentials you provided cannot be determined to be authentic"))
            LoginStatus.WRONG_PASSWORD
        else if (htmlStringContent.contains("<h2>Log In Successful</h2>"))
            LoginStatus.LOGGED_IN
        else if (lt != "" || execution != "")
            LoginStatus.UNAUTHORIZED
        else LoginStatus.UNKNOWN

    object {
        var loginStatus: LoginStatus = loginStatus
        val lt: String = lt
        val execution: String = execution
    }
}

/** Get mybk access token by calling SSO login URL with mybk redirect parameter.
 * If success it will automatically redirect to mybk/stinfo and
 * call the callback with successful response.
 * Else exception will be thrown.
 *
 * Prerequisite: the app has already had a valid SSO access cookies.
 * @param callback The callback which is called when there's an login status event
 */
private fun getMybkToken(callback: ((Response) -> Unit)?) {
    Cookuest.get(
        "https://sso.hcmut.edu.vn/cas/login?service=http%3A%2F%2Fmybk.hcmut.edu.vn%2Fstinfo%2F",
        onResponse = { ssoResponse ->
            // Normally, OkHttp will follow the redirect automatically, but newer android sdks
            // doesn't permit HTTP traffic, so we've got to do the manual way: converting the
            // HTTP URLs to HTTPS URLs and then redirect.
            // This takes more work but also more secure (I suppose).
            if (ssoResponse.code == 302) {
                val redirectTicketURL: String =
                    ssoResponse.headers["Location"]
                        ?: throw Exception("Possibly expired stinfo token")
                // Verify ticket
                Cookuest.get(
                    httpToHttpsURL(redirectTicketURL),
                    onResponse = { mybkResponse ->
                        if (mybkResponse.code == 302) {
                            val mybkRedirectURL: String =
                                mybkResponse.headers["Location"]
                                    ?: throw Exception("Possibly expired stinfo token")
                            // Get access tokens (which are response's Set-Cookie headers)
                            Cookuest.get(
                                httpToHttpsURL(mybkRedirectURL),
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
