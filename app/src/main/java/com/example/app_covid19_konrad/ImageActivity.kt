
/*
package com.example.app_covid19_konrad

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
    }
}
 */
package com.example.app_covid19_konrad

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ImageActivity : AppCompatActivity() {

    private lateinit var virusImageView: ImageView
    private lateinit var catchMeTextView: TextView
    private var isVirusCaught = false
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        virusImageView = findViewById(R.id.virusImageView)
        catchMeTextView = findViewById(R.id.catchMeTextView)

        // Set initial position of the virus
        virusImageView.x = 0f
        virusImageView.y = 0f

        val scoreTextView: TextView = findViewById(R.id.scoreTextView)
        scoreTextView.text = "Score: $score"
        // Set touch listener for the virus image
        virusImageView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    // User touched the virus, they win!
                    isVirusCaught = true
                    catchMeTextView.text = "You caught the virus!"
                    score++
                    // Update the score text
                    scoreTextView.text = "Score: $score"
                    // Hide the virus image and show a message
                    virusImageView.visibility = View.GONE
                    catchMeTextView.visibility = View.VISIBLE

                    // Restart the game after a delay
                    Handler(Looper.getMainLooper()).postDelayed({
                        resetGame()
                    }, 1000)
                }
                return true
            }
        })

        // Start the virus moving
        startVirusMovement()
    }

    private fun startVirusMovement() {
        // Generate random coordinates for the virus to move to
        val maxX = resources.displayMetrics.widthPixels - virusImageView.width
        val maxY = resources.displayMetrics.heightPixels - virusImageView.height
        val targetX = (0..maxX).random().toFloat()
        val targetY = (0..maxY).random().toFloat()

        // Animate the virus to the target coordinates
        virusImageView.animate()
            .x(targetX)
            .y(targetY)
            .setDuration(1000)
            .withEndAction {
                // Virus reached the target, start moving again
                startVirusMovement()
            }
            .start()
    }

    private fun resetGame() {
        // Reset the virus position and visibility
        virusImageView.x = 0f
        virusImageView.y = 0f
        virusImageView.visibility = View.VISIBLE

        // Reset the catch message and flag
        catchMeTextView.text = "Catch Me!"
        isVirusCaught = false

        // Start the virus moving again
        startVirusMovement()
    }
}