/*
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
        val point: Button = findViewById(R.id.button_point)

        var str="0"
        var number_count = 0
        var result = 0.0
        var operation_flag = 0
        // 1 is addition, 2 is subtraction, 3 is multiplication, 4 is division,
        //5 is square root
        val textViewResult: TextView = findViewById(R.id.number_input)


        var operation = 0
        one.setOnClickListener {
            if(str=="0") str="1"
            else str+="1"
            textViewResult.text = str
        }
        two.setOnClickListener {
            if(str=="0") str="2"
            else str+="2"
            textViewResult.text = str
        }
        three.setOnClickListener {
            if(str=="0") str="3"
            else str+="3"
            textViewResult.text = str
        }
        four.setOnClickListener {
            if(str=="0") str="4"
            else str+="4"
            textViewResult.text = str
        }
        five.setOnClickListener {
            if(str=="0") str="5"
            else str+="5"
            textViewResult.text = str
        }
        six.setOnClickListener {
            if(str=="0") str="6"
            else str+="6"
            textViewResult.text = str
        }
        seven.setOnClickListener {
            if(str=="0") str="7"
            else str+="7"
            textViewResult.text = str
        }
        eight.setOnClickListener {
            if(str=="0") str="8"
            else str+="8"
            textViewResult.text = str
        }
        nine.setOnClickListener {
            if(str=="0") str="9"
            else str+="9"
            textViewResult.text = str
        }
        zero.setOnClickListener {
            if(str!="0")
            {
                str+="0"
                textViewResult.text = str
            }
        }
        point.setOnClickListener {
            if(str=="0") str="0."
            else str+="."
            textViewResult.text = str
        }
        equal.setOnClickListener {
            val userInput: EditText = findViewById(R.id.number_input)
            val stringInput = userInput.text.toString()
            if(stringInput.isNotEmpty())
            {
                var num=stringInput.toDouble()
                if(operation_flag == 1) {  //add condition
                    result += num
                    operation_flag = 0
                }
                if(operation_flag == 2) {  //sub condition
                    result -= num
                    operation_flag = 0
                }
                if(operation_flag == 3) {  //mul condition
                    result *= num
                    operation_flag = 0
                }
                if(operation_flag == 4) {  //div condition
                    if (num!=0.0){
                        result /= num
                        operation_flag = 0
                    }
                    else{
                        Toast.makeText(
                            this,
                            R.string.error_division,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                if(operation_flag == 5) {  //sqrt condition
                    Toast.makeText(
                        this,
                        R.string.error_sqrt_equal,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            textViewResult.text = "$result"
            str="0"
            result=0.0
        }
        //addition
        addition.setOnClickListener {
            val userInput: EditText = findViewById(R.id.number_input)
            val stringInput = userInput.text.toString()
            if(stringInput.isNotEmpty())
            {
                number_count++
                var num=stringInput.toDouble()
                result += num
                str="0"
                textViewResult.text = "$str"

            }
            else
            {
                Toast.makeText(
                    this,
                    R.string.error_missing,
                    Toast.LENGTH_SHORT
                ).show()
            }
            operation_flag = 1
        }

        subtraction.setOnClickListener {
            val userInput: EditText = findViewById(R.id.number_input)
            val stringInput = userInput.text.toString()
            if(stringInput.isNotEmpty())
            {
                number_count++
                var num=stringInput.toDouble()
                result -= num
                str="0"
                textViewResult.text = "$str"

            }
            else
            {
                Toast.makeText(
                    this,
                    R.string.error_missing,
                    Toast.LENGTH_SHORT
                ).show()
            }
            operation_flag = 2
        }

        multiplication.setOnClickListener {
            val userInput: EditText = findViewById(R.id.number_input)
            val stringInput = userInput.text.toString()
            if(stringInput.isNotEmpty())
            {
                number_count++
                var num=stringInput.toDouble()
                result *= num
                str="0"
                textViewResult.text = "$str"

            }
            else
            {
                Toast.makeText(
                    this,
                    R.string.error_missing,
                    Toast.LENGTH_SHORT
                ).show()
            }
            operation_flag = 3
        }

        division.setOnClickListener {
            val userInput: EditText = findViewById(R.id.number_input)
            val stringInput = userInput.text.toString()
            if(stringInput.isNotEmpty())
            {
                number_count++
                var num=stringInput.toDouble()
                if (num!=0.0) result /= num
                else
                {
                    Toast.makeText(
                        this,
                        R.string.error_division,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                str="0"
                textViewResult.text = "$str"
            }
            else
            {
                Toast.makeText(
                    this,
                    R.string.error_missing,
                    Toast.LENGTH_SHORT
                ).show()
            }
            operation_flag = 3
        }

        squareroot.setOnClickListener {
            val userInput: EditText = findViewById(R.id.number_input)
            val stringInput = userInput.text.toString()
            if(stringInput.isNotEmpty())
            {
                number_count++
                var num=stringInput.toDouble()
                result *= num
                str="0"
                textViewResult.text = "$str"

            }
            else
            {
                Toast.makeText(
                    this,
                    R.string.error_missing,
                    Toast.LENGTH_SHORT
                ).show()
            }
            operation_flag = 3
        }
    }
}*/
package com.example.simplearithmetic

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val equal: Button = findViewById(R.id.button_equal)
        val addition: Button = findViewById(R.id.button_addition)
        val subtraction: Button = findViewById(R.id.button_subtraction)
        val multiplication: Button = findViewById(R.id.button_multiplication)
        val division: Button = findViewById(R.id.button_division)
        val squareroot: Button = findViewById(R.id.button_square_root)

        var isNegative = false

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
        val point: Button = findViewById(R.id.button_point)

        val textViewResult: TextView = findViewById(R.id.number_input)

        var str = "0"
        var result = 0.0
        var operationFlag = 0
        var isNewOperation = true

        fun updateInput(digit: String) {
            if (isNewOperation) {
                str = ""
                isNewOperation = false
            }
            str += digit
            textViewResult.text = str
        }

        one.setOnClickListener { updateInput("1") }
        two.setOnClickListener { updateInput("2") }
        three.setOnClickListener { updateInput("3") }
        four.setOnClickListener { updateInput("4") }
        five.setOnClickListener { updateInput("5") }
        six.setOnClickListener { updateInput("6") }
        seven.setOnClickListener { updateInput("7") }
        eight.setOnClickListener { updateInput("8") }
        nine.setOnClickListener { updateInput("9") }
        zero.setOnClickListener { if (str != "0") updateInput("0") }
        point.setOnClickListener { if (!str.contains('.')) updateInput(".") }

        fun operate() {
            if (str.isNotEmpty()) {
                val num = str.toDouble()
                when (operationFlag) {
                    1 -> result += num
                    2 -> result -= num
                    3 -> result *= num
                    4 -> {
                        if (num != 0.0) result /= num else Toast.makeText(
                            this,
                            R.string.error_division,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    5 ->{
                        if (num >= 0.0) result = sqrt(num) else Toast.makeText(
                            this,
                            R.string.error_sqrt,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                textViewResult.text = result.toString()
                str = "0"
                isNewOperation = true
            } else {
                Toast.makeText(this, R.string.error_missing, Toast.LENGTH_SHORT).show()
            }
        }

        equal.setOnClickListener {
            operate()
            result = 0.0  // Reset result for the next operation
            operationFlag = 0 // Reset operation flag
        }

        addition.setOnClickListener {
            if (operationFlag == 0) {
                result = str.toDouble()
            }
            operationFlag = 1
            isNewOperation = true
        }

        subtraction.setOnClickListener {
            if (isNewOperation && !isNegative) {
                // Set the current number to be negative
                isNegative = true
                str = "-"
                textViewResult.text = str
                isNewOperation = false
            } else if (isNegative) {
                // If it's already set to negative, remove the negative sign
                isNegative = false
                str = str.removePrefix("-")
                textViewResult.text = str
            } else {
                // Continue with the subtraction operation
                if (operationFlag == 0) {
                    result = str.toDouble()
                }
                operationFlag = 2
                isNewOperation = true
            }
        }

        multiplication.setOnClickListener {
            if (operationFlag == 0) {
                result = str.toDouble()
            }
            operationFlag = 3
            isNewOperation = true
        }

        division.setOnClickListener {
            if (operationFlag == 0) {
                result = str.toDouble()
            }
            operationFlag = 4
            isNewOperation = true
        }

        squareroot.setOnClickListener {
            operationFlag = 5
            operate()
            result = 0.0  // Reset result for the next operation
            operationFlag = 0  // Reset operation flag
        }
    }
}

