package com.example.tournament_creator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import java.io.*


class MainActivity : AppCompatActivity() {
    private val adapter = TournamentListAdapter()
    private val buggedFile = "hw_cached_resid.list" // weird file that sometimes shows up, because of some Huawei firmware bugs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter.data = getSavedTournaments().toMutableList()

        val recycler: RecyclerView = findViewById(R.id.rvTournaments)
        recycler.adapter = adapter

        val btnCreate = findViewById<Button>(R.id.btnCreate)
        btnCreate.setOnClickListener {
            val creationDialog = CreationDialog(getSavedTournaments())
            creationDialog.show(supportFragmentManager, "CreationDialogFragment")
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.data = getSavedTournaments()
        adapter.notifyDataSetChanged()
    }

    private fun getSavedTournaments(): MutableList<String> {
        val path = File(this.filesDir.absolutePath)
        val savedFiles = mutableListOf<String>()
        val fileList = path.listFiles()
        if (fileList != null) {
            for (f in fileList) {
                if (f.isFile && f.name != buggedFile) {
                    savedFiles.add(f.name)
                } else if(f.name == buggedFile){
                    f.delete()
                }
            }
        } else {
            // TODO NIE MA ZADNYCH ZAPISANYCH TURNIEJOW
        }
        return savedFiles
    }
}