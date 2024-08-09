package com.example.activityresults

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible

const val RAINBOW_COLOR_NAME = "RAINBOW_COLOR_NAME" //key to return it in intent
const val RAINBOW_COLOR = "RAINBOW_COLOR" //too
const val DEFAULT_COLOR = "#FFFFFF"

class MainActivity : AppCompatActivity() {
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.
        StartActivityForResult()) { activityResult ->
        val data = activityResult.data
            val backgroundColor = data?.getIntExtra(RAINBOW_COLOR, Color.parseColor(DEFAULT_COLOR))
                ?: Color.parseColor(DEFAULT_COLOR)
            val colorName = data?.getStringExtra(RAINBOW_COLOR_NAME) ?: ""
            val colorMessage = getString(R.string.color_chosen_message, colorName)

            val rainbowColor = findViewById<TextView>(R.id.rainbow_color)
            rainbowColor.setBackgroundColor(ContextCompat.getColor(this, backgroundColor))
            rainbowColor.text = colorMessage
            rainbowColor.isVisible = true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.submit_button)
            .setOnClickListener {
                startForResult.launch(Intent(this, RainbowColorPickerActivity::class.java))
            }
    }
}