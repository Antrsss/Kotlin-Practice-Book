package com.example.ceatergb

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.create_color_button)?.setOnClickListener() {
            val redChannelText = findViewById<TextInputEditText>(R.id.red_channel)
                ?.text.toString().trim()
            val greenChannelText = findViewById<TextInputEditText>(R.id.green_channel)
                ?.text.toString().trim()
            val blueChannelText = findViewById<TextInputEditText>(R.id.blue_channel)
                ?.text.toString().trim()

            if (redChannelText.length == 2 && greenChannelText.length == 2 && blueChannelText.length == 2) {
                val colorDisplay = findViewById<TextView>(R.id.color_display)
                val color = "#" + redChannelText + greenChannelText + blueChannelText
                colorDisplay.setBackgroundColor(Color.parseColor(color))
            }
            else {
                Toast.makeText(this, "Please enter 2 digits for color!", Toast.LENGTH_LONG)
                    .apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
            }
        }
    }
}