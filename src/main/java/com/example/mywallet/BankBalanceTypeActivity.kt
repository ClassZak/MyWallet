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

class BankBalanceTypeActivity() : AppCompatActivity(), OnSelectionChangeListener {

    override fun onSelectionChanged(hasSelectedItems: Boolean) {
        findViewById<ImageButton>(R.id.DeleteButtonBankBalanceType).visibility = if (hasSelectedItems) View.VISIBLE else View.GONE
    }


    private lateinit var adapter: BankBalanceTypeAdapter
    private lateinit var BankBalanceTypeDao: BankBalanceTypeDao

    val addNewBankBalanceTypeActivityLauncher: ActivityResultLauncher<Intent>? = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            result->
        if(result.resultCode== RESULT_OK){
            addBankBalanceType(result.data?.getSerializableExtra("newBankBalanceType")!! as BankBalanceType)
            Global.globalInstance.global.activity?.updateBalance()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_balance_type)

        title="Типы счетов"

        // Инициализация базы данных и DAO
        BankBalanceTypeDao = AppDatabase.getDatabase(this).bankBalanceTypeDao()

        // Инициализируем кастомный адаптер с пустым списком
        adapter = BankBalanceTypeAdapter(
            this, arrayListOf(),
            this,
            AppDatabase.getDatabase(this)
        )

        var listView=findViewById<ListView>(R.id.BankBalanceTypesListView)

        listView.adapter = adapter

        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Загружаем данные из базы данных
        loadBankBalanceTypes()

        findViewById<Button>(R.id.addButton).setOnClickListener {
            addNewBankBalanceTypeActivityLauncher?.launch(Intent(this,AddBankBalanceTypeActivity::class.java))
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


        findViewById<ImageButton>(R.id.DeleteButtonBankBalanceType).setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            // Логика удаления выбранных элементов
            adapter.clearSelection() // Очистка выбора после удаления
            updateButtonsVisibility() // Обновление видимости кнопок
            Global.globalInstance.global.activity?.updateBalance()
        }
    }

    private fun updateButtonsVisibility() {
        findViewById<ImageButton>(R.id.DeleteButtonBankBalanceType).visibility = if (adapter.getSelectedItems().isEmpty()) View.INVISIBLE else View.VISIBLE
    }

    private fun loadBankBalanceTypes() {
        Thread {
            val BankBalanceTypes = BankBalanceTypeDao.getAll()

            val BankBalanceTypesArrayList:ArrayList<BankBalanceType> = ArrayList<BankBalanceType>()
            for(el in BankBalanceTypes){
                BankBalanceTypesArrayList.add(el)
            }

            runOnUiThread{
                adapter.updateData(BankBalanceTypesArrayList)
            }
        }.start()
    }

    private fun addBankBalanceType(name: String) {
        Thread{
            BankBalanceTypeDao.insert(BankBalanceType(BankBalanceType_name = name))
            loadBankBalanceTypes() // Обновляем список после добавления
        }.start()
    }
    private fun addBankBalanceType(BankBalanceType: BankBalanceType) {
        Thread {
            BankBalanceTypeDao.insert(BankBalanceType)
            loadBankBalanceTypes() // Обновляем список после добавления
        }.start()
    }

    private fun deleteBankBalanceType(BankBalanceType: BankBalanceType) {
        Thread {
            BankBalanceTypeDao.delete(BankBalanceType)
            loadBankBalanceTypes() // Обновляем список после удаления
        }.start()
    }
}