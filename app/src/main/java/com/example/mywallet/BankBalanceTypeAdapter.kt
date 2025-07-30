package com.example.mywallet


import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class BankBalanceTypeAdapter(
private val context: Activity,
private var BankBalanceTypes: ArrayList<BankBalanceType>,
private val selectionChangeListener: OnSelectionChangeListener,
private val database: AppDatabase
) : BaseAdapter() {
    private val selectedItems = mutableSetOf<Int>()
    var isSelectionMode = false

    override fun getCount(): Int {
        return BankBalanceTypes.size
    }

    override fun getItem(position: Int): BankBalanceType {
        return BankBalanceTypes[position]
    }

    override fun getItemId(position: Int): Long {
        return BankBalanceTypes[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.bank_balance_type_list_item_layout, parent, false)

        val BankBalanceTypeNameTextView = view.findViewById<TextView>(R.id.bankbalance_type_title_textview)
        BankBalanceTypeNameTextView.text = getItem(position).BankBalanceType_name

        // Выделение элемента (можно добавить визуальные изменения)
        if (selectedItems.contains(position)) {
            view.setBackgroundColor(Color.LTGRAY) // Например, серый фон для выделенного элемента
        } else {
            view.setBackgroundColor(Color.TRANSPARENT) // Обычный фон
        }

        return view
    }

    fun removeAt(position: Int){
        BankBalanceTypes.removeAt(position)
    }

    fun updateData(newBankBalanceTypes: ArrayList<BankBalanceType>) {
        BankBalanceTypes = newBankBalanceTypes
        notifyDataSetChanged()
    }

    fun toggleSelection(position: Int) {
        if (isSelectionMode) {
            if (selectedItems.contains(position)) {
                selectedItems.remove(position)
            } else {
                selectedItems.add(position)
            }
            notifyDataSetChanged()
            selectionChangeListener.onSelectionChanged(selectedItems.isNotEmpty()) // Уведомляем об изменении выделения
        }
    }

    fun getSelectedItems(): Set<Int> = selectedItems

    fun clearSelection() {

        Thread{
            for(el in selectedItems)
                database.bankBalanceTypeDao().delete(BankBalanceTypes[el])
            for(el in selectedItems)
                BankBalanceTypes.removeAt(el)
            selectedItems.clear()
            context.runOnUiThread {
                notifyDataSetChanged()
                selectionChangeListener.onSelectionChanged(false) // Уведомляем, что выделение очищено
            }
        }.start()
    }

    fun enterSelectionMode() {
        isSelectionMode = true
    }

    fun exitSelectionMode() {
        isSelectionMode = false
        clearSelection()
    }
}