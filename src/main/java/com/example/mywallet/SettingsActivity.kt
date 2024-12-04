package com.example.mywallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        title=resources.getString(R.string.settings)

        findViewById<EditText>(R.id.userNameEditText).setText(intent.getStringExtra("userName"))

        findViewById<Button>(R.id.OKButton).setOnClickListener{
            val nameEditTextRef=findViewById<EditText>(R.id.userNameEditText)
            if(nameEditTextRef.text.toString().length!=0){
                val data=Intent()
                data.putExtra("userName",nameEditTextRef.text.toString())
                setResult(RESULT_OK,data)
                finish()
            }
        }
        findViewById<Button>(R.id.CancelButton).setOnClickListener{
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}