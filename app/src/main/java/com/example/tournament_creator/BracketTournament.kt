package com.example.tournament_creator

import android.content.Context
import android.util.Log
import kotlin.math.pow

class BracketTournament(
    override val name: String,
    override val playerList: ArrayList<String>
) : Tournament(){
    private val playerCount = playerList.size
    val playerArray = ArrayList<Player>()
    init{
        var playerArrayLength = 0
        for(i in 0..10){
            if(playerCount <= 2f.pow(i)) {
                playerArrayLength = 2f.pow(i).toInt()
                break
            }
        }
        var nullPlayerCount = playerArrayLength - playerCount
        for(i in playerCount - 1 downTo 0){
            var nullFlag = false
            if(nullPlayerCount > 0){
                playerArray.add(Player("BYE", false))
                nullPlayerCount--
                nullFlag = true
            }
            playerArray.add(Player(playerList[i], true))
            if(nullFlag){
                playerArray.last().wins = 1
                playerArray.last().scores.add("-")
            }
        }

        playerArray.reverse()
    }

    override fun getView(context: Context): BracketView{
        return BracketView(context, playerArray)
    }

    data class Player(var name: String, val isValid: Boolean){
        var wins = 0
        val scores = mutableListOf<String>()
        init{
            for(i in 0 until 10) {
                scores.add("-")
            }
        }

    }
}