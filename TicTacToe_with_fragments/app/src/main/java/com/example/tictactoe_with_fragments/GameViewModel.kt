package com.example.tictactoe_with_fragments

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

    private val state: GameState = GameState()

    private val _gameState: MutableLiveData<GameState> = MutableLiveData(state)
    val gameState: LiveData<GameState> = _gameState

    private val _test: MutableLiveData<Coordinate> = MutableLiveData(state.selectedPos)
    val test: LiveData<Coordinate> = _test

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

        _gameState.value = state
        _test.value = state.selectedPos
    }


/*
    fun xButtonClicked() {

        if(state.boardCoordinates[state.selectedPos.xCoordinate][state.selectedPos.yCoordinate] == " "){
            // print toast message
        }
        else{
            state.boardCoordinates[state.selectedPos.xCoordinate][state.selectedPos.yCoordinate] = "x"
            // seçili yeri x yap boardda

            if(shouldStartNewGame())
                startNewGame()
            else
                aiTurn()
        }
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

    private fun shouldStartNewGame(): Boolean {

        if(terminateGame('x')){
            binding.playerScoreTV.text = (++playerScore).toString()
            Toast.makeText(applicationContext, "YOU WIN", Toast.LENGTH_SHORT).show()
            return true
        }
        else if(terminateGame('o')){
            binding.aiScoreTV.text = (++aiScore).toString()
            Toast.makeText(applicationContext, "AI WINS", Toast.LENGTH_SHORT).show()
            return true
        }

        boardMap.keys.forEach {
            if(boardMap[it] == ' ')
                return false
        }
        Toast.makeText(applicationContext, "TIE", Toast.LENGTH_SHORT).show()
        return true
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
        var bestMoveCoordinate = ""
        var bestDepth = 10000

        boardMap.keys.forEach {
            if(boardMap[it] == ' '){
                boardMap[it] = 'o'

                recursionDepth = 0
                var moveValue = minimax('x')

                boardMap[it] = ' '

                // print for debug
                //Log.d("test", "location: " + it + "     value: " + moveValue.toString() + "     depth: " + recursionDepth)

                if(moveValue > bestValue){
                    bestDepth = recursionDepth
                    bestMoveCoordinate = it
                    bestValue = moveValue
                }
                else if ((moveValue == bestValue) && (recursionDepth < bestDepth)){
                    bestDepth = recursionDepth
                    bestMoveCoordinate = it
                    bestValue = moveValue
                }
            }
        }

        boardMap[bestMoveCoordinate] = 'o'
        val selectedBox = findViewById<ImageView>(resources.getIdentifier("box_${bestMoveCoordinate}", "id", packageName))
        selectedBox.setImageResource(R.mipmap.oimage)

        if(shouldStartNewGame())
            startNewGame()
        else
            playerTurn()
    }

 */
}