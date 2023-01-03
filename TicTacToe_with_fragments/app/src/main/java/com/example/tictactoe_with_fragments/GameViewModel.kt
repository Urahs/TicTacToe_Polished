package com.example.tictactoe_with_fragments

import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {

    fun boxSelection(isHorizontal:Boolean, positiveDirection: Boolean) {

        /*
        var direction = -1
        if(positiveDirection)
            direction = 1

        var x: Int = selectedCoordinate.get(0).digitToInt()
        var y: Int = selectedCoordinate.get(1).digitToInt()

        if(isHorizontal){
            do {
                y = (y+direction+3) % 3
            }
            while (boardMap[x.toString() + y.toString()] != ' ')
        }
        else{
            do {
                x = (x+direction+3) % 3
            }
            while (boardMap[x.toString() + y.toString()] != ' ')
        }

        setColorOfTheBox(selectedCoordinate, false)
        selectedCoordinate = x.toString() + y.toString()
        setColorOfTheBox(selectedCoordinate, true)
        */

    }
}