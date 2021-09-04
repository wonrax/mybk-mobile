package com.wonrax.bkstinfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.wonrax.bkstinfo.utils.Cookuest
import com.wonrax.bkstinfo.utils.OkHttpClientSingleton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init singleton to enable cookies
        OkHttpClientSingleton.init(applicationContext)

        signIn()
    }

    private fun signIn() {
        val textView = findViewById<TextView>(R.id.httpResponse)
        val url = "https://sso.hcmut.edu.vn/cas/login"
        Cookuest.get(url, onResponse = { Cookuest.get(url) })
    }
}


