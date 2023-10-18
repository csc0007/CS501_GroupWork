package com.example.hangman

import androidx.annotation.StringRes

data class Question( val question: String,@StringRes val hintId: Int)