package com.example.mywallet

import android.media.MediaScannerConnection
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.example.mywallet.R
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun loadUserName(){
        try {
            val testFile = File(getExternalFilesDir(null), "Cuentas.csv")
            if (!testFile.exists()) testFile.createNewFile()
            Toast.makeText(this, testFile.path, Toast.LENGTH_LONG).show()

            // Adds a line to the file
            val writer = BufferedWriter(FileWriter(testFile, true))
            writer.write("This is a test file.")
            writer.close()
            // Refresh the data so it can seen when the device is plugged in a
            // computer. You may have to unplug and replug the device to see the
            // latest changes. This is not necessary if the user should not modify
            // the files.
            MediaScannerConnection.scanFile(
                this, arrayOf(testFile.toString()),
                null,
                null
            )
        } catch (e: IOException) {
            //Log.e("ReadWriteFile", "Unable to write to the TestFile.txt file.");
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_LONG).show()
        }
    }
}