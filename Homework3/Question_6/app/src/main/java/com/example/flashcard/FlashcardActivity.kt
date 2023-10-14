package com.example.flashcard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.flashcard.databinding.ActivityFlashcardBinding
import com.google.android.material.snackbar.Snackbar

class FlashcardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFlashcardBinding

    private val flashcardViewModel: FlashcardViewModel by viewModels()
    private var username: String? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_USERNAME, username)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = savedInstanceState?.getString(EXTRA_USERNAME)
            ?: intent.getStringExtra(EXTRA_USERNAME)
        binding = ActivityFlashcardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (username != null) {
            val welcomeMessage = "Welcome $username"
            val snackbar = Snackbar.make(binding.root, welcomeMessage, Snackbar.LENGTH_LONG)
            snackbar.show()
        }

        val feedback = flashcardViewModel.feedbackText
        if (feedback != null) {
            binding.feedbackTextView.text = feedback
            binding.feedbackTextView.visibility = View.VISIBLE
        } else {
            binding.feedbackTextView.visibility = View.GONE
        }


        if (flashcardViewModel.currentIndex > 0) {
            //updateQuestion()
            binding.generateButton.isEnabled = false
            binding.submitButton.isEnabled = true
            if (flashcardViewModel.currentIndex == 10) {
                binding.scoreTextView.text = "${flashcardViewModel.correctAnswersCount} out of 10"
                binding.scoreTextView.visibility = View.VISIBLE
            }
        }

        binding.questionTextView.text = flashcardViewModel.getQuestionText()

        binding.generateButton.setOnClickListener {
            flashcardViewModel.correctAnswersCount = 0
            flashcardViewModel.currentIndex = 0  // Reset the index
            binding.scoreTextView.visibility = View.GONE // Hide score TextView
            binding.generateButton.isEnabled = false
            binding.submitButton.isEnabled = true
            updateQuestion()
        }


        binding.submitButton.setOnClickListener {
            val userAnswer = binding.answerEditText.text.toString().toIntOrNull()

            if (userAnswer == null) {
                Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if the user's answer is correct and increment the score
            if (flashcardViewModel.checkAnswer(userAnswer)) {
                flashcardViewModel.correctAnswersCount++
                binding.feedbackTextView.text = "Correct!"
                binding.feedbackTextView.visibility = View.VISIBLE
                flashcardViewModel.feedbackText = "Correct!"
            } else {
                binding.feedbackTextView.text = "Wrong!"
                binding.feedbackTextView.visibility = View.VISIBLE
                flashcardViewModel.feedbackText = "Wrong!"
            }


            if (flashcardViewModel.currentIndex == 9 && flashcardViewModel.correctAnswersCount <= 9) {
                flashcardViewModel.moveToNext()
                binding.generateButton.isEnabled = true
                binding.submitButton.isEnabled = false

                Toast.makeText(this, "${flashcardViewModel.correctAnswersCount} out of 10", Toast.LENGTH_SHORT).show()
                binding.scoreTextView.text = "${flashcardViewModel.correctAnswersCount} out of 10"
                binding.scoreTextView.visibility = View.VISIBLE
                flashcardViewModel.correctAnswersCount = 0
            } else {
                flashcardViewModel.moveToNext()
                updateQuestion()
            }
            val answerEditText: EditText = findViewById(R.id.answerEditText)
            answerEditText.setText("")
        }
    }

    companion object {
        private const val EXTRA_USERNAME = "com.example.flashcard.username"

        fun newIntent(packageContext: Context, username: String): Intent {
            return Intent(packageContext, FlashcardActivity::class.java).apply {
                putExtra(EXTRA_USERNAME, username)
            }
        }
    }

    private fun updateQuestion(){
        val showText : String = flashcardViewModel.setQuestion()
        binding.questionTextView.text = showText
    }
}