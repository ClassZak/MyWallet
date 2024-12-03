package com.example.mywallet

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.example.mywallet.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Thread(kotlinx.coroutines.Runnable {
            startMainAnimation()
        }).start()
    }

    fun startMainAnimation(){
        val imageViewRef:ImageView=findViewById(R.id.walletImage)
        runOnUiThread {
            val scaleXUp = ObjectAnimator.ofFloat(imageViewRef, "scaleX", 1f, 1.5f)
            val scaleYUp = ObjectAnimator.ofFloat(imageViewRef, "scaleY", 1f, 1.5f)

            // Анимация уменьшения
            val scaleXDown = ObjectAnimator.ofFloat(imageViewRef, "scaleX", 1.5f, 1f)
            val scaleYDown = ObjectAnimator.ofFloat(imageViewRef, "scaleY", 1.5f, 1f)

            // Создаем набор анимаций
            val animatorSet = AnimatorSet()
            animatorSet.play(scaleXUp).with(scaleYUp).after(scaleYDown).with(scaleXDown)
            animatorSet.duration = 2000 // Установите продолжительность анимации
            animatorSet.start()
        }
        Thread.sleep(5000)
        startActivity(Intent(this,MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
    }
}