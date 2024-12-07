package com.example.mywallet

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentContainerView

class BankBalanceAdapter (
    private val context: Activity,
    private var BankBalances: ArrayList<BankBalance>,
    private val selectionChangeListener: OnSelectionChangeListener,
    private val database: AppDatabase,
) : BaseAdapter(){
    private val selectedItems = mutableSetOf<Int>()
    var isSelectionMode = false

    override fun getCount(): Int {
        return BankBalances.size
    }

    override fun getItem(position: Int): Any {
        return BankBalances[position]
    }

    override fun getItemId(position: Int): Long {
        return BankBalances[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View=convertView ?: LayoutInflater.from(context).inflate(R.layout.bank_balance_list_item_layout,parent,false)

        val bankBalanceRef=getItem(position) as BankBalance

        view.findViewById<TextView>(R.id.BankBalanceNameTextView).text=bankBalanceRef.BankBalance_name
        view.findViewById<TextView>(R.id.BankBalanceDescriptionTextView).text=bankBalanceRef.BankBalance_descption
        view.findViewById<TextView>(R.id.BankBalanceBalanceTextView).text=bankBalanceRef.BankBalance_balance.toString()


        if (selectedItems.contains(position)) {
            view.setBackgroundColor(Color.LTGRAY) // Например, серый фон для выделенного элемента
        } else {
            view.setBackgroundColor(Color.TRANSPARENT) // Обычный фон
        }

        Thread{
            val name:String=database.bankBalanceTypeDao().getById(bankBalanceRef.BankBalanceType_id.toLong()).BankBalanceType_name
            context.runOnUiThread {
                view.findViewById<TextView>(R.id.BankBalanceBankBalanceTypeTextView).text=name
            }
        }.start()

        return view
    }

    fun removeAt(position: Int){
        BankBalances.removeAt(position)
    }

    fun updateData(newBankBalances: ArrayList<BankBalance>){
        BankBalances=newBankBalances
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
                database.bankBalanceDao().delete(BankBalances[el])
            for(el in selectedItems)
                BankBalances.removeAt(el)

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