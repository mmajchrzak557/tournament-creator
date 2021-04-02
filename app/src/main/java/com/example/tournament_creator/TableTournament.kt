package com.example.tournament_creator

import android.content.Context


class TableTournament(
    override val name: String,
    override val playerList: ArrayList<String>
) : Tournament(){
    var playerArray = ArrayList<Player>()
    init{
        for(i in 0 until playerList.size){
            playerArray.add(Player(playerList[i]))
        }
    }

    override fun getView(context: Context): TableView{
        return TableView(context, playerArray)
    }

    data class Player(var name: String){
        var wins = 0
    }
}