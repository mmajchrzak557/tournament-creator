package com.example.tournament_creator

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat

abstract class TournamentView(context: Context) : View(context) {

    val _context = context
    private var mActivePointerId = MotionEvent.INVALID_POINTER_ID
    private var startClickTime = 0L

    private val primaryColor = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
    private val accentColor = ResourcesCompat.getColor(resources, R.color.colorAccent, null)
    private val darkColor = ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)
    private var xStart = 0f
    private var yStart = 0f
    var scale = 1f

    val paint = Paint().apply {
        color = accentColor
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 6f
    }

    val primaryColorPaint = Paint().apply {
        color = primaryColor
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }

    val backgroundPaint = Paint().apply {
        color = backgroundColor
        style = Paint.Style.FILL
    }

    val darkPaint = Paint().apply {
        color = darkColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }
    val textPaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        isDither = true
        style = Paint.Style.FILL
        strokeWidth = 3f
        textSize = 80f
        textAlign = Paint.Align.CENTER
    }

    abstract var maxScale: Float
    abstract var minScale: Float

    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scale *= detector.scaleFactor
            scale = minScale.coerceAtLeast(scale.coerceAtMost(maxScale))
            Log.i("SCALE_STUFF", scale.toString())

            invalidate()
            return true
        }
    }
    private val scaleDetector = ScaleGestureDetector(context, scaleListener)

    abstract var xPos: Float
    abstract var yPos: Float

    abstract val xMin: Float
    abstract val yMin: Float

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {

        scaleDetector.onTouchEvent(ev)

        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                ev.actionIndex.also { pointerIndex ->
                    xStart = ev.getX(pointerIndex)
                    yStart = ev.getY(pointerIndex)
                }

                mActivePointerId = ev.getPointerId(0)
                startClickTime = System.currentTimeMillis()
            }

            MotionEvent.ACTION_MOVE -> {
                val (x: Float, y: Float) =
                    ev.findPointerIndex(mActivePointerId).let { pointerIndex ->
                        ev.getX(pointerIndex) to ev.getY(pointerIndex)
                    }

                xPos += x - xStart
                yPos += y - yStart


                boundView()
                invalidate()

                xStart = x
                yStart = y
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mActivePointerId = MotionEvent.INVALID_POINTER_ID
                if (System.currentTimeMillis() - startClickTime < ViewConfiguration.getTapTimeout()) {
                    updateOnTouch(ev)
                    invalidate()
                }
            }
            MotionEvent.ACTION_POINTER_UP -> {

                ev.actionIndex.also { pointerIndex ->
                    ev.getPointerId(pointerIndex)
                        .takeIf { it == mActivePointerId }
                        ?.run {
                            val newPointerIndex = if (pointerIndex == 0) 1 else 0
                            xStart = ev.getX(newPointerIndex)
                            yStart = ev.getY(newPointerIndex)
                            mActivePointerId = ev.getPointerId(newPointerIndex)
                        }
                }
            }
        }
        return true
    }

    fun fitStringToRect(_string: String, width: Float): String {
        var string = _string
        var dropped = false
        while (textPaint.measureText(string) > width) {
            string = string.dropLast(1)
            dropped = true
        }
        if (dropped) {
            string = string.dropLast(1)
            string = string.plus("..")
        }
        return string
    }

    abstract fun boundView()

    abstract fun updateOnTouch(ev: MotionEvent)

}