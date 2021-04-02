package com.example.tournament_creator

import android.app.AlertDialog
import android.content.Context
import android.util.Log

class TableInputDialog(private val cell: TableView.ScoreCell, val view: TableView) :
    ScoreInputDialog(){
    override val playerOneName = cell.name1
    override val playerTwoName = cell.name2

    override fun setListener(dialog: AlertDialog) {
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                cell.score1 = npPlayer1.value.toString()
                cell.clicked = true
                cell.score2 = npPlayer2.value.toString()
                view.invalidate()
                dismiss()
            }
        }
    }

}