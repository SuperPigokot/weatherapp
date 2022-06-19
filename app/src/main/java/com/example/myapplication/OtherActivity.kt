package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class OtherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)
        val otherText = intent.getSerializableExtra("text") as String
        val textlabel: TextView = findViewById(R.id.other_text)
        textlabel.text = otherText
    }
}

//