package com.example.tournament_creator

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment


class CreationDialog(private val savedTournaments: MutableList<String>) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater;
        val dialogView = inflater.inflate(R.layout.creation_dialog_layout, null)

        val npPlayers = dialogView.findViewById<NumberPicker>(R.id.number_picker)
        npPlayers.minValue = 4
        npPlayers.maxValue = 100

        val rbBracket = dialogView.findViewById<RadioButton>(R.id.radio_bracket)
        val rbTable = dialogView.findViewById<RadioButton>(R.id.radio_table)
        val etName = dialogView.findViewById<EditText>(R.id.tournament_name)

        builder.setView(dialogView)
            .setPositiveButton("Create", null)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val dialog: AlertDialog = builder.create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val intent = Intent(activity, PlayerEditionActivity::class.java)
                val playerCount = npPlayers.value

                // CHECK IF NAME IS UNIQUE AND NOT EMPTY
                var nameValid = true
                var name = etName.text.toString()
                if (name == "") {
                    name = "My Tournament"
                }
                for (existingName in savedTournaments) {
                    if (name == existingName) {
                        Toast.makeText(activity, "Name already taken", Toast.LENGTH_LONG).show()
                        nameValid = false
                    }
                }

                // CHECK IF TYPE IS CHECKED
                var typeValid = false
                var type = ""
                when {
                    rbBracket.isChecked -> {
                        typeValid = true
                        type = "BRACKET"
                    }
                    rbTable.isChecked -> {
                        typeValid = true
                        type = "TABLE"
                    }
                    else -> {
                        Toast.makeText(activity, "Please select type", Toast.LENGTH_LONG).show()
                    }
                }
                if (nameValid && typeValid) {
                    intent.putExtra("playerCount", playerCount)
                    intent.putExtra("tournamentName", name)
                    intent.putExtra("tournamentType", type)
                    startActivity(intent)
                    dismiss()
                }
            }
        }
        dialog.window!!.setBackgroundDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.round_rect_background,
                null
            )
        )
        return dialog
    }
}