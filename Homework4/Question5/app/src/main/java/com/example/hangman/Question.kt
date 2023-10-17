package com.example.hangman

import androidx.annotation.StringRes

data class Question(@StringRes val questionId: Int,@StringRes val hintId: Int)