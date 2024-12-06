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

class AddOperationTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_operation_type)


        title="Новый тип операции"

        findViewById<Button>(R.id.OKButton).setOnClickListener{
            val editTextRef=findViewById<EditText>(R.id.OperationTypeEditText)
            if(editTextRef.text.toString().length!=0){
                val data= Intent()
                data.putExtra("newOperationType",OperationType(OperationType_name = editTextRef.text.toString()) as Serializable?)
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