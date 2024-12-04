package com.example.mywallet

import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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


class MainActivity : AppCompatActivity() {

    val settingLauncher : ActivityResultLauncher<Intent?>? = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        if(result.resultCode== RESULT_OK) {
            Global.globalInstance.global.UserName =
                result.data?.getStringExtra("userName") ?: Global.globalInstance.global.UserName
            Global.globalInstance.global.saveSettings(this)
        }
    }
    val aboutActivityLauncher:ActivityResultLauncher<Intent?>? = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result->{

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settingsMenuItem -> settingLauncher?.launch(Intent(this,SettingsActivity::class.java).putExtra("userName",Global.globalInstance.global.UserName))
            R.id.aboutMenuItem->aboutActivityLauncher?.launch(Intent(this,AboutActivity::class.java))
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

        findViewById<TextView>(R.id.text2).setText(Global.globalInstance.global.UserName)
    }
}