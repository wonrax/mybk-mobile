package com.wonrax.bkstinfo.models

import com.wonrax.bkstinfo.network.Cookuest
import com.wonrax.bkstinfo.network.Response
import com.wonrax.bkstinfo.network.await
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

    /** Login request returns forbidden code because of too many tries */
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
    private var stinfoToken: String? = null

    fun init() {
        username = EncryptedStorage.get(SHARE_PREFS_USERNAME_KEY)
        password = EncryptedStorage.get(SHARE_PREFS_PASSWORD_KEY)
    }

    fun getUsername(): String? {
        return username
    }

    fun getPassword(): String? {
        return password
    }

    fun getStInfoToken(): String? {
        return stinfoToken
    }

    /**
     * Sign in function, will login into SSO with provided credentials and get the access token
     * from mybk if the credentials are valid.
     * @param username Username of the user's credential
     * @param password Password of the user's credential
     */
    private suspend fun ssoSignIn(
        username: String,
        password: String
    ): SSOState {
        val ssoResponse = Cookuest.get(SSO_URL).await()
        val checkLoggedIn = checkSSOLoginStatus(ssoResponse)

        return when (checkLoggedIn.loginStatus) {
            SSOState.UNAUTHORIZED -> {
                // Prepare form body containing required credentials to login
                val body: RequestBody = FormBody.Builder().apply {
                    add("_eventId", "submit")
                    add("execution", checkLoggedIn.execution)
                    add("lt", checkLoggedIn.lt)
                    add("username", username)
                    add("password", password)
                }.build()
                val ssoWithCredentialResponse = Cookuest.post(
                    SSO_URL,
                    requestBody = body
                ).await()
                return checkSSOLoginStatus(ssoWithCredentialResponse).loginStatus
            }
            else -> checkLoggedIn.loginStatus
        }
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
     */
    suspend fun getMybkToken(): MybkState {
        val ssoResponse = Cookuest.get(SSO_MYBK_REDIRECT_URL).await()

        // Normally, OkHttp will follow the redirect automatically, but newer
        // android sdks doesn't permit HTTP traffic, so we've got to do the manual way:
        // converting the HTTP URLs to HTTPS URLs and then redirect.
        // This takes more work but also more secure (I suppose).
        return if (ssoResponse.code == 302) {
            val redirectTicketURL: String =
                ssoResponse.headers["Location"] ?: ""
            // Verify ticket
            Cookuest.get(HttpUtils.httpToHttpsURL(redirectTicketURL)).await()
            getStinfoToken()
        } else { // Invalid cookies, require SSO re-login
            MybkState.SSO_REQUIRED
        }
    }

    private suspend fun getStinfoToken(): MybkState {
        val response = Cookuest.get(STINFO_URL).await()

        val token = HtmlUtils.getHtmlElementValue(
            input = response.body,
            tag = "meta",
            name = "_token",
            attr = "content"
        )

        return if (token != null) {
            stinfoToken = token
            MybkState.LOGGED_IN
        } else
            MybkState.UNKNOWN
    }

    /**
     * Login with SSO credentials. If the process completes successfully, the access token
     * cookies of SSO will be saved inside OkHttp's CookieJar. The credentials will then be saved
     * inside local storage SharedPreferences.
     *
     * If either the username or password is null, this function will get the
     * credentials from SharedPrefs.
     *
     * @param username Username of the user's credential
     * @param password Password of the user's credential
     */
    suspend fun signIn(
        username: String? = null,
        password: String? = null
    ): SSOState {
        return if (username != null && password != null) {
            val ssoState = ssoSignIn(username, password)
            if (ssoState == SSOState.LOGGED_IN) updateCredentialsStore(username, password)
            ssoState
        } else {
            // Avoid race condition
            val savedUsername = this.username
            val savedPassword = this.password

            if (savedPassword == null || savedUsername == null)
                SSOState.NO_CREDENTIALS
            else
                ssoSignIn(savedUsername, savedPassword)
        }
    }

    /**
     * Update credential in both memory and SharedPreferences
     */
    private fun updateCredentialsStore(username: String?, password: String?) {
        this.username = username
        this.password = password
        EncryptedStorage.set(SHARE_PREFS_USERNAME_KEY, username)
        EncryptedStorage.set(SHARE_PREFS_PASSWORD_KEY, password)
    }
}
