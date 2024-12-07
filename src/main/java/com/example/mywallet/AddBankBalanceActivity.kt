package com.example.mywallet

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.Serializable

class AddBankBalanceActivity : AppCompatActivity() {
    val database=AppDatabase.getDatabase(this)
    lateinit var bankBalanceTypes:List<BankBalanceType>

    private lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_bank_balance)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        Thread{
            bankBalanceTypes=database.bankBalanceTypeDao().getAll()
            runOnUiThread {
                loadBalanceTypeList()
            }
        }.start()
        title="Новый счёт"
        findViewById<Button>(R.id.OKButton).setOnClickListener{
            try {
                val val1=findViewById<EditText>(R.id.AddBankBalanceNameEditText).text.toString()
                val val2=findViewById<EditText>(R.id.AddBankBalanceDescriptionEditText).text.toString()
                val val3=findViewById<EditText>(R.id.AddBankBalanceBalanceEditText).text.toString().toDouble()
                val val4=bankBalanceTypes[findViewById<Spinner>(R.id.AddBankBalanceSpinner).selectedItemId.toInt()].id

                if(val1.length!=0 && val2.length!=0){
                    val data= Intent()
                    data.putExtra("newBankBalance",BankBalance(BankBalance_name = val1, BankBalance_descption = val2, BankBalance_start_balance = val3, BankBalanceType_id = val4, BankBalance_balance = val3) as Serializable?)
                    setResult(RESULT_OK,data)
                    finish()
                }
            }
            catch (e:Exception){

            }
        }
        findViewById<Button>(R.id.CancelButton).setOnClickListener{
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    fun getIdFromName(name:String):Int{
        return bankBalanceTypes.find { x -> x.BankBalanceType_name==name }?.id ?: -1
    }
    fun loadBalanceTypeList(){
        var spinnerRef : Spinner?=findViewById<Spinner>(R.id.AddBankBalanceSpinner)
        spinnerRef =findViewById<Spinner>(R.id.AddBankBalanceSpinner)

        val names:ArrayList<String> =ArrayList<String>()
        for(el in bankBalanceTypes)
            names.add((el.BankBalanceType_name) )

        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, names)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRef.adapter = adapter
    }
}