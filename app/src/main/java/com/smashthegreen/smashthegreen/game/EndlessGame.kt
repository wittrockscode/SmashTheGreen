package com.smashthegreen.smashthegreen.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.smashthegreen.smashthegreen.R
import com.smashthegreen.smashthegreen.STGTools.Companion.CURR_HEIGHT
import com.smashthegreen.smashthegreen.STGTools.Companion.CURR_WIDTH
import com.smashthegreen.smashthegreen.STGTools.Companion.STG_BLUE
import com.smashthegreen.smashthegreen.STGTools.Companion.STG_GREEN
import com.smashthegreen.smashthegreen.STGTools.Companion.STG_RED
import com.smashthegreen.smashthegreen.STGTools.Companion.STG_YELLOW
import com.smashthegreen.smashthegreen.STGTools.Companion.fInt
import com.smashthegreen.smashthegreen.SaveState
import kotlin.collections.ArrayList
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@SuppressLint("ClickableViewAccessibility", "ViewConstructor")
class EndlessGame(context: Context?, private val activity: EndlessGameActivity) : View(context) {

    private var speed: Int = 1
    private var realSpeed: Int = 1
    private var greensSmashed = 0

    private var angle: Double = Math.toRadians(0.0)
    private val angleRotate: Boolean = true

    private var tileList: ArrayList<ShapeDrawable> = ArrayList(0)
    private var heartList: ArrayList<Drawable> = ArrayList(0)
    private val toRemove: ArrayList<ShapeDrawable> = ArrayList(0)
    private val toAdd: ArrayList<ShapeDrawable> = ArrayList(0)

    private lateinit var textPaint: Paint

    private var greenPercent: Int = 25

    private var starttime: Long = 0

    private var isPlaying: Boolean = false

    val tilesx: Array<Int> = arrayOf(4, 4, 5, 5, 6, 6, 7, 7, 8, 8)
    val tilesy: Array<Int> = arrayOf(5, 6, 6, 7, 7, 8, 8, 9, 9, 10)



    private val tileWidth = CURR_WIDTH/tilesx[SaveState.selectedMode]
    private val tileHeight = CURR_HEIGHT/tilesy[SaveState.selectedMode]

    private val timerHandler: Handler = Handler(Looper.getMainLooper())
    private val update: Thread = Thread(object: Runnable{
        override fun run() {
            if(isPlaying){
                val t: Long = System.currentTimeMillis() - starttime

                if(t > 1000 + (350 * speed)){
                    realSpeed++
                    starttime = System.currentTimeMillis()

                    if(angleRotate){
                        when((0..3).random()){
                            0 -> {
                                angle = Math.toRadians(0.0)
                                speed = realSpeed
                            }
                            1 -> {
                                angle = Math.toRadians(90.0)
                                speed = realSpeed + (CURR_WIDTH/CURR_HEIGHT)
                            }
                            2 -> {
                                angle = Math.toRadians(180.0)
                                speed = realSpeed
                            }
                            3 -> {
                                angle = Math.toRadians(270.0)
                                speed = realSpeed + (CURR_WIDTH/CURR_HEIGHT)
                            }
                            else -> {
                                angle = Math.toRadians(0.0)
                                speed = realSpeed
                            }
                        }

                    }
                }

                greenPercent = 25 - realSpeed
                if(greenPercent <= 5) greenPercent = 5

                for(tiles in tileList){
                    tiles.bounds.offset((speed * sin(angle)).roundToInt(), (speed * cos(angle)).roundToInt())
                }
                toRemove.clear()
                toAdd.clear()
                for(tile in tileList){
                    if(tile.bounds.top > CURR_HEIGHT + tileHeight){
                        val newTile = ShapeDrawable(RectShape())
                        newTile.bounds = tile.bounds
                        newTile.bounds.offset(0,
                            -((tilesy[SaveState.selectedMode] + 2) * tileHeight)
                        )
                        newTile.paint.color = calcColor()
                        toAdd.add(newTile)
                        toRemove.add(tile)
                        continue
                    }
                    if(tile.bounds.bottom <  0 - tileHeight){
                        val newTile = ShapeDrawable(RectShape())
                        newTile.bounds = tile.bounds
                        newTile.bounds.offset(0,
                            ((tilesy[SaveState.selectedMode] + 2) * tileHeight)
                        )
                        newTile.paint.color = calcColor()
                        toAdd.add(newTile)
                        toRemove.add(tile)
                        continue
                    }
                    if(tile.bounds.left < 0 - tileWidth){
                        val newTile = ShapeDrawable(RectShape())
                        newTile.bounds = tile.bounds
                        newTile.bounds.offset(
                            ((tilesx[SaveState.selectedMode] + 2) * tileWidth), 0
                        )
                        newTile.paint.color = calcColor()
                        toAdd.add(newTile)
                        toRemove.add(tile)
                        continue
                    }
                    if(tile.bounds.right > CURR_WIDTH + tileWidth){
                        val newTile = ShapeDrawable(RectShape())
                        newTile.bounds = tile.bounds
                        newTile.bounds.offset(
                            -((tilesx[SaveState.selectedMode] + 2) * tileWidth), 0
                        )
                        newTile.paint.color = calcColor()
                        toAdd.add(newTile)
                        toRemove.add(tile)
                        continue
                    }
                }
                tileList.removeAll(toRemove.toSet())
                tileList.addAll(toAdd.toSet())
                toRemove.clear()
                toAdd.clear()

                invalidate()
                timerHandler.postDelayed(this, 1)

            }
        }

    })


