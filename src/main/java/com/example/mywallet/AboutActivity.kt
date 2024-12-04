package com.example.mywallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        title=getString(R.string.aboutTitle)

        findViewById<Button>(R.id.backButton).setOnClickListener{
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}