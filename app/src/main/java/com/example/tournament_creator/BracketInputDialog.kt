package com.example.tournament_creator

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment


class BracketInputDialog(private val box: BracketView.Box, private val bracket: BracketView.Bracket) :
ScoreInputDialog(){
    override val playerOneName = box.players[0].name
    override val playerTwoName = box.players[0].name

    override fun setListener(dialog: AlertDialog) {
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val currentLevel = box.players[0].wins
                box.players[0].scores[currentLevel] = npPlayer1.value.toString()
                box.players[1].scores[currentLevel] = npPlayer2.value.toString()
                if (npPlayer1.value > npPlayer2.value) {
                    box.players[0].wins++
                } else if (npPlayer1.value < npPlayer2.value) {
                    box.players[1].wins++
                }

                for(player in box.players){
                    Log.i("NO_IDEA", player.scores.toString())
                }
                bracket.update()
                dismiss()
            }
        }
    }

}