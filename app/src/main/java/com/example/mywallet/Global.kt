package com.example.mywallet

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Global {
    var UserName : String="Пользователь"
    var monthBalance : Double=0.0
    var previousBonthBalance : Double=0.0

    val dateFormat = SimpleDateFormat("yyyy.MM.dd.HH:mm:ss", Locale.getDefault())

    lateinit var operationTypes:List<OperationType>

    object globalInstance {
        var global:Global = Global()
    }

    var activity : MainActivity? =null


    fun loadSettings(activity:Activity):Unit{
        val settingsFileName="Settings.txt"
        val file = File(activity.getExternalFilesDir(null),settingsFileName)

        if (!file.exists()) {
            try {
                file.writeText(UserName)
            } catch (e: Exception) {
                activity.runOnUiThread {
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show()
                }
            }
        } else
            Global.globalInstance.global.UserName = file.readText()
    }
    fun saveSettings(activity:Activity):Unit{
        val settingsFileName="Settings.txt"
        val file = File(activity.getExternalFilesDir(null),settingsFileName)

        try {
            file.writeText(UserName)
        } catch (e: Exception) {
            activity.runOnUiThread {
                Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }


    fun loadMonthBalance(activity :Activity){
        val databaseRef=AppDatabase.getDatabase(activity)
        val balancesList=databaseRef.bankBalanceDao().getAll()
        val operationsList=databaseRef.operationDao().getAll()

        monthBalance=0.0
        for (el in balancesList)
            monthBalance+=el.BankBalance_balance

        for(el in operationsList)
            monthBalance+=el.Operation_amount
    }
    fun loadPreviousMonthBalanceByFoundedMonthBalance(activity :Activity){
        val databaseRef=AppDatabase.getDatabase(activity)
        val balancesList=databaseRef.bankBalanceDao().getAll()
        val operationsList=getOperationsForPreviousMonth(activity)

        previousBonthBalance=monthBalance
        for (el in operationsList)
            previousBonthBalance-=el.Operation_amount
    }

    fun loadMonthBalanceAndPreviousMonthBalance(activity: Activity){
        loadMonthBalance(activity)
        loadPreviousMonthBalanceByFoundedMonthBalance(activity)
    }

    fun getOperationsForPreviousMonth(activity :Activity): List<Operation> {
        val calendar = Calendar.getInstance()

        // Устанавливаем на первый день текущего месяца
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        // Переходим на один месяц назад
        calendar.add(Calendar.MONTH, -1)

        // Получаем первый день предыдущего месяца
        val startDate = dateFormat.format(calendar.time)

        // Переходим на первый день текущего месяца
        calendar.add(Calendar.MONTH, 1)

        // Получаем первый день текущего месяца как конец диапазона
        val endDate = dateFormat.format(calendar.time)

        return AppDatabase.getDatabase(activity).operationDao().getOperationsOfPeriod(startDate, endDate)
    }

    fun loadData(activity :Activity){


        loadSettings(activity)
    }
}