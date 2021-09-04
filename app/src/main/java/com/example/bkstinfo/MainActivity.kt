package com.example.bkstinfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.bkstinfo.utils.Cookuest
import com.example.bkstinfo.utils.OkHttpClientSingleton

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
        Cookuest.get(url)
    }
}


