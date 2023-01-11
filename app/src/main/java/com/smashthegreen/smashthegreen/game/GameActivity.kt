package com.smashthegreen.smashthegreen.game

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.smashthegreen.smashthegreen.R
import com.smashthegreen.smashthegreen.STGTools

class GameActivity : AppCompatActivity() {

    lateinit var timerView: TextView

    var startTime: Long = 0
    var gameTime: Long = 0

    val timerHandler: Handler = Handler(Looper.getMainLooper())
    private val timerRunnable: Runnable = object: Runnable{
        override fun run() {
            val millis: Long = System.currentTimeMillis() - startTime
            var seconds: Int = (millis/1000).toInt()
            val minutes: Int = seconds/60
            seconds %= 60

            timerView.text = String.format("%d:%02d:%03d", minutes, seconds, millis)
            gameTime = millis
            timerHandler.postDelayed(this, 50)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        STGTools.hideSystemBars(window, findViewById(android.R.id.content))
        STGTools.suppressBackButton(this, this, onBackPressedDispatcher, this)

        val gameCanvas = GameCanvas(this, this)
        timerView = TextView(this).apply {
            text = "0:00:000"
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17F)
        }
        val fl = FrameLayout(this)
        fl.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        setContentView(fl)

        gameCanvas.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val lp: FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL)
        lp.setMargins(0, STGTools.CURR_WIDTH /32,0,0)

        fl.addView(gameCanvas)
        fl.addView(timerView, lp)
        startTime = System.currentTimeMillis()
        timerHandler.postDelayed(timerRunnable, 0)


    }

    fun changeActivity(won: Boolean){
        if(won){
            val intent = Intent(this, WinScreenActivity::class.java)
            intent.putExtra("TIME_LONG", gameTime)
            startActivity(intent)
            overridePendingTransition(R.anim.rightanim2, R.anim.leftanim2)
            timerHandler.removeCallbacks(timerRunnable)
            finish()
        }else{
            //TODO:
            //lose screen
        }
    }



}