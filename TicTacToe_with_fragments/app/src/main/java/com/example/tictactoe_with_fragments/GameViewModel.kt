package com.example.tictactoe_with_fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Coordinate(var xCoordinate: Int, var yCoordinate: Int)

class GameState(){
    var prevPos = Coordinate(1, 1)
    var selectedPos = Coordinate(1, 1)
    var boardCoordinates = Array(3) {Array(3, {" "})}
}

class GameViewModel: ViewModel() {

    private var recursionDepth = 0
    private val axisLong = 3
    var playerScore = 0
    var aiScore = 0
    var bestAIMoveCoordinate = Coordinate(0,0)

    val state: GameState = GameState()
    val moveSignal = MutableLiveData<Boolean>()
    val xButtonSignal = MutableLiveData<Boolean>()
    val resetSignal = MutableLiveData<Boolean>()
    val scoreSignal = MutableLiveData<Boolean>()
    val aiSignal = MutableLiveData<Coordinate>()
    val toastMessageSignal = MutableLiveData<String>()


    fun boxSelection(isHorizontal:Boolean, positiveDirection: Boolean) {

        var moveStep = -1
        if(positiveDirection)
            moveStep = 1

        state.prevPos.xCoordinate = state.selectedPos.xCoordinate
        state.prevPos.yCoordinate = state.selectedPos.yCoordinate

        if(isHorizontal)
            state.selectedPos.yCoordinate = (state.selectedPos.yCoordinate + moveStep + axisLong) % axisLong
        else
            state.selectedPos.xCoordinate = (state.selectedPos.xCoordinate + moveStep + axisLong) % axisLong

        moveSignal.value = true
    }

    fun xButtonClicked() {

        if(state.boardCoordinates[state.selectedPos.xCoordinate][state.selectedPos.yCoordinate] != " "){
            toastMessageSignal.value = "You can't put X there!"
        }
        else{
            state.boardCoordinates[state.selectedPos.xCoordinate][state.selectedPos.yCoordinate] = "x"
            xButtonSignal.value = true

            if(shouldStartNewGame())
                startNewGame()
            else
                aiTurn()
        }
    }

    private fun shouldStartNewGame(): Boolean {

        if(terminateGame("x")){
            playerScore++
            toastMessageSignal.value = "YOU WIN!"
            scoreSignal.value = true
            return true
        }
        else if(terminateGame("o")){
            aiScore++
            toastMessageSignal.value = "AI WINS!"
            scoreSignal.value = true
            return true
        }

        state.boardCoordinates.forEach { row ->
            row.forEach { element ->
                if (element == " ")
                    return false
            }
        }

        toastMessageSignal.value = "TIE!"
        return true
    }

    fun terminateGame(playerMark: String): Boolean{
        // win conditions
        if(((state.boardCoordinates[0][0] == playerMark) && (state.boardCoordinates[0][1] == playerMark) && (state.boardCoordinates[0][2] == playerMark)) ||
            ((state.boardCoordinates[1][0] == playerMark) && (state.boardCoordinates[1][1] == playerMark) && (state.boardCoordinates[1][2] == playerMark)) ||
            ((state.boardCoordinates[2][0] == playerMark) && (state.boardCoordinates[2][1] == playerMark) && (state.boardCoordinates[2][2] == playerMark)) ||
            ((state.boardCoordinates[0][0] == playerMark) && (state.boardCoordinates[1][0] == playerMark) && (state.boardCoordinates[2][0] == playerMark)) ||
            ((state.boardCoordinates[0][1] == playerMark) && (state.boardCoordinates[1][1] == playerMark) && (state.boardCoordinates[2][1] == playerMark)) ||
            ((state.boardCoordinates[0][2] == playerMark) && (state.boardCoordinates[1][2] == playerMark) && (state.boardCoordinates[2][2] == playerMark)) ||
            ((state.boardCoordinates[0][0] == playerMark) && (state.boardCoordinates[1][1] == playerMark) && (state.boardCoordinates[2][2] == playerMark)) ||
            ((state.boardCoordinates[2][0] == playerMark) && (state.boardCoordinates[1][1] == playerMark) && (state.boardCoordinates[0][2] == playerMark)))
            return true

        return false
    }

    fun startNewGame() {

        for (row in 0..state.boardCoordinates.size-1)
            for (col in 0..state.boardCoordinates[0].size-1)
                state.boardCoordinates[row][col] = " "

        resetSignal.value = true
        playerTurn()
    }

    private fun playerTurn() {
        state.prevPos.xCoordinate = state.selectedPos.xCoordinate
        state.prevPos.yCoordinate = state.selectedPos.yCoordinate

        state.selectedPos.xCoordinate = 1
        state.selectedPos.yCoordinate = 1

        moveSignal.value = true
    }

    fun minimax(playerMark: String): Int{

        recursionDepth = recursionDepth + 1

        var avaliableAreas = ArrayList<Coordinate>()
        for (row in 0..state.boardCoordinates.size-1)
            for (col in 0..state.boardCoordinates[0].size-1)
                if (state.boardCoordinates[row][col] == " ")
                    avaliableAreas.add(Coordinate(row, col))


        // escape points of the recursion
        if(terminateGame("x"))
            return -10
        else if(terminateGame("o"))
            return 10
        if(avaliableAreas.size == 0)
            return 0


        var bestValue = if(playerMark == "x") 100 else -100
        avaliableAreas.forEach{

            state.boardCoordinates[it.xCoordinate][it.yCoordinate] = playerMark

            if(playerMark == "x")
                bestValue = minOf(bestValue, minimax("o"))
            else
                bestValue = maxOf(bestValue, minimax("x"))

            state.boardCoordinates[it.xCoordinate][it.yCoordinate] = " "
        }
        return bestValue
    }

    fun aiTurn(){
        var bestValue = -100
        var bestDepth = 10000

        for (row in 0..state.boardCoordinates.size-1){
            for (col in 0..state.boardCoordinates[0].size-1){
                if (state.boardCoordinates[row][col] == " "){

                    state.boardCoordinates[row][col] = "o"

                    recursionDepth = 0
                    var moveValue = minimax("x")

                    state.boardCoordinates[row][col] = " "

                    if((moveValue > bestValue) || ((moveValue == bestValue) && (recursionDepth < bestDepth))){
                        bestDepth = recursionDepth
                        bestAIMoveCoordinate.xCoordinate = row
                        bestAIMoveCoordinate.yCoordinate = col
                        bestValue = moveValue
                    }
                }
            }
        }

        state.boardCoordinates[bestAIMoveCoordinate.xCoordinate][bestAIMoveCoordinate.yCoordinate] = "o"
        aiSignal.value = bestAIMoveCoordinate

        if(shouldStartNewGame())
            startNewGame()
        else
            playerTurn()
    }
}