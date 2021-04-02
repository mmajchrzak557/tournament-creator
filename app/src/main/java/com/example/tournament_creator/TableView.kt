package com.example.tournament_creator

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("ViewConstructor")
class TableView(context: Context, playerArray: ArrayList<TableTournament.Player>) :
    TournamentView(context) {
    private val cellSize = 300f
    override var xPos = 0f
    override var yPos = 0f

    override val xMin = xPos
    override val yMin = yPos

    override var minScale = 0.1f
    override var maxScale = 3f

    private val playerCount = playerArray.size
    private val tableRect =
        RectF(0f, 0f, cellSize * (playerCount + 1), cellSize * (playerCount + 1))
    private val rectWidth = tableRect.width()

    init {
        scale = resources.displayMetrics.widthPixels / (rectWidth * 1.1f)
        xPos = (resources.displayMetrics.widthPixels - rectWidth * scale) / 2
        yPos = (resources.displayMetrics.heightPixels - rectWidth * scale) / 2.4f
    }

    private var cells: Array<Array<Cell?>> =
        Array(playerCount + 1) { arrayOfNulls<Cell>(playerCount + 1) }

    init {
        for (i in 0 until playerCount + 1) {
            for (j in 0 until playerCount + 1) {
                val left = i * cellSize
                val top = j * cellSize
                val right = left + cellSize
                val bottom = top + cellSize
                val rect = RectF(left, top, right, bottom)
                if (i == j) {
                    cells[i][j] = EmptyCell(rect)
                } else if (i == 0 || j == 0) {
                    cells[i][j] =
                        PlayerCell(rect, playerArray[i + j - 1])
                } else {
                    val name1 = playerArray[i - 1].name
                    val name2 = playerArray[j - 1].name
                    cells[i][j] = ScoreCell(rect, name1, name2)
                }
            }
        }
    }

    abstract inner class Cell(val rect: RectF) {
        var clicked = false
        abstract fun draw(canvas: Canvas)
    }

    inner class EmptyCell(rect: RectF) :
        Cell(rect) {
        override fun draw(canvas: Canvas) {
            darkPaint.style = Paint.Style.FILL
            canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, darkPaint)
            darkPaint.style = Paint.Style.STROKE
        }
    }

    inner class PlayerCell(rect: RectF, player: TableTournament.Player) : Cell(rect) {
        private val name = fitStringToRect(player.name, rect.width())
        private val textVerticalOffset = ((textPaint.descent() + textPaint.ascent()) / 2)
        private var textY = (rect.top + rect.height() / 2) - textVerticalOffset
        private var textX = (rect.left + (rect.right - rect.left) / 2f)
        override fun draw(canvas: Canvas) {
            canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, paint)
            canvas.drawText(name, textX, textY, textPaint)
        }
    }

    inner class ScoreCell(rect: RectF, val name1: String, val name2: String) : Cell(rect) {
        var score1 = "-"
        var score2 = "-"
        private val textVerticalOffset = ((textPaint.descent() + textPaint.ascent()) / 2)
        private var textY = (rect.top + rect.height() / 2) - textVerticalOffset
        private var textX = (rect.left + (rect.right - rect.left) / 2f)

        override fun draw(canvas: Canvas) {
            if(!clicked){
                return
            }
            textPaint.color = Color.BLACK
            canvas.drawText("$score1 : $score2", textX, textY, textPaint)
            textPaint.color = Color.WHITE
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)
        canvas.save()
        canvas.translate(xPos, yPos)
        canvas.scale(scale, scale)
        canvas.drawRect(tableRect, darkPaint)
        for (row in cells) {
            for (cell in row) {
                cell!!.draw(canvas)
            }
        }

        for (i in 0 until playerCount + 2) {
            val horizontalX = (i) * cellSize
            val horizontalY = (playerCount + 1) * cellSize
            canvas.drawLine(0f, horizontalX, horizontalY, horizontalX, darkPaint)
            canvas.drawLine(horizontalX, 0f, horizontalX, horizontalY, darkPaint)
        }

        canvas.restore()
    }

    override fun boundView() {

    }

    override fun updateOnTouch(ev: MotionEvent) {
        val x = (ev.x - xPos) / scale
        val y = (ev.y - yPos) / scale
        for (row in cells) {
            for (cell in row) {
                if (cell!!.rect.contains(x, y) && cell is ScoreCell ) {
                    val scoreDialog = TableInputDialog(cell, this)
                    val activity = _context as AppCompatActivity
                    scoreDialog.show(activity.supportFragmentManager, "CreationDialogFragment")
                }
            }
        }
    }
}