package com.example.tictactoe_with_fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.tictactoe_with_fragments.databinding.FragmentControllerBinding

class ControllerFragment : Fragment() {

    private var _binding: FragmentControllerBinding? = null
    private val binding get() = _binding!!

    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentControllerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.upButton.setOnClickListener{ onUpButtonTapped() }
        binding.downButton.setOnClickListener{ onDownButtonTapped() }
        binding.leftButton.setOnClickListener{ onLeftButtonTapped() }
        binding.rightButton.setOnClickListener{ onRightButtonTapped() }
        binding.actionButton.setOnClickListener{ onActionButtonTapped() }
        binding.restartButton.setOnClickListener{ onRestartButtonTapped() }
    }

    private fun onRestartButtonTapped() {

    }

    private fun onActionButtonTapped() {
        TODO("Not yet implemented")
    }

    private fun onRightButtonTapped() {
        TODO("Not yet implemented")
    }

    private fun onLeftButtonTapped() {
        TODO("Not yet implemented")
    }

    private fun onDownButtonTapped() {
        TODO("Not yet implemented")
    }

    private fun onUpButtonTapped() {
        gameViewModel.boxSelection(false, true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}