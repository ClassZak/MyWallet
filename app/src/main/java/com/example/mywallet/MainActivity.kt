package com.example.mywallet

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mywallet.R
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    val settingLauncher : ActivityResultLauncher<Intent>? = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        if(result.resultCode== RESULT_OK) {
            Global.globalInstance.global.UserName =
                result.data?.getStringExtra("userName") ?: Global.globalInstance.global.UserName
            Global.globalInstance.global.saveSettings(this)
        }
    }
    val aboutActivityLauncher:ActivityResultLauncher<Intent>? = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result->{
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings,menu)
        menu?.findItem(R.id.changeThemeMenuItem)?.setTitle(if(isDarkMode(this))
            R.string.changeToLightTheme;
        else
            R.string.changeToDarkTheme)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settingsMenuItem -> settingLauncher?.launch(Intent(this,SettingsActivity::class.java).putExtra("userName",Global.globalInstance.global.UserName))
            R.id.aboutMenuItem->aboutActivityLauncher?.launch(Intent(this,AboutActivity::class.java))
            R.id.changeThemeMenuItem -> changeTheme()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        title = resources.getString(R.string.myWalletTitle)
        Global.globalInstance.global.activity=this

        updateBalance()

        findViewById<Button>(R.id.operationTypeButton).setOnClickListener{
            startActivity(Intent(this,OperationTypeActivity::class.java))
        }
        findViewById<Button>(R.id.balanceTypeButton).setOnClickListener{
            startActivity(Intent(this,BankBalanceTypeActivity::class.java))
        }
        findViewById<Button>(R.id.balanceButton).setOnClickListener{
            startActivity(Intent(this,BankBalanceActivity::class.java))
        }
        findViewById<Button>(R.id.historyButton).setOnClickListener{
            startActivity(Intent(this,OperationActivity::class.java))
        }
    }

    fun updateBalance(){
        Thread({
            Global.globalInstance.global.loadPreviousMonthBalanceByFoundedMonthBalance(this)
            runOnUiThread{
                findViewById<TextView>(R.id.monthBalance).text=Global.globalInstance.global.monthBalance.toString()
                findViewById<TextView>(R.id.prevMonthBalance).text=Global.globalInstance.global.previousBonthBalance.toString()
            }
        }).start()
    }

    fun isDarkMode(context: Context =this): Boolean {
        return context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
    fun changeTheme(context:Context=this){
        if(isDarkMode(context))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}