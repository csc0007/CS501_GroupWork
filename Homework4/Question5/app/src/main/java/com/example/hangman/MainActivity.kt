package com.example.hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.hangman.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    private val hangmanViewModel: HangmanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableAllButtons(false)

        binding.startButton.setOnClickListener {
            hangmanViewModel.generateQuestion()
            var temp=hangmanViewModel.currentIndex
            binding.hintTextview.text = temp.toString()
            binding.myImageView.setImageResource(R.drawable.nobody)
            enableAllButtons(true)
            updateAnswer()
        }
        binding.aButton.setOnClickListener {
            hangmanViewModel.checkAnswer("a")
            updateAnswer()
            checkStatus()
            binding.aButton.isEnabled = false
        }
        binding.bButton.setOnClickListener {
            hangmanViewModel.checkAnswer("b")
            updateAnswer()
            checkStatus()
            binding.bButton.isEnabled = false
        }
        binding.cButton.setOnClickListener {
            hangmanViewModel.checkAnswer("c")
            updateAnswer()
            checkStatus()
            binding.cButton.isEnabled = false
        }
        binding.dButton.setOnClickListener {
            hangmanViewModel.checkAnswer("d")
            updateAnswer()
            checkStatus()
            binding.dButton.isEnabled = false
        }
        binding.eButton.setOnClickListener {
            hangmanViewModel.checkAnswer("e")
            updateAnswer()
            checkStatus()
            binding.eButton.isEnabled = false
        }
        binding.fButton.setOnClickListener {
            hangmanViewModel.checkAnswer("f")
            updateAnswer()
            checkStatus()
            binding.fButton.isEnabled = false
        }
        binding.gButton.setOnClickListener {
            hangmanViewModel.checkAnswer("g")
            updateAnswer()
            checkStatus()
            binding.gButton.isEnabled = false
        }
        binding.hButton.setOnClickListener {
            hangmanViewModel.checkAnswer("h")
            updateAnswer()
            checkStatus()
            binding.hButton.isEnabled = false
        }
        binding.iButton.setOnClickListener {
            hangmanViewModel.checkAnswer("i")
            updateAnswer()
            checkStatus()
            binding.iButton.isEnabled = false
        }
        binding.jButton.setOnClickListener {
            hangmanViewModel.checkAnswer("j")
            updateAnswer()
            checkStatus()
            binding.jButton.isEnabled = false
        }
        binding.kButton.setOnClickListener {
            hangmanViewModel.checkAnswer("k")
            updateAnswer()
            checkStatus()
            binding.kButton.isEnabled = false
        }
        binding.lButton.setOnClickListener {
            hangmanViewModel.checkAnswer("l")
            updateAnswer()
            checkStatus()
            binding.lButton.isEnabled = false
        }
        binding.mButton.setOnClickListener {
            hangmanViewModel.checkAnswer("m")
            updateAnswer()
            checkStatus()
            binding.mButton.isEnabled = false
        }
        binding.nButton.setOnClickListener {
            hangmanViewModel.checkAnswer("n")
            updateAnswer()
            checkStatus()
            binding.nButton.isEnabled = false
        }
        binding.oButton.setOnClickListener {
            hangmanViewModel.checkAnswer("o")
            updateAnswer()
            checkStatus()
            binding.oButton.isEnabled = false
        }
        binding.pButton.setOnClickListener {
            hangmanViewModel.checkAnswer("p")
            updateAnswer()
            checkStatus()
            binding.pButton.isEnabled = false
        }
        binding.qButton.setOnClickListener {
            hangmanViewModel.checkAnswer("q")
            updateAnswer()
            checkStatus()
            binding.qButton.isEnabled = false
        }
        binding.rButton.setOnClickListener {
            hangmanViewModel.checkAnswer("r")
            updateAnswer()
            checkStatus()
            binding.rButton.isEnabled = false
        }
        binding.sButton.setOnClickListener {
            hangmanViewModel.checkAnswer("s")
            updateAnswer()
            checkStatus()
            binding.sButton.isEnabled = false
        }
        binding.tButton.setOnClickListener {
            hangmanViewModel.checkAnswer("t")
            updateAnswer()
            checkStatus()
            binding.tButton.isEnabled = false
        }
        binding.uButton.setOnClickListener {
            hangmanViewModel.checkAnswer("u")
            updateAnswer()
            checkStatus()
            binding.uButton.isEnabled = false
        }
        binding.vButton.setOnClickListener {
            hangmanViewModel.checkAnswer("v")
            updateAnswer()
            checkStatus()
            binding.vButton.isEnabled = false
        }
        binding.wButton.setOnClickListener {
            hangmanViewModel.checkAnswer("w")
            updateAnswer()
            checkStatus()
            binding.wButton.isEnabled = false
        }
        binding.xButton.setOnClickListener {
            hangmanViewModel.checkAnswer("x")
            updateAnswer()
            checkStatus()
            binding.xButton.isEnabled = false
        }
        binding.yButton.setOnClickListener {
            hangmanViewModel.checkAnswer("y")
            updateAnswer()
            checkStatus()
            binding.yButton.isEnabled = false
        }
        binding.zButton.setOnClickListener {
            hangmanViewModel.checkAnswer("z")
            updateAnswer()
            checkStatus()
            binding.zButton.isEnabled = false
        }
    }

    private fun enableAllButtons(state:Boolean) {
        val buttons = listOf(
            binding.aButton, binding.bButton, binding.cButton,
            binding.dButton, binding.eButton, binding.fButton,
            binding.gButton, binding.hButton, binding.iButton,
            binding.jButton, binding.kButton, binding.lButton,
            binding.mButton, binding.nButton, binding.oButton,
            binding.pButton, binding.qButton, binding.rButton,
            binding.sButton, binding.tButton, binding.uButton,
            binding.vButton, binding.wButton, binding.xButton,
            binding.yButton, binding.zButton
        )
        if (state)
            buttons.forEach { it.isEnabled = true }
        else
            buttons.forEach { it.isEnabled = false }
    }
    private fun checkStatus()
    {
        if(hangmanViewModel.hangStatus==2)
        {
            binding.myImageView.setImageResource(R.drawable.noarmleg)
        }
        if(hangmanViewModel.hangStatus==1)
        {
            binding.myImageView.setImageResource(R.drawable.noarm)
        }
        if(hangmanViewModel.hangStatus==0)
        {
            binding.hintTextview.text = "YOU LOSE"
            binding.myImageView.setImageResource(R.drawable.hangman)
            enableAllButtons(false)
        }
    }
    private fun updateAnswer() {
        val textViews = listOf(
            binding.letter1Textview,
            binding.letter2Textview,
            binding.letter3Textview,
            binding.letter4Textview,
            binding.letter5Textview,
            binding.letter6Textview
        )

        textViews.forEachIndexed { index, textView ->
            val answer = hangmanViewModel.answers.getOrNull(index)
            if (answer != null) {
                textView.text = answer
                textView.isEnabled = true
            } else {
                textView.text = ""
                textView.isEnabled = false
            }
        }
    }
}