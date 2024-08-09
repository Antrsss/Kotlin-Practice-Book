package com.example.intentsintroduction

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
const val FULL_NAME_KEY = "FULL_NAME_KEY"

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

        findViewById<Button>(R.id.submit_button).setOnClickListener {
            val fullName = findViewById<EditText>(R.id.full_name).text.toString()

            if (fullName.isNotEmpty()) {
                //создание intent и передача fullName
                val welcomeIntent = Intent(this, WelcomeActivity::class.java)
                welcomeIntent.putExtra(FULL_NAME_KEY, fullName)
                startActivity(welcomeIntent)
            } else {
                Toast.makeText(this, getString(R.string.full_name_label), Toast.LENGTH_LONG).show()
            }
        }
    }
}