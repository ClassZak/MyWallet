package com.example.mywallet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.Serializable

class AddBankBalanceTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bank_balance_type)


        title="Новый тип счёта"

        findViewById<Button>(R.id.OKButton).setOnClickListener{
            val editTextRef=findViewById<EditText>(R.id.BankBalanceTypeEditText)
            if(editTextRef.text.toString().length!=0){
                val data= Intent()
                data.putExtra("newBankBalanceType",BankBalanceType(BankBalanceType_name = editTextRef.text.toString()) as Serializable?)
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