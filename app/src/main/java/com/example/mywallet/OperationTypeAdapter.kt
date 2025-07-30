package com.example.mywallet


import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class OperationTypeAdapter(
private val context: Activity,
private var operationTypes: ArrayList<OperationType>,
private val selectionChangeListener: OnSelectionChangeListener,
private val database: AppDatabase
) : BaseAdapter() {
    private val selectedItems = mutableSetOf<Int>()
    var isSelectionMode = false

    override fun getCount(): Int {
        return operationTypes.size
    }

    override fun getItem(position: Int): OperationType {
        return operationTypes[position]
    }

    override fun getItemId(position: Int): Long {
        return operationTypes[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.operation_type_list_item_layout, parent, false)

        val operationTypeNameTextView = view.findViewById<TextView>(R.id.operation_type_title_textview)
        operationTypeNameTextView.text = getItem(position).OperationType_name

        // Выделение элемента (можно добавить визуальные изменения)
        if (selectedItems.contains(position)) {
            view.setBackgroundColor(Color.LTGRAY) // Например, серый фон для выделенного элемента
        } else {
            view.setBackgroundColor(Color.TRANSPARENT) // Обычный фон
        }

        return view
    }

    fun removeAt(position: Int){
        operationTypes.removeAt(position)
    }

    fun updateData(newOperationTypes: ArrayList<OperationType>) {
        operationTypes = newOperationTypes
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
                database.operationTypeDao().delete(operationTypes[el])
            for(el in selectedItems)
                removeAt(el)


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