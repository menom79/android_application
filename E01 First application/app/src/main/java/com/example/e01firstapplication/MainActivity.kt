package com.example.e01firstapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button1: Button = findViewById(R.id.button1)

        // Set OnClickListener
        button1.setOnClickListener {
            buttonClicked(it)
        }
    }

    // View class need to be used in parameter, even it is not used
    fun buttonClicked(view: View) {
        // activity_main layout has textView id in TextView element
        // find it and change text
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = resources.getString(R.string.button_clicked_txt)
    }
}