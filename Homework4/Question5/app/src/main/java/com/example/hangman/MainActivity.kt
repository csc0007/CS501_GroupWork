package com.example.hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            enableAllButtons(true)
            updateAnswer()
        }
        binding.aButton.setOnClickListener {
            hangmanViewModel.checkAnswer("a")
            updateAnswer()
            binding.aButton.isEnabled = false
        }
        binding.bButton.setOnClickListener {
            hangmanViewModel.checkAnswer("b")
            updateAnswer()
            binding.bButton.isEnabled = false
        }
        binding.cButton.setOnClickListener {
            hangmanViewModel.checkAnswer("c")
            updateAnswer()
            binding.cButton.isEnabled = false
        }
        binding.dButton.setOnClickListener {
            hangmanViewModel.checkAnswer("d")
            updateAnswer()
            binding.dButton.isEnabled = false
        }
        binding.eButton.setOnClickListener {
            hangmanViewModel.checkAnswer("e")
            updateAnswer()
            binding.eButton.isEnabled = false
        }
        binding.fButton.setOnClickListener {
            hangmanViewModel.checkAnswer("f")
            updateAnswer()
            binding.fButton.isEnabled = false
        }
        binding.gButton.setOnClickListener {
            hangmanViewModel.checkAnswer("g")
            updateAnswer()
            binding.gButton.isEnabled = false
        }
        binding.hButton.setOnClickListener {
            hangmanViewModel.checkAnswer("h")
            updateAnswer()
            binding.hButton.isEnabled = false
        }
        binding.iButton.setOnClickListener {
            hangmanViewModel.checkAnswer("i")
            updateAnswer()
            binding.iButton.isEnabled = false
        }
        binding.jButton.setOnClickListener {
            hangmanViewModel.checkAnswer("j")
            updateAnswer()
            binding.jButton.isEnabled = false
        }
        binding.kButton.setOnClickListener {
            hangmanViewModel.checkAnswer("k")
            updateAnswer()
            binding.kButton.isEnabled = false
        }
        binding.lButton.setOnClickListener {
            hangmanViewModel.checkAnswer("l")
            updateAnswer()
            binding.lButton.isEnabled = false
        }
        binding.mButton.setOnClickListener {
            hangmanViewModel.checkAnswer("m")
            updateAnswer()
            binding.mButton.isEnabled = false
        }
        binding.nButton.setOnClickListener {
            hangmanViewModel.checkAnswer("n")
            updateAnswer()
            binding.nButton.isEnabled = false
        }
        binding.oButton.setOnClickListener {
            hangmanViewModel.checkAnswer("o")
            updateAnswer()
            binding.oButton.isEnabled = false
        }
        binding.pButton.setOnClickListener {
            hangmanViewModel.checkAnswer("p")
            updateAnswer()
            binding.pButton.isEnabled = false
        }
        binding.qButton.setOnClickListener {
            hangmanViewModel.checkAnswer("q")
            updateAnswer()
            binding.qButton.isEnabled = false
        }
        binding.rButton.setOnClickListener {
            hangmanViewModel.checkAnswer("r")
            updateAnswer()
            binding.rButton.isEnabled = false
        }
        binding.sButton.setOnClickListener {
            hangmanViewModel.checkAnswer("s")
            updateAnswer()
            binding.sButton.isEnabled = false
        }
        binding.tButton.setOnClickListener {
            hangmanViewModel.checkAnswer("t")
            updateAnswer()
            binding.tButton.isEnabled = false
        }
        binding.uButton.setOnClickListener {
            hangmanViewModel.checkAnswer("u")
            updateAnswer()
            binding.uButton.isEnabled = false
        }
        binding.vButton.setOnClickListener {
            hangmanViewModel.checkAnswer("v")
            updateAnswer()
            binding.vButton.isEnabled = false
        }
        binding.wButton.setOnClickListener {
            hangmanViewModel.checkAnswer("w")
            updateAnswer()
            binding.wButton.isEnabled = false
        }
        binding.xButton.setOnClickListener {
            hangmanViewModel.checkAnswer("x")
            updateAnswer()
            binding.xButton.isEnabled = false
        }
        binding.yButton.setOnClickListener {
            hangmanViewModel.checkAnswer("y")
            updateAnswer()
            binding.yButton.isEnabled = false
        }
        binding.zButton.setOnClickListener {
            hangmanViewModel.checkAnswer("z")
            updateAnswer()
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