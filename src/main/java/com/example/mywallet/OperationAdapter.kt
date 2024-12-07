package com.example.mywallet

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentContainerView

class OperationAdapter (
    private val context: Activity,
    private var Operations: ArrayList<Operation>,
    private val selectionChangeListener: OnSelectionChangeListener,
    private val database: AppDatabase,
) : BaseAdapter(){
    private val selectedItems = mutableSetOf<Int>()
    var isSelectionMode = false

    override fun getCount(): Int {
        return Operations.size
    }

    override fun getItem(position: Int): Any {
        return Operations[position]
    }

    override fun getItemId(position: Int): Long {
        return Operations[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View=convertView ?: LayoutInflater.from(context).inflate(R.layout.operation_list_item_layout,parent,false)

        val OperationRef=getItem(position) as Operation

        view.findViewById<TextView>(R.id.OperationAmountTextView).text=OperationRef.Operation_amount.toString()
        view.findViewById<TextView>(R.id.OperationDescriptionTextView).text=OperationRef.Operation_description
        view.findViewById<TextView>(R.id.OperationDateTextView).text=OperationRef.Operation_date


        if (selectedItems.contains(position)) {
            view.setBackgroundColor(Color.LTGRAY) // Например, серый фон для выделенного элемента
        } else {
            view.setBackgroundColor(Color.TRANSPARENT) // Обычный фон
        }

        Thread{
            val typename:String=database.operationTypeDao().getById(OperationRef.OperationType_id.toLong()).OperationType_name
            val bankname:String=database.bankBalanceDao().getById(OperationRef.BankBalance_id.toLong()).BankBalance_name
            context.runOnUiThread {
                view.findViewById<TextView>(R.id.OperationOperationTypeTextView).text=typename
                view.findViewById<TextView>(R.id.OperationBankBalanceTypeTextView).text=bankname
            }
        }.start()

        return view
    }

    fun removeAt(position: Int){
        Operations.removeAt(position)
    }

    fun updateData(newOperations: ArrayList<Operation>){
        Operations=newOperations
        notifyDataSetChanged()
    }

    fun toggleSelection(position: Int){
        if(isSelectionMode){
            if(selectedItems.contains(position))
                selectedItems.remove(position)
            else
                selectedItems.add(position)

            notifyDataSetChanged()
            selectionChangeListener.onSelectionChanged(selectedItems.isNotEmpty())
        }
    }

    fun getSelectedItems():Set<Int> = selectedItems

    fun clearSelection(){
        Thread{
            for (el in selectedItems)
                database.operationDao().delete(Operations[el])
            for(el in selectedItems)
                Operations.removeAt(el)

            selectedItems.clear()
            context.runOnUiThread {
                notifyDataSetChanged()
                selectionChangeListener.onSelectionChanged(false)
            }
        }.start()
    }

    fun enterSelectionMode(){
        isSelectionMode=true
    }

    fun exitSelectionMode(){
        isSelectionMode=false
        clearSelection()
    }
}