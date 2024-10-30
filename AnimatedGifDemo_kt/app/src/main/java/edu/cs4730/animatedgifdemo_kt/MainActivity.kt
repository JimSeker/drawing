package edu.cs4730.animatedgifdemo_kt

import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.ImageDecoder
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.cs4730.animatedgifdemo_kt.databinding.ActivityMainBinding
import java.io.IOException


/**
 * Simple demo to load and display animated gifs.
 * The second gifs has it color altered using color filter, (which works on any drawable image, not just animated)
 *
 *
 * Note, this works on API 28+, since no androidx libraries were used.
 */
class MainActivity : AppCompatActivity() {
    lateinit var decodedAnimation: Drawable
    lateinit var decodedAnimation2: Drawable
    lateinit var binding: ActivityMainBinding
    lateinit var colorFilter: ColorFilter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //get the animated gif, but can't be on the main thread, so
        Thread {
            try {
                decodedAnimation = ImageDecoder.decodeDrawable( // create ImageDecoder.Source object
                    ImageDecoder.createSource(resources, R.drawable.what)
                )
                decodedAnimation2 =
                    ImageDecoder.decodeDrawable( // create ImageDecoder.Source object
                        ImageDecoder.createSource(resources, R.drawable.rainbowkitty)
                    )

                // change color of gif, Multiplies the RGB channels by one color, and then adds a second color.

                // change color of gif, Multiplies the RGB channels by one color, and then adds a second color.
                // colorFilter = LightingColorFilter(Color.RED, Color.BLUE)
                // decodedAnimation.colorFilter = colorFilter

                //Change Color with PorterDuffColorFilter
                decodedAnimation2.colorFilter =
                    PorterDuffColorFilter(Color.RED, PorterDuff.Mode.DARKEN)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //can't change the imageview from a thread, so add to the main thread via a post.
            runOnUiThread { // set the drawable as image source of ImageView
                binding.imageView.setImageDrawable(decodedAnimation)
                //start it animated, not animatedImageDrawable is a child of Drawable, so casting.
                (decodedAnimation as AnimatedImageDrawable).start()
                //now the second image.
                binding.imageView2.setImageDrawable(decodedAnimation2)
                (decodedAnimation2 as AnimatedImageDrawable).start()
            }
        }.start()
    }
}
