package com.example.mywallet

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.core.R
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class Global {
    public var UserName : String="Пользователь"

    object globalInstance {
        var global:Global = Global()
    }


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

    fun loadData(activity :Activity){


        loadSettings(activity)
    }
}