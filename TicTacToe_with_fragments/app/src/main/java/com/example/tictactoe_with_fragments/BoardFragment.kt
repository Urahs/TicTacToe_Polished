package com.example.tictactoe_with_fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Message
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.tictactoe_with_fragments.databinding.FragmentBoardBinding

class BoardFragment : Fragment() {

    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!!

    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameViewModel.moveSignal.observe(viewLifecycleOwner) { changeColorsOfBoxs() }
        gameViewModel.xButtonSignal.observe(viewLifecycleOwner){ xButtonTapped() }
        gameViewModel.aiSignal.observe(viewLifecycleOwner){ aiMove(it) }
        gameViewModel.resetSignal.observe(viewLifecycleOwner){ resetGame() }

    }

    private fun resetGame() {
        for (row in 0..gameViewModel.state.boardCoordinates.size-1) {
            for (col in 0..gameViewModel.state.boardCoordinates[0].size - 1) {
                var selectedBox = view?.findViewById<TextView>(resources.getIdentifier("box_$row$col", "id", context?.packageName))
                selectedBox?.text = " "
            }
        }
    }

    private fun aiMove(aiMoveCoordinate: Coordinate) {
        var relatedBox = view?.findViewById<TextView>(resources.getIdentifier("box_${aiMoveCoordinate.xCoordinate}${aiMoveCoordinate.yCoordinate}", "id", context?.packageName))
        relatedBox?.text = "O"
    }

    private fun xButtonTapped() {
        //Log.d("test", "xButton tapped!!!")
        val posX = gameViewModel.state.selectedPos.xCoordinate
        val posY = gameViewModel.state.selectedPos.yCoordinate
        var selectedBox = view?.findViewById<TextView>(resources.getIdentifier("box_$posX$posY", "id", context?.packageName))
        selectedBox?.text = "X"
        changeColorsOfBoxs()
    }

    private fun changeColorsOfBoxs(){
        setColorOfTheBox(gameViewModel.state.prevPos, "#E6548E")
        setColorOfTheBox(gameViewModel.state.selectedPos, "#C60A55")
    }

    private fun setColorOfTheBox(coordinate: Coordinate, boxColor: String){
        var selectedBox = view?.findViewById<TextView>(resources.getIdentifier("box_${coordinate.xCoordinate}${coordinate.yCoordinate}", "id", context?.packageName))
        selectedBox?.setBackgroundColor(Color.parseColor(boxColor))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}