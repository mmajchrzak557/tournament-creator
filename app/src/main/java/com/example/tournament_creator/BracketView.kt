package com.example.tournament_creator

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.*


@SuppressLint("ViewConstructor")
class BracketView(context: Context, playerArray: ArrayList<BracketTournament.Player>) :
    TournamentView(context) {

    private val boxWidth = 800
    private val boxHeight = 220

    override var xPos = (resources.displayMetrics.widthPixels - boxWidth).toFloat() / 1.8f
    override var yPos = xPos

    override val yMin = yPos

    private var bracket = Bracket(playerArray)
    private val w = bracket.bracketWidth
    private val h = bracket.bracketHeight

    override var xMin = xPos
    private var xMax = -(w * scale - (resources.displayMetrics.widthPixels - xMin * scale))
    private var yMax = -(h * scale - (resources.displayMetrics.heightPixels - xMin * scale))
    override var minScale = resources.displayMetrics.widthPixels / (w.toFloat() + 2 * xMin)
    override var maxScale = (resources.displayMetrics.widthPixels - 2 * xMin) / boxWidth.toFloat()

    inner class Box(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        topPlayer: BracketTournament.Player,
        botPlayer: BracketTournament.Player
    ) {
        val rect: RectF = RectF(left, top, right, bottom)
        val players = mutableListOf<BracketTournament.Player>()
        private val textWidth = 0.7f * rect.width()
        private val scoreWidth = 0.3f * rect.width()
        private val lineY = rect.centerY()
        private var textYUpper =
            (top + rect.height() / 4) - ((textPaint.descent() + textPaint.ascent()) / 2)
        private var textYLower =
            (top + rect.height() / 4 * 3) - ((textPaint.descent() + textPaint.ascent()) / 2)

        init {
            players.add(topPlayer)
            players.add(botPlayer)
            players[0].name = fitStringToRect(topPlayer.name, textWidth)
            players[1].name = fitStringToRect(botPlayer.name, textWidth)
        }


        fun draw(canvas: Canvas, layerIndex: Int) {
            canvas.drawRoundRect(rect, 20f, 20f, paint)
            canvas.drawRoundRect(rect, 20f, 20f, darkPaint)
            canvas.drawText(players[0].name, rect.left + textWidth / 2f, textYUpper, textPaint)
            canvas.drawText(players[1].name, rect.left + textWidth / 2f, textYLower, textPaint)

            for((index, player) in players.withIndex()){
                if(player.isValid || player.name == "BYE"){
                    if(index == 0) {
                        canvas.drawText(
                            player.scores[layerIndex],
                            rect.right - scoreWidth / 2f,
                            textYUpper,
                            textPaint
                        )
                    }else{
                        canvas.drawText(
                            player.scores[layerIndex],
                            rect.right - scoreWidth / 2f,
                            textYLower,
                            textPaint
                        )
                    }
                }
            }

            canvas.drawLine(
                rect.left,
                lineY,
                rect.right,
                lineY,
                darkPaint
            )

            canvas.drawLine(
                rect.left + textWidth,
                rect.top + 0.1f * rect.height(),
                rect.left + textWidth,
                rect.bottom - 0.6f * rect.height(),
                primaryColorPaint
            )

            canvas.drawLine(
                rect.left + textWidth,
                rect.top + 0.6f * rect.height(),
                rect.left + textWidth,
                rect.bottom - 0.1f * rect.height(),
                primaryColorPaint
            )
        }

    }

    inner class Layer(boxCount: Int, xOffset: Int, yOffset: Int, val layerIndex: Int) {
        val boxList = mutableListOf<Box>()

        init {
            for (i in 0 until boxCount) {
                val top = yOffset + i * 2 * boxHeight * 2f.pow(layerIndex)
                val right = xOffset + boxWidth
                val bottom = yOffset + boxHeight + i * 2 * boxHeight * 2f.pow(layerIndex)

                boxList.add(
                    Box(
                        xOffset.toFloat(),
                        top,
                        right.toFloat(),
                        bottom,
                        BracketTournament.Player("", false),
                        BracketTournament.Player("", false)
                    )
                )
            }
        }

        fun draw(canvas: Canvas, layer: Layer) {
            for (i in 0 until boxList.size) {
                Log.i("Layer_stuff", layerIndex.toString().plus(" ").plus(i.toString()))
                val startX = boxList[i].rect.right
                val startY = boxList[i].rect.centerY()
                val endLayerIndex = i / 2
                val endX = layer.boxList[endLayerIndex].rect.left
                val endY = layer.boxList[endLayerIndex].rect.centerY()
                val middleX = startX + (endX - startX) / 2f

                canvas.drawLine(
                    startX,
                    startY,
                    middleX,
                    startY,
                    darkPaint
                )

                canvas.drawLine(
                    middleX,
                    startY.toFloat(),
                    middleX,
                    endY.toFloat(),
                    darkPaint
                )

                canvas.drawLine(
                    middleX,
                    endY.toFloat(),
                    endX.toFloat(),
                    endY.toFloat(),
                    darkPaint
                )
                boxList[i].draw(canvas, layerIndex)
            }
        }

        fun draw(canvas: Canvas) {
            for (box in boxList) {
                box.draw(canvas, layerIndex)
            }
        }
    }

    inner class Bracket(players: ArrayList<BracketTournament.Player>) {
        var bracketWidth = 0
        var bracketHeight = 0
        private val layerCount = ceil(log(players.size.toDouble(), 2.0)).toInt()
        val layerList = mutableListOf<Layer>()

        init {
            var temp = -0.5f
            for (i in 0 until layerCount) {
                val boxCount = (2f.pow(layerCount - i - 1)).toInt()
                val xOffset = (boxWidth + 200) * i
                val yOffset = temp * 2 + 1
                temp = yOffset
                Log.i("temp", (i + 1).toString())
                layerList.add(Layer(boxCount, xOffset, yOffset.toInt() * boxHeight, i))

            }

            for ((index, box) in layerList[0].boxList.withIndex()) {
                val newBox = Box(
                    box.rect.left,
                    box.rect.top,
                    box.rect.right,
                    box.rect.bottom,
                    players[index * 2],
                    players[index * 2 + 1]
                )

                layerList[0].boxList[index] = newBox

            }

            for (box in layerList[0].boxList) {
                Log.i("PLAYER_VALIDATION", box.players[0].isValid.toString())
            }
            this.init()
            bracketWidth =
                this.layerList.last().boxList[0].rect.right.toInt() - this.layerList[0].boxList[0].rect.left.toInt()
            bracketHeight =
                this.layerList[0].boxList.last().rect.bottom.toInt() - this.layerList[0].boxList[0].rect.top.toInt()
        }

        fun draw(canvas: Canvas) {
            for (i in 0 until layerCount) {
                if (i < layerCount - 1) {
                    layerList[i].draw(canvas, layerList[i + 1])
                } else {
                    layerList[i].draw(canvas)
                }
            }
        }

        // THIS FUNCTION IS AN ABSOLUTE MESS, IT WILL GET FIXED LATER
        fun update() {
            for ((layerIndex, layer) in this.layerList.withIndex()) {
                for ((boxIndex, box) in layer.boxList.withIndex()) {
                    for ((playerIndex, player) in box.players.withIndex()) {
                        if (player.wins > layerIndex) {
                            if (layerIndex == this.layerList.lastIndex) break
                            val targetLayerIndex = layerIndex + 1
                            val targetBoxIndex = boxIndex / 2
                            val targetPlayerIndex = boxIndex.rem(2)
                            this.layerList[targetLayerIndex].boxList[targetBoxIndex].players[targetPlayerIndex] =
                                box.players[playerIndex]

                            val score = layerList[targetLayerIndex].boxList[targetBoxIndex].players[0].scores[targetLayerIndex]
                            val secondScore = layerList[targetLayerIndex].boxList[targetBoxIndex].players[1].scores[targetLayerIndex]
                            if(score != secondScore){
                                if(score == "-"){
                                    layerList[targetLayerIndex].boxList[targetBoxIndex].players[1].scores[targetLayerIndex] = "-"
                                    layerList[targetLayerIndex].boxList[targetBoxIndex].players[1].wins = targetLayerIndex
                                } else if(secondScore == "-"){
                                    layerList[targetLayerIndex].boxList[targetBoxIndex].players[0].scores[targetLayerIndex] = "-"
                                    layerList[targetLayerIndex].boxList[targetBoxIndex].players[0].wins = targetLayerIndex
                                }
                            }

                        } else if (player.wins < layerIndex) {
                            box.players[playerIndex] = BracketTournament.Player("", false)
                            Log.i("WINS_", box.players[playerIndex].name)
                        }
                    }
                }
            }
            invalidate()
        }

        // THIS ONE IS PRETTY BAD TOO
        private fun init() {
            for ((layerIndex, layer) in this.layerList.withIndex()) {
                for ((boxIndex, box) in layer.boxList.withIndex()) {
                    for ((playerIndex, player) in box.players.withIndex()) {
                        if (player.wins > layerIndex) {
                            if (layerIndex == this.layerList.lastIndex) break
                            val targetLayerIndex = layerIndex + 1
                            val targetBoxIndex = boxIndex / 2
                            val targetPlayerIndex = boxIndex.rem(2)
                            this.layerList[targetLayerIndex].boxList[targetBoxIndex].players[targetPlayerIndex] =
                                box.players[playerIndex]
                        } else if (player.wins < layerIndex) {
                            box.players[playerIndex] = BracketTournament.Player("", false)
                            Log.i("WINS_", box.players[playerIndex].name)
                        }
                    }
                }
            }
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)
        canvas.save()
        canvas.translate(xPos, yPos)
        canvas.scale(scale, scale)
        bracket.draw(canvas)
        canvas.restore()
    }


    override fun boundView() {
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        val navigationBarHeight = resources.getDimensionPixelSize(resourceId)
        xMax = -(w * scale - (resources.displayMetrics.widthPixels - xMin * scale))
        xPos = max(min(xPos, xMin * scale), xMax)

        yMax =
            -(h * scale - (resources.displayMetrics.heightPixels - navigationBarHeight - xMin * scale))
        yPos = max(yPos, yMax)
        yPos = min(yPos, yMin * scale)
    }


    override fun updateOnTouch(ev: MotionEvent) {
        for ((layerIndex, layer) in bracket.layerList.withIndex()) {
            for ((boxIndex, box) in layer.boxList.withIndex()) {
                val x = (ev.x - xPos) / scale
                val y = (ev.y - yPos) / scale
                if (box.rect.contains(x, y)) {
                    val playersValid = box.players[0].isValid and box.players[1].isValid
                    if (!playersValid) {
                        return
                    }
                    for (player in box.players) {
                        player.wins = layerIndex
                        Log.i("WINS_", player.name.plus(player.wins.toString()))
                    }
                    val scoreDialog = BracketInputDialog(box, bracket)
                    val activity = _context as AppCompatActivity
                    scoreDialog.show(activity.supportFragmentManager, "CreationDialogFragment")
                }
            }
        }
    }


}