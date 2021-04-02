package com.example.tournament_creator

import android.content.Context
import android.widget.Toast
import com.google.gson.*
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import kotlin.math.pow


abstract class Tournament{
    abstract val name: String
    abstract val playerList: ArrayList<String>

    abstract fun getView(context: Context): TournamentView
}
