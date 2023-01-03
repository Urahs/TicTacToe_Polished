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

        gameViewModel.test.observe(viewLifecycleOwner) {
            setColorOfTheBox(it)
        }

        /*
        gameViewModel.gameState.observe(viewLifecycleOwner) {
            processGameStateChange(it)
        }
         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setColorOfTheBox(coordinate: Coordinate){
        Log.d("test", "AAAAAAAAAAAAA")
        var selectedBox = view?.findViewById<TextView>(resources.getIdentifier("box_${coordinate.xCoordinate}${coordinate.yCoordinate}", "id", context?.packageName))
        var textBackGroundColor = "#00FF00" //= if(isSelected) "#00FF00" else "#FF0000"
        selectedBox?.setBackgroundColor(Color.parseColor(textBackGroundColor))
    }

}