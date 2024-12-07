package com.example.mywallet

import android.app.Activity
import android.content.ClipDescription
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

class BankBalanceActivity : AppCompatActivity(),OnSelectionChangeListener {
    override fun onSelectionChanged(hasSelectedItems: Boolean) {
        findViewById<ImageButton>(R.id.DeleteButtonBankBalance).visibility = if (hasSelectedItems) View.VISIBLE else View.GONE
    }

    private lateinit var adapter: BankBalanceAdapter
    private lateinit var BankBalanceDao: BankBalanceDao

    val addNewBankBalanceActivityLauncher: ActivityResultLauncher<Intent>? = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            result->
        if(result.resultCode== RESULT_OK) {
            addBankBalance(result.data?.getSerializableExtra("newBankBalance")!! as BankBalance)
            Global.globalInstance.global.activity?.updateBalance()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bank_balance)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        title="Счета"
        BankBalanceDao = AppDatabase.getDatabase(this).bankBalanceDao()

        // Инициализируем кастомный адаптер с пустым списком
        adapter = BankBalanceAdapter(
            this, arrayListOf(),
            this,
            AppDatabase.getDatabase(this)
        )

        var listView=findViewById<ListView>(R.id.BankBalancesListView)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        loadBankBalances()
        findViewById<Button>(R.id.addButton).setOnClickListener {
            addNewBankBalanceActivityLauncher?.launch(Intent(this,AddBankBalanceActivity::class.java))
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

        findViewById<ImageButton>(R.id.DeleteButtonBankBalance).setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            // Логика удаления выбранных элементов
            adapter.clearSelection() // Очистка выбора после удаления
            updateButtonsVisibility() // Обновление видимости кнопок
            Thread{
            Global.globalInstance.global.loadMonthBalanceAndPreviousMonthBalance(Global.globalInstance.global.activity as Activity)}
        }
    }

    private fun updateButtonsVisibility() {
        findViewById<ImageButton>(R.id.DeleteButtonBankBalance).visibility = if (adapter.getSelectedItems().isEmpty()) View.INVISIBLE else View.VISIBLE
    }

    private fun loadBankBalances() {
        Thread {
            val BankBalanceTypes = BankBalanceDao.getAll()

            val BankBalancesArrayList:ArrayList<BankBalance> = ArrayList<BankBalance>()
            for(el in BankBalanceTypes){
                BankBalancesArrayList.add(el)
            }

            runOnUiThread{
                adapter.updateData(BankBalancesArrayList)
            }
        }.start()
    }


    private fun addBankBalance(name: String,description: String,balance:Double,typeId:Int,startBalance: Double) {
        Thread{
            BankBalanceDao.insert(BankBalance(BankBalance_name = name, BankBalance_descption = description, BankBalance_balance = balance, BankBalanceType_id = typeId, BankBalance_start_balance = startBalance))
            loadBankBalances() // Обновляем список после добавления
        }.start()
    }
    private fun addBankBalance(BankBalance: BankBalance) {
        Thread {
            BankBalanceDao.insert(BankBalance)
            loadBankBalances() // Обновляем список после добавления
            Global.globalInstance.global.loadMonthBalanceAndPreviousMonthBalance(Global.globalInstance.global.activity as Activity)
        }.start()
    }

    private fun deleteBankBalance(BankBalance: BankBalance) {
        Thread {
            BankBalanceDao.delete(BankBalance)
            loadBankBalances() // Обновляем список после удаления
        }.start()
    }
}