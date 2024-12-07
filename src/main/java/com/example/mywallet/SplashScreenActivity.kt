package com.example.mywallet

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.WorkDuration
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Thread(Runnable{
            Global.globalInstance.global.loadMonthBalanceAndPreviousMonthBalance(this)

            Thread.sleep(1000)
            scaleView(findViewById(R.id.walletImage),1.0f,0.5f,1.0f,0.5f,750)
            Thread.sleep(750)
            scaleView(findViewById(R.id.walletImage),0.5f,1.0f,0.5f,1.0f,750)
            Thread.sleep(750)
            scaleView(findViewById(R.id.walletImage),1.0f,0.75f,1.0f,0.75f,750)
            Thread.sleep(750)
            scaleView(findViewById(R.id.walletImage),0.75f,1.0f,0.75f,1.0f,750)


            Global.globalInstance.global.loadData(this)
            runOnUiThread{
                showHelloMessage()
            }

            goToMainActivity()
        }).start()
    }

    fun showHelloMessage(){
        val textViewRef=
        findViewById<TextView>(R.id.helloText)
        textViewRef.setText(resources.getText(R.string.helloMessage).toString()+Global.globalInstance.global.UserName)
        textViewRef.visibility=View.VISIBLE
    }

    fun goToMainActivity(){
        Thread.sleep(2000)
        startActivity(Intent(this,MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Задержка на 2 секунды перед переходом к основному экрану
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Закрываем SplashActivity
        }, 2000) // 2000 миллисекунд = 2 секунды
    }*/


    fun scaleView(v: View, startXScale: Float, endXScale: Float,startYScale: Float, endYScale: Float, duration: Long=1000) {
        val anim: Animation = ScaleAnimation(
            startXScale, endXScale,  // Start and end values for the X axis scaling
            startYScale, endYScale,  // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 0.5f
        ) // Pivot point of Y scaling
        anim.fillAfter = false // Needed to keep the result of the animation
        anim.duration = duration
        v.startAnimation(anim)
    }
}