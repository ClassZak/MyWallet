package com.example.mywallet

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddOperationActivity : AppCompatActivity() {
    val database=AppDatabase.getDatabase(this)
    lateinit var bankBalances:List<BankBalance>
    lateinit var operationTypes:List<OperationType>

    private lateinit var typeAdapter: ArrayAdapter<String>
    private lateinit var balanceAdapter: ArrayAdapter<String>

    private lateinit var resultDateTime: String

    private var selectedDate: Calendar = Calendar.getInstance()
    private var selectedTime: Calendar = Calendar.getInstance()


    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            selectedDate.set(year, month, dayOfMonth)
            updateResult()
        }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            selectedTime.set(Calendar.MINUTE, minute)
            updateResult()
        }, selectedTime.get(Calendar.HOUR_OF_DAY), selectedTime.get(Calendar.MINUTE), true)

        timePickerDialog.show()
    }

    private fun updateResult() {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd.HH:mm:ss", Locale.getDefault())
        val combinedCalendar = Calendar.getInstance().apply {
            set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH),
                selectedTime.get(Calendar.HOUR_OF_DAY), selectedTime.get(Calendar.MINUTE), 0)
        }
        resultDateTime = "${dateFormat.format(combinedCalendar.time)}"

        findViewById<TextView>(R.id.AddOperationResultTimeDateTextView).text="Выбранное время: "+resultDateTime

        Toast.makeText(this,resultDateTime,Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_operation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        Thread{
            operationTypes=database.operationTypeDao().getAll()
            bankBalances=database.bankBalanceDao().getAll()

            runOnUiThread {
                loadBalanceList()
                loadOperationList()
            }
        }.start()

        findViewById<Button>(R.id.AddOperationDateButton).setOnClickListener{
            showDatePicker()
        }
        findViewById<Button>(R.id.AddOperationTimeButton).setOnClickListener{
            showTimePicker()
        }
        title="Новая операция"
        findViewById<Button>(R.id.OKButton).setOnClickListener{
            try {
                val val1=findViewById<EditText>(R.id.AddOperationAmountEditText).text.toString().toDouble()
                val val2=findViewById<EditText>(R.id.AddOperationDescriptionEditText).text.toString()
                val val3=resultDateTime
                val val4=operationTypes[findViewById<Spinner>(R.id.AddOperationOperationTypeSpinner).selectedItemId.toInt()].id
                val val5=bankBalances[findViewById<Spinner>(R.id.AddOperationBankBalanceSpinner).selectedItemId.toInt()].id

                if(val3.length!=0 && val2.length!=0){
                    val data= Intent()
                    data.putExtra("newOperation",Operation(
                        Operation_date = val3,
                        Operation_amount = val1,
                        Operation_description = val2,
                        OperationType_id = val4,
                        BankBalance_id = val5) as Serializable?
                    )
                    setResult(RESULT_OK,data)
                    finish()
                }
            }
            catch (e:Exception){
                runOnUiThread{
                    Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
                }
            }
        }
        findViewById<Button>(R.id.CancelButton).setOnClickListener{
            setResult(RESULT_CANCELED)
            finish()
        }

    }


    fun getBankIdFromName(name:String):Int{
        return bankBalances.find { x -> x.BankBalance_name==name }?.id ?: -1
    }
    fun getOperationTypeIdFromName(name:String):Int{
        return bankBalances.find { x -> x.BankBalance_name==name }?.id ?: -1
    }
    fun loadBalanceList(){
        var spinnerRef : Spinner=findViewById<Spinner>(R.id.AddOperationBankBalanceSpinner)

        val names:ArrayList<String> =ArrayList<String>()
        for(el in bankBalances)
            names.add(el.BankBalance_name)

        balanceAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,names)
        balanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRef.adapter = balanceAdapter
    }
    fun loadOperationList(){
        var spinnerRef : Spinner=findViewById<Spinner>(R.id.AddOperationOperationTypeSpinner)

        val names:ArrayList<String> =ArrayList<String>()
        for(el in operationTypes)
            names.add(el.OperationType_name)

        typeAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,names)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRef.adapter = typeAdapter
    }
}