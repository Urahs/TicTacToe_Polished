package com.example.tictactoe_with_fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        gameViewModel.moveSignal.observe(viewLifecycleOwner) { TestFnc() }
        gameViewModel.xButtonSignal.observe(viewLifecycleOwner){ xButtonTapped() }

    }

    private fun xButtonTapped() {
        //Log.d("test", "xButton tapped!!!")
        val posX = gameViewModel.gameState.value!!.selectedPos.xCoordinate
        val posY = gameViewModel.gameState.value!!.selectedPos.yCoordinate
        var selectedBox = view?.findViewById<TextView>(resources.getIdentifier("box_$posX$posY", "id", context?.packageName))
        selectedBox?.text = "X"
    }


    private fun TestFnc(){
        setColorOfTheBox(gameViewModel.gameState.value!!.selectedPos, "#FF0000")
        setColorOfTheBox(gameViewModel.gameState.value!!.prevPos, "#FFFFFF")
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