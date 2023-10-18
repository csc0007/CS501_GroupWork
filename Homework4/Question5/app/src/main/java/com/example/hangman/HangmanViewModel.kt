package com.example.hangman

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class HangmanViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {

    private val questionBank = listOf(
        Question("beer", R.string.hint_drink),
        Question("vodka", R.string.hint_drink),
        Question("whisky", R.string.hint_drink),
        Question("baijiu", R.string.hint_drink),
    )
    private companion object {
        const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
        const val ANSWERS_KEY = "ANSWERS_KEY"
        const val QUESTION_LENGTH_KEY = "QUESTION_LENGTH_KEY"
        const val HANG_STATUS_KEY = "HANG_STATUS_KEY"
        const val GAME_STATUS_KEY = "GAME_STATUS_KEY"
    }
    var gameState: Int
        get() = savedStateHandle[GAME_STATUS_KEY] ?: 0
        set(value) = savedStateHandle.set(GAME_STATUS_KEY, value)

    var currentIndex: Int
        get() = savedStateHandle[CURRENT_INDEX_KEY] ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    private var questionLength: Int
        get() = savedStateHandle[QUESTION_LENGTH_KEY] ?: questionBank[currentIndex].question.length
        set(value) = savedStateHandle.set(QUESTION_LENGTH_KEY, value)

    var answers: MutableList<String>
        get() = savedStateHandle[ANSWERS_KEY] ?: mutableListOf()
        set(value) = savedStateHandle.set(ANSWERS_KEY, value)

    var hangStatus: Int
        get() = savedStateHandle[HANG_STATUS_KEY] ?: 2  //user have 2 change of making mistake
        set(value) = savedStateHandle.set(HANG_STATUS_KEY, value)

    fun generateQuestion(){
        val temp=currentIndex
        currentIndex=Random.nextInt(4)
        while(temp==currentIndex)
            currentIndex=Random.nextInt(4)
        // Update questionLength based on the new currentIndex
        questionLength = questionBank[currentIndex].question.length
        // Clear and initialize a new answers list based on the new questionLength
        val newAnswers = mutableListOf<String>()
        for (i in 0 until questionLength) {
            newAnswers.add("_")
        }
        answers = newAnswers
    }

    fun checkAnswer(letter:String) {
        val word = questionBank[currentIndex].question
        val updatedAnswers = answers  // Create a new list

        if (letter in word) {
            var index = word.indexOf(letter)
            while (index >= 0) {
                updatedAnswers[index] = letter
                index = word.indexOf(letter, index + 1)
            }
        }
        else
        {
            hangStatus-=1
        }
        answers = updatedAnswers
    }
}