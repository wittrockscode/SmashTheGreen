package com.smashthegreen.smashthegreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnLayout
import com.smashthegreen.smashthegreen.game.EndlessGameActivity
import com.smashthegreen.smashthegreen.game.GameActivity

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        STGTools.hideSystemBars(window, findViewById(android.R.id.content))
        STGTools.suppressBackButton(this, this, onBackPressedDispatcher)
        val mView: ConstraintLayout = findViewById(R.id.mainMenuLayout)
        mView.doOnLayout {
            STGTools.CURR_WIDTH = mView.measuredWidth
            STGTools.CURR_HEIGHT = mView.measuredHeight
        }

        findViewById<ImageButton>(R.id.ibMode).apply {
            setOnTouchListener(object: View.OnTouchListener {
                @SuppressLint("ClickableViewAccessibility")
                override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                    when(event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            setBackgroundResource(R.drawable.mode_button_down)
                        }
                        MotionEvent.ACTION_UP -> {
                            setBackgroundResource(R.drawable.mode_button)
                            startActivity(Intent(view?.context, ModeActivity::class.java))
                            overridePendingTransition(R.anim.upanim, R.anim.downanim)
                        }
                    }

                    return view?.onTouchEvent(event) ?: true
                }
            })
        }

        findViewById<ImageButton>(R.id.ibScoreboard).apply {
            setOnTouchListener(object: View.OnTouchListener {
                @SuppressLint("ClickableViewAccessibility")
                override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                    when(event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            setBackgroundResource(R.drawable.scoreboard_button_down)
                            return true
                        }
                        MotionEvent.ACTION_UP -> {
                            setBackgroundResource(R.drawable.scoreboard_button)
                            startActivity(Intent(view?.context, ScoreboardActivity::class.java))
                            overridePendingTransition(R.anim.rightanim, R.anim.leftanim)
                        }
                    }

                    return view?.onTouchEvent(event) ?: true
                }
            })
        }

        findViewById<ImageButton>(R.id.ibSettings).apply {
            setOnTouchListener(object: View.OnTouchListener {
                @SuppressLint("ClickableViewAccessibility")
                override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                    when(event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            setBackgroundResource(R.drawable.settings_button_down)
                        }
                        MotionEvent.ACTION_UP -> {
                            setBackgroundResource(R.drawable.settings_button)
                            startActivity(Intent(view?.context, ScoreboardActivity::class.java))
                            overridePendingTransition(R.anim.rightanim2, R.anim.leftanim2)
                        }
                    }

                    return view?.onTouchEvent(event) ?: true
                }
            })
        }

        findViewById<ImageButton>(R.id.ibGo).apply {
            setOnTouchListener(object: View.OnTouchListener {
                @SuppressLint("ClickableViewAccessibility")
                override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                    when(event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            setBackgroundResource(R.drawable.go_button_down)
                        }
                        MotionEvent.ACTION_UP -> {
                            setBackgroundResource(R.drawable.go_button)
                            if(SaveState.endless) startActivity(Intent(view?.context, EndlessGameActivity::class.java))
                            else startActivity(Intent(view?.context, GameActivity::class.java))
                            overridePendingTransition(R.anim.upanim2, R.anim.downanim2)
                        }
                    }

                    return view?.onTouchEvent(event) ?: true
                }
            })
        }
    }



}