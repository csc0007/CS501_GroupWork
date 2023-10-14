package com.example.flashcard

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.random.Random

private const val TAG = "FlashcardViewModel"
private const val USER = "Tuesday"
private const val PASSWORD = "Thursday"

const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"

class FlashcardViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {

    init {
        Log.d(TAG,"ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG,"ViewModel instance about to be destroyed")
    }
    private val questionBank = MutableList(10) { Question("", 0) }
    private var operationNum: Int = Random.nextInt(0, 1)
    //0 is addition, 1 is subtraction

    var currentIndex : Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY)?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY,value)

    private var isGenerated = false

    private companion object {
        const val RANDOM_NUM_1_KEY = "RANDOM_NUM_1_KEY"
        const val RANDOM_NUM_2_KEY = "RANDOM_NUM_2_KEY"
        const val RANDOM_OPERATION_KEY = "RANDOM_OPERATION_KEY"
        const val CORRECT_ANSWERS_COUNT_KEY = "CORRECT_ANSWERS_COUNT_KEY"
        const val FEEDBACK_TEXT_KEY = "FEEDBACK_TEXT_KEY"
    }
    var feedbackText: String?
        get() = savedStateHandle.get(FEEDBACK_TEXT_KEY)
        set(value) = savedStateHandle.set(FEEDBACK_TEXT_KEY, value)

    private var randomNum1: Int
        get() = savedStateHandle.get(RANDOM_NUM_1_KEY) ?: Random.nextInt(1, 100)
        set(value) = savedStateHandle.set(RANDOM_NUM_1_KEY, value)

    private var randomNum2: Int
        get() = savedStateHandle.get(RANDOM_NUM_2_KEY) ?: Random.nextInt(1, 21)
        set(value) = savedStateHandle.set(RANDOM_NUM_2_KEY, value)

    private var randomOp: Int
        get() = savedStateHandle.get(RANDOM_OPERATION_KEY) ?: Random.nextInt(2)
        set(value) = savedStateHandle.set(RANDOM_OPERATION_KEY, value)

    var correctAnswersCount: Int
        get() = savedStateHandle.get(CORRECT_ANSWERS_COUNT_KEY) ?: 0
        set(value) = savedStateHandle.set(CORRECT_ANSWERS_COUNT_KEY, value)


    fun checkAnswer(userAnswer: Int): Boolean {
        return userAnswer == questionBank[currentIndex].answer
    }

    fun isTextEqualToUSER(text: String): Boolean {
        return text == USER
    }

    fun isTextEqualToPASSWORD(text: String): Boolean {
        return text == PASSWORD
    }

    fun setQuestion(): String {
        // Generating two random numbers
        randomNum1 = Random.nextInt(1, 100)
        randomNum2 = Random.nextInt(1, 21)
        randomOp = Random.nextInt(2)

        // Decide whether to perform addition or subtraction based on the operationNum
        val questionText = if (randomOp == 0) {
            "$randomNum1 + $randomNum2"
        } else {
            "$randomNum1 - $randomNum2"
        }

        val answer = if (randomOp == 0) randomNum1 + randomNum2 else randomNum1 - randomNum2

        // Updating the current question in the bank
        questionBank[currentIndex] = Question(questionText, answer)
        isGenerated=true
        return "${currentIndex + 1}. $questionText"
    }
    fun getQuestionText(): String {
        if (isGenerated==true)
        {
            var temp=currentIndex+1
            return "$temp" + "." + questionBank[currentIndex].question
        }
        else
            return questionBank[currentIndex].question
    }

    fun moveToNext(){
        currentIndex = (currentIndex + 1) % 10
    }

}