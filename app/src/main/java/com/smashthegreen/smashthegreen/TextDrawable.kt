package com.smashthegreen.smashthegreen

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.TypedValue

class TextDrawable(context: Context, private var text: String, color: Int, private val x: Float, private val y: Float, size: Float, align: Boolean, shadowLayer: Float): Drawable() {

    private var paint: Paint = Paint()

    init {
        val pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, context.resources.displayMetrics)

        paint.color = color
        paint.textSize = pixel
        paint.isAntiAlias = true
        paint.isFakeBoldText = true
        paint.setShadowLayer(shadowLayer, 0F, 0F, Color.BLACK)
        paint.style = Paint.Style.FILL
        if(align){
            paint.textAlign = Paint.Align.CENTER
        }else{
            paint.textAlign = Paint.Align.LEFT
        }
    }


    override fun draw(canvas: Canvas) {
        canvas.drawText(text, x, y, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java",
        ReplaceWith("PixelFormat.TRANSLUCENT", "android.graphics.PixelFormat")
    )
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    fun setText(s: String){
        text = s
    }
}