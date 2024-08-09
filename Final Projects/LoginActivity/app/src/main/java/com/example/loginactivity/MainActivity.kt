package com.example.loginactivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

const val USERNAME = "USERNAME"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = getString(R.string.username)
        val password = getString(R.string.password)

        findViewById<Button>(R.id.login_button).setOnClickListener {
            val usernameField = findViewById<EditText>(R.id.username_field)
            val passwordField = findViewById<EditText>(R.id.password_field)
            val usernameFieldText = usernameField.text.trim().toString()
            val passwordFieldText = passwordField.text.trim().toString()

            if (usernameFieldText.isEmpty() || passwordFieldText.isEmpty()) {
                Toast.makeText(this, getString(R.string.empty_field_text), Toast.LENGTH_LONG)
                    .show()
            } else {
                if (usernameFieldText == username && passwordFieldText == password) {
                    callActivity(username)
                } else {
                    Toast.makeText(this, getString(R.string.wrong_filled_field), Toast.LENGTH_LONG)
                        .show()

                    usernameField.text.clear()
                    passwordField.text.clear()
                }
            }
        }
    }

    private fun callActivity(username: String) {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.putExtra(USERNAME, username)
        startActivity(intent)
    }
}