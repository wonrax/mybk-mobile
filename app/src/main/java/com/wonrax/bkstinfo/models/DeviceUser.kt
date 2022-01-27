package com.wonrax.bkstinfo.models

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.wonrax.bkstinfo.network.Cookuest
import com.wonrax.bkstinfo.network.Response
import com.wonrax.bkstinfo.network.utils.HtmlUtils
import com.wonrax.bkstinfo.network.utils.HttpUtils
import okhttp3.FormBody
import okhttp3.RequestBody

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
 * from which you can get the mybk access token from.
 */
private const val SSO_MYBK_REDIRECT_URL =
    "https://sso.hcmut.edu.vn/cas/login?service=http%3A%2F%2Fmybk.hcmut.edu.vn%2Fstinfo%2F"

/**
 * HTML response of this URL contains stinfo token inside a meta tag if authorized.
 */
private const val STINFO_URL =
    "https://mybk.hcmut.edu.vn/stinfo/"

/**
 * The string that appeared in the HTML response when the credentials are confirmed
 * to be valid
 */
private const val HTML_LOGIN_SUCCESS = "<h2>Log In Successful</h2>"

/**
 * The string that appeared in the HTML response when the credentials are confirmed
 * to be invalid
 */
private const val HTML_WRONG_CREDENTIAL =
    "The credentials you provided cannot be determined to be authentic"

private const val SHARED_PREFERENCES_NAME = "UserCredentials"
private const val SHARE_PREFS_USERNAME_KEY = "username"
private const val SHARE_PREFS_PASSWORD_KEY = "password"

enum class SSOState {
    /** Initial state */
    UNAUTHORIZED,

    /** Can't find any saved credentials in local storage */
    NO_CREDENTIALS,

    /** Log in successfully */
    LOGGED_IN,

    /** SSO Login required */
    WRONG_PASSWORD,

    /** Login request returns code forbidden because of too many tries */
    TOO_MANY_TRIES,

    /** Unexpected flow */
    UNKNOWN
}

enum class MybkState {
    /** Get stinfo token successfully */
    LOGGED_IN,

    /** SSO Login required */
    SSO_REQUIRED,

    /** Unexpected flow */
    UNKNOWN
}

object DeviceUser {
    private var username: String? = null
    private var password: String? = null
    private lateinit var sharedPrefs: SharedPreferences

    fun init(context: Context) {
        if (this::sharedPrefs.isInitialized) return
        synchronized(this) {
            // Double check
            if (this::sharedPrefs.isInitialized) {
                return
            }
            sharedPrefs = context.getSharedPreferences(
                SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE
            )
            username = sharedPrefs.getString(SHARE_PREFS_USERNAME_KEY, null)
            password = sharedPrefs.getString(SHARE_PREFS_PASSWORD_KEY, null)
        }
    }

    fun getUsername(): String? {
        return username
    }

    fun getPassword(): String? {
        return password
    }

    /**
     * Sign in function, will login into SSO with provided credentials and get the access token
     * from mybk if the credentials are valid. The callback is called to reflect UI change.
     * @param username Username of the user's credential
     * @param password Password of the user's credential
     * @param onLoggedIn The callback which is called when there's an login status event
     */
    private fun ssoSignIn(
        username: String,
        password: String,
        onLoggedIn: ((SSOState) -> Unit)?
    ) {
        Cookuest.get(
            SSO_URL,
            onResponse = { ssoResponse: Response ->

                val checkLoggedIn = checkSSOLoginStatus(ssoResponse)

                when (checkLoggedIn.loginStatus) {
                    SSOState.UNAUTHORIZED -> {
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
                                    checkSSOLoginStatus(ssoWithCredentialResponse).loginStatus
                                onLoggedIn?.invoke(loginStatus)
                            }
                        )
                    }

                    else -> {
                        onLoggedIn?.invoke(checkLoggedIn.loginStatus)
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
    private val checkSSOLoginStatus = { response: Response ->

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
            if (response.code == 403)
                SSOState.TOO_MANY_TRIES
            else if (htmlStringContent.contains(HTML_WRONG_CREDENTIAL))
                SSOState.WRONG_PASSWORD
            else if (htmlStringContent.contains(HTML_LOGIN_SUCCESS))
                SSOState.LOGGED_IN
            else if (lt != "" || execution != "")
                SSOState.UNAUTHORIZED
            else SSOState.UNKNOWN

        object {
            var loginStatus: SSOState = loginStatus
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
    fun getMybkToken(callback: ((MybkState) -> Unit)? = null) {
        Cookuest.get(
            SSO_MYBK_REDIRECT_URL,
            onResponse = { ssoResponse ->
                // Normally, OkHttp will follow the redirect automatically, but newer
                // android sdks doesn't permit HTTP traffic, so we've got to do the manual way:
                // converting the HTTP URLs to HTTPS URLs and then redirect.
                // This takes more work but also more secure (I suppose).
                if (ssoResponse.code == 302) {
                    val redirectTicketURL: String =
                        ssoResponse.headers["Location"] ?: ""
                    // Verify ticket
                    Cookuest.get(
                        HttpUtils.httpToHttpsURL(redirectTicketURL),
                        onResponse = { getStinfoToken(callback) }
                    )
                } else { // Invalid cookies, require SSO re-login
                    callback?.invoke(MybkState.SSO_REQUIRED)
                }
            }
        )
    }

    private fun getStinfoToken(callback: ((MybkState) -> Unit)?) {
        Cookuest.get(
            STINFO_URL,
            onResponse = { response ->
                val token = HtmlUtils.getHtmlElementValue(
                    input = response.body,
                    tag = "meta",
                    name = "_token",
                    attr = "content"
                )
                if (token != null)
                    callback?.invoke(MybkState.LOGGED_IN)
                else
                    callback?.invoke(MybkState.UNKNOWN)
                print("Mybk Token: ")
                println(token)
            }
        )
    }

    /**
     * Login with SSO credentials. If the process completes successfully, the access token
     * cookies of SSO will be saved inside OkHttp's CookieJar. The credentials will then be saved
     * inside local storage SharedPreferences.
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
        onLoggedIn: ((SSOState) -> Unit)? = null
    ) {
        if (username != null && password != null) {
            ssoSignIn(username, password) { ssoState ->
                onLoggedIn?.invoke(ssoState)
                if (ssoState == SSOState.LOGGED_IN)
                    updateCredentialsStore(username, password)
            }
        } else {
            // Avoid race condition
            val savedUsername = this.username
            val savedPassword = this.password

            if (savedPassword == null || savedUsername == null)
                onLoggedIn?.invoke(SSOState.NO_CREDENTIALS)
            else
                ssoSignIn(savedUsername, savedPassword, onLoggedIn)
        }
    }

    /**
     * Update credential in both memory and SharedPreferences
     */
    private fun updateCredentialsStore(username: String?, password: String?) {
        this.username = username
        this.password = password
        sharedPrefs.edit(commit = true) {
            if (username != null)
                putString(SHARE_PREFS_USERNAME_KEY, username)
            else
                remove(SHARE_PREFS_USERNAME_KEY)

            if (password != null)
                putString(SHARE_PREFS_PASSWORD_KEY, password)
            else
                remove(SHARE_PREFS_PASSWORD_KEY)
        }
    }
}
