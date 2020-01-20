package edu.cs4730.animatedgifdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ImageDecoder;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;


/**
 * Simple demo to load and display animated gifs.
 * The second gifs has it color altered using color filter, (which works on any drawable image, not just animated)
 *
 * Note, this works on API 28+, since no androidx libraries were used.
 */

public class MainActivity extends AppCompatActivity {
    Drawable decodedAnimation, decodedAnimation2;
    ImageView iva, iva2;
    ColorFilter colorFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iva = findViewById(R.id.imageView);
        iva2 = findViewById(R.id.imageView2);

        //get the animated gif, but can't be on the main thread, so
        new Thread(new Runnable() {
            public void run() {
                try {
                    decodedAnimation = ImageDecoder.decodeDrawable(
                        // create ImageDecoder.Source object
                        ImageDecoder.createSource(getResources(), R.drawable.what));
                    decodedAnimation2 = ImageDecoder.decodeDrawable(
                        // create ImageDecoder.Source object
                        ImageDecoder.createSource(getResources(), R.drawable.rainbowkitty));
                    // change color of gif, Multiplies the RGB channels by one color, and then adds a second color.
                   // colorFilter = new LightingColorFilter(Color.RED, Color.BLUE);
                   // decodedAnimation.setColorFilter(colorFilter);

                    //Change Color with PorterDuffColorFilter
                    decodedAnimation2.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.DARKEN));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //can't change the imageview from a thread, so add to the main thread via a post.
                iva.post(new Runnable() {
                    public void run() {
                        // set the drawable as image source of ImageView
                        iva.setImageDrawable(decodedAnimation);
                        //start it animated, not animatedImageDrawable is a child of Drawable, so casting.
                        (( AnimatedImageDrawable) decodedAnimation).start();
                        //now the second image.
                        iva2.setImageDrawable(decodedAnimation2);
                        (( AnimatedImageDrawable) decodedAnimation2).start();
                    }
                });
            }
        }).start();

    }
}
