package com.smashthegreen.smashthegreen.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smashthegreen.smashthegreen.STGTools

class EndlessGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        STGTools.hideSystemBars(window, findViewById(android.R.id.content))
        STGTools.suppressBackButton(this, this, onBackPressedDispatcher, this)

        val endlessGame = EndlessGame(this, this)
        setContentView(endlessGame)
    }
}