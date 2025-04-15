// WelcomeActivity.kt
package com.example.mycalculator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mycalculator.MainActivity
import com.example.mycalculator.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val getStartedButton = findViewById<Button>(R.id.getStartedButton)
        getStartedButton.setOnClickListener {
            // Navigate to MainActivity when Get Started is clicked
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish() // Optional: closes the welcome screen so user can't go back
        }
    }
}