package com.example.mywallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class OperationActivity : AppCompatActivity(),OnSelectionChangeListener {
    override fun onSelectionChanged(hasSelectedItems: Boolean) {
        findViewById<ImageButton>(R.id.DeleteButtonOperation).visibility = if (hasSelectedItems) View.VISIBLE else View.GONE
    }

    private lateinit var adapter: OperationAdapter
    private lateinit var OperationDao: OperationDao

    val addNewOperationActivityLauncher: ActivityResultLauncher<Intent>? = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            result->
        if(result.resultCode== RESULT_OK)
        {
            val operation=result.data?.getSerializableExtra("newOperation")!! as Operation
            addOperation(operation)
            Global.globalInstance.global.activity?.updateBalance()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_operation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        title="Операции"
        OperationDao = AppDatabase.getDatabase(this).operationDao()

        // Инициализируем кастомный адаптер с пустым списком
        adapter = OperationAdapter(
            this, arrayListOf(),
            this,
            AppDatabase.getDatabase(this)
        )

        var listView=findViewById<ListView>(R.id.OperationsListView)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        loadOperations()
        findViewById<Button>(R.id.addButton).setOnClickListener {
            addNewOperationActivityLauncher?.launch(Intent(this,AddOperationActivity::class.java))
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            if (!adapter.isSelectionMode) {
                adapter.enterSelectionMode()
            }
            adapter.toggleSelection(position)
            updateButtonsVisibility()
            true
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            if (adapter.isSelectionMode) {
                adapter.toggleSelection(position)
                updateButtonsVisibility()
            }
        }

        findViewById<ImageButton>(R.id.DeleteButtonOperation).setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            // Логика удаления выбранных элементов
            adapter.clearSelection() // Очистка выбора после удаления
            updateButtonsVisibility() // Обновление видимости кнопок
            Global.globalInstance.global.activity?.updateBalance()
        }
    }

    private fun updateButtonsVisibility() {
        findViewById<ImageButton>(R.id.DeleteButtonOperation).visibility = if (adapter.getSelectedItems().isEmpty()) View.INVISIBLE else View.VISIBLE
    }

    private fun loadOperations() {
        Thread {
            val OperationTypes = OperationDao.getAll()

            val OperationsArrayList:ArrayList<Operation> = ArrayList<Operation>()
            for(el in OperationTypes){
                OperationsArrayList.add(el)
            }

            runOnUiThread{
                adapter.updateData(OperationsArrayList)
            }
        }.start()
    }


    private fun addOperation(
        type: Int, description:String,amount:Double,date:String,balance: Int
    ) {
        Thread{
            OperationDao.insert(Operation(
                Operation_description = description,
                OperationType_id = type,
                Operation_amount = amount,
                Operation_date = date,
                BankBalance_id = balance
            ))
            loadOperations() // Обновляем список после добавления
        }.start()
    }
    private fun addOperation(operation: Operation) {
        Thread {
            OperationDao.insert(operation)
            loadOperations() // Обновляем список после добавления
        }.start()
    }

    private fun deleteOperation(Operation: Operation) {
        Thread {
            OperationDao.delete(Operation)
            loadOperations() // Обновляем список после удаления
        }.start()
    }
}