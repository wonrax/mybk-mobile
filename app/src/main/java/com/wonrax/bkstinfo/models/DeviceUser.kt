package com.wonrax.bkstinfo.models

import com.wonrax.bkstinfo.network.Cookuest
import com.wonrax.bkstinfo.network.Response
import com.wonrax.bkstinfo.network.utils.HtmlUtils
import com.wonrax.bkstinfo.network.utils.HttpUtils
import okhttp3.FormBody
import okhttp3.RequestBody

enum class LoginStatus {
    LOGGED_IN, WRONG_PASSWORD, UNAUTHORIZED, UNKNOWN
}

class DeviceUser {
    companion object {
        /**
         * Single Sign-on URL of HCMUT's Central Authentication Service (CAS)
         *
         * This URL serves to purposes:
         * - GET the HTML content, which contains "lt" and "execution". These fields need to be
         * sent along with the login credentials.
         * - POST and use the HTML response to check the validity of the provided credentials.
         */
        private const val SSO_URL = "https://sso.hcmut.edu.vn/cas/login"

        /**
         * GET this URL after a successful SSO login to be redirected to mybk stinfo,
         * from which you can get the mybk stinfo access token from.
         */
        private const val SSO_MYBK_REDIRECT_URL =
            "https://sso.hcmut.edu.vn/cas/login?service=http%3A%2F%2Fmybk.hcmut.edu.vn%2Fstinfo%2F"

        /**
         * The string that appeared in the HTML response when the credentials are confirmed
         * to be valid
         */
        private const val HTML_WRONG_CREDENTIAL = "<h2>Log In Successful</h2>"

        /**
         * The string that appeared in the HTML response when the credentials are confirmed
         * to be invalid
         */
        private const val HTML_LOGIN_SUCCESS =
            "The credentials you provided cannot be determined to be authentic"

        /**
         * Sign in function, will login into SSO with provided credentials and get the access token
         * from mybk if the credentials are valid. The callback is called to reflect UI change.
         * @param username Username of the user's credential
         * @param password Password of the user's credential
         * @param onLoggedIn The callback which is called when there's an login status event
         */
        private fun proceedSigningIn(
            username: String,
            password: String,
            onLoggedIn: ((LoginStatus) -> Unit)?
        ) {
            Cookuest.get(
                SSO_URL,
                onResponse = { ssoResponse: Response ->

                    val getToken = {
                        getMybkToken { response ->
                            val token = HtmlUtils.getHtmlElementValue(
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
                                SSO_URL,
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

        /**
         * Check login status based on the response HTML from SSO.
         *
         * Lambda params: response - the response HTML from SSO
         */
        private val checkLoginStatus = { response: Response ->

            // Parse 'lt' and 'execution' in HTML form rendered in the request. We'll need this for
            // submitting along login credentials
            val htmlStringContent = response.body
            val lt: String =
                HtmlUtils.getHtmlElementValue(name = "lt", input = htmlStringContent) ?: ""
            val execution: String = HtmlUtils.getHtmlElementValue(
                name = "execution",
                input = htmlStringContent
            ) ?: ""
            val loginStatus =
                if (htmlStringContent.contains(HTML_LOGIN_SUCCESS))
                    LoginStatus.WRONG_PASSWORD
                else if (htmlStringContent.contains(HTML_WRONG_CREDENTIAL))
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

        /**
         * Get mybk access token by calling SSO login URL with mybk redirect parameter.
         * If success it will automatically redirect to mybk/stinfo and
         * call the callback with successful response.
         * Else exception will be thrown.
         *
         * Prerequisite: the app has already had a valid SSO access cookies.
         * @param callback The callback which is called when there's an login status event
         */
        private fun getMybkToken(callback: ((Response) -> Unit)?) {
            // TODO Make and catch the exception class
            Cookuest.get(
                SSO_MYBK_REDIRECT_URL,
                onResponse = { ssoResponse ->
                    // Normally, OkHttp will follow the redirect automatically, but newer
                    // android sdks doesn't permit HTTP traffic, so we've got to do the manual way:
                    // converting the HTTP URLs to HTTPS URLs and then redirect.
                    // This takes more work but also more secure (I suppose).
                    if (ssoResponse.code == 302) {
                        val redirectTicketURL: String =
                            ssoResponse.headers["Location"]
                                ?: throw Exception("Possibly expired stinfo token")
                        // Verify ticket
                        Cookuest.get(
                            HttpUtils.httpToHttpsURL(redirectTicketURL),
                            onResponse = { mybkResponse ->
                                if (mybkResponse.code == 302) {
                                    val mybkRedirectURL: String =
                                        mybkResponse.headers["Location"]
                                            ?: throw Exception("Possibly expired stinfo token")
                                    // Get access tokens (which are response's Set-Cookie headers)
                                    Cookuest.get(
                                        HttpUtils.httpToHttpsURL(mybkRedirectURL),
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
    }

    /**
     * Login with SSO credentials. If the process completes successfully, the access token cookies
     * access tokens cookies of both SSO and mybk will be saved inside OkHttp's CookieJar. Mybk
     * cookies will be saved inside SharedPrefs. SSO cookies, however, will not be saved since
     * they're session cookies and considered non-persistent.
     *
     * The callback will be call with LoginStatus enum as the argument.
     *
     * If either the username or password is null, the function will get the
     * credentials from SharedPrefs.
     *
     * @param username Username of the user's credential
     * @param password Password of the user's credential
     * @param onLoggedIn The callback which is called when there's an login status event
     */
    fun signIn(
        username: String? = null,
        password: String? = null,
        onLoggedIn: ((LoginStatus) -> Unit)? = null
    ) {
        if (username != null && password != null) {
            proceedSigningIn(username, password, onLoggedIn)
        } else {
            // TODO Check if mybk tokens are still valid or Get saved credentials from SharedPrefs
            return
        }
    }
}
