package com.example.mywallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class OperationTypeActivity() : AppCompatActivity(), OnSelectionChangeListener {

    override fun onSelectionChanged(hasSelectedItems: Boolean) {
        findViewById<ImageButton>(R.id.DeleteButtonOperationType).visibility = if (hasSelectedItems) View.VISIBLE else View.GONE
    }


    private lateinit var adapter: OperationTypeAdapter
    private lateinit var operationTypeDao: OperationTypeDao

    val addNewOperationTypeActivityLauncher: ActivityResultLauncher<Intent>? = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            result->
        if(result.resultCode== RESULT_OK){
            addOperationType(result.data?.getSerializableExtra("newOperationType")!! as OperationType)
            Global.globalInstance.global.activity?.updateBalance()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operation_type)

        title="Типы операций"

        // Инициализация базы данных и DAO
        operationTypeDao = AppDatabase.getDatabase(this).operationTypeDao()

        // Инициализируем кастомный адаптер с пустым списком
        adapter = OperationTypeAdapter(
            this, arrayListOf(),
            this,
            AppDatabase.getDatabase(this)
        )

        var listView=findViewById<ListView>(R.id.operationTypesListView)

        listView.adapter = adapter

        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Загружаем данные из базы данных
        loadOperationTypes()

        findViewById<Button>(R.id.addButton).setOnClickListener {
            addNewOperationTypeActivityLauncher?.launch(Intent(this,AddOperationTypeActivity::class.java))
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


        findViewById<ImageButton>(R.id.DeleteButtonOperationType).setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            // Логика удаления выбранных элементов
            adapter.clearSelection() // Очистка выбора после удаления
            updateButtonsVisibility() // Обновление видимости кнопок
            Global.globalInstance.global.activity?.updateBalance()
        }
    }

    private fun updateButtonsVisibility() {
        findViewById<ImageButton>(R.id.DeleteButtonOperationType).visibility = if (adapter.getSelectedItems().isEmpty()) View.INVISIBLE else View.VISIBLE
    }

    private fun loadOperationTypes() {
        Thread {
            val operationTypes = operationTypeDao.getAll()

            val operationTypesArrayList:ArrayList<OperationType> = ArrayList<OperationType>()
            for(el in operationTypes){
                operationTypesArrayList.add(el)
            }

            runOnUiThread{
                adapter.updateData(operationTypesArrayList)
            }
        }.start()
    }

    private fun addOperationType(name: String) {
        Thread{
            operationTypeDao.insert(OperationType(OperationType_name = name))
            loadOperationTypes() // Обновляем список после добавления
        }.start()
    }
    private fun addOperationType(operationType: OperationType) {
        Thread {
            operationTypeDao.insert(operationType)
            loadOperationTypes() // Обновляем список после добавления
        }.start()
    }

    private fun deleteOperationType(operationType: OperationType) {
        Thread {
            operationTypeDao.delete(operationType)
            loadOperationTypes() // Обновляем список после удаления
        }.start()
    }
}