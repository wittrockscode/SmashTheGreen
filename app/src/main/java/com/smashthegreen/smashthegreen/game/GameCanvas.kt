package com.smashthegreen.smashthegreen.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import com.smashthegreen.smashthegreen.STGTools.Companion.fInt
import android.view.MotionEvent
import android.view.View
import com.smashthegreen.smashthegreen.STGTools
import com.smashthegreen.smashthegreen.SaveState
import kotlin.math.roundToInt

@SuppressLint("ViewConstructor", "ClickableViewAccessibility")
class GameCanvas(context: Context?, private val activity: GameActivity) : View(context) {


    private var tileCount: Int = 0
    private var cGreen: Int = 0
    private var cYellow: Int = 0
    private var cBlue: Int = 0
    private var cRed: Int = 0

    private var greenCount: Int = 0

    private var greensSmashed: Int = 0

    private var colArray: Array<Int>
    private var tileArray: Array<ShapeDrawable>

    init {
        val tilesx: Array<Int> = arrayOf(4, 4, 5, 5, 6, 6, 7, 7, 8, 8)
        val tilesCountX: Int = tilesx[SaveState.selectedMode]
        val tilesy: Array<Int> = arrayOf(5, 6, 6, 7, 7, 8, 8, 9, 9, 10)
        val tilesCountY: Int = tilesy[SaveState.selectedMode]

        val tileWidth: Double = STGTools.CURR_WIDTH / tilesCountX.toDouble()
        val tileHeight: Double = STGTools.CURR_HEIGHT / tilesCountY.toDouble()
        tileCount = tilesCountX * tilesCountY

        calculateColors()

        colArray = Array(tileCount){i ->
            getIndex(i)
        }
        colArray.shuffle()

        tileArray = Array(tileCount){ ShapeDrawable(RectShape()) }

        var index = 0
        for(i in 0 until tilesCountY){
            for(j in 0 until tilesCountX){
                if(index < tileArray.size){
                    tileArray[index].setBounds((j*tileWidth).roundToInt(),
                        (i*tileHeight).roundToInt(), ((j+1)*tileWidth).roundToInt(), ((i+1)*tileHeight).roundToInt())

                    var color: Int = Color.WHITE
                    when(colArray[index]){
                        0 -> color = STGTools.STG_GREEN
                        1 -> color = STGTools.STG_YELLOW
                        2 -> color = STGTools.STG_BLUE
                        3 -> color = STGTools.STG_RED
                    }
                    tileArray[index].paint.color = color
                    index++
                }
            }
        }


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


    private fun win(){
        activity.changeActivity(true)
    }

    private fun lose(){
        activity.changeActivity(false)
    }

    private fun handleTouchEvent(event: MotionEvent){
        if(tileArray.isNotEmpty()){
            for(i in tileArray.indices){
                if(tileArray[i].bounds.contains(fInt(event.x), fInt(event.y))){
                    if(colArray[i] == 0){
                        tileArray[i].paint.color = Color.DKGRAY
                        colArray[i] = -1
                        greensSmashed++
                        if(greensSmashed >= greenCount) win()
                    }else if(colArray[i] != -1){
                        tileArray[i].paint.color = Color.BLACK
                        lose()
                    }
                    invalidate()
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for(tile in tileArray){
            tile.draw(canvas!!)
        }
    }

    private fun calculateColors(){
        val c: Int = tileCount / 4
        cGreen = c
        cYellow = c
        cBlue = c
        cRed = c
        while (cGreen + cYellow + cBlue + cRed != tileCount){
            when((0..3).random()){
                0 -> cGreen++
                1 -> cYellow++
                2 -> cBlue++
                3 -> cRed++
                else -> cGreen++
            }
        }
        greenCount = cGreen
    }

    private fun getIndex(i: Int): Int{
        return if(i < cGreen) 0
        else if(i >= cGreen && i < cGreen+cYellow) 1
        else if(i >= cGreen+cYellow && i < cGreen+cYellow+cBlue) 2
        else 3
    }
}