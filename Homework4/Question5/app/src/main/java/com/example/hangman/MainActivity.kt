package com.example.hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hangman.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    private fun updateQuestion(){
        val questionTextResId = HangmanViewModel.currentQuestionText
        binding.letter1_textview.setText(questionTextResId)
    }
}