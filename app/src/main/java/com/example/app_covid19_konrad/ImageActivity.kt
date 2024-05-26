package com.example.app_covid19_konrad

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        // Find the ImageView and set the image resource
        val imageView = findViewById<ImageView>(R.id.stats_button)
        imageView.setImageResource(R.drawable.coronavirus)
    }
}