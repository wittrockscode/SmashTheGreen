package com.smashthegreen.smashthegreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.smashthegreen.smashthegreen.STGTools.Companion.fInt
import kotlin.math.roundToInt

@SuppressLint("ClickableViewAccessibility", "ViewConstructor")
class ModeCanvas(context: Context?, private val activity: ModeActivity) : View(context) {

    private lateinit var background: ShapeDrawable
    private lateinit var backgroundImage: Drawable
    private lateinit var backButton: Drawable
    private lateinit var endless: ShapeDrawable
    private lateinit var modeTitle1: ShapeDrawable
    private lateinit var modeTitle2: ShapeDrawable
    private var choice: Array<ShapeDrawable> = Array(10){ShapeDrawable(RectShape())}
    private var w: Int = 0
    private var h: Int = 0


    init {
        this.setOnTouchListener(object: OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                when(event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        handleTouchEvent(event)
                    }
                }

                return view?.onTouchEvent(event) ?: true
            }
        })
    }

    private fun handleTouchEvent(event: MotionEvent){

        if(backButton.bounds.contains(fInt(event.x), fInt(event.y))){
            context.startActivity(Intent(context, MainMenuActivity::class.java))
            activity.overridePendingTransition(R.anim.upanim2, R.anim.downanim2)
        }else{
            for(i in choice.indices){
                if(choice[i].bounds.contains(fInt(event.x), fInt(event.y))){
                    if(SaveState.modes[i]){
                        if(SaveState.selectedMode != i){
                            choice[SaveState.selectedMode].paint.color = Color.WHITE
                            choice[i].paint.color = Color.GREEN
                            SaveState.selectedMode = i
                            invalidate()
                        }
                    }
                }
            }
            if(SaveState.modes[10]){
                if(endless.bounds.contains(fInt(event.x), fInt(event.y))){
                    SaveState.endless = SaveState.endless.not()
                    if(SaveState.endless) endless.paint.color = Color.GREEN
                    else endless.paint.color = Color.WHITE
                    invalidate()
                }
            }
        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background.draw(canvas!!)
        modeTitle1.draw(canvas)
        modeTitle2.draw(canvas)
        endless.draw(canvas)
        for(c in choice){
            c.draw(canvas)
        }
        backgroundImage.draw(canvas)
        backButton.draw(canvas)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        w = this.measuredWidth
        h = this.measuredHeight
        setup()
    }


    private fun setup(){
        Log.d("setup","SETUP")
        val res: Resources = this.resources
        backButton = ResourcesCompat.getDrawable(res, R.drawable.back_button, null)!!
        backButton.setBounds(w / 3, h / 128 * 110, w / 3 * 2, h / 128*131)

        backgroundImage = ResourcesCompat.getDrawable(res, R.drawable.mode_menu, null)!!
        backgroundImage.setBounds(0,0,w,h)

        background = ShapeDrawable(RectShape())
        background.setBounds(0,0,w,h)
        background.paint.color = Color.BLACK

        modeTitle1 = ShapeDrawable(RectShape())
        modeTitle1.setBounds(((0.2*width).roundToInt()),0,((0.8*width).roundToInt()),((0.2*height).roundToInt()))
        modeTitle1.paint.color = Color.WHITE

        modeTitle2 = ShapeDrawable(RectShape())
        modeTitle2.setBounds(((0.32*width).roundToInt()),((0.19*height).roundToInt()),((0.6*width).roundToInt()),((0.25*height).roundToInt()))
        modeTitle2.paint.color = Color.WHITE

        endless = ShapeDrawable(RectShape())
        endless.setBounds(((0.2*width).roundToInt()),((0.72*height).roundToInt()),((0.52*width).roundToInt()),((0.85*height).roundToInt()))

        if(SaveState.modes[10]){
            if(SaveState.endless){
                endless.paint.color = Color.GREEN
            }else{
                endless.paint.color = Color.WHITE
            }
        }else{
            endless.paint.color = Color.DKGRAY
        }


        for(i in choice.indices){
            if(SaveState.modes[i]){
                choice[i].paint.color = Color.WHITE
            }else{
                choice[i].paint.color = Color.DKGRAY
            }
        }
        choice[SaveState.selectedMode].paint.color = Color.GREEN


        //SET CHOICES
        choice[0].setBounds(0,((height*0.2).roundToInt()),((0.32*width).roundToInt()),((0.41*height).roundToInt()))
        choice[1].setBounds(((0.32*width).roundToInt()),((height*0.26).roundToInt()),((0.71*width).roundToInt()),((0.41*height).roundToInt()))
        choice[2].setBounds(((0.71*width).roundToInt()),((height * 0.26).roundToInt()),width,((0.41*height).roundToInt()))
        choice[3].setBounds(((0.71*width).roundToInt()),((0.41*height).roundToInt()),width,((0.56*height).roundToInt()))
        choice[4].setBounds(((0.32*width).roundToInt()),((0.41*height).roundToInt()),((0.71*width).roundToInt()),((0.56*height).roundToInt()))
        choice[5].setBounds(0,((0.41*height).roundToInt()),((0.32*width).roundToInt()),((0.56*height).roundToInt()))
        choice[6].setBounds(0,((0.56*height).roundToInt()),((0.32*width).roundToInt()),((0.72*height).roundToInt()))
        choice[7].setBounds(((0.32*width).roundToInt()),((0.56*height).roundToInt()),((0.71*width).roundToInt()),((0.72*height).roundToInt()))
        choice[8].setBounds(((0.71*width).roundToInt()),((0.56*height).roundToInt()),width,((0.72*height).roundToInt()))
        choice[9].setBounds(((0.52*width).roundToInt()),((0.72*height).roundToInt()),width,((0.9*height).roundToInt()))

    }


}