package com.example.tournament_creator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayerEditionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val playerCount = intent.getIntExtra("playerCount", 0)
        val tournamentName = intent.getStringExtra("tournamentName")
        val tournamentType = intent.getStringExtra("tournamentType")

        val tvName = findViewById<TextView>(R.id.tvName)
        tvName.text = tournamentName

        val playersArray = Array(playerCount) { i -> "Player ".plus((i + 1).toString()) }
        val adapter = PlayerListAdapter(playersArray.toMutableList())

        val recycler: RecyclerView = findViewById(R.id.recycler_view)
        recycler.adapter = adapter

        val btnCreateBracket = findViewById<Button>(R.id.btnCreateBracket)
        btnCreateBracket.setOnClickListener {
            val intent = Intent(this, TournamentActivity::class.java)
            val namesArray = ArrayList((recycler.adapter as PlayerListAdapter).playerData)

            val tournamentAdapter = TournamentAdapter()

            val klass = tournamentAdapter.classMap[tournamentType]
            val tournament = klass!!.constructors.first().call(tournamentName, namesArray) as Tournament

            intent.putExtra("json", TournamentAdapter().toString(tournament))
            intent.putExtra("tournamentType", tournamentType)
            startActivity(intent)
            finish()
        }
    }
}