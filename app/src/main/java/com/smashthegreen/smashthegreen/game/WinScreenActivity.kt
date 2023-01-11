package com.smashthegreen.smashthegreen.game

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smashthegreen.smashthegreen.STGTools
import com.smashthegreen.smashthegreen.SaveState


class WinScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        STGTools.hideSystemBars(window, findViewById(android.R.id.content))
        STGTools.suppressBackButton(this, this, onBackPressedDispatcher, this)

        val winScreenCanvas = WinScreenCanvas(this, this)
        setContentView(winScreenCanvas)

        val gameTimeLong: Long = intent.getLongExtra("TIME_LONG", 0)

        val nameString: String

        if(gameTimeLong < SaveState.times[SaveState.selectedMode] || SaveState.times[SaveState.selectedMode] < 0){
            SaveState.times[SaveState.selectedMode] = gameTimeLong
            if(!SaveState.modes[SaveState.selectedMode+1])
                if(gameTimeLong < STGTools.UNLOCK_MODE_TIMES[SaveState.selectedMode]){
                    SaveState.modes[SaveState.selectedMode + 1] = true
                    if(SaveState.selectedMode != 9){
                        Toast.makeText(this, "Unlocked mode ${SaveState.selectedMode+2}!", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "Unlocked endless mode!", Toast.LENGTH_LONG).show()
                    }
                }
            STGTools.saveSaveState(this)
            STGTools.sendSaveState(this, arrayOf(SaveState.selectedMode), false)

            nameString = "New Highscore!"

        }else{
            val time: Long = SaveState.times[SaveState.selectedMode]
            var seconds = (time / 1000).toInt()
            val minutes = seconds / 60
            seconds %= 60
            var time2 = time

            if (time >= 1000) time2 = time % 1000
            nameString = "Your highscore: " + String.format("%d:%02d:%03d", minutes, seconds, time2)
        }

        val time: Long = gameTimeLong
        var seconds = (time / 1000).toInt()
        val minutes = seconds / 60
        seconds %= 60
        var time2 = time

        if (time >= 1000) time2 = time % 1000

        val timeString = String.format("%d:%02d:%03d", minutes, seconds, time2)

        winScreenCanvas.setNameText(nameString)
        winScreenCanvas.setTimeText(timeString)

    }
}