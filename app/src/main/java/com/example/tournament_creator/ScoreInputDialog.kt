package com.example.tournament_creator

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.NumberPicker
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment

abstract class ScoreInputDialog() : DialogFragment() {
    abstract val playerOneName: String
    abstract val playerTwoName: String
    lateinit var npPlayer1: NumberPicker
    lateinit var npPlayer2: NumberPicker

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.score_dialog_layout, null)

        npPlayer1 = dialogView.findViewById(R.id.npPlayer1)
        npPlayer1.minValue = 0
        npPlayer1.maxValue = 200


        npPlayer2 = dialogView.findViewById<NumberPicker>(R.id.npPlayer2)
        npPlayer2.minValue = 0
        npPlayer2.maxValue = 200

        val tvPlayer1 = dialogView.findViewById<TextView>(R.id.tvPlayer1)
        tvPlayer1.text = playerOneName

        val tvPlayer2 = dialogView.findViewById<TextView>(R.id.tvPlayer2)
        tvPlayer2.text = playerTwoName

        builder.setView(dialogView)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val dialog: AlertDialog = builder.create()

        setListener(dialog)

        dialog.window!!.setBackgroundDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.round_rect_background,
                null
            )
        )

        dialog.window!!.addFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )
        return dialog
    }

    abstract fun setListener(dialog: AlertDialog)

}