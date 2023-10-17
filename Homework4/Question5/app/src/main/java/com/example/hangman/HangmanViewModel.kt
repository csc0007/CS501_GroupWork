package com.example.hangman

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.hangman.databinding.ActivityMainBinding
import kotlin.random.Random

private const val TAG = "HangmanViewModel"

const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"

class HangmanViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {
    private lateinit var binding: ActivityMainBinding

    private val questionBank = listOf(
        Question(R.string.beer, R.string.hint_drink),
        Question(R.string.vodka, R.string.hint_drink),
        Question(R.string.whisky, R.string.hint_drink),
        Question(R.string.baijiu, R.string.hint_drink),
    )
    private companion object {
        const val RANDOM_NUM_KEY = "RANDOM_NUM_KEY"
    }

    private var randomNum: Int
        get() = savedStateHandle.get(RANDOM_NUM_KEY) ?: Random.nextInt(4)
        set(value) = savedStateHandle.set(RANDOM_NUM_KEY, value)



}