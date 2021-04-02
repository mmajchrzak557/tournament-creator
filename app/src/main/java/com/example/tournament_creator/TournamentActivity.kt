package com.example.tournament_creator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsetsController
import android.widget.Toast
import java.io.OutputStreamWriter


class TournamentActivity : AppCompatActivity() {
    private var tournament: Tournament? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = intent.getStringExtra("json")
        Log.i("PLAYERS_SAVE_lala", json!!)
        tournament = TournamentAdapter().fromString(json!!)
        title = tournament!!.name

        setContentView(tournament!!.getView(this))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_save) {
            val fileOutput = this.openFileOutput(tournament!!.name, Context.MODE_PRIVATE)
            val writer = OutputStreamWriter(fileOutput)
            writer.write(TournamentAdapter().toString(tournament!!))
            writer.close()

            Toast.makeText(this, "Tournament saved", Toast.LENGTH_LONG).show()
        }
        return true
    }
}
