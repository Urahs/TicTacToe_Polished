package com.example.tictactoe_with_fragments

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
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
    private var toastMessage = ""
    var playerScore = 0
    var aiScore = 0
    var bestAIMoveCoordinate = Coordinate(0,0)

    val state: GameState = GameState()
    private val xButtonClickedFire: Boolean = false
    private val fireSignal: Boolean = false

    private val _moveSignal: MutableLiveData<Boolean> = MutableLiveData(fireSignal)
    val moveSignal: LiveData<Boolean> = _moveSignal

    private val _xButtonSignal: MutableLiveData<Boolean> = MutableLiveData(xButtonClickedFire)
    val xButtonSignal: LiveData<Boolean> = _xButtonSignal

    private val _aiSignal: MutableLiveData<Coordinate> = MutableLiveData(bestAIMoveCoordinate)
    val aiSignal: LiveData<Coordinate> = _aiSignal

    private val _resetSignal: MutableLiveData<Boolean> = MutableLiveData(fireSignal)
    val resetSignal: LiveData<Boolean> = _resetSignal

    private val _scoreSignal: MutableLiveData<Boolean> = MutableLiveData(fireSignal)
    val scoreSignal: LiveData<Boolean> = _scoreSignal

    private val _toastMessageSignal: MutableLiveData<String> = MutableLiveData(toastMessage)
    val toastMessageSignal: LiveData<String> = _toastMessageSignal


    private val axisLong = 3

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


        _moveSignal.value = fireSignal
    }



    fun xButtonClicked() {

        if(state.boardCoordinates[state.selectedPos.xCoordinate][state.selectedPos.yCoordinate] != " "){
            toastMessage = "You can't put X there!"
            _toastMessageSignal.value = toastMessage
        }
        else{
            state.boardCoordinates[state.selectedPos.xCoordinate][state.selectedPos.yCoordinate] = "x"
            _xButtonSignal.value = xButtonClickedFire

            if(shouldStartNewGame())
                startNewGame()
            else
                aiTurn()

        }
    }

    private fun shouldStartNewGame(): Boolean {

        if(terminateGame("x")){
            playerScore++
            toastMessage = "YOU WIN!"
            _toastMessageSignal.value = toastMessage
            _scoreSignal.value = fireSignal
            return true
        }
        else if(terminateGame("o")){
            aiScore++
            toastMessage = "AI WINS!"
            _toastMessageSignal.value = toastMessage
            _scoreSignal.value = fireSignal
            return true
        }

        state.boardCoordinates.forEach { row ->
            row.forEach { element ->
                if (element == " ")
                    return false
            }
        }

        toastMessage = "TIE!"
        _toastMessageSignal.value = toastMessage
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

        _resetSignal.value = fireSignal
        playerTurn()
    }

    private fun playerTurn() {
        state.prevPos.xCoordinate = state.selectedPos.xCoordinate
        state.prevPos.yCoordinate = state.selectedPos.yCoordinate

        state.selectedPos.xCoordinate = 1
        state.selectedPos.yCoordinate = 1

        _moveSignal.value = fireSignal
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
        _aiSignal.value = bestAIMoveCoordinate

        if(shouldStartNewGame())
            startNewGame()
        else
            playerTurn()
    }
}