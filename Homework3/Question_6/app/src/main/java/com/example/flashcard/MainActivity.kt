package com.example.flashcard

//import android.app.Activity
//import android.content.Intent
//import android.view.View
//import android.widget.Button
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContract
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.flashcard.databinding.ActivityMainBinding


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val flashcardViewModel: FlashcardViewModel by viewModels()

    private val flashcardLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        //Here is the result handling
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            //Start FlashcardActivity
            val passwordEditText: EditText = findViewById(R.id.passwordEditText)
            val passwordText: String = passwordEditText.text.toString()
            val usernameEditText: EditText = findViewById(R.id.usernameEditText)
            val usernameText: String = usernameEditText.text.toString()

            if (flashcardViewModel.isTextEqualToUSER(usernameText)) {
                if (flashcardViewModel.isTextEqualToPASSWORD(passwordText)){
                    val intent = FlashcardActivity.newIntent(this, usernameText)
                    startActivity(intent)
                    //flashcardLauncher.launch(intent)
                }
                else{
                    Toast.makeText(this, "Password incorrect!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else {
                Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}