package com.example.simplearithmetic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get references to the EditText, Button, and TextView


        val equal: Button = findViewById(R.id.button_equal)
        val addition: Button = findViewById(R.id.button_addition)
        val subtraction: Button = findViewById(R.id.button_subtraction)
        val multiplication: Button = findViewById(R.id.button_multiplication)
        val division: Button = findViewById(R.id.button_division)
        val squareroot: Button = findViewById(R.id.button_square_root)

        val one: Button = findViewById(R.id.button_1)
        val two: Button = findViewById(R.id.button_2)
        val three: Button = findViewById(R.id.button_3)
        val four: Button = findViewById(R.id.button_4)
        val five: Button = findViewById(R.id.button_5)
        val six: Button = findViewById(R.id.button_6)
        val seven: Button = findViewById(R.id.button_7)
        val eight: Button = findViewById(R.id.button_8)
        val nine: Button = findViewById(R.id.button_9)
        val zero: Button = findViewById(R.id.button_0)

        var str="0"
        var number_count = 0
        var result = 0.0
        val textViewResult: TextView = findViewById(R.id.result_text)
        // Get a reference to the RadioGroup
        val radioGroup: RadioGroup = findViewById(R.id.operations)
        var operation = 0
        one.setOnClickListener {
            str+=1
        }
        //addition
        addition.setOnClickListener {
            val userInput: EditText = findViewById(R.id.number_input)
            val stringInput = userInput.text.toString()
            if(stringInput.isNotEmpty())
            {
                number_count++
                if ('.' in stringInput)
                {
                    var num=stringInput.toDouble()
                    result += num
                }
                else
                {
                    var num=stringInput.toInt()
                    result += num
                }
            }
            else
            {
                Toast.makeText(
                    this,
                    R.string.error_missing,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        subtraction.setOnClickListener {
            val userInput: EditText = findViewById(R.id.number_input)
            val stringInput = userInput.text.toString()
            if(stringInput.isNotEmpty())
            {
                number_count++
                if ('.' in stringInput)
                {
                    var num=stringInput.toDouble()
                    result -= num
                }
                else
                {
                    var num=stringInput.toInt()
                    result -= num
                }
            }
            else
            {
                Toast.makeText(
                    this,
                    R.string.error_missing,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        multiplication.setOnClickListener {
            val userInput: EditText = findViewById(R.id.number_input)
            val stringInput = userInput.text.toString()
            if(stringInput.isNotEmpty())
            {
                number_count++
                if ('.' in stringInput)
                {
                    var num=stringInput.toDouble()
                    result = num
                }
                else
                {
                    var num=stringInput.toInt()

                }
            }
            else
            {
                Toast.makeText(
                    this,
                    R.string.error_missing,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
/*
        // Attach an OnCheckedChangeListener to the RadioGroup
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Find the RadioButton that was selected
            val selectedOperation: RadioButton = findViewById(checkedId)
            // Do something with the selected RadioButton
            textViewResult.text = ""
            if (selectedOperation.text =="+")
            {operation =0 }
            if (selectedOperation.text =="-")
            {operation =1 }
            if (selectedOperation.text =="*")
            {operation =2 }
            if (selectedOperation.text =="/")
            {operation =3 }
        }
*/
        // Attach a click listener to the Button
        calbutton.setOnClickListener {
            // Get the user input from the EditText
            val userInput1 = num1.text.toString()
            val userInput2 = num2.text.toString()
            textViewResult.text = ""
            // Check if the input is not empty
            if (userInput1.isNotEmpty() && userInput2.isNotEmpty()) {
                //when either input is a decimal
                if('.' in userInput1 || '.' in userInput2)
                {
                    if (operation==0)
                    {
                        var output=userInput1.toDouble() + userInput2.toDouble()
                        textViewResult.text = "$output"
                    }
                    if (operation==1)
                    {
                        var output=userInput1.toDouble() - userInput2.toDouble()
                        textViewResult.text = "$output"
                    }
                    if (operation==2)
                    {
                        var output=userInput1.toDouble() * userInput2.toDouble()
                        textViewResult.text = "$output"
                    }
                    if (operation==3)
                    {
                        if(userInput2.toDouble()==0.0)
                        {
                            Toast.makeText(
                                this,
                                R.string.error_division,
                                Toast.LENGTH_SHORT
                            ).show()
                            textViewResult.text = "Invalid"
                        }
                        else
                        {
                            var output=userInput1.toDouble() / userInput2.toDouble()
                            textViewResult.text = "$output"
                        }
                    }
                }
                else
                {
                    if (operation==0)
                    {
                        var output=userInput1.toInt() + userInput2.toInt()
                        textViewResult.text = "$output"
                    }
                    if (operation==1)
                    {
                        var output=userInput1.toInt() - userInput2.toInt()
                        textViewResult.text = "$output"
                    }
                    if (operation==2)
                    {
                        var output=userInput1.toInt() * userInput2.toInt()
                        textViewResult.text = "$output"
                    }
                    if (operation==3)
                    {
                        if(userInput2.toInt()==0)
                        {
                            Toast.makeText(
                                this,
                                R.string.error_division,
                                Toast.LENGTH_SHORT
                            ).show()
                            textViewResult.text = "Invalid"
                        }
                        else
                        {
                            var output=userInput1.toInt() / userInput2.toInt()
                            textViewResult.text = "$output"
                        }
                    }
                }
            }
            else {
                // Handle empty input, e.g., show a message or update the TextView
                Toast.makeText(
                    this,
                    R.string.error_missing,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}