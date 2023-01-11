package com.smashthegreen.smashthegreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ModeActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        STGTools.hideSystemBars(window, findViewById(android.R.id.content))
        STGTools.suppressBackButton(this, this, onBackPressedDispatcher)
        val modeCanvas = ModeCanvas(this, this)
        setContentView(modeCanvas)
    }



}