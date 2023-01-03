package com.example.tictactoe_with_fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.tictactoe_with_fragments.databinding.FragmentBoardBinding
import com.example.tictactoe_with_fragments.databinding.FragmentScoreBinding

class ScoreFragment : Fragment() {
    private var _binding: FragmentScoreBinding? = null
    private val binding get() = _binding!!

    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameViewModel.scoreSignal.observe(viewLifecycleOwner) { updateScoreTable() }
    }

    private fun updateScoreTable() {
        binding.playerScoreTV.text = gameViewModel.playerScore.toString()
        binding.aiScoreTV.text = gameViewModel.aiScore.toString()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}