package com.example.tictactoe_with_fragments

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Coordinate(var xCoordinate: Int, var yCoordinate: Int)

class GameState(){
    var prevPos = Coordinate(0, 0)
    var selectedPos = Coordinate(0, 0)
    var boardCoordinates = Array(3) {Array(3, {" "})}
}

class GameViewModel: ViewModel() {

    private var recursionDepth = 0

    private val state: GameState = GameState()
    private val xButtonClickedFire: Boolean = false
    private val fireSignal: Boolean = false

    private val _gameState: MutableLiveData<GameState> = MutableLiveData(state)
    val gameState: LiveData<GameState> = _gameState

    private val _selectedPos: MutableLiveData<Coordinate> = MutableLiveData(state.selectedPos)
    val selectedPos: LiveData<Coordinate> = _selectedPos

    private val _prevPos: MutableLiveData<Coordinate> = MutableLiveData(state.prevPos)
    val prevPos: LiveData<Coordinate> = _prevPos

    private val _moveSignal: MutableLiveData<Boolean> = MutableLiveData(fireSignal)
    val moveSignal: LiveData<Boolean> = _moveSignal

    private val _xButtonSignal: MutableLiveData<Boolean> = MutableLiveData(xButtonClickedFire)
    val xButtonSignal: LiveData<Boolean> = _xButtonSignal


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

        //_gameState.value = state
        //_selectedPos.value = state.selectedPos
        //_prevPos.value = state.prevPos
        _moveSignal.value = fireSignal
    }



    fun xButtonClicked() {

        Log.d("test", "xButton tapped!!!")

        if(state.boardCoordinates[state.selectedPos.xCoordinate][state.selectedPos.yCoordinate] != " "){
            //Toast.makeText(, "YOU WIN", Toast.LENGTH_SHORT).show() --->> toast a message here
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
            // player score güncelle
            // toast mesage at
            return true
        }
        else if(terminateGame("o")){
            // ai score güncelle
            // toast mesage at
            return true
        }

        state.boardCoordinates.forEach { row ->
            row.forEach { element ->
                if (element == " ")
                    return false
            }
        }

        // tie toast mesajı at
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

        boardMap.keys.forEach {
            boardMap[it] = ' '
            setColorOfTheBox(it, false)
        }

        playerTurn()
    }

    private fun playerTurn() {

        selectedCoordinate = ""
        coordinates.forEach {
            if(selectedCoordinate == "")
                if(boardMap[it] == ' ')
                    selectedCoordinate = it
        }

        setColorOfTheBox(selectedCoordinate, true)
    }






    fun minimax(playerMark: Char): Int{

        recursionDepth = recursionDepth + 1
        var avaliableAreas = ArrayList<String>()
        boardMap.keys.forEach {
            if(boardMap[it] == ' ')
                avaliableAreas.add(it)
        }

        // escape points of the recursion
        if(terminateGame('x'))
            return -10
        else if(terminateGame('o'))
            return 10
        if(avaliableAreas.size == 0)
            return 0


        var bestValue = if(playerMark == 'x') 100 else -100
        avaliableAreas.forEach{ blankArea ->
            boardMap[blankArea] = playerMark

            if(playerMark == 'x')
                bestValue = minOf(bestValue, minimax('o'))
            else
                bestValue = maxOf(bestValue, minimax('x'))

            boardMap[blankArea] = ' '
        }
        return bestValue
    }

    fun aiTurn(){
        var bestValue = -100
        var bestMoveCoordinate = Coordinate()
        var bestDepth = 10000


        for (row in 0..state.boardCoordinates.size){
            for (col in 0..state.boardCoordinates[0].size){
                if (state.boardCoordinates[row][col] == " "){

                    state.boardCoordinates[row][col] = "o"

                    recursionDepth = 0
                    var moveValue = minimax('x')

                    state.boardCoordinates[row][col] = " "

                    if((moveValue > bestValue) || ((moveValue == bestValue) && (recursionDepth < bestDepth))){
                        bestDepth = recursionDepth
                        bestMoveCoordinate.xCoordinate = row
                        bestMoveCoordinate.yCoordinate = col
                        bestValue = moveValue
                    }
                }
            }
        }

        state.boardCoordinates[bestMoveCoordinate.xCoordinate][bestMoveCoordinate.yCoordinate] = "o"
        // işaretlenen yeri o yap

        if(shouldStartNewGame())
            startNewGame()
        else
            playerTurn()
    }
}