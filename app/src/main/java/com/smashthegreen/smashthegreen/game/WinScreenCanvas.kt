package com.smashthegreen.smashthegreen.game

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.smashthegreen.smashthegreen.*
import com.smashthegreen.smashthegreen.STGTools.Companion.STG_YELLOW
import com.smashthegreen.smashthegreen.STGTools.Companion.CURR_WIDTH as W
import com.smashthegreen.smashthegreen.STGTools.Companion.CURR_HEIGHT as H
import kotlin.math.roundToInt

@SuppressLint("ClickableViewAccessibility", "ViewConstructor")
class WinScreenCanvas(context: Context, private val activity: WinScreenActivity) : View(context) {


    private lateinit var banner: Drawable
    private lateinit var textbox: Drawable
    private lateinit var menu: Drawable
    private lateinit var retry: Drawable

    private lateinit var background: ShapeDrawable

    private lateinit var mode: TextDrawable
    private lateinit var time: TextDrawable
    private lateinit var name: TextDrawable
    
    init {
        setup(context)
        this.setOnTouchListener(object: OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                when(event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if(retry.bounds.contains(STGTools.fInt(event.x), STGTools.fInt(event.y))){
                            retry = ResourcesCompat.getDrawable(resources, R.drawable.retry_button_down, null)!!
                            retry.setBounds((W * 0.62).roundToInt(), (H * 0.78).roundToInt(), (W * 0.95).roundToInt(), (H * 0.98).roundToInt())
                            invalidate()
                        }

                        if(menu.bounds.contains(STGTools.fInt(event.x), STGTools.fInt(event.y))){
                            menu = ResourcesCompat.getDrawable(resources, R.drawable.menu_button_down, null)!!
                            menu.setBounds((W * 0.05).roundToInt(), (H * 0.78).roundToInt(), (W * 0.42).roundToInt(), (H * 0.99).roundToInt())
                            invalidate()
                        }

                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        if(retry.bounds.contains(STGTools.fInt(event.x), STGTools.fInt(event.y))){
                            changeActivity(1)
                            retry = ResourcesCompat.getDrawable(resources, R.drawable.retry_button, null)!!
                            retry.setBounds((W * 0.62).roundToInt(), (H * 0.78).roundToInt(), (W * 0.95).roundToInt(), (H * 0.98).roundToInt())
                            invalidate()
                        }
                        if(menu.bounds.contains(STGTools.fInt(event.x), STGTools.fInt(event.y))){
                            changeActivity(0)
                            menu = ResourcesCompat.getDrawable(resources, R.drawable.menu_button, null)!!
                            menu.setBounds((W * 0.05).roundToInt(), (H * 0.78).roundToInt(), (W * 0.42).roundToInt(), (H * 0.99).roundToInt())
                            invalidate()
                        }

                        return true
                    }
                }
                return view?.onTouchEvent(event) ?: true
            }
        })
    }

    private fun changeActivity(i: Int) {
        when(i){
            0 -> {
                activity.startActivity(Intent(context, MainMenuActivity::class.java))
                activity.overridePendingTransition(R.anim.topleftanim2, R.anim.topleftanim1)
            }
            1 -> {
                activity.startActivity(Intent(context, GameActivity::class.java))
                activity.overridePendingTransition(R.anim.rightanim, R.anim.leftanim)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background.draw(canvas!!)
        banner.draw(canvas)
        menu.draw(canvas)
        retry.draw(canvas)
        textbox.draw(canvas)
        name.draw(canvas)
        time.draw(canvas)
        mode.draw(canvas)
    }

    fun setTimeText(timeString: String){
        time.setText(timeString)
    }

    fun setNameText(nameString: String){
        name.setText(nameString)
    }

    private fun setup(context: Context) {
        background = ShapeDrawable(RectShape())
        background.setBounds(0,0,W, H)
        background.paint.color = Color.WHITE

        retry = ResourcesCompat.getDrawable(resources, R.drawable.retry_button, null)!!
        retry.setBounds((W * 0.62).roundToInt(), (H * 0.78).roundToInt(), (W * 0.95).roundToInt(), (H * 0.98).roundToInt())

        menu = ResourcesCompat.getDrawable(resources, R.drawable.menu_button, null)!!
        menu.setBounds((W * 0.05).roundToInt(), (H * 0.78).roundToInt(), (W * 0.42).roundToInt(), (H * 0.99).roundToInt())

        banner = ResourcesCompat.getDrawable(resources, R.drawable.won_image, null)!!
        banner.setBounds(W/24*5,H/20,W/24*19,H/12*5)

        textbox = ResourcesCompat.getDrawable(resources, R.drawable.timeline, null)!!
        textbox.setBounds((W * 0.11).roundToInt(), (H * 0.49).roundToInt(), (W * 0.86).roundToInt(), (H * 0.89).roundToInt())

        mode = TextDrawable(context, (SaveState.selectedMode + 1).toString(), STG_YELLOW, W*0.21F, H*0.7F, 24F, false, 2F)
        mode.setBounds((W*0.15).roundToInt(), (H*0.7).roundToInt(), (W*0.17).roundToInt(), (H*0.75).roundToInt())

        time = TextDrawable(context, "", Color.argb(255, 0, 255, 0), W*0.41F, H*0.7F, 24F, false, 0F)
        time.setBounds((W*0.4).roundToInt(), (H*0.7).roundToInt(), (W*0.6).roundToInt(), (H*0.75).roundToInt())

        name = TextDrawable(context, "", Color.argb(255, 255, 0, 0), W*0.5F, H*0.6F, 18F, true, 0F)
        name.setBounds((W*0.45).roundToInt(), (H*0.6).roundToInt(), (W*0.55).roundToInt(), (H*0.65).roundToInt())
    }



}