    init {
        isPlaying = true
        setup()

        starttime = System.currentTimeMillis()

        timerHandler.postDelayed(update, 0)


        this.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object: OnTouchListener {
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


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(isPlaying){
            for(tile in tileList){
                tile.draw(canvas!!)
            }
            for(heart in heartList){
                heart.draw(canvas!!)
            }
            canvas!!.drawText(greensSmashed.toString(),
                (CURR_WIDTH/20).toFloat(), (CURR_HEIGHT/20).toFloat(), textPaint)
        }
    }

    private fun handleTouchEvent(event: MotionEvent) {
        for(tile in tileList){
            if(tile.bounds.contains(fInt(event.x), fInt(event.y))){
                Log.d("COLOR: ", tile.paint.color.toString())
                when (tile.paint.color) {
                    STG_GREEN -> {
                        tile.paint.color = Color.DKGRAY
                        Log.d("SMASHED: ", "GREEN")
                        greensSmashed++
                        continue
                    }
                    Color.DKGRAY, Color.BLACK -> {
                        continue
                    }
                    else -> {
                        Log.d("SMASHED: ", "OTHER")
                        tile.paint.color = Color.BLACK
                        if(heartList.size > 1) heartList.removeAt(heartList.size - 1)
                        else heartList.clear(); lose()

                        continue
                    }
                }
            }
        }
        tileList.removeAll(toRemove.toSet())
    }

    private fun lose() {
        //TODO:
        //lose

    }

    private fun setup(){

        tileList.clear()
        generateTiles()

        for(i in 0..3){
            val heart = ResourcesCompat.getDrawable(resources, R.drawable.heart, null)!!
            heart.setBounds(CURR_WIDTH - ((CURR_WIDTH/20) * (i+2)),
            CURR_WIDTH/20, CURR_WIDTH - ((CURR_WIDTH/20) * (i+1)),
            CURR_WIDTH/20 + CURR_WIDTH/20)

            heartList.add(heart)
        }

        textPaint = Paint()
        textPaint.color = Color.BLACK
        textPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16F,resources.displayMetrics)


    }

    private fun generateTiles() {
        if(tileList.isNotEmpty()) tileList.clear()

        for(j in -1..tilesy[SaveState.selectedMode]){
            for(i in -1..tilesx[SaveState.selectedMode]){
                val tile = ShapeDrawable(RectShape())
                tile.setBounds(tileWidth * i, tileHeight * j, tileWidth * (i+1), tileHeight * (j+1))
                when((0..3).random()){
                    0 -> tile.paint.color = STG_GREEN
                    1 -> tile.paint.color = STG_YELLOW
                    2 -> tile.paint.color = STG_BLUE
                    3 -> tile.paint.color = STG_RED
                }
                tileList.add(tile)
            }
        }

    }

    private fun calcColor(): Int {
        return when((1..100).random()){
            in 1..greenPercent -> STG_GREEN
            in 26..50 -> STG_YELLOW
            in 51..75 -> STG_BLUE
            in 76..100 -> STG_RED
            else -> {
                when((0..3).random()){
                    0 -> STG_GREEN
                    1 -> STG_YELLOW
                    2 -> STG_BLUE
                    3 -> STG_RED
                    else -> STG_GREEN
                }
            }
        }
    }


}